package cab302.server.Billboardserver;

import cab302.database.billboard.BillboardData;
import cab302.database.billboard.BillboardInfo;
import cab302.database.schedule.ScheduleData;
import cab302.database.schedule.ScheduleInfo;
import cab302.database.user.UserData;
import cab302.database.user.UserInfo;
import cab302.server.WillBeControlPanelAction.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class BillboardServer {
    // from week 10 Q and A session
    private static String validSessionToken = null;
    public static HashMap<String, String> validSessionTokens = new HashMap<>();


    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        ServerSocket serverSocket = new ServerSocket(12345);
        UserData data = new UserData();
        String rootpassword = "root";
        String hashedroot = getHashedPass(rootpassword);
        String firstsalt = getSaltString();
        String hasedsaltpassroot = getSaltHashedPass(hashedroot, firstsalt);
        UserInfo rootUser = new UserInfo("admin", "root", hasedsaltpassroot, firstsalt, "root@gmail.com", "true",
                "true", "true", "true");
        data.add(rootUser);

        String passwords = "daafda";
        String hashedsuzan = getHashedPass(passwords);
        String secondsalt = getSaltString();
        String hasedsaltpasssuzan = getSaltHashedPass(hashedsuzan, secondsalt);
        UserInfo suzanUser = new UserInfo("suzan", "suzan", hasedsaltpasssuzan, secondsalt, "susan@gmail.com", "true",
                "true", "true", "true");
        data.add(suzanUser);

        BillboardData billboardData = new BillboardData();
        ScheduleData scheduleData = new ScheduleData();

        for (; ; ) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected to " + socket.getInetAddress());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object o = ois.readObject();
            if (o instanceof Loginrequest){
                Loginrequest loginrequest = (Loginrequest) o;
                System.out.printf("User try to login with username %s and hasedpassword %s\n",
                        loginrequest.getUsername(), loginrequest.getPassword());
                UserInfo u = data.get(loginrequest.getUsername());
                String loggedinUsername = loginrequest.getUsername();
                //get salt and stored password from database
                String salt = u.getSalt();
                String saltHashedPass = u.getPasswords();
                LoginReply reply;
                ArrayList<String> permissions = new ArrayList<>();
                // if the password with salting and hashing is same as stored password, return login succeed.
                if (loginSuccess(saltHashedPass,getSaltHashedPass(loginrequest.getPassword(),salt))){
                    Date LogedInDate = new Date();
                    String token = getSessionTokenString();
                    permissions.add(u.getCreateBillboards());
                    permissions.add(u.getEditAllBillboards());
                    permissions.add(u.getScheduleBillboards());
                    permissions.add(u.getEditUsers());
                    reply = new LoginReply(true,token, permissions, loggedinUsername);
                    validSessionTokens.put(token, loginrequest.getUsername());
                    System.out.println(validSessionTokens);
                }else {
                    reply = new LoginReply(false,null, null,null);
                }
                oos.writeObject(reply);
                oos.flush();
            }else if (o instanceof UserLoggedInrequest){
                UserLoggedInrequest userLoggedInrequest = (UserLoggedInrequest) o;
                System.out.printf("User try to check if sessiontoken is exist or not ");
                String sessionToken = userLoggedInrequest.getSessionToken();
                //get salt and stored password from database
                AlreadyLoginReply reply;
                ArrayList<String> permissions = new ArrayList<>();
                // if the password with salting and hashing is same as stored password, return login succeed.
                if (isValidSessionToken(sessionToken)){
                    String loggedinUsername = getSessionUsername(sessionToken);
                    UserInfo newu = data.get(loggedinUsername);
                    permissions.add(newu.getCreateBillboards());
                    permissions.add(newu.getEditAllBillboards());
                    permissions.add(newu.getScheduleBillboards());
                    permissions.add(newu.getEditUsers());
                    reply = new AlreadyLoginReply(true, sessionToken, permissions, loggedinUsername);
                } else {
                    reply = new AlreadyLoginReply(false, null, null, null);
                }
                oos.writeObject(reply);
                oos.flush();
            } else if (o instanceof SessionExistRequest) {
                SessionExistRequest ser = (SessionExistRequest) o;
                System.out.printf("User try to get session token");
                SessionExistReply reply;
                System.out.println(validSessionTokens.keySet());
                // if the password with salting and hashing is same as stored password, return login succeed.
                if (validSessionTokens.keySet().isEmpty()) {
                    reply = new SessionExistReply(false, null);
                } else {
                    ArrayList<String> stringList = new ArrayList<String>(validSessionTokens.keySet());
                    reply = new SessionExistReply(true, stringList);
                }
                oos.writeObject(reply);
                oos.flush();
            }
            else if(o instanceof BillboardRequest){
                BillboardRequest br = (BillboardRequest) o;
                String sessionToken =  br.getSessionToken();
                System.out.println("client requested billboard content with token :"+ sessionToken);
                if (isValidSessionToken(sessionToken)){
                    String billname = br.getBillboardname();
                    BillboardInfo new_b = billboardData.get(billname);
                    BillboardReply billboardReply = new BillboardReply(new_b.getXMLContent(), new_b.getCreator());
                    oos.writeObject(billboardReply);
                    oos.flush();
                } else {
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            } else if (o instanceof ListBillboardRequest) {
                ListBillboardRequest lbr = (ListBillboardRequest) o;
                String sessionToken = lbr.getSessionToken();
                System.out.println("client requested billboard lists with token :" + sessionToken);
                if (isValidSessionToken(sessionToken)) {
                    ListBillboardReply lbbr = new ListBillboardReply(billboardData.getModel());
                    oos.writeObject(lbbr);
                    oos.flush();
                } else {
                    AcknowledgeReply reply = new AcknowledgeReply("error");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof CreateBillboardRequest){
                CreateBillboardRequest cbbr =(CreateBillboardRequest) o;
                String sessionToken = cbbr.getSessionToken();
                System.out.println("client requested creating lists with token :"+ sessionToken);
                boolean Schedule = false;
                String results = null;
                if (isValidSessionToken(sessionToken)){
                    String currentUser = getSessionUsername(sessionToken);
                    UserInfo nu = data.get(currentUser);

                    if (nu.getCreateBillboards().equals("true")){
                        BillboardInfo new_b = new BillboardInfo(cbbr.getBillboardname(),cbbr.getBillboardContent(),currentUser);
                        billboardData.add(new_b);
                        results = "Success to create a new billboard";
                    }
                    else{
                        results = "falied to create a new billboard";
                    }

                    AcknowledgeReply createBillboardReply = new AcknowledgeReply(results);
                    oos.writeObject(createBillboardReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof EditBillboardRequest){
                EditBillboardRequest ebbr =(EditBillboardRequest) o;
                String sessionToken = ebbr.getSessionToken();
                System.out.println("client requested creating lists with token :"+ sessionToken);
                boolean Schedule = false;
                String results = null;
                if (isValidSessionToken(sessionToken)){
                    String currentUser = getSessionUsername(sessionToken);
                    UserInfo nu = data.get(currentUser);
                    System.out.println(billboardData.get(ebbr.getBillboardname()));
                    if (billboardData.get(ebbr.getBillboardname()) != null){
                        BillboardInfo edit_b = billboardData.get(ebbr.getBillboardname());
//                        ScheduleInfo edit_si = scheduleData.get(cbbr.getBillboardname());
                        System.out.println(edit_b.getCreator());
                        if(edit_b.getCreator().equals(currentUser) && Schedule == false){
                            if (nu.getCreateBillboards().equals("true")){
                                billboardData.edit(edit_b.getName(),ebbr.getBillboardContent(),edit_b.getName());
                                results = "Success to edit billboard";
                            }else{
                                results = "failed to edit billboard";
                            }
                        }else {
                            if (nu.getEditAllBillboards().equals("true")){
                                billboardData.edit(edit_b.getName(),ebbr.getBillboardContent(),edit_b.getName());
                                results = "Success to edit billboard";
                            }else{
                                results = "falied to edit billboard";
                            }
                        }
                    }
                    AcknowledgeReply editBillboardReply = new AcknowledgeReply(results);
                    oos.writeObject(editBillboardReply);
                    oos.flush();
                } else {
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            } else if (o instanceof DeleteBillboardRequest) {
                DeleteBillboardRequest dbbr = (DeleteBillboardRequest) o;
                String sessionToken = dbbr.getSessionToken();
                System.out.println("client requested delating billboard with token :" + sessionToken);
                boolean schedule = false;
                String results = null;
                if (isValidSessionToken(sessionToken)) {
                    String currentUser = getSessionUsername(sessionToken);
                    UserInfo nu = data.get(currentUser);
                    BillboardInfo edit_b = billboardData.get(dbbr.getBillboardname());
                    //ScheduleInfo edit_si = scheduleData.get(dbbr.getBillboardname());

                    if(edit_b.getCreator().equals(currentUser) && schedule == false){
                        if (nu.getCreateBillboards().equals("true")){
                            billboardData.remove(edit_b.getName());
                            results = "Success to delate";
                        }else{
                            results = "You do not have CreateBillboard permission";
                        }
                    }else {
                        if (nu.getEditAllBillboards().equals("true")){
                            billboardData.remove(edit_b.getName());
                            results = "Success to delate";
                        }else{
                            results = "You do not have EditAllBillboard permissions";
                        }
                    }
                    AcknowledgeReply delateBillboardReply = new AcknowledgeReply(results);
                    oos.writeObject(delateBillboardReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof ViewBillboardRequest){
                ViewBillboardRequest vbbr =(ViewBillboardRequest) o;
                String sessionToken = vbbr.getSessionToken();
                System.out.println("client requested viewing schedule with token :"+ sessionToken);
                if (isValidSessionToken(sessionToken)) {
                    String currentUser= getSessionUsername(sessionToken);
                    UserInfo nu = data.get(currentUser);
                    Set<String> sche_lists = null;
                    if(nu.getScheduleBillboards().equals("true")){
                        sche_lists = scheduleData.getScheduleList();
                    }else{
                        sche_lists = null;
                    }
                    ViewBillboardReply viewbillbord = new ViewBillboardReply(sche_lists);
                    oos.writeObject(viewbillbord);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof ScheduleBillboardRequest){
                ScheduleBillboardRequest sbbr =(ScheduleBillboardRequest) o;
                String sessionToken = sbbr.getSessionToken();
                System.out.println("client requested scheduling the billboard with token :"+ sessionToken);
                if (isValidSessionToken(sessionToken)){
                    String currentUser = getSessionUsername(sessionToken);
                    UserInfo nu = data.get(currentUser);
                    String results = null;
                    if (nu.getScheduleBillboards().equals("true")){
                        ScheduleInfo new_schedule = new ScheduleInfo(sbbr.getBillboardname(),currentUser,sbbr.getMonth(),sbbr.getDate(),
                                sbbr.getHour(),sbbr.getMinitue(),sbbr.getDurationHr(),sbbr.getDurationMin(),sbbr.getRecur());
                        scheduleData.add(new_schedule);
                        results = "Success to schedule the billboard";
                    }else {
                        results = "No permission";
                    }

                    AcknowledgeReply scheduleBillboardReply = new AcknowledgeReply(results);
                    oos.writeObject(scheduleBillboardReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof RemoveBillboardRequest){
                RemoveBillboardRequest rbbr =(RemoveBillboardRequest) o;
                String sessionToken = rbbr.getSessionToken();
                System.out.println("client requested removing billboard from schedule with token :"+ sessionToken);
                if (isValidSessionToken(sessionToken)){
                    String currentUser = getSessionUsername(sessionToken);
                    UserInfo un = data.get(currentUser);
                    String result = null;
                    if (un.getScheduleBillboards().equals("True")){
                        ScheduleInfo new_sche = scheduleData.get(rbbr.getBillboardname());
                        if (new_sche.getBoardTitle().equals(rbbr.getBillboardname()) && new_sche.getMonth().equals(rbbr.getMonth())
                        && new_sche.getDate().equals(rbbr.getDate()) && new_sche.getMinute().equals(rbbr.getMinitue()) )
                        {
                            scheduleData.remove(rbbr.getBillboardname(),rbbr.getDate(),rbbr.getHour());
                            result = "Success to remove bb from schedule !";
                        }
                        else{
                            result = "failed to remove cuz no billboard schedule on parameter time!";
                        }
                    }
                    else{
                        result = "failed to remove!";
                    }
                    AcknowledgeReply remove_the_billbord = new AcknowledgeReply(result);
                    oos.writeObject(remove_the_billbord);
                    oos.flush();
                } else {
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            } else if (o instanceof ListUsersRequest) {
                System.out.println("------------------------------------------------------");
                ListUsersRequest lur = (ListUsersRequest) o;
                String sessionToken = lur.getSessionToken();
                System.out.println("client requested user lists with token :" + sessionToken);
                System.out.println(getSessionUsername(sessionToken));
                UserInfo newu = data.get(getSessionUsername(sessionToken));

                if (isValidSessionToken(sessionToken) & newu.getEditUsers().equals("true")) {
                    ListModel Userlist = data.getModel();
                    ListUsersReply listUsersReply = new ListUsersReply(Userlist);
                    oos.writeObject(listUsersReply);
                    oos.flush();
                }else{
                    AcknowledgeReply reply = new AcknowledgeReply("error");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof CreateUsersRequest){
                CreateUsersRequest cur =(CreateUsersRequest) o;
                String sessionToken = cur.getSessionToken();
                System.out.println("client requested user creating with token :"+ sessionToken);
                UserInfo u;
                UserInfo newu = data.get(getSessionUsername(sessionToken));

                if (isValidSessionToken(sessionToken) & newu.getEditUsers().equals("true")){
                    String salt = getSaltString();
                    String hashedsaltPass = getSaltHashedPass(cur.getPassword(),salt);
                    UserInfo user = new UserInfo(cur.getUsername(),cur.getUsername(),hashedsaltPass,salt, cur.getEmail(), cur.getLists_Permissions().get(0),
                            cur.getLists_Permissions().get(1),cur.getLists_Permissions().get(2),cur.getLists_Permissions().get(3));
                    data.add(user);
                    AcknowledgeReply createUsersReply = new AcknowledgeReply("Succeed to create user !");
                    oos.writeObject(createUsersReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof GetUserPemmRequest){
                GetUserPemmRequest gupr =(GetUserPemmRequest) o;
                String sessionToken = gupr.getSessionToken();
                System.out.println("client requested getting user permissions with token :"+ sessionToken);
                ArrayList<String> permissions = new ArrayList<>();
                UserInfo per_u = null;
                if (isValidSessionToken(sessionToken)){
                    if(getSessionUsername(sessionToken).equals(gupr.getUsername())){
                        per_u = data.get(gupr.getUsername());
                        permissions.add(per_u.getCreateBillboards());
                        permissions.add(per_u.getEditAllBillboards());
                        permissions.add(per_u.getScheduleBillboards());
                        permissions.add(per_u.getEditUsers());

                    }else{
                        per_u = data.get(gupr.getUsername());
                        if(per_u.getEditUsers().equals("true")){
                            permissions.add(per_u.getCreateBillboards());
                            permissions.add(per_u.getEditAllBillboards());
                            permissions.add(per_u.getScheduleBillboards());
                            permissions.add(per_u.getEditUsers());
                        }else {
                            System.out.println("No return");
                        }
                    }
                    GetUserpemmReply getUserpemmReply = new GetUserpemmReply(permissions,per_u);
                    oos.writeObject(getUserpemmReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof SetUserPemmRequest){
                SetUserPemmRequest supr =(SetUserPemmRequest) o;
                String sessionToken = supr.getSessionToken();
                System.out.println("client requested setting permissions with token :"+ sessionToken);
                ArrayList<String> permissions = supr.getPermisssions();
                String results = null;
                if (isValidSessionToken(sessionToken)){
                    UserInfo spr_user = data.get(getSessionUsername(sessionToken));
                    if (spr_user.getEditUsers().equals("true")){
                        if(getSessionUsername(sessionToken).equals(supr.getUsername())){
                            data.edit(spr_user.getName(),spr_user.getUsername(),spr_user.getPasswords(), spr_user.getSalt(),spr_user.getEmail(),spr_user.getUsername(),
                                    permissions.get(0),permissions.get(1),permissions.get(2),spr_user.getEditUsers());
                            results = "Succeceed to change same user's permissions";
                        }
                        else{
                            UserInfo new_user = data.get(supr.getUsername());
                            data.edit(new_user.getName(),new_user.getUsername(),new_user.getPasswords(), new_user.getSalt(),new_user.getEmail(),new_user.getUsername(),
                                    permissions.get(0),permissions.get(1),permissions.get(2),permissions.get(3));
                            results = "Succeed to change other user's permsissions";
//                            System.out.println(permissions.get(0));
//                            System.out.println(permissions.get(1));
//                            System.out.println(permissions.get(2));
//                            System.out.println(permissions.get(3));
//                            UserInfo user = data.get(supr.getUsername());
//                            System.out.println(user.getCreateBillboards());
//                            System.out.println(user.getEditAllBillboards());
//                            System.out.println(user.getScheduleBillboards());
//                            System.out.println(user.getEditUsers());
                        }
                    }else{
                        data.edit(spr_user.getName(),spr_user.getUsername(),spr_user.getPasswords(), spr_user.getSalt(),spr_user.getEmail(),spr_user.getUsername(),
                            spr_user.getCreateBillboards(),spr_user.getEditAllBillboards(),spr_user.getScheduleBillboards(),spr_user.getEditUsers());
                        results = "Failed to change user's permsissions because there is no Edit Users permsissions";
                    }

                    AcknowledgeReply setUserPemmReply = new AcknowledgeReply(results);
                    oos.writeObject(setUserPemmReply);
                    oos.flush();
                }else{
                    Object reply = new Object();
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof SetPassRequest){
                SetPassRequest spr = (SetPassRequest) o;
                String hashedstar = getHashedPass("******");
                String results = null;
                UserInfo editU = data.get(spr.getUsername());
                UserInfo currentLoginU = data.get(getSessionUsername(spr.getSessionToken()));

                if (editU.getUsername().equals(getSessionUsername(spr.getSessionToken()))) {
                    if (spr.getHashedPassword().equals(hashedstar)) {
                        data.edit(spr.getName(), spr.getUsername(), editU.getPasswords(), editU.getSalt(), spr.getEmail(), spr.getPreviousUserName(), editU.getCreateBillboards(),
                                editU.getEditAllBillboards(), editU.getScheduleBillboards(), editU.getEditUsers());
                        results = "Succeed to change own name and/or email";
                    } else {
                        String salt = getSaltString();
                        String hashsaltPaass = getSaltHashedPass(spr.getHashedPassword(), salt);
                        data.edit(spr.getName(), spr.getUsername(), hashsaltPaass, salt, spr.getEmail(), spr.getPreviousUserName(), editU.getCreateBillboards(),
                                editU.getEditAllBillboards(), editU.getScheduleBillboards(), editU.getEditUsers());
                        results = "Succeed to change own name, password and/or email";
                    }
                }
                else{
                    if (currentLoginU.getEditUsers().equals("true")) {
                        if (spr.getHashedPassword().equals(hashedstar)) {
                            data.edit(spr.getName(), spr.getUsername(), editU.getPasswords(), editU.getSalt(), spr.getEmail(), spr.getPreviousUserName(), editU.getCreateBillboards(),
                                    editU.getEditAllBillboards(), editU.getScheduleBillboards(), editU.getEditUsers());
                            results = "Succeed to change own name and/or email";
                        } else {
                            String salt = getSaltString();
                            String hashsaltPaass = getSaltHashedPass(spr.getHashedPassword(), salt);
                            data.edit(spr.getName(), spr.getUsername(), hashsaltPaass, salt, spr.getEmail(), spr.getPreviousUserName(), editU.getCreateBillboards(),
                                    editU.getEditAllBillboards(), editU.getScheduleBillboards(), editU.getEditUsers());
                            results = "Succeed to change own name, password and/or email";
                        }
                    } else {
                        results = "failed to change  name, password and/or email";
                    }
                }
                AcknowledgeReply setPassReply = new AcknowledgeReply(results);
                oos.writeObject(setPassReply);
                oos.flush();
            } else if (o instanceof DeleteUserRequest) {
                DeleteUserRequest dur = (DeleteUserRequest) o;
                String sessiontoken = dur.getSessiontoken();
                System.out.println("client requested setting permissions with token :" + sessiontoken);
                String results = null;
                UserInfo del_u = data.get(getSessionUsername(dur.getSessiontoken()));
                if (isValidSessionToken(sessiontoken) & del_u.getEditUsers().equals("true")) {
                    String username = dur.getUsername();

                    if (username.equals(getSessionUsername(dur.getSessiontoken()))) {
                        results = "USER CANNOT CHANGE OWN PAASSWORD!";
                    } else {
                        data.remove(dur.getUsername());
                        results = "Succeed to delate user !";
                    }
                }else{
                    results = "Fail to delate user !";
                }
                AcknowledgeReply delateUserReply = new AcknowledgeReply(results);
                oos.writeObject(delateUserReply);
                oos.flush();
            }
            else if(o instanceof LogoutUsersRequest){
                LogoutUsersRequest lour =(LogoutUsersRequest) o;
                String sessiontoken = lour.getSessionToken();
                System.out.println("client requested expireds with token :"+ sessiontoken);
                validSessionTokens.remove(sessiontoken);
                AcknowledgeReply logoutUserReply = new AcknowledgeReply("Successfully logout user");
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
        return password.equals(dbpassword);
    }
    // get idea from week 9 assignment Q & A
    private static String getSaltString() {
        Random rng = new Random();
        byte[] salt_bytes = new byte[32];
        rng.nextBytes(salt_bytes);
        String salt_str = bytesToString(salt_bytes);
        return salt_str;
    }
    private static String getHashedPass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] temp_byte = md.digest(password.getBytes());
        String hashedPassword= bytesToString(temp_byte);

        return hashedPassword;
    }
    //
}
