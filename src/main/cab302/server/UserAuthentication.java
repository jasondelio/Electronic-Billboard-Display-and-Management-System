package cab302.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Random;

public class UserAuthentication {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        // in control panel
        String password = "password";
        String firsthashedPass = getHashedPass(password);
        System.out.println(firsthashedPass);
        // in server
        String salt = getSaltString();
        System.out.println(salt);
        String newone = getSaltHashedPass(firsthashedPass,salt);
        System.out.println(newone);
    }
    //get idea from week 9 assignment Q & A
    private static String getSaltHashedPass (String hashedpassword, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String SaltHashedPassword= bytesToString(md.digest((hashedpassword + salt).getBytes()));

        return SaltHashedPassword;
    }
    //get idea from week 9 assignment Q & A
    private static String getHashedPass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] temp_byte = md.digest(password.getBytes());
        String hashedPassword= bytesToString(temp_byte);

        return hashedPassword;
    }
    // get idea from week 9 assignment Q & A
    private static String getSaltString() {
        Random rng = new Random();
        byte[] salt_bytes = new byte[32];
        rng.nextBytes(salt_bytes);
        String salt_str = bytesToString(salt_bytes);
        return salt_str;
    }
    // from week 9 assignment Q & A
    public static String bytesToString(byte[] hash){
        StringBuffer str_buff = new StringBuffer();
        for (byte b : hash){
            str_buff.append(String.format("%02x", b & 0xFF));
        }
        return str_buff.toString();
    }
}

