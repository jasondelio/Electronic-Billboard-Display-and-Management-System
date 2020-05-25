package cab302.database.schedule;

import java.util.Set;

public interface ScheduleSources {
    /**
     */
    void createSchedule(ScheduleInfo s);

    /**
     */
    ScheduleInfo getSchedule(String boradtitle);

    /**
     */
    int getSize();

    /**
     *
     */
    void deleteSchedule(String title, String date, String hour);

    /**
     * @return
     */
    void editSchedule(String boardtitle, String creator, String month, String date, String hour,
                      String minute, String duHr, String duMin, String recur);

    /**
     */
    void close();

    /**
     */
    Set<String> titleSet();

    Set<String> GetScheduleList();
}
