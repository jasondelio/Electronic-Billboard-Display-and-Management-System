package cab302.ControlPanel;

import javax.swing.*;
import java.io.IOException;

public class Run {
    private static void createAndShowGUI() throws IOException, ClassNotFoundException {
//        ArrayList<String> lists = new ArrayList<String>();
//        lists.add("True");
//        lists.add("True");
//        lists.add("True");
//        lists.add("True");
//
//        HomeUI GUI  = new HomeUI("c2ca4386faa362c94a556624d3f19ac3fd5a81ca27b19700456e1d8eeade8274",lists);
        UserLoginUI GUI = new UserLoginUI();
        GUI.setVisible(true);
    }
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
