package cab302.viewer.util;

import cab302.controlpanel.dataobjects.Billboard;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Objects;

public class XMLParser {

    private Billboard billboard;
    private String billboardStr;

    public XMLParser(Billboard b)
    {
        this.billboard = b;
    }
    public XMLParser(String billboardStr) {
        this.billboardStr = billboardStr;
    }

    public HashMap<String, String> parseXML() {

        HashMap<String, String> XMLDataMap = new HashMap<>();

        String xmlContent;
        if (this.billboard != null)
            xmlContent = this.billboard.getXMLContent();
        else
            xmlContent = Objects.requireNonNullElse(
                this.billboardStr,
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<billboard>\n<message>No Billboard Found</message>\n</billboard>"
            );

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlContent));

            Document document = builder.parse(is);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getDocumentElement().getChildNodes();

            if (document.getDocumentElement().hasAttribute("background")) {
                XMLDataMap.put("bgColour", document.getDocumentElement().getAttribute("background"));
            }

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (i % 2 == 0) continue;
                Node node = nodeList.item(i);


                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (!Objects.equals(node.getNodeName(), "picture")) {
                        XMLDataMap.put(
                                node.getNodeName(),
                                node.getTextContent().replaceAll("\n", "")
                                        .replaceAll(
                                                "\\s\\s+",
                                                "")
                                        + (node.hasAttributes() ? "%%%%" + node.getAttributes().getNamedItem("colour").getTextContent() : ""));
                    } else {
                        if (node.getAttributes().getNamedItem("url") != null)
                            XMLDataMap.put(node.getNodeName(), node.getAttributes().getNamedItem("url").getNodeValue());
                        else if (node.getAttributes().getNamedItem("data") != null)
                            XMLDataMap.put(node.getNodeName(), node.getAttributes().getNamedItem("data").getNodeValue());
                        else
                            XMLDataMap.put(node.getNodeName(), null);
                    }
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }


        return XMLDataMap;
    }

}
