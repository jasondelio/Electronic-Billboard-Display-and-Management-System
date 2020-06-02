package cab302.database.RemovedSchedule;

import javax.swing.*;

/**
 *  interface class converting data to server
 */
public class RemovedScheduleData {


    DefaultListModel listModel;

    RemovedSchedulesDatasource data;


    /**
     *  Constructor initialising listmodel and data
     */
    public RemovedScheduleData() {
        listModel = new DefaultListModel();
        data = new RemovedSchedulesDatasource();
        for (String name : data.titleSet()) {
            listModel.addElement(name);
        }
    }

    /**
     *  Add element to listmodel and data
     */
    public void add(RemovedScheduleInfo s) {
        listModel.addElement(s.getBoardTitle());
        data.createSchedule(s);
    }


    /**
     *  delete element to listmodel and data
     */
    public void remove(String title) {
        // remove from both list and map
        listModel.removeElement(title);
        data.delateSchedules(title);
    }

    /**
     * method checking if the schedule is in removed history
     */
    public RemovedScheduleInfo get(Object key,Object year,Object month,Object date,Object hour,Object minute,Object duraHr,Object duraMin) {
        return data.getSchedule((String) key,(String) year,(String) month,(String) date,(String) hour,(String) minute,(String) duraHr,(String) duraMin);
    }

    /**
     */
    public ListModel getModel() {
        return listModel;
    }
}


