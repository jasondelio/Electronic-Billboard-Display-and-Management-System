package cab302.database.schedule;

import javax.swing.*;
import java.util.ArrayList;

public class ScheduleData {


    DefaultListModel listModel;

    SchedulesDatasource data;

    DefaultListModel sModel;

    /**
     * Set the list of billboard title and list of schedule
     */
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
     * @param s Add new schedule into SQL quarry
     */
    public void add(ScheduleInfo s) {

        listModel.addElement(s.getBoardTitle());
        sModel.addElement(s.getBoardTitle());
        data.createSchedule(s);
    }

    /**
     * @param title
     * @param month
     * @param date
     * @param hour  Remove items by using parameters to detect specific schedule
     */
    public void remove(String title, String month, String date, String hour) {

        // remove from both list and map
//        sModel.removeElement(key);
        data.deleteSchedule(title, month, date, hour);
    }

    /**
     * Remove all items from list and map
     *
     * @param title
     */
    public void removeAll(String title) {

        // remove from both list and map
        listModel.removeElement(title);
        sModel.removeElement(title);
        data.deleteallSchedule(title);
    }


    /**
     * @param key
     * @return schedule data
     * Get schedule data by title
     */
    public ScheduleInfo get(Object key) {
        return data.getSchedule((String) key);
    }

    /**
     * Find schedule for current time
     *
     * @param year
     * @param month
     * @param date
     * @return
     */
    public ArrayList<ScheduleInfo> findCurrenttime(String year, String month, String date) {
        return data.getCurrentBillboardTitle(year, month, date);
    }

    /**
     * @param title
     * @param date
     * @param hour
     * @return schedule data
     * Find schedule data depends on title, date and hour
     */
    public ScheduleInfo findSchedule(Object title, Object month, Object date, Object hour) {
        return data.findSchedule((String) title, (String) month, (String) date, (String) hour);
    }

    /**
     * Find new schedule is already in the database or not
     *
     * @param title
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param durationHr
     * @param durationMin
     * @param recur
     * @return ScheduleInfo
     */
    public ScheduleInfo findSameSchedule(Object title, Object month, Object date, Object hour, Object minute, Object durationHr, Object durationMin
            , Object recur) {
        return data.findSame((String) title, (String) month, (String) date, (String) hour, (String) minute, (String) durationHr, (String) durationMin, (String) recur);
    }

    /**
     * @param index
     * @return schedule data by the given index of row
     * Find the schedule data which is on the given row of index
     */
    public ScheduleInfo findRow(int index) {
        return data.findRow(index);
    }

    /**
     * @param boardtitle
     * @param creator
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param duHr
     * @param duMin
     * @param recur      Edit schedule
     */
    public void edit(String boardtitle, String creator, String year, String month, String date, String hour,
                     String minute, String duHr, String duMin, String recur) {
        data.editSchedule(boardtitle, creator, year, month, date, hour, minute, duHr, duMin, recur);
    }

    /**
     * @return the list of every schedule
     */
    public ListModel take() {
        return sModel;
    }

    /**
     * @return list model
     */
    public ListModel getModel() {
        return listModel;
    }

    /**
     * @return the size of schedule
     */
    public int getSize() {
        return data.getSize();
    }

//    public Set<String> getScheduleList (){ return data.GetScheduleList(); }

}


