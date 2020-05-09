package cab302.viewer;

import cab302.viewer.gui.Gui;
import cab302.viewer.util.XMLParser;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        // Get data from the XML parser
        XMLParser parser = new XMLParser("xml-exmaples/16.xml");
        HashMap<String, String> xmlData = parser.parseXML();

        new Gui(xmlData);
    }

}