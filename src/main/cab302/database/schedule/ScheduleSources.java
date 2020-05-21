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
     */
    void deleteSchedule(String boradtitle);

    /**
     *
     * @return
     */
    void editSchedule(String boardtitle, String creator, String month, String date, String hour,
                      String minute, String duration);

    /**
     */
    void close();

    /**
     */
    Set<String> titleSet();

    Set<String> GetScheduleList();
}
