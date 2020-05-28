package cab302.database.schedule;

import java.io.Serializable;

public class ScheduleInfoStub implements Comparable<ScheduleInfoStub>, Serializable {

    private long date;

    private String boardTitle;

    private String creator;

    private long duration;

    private String recur;


    /**
     * Blank ScheduleInfoStub Constructor
     */
    public ScheduleInfoStub() {
    }

    /**
     * ScheduleInfoStub
     * @param boardTitle Title of billboard
     * @param creator Creator of scheduling
     * @param date Date in seconds (from Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS)
     * @param duration Duration of billboad in seconds
     * @param recur ???
     */
    public ScheduleInfoStub(String boardTitle, String creator, long date, long duration, String recur) {
        this.date = date;
        this.boardTitle = boardTitle;
        this.creator = creator;
        this.duration = duration;
        this.recur = recur;
    }


    /**
     *
     */
    public String getboardTitle() {
        return boardTitle;
    }

    /**
     *
     */
    public void setboardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }
    /**
     */
    public String getCreator() {
        return creator;
    }

    /**
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     */
    public long getDate() {
        return date;
    }

    /**
     */
    public void setDate(long date) {
        this.date = date;
    }

    /**
     * @return
     */
    public long getDuration() {
        return this.duration;
    }

    /**
     *
     */
    public void setDuration(long duration) {
        this.duration = duration;
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
     * @return
     */
    public int compareTo(ScheduleInfoStub other) {
        if (this.date < other.date)
            return 1;
        else
            return 0;
    }

    /**
     *
     */
    public String toString() {
        return boardTitle + " " + creator + " " + date + " " +
                duration + ", " + recur;
    }
}
