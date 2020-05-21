package cab302.ControlPanel;

import cab302.database.schedule.ScheduleData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;

//sorry i just change public to firsly run the server to create tables
abstract class CalanderGUI extends JFrame implements ActionListener, Runnable, MouseListener {
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

    JList data;

    Integer[][] ob;

    Object[] rowH;

    Object[] lblList;

    public CalanderGUI(ScheduleData dataSet) {
        this.dataSet = dataSet;
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
            String[] title = table.getColumnName(table.getSelectedColumn())
                    .replace("<html><center>", "")
                    .split("<br>");
            chosenDate = title[0] + "/" + month + " (" +title[1] + ")";
            new CustomDialog(chosenDate, dataSet);
//            pnlWeekly.setVisible(false);
//            pnlWeekly.removeAll();
//            weeklyPlanner(((JLabel) e.getSource()).getText());
//            setSchedule();
//            setTable();
//            pnlWeekly.setVisible(true);
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
        int index = 0;
        int[][] d = new int[tC.length][2];
        for (int i = 0; i < t.getColumnCount(); i++) {
            String[] m = t.getColumnName(i)
                    .replace("<html><center>", "")
                    .replace(" ","")
                    .split("<br>");
            tC[i][0] = m[0];
            tC[i][1] = String.valueOf(i);
        }

        while(index < tempData.getModel().getSize()) {

            for (String[] n : tC) {
//                    System.out.println(tempData.getModel().getSize());
                if (tempData.getModel().getSize() > 1) {

                    for (int i = 0; i < d.length; i++) {

                        String tempVal = String.valueOf(tempData.getModel()
                                .getElementAt(0))
                                .split("/,")[index]
                                .replace("[", "")
                                .replace("]", "")
                                .replace("/", "")
                                .replace(" ", "");
//                            System.out.println(tempVal);
                        String[] tempElem = tempVal.split(",");

//                            System.out.println(n[0].equals(tempElem[2]));

                        if (n[0].equals(tempElem[2])) {
                            d[i][0] = Integer.parseInt(n[1]);
                            d[i][1] = i;
//                        System.out.println(d[i][1]);
                        } else {
                            d[i][1] = -1;
                            d[i][0] = -1;
                        }
//                        System.out.println(d[i][0]);
                    }

//                System.out.println(d[0][0]);
                } else if (tempData.getModel().getSize() == 1) {
                    String[] temp = String.valueOf(tempData.getModel())
                            .replace("[[", "")
                            .replace("]]", "")
                            .replace(" ", "")
                            .split(",");
                    if (n[0].equals(temp[2])) {
                        d[0][0] = Integer.parseInt(n[1]);
                        d[0][1] = 0;
                        break;
                    } else {
                        d[0][1] = -1;
                        d[0][0] = -1;
                    }
                }
//                    System.out.println(n[0]);
//            System.out.println(d[0][0]);
//        }

//        System.out.println(d[0][0]);

                for (int i = 0; i < d.length; i++) {
//                            System.out.println(ints[1]);
//                System.out.println(d[i][1]);
                    String[] tmpArray;
                    if (tempData.getModel().getSize() > 1 && d[i][1] != -1) {
                        System.out.println(d[i][1]);
                        String tempVal = String.valueOf(tempData.getModel()
                                .getElementAt(0))
                                .split("/,")[index]
                                .replace(" ", "")
                                .replace("/", "")
                                .replace("[", "")
                                .replace("]", "");


                        tmpArray = tempVal.split(",");

                    } else {
                        tmpArray = String.valueOf(tempData.getModel())
                                .replace("[[", "")
                                .replace("]]", "")
                                .replace(" ", "")
                                .replace("/", "")
                                .split(",");
                    }
                    String value = tmpArray[0] + " - " + tmpArray[3] + ":" + tmpArray[4];

//                System.out.println(d[i][1]);
                    if (d[i][1] != -1 && d[i][0] != -1) {
//                    System.out.println(d[i][0]);
                        t.setValueAt(value,
                                Integer.parseInt(tmpArray[3]),
                                d[i][0]);
                        if (Integer.parseInt(tmpArray[5]) > 0) {
                            for (int j = 1; j < Integer.parseInt(tmpArray[5]); j++) {
                                t.setValueAt(" ",
                                        Integer.parseInt(tmpArray[3]) + j,
                                        d[i][0]);
                            }
                        }
                    }
                }
//                index++;
            }
            index++;
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


    public void setSchedule() {

        for(int i = 0; i < 24; i ++) {
            rowH[i] = i + ":" + "00";
        }
    }


    public void mouseClicked(MouseEvent e) {
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


    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj != null) {
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
