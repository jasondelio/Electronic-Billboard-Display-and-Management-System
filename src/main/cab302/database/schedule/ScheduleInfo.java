package cab302.database.schedule;

import java.io.Serializable;

public class ScheduleInfo implements Comparable<ScheduleInfo>, Serializable {

        private String month;

        private String date;
    
        private String hour;
    
        private String minute;

        private String boardtitle;

        private String creator;

        private String duration;



        /**
         */
        public ScheduleInfo() {
        }

        /**
         */
        public ScheduleInfo(String boardtitle, String creator, String month, String date, String hour, String minute, String duration) {
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
        public String getDate() {
            return date;
        }

        /**
         */
        public void setDate(String date) {
            this.date = date;
        }

        /**
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
         * @return
         */
        public String getHour() {
            return hour;
        }

        /**
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
         */
        public void setMinute(String minute) {
            this.minute = minute;
        }

        /**
         * @return
         */
        public String getDuration() {
            return duration; }

        /**
         */
        public void setDuration(String duration) {
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
