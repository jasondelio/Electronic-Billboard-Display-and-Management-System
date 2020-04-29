package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;
import cab302.viewer.gui.actions.CloseAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

public class Gui extends JFrame {

    private JFrame frame;

    public Gui(HashMap<String, String> xmlInfo) {

        // Get close action
        Action close = new CloseAction("close", "Closes the app", KeyEvent.VK_ESCAPE);

        // Provide a way to decode and display images
        ImageGenerator imgGen = new ImageGenerator();

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
        // Create heading label if exists and add it to the window
        if (xmlInfo.containsKey("message")) {
            JLabel titleText = new JLabel();
            titleText.setText(xmlInfo.get("message"));
            add(titleText);
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
            add(pictureLabel);
        }

        // Create information label if exists and add it to the window
        if (xmlInfo.containsKey("information")) {
            JLabel informationText = new JLabel();
            informationText.setText(xmlInfo.get("information"));
            add(informationText);
        }


        // ## Window Configuration ## \\
        // Set window to fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Layout and closing
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3,3));
        setVisible(true);
    }
}
