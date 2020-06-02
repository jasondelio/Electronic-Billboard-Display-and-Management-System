package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;
import cab302.viewer.exceptions.MalformedHexadecimalColourException;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static cab302.viewer.util.HexToRGB.HexToRGB;

public class DisplayAssembler {

    private HashMap<String,String> xmlInfo;
    private Dimension screenSize;
    private SimpleAttributeSet set;
    /**
     * Creates a basic style set for Swing components
     */
    private void createRootStyle() {
        set = new SimpleAttributeSet();
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(set, "Arial");
    }
    /**
     * Constructor for the display assembler
     * @param xmlInformation HashMap storing data parsed from the XML contents of a billboard
     * @param frameSize The screen size of the component to add to
     */
    public DisplayAssembler(HashMap<String,String> xmlInformation, Dimension frameSize) {
        xmlInfo = xmlInformation;
        screenSize = frameSize;

        this.createRootStyle();
    }

    /**
     * Constructor for Display Assembler
     * @param message The text to display as a title
     * @param messageColour The title text colour
     * @param imageData The URL/Data string to get the image to display from
     * @param information The text to display as an information text
     * @param informationColour The information text colour
     * @param frameSize The size of the screen to create a label for
     */
    public DisplayAssembler(String message, String messageColour, String imageData, String information, String informationColour, Dimension frameSize) {
        xmlInfo = new HashMap<>();
        xmlInfo.put("message", message);
        xmlInfo.put("messageColour", messageColour);
        xmlInfo.put("picture", imageData);
        xmlInfo.put("information", information);
        xmlInfo.put("informationColour", informationColour);

        screenSize = frameSize;

        this.createRootStyle();
    }
    /**
     * Constructor for Display Assembler
     * @param xmlInformation HashMap storing data parsed from the XML contents of a billboard
     * @param width The width of the component to add to
     * @param height The height of the component to add to
     */
    public DisplayAssembler(HashMap<String,String> xmlInformation, int width, int height) {
        this(xmlInformation, new Dimension(width, height));
    }
    /**
     * Constructor for Display Assembler
     * @param message The text to display as a title
     * @param messageColour The title text colour
     * @param imageData The URL/Data string to get the image to display from
     * @param information The text to display as an information text
     * @param informationColour The information text colour
     * @param width The width of the component to add to
     * @param height The height of the component to add to
     */
    public DisplayAssembler(String message, String messageColour, String imageData, String information, String informationColour, int width, int height) {

        this(message, messageColour, imageData, information, informationColour, new Dimension(width, height));
    }
    /**
     * Method to create a text pane with large text as a heading
     * @param bgColour The background colour of the billboard
     * @return A JTextPane containing the content of the "message" key in the xmlInfo Hashmap
     * @throws MalformedHexadecimalColourException if the "messageColour" key is not a valid
     * hexadecimal colour
     */
    public JTextPane assembleMessagePane(Color bgColour) throws MalformedHexadecimalColourException {
        // Initialise the JTextPane
        JTextPane titleText = null;
        // Check that xmlInfo contains a non-null, non-empty string for key "message"
        if (!Objects.equals(xmlInfo.get("message"), null) && xmlInfo.containsKey("message") && xmlInfo.get("message").length() != 0) {
            // Add text to the pane
            titleText = new JTextPane();
            titleText.setText(xmlInfo.get("message"));
            titleText.setPreferredSize(new Dimension(this.screenSize.width, this.screenSize.height/3));

            // Setup styling
            titleText.setBackground(bgColour);
            StyleConstants.setFontSize(this.set, 84);
            StyleConstants.setForeground(
                    this.set,
                    HexToRGB(
                            xmlInfo.getOrDefault("messageColour", "#000000")
                    )
            );
            // Ensure the styles are applied and the JTextPane is not editable by the user
            titleText.setParagraphAttributes(this.set, true);
            titleText.setEditable(false);
        }

        return titleText;
    }
    /**
     * Method to create a text pane with medium text as a more information text
     * @param bgColour The background colour of the billboard
     * @return A JTextPane containing the content of the "information" key in the xmlInfo Hashmap
     * @throws MalformedHexadecimalColourException if the "informationColour" key is not a valid
     * hexadecimal colour
     */
    public JTextPane assembleInformationPane(Color bgColour) throws MalformedHexadecimalColourException {
        // Initialise the JTextPane
        JTextPane informationText = null;
        // Check that xmlInfo contains a non-null, non-empty string for key "information"
        if (!Objects.equals(xmlInfo.get("information"), null) && xmlInfo.containsKey("information") && xmlInfo.get("information").length() != 0) {
            // Set the text
            informationText = new JTextPane();
            informationText.setText(xmlInfo.get("information"));
            informationText.setPreferredSize(new Dimension(this.screenSize.width, this.screenSize.height/3));

            // Set up styling attributes
            informationText.setBackground(bgColour);
            StyleConstants.setFontSize(this.set, 36);
            StyleConstants.setForeground(
                    this.set,
                    HexToRGB(
                            xmlInfo.getOrDefault("informationColour", "#000000")
                    )
            );
            // Apply attributes and disable user editing
            informationText.setParagraphAttributes(this.set, true);
            informationText.setEditable(false);
        }

        return informationText;
    }
    /**
     * Method to create a label with an image, provided a Base64 or URL string
     * @return A JLabel containing the image provided
     * @throws IOException if there is an issue reading the image
     * @throws BadImageFormatException if the image string is not a URL or Base64 encoded string
     */
    public JLabel assemblePictureLabel() throws IOException, BadImageFormatException {
        // Initialise the JLabel
        JLabel pictureLabel = null;
        // Check that the "picture" key in the HashMap is non-null and non-empty
        if (!Objects.equals(xmlInfo.get("picture"), null) && xmlInfo.containsKey("picture") && xmlInfo.get("picture").length() != 0) {
            pictureLabel = new JLabel();

            String imgInfo = xmlInfo.get("picture");

            ImageGenerator imgGen = new ImageGenerator();

            // Get the image from the image generator depending on if it is Base64 or URL
            BufferedImage picture = imgGen.isBase64EncodedImage(imgInfo)
                    ? imgGen.decodeDataString(imgInfo)
                    : imgGen.downloadImage(imgInfo);
            // Get the original image size
            int resizedWidth = picture.getWidth();
            int resizedHeight = picture.getHeight();

            // If the image does not fit in the full allowed size of the screen, resize while
            // maintaining aspect ratio
            if (picture.getWidth() != this.screenSize.width / 2) {
                resizedWidth = this.screenSize.width / 2;
                resizedHeight = (resizedWidth * picture.getHeight()) / picture.getWidth();
            }

            if (picture.getHeight() != this.screenSize.height / 2) {
                resizedHeight = this.screenSize.height / 2;
                resizedWidth = (resizedHeight * picture.getWidth()) / picture.getHeight();
            }
            // Apply resize to the image
            Image resizedImage = picture.getScaledInstance(
                    resizedWidth,
                    resizedHeight,
                    Image.SCALE_SMOOTH
            );
            // Set the label to the image
            pictureLabel.setIcon(new ImageIcon(resizedImage));
        }

        return pictureLabel;
    }

}
