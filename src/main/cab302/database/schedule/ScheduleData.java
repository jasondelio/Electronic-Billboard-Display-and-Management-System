package cab302.database.schedule;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

public class ScheduleData {


    DefaultListModel listModel;

    SchedulesDatasource data;


    public ScheduleData() {
        listModel = new DefaultListModel();
        data = new SchedulesDatasource();
        for (String name : data.titleSet()) {
            listModel.addElement(name);
        }
    }

    /**
     */
    public void add(ScheduleInfo s) {

        if (!listModel.contains(s.getBoardTitle())) {
            listModel.addElement(s.getBoardTitle());
            data.createSchedule(s);
        }
    }

    /**
     */
    public void remove(Object key) {

        // remove from both list and map
        listModel.removeElement(key);
        data.deleteSchedule((String) key);
    }


    /**
     */
    public ScheduleInfo get(Object key) {
        return data.getSchedule((String) key);
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
}


