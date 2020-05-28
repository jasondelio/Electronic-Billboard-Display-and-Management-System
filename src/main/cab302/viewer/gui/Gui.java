package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;
import cab302.viewer.exceptions.MalformedHexadecimalColourException;

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
        Color bgColour;
        try {
            bgColour = HexToRGB(bgString);
        } catch (MalformedHexadecimalColourException e) {
            bgColour = new Color(255,255,255);
        }
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


        // ## XML display ## \\
        DisplayAssembler displayAssembler = new DisplayAssembler(xmlInfo, Toolkit.getDefaultToolkit().getScreenSize());

        JTextPane titleText = null;
        JTextPane informationText = null;
        JLabel pictureLabel = null;
        try {
            titleText = displayAssembler.assembleMessagePane(bgColour);
            informationText = displayAssembler.assembleInformationPane(bgColour);
            pictureLabel = displayAssembler.assemblePictureLabel();
        } catch (MalformedHexadecimalColourException | BadImageFormatException | IOException ignored) {}


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