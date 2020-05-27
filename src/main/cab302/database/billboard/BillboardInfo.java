package cab302.database.billboard;

import java.io.Serializable;

/**
 * Stores the billboard details for a billboard
 */
public class BillboardInfo implements Serializable {

    private static final long serialVersionUID = -7092701502990374424L;

    private String name;

    private String xmlContent;

    private String creator;

    /**
     * No args constructor.
     */
    public BillboardInfo() {
    }

    /**
     * Constructor to set values for the billboard's details.
     *
     * @param name       billboard's name
     * @param xmlContent billboard's xmlContent
     * @param creator    billboard's creator
     */
    public BillboardInfo(String name, String xmlContent, String creator) {
        this.name = name;
        this.xmlContent = xmlContent;
        this.creator = creator;
    }

    /**
     * @return the billboard's xmlContent
     */
    public String getXMLContent() {
        return xmlContent;
    }

    /**
     *
     * @param xmlContent the xmlContent to set
     */
    public void setXMLContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }

    /**
     *
     * @return the billboard's name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name billboard's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the billboard's creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     *
     * @param creator billboard's creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }


    /**
     * @see Object#toString()
     */
    public String toString() {
        return name + ", " + xmlContent + ", " + creator;
    }

}