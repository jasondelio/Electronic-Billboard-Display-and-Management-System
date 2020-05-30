package cab302.ControlPanel;

import cab302.database.schedule.ScheduleInfo;
import cab302.server.Billboardserver.AcknowledgeReply;
import cab302.server.Billboardserver.FindScheduleReply;
import cab302.server.Billboardserver.ViewBillboardReply;
import cab302.server.Billboardserver.listBillboardReply;
import cab302.server.WillBeControlPanelAction.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.Properties;

//sorry i just change public to firsly run the server to create tables
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

//    ScheduleData dataSet;
//    BillboardData boradData;
    private String host;
    ListModel billboardsList;
    ListModel scheduleList;
    ListModel scheduleDupleList;

    //UserData data;
    Socket socket;
    JList data;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    Integer[][] ob;

    Object[] rowH;

    Object[] lblList;

    String[] recurSchedule;

    String sessionToken;

    public CalendarGUI(String sessiontoken) throws IOException, ClassNotFoundException {
//        this.dataSet = dataSet;
//        this.boradData = boradData;
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

        yearChooser.addActionListener(this);
        monthChooser.addActionListener(this);

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



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

        ob = new Integer[DAY_HOUR][WEEK_LENGTH + 1];
        rowH =  new Object[DAY_HOUR];
        lblList = new Object[WEEK_LENGTH];

        socketStart();
        listBillboardRequest lbr = new listBillboardRequest(sessionToken);
        oos.writeObject(lbr);
        oos.flush();

        Object transo = ois.readObject();
        if (transo instanceof listBillboardReply){
            listBillboardReply listreply = (listBillboardReply) transo;
            billboardsList = listreply.getListofBillboards();
        }
        socketStop();

        socketStart();
        ViewBillboardRequest vBbr = new ViewBillboardRequest(sessionToken);
        oos.writeObject(vBbr);
        oos.flush();

        Object trans = ois.readObject();
        if (trans instanceof ViewBillboardReply){
            ViewBillboardReply reply = (ViewBillboardReply) trans;
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

    public void setTable() throws IOException, ClassNotFoundException {
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
                }
                else if (col == 3) {
                    comp.setBackground(Color.LIGHT_GRAY);
                }
                else {
                    comp.setBackground(Color.white);
                }

                return comp;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // set list selection
        ListSelectionModel sm = table.getSelectionModel();
        sm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sm.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String[] title = table.getColumnName(table.getSelectedColumn())
                        .replace("<html><center>", "")
                        .split("<br>");
                chosenDate = title[0] + "/" + month + " (" + title[1] + ")";
                int time = table.getSelectedRow();

                CustomDialog dialog = null;
                try {
                    dialog = new CustomDialog(bg, chosenDate, String.valueOf(year), time, billboardsList, scheduleList, scheduleDupleList, sessionToken);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }


                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        super.windowOpened(e);
                        try {
                            socketStart();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        listBillboardRequest lbr = new listBillboardRequest(sessionToken);
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
                        if (transo instanceof listBillboardReply){
                            listBillboardReply listreply = (listBillboardReply) transo;
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
                        ViewBillboardRequest vBbr = new ViewBillboardRequest(sessionToken);
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
                        if (trans instanceof ViewBillboardReply){
                            ViewBillboardReply reply = (ViewBillboardReply) trans;
                            scheduleList = reply.getScheduledBillboard();
                            scheduleDupleList = reply.getDuplicatedModel();
                        }
                        try {
                            socketStop();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
//                        dataSet = new ScheduleData();
//                        boradData = new BillboardData();
//                        System.out.println(scheduleDupleList);
//                        System.out.println(scheduleList);
//                        System.out.println(billboardsList);
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        try {
                            socketStart();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        listBillboardRequest lbr = new listBillboardRequest(sessionToken);
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
                        if (transo instanceof listBillboardReply){
                            listBillboardReply listreply = (listBillboardReply) transo;
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
                        ViewBillboardRequest vBbr = new ViewBillboardRequest(sessionToken);
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
                        if (trans instanceof ViewBillboardReply){
                            ViewBillboardReply reply = (ViewBillboardReply) trans;
                            scheduleList = reply.getScheduledBillboard();
                            scheduleDupleList = reply.getDuplicatedModel();
                        }
                        try {
                            socketStop();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
//                        dataSet = new ScheduleData();
//                        boradData = new BillboardData();
                        data = new JList(scheduleDupleList);
                        recurSchedule = new String[scheduleDupleList.getSize()];
                        try {
                            setTable();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
//                        System.out.println(scheduleDupleList);
//                        System.out.println(scheduleList);
//                        System.out.println(billboardsList);

                        try {
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

        JList rowHeader = new JList(Rlm);

        rowHeader.setFixedCellWidth(50);

        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setCellRenderer(new TableModel(table));

        JList colHeader = new JList(lblList);
        colHeader.setFixedCellWidth(70);

        setTableValue(table);


        scrollWeekly.setViewportView(table);
        scrollWeekly.setRowHeaderView(rowHeader);
        scrollWeekly.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollWeekly.setPreferredSize(new Dimension(600, 450));

    }

    public void setTableValue(JTable t) throws IOException, ClassNotFoundException {
        int[][] tC = new int[t.getColumnCount()][2];
        int index = 0;
        int[][] d = new int[tC.length][2];
//        setRecurSchedule();
        setRecurring();


        while (index < data.getModel().getSize()) {
//            System.out.println(data.getModel().getSize());
//            System.out.println(index);
            socketStart();
            GetIndexSchedule GIS = new GetIndexSchedule(sessionToken,index);
            oos.writeObject(GIS);
            oos.flush();

            ScheduleInfo sche = new ScheduleInfo();
            Object transo = ois.readObject();
            if (transo instanceof FindScheduleReply){
                FindScheduleReply reply = (FindScheduleReply) transo;
                sche  = reply.getScheduleInfo();
//                System.out.println(sche);
            }
            socketStop();

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

//                String tempDate = dataSet.get(data.getModel().getElementAt(index)).getDate();
                if (sche.getDate().equals(String.valueOf(n[0]))) {
                    d[c] = n;
                } else {
                    d[c][0] = -1;
                    d[c][1] = -1;
                }
                if (d[c][1] != -1 && d[c][0] != -1) {

//                    System.out.println(d[i][0]);
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

    public void setRecurring() throws IOException, ClassNotFoundException {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int index = 0;
        int[] dateRecur = new int[7];
//        socketStart();
//        ViewBillboardRequest vbr = new ViewBillboardRequest(sessionToken,1);
//        oos.writeObject(vbr);
//        oos.flush();
//
//        Object transo = ois.readObject();
//        if (transo instanceof ViewBillboardReply){
//            ViewBillboardReply reply = (ViewBillboardReply) transo;
//            schemodel = reply.getSchModel();
//        }
//        socketStop();
//        int tempData = schemodel.getSize();

        while (index < recurSchedule.length) {
            socketStart();
            GetIndexSchedule gis = new GetIndexSchedule(sessionToken,index);
            oos.writeObject(gis);
            oos.flush();
            ScheduleInfo sche = new ScheduleInfo();
            Object transo = ois.readObject();
            if (transo instanceof FindScheduleReply){
                FindScheduleReply reply = (FindScheduleReply) transo;
                sche  = reply.getScheduleInfo();
            }
            socketStop();

            if (sche.getRecur() != null && !sche.getRecur().equals("")) {
                recurSchedule[index] = sche.toString();

                for (int i = 0; i < 7; i++) {
                    if (Integer.parseInt(recurSchedule[index].split(" ")[4]) + i > last) {
                        dateRecur[i] = Integer.parseInt(recurSchedule[index].split(" ")[4]) - last + i;
                    } else {
                        dateRecur[i] = Integer.parseInt(recurSchedule[index].split(" ")[4]) + i;
                    }
                }
                int re = Integer.parseInt(recurSchedule[index].split(" ")[9]);
                int times = (7 * 24 - Integer.parseInt(recurSchedule[index].split(" ")[5])) / re;

                for (int i = 1; i < times; i++) {
                    int in = 0;
                    int hrs = Integer.parseInt(recurSchedule[index].split(" ")[5]) + re * i;
                    in = hrs / 24;
                    if (hrs > 24) {
                        hrs = hrs % 24;
                    }
                    socketStart();
                    FindScheduleRequest fsr = new FindScheduleRequest(sessionToken, recurSchedule[index].split(" ")[0], String.valueOf(dateRecur[in]),
                            String.valueOf(hrs));
                    oos.writeObject(fsr);
                    oos.flush();
                    ScheduleInfo sch = new ScheduleInfo();
                    Object trans = ois.readObject();
                    if (trans instanceof FindScheduleReply){
                        FindScheduleReply reply = (FindScheduleReply) trans;
                        sch  = reply.getScheduleInfo();
                    }
                    socketStop();
                    ScheduleInfo temp = sch;
                    System.out.println("yey");
                    System.out.println(temp);
                    if (temp.getMonth() == null && temp.getDate() == null && temp.getHour() == null && temp.getMinute() == null &&
                            temp.getRecur() == null) {
                        socketStart();
                        ScheduleBillboardRequest scheduleBillboardRequest = new ScheduleBillboardRequest(recurSchedule[index].split(" ")[0], recurSchedule[index].split(" ")[1],
                                recurSchedule[index].split(" ")[2],String.valueOf(month), String.valueOf(dateRecur[in]), String.valueOf(hrs),
                                recurSchedule[index].split(" ")[6], sessionToken, recurSchedule[index].split(" ")[7],
                                recurSchedule[index].split(" ")[8], "");
                        oos.writeObject(scheduleBillboardRequest);
                        oos.flush();
                        ScheduleInfo s = new ScheduleInfo();
                        Object transoO = ois.readObject();
                        if (transoO instanceof AcknowledgeReply){
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


    public int getYear() {
        year = current.get(Calendar.YEAR);
        return year;
    }

    public int getMonth() {
        month = current.get(Calendar.MONTH) + 1;
        return month;
    }

    public int getDate() {
        date = current.get(Calendar.DATE);
        return date;
    }


    public void setYearChooser(Box x) {

        for(int i=year-YEAR_DURATION; i<=year+YEAR_DURATION; i++) yearBox.addElement(i);

        yearChooser.setModel(yearBox);

        yearChooser.setSelectedItem(year);

        x.add(yearChooser);
    }

    public void setMonthChooser(Box x){


        for(int i=0; i<12; i++) monthBox.addElement(monthNames[i]);

        monthChooser.setModel(monthBox);

        monthChooser.setSelectedItem(monthNames[month - 1]);

        x.add(monthChooser);
    }

    public void setLblOfDays() {
        pnlDayNames.setBackground(Color.lightGray);
        for(int i = 0; i < 7; i++){
            JLabel names = new JLabel(dayNames[i]);
            if(i == 0) names.setForeground(Color.red);
            if(i == 6) names.setForeground(Color.blue);

            pnlDayNames.add(names);
        }
    }

    public void setFirstDate() {
        Calendar tempCal = Calendar.getInstance();

        // Set the calendar as the first day of the month
        tempCal.set(year, month -1, 1);

        // Add empty items before printing the calendar
        for(int i = 1; i < tempCal.get(Calendar.DAY_OF_WEEK); i++) {
            pnlDate.add(new JLabel(" "));
        }
    }

    public void setDatesOfCalendar() {
        Calendar tempCal = Calendar.getInstance();
        lastDate = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for(int i = 1; i <= lastDate; i++) {
            Box box = Box.createVerticalBox();
            JLabel datelbl = new JLabel();
            datelbl.setText(String.valueOf(i));

            // Set the date to define it is on Sun or Sat
            tempCal.set(year, month -1, i);
            if(tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) datelbl.setForeground(Color.red);
            if(tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) datelbl.setForeground((Color.blue));

            datelbl.addMouseListener(this);
            box.add(datelbl);
            pnlDate.add(box);
        }
    }

    public void weeklyPlanner(String comp) {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int d = Integer.parseInt(comp);

        int ind = 0;

        for(int i = -3; i <= 3; i++) {

            int j;

            if(d + i <= 0) {
                j = last + i;
                tempCal.set(year, month - 2, j);
            }
            else if(d + i > last) {
                j = (d - last) + i;
                tempCal.set(year, month, j);
            } else {
                j = d + i;
                tempCal.set(year, month -1, j);
            }

            JLabel names = new JLabel(dayNames[tempCal.get(Calendar.DAY_OF_WEEK) - 1]);
            lblList[ind] = "<html><center>" + j + "<br>" + names.getText();

            ind++;
        }
    }

    public void emptyWeekly() {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int d = date;
        int ind = 0;

        for(int i = -3; i <= 3; i++) {
            int j;

            if(d + i <= 0) {
                j = last + i;
                tempCal.set(year, month - 2, j);
            }
            else if(d + i > last) {
                j = (d - last) + i;
                tempCal.set(year, month, j);
            } else {
                j = d + i;
                tempCal.set(year, month -1, j);
            }

            JLabel names = new JLabel(dayNames[tempCal.get(Calendar.DAY_OF_WEEK) - 1]);

            lblList[ind] = "<html><center>" + j + "<br>" + names.getText();

            ind++;
        }
    }

//    public void setRecurSchedule() throws IOException, ClassNotFoundException {
//        int index = 0;
//
//        while (index < data.getModel().getSize()) {
//            socketStart();
//            ViewBillboardRequest viewBillboardRequest = new ViewBillboardRequest(sessionToken,index);
//            oos.writeObject(viewBillboardRequest);
//            oos.flush();
//            ScheduleInfo sche = new ScheduleInfo();
//            Object transo = ois.readObject();
//            if (transo instanceof ViewBillboardReply){
//                ViewBillboardReply reply = (ViewBillboardReply) transo;
//                sche  = reply.getScheduleInfo();
//            }
//            socketStop();
//            if (sche.getRecur() != null && !sche.getRecur().equals("")) {
//                recurSchedule[index] = sche.toString();
//            }
//            index++;
//        }
//    }


    public void mouseClicked(MouseEvent e) {
        Object comp = e.getSource();

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

    public void setSchedule() {

        for (int i = 0; i < 24; i++) {
            rowH[i] = i + ":" + "00";
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }


    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj != null) {
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

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void run() {

    }
    public void socketStart() throws IOException{
        socket = new Socket(host,Integer.parseInt(port));
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
    }

    public void socketStop() throws IOException{
        ois.close();
        oos.close();
        socket.close();
    }
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
}
