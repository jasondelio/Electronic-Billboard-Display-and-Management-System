package cab302.database.billboard;

import java.io.*;

public class BillboardInfo implements Comparable<BillboardInfo>, Serializable {

        private String title;

        private String singer;

        private String rank;


        /**
         */
        public BillboardInfo() {
        }

        /**
         */
        public BillboardInfo(String title, String singer, String rank) {
            this.title = title;
            this.rank = rank;
            this.singer = singer;
        }

        /**
         */
        public String getSinger() {
            return singer;
        }

        /**
         */
        public void setSinger(String singer) {
            this.singer = singer;
        }

        /**
         */
        public String getTitle() {
            return title;
        }

        /**
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         */
        public String getRank() {
            return rank;
        }

        /**
         */
        public void setRank(String rank) {
            this.rank = rank;
        }

        /**
         */
        public int compareTo(BillboardInfo other) {
            return this.title.compareTo(other.title);
        }

        /**
         */
        public String toString() {
            return title + " " + singer + ", " + rank;
        }

}
