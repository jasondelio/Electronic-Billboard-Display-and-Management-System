package cab302.viewer.gui;

import javax.swing.*;
import java.io.IOException;
import java.util.Timer;

public class RunViewer {
    public static Timer time;

    /**
     * Create the Viewer Gui. For thread safety,
     * this method should be invoked from the
     * event-dispatching thread. The gui will be refresh every 15 seconds by using timer.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void createAndShowGUI(Gui GUI) throws IOException, ClassNotFoundException {
        // set timer to update 15 seconds
        time = new Timer();
        time.scheduleAtFixedRate(new Timer15(GUI), 15000, 15000);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Gui GUI = new Gui();
                    GUI.setVisible(true);
                    createAndShowGUI(GUI);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
