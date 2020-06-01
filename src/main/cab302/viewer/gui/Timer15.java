package cab302.viewer.gui;

import java.io.IOException;
import java.util.TimerTask;

public class Timer15 extends TimerTask {
    private Gui GUI;
    Timer15 (Gui GUI){
        this.GUI = GUI;
    }
    public void run() {
        try {
            try {
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
