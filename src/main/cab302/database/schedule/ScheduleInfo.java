package cab302.database.schedule;

import java.io.Serializable;

/**
 * This class sets the schedule information
 */
public class ScheduleInfo implements Comparable<ScheduleInfo>, Serializable {
    private String year;

    private String month;

    private String date;

    private String hour;

    private String minute;

    private String boardtitle;

    private String creator;

    private String durationHr;

    private String durationMin;

    private String recur;


    public ScheduleInfo() {
    }

    /**
     * Set the schedule information as required
     *
     * @param boardtitle
     * @param creator
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param durationHr
     * @param durationMin
     * @param recur
     */
    public ScheduleInfo(String boardtitle, String creator, String year, String month, String date, String hour, String minute, String durationHr,
                        String durationMin, String recur) {
        this.month = month;
        this.hour = hour;
        this.date = date;
        this.minute = minute;
        this.boardtitle = boardtitle;
        this.creator = creator;
        this.durationHr = durationHr;
        this.durationMin = durationMin;
        this.recur = recur;
        this.year = year;
    }


    /**
     * @return boardtitle
     */
    public String getBoardTitle() {
        return boardtitle;
    }

    /**
     * @param boardtitle
     */
    public void setBoardTitle(String boardtitle) {
        this.boardtitle = boardtitle;
    }

    /**
     * @return creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return month
     */
    public String getMonth() {
        return month;
    }


    /**
     * @param month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     *
     * @return year
     */
    public String getYear() {
        return year;
    }


    /**
     *
     * @param year
     */
    public void setYear(String year) {
        this.year = year;
    }


    /**
     *
     * @return hour
     */
    public String getHour() {
        return hour;
    }

    /**
     *
     * @param hour
     */
    public void setHour(String hour) {
        this.hour = hour;
    }

    /**
     *
     * @return minute
     */
    public String getMinute() {
        return minute;
    }

    /**
     *
     * @param minute
     */
    public void setMinute(String minute) {
        this.minute = minute;
    }

    /**
     * @return durationHr
     */
    public String getDuHr() {
        return durationHr;
    }

    /**
     *
     * @param duHr
     */
    public void setDuHr(String duHr) {
        this.durationHr = duHr;
    }

    /**
     * @return durationMin
     */
    public String getDuMin() {
        return durationMin;
    }

    /**
     *
     * @param duMin
     */
    public void setDuMin(String duMin) {
        this.durationMin = duMin;
    }

    /**
     * @return recur
     */
    public String getRecur() {
        return recur;
    }

    /**
     *
     * @param recur
     */
    public void setRecur(String recur) {
        this.recur = recur;
    }

    /**
     *
     * @param other
     * @return Compared month
     */
    public int compareTo(ScheduleInfo other) {
        return this.month.compareTo(other.month);
    }

    /**
     *
     * @return Return every information of schedule to string
     */
    public String toString() {
        return boardtitle + " " + creator + " " + year + " " + month + " " + date + " " + hour + " " + minute + " " +
                durationHr + " " + durationMin + " " + recur;
    }

}
