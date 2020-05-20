package cab302.database.schedule;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
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

    public ScheduleInfo find(Object key1, Object key2) {
        return data.findSchedule((String) key1, (String) key2);
    }

    public DefaultListModel take() {
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


