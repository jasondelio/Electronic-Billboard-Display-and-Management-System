package cab302.database.billboard;

import java.io.Serializable;

/**
 * Stores information of users
 */
public class BillboardInfo implements Comparable<BillboardInfo>, Serializable {

    private static final long serialVersionUID = -7092701502990374424L;

    private String name;

    private String xmlContent;

    private String creator;

    /**
     *
     */
    public BillboardInfo() {
    }

    /**
     *
     */
    public BillboardInfo(String name, String xmlContent, String creator) {
        this.name = name;
        this.xmlContent = xmlContent;
        this.creator = creator;
    }

    /**
     *
     */
    public String getXMLContent() {
        return xmlContent;
    }

    /**
     *
     */
    public void setXMLContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    /**
     *
     */
    public void setName(String name) {
        this.name = name;
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
    public int compareTo(BillboardInfo other) {
        return this.name.compareTo(other.name);
    }

    /**
     *
     */
    public String toString() {
        return name + ", " + xmlContent;
    }

}