package cab302.database.schedule;

import javax.swing.*;
import java.util.Set;

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
    public void remove(String title, String date, String hour) {

        // remove from both list and map
//        sModel.removeElement(key);
        data.deleteSchedule(title, date, hour);
    }


    /**
     *
     */
    public ScheduleInfo get(Object key) {
        return data.getSchedule((String) key);
    }


    public ScheduleInfo findSchedule(String title, String date, String hour) {
        return data.findSchedule(title, date, hour);
    }

    public ScheduleInfo findRow(int index) {
        return data.findRow(index);
    }

    public void edit(String boardtitle, String creator, String month, String date, String hour,
                     String minute, String duHr, String duMin, String recur) {
        data.editSchedule(boardtitle, creator, month, date, hour, minute, duHr, duMin, recur);

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

    public Set<String> getScheduleList (){ return data.GetScheduleList(); }

}


