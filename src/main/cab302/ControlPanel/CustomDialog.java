package cab302.ControlPanel;

import cab302.database.billboard.BillboardData;
import cab302.database.schedule.ScheduleData;
import cab302.database.schedule.ScheduleInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

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

    private JComboBox billboardChooser;
    private DefaultComboBoxModel chooserBox;

    private JLabel lblName;
    private JLabel lblDuhour;
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
    DefaultListModel model;

    ScheduleData data;
    BillboardData boradData;

    String month;
    String date;
    Integer time;

    Calendar cal;

    JList datalst;


    public CustomDialog(String info, int selectedTime, ScheduleData data, BillboardData boradData) {
        this.data = data;
        this.boradData = boradData;
        time = selectedTime;

        cal = Calendar.getInstance();

        date = info.replace("/", " ").split(" ")[0];
        month = info.replace("/", " ").split(" ")[1];

        pnl = new JPanel();
        setTitle(info);
        pnl.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        datalst = new JList(data.take());

        setDisplay(date, month);

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

        billboardChooser = setBillboardChooser();
        gbc.gridwidth = 3;
        gbc.gridx = 2;
        gbc.gridy = 7;

        pnl.add(billboardChooser, gbc);

        // Creator
        lblCreator = new JLabel("Creator");
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 8;

        pnl.add(lblCreator, gbc);

        creatorbox = new JTextField(20);
        creatorbox.setEditable(false);
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
        hourbox.setText(String.valueOf(time));
        hourbox.setEditable(false);
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

        lblDuhour = new JLabel("hr");
        gbc.gridx = 3;
        gbc.gridy = 10;

        pnl.add(lblDuhour, gbc);


        // Buttons
        newbtn = new JButton("New");
        gbc.gridx = 1;
        gbc.gridy = 11;

        pnl.add(newbtn, gbc);

        save = new JButton("Save");
        gbc.gridx = 2;
        gbc.gridy = 11;

        pnl.add(save, gbc);

        edit = new JButton("Edit");
        gbc.gridx = 3;
        gbc.gridy = 11;

        pnl.add(edit, gbc);

        delete = new JButton("Delete");
        gbc.gridx = 3;
        gbc.gridy = 12;

        pnl.add(delete, gbc);

        close = new JButton("Back to calendar");
        gbc.gridx = 2;
        gbc.gridy = 12;

        pnl.add(close, gbc);

        add(pnl);
        pack();

        newbtn.addActionListener(this);
        save.addActionListener(this);
        delete.addActionListener(this);
        edit.addActionListener(this);
        close.addActionListener(this);

        billboardChooser.addActionListener(e -> {
//            if(billboardChooser.getSelectedItem().equals("HH")) {
//                creatorbox.setText("H");
//            }else
            if(billboardChooser.getSelectedItem().equals("")) {
                creatorbox.setText("");
            }else
                if (billboardChooser.getSelectedItem() != null
                    && !billboardChooser.getSelectedItem().equals("")) {
//                show(data.get(billboardChooser.getSelectedItem().toString()));
                creatorbox.setText(boradData.get(billboardChooser.getSelectedItem()).getCreator());
            }
        });

        nameList.addListSelectionListener(e -> {if(nameList.getSelectedValue() != null){
            show(data.get(nameList.getSelectedValue().toString().split(" ")[0]));
        }});
        setVisible(true);
    }

    public JComboBox setBillboardChooser(){
        billboardChooser = new JComboBox();
        chooserBox = new DefaultComboBoxModel<>();

        // For testing new name and creator set of database as name and creator have to be set in Home UI
        chooserBox.addElement("");
//        chooserBox.addElement("HH");

        for(int i=0; i<boradData.getModel().getSize(); i++) chooserBox.addElement(boradData.getModel().getElementAt(i));

        billboardChooser.setModel(chooserBox);

//        x.add(monthChooser);
        return billboardChooser;
    }

    public void setDisplay(String date, String month) {

        model = new DefaultListModel();


//        JList tempData = datalst;

        int[] d = new int[data.take().getSize()];
        int index = 0;

        while (index < data.take().getSize()) {

                if (data.get(data.take().getElementAt(index)).getDate().equals(date) &&
                        data.get(data.take().getElementAt(index)).getMonth().equals(month)) {
                    d[index] = index;
                } else {
                    d[index] = -1;
                }
                if (d[index] != -1) {
                    String value = data.findRow(index).getBoardTitle() + " - "
                            + data.findRow(index).getHour()
                            + ":" + data.findRow(index).getMinute()
                            + " during " + data.findRow(index).getDuration()
                            + "hrs";
                    model.addElement(value);

                }
            index++;
        }

        nameList = new JList(model);
        pnlList = new JScrollPane(nameList);
    }


    private void show(ScheduleInfo s) {
        if (s != null) {
            billboardChooser.setSelectedItem(s.getBoardTitle());
            creatorbox.setText(s.getCreator());
            hourbox.setText(s.getHour());
            minbox.setText(s.getMinute());
            dubox.setText(s.getDuration());
        }
    }

    private void clearFields() {
        billboardChooser.setSelectedIndex(0);
        minbox.setText("");
        dubox.setText("");
        creatorbox.setText("");
    }

    private void setFieldsEditable(boolean editable) {
        minbox.setEditable(editable);
        dubox.setEditable(editable);
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
            this.dispose();
//            setDisplay(date, month);

        } else if (act == edit) {
            editPressed();
            this.dispose();
//            setDisplay(date, month);

        } else if (act == delete) {
            deletePressed();
            this.dispose();
//            setDisplay(date, month);

        } else if (act == close) {
            this.dispose();
        }
    }

    private void savePressed() {
        if (billboardChooser.getSelectedItem() != null && !billboardChooser.getSelectedItem().equals("") &&
                hourbox.getText() != null && !hourbox.getText().equals("")
        && minbox.getText() != null && !minbox.getText().equals("")) {
            ScheduleInfo s = new ScheduleInfo(String.valueOf(billboardChooser.getSelectedItem()), creatorbox.getText(),
                    month, date, String.valueOf(time), minbox
                    .getText(), dubox.getText());
            data.add(s);
            model.removeAllElements();
//            setDisplay(date, month);
        }
    }

    private void editPressed() {
        if (billboardChooser.getSelectedItem() != null && !billboardChooser.getSelectedItem().equals("") &&
                hourbox.getText() != null && !hourbox.getText().equals("")
                && minbox.getText() != null && !minbox.getText().equals("")) {
            data.edit(billboardChooser.getSelectedItem().toString(), creatorbox.getText(), month, date, String.valueOf(time), minbox
                    .getText(), dubox.getText());
            model.removeAllElements();
//            setDisplay(date, month);
        }
    }

    private void deletePressed() {
        data.remove(nameList.getSelectedValue().toString().split(" ")[0]);
        model.removeAllElements();
//        setDisplay(date, month);
    }
}
