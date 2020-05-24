package cab302.ControlPanel;

import cab302.database.billboard.BillboardData;
import cab302.database.billboard.BillboardInfo;
import cab302.database.user.UserData;
import cab302.database.user.UserInfo;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Base64;
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
    private JTextField name;
    private JTextField username;
    private JTextField password;
    private JTextField email;
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
    private int r;
    private int g;
    private int b;
    private String hexColour;
    private String XMLContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    UserData userData;
    BillboardData billboardData;

    public HomeUI(UserData userData, BillboardData billboardData){
        super("Billboard Control Panel");
        this.userData = userData;
        this.billboardData = billboardData;
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);

        JTabbedPane pane = new JTabbedPane();

        JPanel panelHome = new JPanel(new BorderLayout());
        JLabel lblWelcome = new JLabel("Welcome to Billboard Control Panel !");
        lblWelcome.setHorizontalAlignment(JLabel.CENTER);
        lblWelcome.setFont (lblWelcome.getFont().deriveFont (24.0f));
        panelHome.add(lblWelcome, BorderLayout.CENTER);
        JButton btnLogout = new JButton("Logout");


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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
    private JPanel makePanelCreateBillboards()
    {
        JPanel panelCreateBillboards = new JPanel();
        panelCreateBillboards.setLayout(new BorderLayout());
        panelCreateBillboards.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCreateBillboards.add(makeButtonsPanelCreateBillboards(), BorderLayout.CENTER);
        return panelCreateBillboards;
    }
    private JPanel makePanelEditUsers(){
        JPanel panelEditUsers = new JPanel();
        panelEditUsers.setLayout(new BorderLayout());
        usernameList = new JList(userData.getModel());
        scroller = new JScrollPane(usernameList);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelEditUsers.add(scroller, BorderLayout.CENTER);
        panelEditUsers.add(makeButtonsPanelEditUsers(), BorderLayout.SOUTH);
        return panelEditUsers;
    }
    private JPanel makePanelListBillboards()
    {
        JPanel panelListBillboards = new JPanel();
        panelListBillboards.setLayout(new BorderLayout());
        billboardList = new JList(billboardData.getModel());
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
                .addComponent(lblPicture).addComponent(lblInformation));
        hGroup.addGroup(layout.createParallelGroup().addComponent(message)
                .addComponent(picture).addComponent(information));
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
    private void parseXMLContentsFromDatabase(BillboardInfo b)
    {
        String contents = b.getXMLContent();
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(contents));

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
                    backgroundColour.setText(eElement.getAttribute("colour"));
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

    private void editBillboardPressed() {
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

            BillboardInfo b = billboardData.get(billboardList.getSelectedValue());
            billboardName.setText(b.getName());

            parseXMLContentsFromDatabase(b);

            editBillboardDialog.add(buttonPanel, BorderLayout.SOUTH);
            editBillboardDialog.add(editBillboardPanel);
            editBillboardDialog.setVisible(true);
        }


    }


    private void convertBillboardToXML()
    {
        if(backgroundColour.getText() != null && !backgroundColour.getText().equals("")) {
            addToXMLContents("<billboard background=\"" + backgroundColour.getText() + "\">\n");
        }
        else {
            addToXMLContents("<billboard>\n");
        }
        if(message.getText() != null && !message.getText().equals("") &&
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

    private void saveNewBillboardPressed()
    {
        convertBillboardToXML();

        if (billboardName.getText() != null && !billboardName.getText().equals("")) {
            BillboardInfo b = new BillboardInfo(billboardName.getText(), XMLContents);
            billboardData.add(b);
            createNewBillboardDialog.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Billboard name must be filled",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
        XMLContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

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
    private void editUserPressed()
    {
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
            UserInfo u = userData.get(usernameList.getSelectedValue());
            name.setText(u.getName());
            username.setText(u.getUsername());
            password.setText(u.getPasswords());
            email.setText(u.getEmail());
            cbCreateBillboardsPermission.setSelected(Boolean.parseBoolean(u.getCreateBillboards()));
            cbEditAllBillboardsPermission.setSelected(Boolean.parseBoolean(u.getEditAllBillboards()));
            cbScheduleBillboardsPermission.setSelected(Boolean.parseBoolean(u.getScheduleBillboards()));
            cbEditUsersPermission.setSelected(Boolean.parseBoolean(u.getEditUsers()));
            editUserDialog.add(buttonPanel, BorderLayout.SOUTH);
            editUserDialog.add(editUserPanel);
            editUserDialog.setVisible(true);
        }
    }
    private void deleteUserPressed()
    {
        userData.remove(usernameList.getSelectedValue());
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
    private void saveNewUserPressed(){
        if (name.getText() != null && !name.getText().equals("") &&
                username.getText() != null && !username.getText().equals("") && password.getText() != null
                && !password.getText().equals("") && email.getText() != null && !email.getText().equals("")) {
            UserInfo u = new UserInfo(name.getText(), username.getText(), password.getText(), email.getText(),
                    Boolean.toString(cbCreateBillboardsPermission.isSelected()),
                    Boolean.toString(cbEditAllBillboardsPermission.isSelected()),
                    Boolean.toString(cbScheduleBillboardsPermission.isSelected()),
                    Boolean.toString(cbEditUsersPermission.isSelected()));
            userData.add(u);
            addNewUserDialog.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Data cannot be null",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void updateUserPressed() {
        if (name.getText() != null && !name.getText().equals("") &&
                username.getText() != null && !username.getText().equals("") && password.getText() != null
                && !password.getText().equals("") && email.getText() != null && !email.getText().equals("")) {
            userData.edit(name.getText(), username.getText(), password.getText(), email.getText(),
                    usernameList.getSelectedValue().toString(),
                    Boolean.toString(cbCreateBillboardsPermission.isSelected()),
                    Boolean.toString(cbEditAllBillboardsPermission.isSelected()),
                    Boolean.toString(cbScheduleBillboardsPermission.isSelected()),
                    Boolean.toString(cbEditUsersPermission.isSelected()));
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
        BillboardInfo b = billboardData.get(billboardList.getSelectedValue());
        billboardName.setText(b.getName());
        parseXMLContentsFromDatabase(b);
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
    private void saveImportedBillboardPressed() {
        BillboardInfo b = new BillboardInfo(billboardName.getText(), XMLContents);
        billboardData.add(b);
        importBillboardDialog.dispose();
        XMLContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    }
    private void saveEditedBillboardPressed() {
        convertBillboardToXML();
        if (billboardName.getText() != null && !billboardName.getText().equals("")) {
            billboardData.edit(billboardName.getText(), XMLContents, billboardList.getSelectedValue().toString());
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
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }
    private void addToXMLContents(String content)
    {
        XMLContents = XMLContents + content;
    }


    private void previewBillboardPressed(JButton btnSource)
    {
        previewBillboardDialog = new JDialog(this,"Preview Billboard");
        previewBillboardDialog.setSize(new Dimension(860, 600));

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        previewBillboardDialog.setLocation(dim.width / 2 - 860, dim.height / 2 - 300);

        JPanel previewPanel = new JPanel();
        XMLParser parser;

        if (btnSource == btnPreviewEditedBillboard || btnSource == btnPreviewNewBillboard) {
            convertBillboardToXML();
            parser = new XMLParser(new BillboardInfo(billboardName.getText(), XMLContents));
        }
        else if (btnSource == btnPreviewBillboard)
            parser = new XMLParser(billboardData.get(billboardList.getSelectedValue()));
        else
            parser = new XMLParser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<billboard>\n<message>No Billboard Found</message>\n</billboard>");

        HashMap<String,String> xmlInfo = parser.parseXML();

        // Layout and closing
        BorderLayout layout = new BorderLayout();
        previewPanel.setLayout(layout);

        // Get and set Background
        String bgString = xmlInfo.getOrDefault("bgColour", "#FFFFFF");
        Color bgColour = HexToRGB(bgString);
        previewPanel.setBackground(bgColour);

        // ## Utilities and Actions ## \\
        // Provide a way to decode and display images
        ImageGenerator imgGen = new ImageGenerator();


        // ## XML display ## \\
        // Create heading label if exists and add it to the window
        if (xmlInfo.containsKey("message")) {
            JTextArea titleText = new JTextArea();
            String[] messageStringArr = xmlInfo.get("message").split("%%%%");
            titleText.setText(messageStringArr[0]);
            titleText.setFont(new Font("Arial", Font.PLAIN, 84));
            titleText.setForeground(
                    HexToRGB(
                            messageStringArr.length != 1 ?
                                    (!messageStringArr[messageStringArr.length - 1].equals("") ?
                                            messageStringArr[messageStringArr.length - 1] : "#000000")
                                    : "#000000"
                    )
            );
            titleText.setBackground(bgColour);

            titleText.setEditable(false);
            titleText.setLineWrap(true);
            titleText.setWrapStyleWord(true);
            titleText.setRows(3);
            titleText.setColumns(25);

            previewPanel.add(titleText, BorderLayout.NORTH);
        }

        // Create picture label if exists and add it to the window
        if (xmlInfo.containsKey("picture")) {
            JLabel pictureLabel = new JLabel();
            String imgInfo = xmlInfo.get("picture");
            try {
                pictureLabel.setIcon(new ImageIcon(imgGen.isBase64EncodedImage(imgInfo) ? imgGen.decodeDataString(imgInfo) : imgGen.downloadImage(imgInfo)));
            } catch (IOException | BadImageFormatException e) {
                e.printStackTrace();
            }

            previewPanel.add(pictureLabel, BorderLayout.CENTER);
        }

        // Create information label if exists and add it to the window
        if (xmlInfo.containsKey("information")) {
            JTextArea informationText = new JTextArea();
            String[] infoStringArr = xmlInfo.get("information").split("%%%%");
            informationText.setText(infoStringArr[0]);
            informationText.setFont(new Font("Arial", Font.PLAIN, 36));
            informationText.setForeground(
                    HexToRGB(infoStringArr.length > 1
                                    ? (
                                    !infoStringArr[infoStringArr.length - 1].equals("")
                                            ? infoStringArr[infoStringArr.length - 1]
                                            : "#000000"
                            )
                                    : "#000000"
                    )
            );
            informationText.setBackground(bgColour);

            informationText.setEditable(false);
            informationText.setLineWrap(true);
            informationText.setWrapStyleWord(true);
            informationText.setColumns(55);

            previewPanel.add(informationText, BorderLayout.SOUTH);
        }

        previewBillboardDialog.add(previewPanel);
        previewBillboardDialog.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnSource = (JButton)e.getSource();
        if (btnSource == btnNewUser) {
            newUserPressed();
        }
        else if (btnSource == btnEditUser) {
            editUserPressed();
        }
        else if (btnSource == btnDeleteUser) {
            deleteUserPressed();
        }
        else if (btnSource == btnResetNewUser){
            resetNewUserPressed();
        }
        else if(btnSource == btnSaveNewUser){
            saveNewUserPressed();
        }
        else if(btnSource == btnUpdateUser)
        {
            updateUserPressed();
        }
        else if(btnSource == btnSaveEditedBillboard)
        {
            saveEditedBillboardPressed();
        }
        else if (btnSource == btnImportBillboard) {
            importBillboardPressed();
        }
        else if(btnSource == btnCreateNewBillboard)
        {
            createNewBillboardPressed();
        }
        else if (btnSource == btnBackgroundColour)
        {
            displayColorChooser();
            backgroundColour.setText(hexColour);
        }
        else if (btnSource == btnMessageColour){
            displayColorChooser();
            messageColour.setText(hexColour);
        }
        else if (btnSource == btnInformationColour){
           displayColorChooser();
            informationColour.setText(hexColour);
        }
        else if(btnSource == btnUploadPicture)
        {
            uploadPicturePressed();
        }
        else if(btnSource == btnSaveImportedBillboard)
        {
            saveImportedBillboardPressed();
        }
        else if(btnSource == btnSaveNewBillboard)
        {
            saveNewBillboardPressed();
        }
        else if(btnSource == btnResetNewBillboard)
        {
            resetNewBillboardPressed();
        }
        else if(btnSource == btnResetEditedBillboard)
        {
            resetEditedBillboardPressed();
        }
        else if(btnSource == btnExportBillboard)
        {
            exportBillboardPressed();
        }
        else if(btnSource == btnDeleteBillboard)
        {
            billboardData.remove(billboardList.getSelectedValue());
        }
        else if(btnSource == btnEditBillboard)
        {
            editBillboardPressed();
        }
        else if (btnSource == btnPreviewBillboard || btnSource == btnPreviewEditedBillboard || btnSource == btnPreviewNewBillboard)
        {
            previewBillboardPressed(btnSource);
        }

    }
}
