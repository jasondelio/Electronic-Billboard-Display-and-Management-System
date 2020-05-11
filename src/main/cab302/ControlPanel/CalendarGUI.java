

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public abstract class CalendarGUI extends JFrame implements ActionListener, Runnable {
    public static final int YEAR_DURATION = 10;


    private JPanel pnlTop;
    private JPanel pnlCntr;
    private JPanel pnlDate;
    private JPanel pnlDayNames;


    private JButton btnLast;
    private JButton btnNext;

    private JLabel emptyLbl;

//    private JLabel year;
//    private JLabel month;

    private JComboBox<Integer> yearChooser;
    private JComboBox<String> monthChooser;
    private DefaultComboBoxModel<Integer> yearBox;
    private DefaultComboBoxModel<String> monthBox;

    int year, month, date, lastDate;
    private Calendar current;

    protected String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    protected String[] monthNames = {"Jan", "Feb",
            "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public void Calendar() {
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

        emptyLbl = new JLabel(" ");

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
        pnlCntr.add(pnlDayNames,"North");
    }

    public void setFirstDate() {
        Calendar tempCal = Calendar.getInstance();

        // Set the calendar as the first day of the month
        tempCal.set(year, month -1, 1);

        // Add empty items before printing the calendar
        for(int i = 1; i < tempCal.get(Calendar.DAY_OF_WEEK); i++) pnlDate.add(emptyLbl);
    }

    public void setDatesOfCalendar() {
        Calendar tempCal = Calendar.getInstance();
        lastDate = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for(int i = 1; i < lastDate; i++) {

            JLabel datelbl = new JLabel();
            datelbl.setText(String.valueOf(i));
            // Set the date to define it is on Sun or Sat
            tempCal.set(year, month -1, i);
            if(tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) datelbl.setForeground(Color.red);
            if(tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) datelbl.setForeground((Color.blue));

            pnlDate.add(datelbl);
        }
    }


    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj != null) {
            int y = (Integer) yearChooser.getSelectedItem();
            int m = monthChooser.getSelectedIndex();

            if (obj == btnLast) {
                if (m == 0) y--;
                m = 11;
                if (m > 0) m--;
            }
            if (obj == btnNext) {
                if (m == 11) y++;
                m = 0;
                if (m < 11) m++;
            }

            if (obj == yearChooser || obj == monthChooser) {

                year = (Integer) yearChooser.getSelectedItem();
                month = monthChooser.getSelectedIndex() + 1;
            }

            pnlDate.setVisible(false);
            pnlDate.removeAll();
            setFirstDate();
            setDatesOfCalendar();
            pnlDate.setVisible(true);
        }

    }


}
