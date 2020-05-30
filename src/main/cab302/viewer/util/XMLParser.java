package cab302.viewer.util;

import cab302.database.billboard.BillboardInfo;
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

    private BillboardInfo billboard;
    private String billboardStr;
    /**
     * XMLParser constructor for use with BillboardInfo objects
     * @param b The BillboardInfo object containing XML to parse
     */
    public XMLParser(BillboardInfo b)
    {
        this.billboard = b;
    }
    /**
     * XMLParser constructor for use with raw XML strings
     * @param billboardStr The XML content string to parse
     */
    public XMLParser(String billboardStr) {
        this.billboardStr = billboardStr;
    }
    /**
     * Main XML parsing method
     * @return HashMap containing key-value pairs of nodes and their contents
     */
    public HashMap<String, String> parseXML() {

        HashMap<String, String> XMLDataMap = new HashMap<>();

        String xmlContent;
        // Assign value to xmlContent
        if (this.billboard != null)
            xmlContent = this.billboard.getXMLContent();
        else
            xmlContent = Objects.requireNonNullElse(
                    this.billboardStr,
                    "<?xml version=\"1.0\"encoding=\"UTF-8\"?>\n<billboard>\n<message>No Billboard Found</message>\n</billboard>"
            );

        try {
            // Create DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Get InputSource
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlContent));
            // Create XML Document
            Document document = builder.parse(is);
            document.getDocumentElement().normalize();
            // Get the NodeList from XML
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            // Get the background attribute and put it into the HashMap
            if (document.getDocumentElement().hasAttribute("background")) {
                XMLDataMap.put("bgColour", document.getDocumentElement().getAttribute("background"));
            }
            // Iterate through the nodes in the NodeList
            for (int i = 0; i < nodeList.getLength(); i++) {
                // Only take tags, instead of their types
                if (i % 2 == 0) continue;
                Node node = nodeList.item(i);

                // Check that the node is of type ELEMENT_NODE
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    // If the node does not correspond to a picture, add text and name to the HashMap
                    if (!Objects.equals(node.getNodeName(), "picture")) {
                        XMLDataMap.put(
                                node.getNodeName(),
                                node.getTextContent().replaceAll("\n", "")
                                        .replaceAll("\\s\\s+", "")
                        );
                        // If the node has attributes, add the colour attribute to the HashMap, otherwise,
                        // add colour #000 as a default
                        if (node.hasAttributes()) {
                            XMLDataMap.put(
                                    node.getNodeName() + "Colour",
                                    node.getAttributes().getNamedItem("colour").getTextContent()
                            );
                        } else {
                            XMLDataMap.put(node.getNodeName() + "Colour", "#000000");
                        }
                        // If the node is a picture, get the attributes as either URL or Data string, null otherwise
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

        // Return the HashMap
        return XMLDataMap;
    }

}
