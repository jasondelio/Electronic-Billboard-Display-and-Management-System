package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

import static cab302.viewer.util.HexToRGB.HexToRGB;

public class Gui extends JFrame {

    private JFrame frame;

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
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });


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

            gbc.gridx = 0;
            gbc.gridy = 0;
            add(titleText, gbc);
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

            gbc.gridy = 1;
            add(pictureLabel, gbc);
        }

        // Create information label if exists and add it to the window
        if (xmlInfo.containsKey("information")) {
            JTextArea informationText = new JTextArea();
            String[] infoStringArr = xmlInfo.get("information").split("%%%%");
            informationText.setText(infoStringArr[0]);
            informationText.setFont(new Font("Arial", Font.PLAIN, 36));
            informationText.setForeground(
                    HexToRGB(
                        infoStringArr.length > 1
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

            gbc.gridy = 2;
            add(informationText, gbc);
        }

        // Show window
        setVisible(true);
    }
}
