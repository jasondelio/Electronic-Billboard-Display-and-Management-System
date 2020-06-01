package cab302.database.RemovedSchedule;

import javax.swing.*;

public class RemovedScheduleData {


    DefaultListModel listModel;

    RemovedSchedulesDatasource data;



    public RemovedScheduleData() {
        listModel = new DefaultListModel();
        data = new RemovedSchedulesDatasource();
        for (String name : data.titleSet()) {
            listModel.addElement(name);
        }
    }

    /**
     */
    public void add(RemovedScheduleInfo s) {

//        if (!listModel.contains(s.getBoardTitle())) {
        listModel.addElement(s.getBoardTitle());
        data.createSchedule(s);
//        }
    }

    public void remove(String title) {

        // remove from both list and map
        listModel.removeElement(title);
        data.delateSchedules(title);
    }

    /**
     *
     */
    public RemovedScheduleInfo get(Object key,Object year,Object month,Object date,Object hour,Object minute,Object duraHr,Object duraMin) {
        return data.getSchedule((String) key,(String) year,(String) month,(String) date,(String) hour,(String) minute,(String) duraHr,(String) duraMin);
    }

    /**
     */
    public ListModel getModel() {
        return listModel;
    }


//    public Set<String> getScheduleList (){ return data.GetScheduleList(); }

}


