package cab302.controlpanel.gui;

import cab302.controlpanel.data.UserData;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserLoginUI extends JFrame implements ActionListener, KeyListener{
    public static final int WIDTH = 350;
    public static final int HEIGHT = 200;

    private JButton btnLogin;
    private JButton btnCancel;
    private JTextField username;
    private JPasswordField password;

    UserData data;

    public UserLoginUI(UserData data){
        super("Login Page");
        this.data = data;
        setSize(WIDTH, HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.GRAY);
        this.add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.GRAY);
        this.add(btnPanel, BorderLayout.SOUTH);

        btnLogin = new JButton();
        btnLogin.setText("Login");
        btnLogin.addActionListener(this);

        btnCancel = new JButton();
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(this);

        JLabel lblUsername = new JLabel("Username   :");
        JLabel lblPassword = new JLabel("Password   :");

        username = new JTextField(15);
        username.addKeyListener(this);

        password  = new JPasswordField(15);
        password.addKeyListener(this);

        addToPanel(panel, lblUsername, 0,0,2,1);
        addToPanel(panel, username, 10, 0,2,1);
        addToPanel(panel, lblPassword, 0,1,2,1);
        addToPanel(panel,password,10,1,2,1);
        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);

        setVisible(true);
    }

    private void addToPanel(JPanel jp,Component c,int x, int y, int w, int h) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 5, 10, 5);
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == btnLogin)
        {
            String name = username.getText();
            String pass = String.valueOf(password.getPassword());
            if(data.isValidUser(name, pass) == true)
            {
                System.out.println(data.isValidUser(name, pass));
                //HomeUI GUI = new HomeUI(data);
                //GUI.setVisible(true);
                //dispose();
            }
            else
            {
                System.out.println(data.isValidUser(name, pass));
                username.setBorder(new LineBorder(Color.RED));
                password.setBorder(new LineBorder(Color.RED));
                JOptionPane.showMessageDialog(this,"Invalid username or password",
                        "Error", JOptionPane.WARNING_MESSAGE);
            }

        }
        else if(src == btnCancel)
        {
            username.setText("");
            password.setText("");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnLogin.doClick();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}

