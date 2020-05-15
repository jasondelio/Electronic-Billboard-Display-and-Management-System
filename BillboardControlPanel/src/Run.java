import javax.swing.*;

public class Run {
    private static void createAndShowGUI() {
        HomeUI GUI = new HomeUI(new UserData(), new BillboardData());
        GUI.setVisible(true);
    }
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
