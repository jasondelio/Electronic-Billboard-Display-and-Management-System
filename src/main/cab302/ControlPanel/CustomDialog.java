package cab302.database;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cab302.database.schedule.ScheduleData;
import cab302.database.schedule.ScheduleInfo;

public class CustomDialog extends JDialog implements ActionListener {

    private JButton save;
    private JButton edit;
    private JButton delete;
    private JButton close;
    private JButton newbtn;
    private JPanel pnl;
    private JScrollPane pnlList;
    private JFrame bg;


    private JTextField namebox;
    private JTextField hourbox;
    private JTextField minbox;
    private JTextField dubox;
    private JTextField creatorbox;

    private JLabel lblName;
    private JLabel lblselectedDate;
    private JLabel lblTime;
    private JLabel lblHour;
    private JLabel lblMin;
    private JLabel lblduration;
    private JLabel lblCreator;

    private String name;
    private Integer scheduledMonth;
    private Integer scheduledDate;
    private Integer scheduledHour;
    private Integer scheduledMin;

    private JList nameList;

    ScheduleData data;

    String month;
    String date;

    Calendar cal;

    JList datalst;


    public CustomDialog(String info) {

        cal = Calendar.getInstance();
//        getMonth();
//        getDate();


        bg = new JFrame();
        pnl = new JPanel();
        setTitle("Scheduling");
        pnl.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        //TODO: Change this list to import the data from SQL

//        dataSet = new DefaultListModel();
//
//        dataSet.addElement(test[0]);
//        dataSet.addElement(test[1]);

        datalst = new JList(data.getModel());

        setDisplay(info);

        gbc.gridwidth = 5;
        gbc.gridheight = 5;
        gbc.gridx = 1;
        gbc.gridy = 1;

        pnl.add(pnlList, gbc);

        // name
        lblName = new JLabel("Billboard name");
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 7;

        pnl.add(lblName, gbc);

        namebox = new JTextField(20);
        gbc.gridwidth = 3;
        gbc.gridx = 2;
        gbc.gridy = 7;

        pnl.add(namebox, gbc);

        // Creator
        lblCreator = new JLabel("Creator");
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 8;

        pnl.add(lblCreator, gbc);

        creatorbox = new JTextField(20);
        gbc.gridwidth = 3;
        gbc.gridx = 2;
        gbc.gridy = 8;

        pnl.add(creatorbox, gbc);
        // time

        lblTime = new JLabel("Time");
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 9;

        pnl.add(lblTime, gbc);

        hourbox = new JTextField(5);
        gbc.gridx = 2;
        gbc.gridy = 9;

        pnl.add(hourbox, gbc);

        lblHour = new JLabel("hr");
        gbc.gridx = 3;
        gbc.gridy = 9;

        pnl.add(lblHour, gbc);

        minbox = new JTextField(5);
        gbc.gridx = 4;
        gbc.gridy = 9;

        pnl.add(minbox, gbc);

        lblMin = new JLabel("min");
        gbc.gridx = 5;
        gbc.gridy = 9;

        pnl.add(lblMin, gbc);

        // Duration

        lblduration = new JLabel("Duration");
        gbc.gridx = 1;
        gbc.gridy = 10;

        pnl.add(lblduration, gbc);

        dubox = new JTextField(5);
        gbc.gridx = 2;
        gbc.gridy = 10;

        pnl.add(dubox, gbc);


        // Buttons
        newbtn = new JButton("New");
//        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 11;

        pnl.add(newbtn, gbc);

        save = new JButton("Save");
//        gbc.gridwidth = 1;
        gbc.gridx = 3;
        gbc.gridy = 11;

        pnl.add(save, gbc);

        delete = new JButton("Delete");
//        gbc.gridwidth = 2;
        gbc.gridx = 4;
        gbc.gridy = 12;

        pnl.add(delete, gbc);

        close = new JButton("Back to calendar");
        gbc.gridx = 2;
        gbc.gridy = 12;

        pnl.add(close, gbc);

        bg.add(pnl);
        bg.pack();

        save.addActionListener(this);
        delete.addActionListener(this);
        nameList.addListSelectionListener((ListSelectionListener) this);

        bg.setVisible(true);
    }

