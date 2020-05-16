package cab302.controlpanel;

import cab302.controlpanel.data.BillboardData;
import cab302.controlpanel.data.UserData;
import cab302.controlpanel.gui.HomeUI;

import javax.swing.*;

public class Run {
    private static void createAndShowGUI() {
        HomeUI GUI = new HomeUI(new UserData(), new BillboardData());
        GUI.setVisible(true);
    }
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(Run::createAndShowGUI);
    }
}
