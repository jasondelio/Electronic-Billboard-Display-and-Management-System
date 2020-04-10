package cab302.viewer;

import cab302.viewer.util.XMLParser;

import java.io.File;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        XMLParser parser = new XMLParser(new File("xml-exmaples/billboard-example-1.xml"));
        HashMap<String, String> xmlData = parser.parseXML();

        System.out.println(xmlData.get("picture"));
    }

}
