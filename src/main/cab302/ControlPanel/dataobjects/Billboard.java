package cab302.controlpanel.dataobjects;

import java.io.*;

/**
 * Stores information of users
 */
public class Billboard implements Comparable<Billboard>, Serializable {

    private static final long serialVersionUID = -7092701502990374424L;

    private String name;

    private String xmlContent;



    /**
     */
    public Billboard() {
    }

    /**
     */
    public Billboard(String name, String xmlContent) {
        this.name = name;
        this.xmlContent = xmlContent;

    }

    /**
     */
    public String getXMLContent() {
        return xmlContent;
    }

    /**
     */
    public void setXMLContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }

    /**
     */
    public String getName() {
        return name;
    }

    /**
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     */
    public int compareTo(Billboard other) {
        return this.name.compareTo(other.name);
    }

    /**
     */
    public String toString() {
        return name + ", " + xmlContent;
    }

}