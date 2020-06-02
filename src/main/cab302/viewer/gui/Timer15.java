package cab302.viewer.gui;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Class updating Viewer Gui with sending request to server by 15 seconds
 */
public class Timer15 extends TimerTask {
    private Gui GUI;
    Timer15 (Gui GUI){
        this.GUI = GUI;
    }
    public void run() {
        try {
            try {
                // dispose previous one first
                GUI.dispose();
                GUI = new Gui();
                GUI.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
