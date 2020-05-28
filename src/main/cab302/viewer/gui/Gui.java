package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;

import javax.swing.*;
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
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public Gui(HashMap<String, String> xmlInfo) {

        // ## Window Configuration ## \\
        // Set window to fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        String bgString = xmlInfo.getOrDefault("bgColour", "#FFFFFF");
        Color bgColour = HexToRGB(bgString);
        getContentPane().setBackground(bgColour);

        // Layout and closing
        GridBagLayout layout = new GridBagLayout();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(layout);

        // ## Utilities and Actions ## \\
        // Get close action
        Action close = new CloseAction("close", "Closes the app", KeyEvent.VK_ESCAPE);

        // Provide a way to decode and display images
        ImageGenerator imgGen = new ImageGenerator();


        // ## GridBag Constraints ## \\
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1;
        gbc.weightx = 1;


        // ## Input Handling ## \\
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

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(set, "Arial");

        // ## XML display ## \\
        // Create heading label if exists and add it to the window
        if (xmlInfo.containsKey("message")) {
            JTextPane titleText = new JTextPane();
            titleText.setText(xmlInfo.get("message"));
            titleText.setFont(new Font("Arial", Font.PLAIN, 84));

            titleText.setBackground(bgColour);
            StyleConstants.setFontSize(set, 84);
            StyleConstants.setForeground(
                    set,
                    HexToRGB(
                            xmlInfo.getOrDefault("messageColour", "#000000")
                    )
            );

            titleText.setParagraphAttributes(set, true);
            titleText.setEditable(false);

            gbc.gridx = 0;
            gbc.gridy = 0;
            add(titleText, gbc);
        }

        // Create picture label if exists and add it to the window
        if (xmlInfo.containsKey("picture")) {
            JLabel pictureLabel = new JLabel();
            String imgInfo = xmlInfo.get("picture");

            try {
                BufferedImage picture = imgGen.isBase64EncodedImage(imgInfo) ? imgGen.decodeDataString(imgInfo) : imgGen.downloadImage(imgInfo);

                int resizedWidth = picture.getWidth();
                int resizedHeight = picture.getHeight();

                if (picture.getWidth() != screenSize.width / 2) {
                    resizedWidth = screenSize.width / 2;
                    resizedHeight = (resizedWidth * picture.getHeight()) / picture.getWidth();
                }

                if (picture.getHeight() != screenSize.height / 2) {
                    resizedHeight = screenSize.height / 2;
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
            add(pictureLabel, gbc);
        }

        // Create information label if exists and add it to the window
        if (xmlInfo.containsKey("information")) {
            JTextPane informationText = new JTextPane();
            informationText.setText(xmlInfo.get("information"));
            informationText.setBackground(bgColour);

            StyleConstants.setFontSize(set, 36);
            StyleConstants.setForeground(
                    set,
                    HexToRGB(
                            xmlInfo.getOrDefault("informationColour", "#000000")
                    )
            );

            informationText.setParagraphAttributes(set, true);
            informationText.setEditable(false);

            gbc.gridy = 2;
            add(informationText, gbc);
        }

        // Show window
        setVisible(true);
    }
}