    public void setDisplay(String inf) {

        String date = inf.replace("/", " ").split(" ")[0];
        String month = inf.replace("/", " ").split(" ")[1];
        DefaultListModel model = new DefaultListModel();


        JList tempData = datalst;

        int[] d = new int[tempData.getModel().getSize()];
//        System.out.println(tempData.getModel().getSize());

        for (int i = 0; i < tempData.getModel().getSize(); i++) {
            String[] tempElem = (String[]) tempData.getModel().getElementAt(i);
//            System.out.println(tempElem[2] + tempElem[3] + tempElem[4]);
            if (date.equals(tempElem[3]) && month.equals(tempElem[2])) {
                d[i] = i;
            }
        }

//        System.out.println(Arrays.toString(tempData.getModel()));
        // String[] test = {"Test", "2020", "5", "20", "13","30","3"};
        for(int i = 0; i < d.length; i++) {
            String[] t = (String[]) tempData.getModel().getElementAt(d[i]);
            model.addElement(t[0] + " - " + t[4] + ":" + t[5] + " during " + t[6] + " hrs");
        }
        nameList = new JList(model);
        pnlList = new JScrollPane(nameList);
    }


    public String getMonth(){
        month = String.valueOf(cal.get(Calendar.MONTH)+1);
        return month;
    }

    public String getDate(){
        date = String.valueOf(cal.get(Calendar.DATE));
        return date;
    }

    private void show(ScheduleInfo s) {
        if (s != null) {
            // TODO: Need to set up the dataset from ScheduleInfo and Data
            namebox.setText(s.getBoardTitle());
            creatorbox.setText(s.getCreator());
            hourbox.setText(s.getHour());
            minbox.setText(s.getMinute());
            dubox.setText(s.getDuration());
        }
    }

    private void clearFields() {
        namebox.setText("");
        hourbox.setText("");
        minbox.setText("");
        dubox.setText("");
        creatorbox.setText("");
    }

    private void setFieldsEditable(boolean editable) {
        namebox.setEditable(editable);
        hourbox.setEditable(editable);
        minbox.setEditable(editable);
        dubox.setEditable(editable);
        creatorbox.setEditable(editable);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
//        dispose();
        JButton act = (JButton) e.getSource();

        if (act == newbtn){
            clearFields();
            setFieldsEditable(true);
            save.setEnabled(true);
        }
        else if(act == save){
            savePressed();

        } else if (act == edit) {
            editPressed();

        } else if (act == delete) {
            deletePressed();

        } else if (act == close) {
            dispose();
        }
    }

    private void savePressed() {
        if (namebox.getText() != null && !namebox.getText().equals("") && hourbox.getText() != null && !hourbox.getText().equals("")
        && minbox.getText() != null && !minbox.getText().equals("")) {
            ScheduleInfo s = new ScheduleInfo(namebox.getText(), creatorbox.getText(), getMonth(), getDate(), hourbox.getText(), minbox
                    .getText(), dubox.getText());
            data.add(s);
        }
        setFieldsEditable(false);
        save.setEnabled(false);
    }

    // TODO: I think it is not really essential button to edit as we already have save button
    private void editPressed() {

    }

    private void deletePressed() {

        data.remove(nameList.getSelectedValue());
        clearFields();
        delete.setEnabled(data.getSize() != 0);
    }

    public void listAction(ListSelectionEvent e) {
        if (nameList.getSelectedValue() != null
                && !nameList.getSelectedValue().equals("")) {
            show(data.get(nameList.getSelectedValue()));
        }
    }

}
