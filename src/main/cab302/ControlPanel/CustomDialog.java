package cab302.ControlPanel;

import cab302.database.schedule.ScheduleInfo;
import cab302.server.Billboardserver.AcknowledgeReply;
import cab302.server.Billboardserver.BillboardReply;
import cab302.server.Billboardserver.FindScheduleReply;
import cab302.server.WillBeControlPanelAction.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.Properties;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class CustomDialog extends JDialog implements ActionListener {

    private JButton save;
    private JButton edit;
    private JButton delete;
    private JButton close;
    private JButton newbtn;
    DefaultListModel model;
    private JPanel pnl;
    private JScrollPane pnlList;
    private JFrame bg;

    private JTextField hourbox;
    private JTextField minbox;

    Integer time;
    private JTextField creatorbox;
    private JTextField duHrbox;
    private JTextField duMinbox;

    private JLabel lblName;
    private JTextField recurbox;
    private JComboBox billboardChooser;
    private JLabel lblTime;
    private JLabel lblHour;
    private JLabel lblMin;
    private JLabel lblduration;
    private JLabel lblCreator;
    private DefaultComboBoxModel chooserBox;
    private JLabel lblDuhour;

    private JList nameList;
    private JLabel lblDumin;
    private JList lblRehr;
    ListModel billboardLists;
    ListModel scheduleLists;
    ListModel schedulDuple;
    private JLabel lblRecur;
    String year1;
    String month;
    String date;

    Calendar cal;
    String sessionToken;
    JList datalst;
    private String host;

    private static String port;
    Socket socket;
    JList data;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream oos;
    ObjectInputStream ois;


    public CustomDialog(JFrame Bg, String info, String year, int selectedTime, ListModel billboards, ListModel schedules, ListModel duppleSchedules, String token) throws IOException, ClassNotFoundException {
//        this.data = data;
////        this.boradData = boradData;
        bg = Bg;
        getPropValues();
        billboardLists = billboards;
        scheduleLists = schedules;
        schedulDuple = duppleSchedules;
        time = selectedTime;
        sessionToken = token;
        cal = Calendar.getInstance();
        year1 = year;

        date = info.replace("/", " ").split(" ")[0];
        month = info.replace("/", " ").split(" ")[1];

        pnl = new JPanel();
        setTitle(info);
        pnl.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        datalst = new JList(scheduleLists);

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

        lblRecur = new JLabel("Recur duration");
        gbc.gridx = 1;
        gbc.gridy = 10;

        pnl.add(lblRecur, gbc);

        recurbox = new JTextField(3);
        gbc.gridx = 2;
        gbc.gridy = 10;
        recurbox.setEditable(false);

        pnl.add(recurbox, gbc);
        lblRecur = new JLabel("hrs");
        gbc.gridx = 3;
        gbc.gridy = 10;

        pnl.add(lblRecur, gbc);

        // Duration

        lblduration = new JLabel("Duration");
        gbc.gridx = 1;
        gbc.gridy = 11;

        pnl.add(lblduration, gbc);

        duHrbox = new JTextField(3);
        gbc.gridx = 2;
        gbc.gridy = 11;

        pnl.add(duHrbox, gbc);

        lblDuhour = new JLabel("hr");
        gbc.gridx = 3;
        gbc.gridy = 11;

        pnl.add(lblDuhour, gbc);

        duMinbox = new JTextField(3);
        gbc.gridx = 4;
        gbc.gridy = 11;

        pnl.add(duMinbox, gbc);

        lblDumin = new JLabel("min");
        gbc.gridx = 5;
        gbc.gridy = 11;

        pnl.add(lblDuhour, gbc);


        // Buttons
        newbtn = new JButton("New");
        gbc.gridx = 1;
        gbc.gridy = 12;

        pnl.add(newbtn, gbc);

        save = new JButton("Save");
        gbc.gridx = 2;
        gbc.gridy = 12;

        pnl.add(save, gbc);

        edit = new JButton("Edit");
        gbc.gridx = 3;
        gbc.gridy = 12;

        pnl.add(edit, gbc);


        delete = new JButton("Delete");
        gbc.gridx = 3;
        gbc.gridy = 13;

        pnl.add(delete, gbc);

        close = new JButton("Back to calendar");
        gbc.gridx = 2;
        gbc.gridy = 13;

        pnl.add(close, gbc);

        add(pnl);
        pack();

        newbtn.addActionListener(this);
        save.addActionListener(this);
        delete.addActionListener(this);
        edit.addActionListener(this);
        close.addActionListener(this);



//        System.out.println(billboardLists);
//        System.out.println("yeyey");
//        System.out.println(billboardChooser.getSelectedItem());

//        System.out.println(finalBillboardcreator);
        billboardChooser.addActionListener(e -> {
//            if(billboardChooser.getSelectedItem().equals("HH")) {
//                creatorbox.setText("H");
//            }else
            if (billboardChooser.getSelectedItem().equals("")) {
                creatorbox.setText("");
            } else if (billboardChooser.getSelectedItem() != null
                    && !billboardChooser.getSelectedItem().equals("")) {
//                show(data.get(billboardChooser.getSelectedItem().toString()));
                try {
                    socketStart();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                BillboardRequest billboardRequest = new BillboardRequest((String)billboardChooser.getSelectedItem(),sessionToken);
                try {
                    oos.writeObject(billboardRequest);
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
                String billboardcreator = null;
                if (trans instanceof BillboardReply){
                    BillboardReply reply = (BillboardReply) trans;
                    billboardcreator = reply.getCreator();
                }
                try {
                    socketStop();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                creatorbox.setText(billboardcreator);
            }
        });

        nameList.addListSelectionListener(e -> {
            if (nameList.getSelectedValue() != null) {
                try {
                    socketStart();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                FindScheduleRequest findScheduleRequest = new FindScheduleRequest(sessionToken,nameList.getSelectedValue().toString().split(" ")[0], date,
                        nameList.getSelectedValue().toString().split(" ")[2]);
                try {
                    oos.writeObject(findScheduleRequest);
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
                ScheduleInfo schedule = new ScheduleInfo();
                if (transo instanceof FindScheduleReply){
                    FindScheduleReply reply = (FindScheduleReply) transo;
                    schedule = reply.getScheduleInfo();
                }
                try {
                    socketStop();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                show(schedule);
            }
        });
        setVisible(true);
    }

    public JComboBox setBillboardChooser() {
        billboardChooser = new JComboBox();
        chooserBox = new DefaultComboBoxModel<>();

        // For testing new name and creator set of database as name and creator have to be set in Home UI
//        chooserBox.addElement("HH");
//        System.out.println(billboardLists.getSize());
        for (int i = 0; i < billboardLists.getSize(); i++)
            chooserBox.addElement(billboardLists.getElementAt(i));

        billboardChooser.setModel(chooserBox);

//        x.add(monthChooser);
        return billboardChooser;
    }

    public void setDisplay(String date, String month) throws IOException, ClassNotFoundException {

        model = new DefaultListModel();


//        JList tempData = datalst;

        int[] d = new int[schedulDuple.getSize()];
        int index = 0;

        while (index < schedulDuple.getSize()) {
            socketStart();
            GetIndexSchedule gis = new GetIndexSchedule(sessionToken,index);
            oos.writeObject(gis);
            oos.flush();

            Object transo = ois.readObject();
            ScheduleInfo schedule = new ScheduleInfo();
            if (transo instanceof FindScheduleReply){
                FindScheduleReply reply = (FindScheduleReply) transo;
                schedule = reply.getScheduleInfo();
            }
            socketStop();
            if (schedule.getDate().equals(date) &&
                    schedule.getMonth().equals(month)) {
                d[index] = index;
            } else {
                d[index] = -1;
            }
            if (d[index] != -1) {
                String setrehrs = schedule.getRecur();
                if (schedule.getRecur() == null || schedule.getRecur().equals("")) {
                    setrehrs = "0";
                }
                String value = schedule.getBoardTitle() + " - "
                        + schedule.getHour()
                        + " : " + schedule.getMinute()
                        + " during " + schedule.getDuHr()
                        + " hrs " + schedule.getDuMin()
                        + " mins frequently "
                        + setrehrs + " hrs";
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
            duHrbox.setText(s.getDuHr());
            duMinbox.setText(s.getDuMin());
            recurbox.setText(s.getRecur());
        }
    }

    private void clearFields() {
        billboardChooser.setSelectedIndex(0);
        minbox.setText("");
        duHrbox.setText("");
        duMinbox.setText("");
        recurbox.setText("");
        creatorbox.setText("");
    }

    private void setFieldsEditable(boolean editable) {
        minbox.setEditable(editable);
        duHrbox.setEditable(editable);
        duMinbox.setEditable(editable);
        recurbox.setEditable(editable);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
//        dispose();
        JButton act = (JButton) e.getSource();

        if (act == newbtn) {
            clearFields();
            recurbox.setEditable(true);
            save.setEnabled(true);
        } else if (act == save) {
            try {
                savePressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            dispose();
        } else if (act == edit) {
            try {
                editPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            dispose();
//            setDisplay(date, month);

        } else if (act == delete) {
            try {
                deletePressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            dispose();
//            setDisplay(date, month);

        } else if (act == close) {
            dispose();
        }
    }

    private void savePressed() throws IOException, ClassNotFoundException {
        billboardChooser.setEditable(false);
        if (Integer.parseInt(duHrbox.getText()) + Integer.parseInt(hourbox.getText()) > 24) {
            showMessageDialog(null, "Duration cannot be over selected date", "Title", ERROR_MESSAGE);
            clearFields();
        } else if (!recurbox.getText().equals("") && recurbox.getText() != null && Integer.parseInt(recurbox.getText()) < Integer.parseInt(duHrbox.getText())) {
//                if(Integer.parseInt(recurbox.getText()) < Integer.parseInt(duHrbox.getText()))
//            {
            showMessageDialog(null, "Recurring time cannot be lesser than duration", "Title", ERROR_MESSAGE);
            clearFields();
//            }
        } else if (billboardChooser.getSelectedItem() != null && !billboardChooser.getSelectedItem().equals("") &&
                hourbox.getText() != null && !hourbox.getText().equals("")
                && minbox.getText() != null && !minbox.getText().equals("")
                && duHrbox.getText() != null && !duHrbox.getText().equals("")
                && duMinbox.getText() != null && !duMinbox.getText().equals("")) {
            socketStart();
            ScheduleBillboardRequest scheduleBillboardRequest = new ScheduleBillboardRequest(String.valueOf(billboardChooser.getSelectedItem()),creatorbox.getText(),
                    year1, month, date, String.valueOf(time), minbox
                    .getText(), sessionToken, duHrbox.getText(), duMinbox.getText(), recurbox.getText());
            oos.writeObject(scheduleBillboardRequest);
            oos.flush();
            Object trans = ois.readObject();
            if (trans instanceof AcknowledgeReply){
                AcknowledgeReply reply = (AcknowledgeReply) trans;
                System.out.println(reply.getAcknowledgement());
            }
            socketStop();
//            data.add(s);
            model.removeAllElements();
            bg.dispose();
//            setDisplay(date, month);
        }
    }

    private void editPressed() throws IOException, ClassNotFoundException {
        if (Integer.parseInt(duHrbox.getText()) + Integer.parseInt(duMinbox.getText()) + time > 24) {
            showMessageDialog(null, "Duration cannot be over selected date", "Title", ERROR_MESSAGE);
            clearFields();
        } else if (!billboardChooser.getSelectedItem().toString().equals(nameList.getSelectedValue().toString().split(" ")[0])){
           showMessageDialog(null, "Billboard name should be same !", "Title", ERROR_MESSAGE);
           clearFields();
        }
        else if (!recurbox.getText().equals("")){
            if ( Integer.parseInt(recurbox.getText()) < Integer.parseInt(duHrbox.getText()) + Integer.parseInt(duMinbox.getText())) {
            showMessageDialog(null, "Recurring time cannot be lesser than duration", "Title", ERROR_MESSAGE);
            clearFields();
            }
        } else if (billboardChooser.getSelectedItem() != null && !billboardChooser.getSelectedItem().equals("") &&
                hourbox.getText() != null && !hourbox.getText().equals("")
                && minbox.getText() != null && !minbox.getText().equals("")) {

            socketStart();
            EditScheduleBillboard esbr = new EditScheduleBillboard(sessionToken, String.valueOf(billboardChooser.getSelectedItem()), creatorbox.getText(), year1, month, date, hourbox.getText(), minbox
                    .getText(),  duHrbox.getText(), duMinbox.getText(), recurbox.getText());
            oos.writeObject(esbr);
            oos.flush();
            Object trans = ois.readObject();
            if (trans instanceof AcknowledgeReply){
                AcknowledgeReply reply = (AcknowledgeReply) trans;
                System.out.println(reply.getAcknowledgement());
            }
            socketStop();
//            data.edit(billboardChooser.getSelectedItem().toString(), creatorbox.getText(), month, date, String.valueOf(time), minbox
//                    .getText(), duHrbox.getText(), duMinbox.getText(), recurbox.getText());
            model.removeAllElements();
            bg.dispose();
//            setDisplay(date, month);
        }
    }

    private void deletePressed() throws IOException, ClassNotFoundException {
        if ((billboardChooser.getSelectedItem() != null && !billboardChooser.getSelectedItem().equals("") &&
                hourbox.getText() != null && !hourbox.getText().equals("")
                && minbox.getText() != null && !minbox.getText().equals("")
                && duHrbox.getText() != null && !duHrbox.getText().equals("")
                && duMinbox.getText() != null && !duMinbox.getText().equals("") ) ){
            if(String.valueOf(billboardChooser.getSelectedItem()).equals(nameList.getSelectedValue().toString().split(" ")[0]) && hourbox.getText().equals(nameList.getSelectedValue().toString().split(" ")[2])
                    && minbox.getText().equals(nameList.getSelectedValue().toString().split(" ")[4]) && duHrbox.getText().equals(nameList.getSelectedValue().toString().split(" ")[6])
                    && duMinbox.getText().equals(nameList.getSelectedValue().toString().split(" ")[8])){
                socketStart();
                RemoveBillboardRequest removeBillboardRequest = new RemoveBillboardRequest(String.valueOf(billboardChooser.getSelectedItem()), sessionToken, month, date, hourbox.getText());
                oos.writeObject(removeBillboardRequest);
                oos.flush();
                Object trans = ois.readObject();
                if (trans instanceof AcknowledgeReply){
                    AcknowledgeReply reply = (AcknowledgeReply) trans;
                    System.out.println(reply.getAcknowledgement());
                }
                socketStop();
//        data.remove(billboardChooser.getSelectedItem().toString(), date, hourbox.getText());
                model.removeAllElements();
                bg.dispose();
            }
            else{
                showMessageDialog(null, "Cannot delate new Schedule off course", "Title", ERROR_MESSAGE);
                clearFields();
                bg.dispose();
            }
        }

        else{
            showMessageDialog(null, "Cannot be null", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        }
//        setDisplay(date, month);
    }

    private void recurePressed() {
        recurbox.setEditable(true);
    }
    public void socketStart() throws IOException {
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
