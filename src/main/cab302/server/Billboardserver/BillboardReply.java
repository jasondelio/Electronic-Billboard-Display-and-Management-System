package cab302.server.Billboardserver;

import java.io.Serializable;
/**
 * Replying to client and the client is able to retrieve billboard details
 */
public class BillboardReply implements Serializable {
    private String xmlcontent;
    private String creator;


    public BillboardReply(String xmlContent, String creator) {
        this.xmlcontent = xmlContent;
        this.creator = creator;
    }

    public String getXmlcontent() {
        return xmlcontent;
    }

    public String getCreator() {
        return creator;
    }

}

