package cab302.viewer;

import cab302.controlpanel.dataobjects.Billboard;
import cab302.controlpanel.datasources.BillboardDataSource;
import cab302.viewer.gui.Gui;
import cab302.viewer.util.XMLParser;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        // Get Billboard from Database
        Billboard billboard = new BillboardDataSource().getBillboard("test");

        // Get data from the XML parser
        XMLParser parser = new XMLParser(billboard);
        HashMap<String, String> xmlData = parser.parseXML();

        // Create GUI
        new Gui(xmlData);
    }

}
