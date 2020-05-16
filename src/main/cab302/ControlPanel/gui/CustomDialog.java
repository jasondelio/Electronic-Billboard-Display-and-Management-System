package cab302.controlpanel.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomDialog extends JDialog implements ActionListener {

    private JButton confirm;
    private JPanel pnl;
    private JFrame bg;
    private JTextField namebox;
    private JTextField hourbox;
    private JTextField minbox;

    private JLabel lblName;
    private JLabel lblselectedDate;
    private JLabel lblTime;
    private JLabel lblHour;
    private JLabel lblMin;

    private String name;
    private Integer scheduledMonth;
    private Integer scheduledDate;
    private Integer scheduledHour;
    private Integer scheduledMin;


    public CustomDialog(String info) {
        bg = new JFrame();
        pnl = new JPanel();
        setTitle("Scheduling");
        pnl.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // name
        lblName = new JLabel("Billboard name");
        gbc.gridx = 1;
        gbc.gridy = 1;

        pnl.add(lblName, gbc);

        namebox = new JTextField(20);
        gbc.gridwidth = 3;
        gbc.gridx = 2;
        gbc.gridy = 1;

        pnl.add(namebox, gbc);
        // time

        lblTime = new JLabel("Enter time");
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 2;

        pnl.add(lblTime, gbc);

        hourbox = new JTextField(5);
        gbc.gridwidth = 1;
        gbc.gridx = 2;
        gbc.gridy = 2;

        pnl.add(hourbox, gbc);

        lblHour = new JLabel("hr");
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        gbc.gridy = 2;

        pnl.add(lblHour, gbc);

        minbox = new JTextField(5);
        gbc.gridwidth = 1;
        gbc.gridx = 4;
        gbc.gridy = 2;

        pnl.add(minbox, gbc);

        lblMin = new JLabel("min");
        gbc.gridwidth = 1;
        gbc.gridx = 5;
        gbc.gridy = 2;

        pnl.add(lblMin, gbc);

        confirm = new JButton("Confirm");
        gbc.gridwidth = 2;
        gbc.gridx = 2;
        gbc.gridy = 3;

        pnl.add(confirm, gbc);

        bg.add(pnl);
        bg.pack();

        confirm.addActionListener(this);

        bg.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}
