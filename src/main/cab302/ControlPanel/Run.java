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

//    public void socketStart() throws IOException{
//        socket = new Socket("localhost",12345);
//        outputStream = socket.getOutputStream();
//        inputStream = socket.getInputStream();
//        oos = new ObjectOutputStream(outputStream);
//        ois = new ObjectInputStream(inputStream);
//    }
//
//    public void socketStop() throws IOException{
//        ois.close();
//        oos.close();
//        socket.close();
//    }
}
