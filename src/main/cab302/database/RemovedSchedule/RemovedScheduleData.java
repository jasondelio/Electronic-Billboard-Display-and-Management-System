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
     * Add element to listModel and data
     * @param s removed scheduleInfo to add in RemovedSchedule history
     */
    public void add(RemovedScheduleInfo s) {
        listModel.addElement(s.getBoardTitle());
        data.createSchedule(s);
    }


    /**
     *  delete element from listmodel and data
     * @param title billboard title
     */
    public void remove(String title) {
        // remove from both list and map
        listModel.removeElement(title);
        data.delateSchedules(title);
    }

    /**
     * method checking if the schedule is in removed history
     * @param key
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param duraHr
     * @param duraMin
     * @return RemovedScheduleInfo
     */
    public RemovedScheduleInfo get(Object key,Object year,Object month,Object date,Object hour,Object minute,Object duraHr,Object duraMin) {
        return data.getSchedule((String) key,(String) year,(String) month,(String) date,(String) hour,(String) minute,(String) duraHr,(String) duraMin);
    }

    /**@return list model
     */
    public ListModel getModel() {
        return listModel;
    }
}


