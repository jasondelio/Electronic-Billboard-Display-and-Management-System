package cab302.viewer.gui;

import cab302.database.billboard.BillboardInfo;
import cab302.server.ApplicationsRequests.ViewerRequest;
import cab302.server.ReplyToApplications.ViewerReply;
import cab302.viewer.exceptions.BadImageFormatException;
import cab302.viewer.exceptions.MalformedHexadecimalColourException;
import cab302.viewer.util.XMLParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

import static cab302.viewer.util.HexToRGB.HexToRGB;

public class Gui extends JFrame {

    private JFrame frame;

    private static String port;
    private String host;

    Socket socket;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    /**
     * Constructor to assemble the GUI
     */
    public Gui() throws IOException, ClassNotFoundException {
        // get the current scheduled billboard info from server
        getPropValues();

        BillboardInfo billBoardInfo = new BillboardInfo();

        socketStart();
        ViewerRequest viewerRequest = new ViewerRequest("Get current time shcedule");
        oos.writeObject(viewerRequest);
        oos.flush();
        Object reply_ois = ois.readObject();
        if (reply_ois instanceof ViewerReply){
            ViewerReply reply = (ViewerReply) reply_ois;
            billBoardInfo = reply.getBillboardInfo();
        }
        socketStop();
        // enter current billboard Information
        XMLParser parser = new XMLParser(billBoardInfo);
        HashMap<String, String> xmlInfo = parser.parseXML();

        // Window Configuration
        // Set window to fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        String bgString = xmlInfo.getOrDefault("bgColour", "#FFFFFF");
        Color bgColour;
        try {
            bgColour = HexToRGB(bgString);
        } catch (MalformedHexadecimalColourException e) {
            bgColour = new Color(255,255,255);
        }
        getContentPane().setBackground(bgColour);

        // Set screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Set layout and default closing
        GridBagLayout layout = new GridBagLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(layout);

        // Utilities and Actions
        // Get close action
        Action close = new CloseAction("close", "Closes the app", KeyEvent.VK_ESCAPE);

        // Setup gridbag constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1;
        gbc.weightx = 1;


        // Input Handling
        // Set up root pane to handle inputs
        JRootPane root = this.getRootPane();
        root.getActionMap().put("CLOSE_APPLICATION", close);
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "CLOSE_APPLICATION");
        root.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Frame frame : Frame.getFrames()) {
                    frame.dispose();
                    RunViewer run = new RunViewer();
                    RunViewer.time.cancel();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });


        // XML display
        // Create a display assembler
        DisplayAssembler displayAssembler = new DisplayAssembler(xmlInfo, screenSize);
        // Initialise all as null
        JTextPane titleText = null;
        JTextPane informationText = null;
        JLabel pictureLabel = null;
        // Attempt to create components, fail quietly if Exception occurs
        try {
            titleText = displayAssembler.assembleMessagePane(bgColour);
            informationText = displayAssembler.assembleInformationPane(bgColour);
            pictureLabel = displayAssembler.assemblePictureLabel();
        } catch (MalformedHexadecimalColourException | BadImageFormatException | IOException ignored) {}

        // Add components to JFrame if not null
        if (titleText != null) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(titleText, gbc);
        }

        if (pictureLabel != null) {
            gbc.gridy = 1;
            add(pictureLabel, gbc);
        }

        if (informationText != null) {
            gbc.gridy = 2;
            add(informationText, gbc);
        }

        // Show window
        setVisible(true);
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