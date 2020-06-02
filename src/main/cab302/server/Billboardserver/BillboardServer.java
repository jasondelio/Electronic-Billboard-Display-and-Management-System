package cab302.server.Billboardserver;

import cab302.database.RemovedSchedule.RemovedScheduleData;
import cab302.database.RemovedSchedule.RemovedScheduleInfo;
import cab302.database.billboard.BillboardData;
import cab302.database.billboard.BillboardInfo;
import cab302.database.schedule.ScheduleData;
import cab302.database.schedule.ScheduleInfo;
import cab302.database.user.UserData;
import cab302.database.user.UserInfo;
import cab302.server.WillBeControlPanelAction.*;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class BillboardServer {
    // from week 10 Q and A session
    public static HashMap<String, String> validSessionTokens = new HashMap<>();
    public static HashMap<String, Date> TimeSessionTokensmade = new HashMap<>();
    private static String port;
    /**
     * Server connecting database and two applications
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        // waiting connection from GUIs with listening port
        getPropValues();
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port));
        // initialise the userdata
        UserData data = new UserData();

        // make the administrator with all permissions
        String root_password = "root";
        String hashedrootpassword = getHashedPass(root_password);
        String firstsalt = getSaltString();
        String hasedsaltpassroot = getSaltHashedPass(hashedrootpassword, firstsalt);
        UserInfo rootUser = new UserInfo("admin", "root", hasedsaltpassroot, firstsalt, "root@gmail.com", "true",
                "true", "true", "true");
        data.add(rootUser);

        // initialise the billboardData
        BillboardData billboardData = new BillboardData();
        // initialise the scheduleData
        ScheduleData scheduleData = new ScheduleData();
        // initialise the removedScheduleData
        RemovedScheduleData removedScheduleData = new RemovedScheduleData();

        // listen the port until socket is closed
        for (; ; ) {
            // start socket and setting the output and input stream
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object o = ois.readObject();
            if (o instanceof Loginrequest){
                // get login request from Control Panel
                Loginrequest loginrequest = (Loginrequest) o;
                System.out.printf("User try to login with username %s and hasedpassword %s\n",
                        loginrequest.getUsername(), loginrequest.getPassword());

                //get user information from database
                UserInfo u = data.get(loginrequest.getUsername());
                String loggedinUsername = loginrequest.getUsername();

                //get salt and stored password from database
                String salt = u.getSalt();
                String saltHashedPass = u.getPasswords();

                LoginReply reply;

                ArrayList<String> permissions = new ArrayList<>();
                if (loginSuccess(saltHashedPass,getSaltHashedPass(loginrequest.getPassword(),salt))){
                    // if login is success get new sessiontoken and store the permissions to sent those to client
                    String token = getSessionTokenString();
                    permissions.add(u.getCreateBillboards());
                    permissions.add(u.getEditAllBillboards());
                    permissions.add(u.getScheduleBillboards());
                    permissions.add(u.getEditUsers());

                    // send above information to Control Panel through this reply
                    reply = new LoginReply(true,token, permissions, loggedinUsername);
                    //enter the set of session token and username in hashmap
                    validSessionTokens.put(token, loginrequest.getUsername());
                    //enter session token and logged in time in hashmap
                    TimeSessionTokensmade.put(token, loginrequest.getLoginTime());
                    isSessionTokenExpired(token);
                } else {
                    reply = new LoginReply(false,null, null,null);
                }
                oos.writeObject(reply);
                oos.flush();
            } else if (o instanceof UserLoggedInrequest){
                // if the sessiontoken is not expired yet, send the user with the sessinotoken info will be sent to
                // control panel
                UserLoggedInrequest userLoggedInrequest = (UserLoggedInrequest) o;
                System.out.printf("User try to get already logged in user information  ");
                String sessionToken = userLoggedInrequest.getSessionToken();
                //check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    AlreadyLoginReply reply;
                    ArrayList<String> permissions = new ArrayList<>();
                    if (isValidSessionToken(sessionToken)) {
                        // if login is success get the sessiontoken and store the permissions to sent those to client
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
                } else{
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    TimeSessionTokensmade.remove(sessionToken);
                    oos.writeObject(reply);
                    oos.flush();
                }
            }else if (o instanceof sessionExistRequest){
                // check if a sessiontoken is exist in the sessiontoken Hashmap.
                sessionExistRequest ser = (sessionExistRequest) o;
                System.out.printf("User try to get session token");
                SessionExistReply reply;
                if (validSessionTokens.keySet().isEmpty()){
                    // if the Hashmap is empty return null
                    reply = new SessionExistReply(false,null);
                } else {
                    // otherwise send back the sessiontoken list
                    ArrayList<String> stringList = new ArrayList<String>(validSessionTokens.keySet());
                    reply = new SessionExistReply(true,stringList);
                }
                oos.writeObject(reply);
                oos.flush();
            }
            else if(o instanceof BillboardRequest){
                // request getting the billboard info with sessiontoken and the billboard title
                BillboardRequest br = (BillboardRequest) o;
                String sessionToken =  br.getSessionToken();
                System.out.println("client requested billboard content with token :"+ sessionToken);
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String billname = br.getBillboardname();
                        // get the billboard info from database
                        BillboardInfo new_b = billboardData.get(billname);
                        BillboardReply billboardReply = new BillboardReply(new_b.getXMLContent(), new_b.getCreator());
                        oos.writeObject(billboardReply);
                        oos.flush();
                    } else {
                        Object reply = new Object();
                        oos.writeObject(reply);
                        oos.flush();
                    }
                } else {
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof ListBillboardRequest){
                // requesting list billboard
                ListBillboardRequest lbr =(ListBillboardRequest) o;
                String sessionToken = lbr.getSessionToken();
                System.out.println("client requested billboard lists with token :" + sessionToken);
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        // send listmodel to control panel
                        ListBillboardReply lbbr = new ListBillboardReply(billboardData.getModel());
                        oos.writeObject(lbbr);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("error");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                } else {
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof CreateBillboardRequest){
                // request creating billboard
                CreateBillboardRequest cbbr =(CreateBillboardRequest) o;
                String sessionToken = cbbr.getSessionToken();
                System.out.println("client requested creating lists with token :"+ sessionToken);
                String results = null;
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo nu = data.get(currentUser);
                        // check if the user has the permission 'crete billboard' or not
                        if (nu.getCreateBillboards().equals("true")) {
                            BillboardInfo new_b = new BillboardInfo(cbbr.getBillboardname(), cbbr.getBillboardContent(), currentUser);
                            billboardData.add(new_b);
                            results = "Success to edit billboard";
                        } else {
                            results = "falied to edit";
                        }
                        // send the result of action
                        AcknowledgeReply createBillboardReply = new AcknowledgeReply(results);
                        oos.writeObject(createBillboardReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                } else {
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof EditBillboardRequest){
                // request editing the billboard
                EditBillboardRequest ebbr =(EditBillboardRequest) o;
                String sessionToken = ebbr.getSessionToken();
                System.out.println("client requested creating lists with token :"+ sessionToken);
                String results = null;
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo nu = data.get(currentUser);
                        // check if the billboard is exist or not
                        if (billboardData.get(ebbr.getBillboardname()) != null) {
                            // get the billboard info and schedule of the billboard
                            BillboardInfo edit_b = billboardData.get(ebbr.getBillboardname());
                            ScheduleInfo edit_si = scheduleData.get(ebbr.getBillboardname());
                            // check if the current user is editing own bllboard or not and check if the billboard is scheduled or not
                            if (edit_b.getCreator().equals(currentUser) && edit_si.getBoardTitle() == null) {
                                if (nu.getCreateBillboards().equals("true")) {
                                    // if the user editing own one and it is not scheduled, current user can
                                    // edit this billboard with "crete billboard" permission;
                                    billboardData.edit(edit_b.getName(), ebbr.getBillboardContent(), edit_b.getName());
                                    results = "Success to edit billboard";
                                } else {
                                    results = "failed to edit billboard";
                                }
                            } else {
                                if (nu.getEditAllBillboards().equals("true")) {
                                    // if the user editing other one or it is scheduled, current user can
                                    // edit this billboard with "Edit all billboard" permission;
                                    billboardData.edit(edit_b.getName(), ebbr.getBillboardContent(), edit_b.getName());
                                    results = "Success to edit billboard";
                                } else {
                                    results = "falied to edit billboard";
                                }
                            }
                        }
                        AcknowledgeReply editBillboardReply = new AcknowledgeReply(results);
                        oos.writeObject(editBillboardReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof DeleteBillboardRequest){
                DeleteBillboardRequest dbbr =(DeleteBillboardRequest) o;
                String sessionToken = dbbr.getSessionToken();
                System.out.println("client requested delating billboard with token :"+ sessionToken);
                String results = null;
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo nu = data.get(currentUser);
                        // get the billboard info and schedule of the billboard
                        BillboardInfo edit_b = billboardData.get(dbbr.getBillboardname());
                        ScheduleInfo edit_si = scheduleData.get(dbbr.getBillboardname());
                        // check if the current user is editing own bllboard or not and check if the billboard is scheduled or not
                        if (edit_b.getCreator().equals(currentUser) && edit_si.getBoardTitle() == null) {
                            if (nu.getCreateBillboards().equals("true")) {
                                // if the user deleting own one and it is not scheduled, current user can
                                // delete this billboard with "crete billboard" permission;
                                billboardData.remove(edit_b.getName());
                                // when the billboard is delated, current schedule with the billboard title will be delated also.
                                scheduleData.removeAll(edit_b.getName());
                                // delate the billboards from the delate history database
                                removedScheduleData.remove(edit_b.getName());
                                results = "Success to delate";
                            } else {
                                results = "You do not have CreateBillboard permission";
                            }
                        } else {
                            if (nu.getEditAllBillboards().equals("true")) {
                                // if the user deleting other one or it is scheduled, current user can
                                // delete this billboard with "Edit all billboard" permission;
                                billboardData.remove(edit_b.getName());
                                // when the billboard is delated, current schedule with the billboard title will be delated also.
                                scheduleData.removeAll(edit_b.getName());
                                // delate the billboards from the delate history database
                                removedScheduleData.remove(edit_b.getName());
                                results = "Success to delate";
                            } else {
                                results = "You do not have EditAllBillboard permissions";
                            }
                        }
                        AcknowledgeReply delateBillboardReply = new AcknowledgeReply(results);
                        oos.writeObject(delateBillboardReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof ViewBillboardRequest){
                ViewBillboardRequest vbbr =(ViewBillboardRequest) o;
                String sessionToken = vbbr.getSessionToken();
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo nu = data.get(currentUser);
                        ScheduleData scheduleData1 = new ScheduleData();
                        // initialise the lists getting from database later
                        ListModel sche_lists = null;
                        ListModel dupleSchedules = null;
                        // check if the user has "schedule billboard" permission or not
                        if (nu.getScheduleBillboards().equals("true")) {
                            // enter the unique schedule list
                            sche_lists = scheduleData1.getModel();
                            // enter the schedule list which might have same bllboard title
                            dupleSchedules = scheduleData1.take();
                        } else {
                            sche_lists = null;
                        }
                        ViewBillboardReply viewbillbord = new ViewBillboardReply(sche_lists,dupleSchedules);
                        oos.writeObject(viewbillbord);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof FindScheduleRequest){
                FindScheduleRequest sBbr =(FindScheduleRequest) o;
                String sessionToken = sBbr.getSessiontoken();
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo nu = data.get(currentUser);
                        ScheduleInfo sch = new ScheduleInfo();
                        if (nu.getScheduleBillboards().equals("true")) {
                            // if current user has "schedule billboard" permission
                            // get the schedule information from database with title, month, data and hour.
                            sch = scheduleData.findSchedule(sBbr.getTitle(), sBbr.getMonth(),sBbr.getDate(),sBbr.getHour());
                        } else {
                            sch = null;
                        }
                        FindScheduleReply findScheduleReply = new FindScheduleReply(sch);
                        oos.writeObject(findScheduleReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof GetIndexSchedule){
                GetIndexSchedule gis =(GetIndexSchedule) o;
                String sessionToken = gis.getSessiontoken();
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo nu = data.get(currentUser);
                        ScheduleInfo sch = new ScheduleInfo();
                        if (nu.getScheduleBillboards().equals("true")) {
                            // if current user has "schedule permission", the user find the schedule from
                            // database with index of database.
                            sch = scheduleData.findRow(gis.getIndex());
                        } else {
                            sch = null;
                        }
                        FindScheduleReply findScheduleReply = new FindScheduleReply(sch);
                        oos.writeObject(findScheduleReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof ScheduleBillboardRequest){
                ScheduleBillboardRequest sbbr =(ScheduleBillboardRequest) o;
                String sessionToken = sbbr.getSessionToken();
                System.out.println("client requested scheduling the billboard with token :"+ sessionToken);
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo nu = data.get(currentUser);
                        String results = null;
                        if (nu.getScheduleBillboards().equals("true")) {
                            // if current user has "schedule permission", the user will try to schedule the billboard with below information.

                            // get the schedule information from schdule database with bellow all information of schedule
                            ScheduleInfo new_schedule = new ScheduleInfo(sbbr.getBillboardname(), sbbr.getCreator(), sbbr.getYear(), sbbr.getMonth(), sbbr.getDate(),
                                    sbbr.getHour(), sbbr.getMinitue(), sbbr.getDurationHr(), sbbr.getDurationMin(), sbbr.getRecur());
//                            System.out.println(isAlreadyRemodeOnce(new_schedule,removedScheduleData));
//                            System.out.println(scheduleData.findSameSchedule(sbbr.getBillboardname(), sbbr.getMonth(), sbbr.getDate(),
//                                    sbbr.getHour(), sbbr.getMinitue(), sbbr.getDurationHr(), sbbr.getDurationMin(), sbbr.getRecur()).getDuMin());

                            // check if the schedule is already removed or not by checking the history of removed once
                            if (isAlreadyRemodeOnce(new_schedule,removedScheduleData)){
                                // if the schedule is already removed once, the schedule cannot be scheduled.
                                // this is for avoiding the writing again by setting recur in gui.
                                results = "Failed becuase already removed.";
                            } else if (scheduleData.findSameSchedule(sbbr.getBillboardname(), sbbr.getMonth(), sbbr.getDate(),
                                    sbbr.getHour(), sbbr.getMinitue(), sbbr.getDurationHr(), sbbr.getDurationMin(), sbbr.getRecur()).getDuMin() != null){
                                // check if the schedule is already in the schedule database or not
                                results = "Failed becuase there is already";
                            } else {
                                // if it is not scheduled and not removed yet, make new schedule.
                                scheduleData.add(new_schedule);
                                results = "Success to schedule the billboard";
                            }
                        } else {
                            results = "No permission";
                        }
                        AcknowledgeReply scheduleBillboardReply = new AcknowledgeReply(results);
                        oos.writeObject(scheduleBillboardReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }
                else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof EditScheduleBillboard){
                EditScheduleBillboard esbb =(EditScheduleBillboard) o;
                String sessionToken = esbb.getSessionToken();
                System.out.println("client requested editing the billboard with token :"+ sessionToken);
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo nu = data.get(currentUser);
                        String results = null;
                        if (nu.getScheduleBillboards().equals("true")) {
                            // if current user has "schedule permission", the user will try to edit the schedule of the billboard with below information.
                            ScheduleInfo previousSche = scheduleData.findSchedule(esbb.getBillboardname(), esbb.getMonth(),esbb.getDate(), esbb.getHour());
//                            System.out.println(previousSche);

                            if(previousSche.getDuHr().equals(esbb.getDurationHr()) && previousSche.getMinute().equals(esbb.getMinute())
                                    && previousSche.getDuMin().equals(esbb.getDurationMin())){
                                // if user try to edit the schedule with same information, the schdule will be scheduled.
                                // the same value edit shoud not in removed history database to avoid not entering the schedule in delete history database
                                scheduleData.edit(esbb.getBillboardname(), esbb.getCreator(), esbb.getYear(), esbb.getMonth(),esbb.getDate(), esbb.getHour(),
                                        esbb.getMinute(), esbb.getDurationHr(), esbb.getDurationMin(), esbb.getRecur());
                                results = "Success to edit the scheduled billboard";
                            }
                            else{
                                // if user try to edit the schedule, the previous schedule time should be removed to avoid showing the schedule again with
                                // recurring.
                                RemovedScheduleInfo delatedSched = new RemovedScheduleInfo(previousSche.getBoardTitle(),previousSche.getCreator(),previousSche.getYear(),
                                        previousSche.getMonth(),previousSche.getDate(),previousSche.getHour(),previousSche.getMinute(),previousSche.getDuHr(),previousSche.getDuMin(),
                                        previousSche.getRecur());
                                removedScheduleData.add(delatedSched);

                                // change the schedule
                                scheduleData.edit(esbb.getBillboardname(), esbb.getCreator(), esbb.getYear(), esbb.getMonth(),esbb.getDate(), esbb.getHour(),
                                        esbb.getMinute(), esbb.getDurationHr(), esbb.getDurationMin(), esbb.getRecur());
                                results = "Success to edit the scheduled billboard";
                            }
                        } else {
                            results = "No permission";
                        }
//                        System.out.println(esbb.getBillboardname());
//                        System.out.println(esbb.getCreator());
//                        System.out.println(esbb.getYear());
//                        System.out.println(esbb.getMonth());
//                        System.out.println(esbb.getDate());
//                        System.out.println(esbb.getHour());
//                        System.out.println(esbb.getMinitue());
//                        System.out.println(esbb.getDurationHr());
//                        System.out.println(esbb.getDurationMin());
//                        System.out.println(esbb.getRecur());
                        AcknowledgeReply scheduleBillboardReply = new AcknowledgeReply(results);
                        oos.writeObject(scheduleBillboardReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }
                else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if(o instanceof RemoveBillboardRequest){
                RemoveBillboardRequest rbbr =(RemoveBillboardRequest) o;
                String sessionToken = rbbr.getSessionToken();
                System.out.println("client requested removing billboard from schedule with token :"+ sessionToken);
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        String currentUser = getSessionUsername(sessionToken);
                        UserInfo un = data.get(currentUser);
                        String result = null;
                        if (un.getScheduleBillboards().equals("true")) {
                            // if current user has "schedule permission", the user will try to delete the schedule
                            // of the billboard with below information.
                            ScheduleInfo new_sche = scheduleData.findSchedule(rbbr.getBillboardname(),rbbr.getMonth(), rbbr.getDate(),rbbr.getHour());
//                            System.out.println(new_sche.getBoardTitle());
//                            System.out.println(rbbr.getBillboardname());
//                            System.out.println(new_sche.getMonth());
//                            System.out.println(rbbr.getMonth());
//                            System.out.println(new_sche.getDate());
//                            System.out.println(rbbr.getDate());
//                            System.out.println(new_sche.getHour());
//                            System.out.println(rbbr.getHour());

                            //enter the deleted schedule into the remove history database to aboid making the schedule in database
                            // when  the calender gui try to make the table with recurring.
                            RemovedScheduleInfo delatedSched = new RemovedScheduleInfo(new_sche.getBoardTitle(),new_sche.getCreator(),new_sche.getYear(),
                                    new_sche.getMonth(),new_sche.getDate(),new_sche.getHour(),new_sche.getMinute(),new_sche.getDuHr(),new_sche.getDuMin(),
                                    new_sche.getRecur());
                            removedScheduleData.add(delatedSched);
                            if (new_sche.getBoardTitle().equals(rbbr.getBillboardname()) && new_sche.getMonth().equals(rbbr.getMonth())
                                    && new_sche.getDate().equals(rbbr.getDate()) && new_sche.getHour().equals(rbbr.getHour())) {
                                // if the billboard info is in the schdule.
                                scheduleData.remove(rbbr.getBillboardname(), rbbr.getMonth(), rbbr.getDate(), rbbr.getHour());
                                result = "Success to remove bb from schedule !";
                            } else {
                                result = "failed to remove cuz no billboard schedule on parameter time!";
                            }
                        } else {
                            result = "failed to remove!";
                        }
                        AcknowledgeReply remove_the_billbord = new AcknowledgeReply(result);
                        oos.writeObject(remove_the_billbord);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }
                else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            }
            else if (o instanceof listUsersRequest) {
                listUsersRequest lur =(listUsersRequest) o;
                String sessionToken = lur.getSessionToken();
                System.out.println("client requested user lists with token :"+ sessionToken);
                UserInfo newu = data.get(getSessionUsername(sessionToken));
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken) & newu.getEditUsers().equals("true")) {
                        // if the user has "Edit users" permission, send the listmodel to control panel
                        ListModel Userlist = data.getModel();
                        ListUsersReply listUsersReply = new ListUsersReply(Userlist);
                        oos.writeObject(listUsersReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                } else {
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
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
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false){
                    // check if the session token is valed or not and the user has "Edit users" permission or not.
                    if (isValidSessionToken(sessionToken) & newu.getEditUsers().equals("true")){
                        // get new salt and hash password with the salt.
                        String salt = getSaltString();
                        String hashedsaltPass = getSaltHashedPass(cur.getPassword(),salt);
                        // make new user with the sent information from control panel
                        UserInfo user = new UserInfo(cur.getUsername(),cur.getUsername(),hashedsaltPass,salt, cur.getEmail(), cur.getLists_Permissions().get(0),
                                cur.getLists_Permissions().get(1),cur.getLists_Permissions().get(2),cur.getLists_Permissions().get(3));
                        data.add(user);
                        AcknowledgeReply createUsersReply = new AcknowledgeReply("Succeed to create user !");
                        oos.writeObject(createUsersReply);
                        oos.flush();
                    }else{
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
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
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        // check if the user try to get own permission or not
                        if (getSessionUsername(sessionToken).equals(gupr.getUsername())) {
                            // if own permissions, return the list of permissions without any permissions
                            per_u = data.get(gupr.getUsername());
                            permissions.add(per_u.getCreateBillboards());
                            permissions.add(per_u.getEditAllBillboards());
                            permissions.add(per_u.getScheduleBillboards());
                            permissions.add(per_u.getEditUsers());

                        } else {
                            per_u = data.get(gupr.getUsername());
                            // if getting other user's permissions, check if the user has "Edit users" permission or not
                            if (per_u.getEditUsers().equals("true")) {
                                permissions.add(per_u.getCreateBillboards());
                                permissions.add(per_u.getEditAllBillboards());
                                permissions.add(per_u.getScheduleBillboards());
                                permissions.add(per_u.getEditUsers());
                            } else {
                                System.out.println("No return");
                            }
                        }
                        GetUserpemmReply getUserpemmReply = new GetUserpemmReply(permissions, per_u);
                        oos.writeObject(getUserpemmReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
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
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessionToken) == false) {
                    // check if the session token is valid or not
                    if (isValidSessionToken(sessionToken)) {
                        // get the user information of current user
                        UserInfo spr_user = data.get(getSessionUsername(sessionToken));
                        //check if the user has permission "Edit users"
                        if (spr_user.getEditUsers().equals("true")) {
                            // check if the user try to edit own or not.
                            if (getSessionUsername(sessionToken).equals(supr.getUsername())) {
                                // if editing own, the edit permission cannot be edited
                                data.edit(spr_user.getName(), spr_user.getUsername(), spr_user.getPasswords(), spr_user.getSalt(), spr_user.getEmail(), spr_user.getUsername(),
                                        permissions.get(0), permissions.get(1), permissions.get(2), spr_user.getEditUsers());
                                results = "Succeceed to change same user's permissions";
                            } else {
                                // if editing other user's permissions, the user can change all of them.
                                UserInfo new_user = data.get(supr.getUsername());
                                data.edit(new_user.getName(), new_user.getUsername(), new_user.getPasswords(), new_user.getSalt(), new_user.getEmail(), new_user.getUsername(),
                                        permissions.get(0), permissions.get(1), permissions.get(2), permissions.get(3));
                                results = "Succeed to change other user's permsissions";
                            }
                        } else {
                            // if the user do not have "Edit users" permission, do not change any perimssion.
                            // we need to edit this to update the userinfo ???????
                            data.edit(spr_user.getName(), spr_user.getUsername(), spr_user.getPasswords(), spr_user.getSalt(), spr_user.getEmail(), spr_user.getUsername(),
                                    spr_user.getCreateBillboards(), spr_user.getEditAllBillboards(), spr_user.getScheduleBillboards(), spr_user.getEditUsers());
                            results = "Failed to change user's permsissions because there is no Edit Users permsissions";
                        }

                        AcknowledgeReply setUserPemmReply = new AcknowledgeReply(results);
                        oos.writeObject(setUserPemmReply);
                        oos.flush();
                    } else {
                        AcknowledgeReply reply = new AcknowledgeReply("Invalid Session");
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }else{
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
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
                // check if the session token is exired or not
                if (isSessionTokenExpired(spr.getSessionToken()) == false) {
                    // check if the user try to edit own password or not.
                    if (editU.getUsername().equals(getSessionUsername(spr.getSessionToken()))) {
                        // check if the user do not change password or not,
                        if (spr.getHashedPassword().equals(hashedstar)) {
                            // if the user do not change password, change the email, name or username.
                            data.edit(spr.getName(), spr.getUsername(), editU.getPasswords(), editU.getSalt(), spr.getEmail(), spr.getPreviousUserName(), editU.getCreateBillboards(),
                                    editU.getEditAllBillboards(), editU.getScheduleBillboards(), editU.getEditUsers());
                            results = "Succeed to change own name and/or email";
                        } else {
                            // if the user change password, make new salt and hash it again
                            String salt = getSaltString();
                            String hashsaltPaass = getSaltHashedPass(spr.getHashedPassword(), salt);
                            data.edit(spr.getName(), spr.getUsername(), hashsaltPaass, salt, spr.getEmail(), spr.getPreviousUserName(), editU.getCreateBillboards(),
                                    editU.getEditAllBillboards(), editU.getScheduleBillboards(), editU.getEditUsers());
                            results = "Succeed to change own name, password and/or email";
                        }
                    } else {
                        // if changing other user's password, permission "Edit users" is needed to change.
                        if (currentLoginU.getEditUsers().equals("true")) {
                            // check if not changing password,
                            if (spr.getHashedPassword().equals(hashedstar)) {
                                // if password is not changed, change others
                                data.edit(spr.getName(), spr.getUsername(), editU.getPasswords(), editU.getSalt(), spr.getEmail(), spr.getPreviousUserName(), editU.getCreateBillboards(),
                                        editU.getEditAllBillboards(), editU.getScheduleBillboards(), editU.getEditUsers());
                                results = "Succeed to change own name and/or email";
                            } else {
                                // if password also changed, create new salt and hash again with it
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
                } else {
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            } else if(o instanceof DelateUserRequest){
                DelateUserRequest dur =(DelateUserRequest) o;
                String sessiontoken = dur.getSessiontoken();
                System.out.println("client requested setting permissions with token :"+ sessiontoken);
                String results = null;
                UserInfo del_u = data.get(getSessionUsername(dur.getSessiontoken()));
                // check if the session token is exired or not
                if (isSessionTokenExpired(sessiontoken) == false) {
                    // check if the session token is valid or not and check current use has "Edit users" permission or not
                    if (isValidSessionToken(sessiontoken) & del_u.getEditUsers().equals("true")) {
                        String username = dur.getUsername();
                        // check if current user try to delete himself/herself or not.
                        if (username.equals(getSessionUsername(dur.getSessiontoken()))) {
                            results = "USER CANNOT DELETE OWN PAASSWORD!";
                        } else {
                            data.remove(dur.getUsername());
                            results = "Succeed to delate user !";
                        }
                    } else {
                        results = "Fail to delate user !";
                    }
                    AcknowledgeReply delateUserReply = new AcknowledgeReply(results);
                    oos.writeObject(delateUserReply);
                    oos.flush();
                } else {
                    // if session is expired, send the expired to control panel
                    AcknowledgeReply reply = new AcknowledgeReply("Expired");
                    oos.writeObject(reply);
                    oos.flush();
                }
            } else if(o instanceof LogoutUsersRequest){
                LogoutUsersRequest lour =(LogoutUsersRequest) o;
                String sessiontoken = lour.getSessionToken();
                System.out.println("client requested expired with token :"+ sessiontoken);
                // remove from hashmap
                validSessionTokens.remove(sessiontoken);
                // remove from time hashmap
                TimeSessionTokensmade.remove(sessiontoken);
                AcknowledgeReply logoutUserReply = new AcknowledgeReply("Successfully logout user");
                oos.writeObject(logoutUserReply);
                oos.flush();
            } else if(o instanceof ViewerRequest){
                ViewerRequest lour =(ViewerRequest) o;
                // get current local time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                // split above time to each year, month, and others
                String year = dtf.format(now).split(":")[0];

                // for month, date, hour, minute, if each is less than 10, removed 0 from second digits to
                // adopt finging in database
                String month = dtf.format(now).split(":")[1];
                if (Character.toString(month.charAt(0)).equals("0")){
                    month = Character.toString(month.charAt(1));
                }
                String date = dtf.format(now).split(":")[2];
                if (Character.toString(date.charAt(0)).equals("0")){
                    date = Character.toString(date.charAt(1));
                }
                String hour = dtf.format(now).split(":")[3];
                if (Character.toString(hour.charAt(0)).equals("0")){
                    hour = Character.toString(hour.charAt(1));
                }
                String minute = dtf.format(now).split(":")[4];
                if (Character.toString(minute.charAt(0)).equals("0")){
                    minute = Character.toString(minute.charAt(1));
                }

                // get all schedule on today
                ArrayList<ScheduleInfo> currentSchedules = scheduleData.findCurrenttime(year,month,date);
                ScheduleInfo currentSche = new ScheduleInfo();
                BillboardInfo billboardInfo = new BillboardInfo();

                System.out.println(currentSchedules);
                // find the current schedule in current hour and minute
                for (int i = 0; i < currentSchedules.size();i++){
                    // if the second digits is 0, just use first digit to adopt computing below
                    String scheduledDuHr = currentSchedules.get(i).getDuHr();
                    if (Character.toString(scheduledDuHr.charAt(0)).equals("0")){
                        scheduledDuHr = Character.toString(scheduledDuHr.charAt(1));
                    }
                    String scheduledDuMin = currentSchedules.get(i).getDuMin();
                    if (Character.toString(scheduledDuMin.charAt(0)).equals("0")){
                        scheduledDuMin = Character.toString(scheduledDuMin.charAt(1));
                    }
                    String scheduledHr = currentSchedules.get(i).getHour();

                    String scheduledMin = currentSchedules.get(i).getMinute();
                    if (Character.toString(scheduledMin.charAt(0)).equals("0")){
                        scheduledMin = Character.toString(scheduledMin.charAt(1));
                    }

                    // get the whole duration as a minute, by following calculation.
                    int durtionWholeMinute = 60 * Integer.parseInt(scheduledDuHr) + Integer.parseInt(scheduledDuMin);
                    // get the gap between current time and each schedule time, by following calculation.
                    int durtionCurrentWholeMinute = 60 * ((Integer.parseInt(hour) - Integer.parseInt(scheduledHr))) + (Integer.parseInt(minute) - Integer.parseInt(scheduledMin));

                    // check if each schedule is in between schedule time and duration time.
                    if (0 <= durtionCurrentWholeMinute &&  durtionCurrentWholeMinute < durtionWholeMinute){
                        // if the schedule is in between schedule time, enter the schedule
//                        System.out.println("there is schedule");
                        currentSche = currentSchedules.get(i);

                    }
                }

                // if there is no schedule at the time, return this billboard
                if (currentSche.getBoardTitle() == null){
                    billboardInfo = billboardData.get("NotScheduled");
                }else{
                    billboardInfo = billboardData.get(currentSche.getBoardTitle());
                }

                ViewerReply viewerReply = new ViewerReply(billboardInfo);
                oos.writeObject(viewerReply);
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

    /**
     * check if the sessionToken is valid or not
     */
    private static boolean isValidSessionToken(String sessionToken) {
        return validSessionTokens.containsKey(sessionToken);
    }
    /**
     * check if the schedule is already removed once or not. In our calendar gui, we always make schedule with following the recurring time.
     * To avoid writing already removed one, checking the schedule is already removed or not is necessary.
     */
    private static boolean isAlreadyRemodeOnce(ScheduleInfo shce, RemovedScheduleData data) {
        // get already removed schedule from remove histroy database
        RemovedScheduleInfo removeOne = data.get(shce.getBoardTitle(),shce.getYear(),shce.getMonth(),shce.getDate(),shce.getHour(),shce.getMinute(),
                shce.getDuHr(),shce.getDuMin());
//        System.out.println(removeOne);
        // check if the schedule is exist or not
        if (removeOne.getBoardTitle() != null){
            if(removeOne.getBoardTitle().equals(shce.getBoardTitle()) && removeOne.getYear().equals(shce.getYear()) && removeOne.getMonth().equals(shce.getMonth())
                    && removeOne.getDate().equals(shce.getDate()) && removeOne.getHour().equals(shce.getHour())
                    && removeOne.getMinute().equals(shce.getMinute())  && removeOne.getDuHr().equals(shce.getDuHr())
                    && removeOne.getDuMin().equals(shce.getDuMin())){
                // if the schedule which user try to make with recurring already in removed history, true
                return  true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**
     * check if the session Token is expired or not with checking the current time and logged in time.
     */
    private static boolean isSessionTokenExpired(String sessionToken) {
        Date newdate = new Date();
        // get the gap between current time and logged in time.
        long diff = newdate.getTime() - TimeSessionTokensmade.get(sessionToken).getTime();
        int diffday = (int) (diff / (24 * 60 * 60 * 1000));
        if (diffday >= 1){
            return true;
        }else{
            TimeSessionTokensmade.put(sessionToken,newdate);
            return false;
        }
    }
    /**
     * get username with the sessinotoken
     */
    private static String getSessionUsername(String sessionToken) {
        return validSessionTokens.get(sessionToken);
    }

    /**
     * get salt hashed pass from week 9 Q and A
     */
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
    /**
     * check if login success or not
     */
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

    // read from network prop file
    private static void getPropValues() throws IOException {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("src/main/cab302/network.props");
            props.load(in);
            in.close();
            // get the property value and print it out
            port = props.getProperty("port");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
    //
}
