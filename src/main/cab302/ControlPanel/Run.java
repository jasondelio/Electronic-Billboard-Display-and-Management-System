package cab302.ControlPanel;

import javax.swing.*;
import java.io.IOException;

public class Run {
//    private Socket socket;
//    private OutputStream outputStream;
//    private InputStream inputStream;
//    private ObjectInputStream ois;
//    private ObjectOutputStream oos;

    private static void createAndShowGUI() throws IOException, ClassNotFoundException {
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
