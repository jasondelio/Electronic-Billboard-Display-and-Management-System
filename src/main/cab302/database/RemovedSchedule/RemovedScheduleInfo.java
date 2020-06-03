package cab302.database.RemovedSchedule;

import java.io.Serializable;

/**
 * Class of removed schedule information
 */
public class RemovedScheduleInfo implements Comparable<RemovedScheduleInfo>, Serializable {
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

    public RemovedScheduleInfo(){};
    /**
     * Set the removedSchedule information as required
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
    public RemovedScheduleInfo(String boardtitle, String creator, String year, String month, String date, String hour, String minute, String durationHr,
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
     * @return get billboard title
     */
    public String getBoardTitle() {
        return boardtitle;
    }

    /**
     * enter billboard title
     * @param boardtitle billboard title
     */
    public void setBoardTitle(String boardtitle) {
            this.boardtitle = boardtitle;
        }
    /**
     * get creator
     * @return get creator
     */
    public String getCreator() {
            return creator;
        }
    /**
     * set creator
     * @param creator billboard's creator
     */
    public void setCreator(String creator) {
            this.creator = creator;
        }
    /**
     * get date
     * @return date
     */
    public String getDate() {
            return date;
        }

    /**
     * set date
     * @param date scheduled date
     */
    public void setDate(String date) {
            this.date = date;
        }

    /**
     * get month
     * @return month
     */
    public String getMonth() {
            return month;
        }


    /**
     * set month
     * @param month schedule month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * get year
     * @return year
     */
    public String getYear() {
        return year;
    }


    /**
     * set year
     * @param year schedule year
     */
    public void setYear(String year) {
        this.year = year;
    }


    /**
     * get hour
     * @return hour
     */
    public String getHour() {
        return hour;
    }

    /**
     * set hour
     * @param hour schedule hour
     */
    public void setHour(String hour) {
            this.hour = hour;
        }

    /**
     * get minute
     * @return minute
     */
    public String getMinute() {
        return minute;
    }

    /**
     * set minute
     * @param minute schedule minute
     */
    public void setMinute(String minute) {
        this.minute = minute;
    }

    /**
     * get durationHr
     * @return durationHr
     */
    public String getDuHr() {
        return durationHr;
    }

    /**
     * set duHr
     * @param duHr schedule duHr
     */
    public void setDuHr(String duHr) {
        this.durationHr = duHr;
    }

    /**
     * get durationMin
     * @return durationMin
     */
    public String getDuMin() {
        return durationMin;
    }

    /**
     * set duMin
     * @param duMin schedule duMin
     */
    public void setDuMin(String duMin) {
        this.durationMin = duMin;
    }

    /**
     * get recur
     * @return recur
     */
    public String getRecur() {
        return recur;
    }

    /**
     * set recur
     * @param recur schedule recur
     */
    public void setRecur(String recur) {
        this.recur = recur;
    }

    /**
     * compare to RemovedSchedule Information
     * @param other for comparing the RemovedScheduleInfo
     */
    public int compareTo(RemovedScheduleInfo other) {
        return this.month.compareTo(other.month);
    }

    /**
     * get minute
     * @return string connecting all information
     */
    public String toString() {
        return boardtitle + " " + creator + " " + year + " " + month + " " + date + " " + hour + " " + minute + " " +
                durationHr + " " + durationMin + " " + recur;
    }

}
