package cab302.ControlPanel;

import cab302.database.schedule.ScheduleInfo;
import cab302.server.ApplicationsRequests.*;
import cab302.server.ReplyToApplications.AcknowledgeReply;
import cab302.server.ReplyToApplications.FindScheduleReply;
import cab302.server.ReplyToApplications.ListBillboardReply;
import cab302.server.ReplyToApplications.ViewScheduleListsReply;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.Properties;

/**
 * Initiates CalendarGUI interface for scheduling the billboard.
 */
public class CalendarGUI extends JFrame implements ActionListener, Runnable, MouseListener {
    public static final int YEAR_DURATION = 10;
    public static final int WEEK_LENGTH = 7;
    public static final int DAY_HOUR = 24;
    private static String port;

    private JFrame bg;
    private JPanel pnlTop;
    private JPanel pnlDate;
    private JPanel pnlDayNames;
    private JPanel pnlWeekly;
    private JPanel pnlMain;

    private JScrollPane scrollWeekly;

    String chosenDate;

    private JComboBox<Integer> yearChooser;
    private JComboBox<String> monthChooser;
    private DefaultComboBoxModel<Integer> yearBox;
    private DefaultComboBoxModel<String> monthBox;

    int year;
    int month;
    int date;
    int lastDate;
    private Calendar current;

