package cab302.ControlPanel;

import cab302.database.user.UserInfo;
import cab302.server.Billboardserver.*;
import cab302.server.WillBeControlPanelAction.*;
import cab302.viewer.exceptions.BadImageFormatException;
import cab302.viewer.gui.ImageGenerator;
import cab302.viewer.util.XMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import static cab302.viewer.util.HexToRGB.HexToRGB;

public class HomeUI extends JFrame implements ActionListener {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private Color initialColor = Color.BLACK;
    private JList usernameList;
    private JList billboardList;
    private JButton btnNewUser;
    private JButton btnEditUser;
    private JButton btnDeleteUser;
    private JButton btnSaveNewUser;
    private JButton btnResetNewUser;
    private JButton btnUpdateUser;
    private JButton btnUpdateCurrentUser;
    private JButton btnCreateNewBillboard;
    private JButton btnImportBillboard;
    private JButton btnEditBillboard;
    private JButton btnPreviewBillboard;
    private JButton btnDeleteBillboard;
    private JButton btnExportBillboard;
    private JButton btnBackgroundColour;
    private JButton btnMessageColour;
    private JButton btnInformationColour;
    private JButton btnUploadPicture;
    private JButton btnSaveNewBillboard;
    private JButton btnResetNewBillboard;
    private JButton btnPreviewNewBillboard;
    private JButton btnSaveImportedBillboard;
    private JButton btnSaveEditedBillboard;
    private JButton btnPreviewEditedBillboard;
    private JButton btnResetEditedBillboard;
    private JButton btnEditProfile;
    private JButton btnLogout;
    private JTextField name;
    private JTextField username;
    private JTextField password;
    private JTextField email;
    private JTextField creator;
    private JScrollPane scroller;
    private JDialog addNewUserDialog;
    private JDialog editUserDialog;
    private JDialog createNewBillboardDialog;
    private JDialog importBillboardDialog;
    private JDialog previewBillboardDialog;
    private JDialog editBillboardDialog;
    private JTextField message;
    private JTextField picture;
    private JTextField information;
    private JTextField backgroundColour;
    private JTextField messageColour;
    private JTextField informationColour;
    private JTextField billboardName;
    private JCheckBox cbUrl;
    private JCheckBox cbCreateBillboardsPermission;
    private JCheckBox cbEditAllBillboardsPermission;
    private JCheckBox cbScheduleBillboardsPermission;
    private JCheckBox cbEditUsersPermission;
    private JPanel panelEditUsers;
    private JTabbedPane pane;
    private int r;
    private int g;
    private int b;
    private String hexColour;
    private String XMLContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    private String content;


    //UserData data;
    Socket socket;
    String sessionToken;
    String currentUsername;
    Date loggedinTime;
    ArrayList<String> permissionsList;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public HomeUI(String sessiontoken, ArrayList<String> perm_lists, String loggedInuser, Date date, int currentTab) throws IOException, ClassNotFoundException {
        super("Billboard Control Panel");
        sessionToken = sessiontoken;
        permissionsList = perm_lists;
        currentUsername = loggedInuser;
        loggedinTime = date;
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);
        Date currentdate = new Date();
        long diff = currentdate.getTime() - loggedinTime.getTime();
        long diffHours = diff/(60 * 60 * 1000);
        if (diffHours >= 24){
            logout();
        }

        pane = new JTabbedPane();

        JPanel panelHome = new JPanel(new BorderLayout());
        JLabel lblWelcome = new JLabel("Welcome to Billboard Control Panel !");
        lblWelcome.setHorizontalAlignment(JLabel.CENTER);
        lblWelcome.setFont(lblWelcome.getFont().deriveFont(24.0f));
        btnLogout = new JButton("Logout");
        btnLogout.addActionListener(this);
        btnEditProfile = new JButton("Edit profile");
        btnEditProfile.addActionListener(this);
        JPanel editProfilePanel = new JPanel();
        editProfilePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        editProfilePanel.add(btnLogout);
        panelHome.add(lblWelcome, BorderLayout.CENTER);
        panelHome.add(btnEditProfile, BorderLayout.SOUTH);
        panelHome.add(editProfilePanel, BorderLayout.NORTH);


        JPanel panelListBillboards = new JPanel();
        panelListBillboards.add(new JLabel("List Billboards"));


        JPanel panelScheduleBillboards = new JPanel();
        panelScheduleBillboards.add(new JLabel("Schedule Billboards.."));

        pane.add("Home", panelHome);
        pane.add("List Billboards", makePanelListBillboards());
        pane.add("Create Billboards", makePanelCreateBillboards());
        pane.add("Edit Users", makePanelEditUsers());
        pane.add("Schedule Billboards", panelScheduleBillboards);

        this.add(pane, BorderLayout.CENTER);
        pane.setSelectedIndex(currentTab);
        disableFeatureBasedOnPermissions();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private void disableFeatureBasedOnPermissions() {
        if (!permissionsList.get(0).equals("true")) {
            pane.setEnabledAt(2, false);
        }
        if (!permissionsList.get(2).equals("true")) {
            pane.setEnabledAt(4, false);
        }
        if (!permissionsList.get(3).equals("true")) {
            pane.setEnabledAt(3, false);
        }
    }

    private JPanel makePanelCreateBillboards() {
        JPanel panelCreateBillboards = new JPanel();
        panelCreateBillboards.setLayout(new BorderLayout());
        panelCreateBillboards.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCreateBillboards.add(makeButtonsPanelCreateBillboards(), BorderLayout.CENTER);
        return panelCreateBillboards;
    }

