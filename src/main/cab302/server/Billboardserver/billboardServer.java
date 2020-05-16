package cab302.server.Billboardserver;

import cab302.database.user.UserInfo;
import cab302.server.UserServer.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class billboardServer {
    // from week 10 Q and A session
    private static String validSessionToken = null;
    public static HashMap<String, String> validSessionTokens = new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        ServerSocket serverSocket = new ServerSocket(3306);

        Map<String,String> tokens = null;
        for (;;) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected to " + socket.getInetAddress());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object o = ois.readObject();
            if (o instanceof Loginrequest){
                Loginrequest loginrequest = (Loginrequest) o;
                System.out.printf("User try to login with username %s and hasedpassword %s\n",
                        loginrequest.getUsername(), loginrequest.getPassword());
                UserInfo u = new UserInfo();
                //get salt and stored password from database
                String salt = u.getId();
                // String saltHashedPass = u.getPasswords();
                // temporary
                String saltHashedPass = "7462fc6c69047ae29e3067a07c9a25abdf74e3690aab6032aa2c32e70df42e6d";
                //System.out.println(getSaltHashedPass(loginrequest.getPassword(),salt));
                LoginReply reply;
                // if the password with salting and hashing is same as stored password, return login succeed.
                if (loginSuccess(saltHashedPass,getSaltHashedPass(loginrequest.getPassword(),salt))){
                    String token = getSessionTokenString();
                    reply = new LoginReply(true,token);
                    validSessionTokens.put(token, loginrequest.getUsername());
                }else {
                    reply = new LoginReply(false,null);
                }
                oos.writeObject(reply);
                oos.flush();
            } else if(o instanceof BillboardRequest){
                System.out.println("Client request billboard content");
                BillboardReply billboardreply = new BillboardReply("billboard1");
                oos.writeObject(billboardreply);
                oos.flush();
            } else if(o instanceof listBillboardRequest){
                System.out.println("Client request billboard lists");
                listBillboardRequest lbr =(listBillboardRequest) o;
                String sessionToken = lbr.getSessionToken();
                System.out.println("client requested billboard lists with token :"+ sessionToken);
                if (isValidSessionToken(sessionToken)){
                    String username = getSessionUsername(sessionToken);
                    listBillboardReply listBillboardReply = new listBillboardReply(null);
                    oos.writeObject(listBillboardReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }else if(o instanceof CreateBillboardRequest){
                System.out.println("Client request creating billboard");
                CreateBillboardRequest cbbr =(CreateBillboardRequest) o;
                String sessionToken = cbbr.getSessionToken();
                System.out.println("client requested billboard lists with token :"+ sessionToken);
                boolean CreateBillboards;
                CreateBillboards = true;
                if (isValidSessionToken(sessionToken) & CreateBillboards){
                    String username = getSessionUsername(sessionToken);
                    CreateBillboardReply createBillboardReply = new CreateBillboardReply("create/edit the billlss");
                    oos.writeObject(createBillboardReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof DelateBillboardRequest){
                System.out.println("Client request creating billboard");
                DelateBillboardRequest cbbr =(DelateBillboardRequest) o;
                String sessionToken = cbbr.getSessionToken();
                System.out.println("client requested billboard lists with token :"+ sessionToken);
                boolean CreateBillboards;
                CreateBillboards = true;
                if (isValidSessionToken(sessionToken) & CreateBillboards){
                    String username = getSessionUsername(sessionToken);
                    DelateBillboardReply delateBillboardReply = new DelateBillboardReply("Delate the billbord");
                    oos.writeObject(delateBillboardReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof ViewBillboardRequest){
                System.out.println("Client request creating billboard");
                ViewBillboardRequest vbbr =(ViewBillboardRequest) o;
                String sessionToken = vbbr.getSessionToken();
                System.out.println("client requested billboard lists with token :"+ sessionToken);
                boolean CreateBillboards;
                CreateBillboards = true;
                if (isValidSessionToken(sessionToken) & CreateBillboards){
                    String username = getSessionUsername(sessionToken);
                    ViewBillboardReply viewbillbord = new ViewBillboardReply("View the billbord");
                    oos.writeObject(viewbillbord);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof ScheduleBillboardRequest){
                System.out.println("Client request schedule billboard");
                ScheduleBillboardRequest sbbr =(ScheduleBillboardRequest) o;
                String sessionToken = sbbr.getSessionToken();
                System.out.println("client requested billboard lists with token :"+ sessionToken);
                boolean CreateBillboards;
                CreateBillboards = true;
                if (isValidSessionToken(sessionToken) & CreateBillboards){
                    String username = getSessionUsername(sessionToken);
                    ScheduleBillboardReply delateBillboardReply = new ScheduleBillboardReply("Schedule the billbord");
                    oos.writeObject(delateBillboardReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof RemoveBillboardRequest){
                System.out.println("Client request remove billboard schedule");
                RemoveBillboardRequest rbbr =(RemoveBillboardRequest) o;
                String sessionToken = rbbr.getSessionToken();
                System.out.println("client requested billboard lists with token :"+ sessionToken);
                boolean CreateBillboards;
                CreateBillboards = true;
                if (isValidSessionToken(sessionToken) & CreateBillboards){
                    String username = getSessionUsername(sessionToken);
                    RemoveBillboardReply remove_the_billbord = new RemoveBillboardReply("Remove the billbord");
                    oos.writeObject(remove_the_billbord);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof listUsersRequest){
                System.out.println("------------------------------------------------------");
                System.out.println("Client request user lists");
                listUsersRequest lur =(listUsersRequest) o;
                String sessionToken = lur.getSessionToken();
                System.out.println("client requested user lists with token :"+ sessionToken);
                //UserInfo u;
                //editUsers = u.getPermissionEditUser();
                boolean editUsers;
                editUsers = true;
                if (isValidSessionToken(sessionToken) & editUsers == true){
                    String username = getSessionUsername(sessionToken);
                    listUsersReply listUsersReply = new listUsersReply(null);
                    oos.writeObject(listUsersReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }else if(o instanceof CreateUsersRequest){
                System.out.println("Client request user Creating");
                CreateUsersRequest cur =(CreateUsersRequest) o;
                String sessionToken = cur.getSessionToken();
                System.out.println("client requested user creating with token :"+ sessionToken);
                //UserInfo u;
                //editUsers = u.getPermissionEditUser();
                boolean editUsers;
                editUsers = true;
                if (isValidSessionToken(sessionToken) & editUsers){
                    //String password = cur.getPassword();
                    //String name = cur.getUsername();
                    //ArrayList<String> permissions = lur.getLists_Permissions();
                    String salt = getSaltString();
                    //UserInfo u = new UserInfo (name,salt,permissions,password);
                    //UserData.add(u)
                    CreateUsersReply createUsersReply = new CreateUsersReply(null);
                    oos.writeObject(createUsersReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof GetUserPemmRequest){
                System.out.println("Client request user Permissions request");
                GetUserPemmRequest gupr =(GetUserPemmRequest) o;
                String sessionToken = gupr.getSessionToken();
                System.out.println("client requested user creating with token :"+ sessionToken);
                //UserInfo u;
                //editUsers = u.getPermissionEditUser();
                boolean editUsers;
                editUsers = true;
                if (isValidSessionToken(sessionToken) & editUsers){
                    System.out.println("request");
                    //ArrayList<String> permissions = lur.getLists_Permissions();
                    //UserInfo u = new UserInfo (name,salt,permissions,password);
                    //UserData.add(u)
                    GetUserpemmReply getUserpemmReply = new GetUserpemmReply(null);
                    oos.writeObject(getUserpemmReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof SetUserPemmRequest){
                System.out.println("Client request setting user Permissions request");
                SetUserPemmRequest gupr =(SetUserPemmRequest) o;
                String sessionToken = gupr.getSessionToken();
                System.out.println("client requested setting permissions with token :"+ sessionToken);
                //UserInfo u;
                //editUsers = u.getPermissionEditUser();
                boolean editUsers;
                editUsers = true;
                if (isValidSessionToken(sessionToken) & editUsers){
                    System.out.println("request");
                    ArrayList<String> permissions = gupr.getPermisssions();
                    //UserInfo u = new UserInfo (name,salt,permissions,password);
                    //UserData.add(u)
                    SetUserpemmReply setUserPemmReply = new SetUserpemmReply(permissions);
                    oos.writeObject(setUserPemmReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof SetPassRequest){
                System.out.println("Client request setting user Permissions request");
                SetPassRequest spr =(SetPassRequest) o;
                String hashedPassword = spr.getHashedPassword();
                System.out.println("client requested setting permissions with token :"+ hashedPassword);
                //UserInfo u;
                //editUsers = u.getPermissionEditUser();
                boolean editUsers;
                editUsers = true;
                if (editUsers){
                    System.out.println("successfully change password");
                    String username = spr.getUsername();
                    //UserInfo u = new UserInfo (name,salt,permissions,password);
                    //UserData.add(u)
                    SetPassReply setPassReply = new SetPassReply(null);
                    oos.writeObject(setPassReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof DelateUserRequest){
                System.out.println("Client request delate user request");
                DelateUserRequest dur =(DelateUserRequest) o;
                String sessiontoken = dur.getSessiontoken();
                System.out.println("client requested setting permissions with token :"+ sessiontoken);
                //UserInfo u;
                //editUsers = u.getPermissionEditUser();
                boolean editUsers;
                editUsers = true;
                if (isValidSessionToken(sessiontoken) & editUsers){
                    System.out.println("successfully delate user");
                    String username = dur.getUsername();
                    //UserInfo u = new UserInfo (name,salt,permissions,password);
                    //UserData.add(u)
                    DelateUserReply delateUserReply = new DelateUserReply(null);
                    oos.writeObject(delateUserReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof LogoutUsersRequest){
                System.out.println("Client request logout user request");
                LogoutUsersRequest lour =(LogoutUsersRequest) o;
                String sessiontoken = lour.getSessionToken();
                System.out.println("client requested expireds with token :"+ sessiontoken);
                //UserInfo u;
                //editUsers = u.getPermissionEditUser();

                validSessionTokens.remove(sessiontoken);
                System.out.println("successfully logout user");
                //UserInfo u = new UserInfo (name,salt,permissions,password);
                //UserData.add(u)
                LogoutUserReply logoutUserReply = new LogoutUserReply(null);
                oos.writeObject(logoutUserReply);
                oos.flush();

            }
            else{
                System.out.println("Invalid request");
            }
            oos.close();
            ois.close();
            socket.close();
        }
    }

    private static boolean isValidSessionToken(String sessionToken) {
        return validSessionTokens.containsKey(sessionToken);
    }
    private static String getSessionUsername(String sessionToken) {
        return validSessionTokens.get(sessionToken);
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
    // get idea from week 9 assignment Q & A
    private static String getSaltString() {
        Random rng = new Random();
        byte[] salt_bytes = new byte[32];
        rng.nextBytes(salt_bytes);
        String salt_str = bytesToString(salt_bytes);
        return salt_str;
    }
    //
}
