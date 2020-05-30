package cab302.server.WillBeControlPanelAction;

import cab302.server.Billboardserver.*;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserClient {
    public static void run() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        Socket socket = new Socket("localhost",12345);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        ObjectInputStream ois = new ObjectInputStream(inputStream);

        String password ="daafda";
        String hashePass = getHashedPass(password);
        LocalDateTime localDateTime = LocalDateTime.now();
        Loginrequest loginrequest = new Loginrequest("suzan",hashePass,null);
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

        socket = new Socket("localhost",12345);
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
            System.out.println("Billboards List : "+ lbrlist.getListofBillboards());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",12345);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        String billboardname = "billboard1";
        BillboardRequest bbr = new BillboardRequest(billboardname,sessionToken);
        oos.writeObject(bbr);
        oos.flush();
        transoO = ois.readObject();
        String billboardContent = null;
        if (transoO instanceof BillboardReply){
            BillboardReply bbrcontent =  (BillboardReply) transoO;
            billboardContent = bbrcontent.getXmlcontent();
            System.out.println("billboard content : " + billboardContent);
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();
        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",12345);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        CreateBillboardRequest cbbr = new CreateBillboardRequest(billboardname,billboardContent,sessionToken);
        oos.writeObject(cbbr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply){
            AcknowledgeReply cbbrcontent =  (AcknowledgeReply) transoO;
            System.out.println(cbbrcontent.getAcknowledgement());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();
        socket.close();
//
//        if (sessionToken.isEmpty()) return;
//
//        socket = new Socket("localhost",12345);
//        outputStream = socket.getOutputStream();
//        inputStream = socket.getInputStream();
//
//        oos = new ObjectOutputStream(outputStream);
//        ois = new ObjectInputStream(inputStream);
//        DelateBillboardRequest dbbr = new DelateBillboardRequest(billboardname,sessionToken);
//        oos.writeObject(dbbr);
//        oos.flush();
//        transoO = ois.readObject();
//        if (transoO instanceof AcknowledgeReply){
//            AcknowledgeReply dbbrcontent =  (AcknowledgeReply) transoO;
//            System.out.println(dbbrcontent.getAcknowledgement());
//        }else{
//            System.out.println("error");
//        }
//        ois.close();
//        oos.close();
//        socket.close();

//        if (sessionToken.isEmpty()) return;
//
//        socket = new Socket("localhost",12345);
//        outputStream = socket.getOutputStream();
//        inputStream = socket.getInputStream();
//
//        oos = new ObjectOutputStream(outputStream);
//        ois = new ObjectInputStream(inputStream);
//        int time = 10;
//        int duration = 100;
//        ViewBillboardRequest vbbr = new ViewBillboardRequest(sessionToken);
//        oos.writeObject(vbbr);
//        oos.flush();
//        transoO = ois.readObject();
//        if (transoO instanceof ViewBillboardReply){
//            ViewBillboardReply vbbrcontent =  (ViewBillboardReply) transoO;
//            System.out.println("server replied with "+ vbbrcontent.getbillboard());
//        }else{
//            System.out.println("error");
//        }
//        ois.close();
//        oos.close();
//        socket.close();

//        if (sessionToken.isEmpty()) return;
//
//        socket = new Socket("localhost",12345);
//        outputStream = socket.getOutputStream();
//        inputStream = socket.getInputStream();
//
//        oos = new ObjectOutputStream(outputStream);
//        ois = new ObjectInputStream(inputStream);
//        String year1 = "2020";
//        String month1 = "5";
//        String date1 = "19";
//        String hour = "16";
//        String minitue = "10";
//        String duractionhr = "1";
//        String duractionmin = "16";
//        String ricur = "2";
//        ScheduleBillboardRequest sbbr = new ScheduleBillboardRequest(billboardname, year1, month1, date1, hour, minitue, sessionToken, duractionhr, duractionmin, ricur);
//        oos.writeObject(sbbr);
//        oos.flush();
//        transoO = ois.readObject();
//        if (transoO instanceof AcknowledgeReply){
//            AcknowledgeReply sbbrcontent =  (AcknowledgeReply) transoO;
//            System.out.println("Scheduled billboard list : "+ sbbrcontent.getAcknowledgement());
//        }else{
//            System.out.println("error");
//        }
//        ois.close();
//        oos.close();
//        socket.close();
//
//        if (sessionToken.isEmpty()) return;
//
//        socket = new Socket("localhost",12345);
//        outputStream = socket.getOutputStream();
//        inputStream = socket.getInputStream();
//
//        oos = new ObjectOutputStream(outputStream);
//        ois = new ObjectInputStream(inputStream);
//        int time2 = 10;
//        int month2 = 5;
//        int date2 = 19;
//        String hou2 = "16";
//        String minitu2 = "10";
//        RemoveBillboardRequest rbbr = new RemoveBillboardRequest(billboardname,sessionToken, month2, date2, hou2,minitu2);
//        oos.writeObject(rbbr);
//        oos.flush();
//        transoO = ois.readObject();
//        if (transoO instanceof AcknowledgeReply){
//            AcknowledgeReply rbbrcontent =  (AcknowledgeReply) transoO;
//            System.out.println(rbbrcontent.getAcknowledgement());
//        }else{
//            System.out.println("error");
//        }
//        ois.close();
//        oos.close();
//        socket.close();


        System.out.println("------------------------------------------------------");
        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",12345);
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
            System.out.println("User Lists : "+ lurlist.getListOfUsers());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",12345);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        String username = "suu";
        String password1 = "dfdfcc";
        String hashePass1 = getHashedPass(password1);
        ArrayList<String> lists_permission = new ArrayList<>();
        lists_permission.add("True");
        lists_permission.add("True");
        lists_permission.add("True");
        lists_permission.add("True");
        CreateUsersRequest cur = new CreateUsersRequest(sessionToken, hashePass1,username,"suu@gmail.com" , lists_permission);

        oos.writeObject(cur);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply){
            AcknowledgeReply lurlist =  (AcknowledgeReply) transoO;
            System.out.println(lurlist.getAcknowledgement());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();
        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",12345);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        GetUserPemmRequest gupr = new GetUserPemmRequest("suu", sessionToken);

        oos.writeObject(gupr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof GetUserpemmReply){
            GetUserpemmReply lurlist =  (GetUserpemmReply) transoO;
            System.out.println(lurlist.getListPermissions());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();

        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",12345);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        ArrayList<String> list_permission = new ArrayList<String>();
        list_permission.add("True");
        list_permission.add("True");
        list_permission.add("True");
        list_permission.add("True");

        String hashePass11 = getHashedPass("sfaad");
        SetUserPemmRequest supr = new SetUserPemmRequest("suu", sessionToken, hashePass11, list_permission);

        oos.writeObject(supr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply){
            AcknowledgeReply supr1 = (AcknowledgeReply)  transoO;
            System.out.println(supr1.getAcknowledgement());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();


        socket = new Socket("localhost",12345);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);
        String hashePassd = getHashedPass("sfaad");
        SetPassRequest spr = new SetPassRequest("suu", "suu", "email",hashePassd, sessionToken,"suu");

        oos.writeObject(spr);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply){
            AcknowledgeReply replys = (AcknowledgeReply) transoO;
            System.out.println(replys.getAcknowledgement());
        }else{
            System.out.println("error");
        }
        ois.close();
        oos.close();

        socket.close();
//        if (sessionToken.isEmpty()) return;
//
//        socket = new Socket("localhost",12345);
//        outputStream = socket.getOutputStream();
//        inputStream = socket.getInputStream();
//
//        oos = new ObjectOutputStream(outputStream);
//        ois = new ObjectInputStream(inputStream);
//
//        DelateUserRequest dur = new DelateUserRequest("suu", sessionToken);
//
//        oos.writeObject(dur);
//        oos.flush();
//        transoO = ois.readObject();
//        if (transoO instanceof AcknowledgeReply){
//            AcknowledgeReply deuser = (AcknowledgeReply) transoO;
//            System.out.println(deuser.getAcknowledgement());
//        }else{
//            System.out.println("error");
//        }
//        ois.close();
//        oos.close();
//
//        socket.close();
        if (sessionToken.isEmpty()) return;

        socket = new Socket("localhost",12345);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        oos = new ObjectOutputStream(outputStream);
        ois = new ObjectInputStream(inputStream);

        LogoutUsersRequest lour = new LogoutUsersRequest(sessionToken);

        oos.writeObject(lour);
        oos.flush();
        transoO = ois.readObject();
        if (transoO instanceof AcknowledgeReply){
            AcknowledgeReply loure = (AcknowledgeReply) transoO;
            System.out.println(loure.getAcknowledgement());
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
