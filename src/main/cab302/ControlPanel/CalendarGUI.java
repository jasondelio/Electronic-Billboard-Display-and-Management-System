package cab302.database;

import cab302.database.schedule.ScheduleInfo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import cab302.database.schedule.ScheduleData;
import cab302.database.schedule.ScheduleInfo;

public abstract class CalendarGUI extends JFrame implements ActionListener, Runnable, MouseListener {
    public static final int YEAR_DURATION = 10;


    private JPanel pnlTop;
    private JPanel pnlCntr;
    private JPanel pnlDate;
    private JPanel pnlDayNames;


    private JButton btnLast;
    private JButton btnNext;


    private JComboBox<Integer> yearChooser;
    private JComboBox<String> monthChooser;
    private DefaultComboBoxModel<Integer> yearBox;
    private DefaultComboBoxModel<String> monthBox;

    int year;
    int month;
    int date;
    int lastDate;
    String chosenDate;
    private Calendar current;
    ScheduleData data;

    protected String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    protected String[] monthNames = {"Jan", "Feb",
            "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public CalendarGUI() {
        initializer();

        pnlTop.add(btnLast);
        setMonthChooser();
        setYearChooser();
        pnlTop.add(btnNext);
        pnlTop.setBackground(new Color(100, 200, 200));

        add(pnlTop, "North");
        setLblOfDays();


        setFirstDate();
        setDatesOfCalendar();
        pnlCntr.add(pnlDate,"Center");
        add(pnlCntr, "Center");

        setVisible(true);

        yearChooser.addActionListener(this);
        monthChooser.addActionListener(this);
        btnLast.addActionListener(this);
        btnNext.addActionListener(this);

    }

    public void initializer() {

        current = Calendar.getInstance();

        yearChooser = new JComboBox<Integer>();
        yearBox = new DefaultComboBoxModel<Integer>();
        monthChooser = new JComboBox<String>();
        monthBox = new DefaultComboBoxModel<String>();

        btnLast = new JButton("<-");

        btnNext = new JButton("->");

        pnlTop = new JPanel();

        pnlCntr = new JPanel(new BorderLayout());

        pnlDayNames = new JPanel(new GridLayout(1, 7));

        pnlDate = new JPanel(new GridLayout(0, 7));

        setSize(600, 400);

        getDate();
        getMonth();
        getYear();

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

//    public int getTime(){
//
//    }

    public void setYearChooser(){

        for(int i=year-YEAR_DURATION; i<=year+YEAR_DURATION; i++) yearBox.addElement(i);

        yearChooser.setModel(yearBox);

        yearChooser.setSelectedItem(year);

        pnlTop.add(yearChooser);
    }

    public void setMonthChooser(){


        for(int i=0; i<12; i++) monthBox.addElement(monthNames[i]);

        monthChooser.setModel(monthBox);

        monthChooser.setSelectedItem(monthNames[month - 1]);

        pnlTop.add(monthChooser);
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
//            JLabel lbl = new JLabel();
            datelbl.setText(String.valueOf(i));

            // Set the date to define it is on Sun or Sat
            tempCal.set(year, month -1, i);
            if(tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) datelbl.setForeground(Color.red);
            if(tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) datelbl.setForeground((Color.blue));



            datelbl.addMouseListener(this);

//            lbl.setText("Bill");

            box.add(datelbl);

            //TODO: Not sure it will work well or not
            box.add(show(data.find(month-1, i)));

            pnlDate.add(box);
        }
    }


    public void mouseClicked(MouseEvent e) {
        Component comp = e.getComponent();

        if(comp != null) {
            chosenDate = ((JLabel)e.getSource()).getText();
//            JOptionPane.showMessageDialog(null, chosenDate, "Me", JOptionPane.ERROR_MESSAGE);
            CustomDialog d = new CustomDialog(chosenDate);

        }
    }




    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj != null) {
            int y = (int) yearChooser.getSelectedItem();
            int m = monthChooser.getSelectedIndex();

            if (obj == btnLast) {
                if (m == 0) {y--;
                    m = 11;}
                else {m--;}
                year = y;
                month = m + 1;
            }
            else if (obj == btnNext) {
                if (m == 11) {y++;
                    m = 0;}
                else {m++;}

                year = y;
                month = m + 1;
            }

            else if (obj == yearChooser || obj == monthChooser) {

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

    private JLabel show(ScheduleInfo s) {
        JLabel lbl = new JLabel();
        if (s != null) {
            lbl.setText(s.getBoardTitle() + ":" + s.getHour() + s.getMinute());
        }
        return lbl;
    }
//
//    @Override
//    public void run() {
//        nn();
//    }

    public static void main(String[] args) {

        new CalendarGUI() {
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
