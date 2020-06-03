package cab302.ControlPanel;

import cab302.server.ReplyToApplications.AcknowledgeReply;
import cab302.server.ReplyToApplications.AlreadyLoginReply;
import cab302.server.ReplyToApplications.LoginReply;
import cab302.server.ReplyToApplications.SessionExistReply;
import cab302.server.ApplicationsRequests.Loginrequest;
import cab302.server.ApplicationsRequests.UserLoggedInrequest;
import cab302.server.ApplicationsRequests.SessionExistRequest;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * Initiates user login's user interface for Billboard Control Panel application.
 */
public class UserLoginUI extends JFrame implements ActionListener, KeyListener {
    public static final int WIDTH = 350;
    public static final int HEIGHT = 200;

    private JButton btnLogin;
    private JButton btnCancel;
    private JTextField username;
    private JPasswordField password;

    Socket socket;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    String sessionToken;
    boolean isAlreadyLogin;
    private String port;
    private String host;
//    private Date loginTime;


    /**
     * Constructor sets up the user interface and displays
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public UserLoginUI() throws IOException, ClassNotFoundException {
        //if the user is already login before and the session token hasn't expired
        getPropValues();
        socketStart();
        SessionExistRequest ser = new SessionExistRequest("get session token");
        oos.writeObject(ser);
        oos.flush();
        Object trans = ois.readObject();
        if (trans instanceof SessionExistReply) {
            SessionExistReply reply = (SessionExistReply) trans;
            isAlreadyLogin = reply.isLoginAlready();
            if (isAlreadyLogin == true) {
                sessionToken = reply.getSessiontokens().get(0);
            }

        }
        socketStop();
        if (isAlreadyLogin == true) {
            socketStart();
            UserLoggedInrequest userLoggedInrequest = new UserLoggedInrequest(sessionToken);
            oos.writeObject(userLoggedInrequest);
            oos.flush();
            Object transoO = ois.readObject();
            if (transoO instanceof AlreadyLoginReply) {
                AlreadyLoginReply reply = (AlreadyLoginReply) transoO;
                ArrayList<String> permlists = reply.getPermissionsList();
                String loggedinuser = reply.getLoggedInUsername();
//                LocalDateTime loginTime = LocalDateTime.parse(reply.getLogintime());
                HomeUI homeUI = new HomeUI(sessionToken, permlists, loggedinuser, 0);
                homeUI.setVisible(true);
                socketStop();
                dispose();
                setVisible(false);
                setUndecorated(true);
            }else if(transoO instanceof AcknowledgeReply){
                socketStop();
                setTitle("User Login");
                setSize(WIDTH, HEIGHT);
                initUI();
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setVisible(true);
            }
        }
        //Show the user login user interface if the user hasn't login before
        else {

            setTitle("User Login");
            setSize(WIDTH, HEIGHT);
            initUI();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }

    }

    /**
     * Makes the plain text password to the hashed password
     *
     * @param password The plain text password
     * @return The hashed password
     * @throws NoSuchAlgorithmException
     */
    public static String getHashedPass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] temp_byte = md.digest(password.getBytes());
        String hashedPassword = bytesToString(temp_byte);

        return hashedPassword;
    }

    /**
     * Makes the bytes from hashed password to string
     *
     * @param hash the bytes of hashed password
     * @return hashed password after be converted to string
     */
    public static String bytesToString(byte[] hash) {
        StringBuffer str_buff = new StringBuffer();
        for (byte b : hash) {
            str_buff.append(String.format("%02x", b & 0xFF));
        }
        return str_buff.toString();
    }

    /**
     * Set the frame to BorderLayout and places all the component to it. The username and password
     * will be place in GridBagLayout panel, and btnLogin will be placed in FlowLayout panel, and all
     * created panel will be set into the frame.
     */
    private void initUI() {
        this.setLayout(new BorderLayout());
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

        password = new JPasswordField(15);
        password.addKeyListener(this);


        addToPanel(panel, lblUsername, 0, 0, 2, 1);
        addToPanel(panel, username, 10, 0, 2, 1);
        addToPanel(panel, lblPassword, 0, 1, 2, 1);
        addToPanel(panel, password, 10, 1, 2, 1);
        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);

    }

    /**
     * Adds the GridBagConstraints to a JPanel
     *
     * @param jp A JPanel that wants to be added the GridBagConstraints
     * @param c  A component placed on the JPanel
     * @param x  GridX constraint value
     * @param y  GridY constraint value
     * @param w  Gridwidth constraint value
     * @param h  Gridheight constraint value
     */
    private void addToPanel(JPanel jp, Component c, int x, int y, int w, int h) {
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

    /**
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnLogin) {
            try {
                processInformation();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        } else if (src == btnCancel) {
            username.setText("");
            password.setText("");
        }
    }

    /**
     * Checks the username and password input are valid or not, if valid
     * the user will be redirected to Home user interface
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void processInformation() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        socketStart();
        String password1 = String.valueOf(password.getPassword());
        String hashePass = getHashedPass(password1);
        Date now = new Date();

        Loginrequest loginrequest = new Loginrequest(username.getText(), hashePass, now);
        oos.writeObject(loginrequest);
        oos.flush();

        Object transoO = ois.readObject();
        ArrayList<String> permlists = new ArrayList<String>();
        String loggedinuser = null;
        if (transoO instanceof LoginReply) {
            LoginReply reply = (LoginReply) transoO;
            if (reply.isLoginSucceed()) {
                sessionToken = reply.getSessionToken();
                loggedinuser = reply.getLoggedInUsername();

                System.out.println("Success to log in, recieve the token " + sessionToken);
                permlists = reply.getPermissionsList();
                System.out.println(permlists);
                HomeUI GUI = new HomeUI(sessionToken,permlists,loggedinuser,0);
                GUI.setVisible(true);
                socketStop();
                dispose();

            } else {
                username.setBorder(new LineBorder(Color.RED));
                password.setBorder(new LineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Invalid username or password",
                        "Error", JOptionPane.WARNING_MESSAGE);
                System.out.println("fail to login");
            }
        }


    }

    /**
     * @see KeyListener#keyTyped(KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * @see KeyListener#keyPressed(KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnLogin.doClick();
        }

    }

    /**
     * @see KeyListener#keyReleased(KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Creates a new connection to socket to make request
     *
     * @throws IOException
     */
    public void socketStart() throws IOException {
        socket = new Socket(host,Integer.parseInt(port));
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
    }

    /**
     * Stop the connection to socket after request and getting the reply
     *
     * @throws IOException
     */
    public void socketStop() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }
    public void getPropValues() throws IOException {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("src/main/cab302/network.props");
            props.load(in);
            in.close();
            // get the property value and print it out
            host = props.getProperty("host");
            port = props.getProperty("port");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}

