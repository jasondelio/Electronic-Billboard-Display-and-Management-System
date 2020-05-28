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

    private void createRootStyle() {
        set = new SimpleAttributeSet();
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(set, "Arial");
    }

    public DisplayAssembler(HashMap<String,String> xmlInformation, Dimension frameSize) {
        xmlInfo = xmlInformation;
        screenSize = frameSize;

        this.createRootStyle();
    }

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

    public DisplayAssembler(HashMap<String,String> xmlInformation, int width, int height) {
        this(xmlInformation, new Dimension(width, height));
    }

    public DisplayAssembler(String message, String messageColour, String imageData, String information, String informationColour, int width, int height) {

        this(message, messageColour, imageData, information, informationColour, new Dimension(width, height));
    }

    public JTextPane assembleMessagePane(Color bgColour) throws MalformedHexadecimalColourException {
        JTextPane titleText = null;

        if (!Objects.equals(xmlInfo.get("message"), null) && xmlInfo.containsKey("message") && xmlInfo.get("message").length() != 0) {
            titleText = new JTextPane();
            titleText.setText(xmlInfo.get("message"));
            titleText.setFont(new Font("Arial", Font.PLAIN, 84));

            titleText.setBackground(bgColour);
            StyleConstants.setFontSize(this.set, 84);
            StyleConstants.setForeground(
                    this.set,
                    HexToRGB(
                            xmlInfo.getOrDefault("messageColour", "#000000")
                    )
            );

            titleText.setParagraphAttributes(this.set, true);
            titleText.setEditable(false);
        }

        return titleText;
    }

    public JTextPane assembleInformationPane(Color bgColour) throws MalformedHexadecimalColourException {
        JTextPane informationText = null;

        if (!Objects.equals(xmlInfo.get("information"), null) && xmlInfo.containsKey("information") && xmlInfo.get("information").length() != 0) {
            informationText = new JTextPane();
            informationText.setText(xmlInfo.get("information"));
            informationText.setFont(new Font("Arial", Font.PLAIN, 36));
            informationText.setBackground(bgColour);

            StyleConstants.setFontSize(this.set, 36);
            StyleConstants.setForeground(
                    this.set,
                    HexToRGB(
                            xmlInfo.getOrDefault("informationColour", "#000000")
                    )
            );

            informationText.setParagraphAttributes(this.set, true);
            informationText.setEditable(false);
        }

        return informationText;
    }

    public JLabel assemblePictureLabel() throws IOException, BadImageFormatException {

        JLabel pictureLabel = null;

        if (!Objects.equals(xmlInfo.get("picture"), null) && xmlInfo.containsKey("picture") && xmlInfo.get("picture").length() != 0) {
            pictureLabel = new JLabel();

            String imgInfo = xmlInfo.get("picture");

            ImageGenerator imgGen = new ImageGenerator();

            BufferedImage picture = imgGen.isBase64EncodedImage(imgInfo) ? imgGen.decodeDataString(imgInfo) : imgGen.downloadImage(imgInfo);

            int resizedWidth = picture.getWidth();
            int resizedHeight = picture.getHeight();

            if (picture.getWidth() != this.screenSize.width / 2) {
                resizedWidth = this.screenSize.width / 2;
                resizedHeight = (resizedWidth * picture.getHeight()) / picture.getWidth();
            }

            if (picture.getHeight() != this.screenSize.height / 2) {
                resizedHeight = this.screenSize.height / 2;
                resizedWidth = (resizedHeight * picture.getWidth()) / picture.getHeight();
            }

            Image resizedImage = picture.getScaledInstance(
                    resizedWidth,
                    resizedHeight,
                    Image.SCALE_SMOOTH
            );
            pictureLabel.setIcon(new ImageIcon(resizedImage));
        }

        return pictureLabel;
    }

}
