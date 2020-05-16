package cab302.server.UserServer;

import cab302.server.Billboardserver.*;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UserClient {
    public static void main (String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        Socket socket = new Socket("localhost",3306);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        String password ="dfddsd";
        String hashePass = getHashedPass(password);
        Loginrequest loginrequest = new Loginrequest("souu",hashePass);
        oos.writeObject(loginrequest);
        oos.flush();
        Object transoO = ois.readObject();

        String sessionToken = null;
        if (transoO instanceof LoginReply){
            LoginReply reply = (LoginReply) transoO;
            if (reply.isLoginSucceed()){
                sessionToken = reply.getSessionToken();
                System.out.println("Success to log in, recieve the token "+ sessionToken);
            } else{
                System.out.println("fail to login");
            }
        }
        ois.close();
        oos.close();
        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);

        listBillboardRequest lbr = new listBillboardRequest(sessionToken);
        oos.writeObject(lbr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof listBillboardReply){
            listBillboardReply lbrlist =  (listBillboardReply) transoO;
            System.out.println("server replied with "+ lbrlist);
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        String billboardname = "dfdfd";
        BillboardRequest bbr = new BillboardRequest(billboardname,sessionToken);
        oos.writeObject(bbr);
        oos.flush();
        transoO = ois.readObject();
        String billboardContent = null;
        if (transoO instanceof BillboardReply){
            BillboardReply bbrcontent =  (BillboardReply) transoO;
            billboardContent = bbrcontent.getbillboard();
            System.out.println("server replied with "+ billboardContent);
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();
        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        CreateBillboardRequest cbbr = new CreateBillboardRequest(billboardname,billboardContent,sessionToken);
        oos.writeObject(cbbr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof CreateBillboardReply){
            CreateBillboardReply cbbrcontent =  (CreateBillboardReply) transoO;
            System.out.println("server replied with "+ cbbrcontent.getbillboard());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();
        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        DelateBillboardRequest dbbr = new DelateBillboardRequest(billboardname,sessionToken);
        oos.writeObject(dbbr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof DelateBillboardReply){
            DelateBillboardReply dbbrcontent =  (DelateBillboardReply) transoO;
            System.out.println("server replied with "+ dbbrcontent.getbillboard());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();
        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        int time = 10;
        int duration = 100;
        ViewBillboardRequest vbbr = new ViewBillboardRequest(sessionToken);
        oos.writeObject(vbbr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof ViewBillboardReply){
            ViewBillboardReply vbbrcontent =  (ViewBillboardReply) transoO;
            System.out.println("server replied with "+ vbbrcontent.getbillboard());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();
        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        int time1 = 10;
        int duration1 = 100;
        ScheduleBillboardRequest sbbr = new ScheduleBillboardRequest(billboardname,sessionToken, time1, duration1);
        oos.writeObject(sbbr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof ScheduleBillboardReply){
            ScheduleBillboardReply sbbrcontent =  (ScheduleBillboardReply) transoO;
            System.out.println("server replied with "+ sbbrcontent.getbillboard());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();
        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        int time2 = 10;
        RemoveBillboardRequest rbbr = new RemoveBillboardRequest(billboardname,sessionToken, time2);
        oos.writeObject(rbbr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof RemoveBillboardReply){
            RemoveBillboardReply rbbrcontent =  (RemoveBillboardReply) transoO;
            System.out.println("server replied with "+ rbbrcontent.getbillboard());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();
        socket.close();


        System.out.println("------------------------------------------------------");
        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);

        listUsersRequest lur = new listUsersRequest(sessionToken);
        oos.writeObject(lur);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof listUsersReply){
            listUsersReply lurlist =  (listUsersReply) transoO;
            System.out.println("server replied with "+ lurlist);
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        String username = "suu";
        String password1 = "dfdfcc";
        String hashePass1 = getHashedPass(password1);
        ArrayList<String> lists_permission = new ArrayList<String>();
        lists_permission.add("EditUsers");
        lists_permission.add("ScheduleBillboards");
        lists_permission.add("EditAllBillboards");
        lists_permission.add("CreateBillboards");
        CreateUsersRequest cur = new CreateUsersRequest(sessionToken, username, hashePass1,lists_permission);

        oos.writeObject(cur);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof CreateUsersReply){
            System.out.println("Server successfully made the user ! ");
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();
        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        GetUserPemmRequest gupr = new GetUserPemmRequest("souu", sessionToken);

        oos.writeObject(gupr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof GetUserpemmReply){
            System.out.println("Server successfully get the user permission ! ");
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        ArrayList<String> list_permission = new ArrayList<String>();
        list_permission.add("True");
        list_permission.add("False");
        SetUserPemmRequest supr = new SetUserPemmRequest("souu", sessionToken, list_permission);

        oos.writeObject(supr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof SetUserpemmReply){
            System.out.println("Server successfully set the user permission ! ");
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();


        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);

        SetPassRequest spr = new SetPassRequest("souu", hashePass);

        oos.writeObject(spr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof SetPassReply){
            System.out.println("Server successfully set the user password ! ");
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();
        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);

        DelateUserRequest dur = new DelateUserRequest("suu", sessionToken);

        oos.writeObject(dur);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof DelateUserReply){
            System.out.println("Server successfully delate the user! ");
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();
        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",3306);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);

        LogoutUsersRequest lour = new LogoutUsersRequest(sessionToken);

        oos.writeObject(lour);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof LogoutUserReply){
            System.out.println("Server successfully logout the user! ");
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();

    }
    public static String getHashedPass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] temp_byte = md.digest(password.getBytes());
        String hashedPassword= bytesToString(temp_byte);

        return hashedPassword;
    }
    public static String bytesToString(byte[] hash){
        StringBuffer str_buff = new StringBuffer();
        for (byte b : hash){
            str_buff.append(String.format("%02x", b & 0xFF));
        }
        return str_buff.toString();
    }
}
