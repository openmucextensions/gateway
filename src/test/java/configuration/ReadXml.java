package configuration;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXml {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
				
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = db.parse("./config.xml");
		
		doc.getDocumentElement().normalize();
		NodeList nodes = doc.getElementsByTagName("wiring");
		
		System.out.println("Found " + nodes.getLength() + " wiring(s)");
		
		for(int index=0; index<nodes.getLength(); index++) {
			
			Node wiring = nodes.item(index);
			
			if(wiring.getNodeType() == Node.ELEMENT_NODE) {
				
				Element wiringElement = (Element) wiring;

				// accessing an attribute:
				// wiringElement.getAttribute("id");
				System.out.println("Input: " + wiringElement.getElementsByTagName("input").item(0).getTextContent());
				System.out.println("Output: " + wiringElement.getElementsByTagName("output").item(0).getTextContent());

			}
			
		}
		
	}

}
