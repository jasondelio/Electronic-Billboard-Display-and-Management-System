package cab302.ControlPanel;

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


    public CustomDialog(String info, ScheduleData data) {
        this.data = data;

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
        gbc.gridx = 4;
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

        nameList.addListSelectionListener(e -> {
            if (nameList.getSelectedValue() != null
                    && !nameList.getSelectedValue().equals("")) {
                show(data.get(nameList.getSelectedValue().toString().split(" ")[0]));

            }
        });
        setVisible(true);
    }


    public void setDisplay(String date, String month) {

        DefaultListModel model = new DefaultListModel();


        JList tempData = datalst;

        int[] d = new int[tempData.getModel().getSize()];

        for (int i = 0; i < tempData.getModel().getSize(); i++) {
            if (tempData.getModel().getSize() > 1) {
                String tempVal = String.valueOf(tempData.getModel()
                        .getElementAt(0))
                        .split("/,")[i]
                        .replace("[", "")
                        .replace("]", "")
                        .replace("/", "")
                        .replace(" ","");

                String[] tempElem = tempVal.split(",");

                if (date.equals(tempElem[2]) && month.equals(tempElem[1])) {
                    d[i] = i;
                }
                else {
                    d[i] = -1;
                }

            } else
            {
                String[] temp = String.valueOf(tempData.getModel())
                        .replace("[[","")
                        .replace("]]","")
                        .replace(" ","")
                        .split(",");
                if (date.equals(temp[2]) && month.equals(temp[1])) {
                    d[i] = i;
                }
                else {
                    d[i] = -1;
                }
            }
        }

        for(int i = 0; i < d.length; i++) {
            String[] t;
            if (tempData.getModel().getSize() > 1) {
                String tempVal = String.valueOf(tempData.getModel()
                        .getElementAt(0))
                        .split("/,")[i]
                        .replace("[", "")
                        .replace("]", "")
                        .replace("/", "")
                        .replace(" ","");

                t = tempVal.split(",");
            } else
            {
                t = String.valueOf(tempData.getModel())
                        .replace("[[","")
                        .replace("]]","")
                        .replace("/","")
                        .replace(" ","")
                        .split(",");
            }
//            System.out.println(d[i]);
            if(d[i] != -1) {
                model.addElement(t[0] + " - " + t[3] + ":" + t[4] + " during " + t[5] + " hrs");
            }
        }
        nameList = new JList(model);
        pnlList = new JScrollPane(nameList);
    }


    private void show(ScheduleInfo s) {
        if (s != null) {
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
            this.dispose();

        } else if (act == edit) {
            editPressed();
            this.dispose();

        } else if (act == delete) {
            deletePressed();
            this.dispose();

        } else if (act == close) {
            this.dispose();
        }
    }

    private void savePressed() {
        if (namebox.getText() != null && !namebox.getText().equals("") && hourbox.getText() != null && !hourbox.getText().equals("")
        && minbox.getText() != null && !minbox.getText().equals("")) {
            ScheduleInfo s = new ScheduleInfo(namebox.getText(), creatorbox.getText(), month, date, hourbox.getText(), minbox
                    .getText(), dubox.getText());
            data.add(s);
        }
    }

    private void editPressed() {
        if (namebox.getText() != null && !namebox.getText().equals("") && hourbox.getText() != null && !hourbox.getText().equals("")
                && minbox.getText() != null && !minbox.getText().equals("")) {
            data.edit(namebox.getText(), creatorbox.getText(), month, date, hourbox.getText(), minbox
                    .getText(), dubox.getText());
        }
    }

    private void deletePressed() {
        data.remove(nameList.getSelectedValue().toString().split(" ")[0]);
    }
}
