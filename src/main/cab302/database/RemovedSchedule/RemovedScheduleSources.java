package cab302.database.RemovedSchedule;

import java.util.Set;

public interface RemovedScheduleSources {
    /** create schedule
     */
    void createSchedule(RemovedScheduleInfo s);

    /** delete schedule info
     */
    void delateSchedules(String title);

    /** get removed schedule info
     */
    RemovedScheduleInfo getSchedule(String title, String year, String month, String date, String hour, String minute, String durationHr, String durationMin);

    /** title set
     */
    Set<String> titleSet();

    /** close
     */
    void close();
}
