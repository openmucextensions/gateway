package org.openmucextensions.app.gateway;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigurationUtil {
			
	public static List<Wiring> loadConfiguration(final String filename) throws IOException {
		
		try {
			
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(filename);
			
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("wiring");
			
			List<Wiring> result = new ArrayList<>();
			
			for(int index=0; index<nodes.getLength(); index++) {
				
				Node node = nodes.item(index);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					
					if(element.getElementsByTagName("input").getLength() > 0) {
						String inputChannelId = element.getElementsByTagName("input").item(0).getTextContent();
						if(element.getElementsByTagName("output").getLength() > 0) {
							String outputChannelId = element.getElementsByTagName("output").item(0).getTextContent();
							result.add(new Wiring(inputChannelId, outputChannelId));
						}
					}
				}
			}
			
			return result;
			
		} catch (DOMException e) {
			throw new IOException(e);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}
	
	public static void createTestConfiguration(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"); writer.newLine();
		writer.write("<wirings>"); writer.newLine();
		writer.write("<!--"); writer.newLine();
		writer.write("<wiring><input>inputChannelId</input><output>outputChannelId</output></wiring>"); writer.newLine();
		writer.write("-->"); writer.newLine();
		writer.write("</wirings>"); writer.newLine();
		writer.flush();
		writer.close();
	}
	
}
