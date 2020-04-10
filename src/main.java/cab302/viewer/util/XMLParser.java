package cab302.viewer.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class XMLParser {

    private File documentToParse;

    public XMLParser(File documentToParse)
    {
        this.documentToParse = documentToParse;
    }

    public HashMap<String, String> parseXML() {

        HashMap<String, String> XMLDataMap = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(documentToParse);

            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (i % 2 == 0) continue;
                Node node = nodeList.item(i);


                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if (i != 3) {
                        XMLDataMap.put(node.getNodeName(), node.getTextContent().replaceAll("\n", "").replaceAll("\\s\\s+", ""));
                    } else {
                        XMLDataMap.put(node.getNodeName(), node.getAttributes().getNamedItem("url").getNodeValue());
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
