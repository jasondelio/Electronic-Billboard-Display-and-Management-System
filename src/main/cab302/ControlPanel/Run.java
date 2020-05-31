package cab302.ControlPanel;

import javax.swing.*;
import java.io.IOException;

/**
 * Invokes the billboard control panel application.
 */
public class Run {
    /**
     * Create the UserLoginUI. For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void createAndShowGUI() throws IOException, ClassNotFoundException {
        UserLoginUI GUI = new UserLoginUI();
        GUI.setVisible(true);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