    private JPanel makePanelEditUsers() throws IOException, ClassNotFoundException {
        panelEditUsers = new JPanel();
        panelEditUsers.setLayout(new BorderLayout());
        socketStart();
        listUsersRequest liur = new listUsersRequest(sessionToken);

        oos.writeObject(liur);
        oos.flush();

        Object transo = ois.readObject();
        if (transo instanceof listUsersReply) {
            listUsersReply reply = (listUsersReply) transo;
            usernameList = new JList(reply.getListOfUsers());
        }
        //usernameList = new JList(userData.getModel());
        socketStop();
        scroller = new JScrollPane(usernameList);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelEditUsers.add(scroller, BorderLayout.CENTER);
        panelEditUsers.add(makeButtonsPanelEditUsers(), BorderLayout.SOUTH);
        return panelEditUsers;
    }
    private JPanel makePanelListBillboards() throws IOException, ClassNotFoundException {
        JPanel panelListBillboards = new JPanel();
        panelListBillboards.setLayout(new BorderLayout());
        socketStart();
        listBillboardRequest listBillboardRequest = new listBillboardRequest(sessionToken);
        oos.writeObject(listBillboardRequest);
        oos.flush();

        Object transoO = ois.readObject();
        if (transoO instanceof listBillboardReply) {
            listBillboardReply reply = (listBillboardReply) transoO;
            billboardList = new JList(reply.getListofBillboards());
        }
        socketStop();
        scroller = new JScrollPane(billboardList);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelListBillboards.add(scroller, BorderLayout.CENTER);
        panelListBillboards.add(makeButtonsPanelListBillboards(), BorderLayout.SOUTH);
        return panelListBillboards;
    }
    private JPanel makeButtonsPanelCreateBillboards()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        btnCreateNewBillboard = new JButton("Create a new billboard");
        btnCreateNewBillboard.addActionListener(this);
        btnImportBillboard = new JButton("Import billboard");
        btnImportBillboard.addActionListener(this);
        btnCreateNewBillboard.setHorizontalAlignment(JButton.CENTER);
        btnImportBillboard.setHorizontalAlignment(JButton.CENTER);
        btnCreateNewBillboard.setAlignmentX(CENTER_ALIGNMENT);
        btnImportBillboard.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(Box.createVerticalStrut(200));
        buttonPanel.add(btnCreateNewBillboard);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(btnImportBillboard);