    protected String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    protected String[] monthNames = {"Jan", "Feb",
            "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private String host;
    ListModel billboardsList;
    ListModel scheduleList;
    ListModel scheduleDupleList;

    Socket socket;
    JList data;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    Integer[][] tableItem;

    Integer[][] ob;

    Object[] rowH;

    Object[] lblList;

    String[] recurSchedule;

    String sessionToken;

    /**
     * The cunstructor for calling calendar GUI
     *
     * @param sessiontoken : the session token to use the server
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public CalendarGUI(String sessiontoken) throws IOException, ClassNotFoundException {
        getPropValues();
        sessionToken = sessiontoken;
        initializer();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        Box topBox = Box.createHorizontalBox();
        setMonthChooser(topBox);
        setYearChooser(topBox);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlTop.add(topBox);

        pnlTop.setBackground(new Color(100, 200, 200));

        pnlMain.add(pnlTop, gbc);


        setLblOfDays();
        setFirstDate();
        setDatesOfCalendar();
        emptyWeekly();
        setSchedule();
        setTable();

        gbc.ipadx = 0;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        pnlMain.add(pnlDayNames, gbc);

        gbc.gridwidth = 4;
        gbc.gridheight = 4;
        gbc.ipady = 0;
        gbc.gridx = 3;
        gbc.gridy = 2;
        pnlMain.add(scrollWeekly, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.ipadx = 0;
        gbc.gridx = 1;
        gbc.gridy = 3;
        pnlMain.add(pnlDate, gbc);

        bg.add(pnlMain);
        bg.pack();

        bg.setVisible(true);

        // Add action listeners for chooser boxes
        yearChooser.addActionListener(this);
        monthChooser.addActionListener(this);
    }

    /**
     * Get the data of props for server
     *
     * @throws IOException
     */
    private static void getPropValues() throws IOException {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("src/main/cab302/network.props");
            props.load(in);
            in.close();
            // get the property value and print it out
            port = props.getProperty("port");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    /**
     * Initializing every class members
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void initializer() throws IOException, ClassNotFoundException {

        current = Calendar.getInstance();

        bg = new JFrame();
        yearChooser = new JComboBox<>();
        yearBox = new DefaultComboBoxModel<>();
        monthChooser = new JComboBox<>();
        monthBox = new DefaultComboBoxModel<>();

        scrollWeekly = new JScrollPane();

        pnlMain = new JPanel(new GridBagLayout());

        pnlTop = new JPanel();

        pnlDayNames = new JPanel(new GridLayout(1, 7));

        pnlDate = new JPanel(new GridLayout(0, 7));

        pnlWeekly = new JPanel(new GridLayout(0, 8));

        bg.setSize(800, 400);

        tableItem = new Integer[7][2];

        ob = new Integer[DAY_HOUR][WEEK_LENGTH + 1];
        rowH = new Object[DAY_HOUR];
        lblList = new Object[WEEK_LENGTH];

        socketStart();
        ListBillboardRequest lbr = new ListBillboardRequest(sessionToken);
        oos.writeObject(lbr);
        oos.flush();

        Object transo = ois.readObject();
        if (transo instanceof ListBillboardReply){
            ListBillboardReply listreply = (ListBillboardReply) transo;
            billboardsList = listreply.getListofBillboards();
        }
        socketStop();

        socketStart();
        ViewScheduleListsRequest vBbr = new ViewScheduleListsRequest(sessionToken);
        oos.writeObject(vBbr);
        oos.flush();

        Object trans = ois.readObject();
        if (trans instanceof ViewScheduleListsReply){
            ViewScheduleListsReply reply = (ViewScheduleListsReply) trans;
            scheduleList = reply.getScheduledBillboard();
            scheduleDupleList = reply.getDuplicatedModel();
        }
        socketStop();

        recurSchedule = new String[scheduleDupleList.getSize()];
        data = new JList(scheduleDupleList);

        getDate();
        getMonth();
        getYear();

    }

    /**
     * Set table to show information of weekly schedules
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void setTable() throws IOException, ClassNotFoundException {

        // Set the list model for row header
        ListModel Rlm = new AbstractListModel() {
            @Override
            public int getSize() {
                return rowH.length;
            }

            @Override
            public Object getElementAt(int index) {
                return rowH[index];
            }
        };

        DefaultTableModel dm = new DefaultTableModel(Rlm.getSize(),lblList.length);
        dm.setColumnIdentifiers(lblList);
        JTable table = new JTable( dm ){
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                // get the current row
                Component comp = super.prepareRenderer(renderer, row, col);

                String type = (String) getModel().getValueAt(row, col);

                // even index, not selected
                if (type != null) {
                    comp.setBackground(Color.CYAN);
                } else if (col == 3) {
                    comp.setBackground(Color.LIGHT_GRAY);
                } else {
                    comp.setBackground(Color.white);
                }

                return comp;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // set list selection
        ListSelectionModel sm = table.getSelectionModel();
        sm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /*
          When cell is clicked by the user
         */
        sm.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Ignore when the cell is double clicked
                String[] title = table.getColumnName(table.getSelectedColumn())
                        .replace("<html><center>", "")
                        .split("<br>");
                int mon = 0;
                for (Integer[] i : tableItem) {
                    if (i[1].equals(Integer.valueOf(title[0]))) {
                        mon = i[0];
                        break;
                    }
                }

                chosenDate = title[0] + "/" + mon + " (" + title[1] + ")";
                int time = table.getSelectedRow();

                CustomDialog dialog = null;
                try {
                    // Call customizing dialog
                    dialog = new CustomDialog(bg, chosenDate, String.valueOf(year), time, billboardsList, scheduleList, scheduleDupleList, sessionToken);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }


                dialog.addWindowListener(new WindowAdapter() {
                    /**
                     * When the dialog is opened
                     *
                     * @param e : the window action even object
                     */
                    @Override
                    public void windowOpened(WindowEvent e) {
                        // Send the requests to get the changed data for displaying current statement on the dialog
                        super.windowOpened(e);
                        try {
                            socketStart();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        ListBillboardRequest lbr = new ListBillboardRequest(sessionToken);
                        try {
                            oos.writeObject(lbr);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            oos.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        Object transo = null;
                        try {
                            transo = ois.readObject();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        if (transo instanceof ListBillboardReply){
                            ListBillboardReply listreply = (ListBillboardReply) transo;
                            billboardsList = listreply.getListofBillboards();
                        }
                        try {
                            socketStop();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        try {
                            socketStart();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        ViewScheduleListsRequest vBbr = new ViewScheduleListsRequest(sessionToken);
                        try {
                            oos.writeObject(vBbr);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            oos.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        Object trans = null;
                        try {
                            trans = ois.readObject();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        if (trans instanceof ViewScheduleListsReply) {
                            ViewScheduleListsReply reply = (ViewScheduleListsReply) trans;
                            // Set the new data
                            scheduleList = reply.getScheduledBillboard();
                            scheduleDupleList = reply.getDuplicatedModel();
                        }
                        try {
                            socketStop();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    /**
                     * When the dialog is closed
                     *
                     * @param e : the window action even object
                     */
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        // Send the requests to get the changed data after closing
                        try {
                            socketStart();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        ListBillboardRequest lbr = new ListBillboardRequest(sessionToken);
                        try {
                            oos.writeObject(lbr);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            oos.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        Object transo = null;
                        try {
                            transo = ois.readObject();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        if (transo instanceof ListBillboardReply){
                            ListBillboardReply listreply = (ListBillboardReply) transo;
                            billboardsList = listreply.getListofBillboards();
                        }
                        try {
                            socketStop();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        try {
                            socketStart();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        ViewScheduleListsRequest vBbr = new ViewScheduleListsRequest(sessionToken);
                        try {
                            oos.writeObject(vBbr);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            oos.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        Object trans = null;
                        try {
                            trans = ois.readObject();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        if (trans instanceof ViewScheduleListsReply){
                            ViewScheduleListsReply reply = (ViewScheduleListsReply) trans;
                            scheduleList = reply.getScheduledBillboard();
                            scheduleDupleList = reply.getDuplicatedModel();
                        }
                        try {
                            socketStop();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        // Set the new data from the database
                        data = new JList(scheduleDupleList);
                        recurSchedule = new String[scheduleDupleList.getSize()];
                        try {
                            setTable(); // Set new table
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            // Call the Calendar again
                            CalendarGUI GUI = new CalendarGUI(sessionToken);
                            GUI.setVisible(true);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }

                    }

                });
            }
        });

        // Set the row header to show the times and the column header to show the dates and names of days
        JList rowHeader = new JList(Rlm);

        rowHeader.setFixedCellWidth(50);

        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setCellRenderer(new TableModel(table));

        JList colHeader = new JList(lblList);
        colHeader.setFixedCellWidth(70);

        setTableValue(table);

        // Set the scroll panel for weekly
        scrollWeekly.setViewportView(table);
        scrollWeekly.setRowHeaderView(rowHeader);
        scrollWeekly.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollWeekly.setPreferredSize(new Dimension(600, 450));

    }

    /**
     * Set the values for weekly table
     *
     * @param t : the table of weekly
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void setTableValue(JTable t) throws IOException, ClassNotFoundException {
        int[][] tC = new int[t.getColumnCount()][2];
        int index = 0;
        int[][] d = new int[tC.length][2];
        setRecurring();

        // While every schedules show on the table
        while (index < data.getModel().getSize()) {
            socketStart();
            GetIndexSchedule GIS = new GetIndexSchedule(sessionToken, index);
            oos.writeObject(GIS);
            oos.flush();

            ScheduleInfo sche = new ScheduleInfo();
            Object transo = ois.readObject();
            if (transo instanceof FindScheduleReply) {
                FindScheduleReply reply = (FindScheduleReply) transo;
                sche = reply.getScheduleInfo();
            }
            socketStop();

            // Set the value of strings to print data
            // Format) Title - Hours of Scheduled Time : Minutes of Scheduled Time
            int c = 0;
            String value = sche.getBoardTitle() + " - "
                    + sche.getHour()
                    + ":" + sche.getMinute();
            for (int i = 0; i < t.getColumnCount(); i++) {
                String[] m = t.getColumnName(i)
                        .replace("<html><center>", "")
                        .replace(" ", "")
                        .split("<br>");
                tC[i][0] = Integer.parseInt(m[0]);
                tC[i][1] = i;
            }


            for (int[] n : tC) {

                // Find schedules on selected week
                if (sche.getDate().equals(String.valueOf(n[0]))  && sche.getMonth().equals(String.valueOf(tableItem[n[1]][0]))) {
                    d[c] = n;
                } else {
                    d[c][0] = -1;
                    d[c][1] = -1;
                }
                if (d[c][1] != -1 && d[c][0] != -1) {

                    if (Integer.parseInt(sche.getHour()) < 24) {
                        t.setValueAt(value,
                                Integer.parseInt(sche.getHour()),
                                d[c][1]);
                    } else {
                        if (d[c][1] + 1 < 6) {
                            t.setValueAt(value,
                                    Integer.parseInt(sche.getHour()) - 24,
                                    d[c][1] + 1);
                        }
                    }

                    /*
                    Calculate the duration for hourly set as the table of weekly is designed for hourly displaying.
                    Eg. if the duration hour is 1 and duration minutes are 30, the number of cell must be 2 cells
                    */
                    int min = 0;
                    int hrs = 0;
                    if (Integer.parseInt(sche.getDuMin()) > 0 &&
                            Integer.parseInt(sche.getDuMin()) < 60) {
                        hrs = 1;
                    } else if (Integer.parseInt(sche.getDuMin()) > 59) {
                        hrs = Integer.parseInt(sche.getDuMin()) / 60;
                        if (Integer.parseInt(sche.getDuMin()) % 60 > 0) {
                            min = Integer.parseInt(sche.getDuMin()) % 60;
                            if (min > 0) {
                                hrs++;
                            }
                        }
                    }
                    // Set the table cells with duration of the given schedule
                    int duration = Integer.parseInt(sche.getDuHr()) + hrs;
                    if (duration > 0) {
                        for (int j = 1; j < duration; j++) {
                            if (Integer.parseInt(sche.getHour()) + j < 24) {
                                t.setValueAt(" ",
                                        Integer.parseInt(sche.getHour()) + j,
                                        d[c][1]);
                            } else {
                                if (d[c][1] + 1 < 6) {
                                    t.setValueAt(" ",
                                            Integer.parseInt(sche.getHour()) + j - 24,
                                            d[c][1] + 1);
                                }
                            }
                        }
                    }
                }
                c++;
            }
            index++;
        }
    }

    /**
     * Set recurrences on the database
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void setRecurring() throws IOException, ClassNotFoundException {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int index = 0;
        int[][] dateRecur = new int[7][2];

        /*
        After taking the data of schedules with recurring,
        if the recurrences are not stored on the database, save them until every schedules of recurrences are
        safely being as data on the database.
         */
        while (index < recurSchedule.length) {
            socketStart();
            GetIndexSchedule gis = new GetIndexSchedule(sessionToken,index);
            oos.writeObject(gis);
            oos.flush();
            ScheduleInfo sche = new ScheduleInfo();
            Object transo = ois.readObject();
            if (transo instanceof FindScheduleReply) {
                FindScheduleReply reply = (FindScheduleReply) transo;
                sche = reply.getScheduleInfo();
            }
            socketStop();

            if (sche.getRecur() != null && !sche.getRecur().equals("")) {
                recurSchedule[index] = sche.toString();
                // Calculate the date and month as we set the limitation of recurrence by 7 days
                for (int i = 0; i < 7; i++) {
                    if (Integer.parseInt(recurSchedule[index].split(" ")[4]) + i > last) {
                        dateRecur[i][0] = Integer.parseInt(recurSchedule[index].split(" ")[4]) - last + i;
                        dateRecur[i][1] = Integer.parseInt(recurSchedule[index].split(" ")[3]) + 1;
                    } else {
                        dateRecur[i][0] = Integer.parseInt(recurSchedule[index].split(" ")[4]) + i;
                        dateRecur[i][1] = Integer.parseInt(recurSchedule[index].split(" ")[3]);
                    }
                }

                // Set the time with frequency
                int re = Integer.parseInt(recurSchedule[index].split(" ")[9]);
                int times = (7 * 24 - Integer.parseInt(recurSchedule[index].split(" ")[5])) / re;

                for (int i = 1; i < times; i++) {
                    int in = 0;
                    int hrs = Integer.parseInt(recurSchedule[index].split(" ")[5]) + re * i;
                    in = hrs / 24;
                    if (hrs > 24) {
                        hrs = hrs % 24;
                    }

                    // Set the server to find the schedule with recurrence
                    socketStart();
                    FindScheduleRequest fsr = new FindScheduleRequest(sessionToken, recurSchedule[index].split(" ")[0], String.valueOf(dateRecur[in][1]), String.valueOf(dateRecur[in][0]),
                            String.valueOf(hrs), recurSchedule[index].split(" ")[6]);
                    oos.writeObject(fsr);
                    oos.flush();
                    ScheduleInfo sch = new ScheduleInfo();
                    Object trans = ois.readObject();
                    if (trans instanceof FindScheduleReply){
                        FindScheduleReply reply = (FindScheduleReply) trans;
                        sch = reply.getScheduleInfo();
                    }
                    socketStop();
                    ScheduleInfo temp = sch;
                    // Check the data is enough or not
                    if (temp.getMonth() == null && temp.getDate() == null && temp.getHour() == null && temp.getMinute() == null &&
                            temp.getRecur() == null) {
                        socketStart();
                        // Send the request to store the recurrences
                        RecurScheduleBillboardRequest recurScheduleBillboardRequest = new RecurScheduleBillboardRequest(recurSchedule[index].split(" ")[0], recurSchedule[index].split(" ")[1],
                                recurSchedule[index].split(" ")[2],String.valueOf(dateRecur[in][1]), String.valueOf(dateRecur[in][0]), String.valueOf(hrs),
                                recurSchedule[index].split(" ")[6], sessionToken, recurSchedule[index].split(" ")[7],
                                recurSchedule[index].split(" ")[8], "");
                        oos.writeObject(recurScheduleBillboardRequest);
                        oos.flush();
                        ScheduleInfo s = new ScheduleInfo();
                        Object transoO = ois.readObject();
                        if (transoO instanceof AcknowledgeReply) {
                            AcknowledgeReply reply = (AcknowledgeReply) transoO;
                            System.out.println(reply.getAcknowledgement());
                        }
                        socketStop();
                    }
                }
            }
            index++;
        }

    }

    /**
     * Get current year
     *
     * @return year
     */
    public int getYear() {
        year = current.get(Calendar.YEAR);
        return year;
    }

    /**
     * Get current month
     *
     * @return month
     */
    public int getMonth() {
        month = current.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * Get current date
     *
     * @return date
     */
    public int getDate() {
        date = current.get(Calendar.DATE);
        return date;
    }

    /**
     * Set year chooser combo box
     *
     * @param x : the box for year chooser on frame
     */
    public void setYearChooser(Box x) {

        for (int i = year - YEAR_DURATION; i <= year + YEAR_DURATION; i++) yearBox.addElement(i);

        yearChooser.setModel(yearBox);

        yearChooser.setSelectedItem(year);

        x.add(yearChooser);
    }

    /**
     * Set month chooser combo box
     *
     * @param x : the box of month chooser on frame
     */
    public void setMonthChooser(Box x) {

        for (int i = 0; i < 12; i++) monthBox.addElement(monthNames[i]);

        monthChooser.setModel(monthBox);

        monthChooser.setSelectedItem(monthNames[month - 1]);

        x.add(monthChooser);
    }

    /**
     * Set the lables of dates for monthly calendar
     */
    public void setLblOfDays() {
        pnlDayNames.setBackground(Color.lightGray);
        for (int i = 0; i < 7; i++) {
            JLabel names = new JLabel(dayNames[i]);
            if (i == 0) names.setForeground(Color.red); // Set the colour to red if the date is Sunday
            if (i == 6) names.setForeground(Color.blue); // Set the colour to blue if the date is Saturday

            pnlDayNames.add(names);
        }
    }

    /**
     * set the first date of monthly calendar to show every each month on selected year
     */
    public void setFirstDate() {
        Calendar tempCal = Calendar.getInstance();

        // Set the calendar as the first day of the month
        tempCal.set(year, month - 1, 1);

        // Add empty items before printing the calendar
        for (int i = 1; i < tempCal.get(Calendar.DAY_OF_WEEK); i++) {
            pnlDate.add(new JLabel(" "));
        }
    }

    /**
     * Make the monthly calendar
     */
    public void setDatesOfCalendar() {
        Calendar tempCal = Calendar.getInstance();
        lastDate = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= lastDate; i++) {
            Box box = Box.createVerticalBox();
            JLabel datelbl = new JLabel();
            datelbl.setText(String.valueOf(i));

            // Set the date to define it is on Sun or Sat
            tempCal.set(year, month - 1, i);
            // Set the colour to red if the date is Sunday
            if (tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) datelbl.setForeground(Color.red);
            // Set the colour to blue if the date is Saturday
            if (tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) datelbl.setForeground((Color.blue));

            datelbl.addMouseListener(this);
            box.add(datelbl);
            pnlDate.add(box);
        }
    }

    /**
     * Set the weekly after taking user's chosen date from monthly calendar
     *
     * @param comp : the component which is the user selection
     */
    public void weeklyPlanner(String comp) {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int d = Integer.parseInt(comp);
        int ind = 0;

        // Set the table is 7 days
        for (int i = -3; i <= 3; i++) {

            int j;
            int m;

            /*
            if the changed date is being lesser than the end of month, change to last month and date
            if the changed date is being bigger than the end of month, change the month to next and date
            Otherwise, keep on same month and change date
             */
            if (d + i <= 0) {
                j = last + i;
                m = month - 2;
                tempCal.set(year, m, j);

            } else if (d + i > last) {
                j = (d - last) + i;
                m = month;
                tempCal.set(year, m, j);
            } else {
                j = d + i;
                m = month - 1;
                tempCal.set(year, m, j);
            }

            // Save the information of date and month of weekly
            tableItem[ind][0] = m + 1;
            tableItem[ind][1] = j;

            JLabel names = new JLabel(dayNames[tempCal.get(Calendar.DAY_OF_WEEK) - 1]);
            lblList[ind] = "<html><center>" + j + "<br>" + names.getText();

            ind++;
        }
    }

    /**
     * Set empty weekly labels
     */
    public void emptyWeekly() {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int d = date;
        int ind = 0;

        // Set the table is 7 days
        for (int i = -3; i <= 3; i++) {

            int j;
            int m;

            /*
            if the changed date is being lesser than the end of month, change to last month and date
            if the changed date is being bigger than the end of month, change the month to next and date
            Otherwise, keep on same month and change date
             */
            if (d + i <= 0) {
                j = last + i;
                m = month - 2;
                tempCal.set(year, m, j);

            } else if (d + i > last) {
                j = (d - last) + i;
                m = month;
                tempCal.set(year, m, j);
            } else {
                j = d + i;
                m = month - 1;
                tempCal.set(year, m, j);
            }

            // Save the information of date and month of weekly
            tableItem[ind][0] = m + 1;
            tableItem[ind][1] = j;
            JLabel names = new JLabel(dayNames[tempCal.get(Calendar.DAY_OF_WEEK) - 1]);

            lblList[ind] = "<html><center>" + j + "<br>" + names.getText();

            ind++;
        }
    }

    /**
     * The performances for mouse clicking
     *
     * @param e : the mouse clicked action object
     */
    public void mouseClicked(MouseEvent e) {
        Object comp = e.getSource();
        /*
        When the user clicks the label of monthly calendar,
        the weekly table will be changed by setting the chosen date is on the middle of week
         */
        if (comp != null) {
            pnlWeekly.setVisible(false);
            pnlWeekly.removeAll();
            weeklyPlanner(((JLabel) e.getSource()).getText());
            setSchedule();
            try {
                setTable();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            pnlWeekly.setVisible(true);
        }
    }

    /**
     * Set the schedule text on table
     */
    public void setSchedule() {

        for (int i = 0; i < 24; i++) {
            rowH[i] = i + ":" + "00";
        }
    }

    /**
     * Set the performances for user actions
     *
     * @param e : the event action object
     */
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        /*
        When the user changes month or year chooser combo box,
        the month or year will be changed and set the monthly calendar by tracking changed month and year data
         */
        if (obj != null) {
            //
            if (obj == yearChooser || obj == monthChooser) {

                year = (int) yearChooser.getSelectedItem();
                month = monthChooser.getSelectedIndex() + 1;
            }
        }
        pnlDate.setVisible(false);
        pnlDate.removeAll();
        setFirstDate();
        setDatesOfCalendar();
        pnlDate.setVisible(true);

    }

    /**
     * Set to start the socket for server
     *
     * @throws IOException
     */
    public void socketStart() throws IOException {
        socket = new Socket(host, Integer.parseInt(port));
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
    }

    /**
     * Stop the socket
     *
     * @throws IOException
     */
    public void socketStop() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }

    /*
    The methods that are called by IntelliJ to use the mouse clicked event.
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void run() {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}
}
