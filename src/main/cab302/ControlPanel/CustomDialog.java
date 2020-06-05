package cab302.ControlPanel;

import cab302.database.schedule.ScheduleInfo;
import cab302.server.ApplicationsRequests.*;
import cab302.server.ReplyToApplications.AcknowledgeReply;
import cab302.server.ReplyToApplications.BillboardReply;
import cab302.server.ReplyToApplications.FindScheduleReply;

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

/**
 * This class is for dialog of calendar to show the data of each date and set the new data for schedule database
 */
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
    String minute;

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

    /**
     * The constructor to call and set the dialog for customizing schedule
     *
     * @param Bg              : the frame of calendarGUI
     * @param info            : title information
     * @param year            : current year
     * @param selectedTime    : selected time by user
     * @param billboards      : selected billboard by user
     * @param schedules       : selected schedule by user
     * @param duppleSchedules : selected recurrence Schedule information
     * @param token           : session token
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public CustomDialog(JFrame Bg, String info, String year, int selectedTime, ListModel billboards, ListModel schedules, ListModel duppleSchedules, String token) throws IOException, ClassNotFoundException {
        bg = Bg;
        getPropValues();
        billboardLists = billboards;
        scheduleLists = schedules;
        schedulDuple = duppleSchedules;
        time = selectedTime;
        sessionToken = token;
        cal = Calendar.getInstance();
        year1 = year;

        /*
        Set the date and month by using the title of dialog
         */
        date = info.replace("/", " ").split(" ")[0];
        month = info.replace("/", " ").split(" ")[1];

        pnl = new JPanel();
        setTitle(info);
        /*
        Set layout by importing grid bag layout
         */
        pnl.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        /*
        Set the list of every schedule on selected date
         */
        datalst = new JList(scheduleLists);

        setDisplay(date, month);

        gbc.gridwidth = 5;
        gbc.gridheight = 5;
        gbc.gridx = 1;
        gbc.gridy = 1;

        pnl.add(pnlList, gbc);

        /*
        Billboard title and chooser box
         */
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

        /*
        Creator
         */
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

        /*
        Time information - hour and minutes
         */
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

        /*
        Recurrence set - Hours
         */
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

        /*
        Duration - Hour and minutes
         */

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

        pnl.add(lblDumin, gbc);


        /*
        Set buttons - new, save, edit, back to main, delete
         */
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

        /*
        Add action listener to buttons
         */
        newbtn.addActionListener(this);
        save.addActionListener(this);
        delete.addActionListener(this);
        edit.addActionListener(this);
        close.addActionListener(this);

        setEditable(false);
        /*
        Set the specific action to billboard chooser as it needs to take the data from
        billboard table as the billboard title and creator must be stored before modify the schedule data
         */
        billboardChooser.addActionListener(e -> {
            if (billboardChooser.getSelectedItem().equals("")) {
                creatorbox.setText("");
            } // When the billboard title is selected
            else if (billboardChooser.getSelectedItem() != null
                    && !billboardChooser.getSelectedItem().equals("")) {
                // Send the request to server to take items
                try {
                    socketStart();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                GetBillboardInfoRequest getBillboardInfoRequest = new GetBillboardInfoRequest((String) billboardChooser.getSelectedItem(), sessionToken);
                try {
                    oos.writeObject(getBillboardInfoRequest);
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
                if (trans instanceof BillboardReply) {
                    BillboardReply reply = (BillboardReply) trans;
                    billboardcreator = reply.getCreator();
                }
                try {
                    socketStop();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // After taking the information of billboard title and creator
                // the creator text box must be set up automatically
                creatorbox.setText(billboardcreator);
            }
        });

        /*
        Set the action of list of schedule at selected date as when the user selected each item of list,
        the information must be returned on each boxes of schedule eg. time hour, minutes, durations and so on
         */
        nameList.addListSelectionListener(e -> {
            setEditable(true);
            if (nameList.getSelectedValue() != null) {
                // Send the request to server to take items
                try {
                    socketStart();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                FindScheduleRequest findScheduleRequest = new FindScheduleRequest(sessionToken, nameList.getSelectedValue().toString().split(" ")[0], month, date,
                        nameList.getSelectedValue().toString().split(" ")[2],nameList.getSelectedValue().toString().split(" ")[4]);
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
                    minute = schedule.getMinute();
                }
                try {
                    socketStop();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // Show the information of selected schedule
                show(schedule);
            }
        });
        setVisible(true);
    }

    /**
     * Get the data of props for server
     *
     * @throws IOException
     */
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

    /**
     * Set the combo box for selecting the titles and creator
     *
     * @return billboardChooser
     */
    public JComboBox setBillboardChooser() {
        billboardChooser = new JComboBox();
        chooserBox = new DefaultComboBoxModel<>();
        // for initial element of this combo box is blanket
        chooserBox.addElement("");
        for (int i = 0; i < billboardLists.getSize(); i++)
            chooserBox.addElement(billboardLists.getElementAt(i)); // Add every titles from billboard database into combo box

        billboardChooser.setModel(chooserBox);

        return billboardChooser;
    }

    /**
     * Set the display of schedule list
     *
     * @param date : the date to display every schedules
     * @param month : the month to display every schedules
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void setDisplay(String date, String month) throws IOException, ClassNotFoundException {

        model = new DefaultListModel();

        int[] d = new int[schedulDuple.getSize()];
        int index = 0;

        /*
        While the index is same wilt the size of schedule, get the information of schedule that are
        title, time, hour, duration and frequency of recurrence to show on the Jlist
         */
        while (index < schedulDuple.getSize()) {
            // Send the request to server to take the data
            socketStart();
            GetIndexSchedule gis = new GetIndexSchedule(sessionToken, index);
            oos.writeObject(gis);
            oos.flush();

            Object transo = ois.readObject();
            ScheduleInfo schedule = new ScheduleInfo();
            if (transo instanceof FindScheduleReply) {
                FindScheduleReply reply = (FindScheduleReply) transo;
                schedule = reply.getScheduleInfo();
            }
            socketStop();

            // If the schedule date and month are same with selected date and month,
            // store the index of schedule into list of d
            if (schedule.getDate().equals(date) &&
                    schedule.getMonth().equals(month)) {
                d[index] = index;
            } else {
                d[index] = -1;
            }
            // If the data at index is not null
            if (d[index] != -1) {
                String setrehrs = schedule.getRecur();
                // If the recurrence is not set
                if (schedule.getRecur() == null || schedule.getRecur().equals("")) {
                    setrehrs = "0";
                }
                // Set the printing value of string
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

    /**
     * Set false until new button is pressed or the Jlist pressed
     *
     * @param editable : boolean value to set editable
     */
    private void setEditable(boolean editable) {
        this.minbox.setEditable(editable);
        this.duHrbox.setEditable(editable);
        this.duMinbox.setEditable(editable);
        this.recurbox.setEditable(editable);
        this.edit.setEnabled(editable);
        this.save.setEnabled(editable);
        this.delete.setEnabled(editable);
        this.billboardChooser.setEnabled(editable);
    }

    /**
     * To show the information on every textbox by selecting the list
     *
     * @param s : the schedule information
     */
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

    /**
     * Clear every textbox
     */
    private void clearFields() {
        billboardChooser.setSelectedIndex(0);
        minbox.setText("");
        duHrbox.setText("");
        duMinbox.setText("");
        recurbox.setText("");
        creatorbox.setText("");
    }

    /**
     * Set action performances of buttons
     *
     * @param e : the action event object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton act = (JButton) e.getSource();

        // When new button is clicked
        if (act == newbtn) {
            clearFields();
            recurbox.setEditable(true);
            save.setEnabled(true);
            setEditable(true);
        } else if (act == save) { // When save button is clicked
            try {
                savePressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            dispose();
        } else if (act == edit) { // When edit button is clicked
            try {
                editPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            dispose();

        } else if (act == delete) { // When delete button is clicked
            try {
                deletePressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            dispose();

        } else if (act == close) { // When close button is clicked
            dispose();
        }
    }

    /**
     * When the save button is pressed
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void savePressed() throws IOException, ClassNotFoundException {
        billboardChooser.setEditable(false);
        // When the hour + duration is over midnight
        if (Integer.parseInt(duHrbox.getText()) + Integer.parseInt(hourbox.getText()) > 24) {
            showMessageDialog(null, "Duration cannot be over selected date", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        } // If the inputs are not 2 digits
        else if (duHrbox.getText().length() <= 1 || duHrbox.getText().length() > 2) {
            showMessageDialog(null, "Input must be 2 digits eg.00, 01,...,12", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        }// If the inputs are not 2 digits
        else if (duMinbox.getText().length() <= 1 || duMinbox.getText().length() > 2) {
            showMessageDialog(null, "Input must be 2 digits eg.00, 01,...,12", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        }
        // If the recurrence is lesser than duration
        else if (!recurbox.getText().equals("") && recurbox.getText() != null && Integer.parseInt(recurbox.getText()) < Integer.parseInt(duHrbox.getText())) {

            showMessageDialog(null, "Recurring time cannot be lesser than duration", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        } // When the entered information is enough to create the new schedule
        else if (billboardChooser.getSelectedItem() != null && !billboardChooser.getSelectedItem().equals("") &&
                hourbox.getText() != null && !hourbox.getText().equals("")
                && minbox.getText() != null && !minbox.getText().equals("")
                && duHrbox.getText() != null && !duHrbox.getText().equals("")
                && duMinbox.getText() != null && !duMinbox.getText().equals("")) {
            socketStart();
            ScheduleBillboardRequest scheduleBillboardRequest = new ScheduleBillboardRequest(String.valueOf(billboardChooser.getSelectedItem()), creatorbox.getText(),
                    year1, month, date, String.valueOf(time), minbox
                    .getText(), sessionToken, duHrbox.getText(), duMinbox.getText(), recurbox.getText());
            oos.writeObject(scheduleBillboardRequest);
            oos.flush();
            Object trans = ois.readObject();
            if (trans instanceof AcknowledgeReply) {
                AcknowledgeReply reply = (AcknowledgeReply) trans;
                System.out.println(reply.getAcknowledgement());
            }
            socketStop();
            // Remove all elements of list for next dialog
            model.removeAllElements();
            bg.dispose();
        } // when user do not choose any title, should be error
        else if (billboardChooser.getSelectedItem().equals("")){
            showMessageDialog(null, "Choose title", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        }
    }

    /**
     * When the edit button is pressed
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void editPressed() throws IOException, ClassNotFoundException {
        /*
        Same four conditions for the time and inputs with save button, another new condition is when the billboard title is same
         */
        if (Integer.parseInt(duHrbox.getText()) + Integer.parseInt(duMinbox.getText()) + time > 24) {
            showMessageDialog(null, "Duration cannot be over selected date", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        } else if (!billboardChooser.getSelectedItem().toString().equals(nameList.getSelectedValue().toString().split(" ")[0])) {
            showMessageDialog(null, "Billboard name should be same !", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        } else if (duHrbox.getText().length() <= 1 || duHrbox.getText().length() > 2) {
            showMessageDialog(null, "Input must be 2 digits eg.00, 01,...,12", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();

        } else if (duMinbox.getText().length() <= 1 || duMinbox.getText().length() > 2) {
            showMessageDialog(null, "Input must be 2 digits eg.00, 01,...,12", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();

        }
        // When the entered information is enough to edit the schedule
        else if (billboardChooser.getSelectedItem() != null && !billboardChooser.getSelectedItem().equals("") &&
                hourbox.getText() != null && !hourbox.getText().equals("")
                && minbox.getText() != null && !minbox.getText().equals("")) {

            socketStart();
            EditScheduleBillboard esbr = new EditScheduleBillboard(sessionToken, String.valueOf(billboardChooser.getSelectedItem()), creatorbox.getText(), year1, month, date, hourbox.getText(), minute,
                    minbox.getText(),duHrbox.getText(), duMinbox.getText(), recurbox.getText());
            oos.writeObject(esbr);
            oos.flush();
            Object trans = ois.readObject();
            if (trans instanceof AcknowledgeReply) {
                AcknowledgeReply reply = (AcknowledgeReply) trans;
                System.out.println(reply.getAcknowledgement());
            }
            socketStop();
            // Remove all elements of list for next dialog
            model.removeAllElements();
            bg.dispose();
        }
    }

    /**
     * When the delete button is pressed
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void deletePressed() throws IOException, ClassNotFoundException {
        // When the selected information is enough to delete the schedule
        if ((billboardChooser.getSelectedItem() != null && !billboardChooser.getSelectedItem().equals("") &&
                hourbox.getText() != null && !hourbox.getText().equals("")
                && minbox.getText() != null && !minbox.getText().equals("")
                && duHrbox.getText() != null && !duHrbox.getText().equals("")
                && duMinbox.getText() != null && !duMinbox.getText().equals(""))) {
            // Check the edited version is same with one of the scheduled data from the database
            if (String.valueOf(billboardChooser.getSelectedItem()).equals(nameList.getSelectedValue().toString().split(" ")[0]) && hourbox.getText().equals(nameList.getSelectedValue().toString().split(" ")[2])
                    && minbox.getText().equals(nameList.getSelectedValue().toString().split(" ")[4]) && duHrbox.getText().equals(nameList.getSelectedValue().toString().split(" ")[6])
                    && duMinbox.getText().equals(nameList.getSelectedValue().toString().split(" ")[8])) {
                socketStart();
                RemoveBillboardRequest removeBillboardRequest = new RemoveBillboardRequest(String.valueOf(billboardChooser.getSelectedItem()), sessionToken, month, date, hourbox.getText(), minbox.getText());
                oos.writeObject(removeBillboardRequest);
                oos.flush();
                Object trans = ois.readObject();
                if (trans instanceof AcknowledgeReply) {
                    AcknowledgeReply reply = (AcknowledgeReply) trans;
                    System.out.println(reply.getAcknowledgement());
                }
                socketStop();
                model.removeAllElements();
                bg.dispose();
            }
            // Otherwise,
            else {
                showMessageDialog(null, "Cannot delete this as this schedule has not been set", "Title", ERROR_MESSAGE);
                clearFields();
                bg.dispose();
            }
        }
        // Otherwise, if some text boxes are empty
        else {
            showMessageDialog(null, "Cannot be null", "Title", ERROR_MESSAGE);
            clearFields();
            bg.dispose();
        }
    }

    /**
     * Set to start the socket for server
     *
     * @throws IOException
     */
    public void socketStart() throws IOException {
        socket = new Socket(host, Integer.parseInt(port));
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
    }

    /**
     * Stop the socket
     *
     * @throws IOException
     */
    public void socketStop() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }
}
