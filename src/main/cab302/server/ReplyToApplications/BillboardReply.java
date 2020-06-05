package cab302.server.ReplyToApplications;

import java.io.Serializable;
/**
 * Replying to client and the client is able to retrieve billboard details
 */
public class BillboardReply implements Serializable {
    private String xmlcontent;
    private String creator;

    /**
     * Constructor setting value to get billboard information to an application
     * @param xmlContent xml content of billboard
     * @param creator creator of the billboard
     */
    public BillboardReply(String xmlContent, String creator) {
        this.xmlcontent = xmlContent;
        this.creator = creator;
    }
    /**
     * @return xmlcontent
     */
    public String getXmlcontent() {
        return xmlcontent;
    }
    /**
     * @return creator
     */
    public String getCreator() {
        return creator;
    }

}

