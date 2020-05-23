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

        if (!sModel.contains(s.getBoardTitle())) {
            sModel.addElement(s.getBoardTitle());
            data.createSchedule(s);
        }
    }

    /**
     */
    public void remove(Object key) {

        // remove from both list and map
        sModel.removeElement(key);
        data.deleteSchedule((String) key);
    }


    /**
     */
    public ScheduleInfo get(Object key) {
        return data.getSchedule((String) key);
    }

    public ScheduleInfo findSchedule(String month, String date) {
        return data.findSchedule(month, date);
    }

    public void edit(String boardtitle, String creator, String month, String date, String hour,
                     String minute, String duration)
    {
        data.editSchedule(boardtitle, creator, month, date, hour, minute, duration);

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


