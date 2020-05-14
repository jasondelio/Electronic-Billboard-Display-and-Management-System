package cab302.server;

import cab302.database.user.UserInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

public class billboardServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        ServerSocket serverSocket = new ServerSocket(3306);

        Map<String,String> tokens = null;
        for (;;) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected to " + socket.getInetAddress());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object o = ois.readObject();
            if (o instanceof Myclass){
                Myclass myclass = (Myclass) o;
                System.out.printf("User try to login with username %s and hasedpassword %s\n",
                        myclass.getUsername(),myclass.getPassword());
                UserInfo u = new UserInfo();
                String salt = u.getId();
                String saltHashedpass = u.getPasswords();
                if (loginSuccess(saltHashedpass,getSaltHashedPass(myclass.getPassword(),salt))){
                    String token = getSessionTokenString();
                    tokens.put("1",token);
                }else {
                    System.out.println("Invalid log in");
                }

            } else {
                System.out.println("Invalid request");
            }


            oos.close();
            ois.close();

            socket.close();
        }
    }
    private static String getSaltHashedPass (String hashedpassword, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String SaltHashedPassword= bytesToString(md.digest((hashedpassword + salt).getBytes()));

        return SaltHashedPassword;
    }
    public static String bytesToString(byte[] hash){
        StringBuffer str_buff = new StringBuffer();
        for (byte b : hash){
            str_buff.append(String.format("%02x", b & 0xFF));
        }
        return str_buff.toString();
    }
    private static String getSessionTokenString() {
        Random rng = new Random();
        byte[] token_bytes = new byte[32];
        rng.nextBytes(token_bytes);
        String token_str = bytesToString(token_bytes);
        return token_str;
    }
    private static boolean loginSuccess(String dbpassword, String password){
        if (password.equals(dbpassword)) return true;
        return false;
    }
}
