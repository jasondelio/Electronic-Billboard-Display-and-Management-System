package cab302.database.schedule;

import javax.swing.*;
import java.util.ArrayList;

public class ScheduleData {


    DefaultListModel listModel;

    SchedulesDatasource data;

    DefaultListModel sModel;


    public ScheduleData() {
        listModel = new DefaultListModel();
        data = new SchedulesDatasource();
        sModel = new DefaultListModel();
        for (String name : data.titleSet()) {
            listModel.addElement(name);
        }
        sModel = data.takeSchedule();
    }

    /**
     */
    public void add(ScheduleInfo s) {

//        if (!listModel.contains(s.getBoardTitle())) {
        listModel.addElement(s.getBoardTitle());
        sModel.addElement(s.getBoardTitle());
        data.createSchedule(s);
//        }
    }

    /**
     *
     */
    public void remove(String title, String month, String date, String hour) {

        // remove from both list and map
//        sModel.removeElement(key);
        data.deleteSchedule(title, month, date, hour);
    }

    public void removeAll(String title) {

        // remove from both list and map
        listModel.removeElement(title);
        sModel.removeElement(title);
        data.deleteallSchedule(title);
    }


    /**
     *
     */
    public ScheduleInfo get(Object key) {
        return data.getSchedule((String) key);
    }

    public ArrayList<ScheduleInfo> findCurrenttime(String year, String month, String date) {
        return data.getCurrentBillboardTitle(year, month, date);
    }

    public ScheduleInfo findSchedule(Object title, Object month ,Object date, Object hour) {
        return data.findSchedule((String) title,(String) month,(String) date,(String) hour);
    }

    public ScheduleInfo findSameSchedule(Object title, Object month, Object date, Object hour, Object minute, Object durationHr, Object durationMin
    ,Object recur) {
        return data.findSame((String) title,(String) month,(String) date,(String) hour, (String) minute, (String) durationHr,(String) durationMin, (String) recur);
    }

    public ScheduleInfo findRow(int index) {
        return data.findRow(index);
    }

    public void edit(String boardtitle, String creator, String year, String month, String date, String hour,
                     String minute, String duHr, String duMin, String recur) {
        data.editSchedule(boardtitle, creator, year, month, date, hour, minute, duHr, duMin, recur);
    }

    public ListModel take() {
        return sModel;
    }

    /**
     */
    public ListModel getModel() {
        return listModel;
    }

    /**
     */
    public int getSize() {
        return data.getSize();
    }

//    public Set<String> getScheduleList (){ return data.GetScheduleList(); }

}


