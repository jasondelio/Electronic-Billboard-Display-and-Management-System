package cab302.ControlPanel;

import cab302.database.user.UserData;
import cab302.server.Billboardserver.AlreadyLoginReply;
import cab302.server.Billboardserver.LoginReply;
import cab302.server.Billboardserver.sessionExistReply;
import cab302.server.WillBeControlPanelAction.Loginrequest;
import cab302.server.WillBeControlPanelAction.UserLoggedInrequest;
import cab302.server.WillBeControlPanelAction.sessionExistRequest;

import javax.swing.*;
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
import java.util.HashMap;

public class UserLoginUI extends JFrame implements ActionListener, KeyListener {
    public static final int WIDTH = 350;
    public static final int HEIGHT = 200;

    private JButton btnLogin;
    private JButton btnCancel;
    private JTextField username;
    private JPasswordField password;

    HashMap<Date, String> SessionTokensTimers = new HashMap<>();
    UserData data;
    Socket socket;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    String sessionToken;
    Date loggedinTime;
    boolean alreadylogin;
    public UserLoginUI() throws IOException, ClassNotFoundException {
//        this.setTitle("User Login");
//        setSize(WIDTH, HEIGHT);
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLayout(new BorderLayout());
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        socketStart();
        sessionExistRequest ser = new sessionExistRequest("get session token");
        oos.writeObject(ser);
        oos.flush();
        Object trans = ois.readObject();
        if (trans instanceof sessionExistReply) {
            sessionExistReply reply = (sessionExistReply) trans;
            alreadylogin = reply.isLoginAlready();
            System.out.println(alreadylogin);
            if (alreadylogin == true){
                sessionToken = reply.getSessiontokens().get(0);
            }

        }
        socketStop();
        if (alreadylogin == true){
            socketStart();
            UserLoggedInrequest userLoggedInrequest = new UserLoggedInrequest(sessionToken);
            oos.writeObject(userLoggedInrequest);
            oos.flush();
            loggedinTime = new Date();
            Object transoO = ois.readObject();
            if (transoO instanceof AlreadyLoginReply) {
                AlreadyLoginReply reply = (AlreadyLoginReply) transoO;
                ArrayList<String > permlists = reply.getPermissionsList();
                String loggedinuser = reply.getLoggedInUsername();

                HomeUI GUI = new HomeUI(sessionToken,permlists,loggedinuser, loggedinTime,0);
                GUI.setVisible(true);
                socketStop();
                dispose();
                setVisible(false);
                setUndecorated(true);
            }
        }else{

            this.setTitle("User Login");
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
    public void actionPerformed(ActionEvent e){
        Object src = e.getSource();
        if(src == btnLogin)
        {
            try {
                processInformation();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }
        else if(src == btnCancel)
        {
            username.setText("");
            password.setText("");
        }
    }
    public void processInformation() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        socketStart();
        String username = this.username.getText();
        String password1 = String.valueOf(password.getPassword());
        String hashePass = getHashedPass(password1);

        Loginrequest loginrequest = new Loginrequest(username,hashePass);
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
                loggedinTime = new Date();
                SessionTokensTimers.put(loggedinTime,sessionToken);
                System.out.println("Success to log in, recieve the token " + sessionToken);
                permlists = reply.getPermissionsList();
                System.out.println(permlists);
                HomeUI GUI = new HomeUI(sessionToken,permlists,loggedinuser, loggedinTime,0);
                GUI.setVisible(true);
                socketStop();
                dispose();

            } else {
                System.out.println("fail to login");
            }
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
    public static String getHashedPass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] temp_byte = md.digest(password.getBytes());
        String hashedPassword= bytesToString(temp_byte);

        return hashedPassword;
    }

    public static String bytesToString(byte[] hash){
        StringBuffer str_buff = new StringBuffer();
        for (byte b : hash){
            str_buff.append(String.format("%02x", b & 0xFF));
        }
        return str_buff.toString();
    }

    public void socketStart() throws IOException{
        socket = new Socket("localhost",12345);
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
}

