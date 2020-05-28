package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static cab302.viewer.util.HexToRGB.HexToRGB;

public class Gui extends JFrame {

    private JFrame frame;

    // Since JFrame size is the entire screen, get the screen dimensions
    // as the application window size
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // Create a mouse event listener in that, on-click, the viewer closes
    private static final MouseListener mouseListener = new MouseListener() {
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
    };

    /**
     * Creates the message JTextPane and adds it to the Billboard Viewer
     * @param xmlInfo The HashMap containing the information from the XML parser
     * @param bgColour The background colour of the billboard
     * @param set The styling set for the JTextPane
     * @param gbc The constraints for the GridBagLayout
     */
    private void createMessagePane(HashMap<String, String> xmlInfo, Color bgColour, MutableAttributeSet set, GridBagConstraints gbc) {

        // Setup message JTextPane
        JTextPane titleText = new JTextPane();
        titleText.setText(xmlInfo.get("message"));
        titleText.setBackground(bgColour);

        // Set up styling for message text pane
        StyleConstants.setFontSize(set, 84);
        StyleConstants.setForeground(
                set,
                HexToRGB(
                        xmlInfo.getOrDefault("messageColour", "#000000")
                )
        );

        // Set the styling
        titleText.setParagraphAttributes(set, true);

        // Ensure text not editable
        titleText.setEditable(false);

        // Add to JFrame
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(titleText, gbc);
    }

    /**
     * Creates the picture JLabel and adds it to the Billboard Viewer
     * @param xmlInfo The HashMap containing the information from the XML parser
     * @param gbc The constraints for the GridBagLayout
     */
    private void createPictureLabel(HashMap<String, String> xmlInfo, GridBagConstraints gbc) {
        JLabel pictureLabel = new JLabel();

        // Provide a way to decode and display images
        ImageGenerator imgGen = new ImageGenerator();

        // Get image information
        String imgInfo = xmlInfo.get("picture");

        try {

            // Get image as BufferedImage depending on if it is base64 encoded or URL
            BufferedImage picture = imgGen.isBase64EncodedImage(imgInfo) ? imgGen.decodeDataString(imgInfo) : imgGen.downloadImage(imgInfo);

            // Get picture widths
            int resizedWidth = picture.getWidth();
            int resizedHeight = picture.getHeight();

            // If the width needs to be resized, resize whil maintaining aspect ratio
            if (picture.getWidth() != screenSize.width / 2) {
                resizedWidth = screenSize.width / 2;
                resizedHeight = (resizedWidth * picture.getHeight()) / picture.getWidth();
            }

            // If the height needs to be resized, resize while maintaining aspect ratio
            if (picture.getHeight() != screenSize.height / 2) {
                resizedHeight = screenSize.height / 2;
                resizedWidth = (resizedHeight * picture.getWidth()) / picture.getHeight();
            }

            // Rescale the image
            Image resizedImage = picture.getScaledInstance(
                    resizedWidth,
                    resizedHeight,
                    Image.SCALE_SMOOTH
            );

            // Add the Image as ImageIcon
            pictureLabel.setIcon(new ImageIcon(resizedImage));

        } catch (IOException | BadImageFormatException e) {
            e.printStackTrace();
        }

        // Add to JFrame
        gbc.gridy = 1;
        this.add(pictureLabel, gbc);
    }

    /**
     * Creates the information JTextPane and adds it to the Billboard Viewer
     * @param xmlInfo The HashMap containing the information from the XML parser
     * @param bgColour The background colour of the billboard
     * @param set The styling set for the JTextPane
     * @param gbc The constraints for the GridBagLayout
     */
    private void createInformationPane(HashMap<String, String> xmlInfo, Color bgColour, MutableAttributeSet set, GridBagConstraints gbc) {

        // Setup JTextPane
        JTextPane informationText = new JTextPane();
        informationText.setText(xmlInfo.get("information"));
        informationText.setBackground(bgColour);

        // Set styling specific to the information text pane
        StyleConstants.setFontSize(set, 36);
        StyleConstants.setForeground(
                set,
                HexToRGB(
                        xmlInfo.getOrDefault("informationColour", "#000000")
                )
        );
        informationText.setParagraphAttributes(set, true);

        // Ensure the text cannot be changed
        informationText.setEditable(false);

        // Add to the JFrame
        gbc.gridy = 2;
        this.add(informationText, gbc);
    }

    /**
     * Gui Display class
     * @param xmlInfo The HashMap storing infomation parsed from a
     *                given XML document in cab302.viewer.XMLParser
     */
    public Gui(HashMap<String, String> xmlInfo) {

        // Window Configuration \\
        // Set window to fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Set overall background colour
        String bgString = xmlInfo.getOrDefault("bgColour", "#FFFFFF");
        Color bgColour = HexToRGB(bgString);
        getContentPane().setBackground(bgColour);

        // Layout and closing
        GridBagLayout layout = new GridBagLayout();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(layout);

        // Get close action
        Action close = new CloseAction("close", "Closes the app", KeyEvent.VK_ESCAPE);

        // GridBag Constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1;
        gbc.weightx = 1;

        // Input Handling \\
        // Set up root pane to handle inputs
        JRootPane root = this.getRootPane();
        root.getActionMap().put("CLOSE_APPLICATION", close);
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "CLOSE_APPLICATION");
        root.addMouseListener(mouseListener);

        // Styling \\
        // Create global styling component
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(set, "Arial");

        // XML display
        // Create heading label if exists and add it to the window
        if (xmlInfo.containsKey("message")) {
            this.createMessagePane(xmlInfo, bgColour, set, gbc);
        }

        // Create picture label if exists and add it to the window
        if (xmlInfo.containsKey("picture")) {
            this.createPictureLabel(xmlInfo, gbc);
        }

        // Create information label if exists and add it to the window
        if (xmlInfo.containsKey("information")) {
            this.createInformationPane(xmlInfo, bgColour, set, gbc);
        }

        // Show window
        setVisible(true);
    }
}