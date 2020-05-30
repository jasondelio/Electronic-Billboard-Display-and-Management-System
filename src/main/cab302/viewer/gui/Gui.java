package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;
import cab302.viewer.exceptions.MalformedHexadecimalColourException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.Action;
import javax.swing.JRootPane;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

import static cab302.viewer.util.HexToRGB.HexToRGB;

public class Gui extends JFrame {

    private JFrame frame;

    /**
     * Constructor to assemble the GUI
     * @param xmlInfo The XML data HashMap from the XMLParser.parseXML() method
     */
    public Gui(HashMap<String, String> xmlInfo) {

        // Window Configuration
        // Set window to fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Create background colour, if it fails, create default colour as white (#FFF)
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
}