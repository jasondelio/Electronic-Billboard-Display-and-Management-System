package cab302.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class UserAuthentication {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        String passwordToHash = "password";
        String firsthashPass = getHash(passwordToHash);
        String passwordToHash1 = "password";
        String firsthashPass1 = getHash(passwordToHash1);
        System.out.println(firsthashPass);
        System.out.println(firsthashPass1);
        byte[] salt = getSalt();
        byte[] salt1 = getSalt();
        System.out.println(salt);
        System.out.println(salt1);
        String securePassword = get_SHA_256_SecurePassword(firsthashPass, salt);
        System.out.println(securePassword);
        String securePassword1 = get_SHA_256_SecurePassword(firsthashPass1, salt1);
        System.out.println(securePassword1);
    }

    private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        byte[] bytes;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder(64);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            //for(int i=0; i< bytes.length ;i++)
            //{
            //    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            //}
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add hash
    private static String getHash(String passwordToHash) throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] temp_byte;
        String gen = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            temp_byte = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder(64);
            for (byte b : temp_byte) {
                sb.append(String.format("%02x", b));
            }
            gen = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gen;
    }

    //Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }

}

