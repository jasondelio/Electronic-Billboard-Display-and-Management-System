package cab302.database;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;


import cab302.database.schedule.ScheduleData;
import cab302.database.schedule.ScheduleInfo;

public abstract class CalanderGUI extends JFrame implements ActionListener, Runnable, MouseListener {
    public static final int YEAR_DURATION = 10;
    public static final int WEEK_LENGTH = 7;
    public static final int DAY_HOUR = 24;

    private JFrame bg;
    private JPanel pnlTop;
    private JPanel pnlCntr;
    private JPanel pnlDate;
    private JPanel pnlDayNames;
    private JPanel pnlWeekly;
    private JPanel pnlNames;
    private JPanel pnlTime;
    private JPanel pnlMain;

    private JTable table;

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

    JList data;

//    String[] t1 = {"Test", "2020", "5", "19", "10","00", "1"};
//
//    String[] t2 = {"Test", "2020", "5", "20", "13","30","3"};

//    String[][] data;

    Integer[][] ob;

    Object[] rowH;

    Object[] lblList;

    public CalanderGUI(ScheduleData dataSet) {
        this.dataSet = dataSet;
        initializer();

//        dataSet = new DefaultListModel();
//
//        dataSet.addElement(t1);
//        dataSet.addElement(t2);
//
//
//        data= new JList(dataSet);

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
//        pnlMain.add(pnlWeekly, gbc);
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

//    public JList<String> test(String[] i) {
//        JList<String> t = new JList<String>();
//
//        for(String s: i) {
//            t.add;
//        }
//        return t;
//    }
//
//    public ListModel tt() {
//        return dataSet;
//    }

    public void initializer() {

        current = Calendar.getInstance();

        bg = new JFrame();
        yearChooser = new JComboBox<Integer>();
        yearBox = new DefaultComboBoxModel<Integer>();
        monthChooser = new JComboBox<String>();
        monthBox = new DefaultComboBoxModel<String>();

        table = new JTable();

        scrollWeekly = new JScrollPane();

        pnlMain = new JPanel(new GridBagLayout());

        pnlTop = new JPanel();

        pnlTime = new JPanel();

        pnlNames = new JPanel();

        pnlCntr = new JPanel(new BorderLayout());

        pnlDayNames = new JPanel(new GridLayout(1, 7));

        pnlDate = new JPanel(new GridLayout(0, 7));

        pnlWeekly = new JPanel(new GridLayout(0, 8));

        bg.setSize(800, 400);

        ob = new Integer[DAY_HOUR][WEEK_LENGTH + 1];
        rowH =  new Object[DAY_HOUR];
        lblList = new Object[WEEK_LENGTH];

        data = new JList(dataSet.getModel());

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
//                String otherType = (String) getModel().getValueAt(row, 3);
//                Integer du = (Integer) ob[row][col];
////
//                if(otherType == null) {
//                    comp.setBackground(Color.GRAY);
//                }

                // even index, not selected
                if (type != null) {
                    comp.setBackground(Color.CYAN);
//                    System.out.println(type);
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
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // set list selection
        ListSelectionModel sm = table.getSelectionModel();
        sm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sm.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String[] title =table.getColumnName(table.getSelectedColumn())
                        .replace("<html><center>", "")
                        .split("<br>");
                chosenDate = title[0] + "/" + month + " (" +title[1] + ")";

                CustomDialog d = new CustomDialog(chosenDate);
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
        JList tempData = data;

        String[][] tC = new String[t.getColumnCount()][2];
        String[] tR = new String[t.getRowCount()];
        boolean find = false;
        int c = 0;
        int[][] d = new int[tC.length][2];
        for(int i = 0; i < t.getColumnCount(); i++) {
            String[] m = t.getColumnName(i).replace("<html><center>", "").split("<br>");
            tC[i][0] = m[0];
            tC[i][1] = String.valueOf(i);

        }

        while(c < tempData.getModel().getSize()) {
            for (int i = 0; i < tempData.getModel().getSize(); i++) {
                for (String[] n : tC) {
                    String[] tempElem = (String[]) tempData.getModel().getElementAt(i);
                    if (n[0].equals(tempElem[3])) {
                        d[i][0] = Integer.parseInt(n[1]);
                        d[i][1] = i;
                        find = true;
                    }
                }

                if(!find) {d[i][0] = -1; d[i][1] = -1;}
            }

            if(find && d[c][0] != -1 && d[c][1] != -1) {
                String[] tmpArray = (String[]) tempData.getModel().getElementAt(d[c][1]);
                String value = tmpArray[0] + " - " + tmpArray[4] + ":" + tmpArray[5];

                t.setValueAt(value,
                        Integer.parseInt(tmpArray[4]),
                        d[c][0]);
                if(Integer.parseInt(tmpArray[6]) > 0) {
                    for(int i = 1; i < Integer.parseInt(tmpArray[6]); i++) {
                        t.setValueAt(" ",
                                Integer.parseInt(tmpArray[4]) + i,
                                d[c][0]);
                    }
                }

                c++;
                find = false;
            } else {
                c++;
            }
        }

    }

    public int getYear(){
        year = current.get(Calendar.YEAR);
        return year;
    }

    public int getMonth(){
        month = current.get(Calendar.MONTH)+1;
        return month;
    }

    public int getDate(){
        date = current.get(Calendar.DATE);
        return date;
    }


    public void setYearChooser(Box x){

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


            //TODO: Not sure it will work well or not
//            box.add(show(data.find(month-1, i)));

            pnlDate.add(box);
        }
    }

    public void weeklyPlanner(String comp) {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int d = Integer.parseInt(comp);

//        pnlWeekly.add(new JLabel(" "));

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

//            pnlWeekly.add(box);
        }
    }

    public void emptyWeekly() {
        Calendar tempCal = Calendar.getInstance();
        int last = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int d = date;

//        pnlWeekly.add(new JLabel(" "));

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

//            pnlWeekly.add(box);
        }
    }


    public void setSchedule() {

        for(int i = 0; i < 24; i ++) {
            rowH[i] = (int)(i) + ":" + "00";
        }
    }


    public void mouseClicked(MouseEvent e) {
//        Component comp = e.getComponent();

        Object comp = e.getSource();

        if(comp != null) {
            pnlWeekly.setVisible(false);
            pnlWeekly.removeAll();
            weeklyPlanner(((JLabel) e.getSource()).getText());
            setSchedule();
            setTable();
            pnlWeekly.setVisible(true);
        }
    }


    /*
    Actions
     */
//    private JLabel show(ScheduleInfo s) {
//        JLabel lbl = new JLabel();
//        if (s != null) {
//            lbl.setText(s.getBoardTitle() + ":" + s.getHour() + s.getMinute());
//        }
//        return lbl;
//    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj != null) {
            int y = (int) yearChooser.getSelectedItem();
            int m = monthChooser.getSelectedIndex();

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
//
//    @Override
//    public void run() {
//        nn();
//    }

    public static void main(String[] args) {

        new CalanderGUI(new ScheduleData()) {
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

}
