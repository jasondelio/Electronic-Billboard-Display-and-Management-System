package cab302.viewer.gui;

import javax.swing.*;
import java.io.IOException;
import java.util.Timer;

public class RunViewr {
    private static void createAndShowGUI(Gui GUI) throws IOException, ClassNotFoundException{
        Timer time = new Timer();
        time.scheduleAtFixedRate(new Timer15(GUI),15000,15000);
    }
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
