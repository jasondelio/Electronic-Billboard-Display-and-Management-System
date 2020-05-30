package cab302.database.schedule;

import java.io.Serializable;

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


    /**
     *
     */
    public ScheduleInfo() {
    }

    /**
     *
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
     *
     */
    public String getBoardTitle() {
        return boardtitle;
    }

    /**
     *
     */
    public void setBoardTitle(String boardtitle) {
            this.boardtitle = boardtitle;
        }
    /**
     *
     */
    public String getCreator() {
            return creator;
        }
    /**
     *
     */
    public void setCreator(String creator) {
            this.creator = creator;
        }
    /**
     *
     */
    public String getDate() {
            return date;
        }

    /**
     *
     */
    public void setDate(String date) {
            this.date = date;
        }

    /**
     *
     */
    public String getMonth() {
            return month;
        }


    /**
     * @param
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     *
     */
    public String getYear() {
        return year;
    }


    /**
     * @param
     */
    public void setYear(String year) {
        this.year = year;
    }


    /**
     * @return
     */
    public String getHour() {
        return hour;
    }

    /**
     *
     */
    public void setHour(String hour) {
            this.hour = hour;
        }

    /**
     * @return
     */
    public String getMinute() {
        return minute;
    }

    /**
     *
     */
    public void setMinute(String minute) {
        this.minute = minute;
    }

    /**
     * @return
     */
    public String getDuHr() {
        return durationHr;
    }

    /**
     *
     */
    public void setDuHr(String duHr) {
        this.durationHr = duHr;
    }

    /**
     * @return
     */
    public String getDuMin() {
        return durationMin;
    }

    /**
     *
     */
    public void setDuMin(String duMin) {
        this.durationMin = duMin;
    }

    /**
     * @return
     */
    public String getRecur() {
        return recur;
    }

    /**
     *
     */
    public void setRecur(String recur) {
        this.recur = recur;
    }

    /**
     *
     */
    public int compareTo(ScheduleInfo other) {
        return this.month.compareTo(other.month);
    }

    /**
     *
     */
    public String toString() {
        return boardtitle + " " + creator + " " + year + " " + month + " " + date + " " + hour + " " + minute + " " +
                durationHr + " " + durationMin + " " + recur;
    }

}