        return buttonPanel;
    }
    private JPanel makeButtonsPanelEditUsers() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        btnNewUser = new JButton("New");
        btnNewUser.addActionListener(this);
        btnEditUser = new JButton("Edit");
        btnEditUser.addActionListener(this);
        btnDeleteUser = new JButton("Delete");
        btnDeleteUser.addActionListener(this);
        buttonPanel.add(Box.createHorizontalStrut(150));
        buttonPanel.add(btnNewUser);
        buttonPanel.add(Box.createHorizontalStrut(50));
        buttonPanel.add(btnEditUser);
        buttonPanel.add(Box.createHorizontalStrut(50));
        buttonPanel.add(btnDeleteUser);
        buttonPanel.add(Box.createHorizontalStrut(150));

        return buttonPanel;
    }
    private JPanel makeButtonsPanelListBillboards()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        btnEditBillboard = new JButton("Edit");
        btnEditBillboard.addActionListener(this);
        btnPreviewBillboard = new JButton("Preview");
        btnPreviewBillboard.addActionListener(this);
        btnDeleteBillboard = new JButton("Delete");
        btnDeleteBillboard.addActionListener(this);
        btnExportBillboard = new JButton("Export");
        btnExportBillboard.addActionListener(this);
        buttonPanel.add(Box.createHorizontalStrut(150));
        buttonPanel.add(btnEditBillboard);
        buttonPanel.add(Box.createHorizontalStrut(50));
        buttonPanel.add(btnPreviewBillboard);
        buttonPanel.add(Box.createHorizontalStrut(50));
        buttonPanel.add(btnDeleteBillboard);


        return buttonPanel;
    }

    private JPanel makeCreateBillboardFieldsPanel(){
        JPanel createBillboardFieldsPanel = new JPanel();
        GroupLayout layout = new GroupLayout(createBillboardFieldsPanel);
        createBillboardFieldsPanel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel lblBackground = new JLabel("Background");
        JLabel lblMessage = new JLabel("Message");
        JLabel lblPicture = new JLabel("Picture");
        JLabel lblInformation = new JLabel("Information");
        JLabel lblCreator = new JLabel("Creator");

        message = new JTextField(20);
        picture = new JTextField(20);
        picture.setEnabled(false);
        information = new JTextField(20);
        backgroundColour = new JTextField(10);
        backgroundColour.setEnabled(false);
        messageColour = new JTextField(10);
        messageColour.setEnabled(false);
        informationColour = new JTextField(10);
        informationColour.setEnabled(false);
        creator = new JTextField(20);
        creator.setEditable(false);

        btnBackgroundColour = new JButton("Colour");
        btnBackgroundColour.addActionListener(this);
        btnMessageColour = new JButton("Colour");
        btnMessageColour.addActionListener(this);
        btnInformationColour = new JButton("Colour");
        btnInformationColour.addActionListener(this);
        btnUploadPicture = new JButton("Upload");
        btnUploadPicture.addActionListener(this);
        cbUrl = new JCheckBox("URL");
        cbUrl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbUrl.isSelected()) {
                    picture.setEnabled(true);
                    btnUploadPicture.setEnabled(false);
                }
                else{
                    picture.setText("");
                    picture.setEnabled(false);
                    btnUploadPicture.setEnabled(true);
                }
            }
        });


        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup().addComponent(lblBackground).addComponent(lblMessage)
                .addComponent(lblPicture).addComponent(lblInformation).addComponent(lblCreator));
        hGroup.addGroup(layout.createParallelGroup().addComponent(message)
                .addComponent(picture).addComponent(information).addComponent(creator));
        hGroup.addGroup(layout.createParallelGroup().addComponent(btnBackgroundColour)
                .addComponent(btnMessageColour).addComponent(btnUploadPicture).addComponent(btnInformationColour));
        hGroup.addGroup(layout.createParallelGroup().addComponent(backgroundColour).addComponent(messageColour)
                .addComponent(cbUrl).addComponent(informationColour));
        layout.setHorizontalGroup(hGroup);


        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblBackground).addComponent(btnBackgroundColour).addComponent(backgroundColour));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMessage).addComponent(message).addComponent(btnMessageColour)
                .addComponent(messageColour));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblPicture).addComponent(picture).addComponent(btnUploadPicture).addComponent(cbUrl));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblInformation).addComponent(information).addComponent(btnInformationColour)
                .addComponent(informationColour));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
                .addComponent(lblCreator).addComponent(creator);
        layout.setVerticalGroup(vGroup);

        return createBillboardFieldsPanel;
    }
    private JPanel makeUserFieldsPanel() {
        JPanel userFieldsPanel = new JPanel();
        GroupLayout layout = new GroupLayout(userFieldsPanel);
        userFieldsPanel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel lblName = new JLabel("Name");
        JLabel lblUsername = new JLabel("Username");
        JLabel lblPassword = new JLabel("Password");
        JLabel lblEmail = new JLabel("Email");
        JLabel lblPermission = new JLabel("Permission :");

        name = new JTextField(20);
        username = new JTextField(20);
        password = new JTextField(20);
        email = new JTextField(20);
        cbCreateBillboardsPermission = new JCheckBox("Create Billboard");
        cbEditAllBillboardsPermission = new JCheckBox("Edit All Billboards");
        cbScheduleBillboardsPermission = new JCheckBox("Schedule Billboards");
        cbEditUsersPermission = new JCheckBox("Edit Users");


        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();


        hGroup.addGroup(layout.createParallelGroup().addComponent(lblName)
                .addComponent(lblUsername).addComponent(lblPassword).addComponent(
                        lblEmail).addComponent(lblPermission)
                .addComponent(cbCreateBillboardsPermission).addComponent(cbScheduleBillboardsPermission));
        hGroup.addGroup(layout.createParallelGroup().addComponent(name)
                .addComponent(username).addComponent(password).addComponent(email)
                .addComponent(email).addComponent(cbEditAllBillboardsPermission).addComponent(cbEditUsersPermission));
        layout.setHorizontalGroup(hGroup);


        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();


        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblName).addComponent(name));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblUsername).addComponent(username));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblPassword).addComponent(password));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblEmail).addComponent(email));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblPermission));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(cbCreateBillboardsPermission).addComponent(cbEditAllBillboardsPermission));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(cbScheduleBillboardsPermission).addComponent(cbEditUsersPermission));
        layout.setVerticalGroup(vGroup);

        return userFieldsPanel;
    }
    private void createNewBillboardPressed()
    {
        createNewBillboardDialog = new JDialog(this, "Create new billboard");
        createNewBillboardDialog.setSize(430, 300);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        createNewBillboardDialog.setLocation(dim.width / 2 - 215, dim.height / 2 - 150);
        JPanel createNewBillboardPanel = new JPanel();
        createNewBillboardPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        btnSaveNewBillboard = new JButton("Save");
        btnSaveNewBillboard.addActionListener(this);
        btnPreviewNewBillboard = new JButton("Preview");
        btnPreviewNewBillboard.addActionListener(this);
        btnResetNewBillboard = new JButton("Reset");
        btnResetNewBillboard.addActionListener(this);
        btnExportBillboard = new JButton("Export");
        btnExportBillboard.addActionListener(this);
        buttonPanel.add(Box.createHorizontalStrut(25));
        buttonPanel.add(btnSaveNewBillboard);
        buttonPanel.add(Box.createHorizontalStrut(25));
        buttonPanel.add(btnPreviewNewBillboard);
        buttonPanel.add(Box.createHorizontalStrut(25));
        buttonPanel.add(btnResetNewBillboard);
        buttonPanel.add(Box.createHorizontalStrut(25));
        buttonPanel.add(btnExportBillboard);
        JPanel panelBillboardName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBillboardName = new JLabel("Billboard Name");
        billboardName = new JTextField(30);
        panelBillboardName.add(lblBillboardName);
        panelBillboardName.add(billboardName);
        createNewBillboardPanel.add(panelBillboardName, BorderLayout.NORTH);
        createNewBillboardPanel.add(makeCreateBillboardFieldsPanel(), BorderLayout.CENTER);
        creator.setText(currentUsername);
        createNewBillboardDialog.add(createNewBillboardPanel);
        createNewBillboardDialog.add(buttonPanel, BorderLayout.SOUTH);
        createNewBillboardDialog.setVisible(true);
    }

    private void importBillboardPressed()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Documents", "xml"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            importBillboardDialog = new JDialog(this, "Billboard name");
            importBillboardDialog.setSize(300, 100);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            importBillboardDialog.setLocation(dim.width / 2 - 150, dim.height / 2 - 50);
            JPanel setBillboardNamePanel = new JPanel();
            setBillboardNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel lblBillboardName = new JLabel("Billboard Name");
            btnSaveImportedBillboard = new JButton("Import");
            btnSaveImportedBillboard.addActionListener(this);
            billboardName = new JTextField(15);
            setBillboardNamePanel.add(lblBillboardName);
            setBillboardNamePanel.add(billboardName);
            importBillboardDialog.add(setBillboardNamePanel, BorderLayout.CENTER);
            importBillboardDialog.add(btnSaveImportedBillboard, BorderLayout.SOUTH);
            importBillboardDialog.setVisible(true);

            String temp = "";
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    temp = temp + line + "\n";
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            XMLContents = temp;
        }
    }
    private void parseXMLContentsFromDatabase(String billboardContent)
    {
        content = billboardContent;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(content));

            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("billboard");

            for (int i = 0; i < nodes.getLength(); i++) {
                Node nNode = nodes.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Element messageElement = (Element) eElement.getElementsByTagName("message").item(0);
                    Element pictureElement = (Element) eElement.getElementsByTagName("picture").item(0);
                    Element informationElement = (Element) eElement.getElementsByTagName("information").item(0);
                    backgroundColour.setText(eElement.getAttribute("background"));
                    if(messageElement != null)
                    {
                        messageColour.setText(messageElement.getAttribute("colour"));
                        message.setText(messageElement.getTextContent());
                    }
                    if(informationElement != null)
                    {
                        informationColour.setText(informationElement.getAttribute("colour"));
                        information.setText(informationElement.getTextContent());
                    }
                    if(pictureElement != null)
                    {
                        if(!pictureElement.getAttribute("url").equals(""))
                        {
                            btnUploadPicture.setEnabled(false);
                            cbUrl.setSelected(true);
                            picture.setEnabled(true);
                            picture.setText(pictureElement.getAttribute("url"));
                        }
                        if(!pictureElement.getAttribute("data").equals(""))
                        {
                            picture.setText(pictureElement.getAttribute("data"));
                        }
                    }
                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editBillboardPressed() throws IOException, ClassNotFoundException {
        if(billboardList.getSelectedValue() != null) {
            editBillboardDialog = new JDialog(this, "Edit billboard");
            editBillboardDialog.setSize(430, 300);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            editBillboardDialog.setLocation(dim.width / 2 - 215, dim.height / 2 - 150);
            JPanel editBillboardPanel = new JPanel();
            editBillboardPanel.setLayout(new BorderLayout());
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            btnSaveEditedBillboard = new JButton("Save");
            btnSaveEditedBillboard.addActionListener(this);
            btnPreviewEditedBillboard = new JButton("Preview");
            btnPreviewEditedBillboard.addActionListener(this);
            btnResetEditedBillboard = new JButton("Reset");
            btnResetEditedBillboard.addActionListener(this);
            btnExportBillboard = new JButton("Export");
            btnExportBillboard.addActionListener(this);
            buttonPanel.add(Box.createHorizontalStrut(25));
            buttonPanel.add(btnSaveEditedBillboard);
            buttonPanel.add(Box.createHorizontalStrut(25));
            buttonPanel.add(btnPreviewEditedBillboard);
            buttonPanel.add(Box.createHorizontalStrut(25));
            buttonPanel.add(btnResetEditedBillboard);
            buttonPanel.add(Box.createHorizontalStrut(25));
            buttonPanel.add(btnExportBillboard);
            editBillboardPanel.add(makeCreateBillboardFieldsPanel(), BorderLayout.CENTER);
            editBillboardPanel.add(buttonPanel, BorderLayout.SOUTH);
            JPanel panelBillboardName = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lblBillboardName = new JLabel("Billboard Name");
            billboardName = new JTextField(30);
            panelBillboardName.add(lblBillboardName);
            panelBillboardName.add(billboardName);
            editBillboardPanel.add(panelBillboardName, BorderLayout.NORTH);
            socketStart();
            BillboardRequest billboardRequest = new BillboardRequest((String) billboardList.getSelectedValue(),sessionToken);
            oos.writeObject(billboardRequest);
            oos.flush();
            String billboardContent = null;
            String billboardCreator = null;
            Object transoO = ois.readObject();
            if (transoO instanceof BillboardReply) {
                BillboardReply reply = (BillboardReply) transoO;
                billboardContent = reply.getXmlcontent();
                billboardCreator = reply.getCreator();
            }
            socketStop();

            billboardName.setText((String) billboardList.getSelectedValue());
            System.out.println(billboardContent);
            System.out.println((String) billboardList.getSelectedValue());
            parseXMLContentsFromDatabase(billboardContent);
            creator.setText(billboardCreator);
            editBillboardDialog.add(buttonPanel, BorderLayout.SOUTH);
            editBillboardDialog.add(editBillboardPanel);
            editBillboardDialog.setVisible(true);
        }


    }


    private void convertBillboardToXML() {
        XMLContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

        if (backgroundColour.getText() != null && !backgroundColour.getText().equals("")) {
            addToXMLContents("<billboard background=\"" + backgroundColour.getText() + "\">\n");
        } else {
            addToXMLContents("<billboard>\n");
        }
        if (message.getText() != null && !message.getText().equals("") &&
                messageColour.getText() != null && !messageColour.getText().equals("")) {
            addToXMLContents("<message colour=\"" + messageColour.getText() + "\">" + message.getText() + "</message>\n");
        }
        else if (message.getText() != null && !message.getText().equals("")){
            addToXMLContents("<message>" + message.getText() + "</message>\n");
        }
        if(picture.getText() != null && !picture.getText().equals("") && cbUrl.isSelected()) {
            addToXMLContents("<picture url=\"" + picture.getText() + "\"/>\n");
        }
        else if(picture.getText() != null && !picture.getText().equals("") && !cbUrl.isSelected()){
            addToXMLContents("<picture data=\"" + picture.getText() + "\"/>\n");
        }
        if(information.getText() != null && !information.getText().equals("") &&
                informationColour.getText() != null && !informationColour.getText().equals("")) {
            addToXMLContents("<information colour=\"" + informationColour.getText() + "\">" + information.getText()
                    + "</information>\n");
        }
        else if (information.getText() != null && !message.getText().equals("")){
            addToXMLContents("<information>" + information.getText()+ "</information>\n");
        }
        addToXMLContents("</billboard>");
    }

    private void saveNewBillboardPressed() throws IOException, ClassNotFoundException {
        convertBillboardToXML();
        socketStart();
        CreateBillboardRequest billboardRequest = new CreateBillboardRequest(billboardName.getText(),
                XMLContents, sessionToken);
        oos.writeObject(billboardRequest);
        oos.flush();
        Object transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply) {
            AcknowledgeReply reply = (AcknowledgeReply) transoO;
            System.out.println(reply.getAcknowledgement());
        }
        socketStop();
        if (billboardName.getText() != null && !billboardName.getText().equals("")) {
//            BillboardInfo b = new BillboardInfo(billboardName.getText(), XMLContents, null);

            createNewBillboardDialog.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Billboard name must be filled",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
        XMLContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        dispose();
        HomeUI GUI = new HomeUI(sessionToken, permissionsList,currentUsername, loggedinTime,pane.getSelectedIndex());
        GUI.setVisible(true);
    }
    private void deleteBillboardPressed() throws IOException, ClassNotFoundException {
        socketStart();
        DelateBillboardRequest delateBillboardRequest = new DelateBillboardRequest((String) billboardList.getSelectedValue(), sessionToken);
        oos.writeObject(delateBillboardRequest);
        oos.flush();
        Object transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply) {
            AcknowledgeReply lurlist = (AcknowledgeReply) transoO;
            System.out.println(lurlist.getAcknowledgement());
        }
        socketStop();
        dispose();
        HomeUI GUI = new HomeUI(sessionToken, permissionsList,currentUsername,loggedinTime,pane.getSelectedIndex());
        GUI.setVisible(true);
    }
    public void exportBillboardPressed()
    {
        if (billboardName.getText() != null && !billboardName.getText().equals("")) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            fileChooser.setSelectedFile(new File(billboardName.getText() + ".xml"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("XML file","xml"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int result = fileChooser.showSaveDialog(this);
            String fileName = fileChooser.getSelectedFile().toString();
            if (result == JFileChooser.APPROVE_OPTION) {
                if (!fileName .endsWith(".xml"))
                    fileName += ".xml";
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(new File(fileName).getPath()));
                    convertBillboardToXML();
                    writer.write(XMLContents);
                    XMLContents = "";
                } catch (Exception e1) {
                    e1.printStackTrace();
                }finally {
                    try {
                        if (null != writer) {
                            writer.close();
                        }
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }
        else
        {
            JOptionPane.showMessageDialog(this,"Billboard name must be filled",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void newUserPressed()
    {
        addNewUserDialog = new JDialog(this, "Add new user");
        addNewUserDialog.setSize(300,300);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        addNewUserDialog.setLocation(dim.width / 2 - 150, dim.height / 2 - 150);
        JPanel addNewUserPanel = new JPanel();
        addNewUserPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        btnSaveNewUser = new JButton("Save");
        btnSaveNewUser.addActionListener(this);
        btnResetNewUser = new JButton("Reset");
        btnResetNewUser.addActionListener(this);
        buttonPanel.add(Box.createHorizontalStrut(75));
        buttonPanel.add(btnSaveNewUser);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(btnResetNewUser);
        addNewUserPanel.add(makeUserFieldsPanel(), BorderLayout.CENTER);
        addNewUserDialog.add(buttonPanel, BorderLayout.SOUTH);
        addNewUserDialog.add(addNewUserPanel);
        addNewUserDialog.setVisible(true);
    }
    private void editUserPressed() throws IOException, ClassNotFoundException {
        if(usernameList.getSelectedValue() != null) {
            editUserDialog = new JDialog(this, "Edit user");
            editUserDialog.setSize(300, 300);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            editUserDialog.setLocation(dim.width / 2 - 150, dim.height / 2 - 150);
            JPanel editUserPanel = new JPanel();
            editUserPanel.setLayout(new BorderLayout());
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            btnUpdateUser = new JButton("Update");
            btnUpdateUser.addActionListener(this);
            buttonPanel.add(Box.createHorizontalStrut(110));
            buttonPanel.add(btnUpdateUser);
            editUserPanel.add(makeUserFieldsPanel(), BorderLayout.CENTER);
            socketStart();
            GetUserPemmRequest getUserPemmRequest = new GetUserPemmRequest((String) usernameList.getSelectedValue(), sessionToken);
            oos.writeObject(getUserPemmRequest);
            oos.flush();
            UserInfo u = null;
            Object transoO = ois.readObject();
            if (transoO instanceof GetUserpemmReply) {
                GetUserpemmReply lurlist = (GetUserpemmReply) transoO;
                System.out.println(lurlist.getListPermissions());
                u = lurlist.getU();
            }
            socketStop();

            name.setText(u.getName());
            username.setText(u.getUsername());
            username.setEditable(false);
            password.setText("******");
            email.setText(u.getEmail());
            cbCreateBillboardsPermission.setSelected(Boolean.parseBoolean(u.getCreateBillboards()));
            cbEditAllBillboardsPermission.setSelected(Boolean.parseBoolean(u.getEditAllBillboards()));
            cbScheduleBillboardsPermission.setSelected(Boolean.parseBoolean(u.getScheduleBillboards()));
            if(usernameList.getSelectedValue().equals(currentUsername)){
                cbEditUsersPermission.setSelected(Boolean.parseBoolean(u.getEditUsers()));
                cbEditUsersPermission.setEnabled(false);
            }else{
                cbEditUsersPermission.setSelected(Boolean.parseBoolean(u.getEditUsers()));
            }

            editUserDialog.add(buttonPanel, BorderLayout.SOUTH);
            editUserDialog.add(editUserPanel);
            editUserDialog.setVisible(true);
        }
    }
    private void editCurrentUser() throws IOException, ClassNotFoundException{
        editUserDialog = new JDialog(this, "Edit Current User");
        editUserDialog.setSize(300, 300);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        editUserDialog.setLocation(dim.width / 2 - 150, dim.height / 2 - 150);
        JPanel editUserPanel = new JPanel();
        editUserPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        btnUpdateCurrentUser = new JButton("Update Profile");
        btnUpdateCurrentUser.addActionListener(this);
        buttonPanel.add(Box.createHorizontalStrut(110));
        buttonPanel.add(btnUpdateCurrentUser);
        editUserPanel.add(makeUserFieldsPanel(), BorderLayout.CENTER);
        socketStart();
        GetUserPemmRequest getUserPemmRequest = new GetUserPemmRequest(currentUsername, sessionToken);
        oos.writeObject(getUserPemmRequest);
        oos.flush();
        UserInfo u = null;
        Object transoO = ois.readObject();
        if (transoO instanceof GetUserpemmReply) {
            GetUserpemmReply lurlist = (GetUserpemmReply) transoO;
            System.out.println(lurlist.getListPermissions());
            u = lurlist.getU();
        }
        socketStop();

        name.setText(u.getName());
        username.setText(u.getUsername());
        username.setEditable(false);
        password.setText("******");
        email.setText(u.getEmail());
        if(permissionsList.get(3).equals("true")){
            cbCreateBillboardsPermission.setSelected(Boolean.parseBoolean(u.getCreateBillboards()));
            cbEditAllBillboardsPermission.setSelected(Boolean.parseBoolean(u.getEditAllBillboards()));
            cbScheduleBillboardsPermission.setSelected(Boolean.parseBoolean(u.getScheduleBillboards()));
            cbEditUsersPermission.setSelected(Boolean.parseBoolean(u.getEditUsers()));
            cbEditUsersPermission.setEnabled(false);
        }else{
            cbCreateBillboardsPermission.setSelected(Boolean.parseBoolean(u.getCreateBillboards()));
            cbCreateBillboardsPermission.setEnabled(false);
            cbEditAllBillboardsPermission.setSelected(Boolean.parseBoolean(u.getEditAllBillboards()));
            cbEditAllBillboardsPermission.setEnabled(false);
            cbScheduleBillboardsPermission.setSelected(Boolean.parseBoolean(u.getScheduleBillboards()));
            cbScheduleBillboardsPermission.setEnabled(false);
            cbEditUsersPermission.setSelected(Boolean.parseBoolean(u.getEditUsers()));
            cbEditUsersPermission.setEnabled(false);
        }




        editUserDialog.add(buttonPanel, BorderLayout.SOUTH);
        editUserDialog.add(editUserPanel);
        editUserDialog.setVisible(true);

    }
    private void deleteUserPressed() throws IOException, ClassNotFoundException {
        socketStart();
        DelateUserRequest delateUserRequest = new DelateUserRequest((String) usernameList.getSelectedValue(), sessionToken);
        oos.writeObject(delateUserRequest);
        oos.flush();
        Object transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply) {
            AcknowledgeReply lurlist = (AcknowledgeReply) transoO;
            System.out.println(lurlist.getAcknowledgement());
            dispose();
            HomeUI GUI = new HomeUI(sessionToken, permissionsList,currentUsername, loggedinTime, pane.getSelectedIndex());
            GUI.setVisible(true);
        }
        socketStop();
    }
    private void resetNewUserPressed() {
        name.setText("");
        username.setText("");
        password.setText("");
        email.setText("");
        cbCreateBillboardsPermission.setSelected(false);
        cbEditAllBillboardsPermission.setSelected(false);
        cbScheduleBillboardsPermission.setSelected(false);
        cbEditUsersPermission.setSelected(false);
    }
    private void saveNewUserPressed() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        if (name.getText() != null && !name.getText().equals("") &&
                username.getText() != null && !username.getText().equals("") && password.getText() != null
                && !password.getText().equals("") && email.getText() != null && !email.getText().equals("")) {
            socketStart();
            String hashedPassword = getHashedPass(password.getText());
            ArrayList<String> list_permission = new ArrayList<String>();
            list_permission.add(Boolean.toString(cbCreateBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditAllBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbScheduleBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditUsersPermission.isSelected()));

            CreateUsersRequest createUsersRequest = new CreateUsersRequest(sessionToken, hashedPassword, username.getText(), email.getText(),list_permission);
            oos.writeObject(createUsersRequest);
            oos.flush();
            Object transoO = ois.readObject();
            if (transoO instanceof AcknowledgeReply) {
                AcknowledgeReply lurlist = (AcknowledgeReply) transoO;
                System.out.println(lurlist.getAcknowledgement());
                dispose();
                HomeUI GUI = new HomeUI(sessionToken, permissionsList,currentUsername, loggedinTime, pane.getSelectedIndex());
                GUI.setVisible(true);
            }
            socketStop();

//            addNewUserDialog.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Data cannot be null",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void updateUserPressed() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        if (name.getText() != null && !name.getText().equals("") &&
                username.getText() != null && !username.getText().equals("") && password.getText() != null
                && email.getText() != null && !email.getText().equals("")) {
            ArrayList<String> list_permission = new ArrayList<String>();
            socketStart();
            list_permission.add(Boolean.toString(cbCreateBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditAllBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbScheduleBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditUsersPermission.isSelected()));

            SetUserPemmRequest setUserPemmRequest = new SetUserPemmRequest(username.getText(), sessionToken, email.getText(), list_permission);
            oos.writeObject(setUserPemmRequest);
            oos.flush();
            Object transoO = ois.readObject();
            if (transoO instanceof AcknowledgeReply) {
                AcknowledgeReply lurlist = (AcknowledgeReply) transoO;
                System.out.println(lurlist.getAcknowledgement());

            }
            socketStop();
            socketStart();
            list_permission.add(Boolean.toString(cbCreateBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditAllBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbScheduleBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditUsersPermission.isSelected()));
            String hashedPassword = getHashedPass(password.getText());

            SetPassRequest setPassRequest = new SetPassRequest(name.getText(),username.getText(),email.getText(),hashedPassword,sessionToken,(String)usernameList.getSelectedValue());
            oos.writeObject(setPassRequest);
            oos.flush();
            Object transo = ois.readObject();
            if (transo instanceof AcknowledgeReply) {
                AcknowledgeReply lurlist = (AcknowledgeReply) transo;
                System.out.println(lurlist.getAcknowledgement());
//
//                dispose();
//                HomeUI GUI = new HomeUI(sessionToken, permissionsList,pane.getSelectedIndex());
//                GUI.setVisible(true);
            }
            socketStop();
            socketStart();
            GetUserPemmRequest getUserPemmRequest = new GetUserPemmRequest(currentUsername, sessionToken);
            oos.writeObject(getUserPemmRequest);
            oos.flush();

            Object getPermm = ois.readObject();
            if (getPermm instanceof GetUserpemmReply) {
                GetUserpemmReply lurlist = (GetUserpemmReply) getPermm;
                System.out.println(lurlist.getListPermissions());
//                permissionsList = lurlist.getListPermissions();
//                disableFeatureBasedOnPermissions();
                dispose();
                HomeUI GUI = new HomeUI(sessionToken, lurlist.getListPermissions(), currentUsername, loggedinTime, pane.getSelectedIndex());
                GUI.setVisible(true);
            }
            socketStop();
            editUserDialog.dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,"Data cannot be null",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void updateCurrentUserPressed() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        if (name.getText() != null && !name.getText().equals("") &&
                username.getText() != null && !username.getText().equals("") && password.getText() != null
                && email.getText() != null && !email.getText().equals("")) {
            ArrayList<String> list_permission = new ArrayList<String>();
            socketStart();
            list_permission.add(Boolean.toString(cbCreateBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditAllBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbScheduleBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditUsersPermission.isSelected()));

            SetUserPemmRequest setUserPemmRequest = new SetUserPemmRequest(username.getText(), sessionToken, email.getText(), list_permission);
            oos.writeObject(setUserPemmRequest);
            oos.flush();
            Object transoO = ois.readObject();
            if (transoO instanceof AcknowledgeReply) {
                AcknowledgeReply lurlist = (AcknowledgeReply) transoO;
                System.out.println(lurlist.getAcknowledgement());

            }
            socketStop();
            socketStart();
            list_permission.add(Boolean.toString(cbCreateBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditAllBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbScheduleBillboardsPermission.isSelected()));
            list_permission.add(Boolean.toString(cbEditUsersPermission.isSelected()));
            String hashedPassword = getHashedPass(password.getText());

            SetPassRequest setPassRequest = new SetPassRequest(name.getText(),username.getText(),email.getText(),hashedPassword,sessionToken,username.getText());
            oos.writeObject(setPassRequest);
            oos.flush();
            Object transo = ois.readObject();
            if (transo instanceof AcknowledgeReply) {
                AcknowledgeReply lurlist = (AcknowledgeReply) transo;
                System.out.println(lurlist.getAcknowledgement());
            }
            socketStop();

            socketStart();
            GetUserPemmRequest getUserPemmRequest = new GetUserPemmRequest(currentUsername, sessionToken);
            oos.writeObject(getUserPemmRequest);
            oos.flush();
            UserInfo u = null;
            Object getPermm = ois.readObject();
            if (getPermm instanceof GetUserpemmReply) {
                GetUserpemmReply lurlist = (GetUserpemmReply) getPermm;
                System.out.println(lurlist.getListPermissions());

                dispose();
                HomeUI GUI = new HomeUI(sessionToken, lurlist.getListPermissions(), currentUsername,loggedinTime, pane.getSelectedIndex());
                GUI.setVisible(true);
            }
            socketStop();

            editUserDialog.dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,"Data cannot be null",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void displayColorChooser()
    {
        Color color = JColorChooser.showDialog(this,
                "Select a color", initialColor);
        if(color != null) {
            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();
        }
        hexColour = String.format("#%02X%02X%02X", r, g, b);
        initialColor = new Color(r, g, b);
    }
    private void resetEditedBillboardPressed() {
        billboardName.setText((String) billboardList.getSelectedValue());
        parseXMLContentsFromDatabase(content);
    }
    private void uploadPicturePressed() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Picture Files"
                , "bmp", "jpeg", "png");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            picture.setText(encodeImageToBase64(selectedFile.toString()));
        }
    }
    private void saveImportedBillboardPressed() throws IOException, ClassNotFoundException {
        socketStart();
        CreateBillboardRequest billboardRequest = new CreateBillboardRequest(billboardName.getText(),
                XMLContents, sessionToken);
        oos.writeObject(billboardRequest);
        oos.flush();
        String billboardContent = null;
        Object transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply) {
            AcknowledgeReply reply = (AcknowledgeReply) transoO;
            System.out.println(reply.getAcknowledgement());
        }
        socketStop();

        importBillboardDialog.dispose();
        XMLContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        dispose();
        HomeUI GUI = new HomeUI(sessionToken, permissionsList,currentUsername, loggedinTime, pane.getSelectedIndex());
        GUI.setVisible(true);
    }
    private void saveEditedBillboardPressed() throws IOException, ClassNotFoundException {
        convertBillboardToXML();
        if (billboardName.getText() != null && !billboardName.getText().equals("")) {
            socketStart();
            EditBillboardRequest editBillboardRequest = new EditBillboardRequest(billboardName.getText(),
                    XMLContents, sessionToken);
            oos.writeObject(editBillboardRequest);
            oos.flush();
            String billboardContent = null;
            Object transoO = ois.readObject();
            if (transoO instanceof AcknowledgeReply) {
                AcknowledgeReply reply = (AcknowledgeReply) transoO;
                System.out.println(reply.getAcknowledgement());
            }
            socketStop();

            editBillboardDialog.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Billboard name must be filled",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
        XMLContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    }

    private void resetNewBillboardPressed(){
        cbUrl.setSelected(false);
        initialColor = Color.BLACK;
        billboardName.setText("");
        backgroundColour.setText("");
        message.setText("");
        messageColour.setText("");
        picture.setText("");
        information.setText("");
        informationColour.setText("");
    }

    public static String encodeImageToBase64(String imagePath) {    //from https://grokonez.com/java/java-advanced/java-8-encode-decode-an-image-base64
        String base64Image = "";
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }

    private void addToXMLContents(String content) {
        XMLContents = XMLContents + content;
    }

    //    private void previewBillboardPressed(JButton btnSource)
//    {
//        Dimension dialogSize = new Dimension(860, 600);
//
//        previewBillboardDialog = new JDialog(this,"Preview Billboard");
//        previewBillboardDialog.setSize(dialogSize);
//        previewBillboardDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//
//        SimpleAttributeSet set=new SimpleAttributeSet();
//        StyleConstants.setAlignment(set,StyleConstants.ALIGN_CENTER);
//        StyleConstants.setFontFamily(set, "Arial");
//
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        previewBillboardDialog.setLocation(dim.width / 2 - 430, dim.height / 2 - 300);
//
//        JPanel previewPanel = new JPanel();
//        XMLParser parser;
//        HashMap<String,String> xmlInfo;
//
//        if (btnSource == btnPreviewEditedBillboard || btnSource == btnPreviewNewBillboard) {
//            convertBillboardToXML();
//            parser = new XMLParser(XMLContents);
//        }
//        else if (btnSource == btnPreviewBillboard)
//            parser = new XMLParser(billboardData.get(billboardList.getSelectedValue()).getXMLContent());
//        else
//            parser = new XMLParser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<billboard>\n<message>No Billboard Found</message>\n</billboard>");
//
//        xmlInfo = parser.parseXML();
//
//        // Layout and closing
//        GridBagLayout layout = new GridBagLayout();
//        previewPanel.setLayout(layout);
//
//        // Get and set Background
//        String bgString = xmlInfo.getOrDefault("bgColour", "#FFFFFF");
//        Color bgColour = HexToRGB(bgString);
//        previewPanel.setBackground(bgColour);
//
//        // ## GridBag Constraints ## \\
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.weighty = 1;
//        gbc.weightx = 1;
//
//        // ## Utilities and Actions ## \\
//        // Provide a way to decode and display images
//        ImageGenerator imgGen = new ImageGenerator();
//
//
//        // ## XML display ## \\
//        // Create heading label if exists and add it to the window
//        if (xmlInfo.containsKey("message")) {
//            JTextPane titleText = new JTextPane();
//            titleText.setText(xmlInfo.get("message"));
//            titleText.setFont(new Font("Arial", Font.PLAIN, 84));
//            titleText.setForeground(
//                    HexToRGB(
//                            xmlInfo.getOrDefault("messageColour", "#000000")
//                    )
//            );
//            titleText.setBackground(bgColour);
//            StyleConstants.setFontSize(set, 84);
//
//            titleText.setParagraphAttributes(set,true);
//            titleText.setEditable(false);
//
//            gbc.gridx = 0;
//            gbc.gridy = 0;
//            previewPanel.add(titleText, gbc);
//        }
//
//        // Create picture label if exists and add it to the window
//        // Create picture label if exists and add it to the window
//        if (xmlInfo.containsKey("picture")) {
//            JLabel pictureLabel = new JLabel();
//            String imgInfo = xmlInfo.get("picture");
//
//            try {
//                BufferedImage picture = imgGen.isBase64EncodedImage(imgInfo) ? imgGen.decodeDataString(imgInfo) : imgGen.downloadImage(imgInfo);
//
//                int resizedWidth = picture.getWidth();
//                int resizedHeight = picture.getHeight();
//
//                if (picture.getWidth() != dialogSize.width / 2) {
//                    resizedWidth = dialogSize.width / 2;
//                    resizedHeight = (resizedWidth * picture.getHeight()) / picture.getWidth();
//                }
//
//                if (picture.getHeight() != dialogSize.height / 2) {
//                    resizedHeight = dialogSize.height / 2;
//                    resizedWidth = (resizedHeight * picture.getWidth()) / picture.getHeight();
//                }
//
//                Image resizedImage = picture.getScaledInstance(
//                        resizedWidth,
//                        resizedHeight,
//                        Image.SCALE_SMOOTH
//                );
//                pictureLabel.setIcon(new ImageIcon(resizedImage));
//            } catch (IOException | BadImageFormatException e) {
//                e.printStackTrace();
//            }
//
//            gbc.gridy = 1;
//            previewPanel.add(pictureLabel, gbc);
//        }
//
//        // Create information label if exists and add it to the window
//        if (xmlInfo.containsKey("information")) {
//            JTextPane informationText = new JTextPane();
//            informationText.setText(xmlInfo.get("information"));
//            informationText.setFont(new Font("Arial", Font.PLAIN, 36));
//            informationText.setForeground(
//                    HexToRGB(
//                            xmlInfo.getOrDefault("informationColour", "#000000")
//                    )
//            );
//            informationText.setBackground(bgColour);
//            StyleConstants.setFontSize(set, 36);
//
//            informationText.setParagraphAttributes(set, true);
//            informationText.setEditable(false);
//
//            gbc.gridy = 2;
//            previewPanel.add(informationText, gbc);
//        }
//
//        previewBillboardDialog.add(previewPanel);
//        previewBillboardDialog.setVisible(true);
//    }
    private void previewBillboardPressed(JButton btnSource) throws IOException, ClassNotFoundException {
        Dimension dialogSize = new Dimension(860, 600);

        previewBillboardDialog = new JDialog(this, "Preview Billboard");
        previewBillboardDialog.setSize(dialogSize);
        previewBillboardDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(set, "Arial");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        previewBillboardDialog.setLocation(dim.width / 2 - 430, dim.height / 2 - 300);

        JPanel previewPanel = new JPanel();
        XMLParser parser;
        HashMap<String, String> xmlInfo;

        if (btnSource == btnPreviewEditedBillboard || btnSource == btnPreviewNewBillboard) {
            convertBillboardToXML();
            System.out.println(XMLContents);
            parser = new XMLParser(XMLContents);
        } else if (btnSource == btnPreviewBillboard) {
            socketStart();
            BillboardRequest billboardRequest = new BillboardRequest((String) billboardList.getSelectedValue(), sessionToken);
            oos.writeObject(billboardRequest);
            oos.flush();
            String billContent = null;
            Object trans = ois.readObject();
            if (trans instanceof BillboardReply) {
                BillboardReply reply = (BillboardReply) trans;
                billContent = reply.getXmlcontent();
            }
            parser = new XMLParser(billContent);
        } else
            parser = new XMLParser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<billboard>\n<message>No Billboard Found</message>\n</billboard>");

        xmlInfo = parser.parseXML();

        // Layout and closing
        GridBagLayout layout = new GridBagLayout();
        previewPanel.setLayout(layout);

        // Get and set Background
        String bgString = xmlInfo.getOrDefault("bgColour", "#FFFFFF");
        Color bgColour = HexToRGB(bgString);
        previewPanel.setBackground(bgColour);

        // ## GridBag Constraints ## \\
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1;
        gbc.weightx = 1;

        // ## Utilities and Actions ## \\
        // Provide a way to decode and display images
        ImageGenerator imgGen = new ImageGenerator();


        // ## XML display ## \\
        // Create heading label if exists and add it to the window
        if (xmlInfo.containsKey("message")) {
            JTextPane titleText = new JTextPane();
            titleText.setText(xmlInfo.get("message"));
            titleText.setFont(new Font("Arial", Font.PLAIN, 84));
            titleText.setForeground(
                    HexToRGB(
                            xmlInfo.getOrDefault("messageColour", "#000000")
                    )
            );
            titleText.setBackground(bgColour);
            StyleConstants.setFontSize(set, 84);

            titleText.setParagraphAttributes(set, true);
            titleText.setEditable(false);

            gbc.gridx = 0;
            gbc.gridy = 0;
            previewPanel.add(titleText, gbc);
        }

        // Create picture label if exists and add it to the window
        // Create picture label if exists and add it to the window
        if (xmlInfo.containsKey("picture")) {
            JLabel pictureLabel = new JLabel();
            String imgInfo = xmlInfo.get("picture");

            try {
                BufferedImage picture = imgGen.isBase64EncodedImage(imgInfo) ? imgGen.decodeDataString(imgInfo) : imgGen.downloadImage(imgInfo);

                int resizedWidth = picture.getWidth();
                int resizedHeight = picture.getHeight();

                if (picture.getWidth() != dialogSize.width / 2) {
                    resizedWidth = dialogSize.width / 2;
                    resizedHeight = (resizedWidth * picture.getHeight()) / picture.getWidth();
                }

                if (picture.getHeight() != dialogSize.height / 2) {
                    resizedHeight = dialogSize.height / 2;
                    resizedWidth = (resizedHeight * picture.getWidth()) / picture.getHeight();
                }

                Image resizedImage = picture.getScaledInstance(
                        resizedWidth,
                        resizedHeight,
                        Image.SCALE_SMOOTH
                );
                pictureLabel.setIcon(new ImageIcon(resizedImage));
            } catch (IOException | BadImageFormatException e) {
                e.printStackTrace();
            }

            gbc.gridy = 1;
            previewPanel.add(pictureLabel, gbc);
        }

        // Create information label if exists and add it to the window
        if (xmlInfo.containsKey("information")) {
            JTextPane informationText = new JTextPane();
            informationText.setText(xmlInfo.get("information"));
            informationText.setFont(new Font("Arial", Font.PLAIN, 36));
            informationText.setForeground(
                    HexToRGB(
                            xmlInfo.getOrDefault("informationColour", "#000000")
                    )
            );
            informationText.setBackground(bgColour);
            StyleConstants.setFontSize(set, 36);

            informationText.setParagraphAttributes(set, true);
            informationText.setEditable(false);

            gbc.gridy = 2;
            previewPanel.add(informationText, gbc);
        }

        previewBillboardDialog.add(previewPanel);
        previewBillboardDialog.setVisible(true);
    }

    private void logout() throws IOException, ClassNotFoundException {
        socketStart();
        LogoutUsersRequest logoutUsersRequest = new LogoutUsersRequest(sessionToken);
        oos.writeObject(logoutUsersRequest);
        oos.flush();
        Object logoutO = ois.readObject();
        if (logoutO instanceof  AcknowledgeReply){
            AcknowledgeReply reply = (AcknowledgeReply) logoutO;
            System.out.println(reply.getAcknowledgement());
        }
        socketStop();
        dispose();
        dispose();
        UserLoginUI GUI = new UserLoginUI();
        GUI.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnSource = (JButton) e.getSource();
        if (btnSource == btnNewUser) {
            newUserPressed();
        } else if (btnSource == btnEditUser) {
            try {
                editUserPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnDeleteUser) {
            try {
                deleteUserPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnResetNewUser) {
            resetNewUserPressed();
        } else if (btnSource == btnSaveNewUser) {
            try {
                saveNewUserPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnUpdateUser) {
            try {
                updateUserPressed();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnSaveEditedBillboard) {
            try {
                saveEditedBillboardPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnImportBillboard) {
            importBillboardPressed();
        } else if (btnSource == btnCreateNewBillboard) {
            createNewBillboardPressed();
        } else if (btnSource == btnBackgroundColour) {
            displayColorChooser();
            backgroundColour.setText(hexColour);
        } else if (btnSource == btnMessageColour) {
            displayColorChooser();
            messageColour.setText(hexColour);
        } else if (btnSource == btnInformationColour) {
            displayColorChooser();
            informationColour.setText(hexColour);
        } else if (btnSource == btnUploadPicture) {
            uploadPicturePressed();
        } else if (btnSource == btnSaveImportedBillboard) {
            try {
                saveImportedBillboardPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnSaveNewBillboard) {
            try {
                saveNewBillboardPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnResetNewBillboard) {
            resetNewBillboardPressed();
        } else if (btnSource == btnResetEditedBillboard) {
            resetEditedBillboardPressed();
        } else if (btnSource == btnExportBillboard) {
            exportBillboardPressed();
        } else if (btnSource == btnDeleteBillboard) {
            try {
                deleteBillboardPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnEditBillboard) {
            try {
                editBillboardPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        else if (btnSource == btnEditProfile) {
            try {
                editCurrentUser();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }else if (btnSource == btnUpdateCurrentUser) {
            try {
                updateCurrentUserPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        }
        else if (btnSource == btnLogout) {
            try {
                logout();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (btnSource == btnPreviewBillboard || btnSource == btnPreviewEditedBillboard || btnSource == btnPreviewNewBillboard) {
            try {
                previewBillboardPressed(btnSource);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
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

