package cab302.database.schedule;

import java.util.ArrayList;
import java.util.Set;

public interface ScheduleSources {
    /**
     */
    void createSchedule(ScheduleInfo s);

    /**
     */
    ScheduleInfo getSchedule(String boradtitle);
    ScheduleInfo findSame(String title, String month, String date, String hour, String minute, String durationHr, String durationMin, String recur);
    ScheduleInfo findSchedule(String title, String month, String date, String hour);
    /**
     */
    int getSize();
    ArrayList<ScheduleInfo> getCurrentBillboardTitle(String year, String month, String date);
    /**
     *
     */
    void deleteSchedule(String title, String month, String date, String hour);
    /**
     *
     */
    void deleteallSchedule(String title);

    /**
     * @return
     */
    void editSchedule(String boardtitle, String creator, String year, String month, String date, String hour,
                      String minute, String duHr, String duMin, String recur);

    /**
     */
    void close();

    /**
     */
    Set<String> titleSet();

    Set<String> GetScheduleList();
}
