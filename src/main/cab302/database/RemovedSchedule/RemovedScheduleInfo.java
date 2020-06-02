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
     * Constructor of removed schedule information
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
     * get billboard title
     */
    public String getBoardTitle() {
        return boardtitle;
    }

    /**
     * set billboard title
     */
    public void setBoardTitle(String boardtitle) {
            this.boardtitle = boardtitle;
        }
    /**
     * get creator
     */
    public String getCreator() {
            return creator;
        }
    /**
     * set creator
     */
    public void setCreator(String creator) {
            this.creator = creator;
        }
    /**
     * get date
     */
    public String getDate() {
            return date;
        }

    /**
     * set date
     */
    public void setDate(String date) {
            this.date = date;
        }

    /**
     * get month
     */
    public String getMonth() {
            return month;
        }


    /**
     * set month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * get year
     */
    public String getYear() {
        return year;
    }


    /**
     * set year
     */
    public void setYear(String year) {
        this.year = year;
    }


    /**
     * get hour
     */
    public String getHour() {
        return hour;
    }

    /**
     * set hour
     */
    public void setHour(String hour) {
            this.hour = hour;
        }

    /**
     *  get minute
     */
    public String getMinute() {
        return minute;
    }

    /**
     * set minute
     */
    public void setMinute(String minute) {
        this.minute = minute;
    }

    /**
     *  get Duration hour
     */
    public String getDuHr() {
        return durationHr;
    }

    /**
     * set Duration hour
     */
    public void setDuHr(String duHr) {
        this.durationHr = duHr;
    }

    /**
     *  get Duration minute
     */
    public String getDuMin() {
        return durationMin;
    }

    /**
     * set Duration minute
     */
    public void setDuMin(String duMin) {
        this.durationMin = duMin;
    }

    /**
     *  get recur
     */
    public String getRecur() {
        return recur;
    }

    /**
     * set recur
     */
    public void setRecur(String recur) {
        this.recur = recur;
    }

    /**
     * compare
     */
    public int compareTo(RemovedScheduleInfo other) {
        return this.month.compareTo(other.month);
    }

    /**
     * set to string
     */
    public String toString() {
        return boardtitle + " " + creator + " " + year + " " + month + " " + date + " " + hour + " " + minute + " " +
                durationHr + " " + durationMin + " " + recur;
    }

}
