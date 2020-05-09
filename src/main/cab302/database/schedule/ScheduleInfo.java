package cab302.database.schedule;

import java.io.Serializable;

public class ScheduleInfo implements Comparable<ScheduleInfo>, Serializable {

        private Integer month;

        private Integer date;
    
        private Integer hour;
    
        private Integer minute;

        private String boardtitle;

        private String creator;

        private Integer duration;



        /**
         */
        public ScheduleInfo() {
        }

        /**
         */
        public ScheduleInfo(String boardtitle, String creator, Integer month, Integer date, Integer hour, Integer minute, Integer duration) {
            this.month = month;
            this.hour = hour;
            this.date = date;
            this.minute = minute;
            this.boardtitle = boardtitle;
            this.creator = creator;
            this.duration = duration;
        }

        /**
         */
        public String getBoardTitle() {
            return boardtitle;
        }

        /**
         */
        public void setBoardTitle(String boardtitle) {
            this.boardtitle = boardtitle;
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
        public Integer getDate() {
            return date;
        }

        /**
         */
        public void setDate(Integer date) {
            this.date = date;
        }

        /**
         */
        public Integer getMonth() {
            return month;
        }

        /**
         */
        public void setMonth(Integer month) {
            this.month = month;
        }

        /**
         */
        public Integer getHour() {
            return hour;
        }

        /**
         */
        public void setHour(Integer hour) {
            this.hour = hour;
        }

        /**
         */
        public Integer getMinute() {
            return minute;
        }

        /**
         */
        public void setMinute(Integer minute) {
            this.minute = minute;
        }

        /**
         */
        public Integer getDuration() {
            return duration; }

        /**
         */
        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        /**
         */
        public int compareTo(ScheduleInfo other) {
            return this.month.compareTo(other.month);
        }

        /**
         */
        public String toString() {
            return boardtitle + " " + creator + " " + month + " " + date + " " + hour + " " + minute + ", " + duration;
        }

}
