package cab302.ControlPanel;

import cab302.database.billboard.BillboardData;
import cab302.database.schedule.ScheduleData;
import cab302.database.schedule.ScheduleInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

//sorry i just change public to firsly run the server to create tables
public class CalendarGUI extends JFrame implements ActionListener, Runnable, MouseListener {
    public static final int YEAR_DURATION = 10;
    public static final int WEEK_LENGTH = 7;
    public static final int DAY_HOUR = 24;

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

    ScheduleData dataSet;
    BillboardData boradData;

    JList data;

    Integer[][] ob;

    Object[] rowH;

    Object[] lblList;

    String[] recurSchedule;

    public CalendarGUI(ScheduleData dataSet, BillboardData boradData) {
        this.dataSet = dataSet;
        this.boradData = boradData;
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
    }

    public static void main(String[] args) {

        new CalendarGUI(new ScheduleData(), new BillboardData()) {
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void run() {

            }
        };


    }

    public void initializer() {

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
        recurSchedule = new String[dataSet.take().getSize()];

        data = new JList(dataSet.take());

        getDate();
        getMonth();
        getYear();

    }

    public void setTable() {
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
                CustomDialog dialog = new CustomDialog(chosenDate, String.valueOf(year), time, dataSet, boradData);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        super.windowOpened(e);
                        dataSet = new ScheduleData();
                        boradData = new BillboardData();
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        dataSet = new ScheduleData();
                        boradData = new BillboardData();
                        data = new JList(dataSet.take());
                        recurSchedule = new String[dataSet.take().getSize()];

                        setTable();
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

    public void setTableValue(JTable t) {
        int[][] tC = new int[t.getColumnCount()][2];
        int index = 0;
        int[][] d = new int[tC.length][2];
//        setRecurSchedule();
        setRecurring();


        while (index < data.getModel().getSize()) {
            int c = 0;
            String value = dataSet.findRow(index).getBoardTitle() + " - "
                    + dataSet.findRow(index).getHour()
                    + ":" + dataSet.findRow(index).getMinute();
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
                if (dataSet.findRow(index).getDate().equals(String.valueOf(n[0]))) {
                    d[c] = n;
                } else {
                    d[c][0] = -1;
                    d[c][1] = -1;
                }
                if (d[c][1] != -1 && d[c][0] != -1) {

//                    System.out.println(d[i][0]);
                    if (Integer.parseInt(dataSet.findRow(index).getHour()) < 24) {
                        t.setValueAt(value,
                                Integer.parseInt(dataSet.findRow(index).getHour()),
                                d[c][1]);
                    } else {
                        if (d[c][1] + 1 < 6) {
                            t.setValueAt(value,
                                    Integer.parseInt(dataSet.findRow(index).getHour()) - 24,
                                    d[c][1] + 1);
                        }
                    }
                    int min = 0;
                    int hrs = 0;
                    if (Integer.parseInt(dataSet.findRow(index).getDuMin()) > 0 &&
                            Integer.parseInt(dataSet.findRow(index).getDuMin()) < 60) {
                        hrs = 1;
                    } else if (Integer.parseInt(dataSet.findRow(index).getDuMin()) > 59) {
                        hrs = Integer.parseInt(dataSet.findRow(index).getDuMin()) / 60;
                        if (Integer.parseInt(dataSet.findRow(index).getDuMin()) % 60 > 0) {
                            min = Integer.parseInt(dataSet.findRow(index).getDuMin()) % 60;
                            if (min > 0) {
                                hrs++;
                            }
                        }
                    }
                    int duration = Integer.parseInt(dataSet.findRow(index).getDuHr()) + hrs;
                    if (duration > 0) {
                        for (int j = 1; j < duration; j++) {
                            if (Integer.parseInt(dataSet.findRow(index).getHour()) + j < 24) {
                                t.setValueAt(" ",
                                        Integer.parseInt(dataSet.findRow(index).getHour()) + j,
                                        d[c][1]);
                            } else {
                                if (d[c][1] + 1 < 6) {
                                    t.setValueAt(" ",
                                            Integer.parseInt(dataSet.findRow(index).getHour()) + j - 24,
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

    public void setRecurring() {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int index = 0;
        int[] dateRecur = new int[7];
        int tempData = dataSet.getModel().getSize();

        while (index < recurSchedule.length) {
            if (dataSet.findRow(index).getRecur() != null && !dataSet.findRow(index).getRecur().equals("")) {
                recurSchedule[index] = dataSet.findRow(index).toString();

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
                    ScheduleInfo temp = dataSet.findSchedule(recurSchedule[index].split(" ")[0], String.valueOf(dateRecur[in]),
                            String.valueOf(hrs));
                    if (temp.getMonth() == null && temp.getDate() == null && temp.getHour() == null && temp.getMinute() == null &&
                            temp.getRecur() == null) {
                        ScheduleInfo s = new ScheduleInfo(recurSchedule[index].split(" ")[0], recurSchedule[index].split(" ")[1],
                                recurSchedule[index].split(" ")[2],
                                String.valueOf(month), String.valueOf(dateRecur[in]), String.valueOf(hrs),
                                recurSchedule[index].split(" ")[6], recurSchedule[index].split(" ")[7],
                                recurSchedule[index].split(" ")[8], "");
                        dataSet.add(s);
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

    public void setRecurSchedule() {
        int index = 0;

        while (index < data.getModel().getSize()) {
            if (dataSet.findRow(index).getRecur() != null && !dataSet.findRow(index).getRecur().equals("")) {
                recurSchedule[index] = dataSet.findRow(index).toString();
            }
            index++;
        }
    }


    public void mouseClicked(MouseEvent e) {
        Object comp = e.getSource();

        if (comp != null) {
            pnlWeekly.setVisible(false);
            pnlWeekly.removeAll();
            weeklyPlanner(((JLabel) e.getSource()).getText());
            setSchedule();
            setTable();
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
}
