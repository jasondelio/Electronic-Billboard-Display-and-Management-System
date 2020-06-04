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
     * Remove items by using parameters to detect specific schedule
     *
     * @param title : the title of schedule to remove
     * @param month : the month of schedule to remove
     * @param date  : the date of schedule to remove
     * @param hour  : the hour of schedule to remove
     */
    public void remove(String title, String month, String date, String hour, String minute) {

        // remove from both list and map
        data.deleteSchedule(title, month, date, hour,minute);
    }

    /**
     * Remove all items from list and map
     *
     * @param title : the title of schedule to remove
     */
    public void removeAll(String title) {

        // remove from both list and map
        listModel.removeElement(title);
        sModel.removeElement(title);
        data.deleteAllSchedule(title);
    }


    /**
     * @param key : the key of a schedule to get the information
     * @return schedule data
     * Get schedule data by title
     */
    public ScheduleInfo get(Object key) {
        return data.getSchedule((String) key);
    }

    /**
     * Find schedule for current time
     *
     * @param year : the year of schedule to find at current time
     * @param month : the month of schedule to find at current time
     * @param date : the date of schedule to find at current time
     * @return the data of found schedule
     */
    public ArrayList<ScheduleInfo> findCurrenttime(String year, String month, String date) {
        return data.getCurrentBillboardTitle(year, month, date);
    }

    /**
     * @param title : the title of schedule to find
     * @param date : the date of schedule to find
     * @param hour : the hour of schedule to find
     * @return schedule data
     * Find schedule data depends on title, date and hour
     */
    public ScheduleInfo findSchedule(Object title, Object month, Object date, Object hour, Object minute) {
        return data.findSchedule((String) title, (String) month, (String) date, (String) hour, (String) minute);
    }

    /**
     * Find new schedule is already in the database or not
     *
     * @param title : the title of a schedule to verify it is already in the current database
     * @param month : the month of a schedule to verify it is already in the current database
     * @param date : the date of a schedule to verify it is already in the current database
     * @param hour : the hour of a schedule to verify it is already in the current database
     * @param minute : the minute of a schedule to verify it is already in the current database
     * @param durationHr : the durationHr of a schedule to verify it is already in the current database
     * @param durationMin : the durationMin of a schedule to verify it is already in the current database
     * @param recur : the recur of a schedule to verify it is already in the current database
     * @return ScheduleInfo
     */
    public ScheduleInfo findSameSchedule(Object title, Object month, Object date, Object hour, Object minute, Object durationHr, Object durationMin
            , Object recur) {
        return data.findSame((String) title, (String) month, (String) date, (String) hour, (String) minute, (String) durationHr, (String) durationMin, (String) recur);
    }

    /**
     * @param index : the index of a schedule which has to be found
     * @return schedule data by the given index of row
     * Find the schedule data which is on the given row of index
     */
    public ScheduleInfo findRow(int index) {
        return data.findRow(index);
    }

    /**
     * Edit schedule
     * @param boardtitle : the title of schedule to edit
     * @param creator : the creator of schedule to edit
     * @param year : the year of schedule to edit
     * @param month : the month of schedule to edit
     * @param date : the date of schedule to edit
     * @param hour : the hour of schedule to edit
     * @param minute : the minute of schedule to edit
     * @param duHr : the duHr of schedule to edit
     * @param duMin : the duMin of schedule to edit
     * @param recur : the recur of schedule to edit
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


}


