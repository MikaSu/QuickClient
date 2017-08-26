package org.quickclient.classes;

import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLConfigReader {

	public static Vector<ListConfiguration> readAttributes(String docbaseName) {
		Vector<ListConfiguration> config = new Vector<ListConfiguration>();
		Vector<String> confignames = new Vector<String>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = null;
			String configXpath = "/listattributes/repository[@name='" + docbaseName + "']/config";
			NodeList nodes = null;
			String xmlString = Utils.readFileAsString("listattributes.xml");
			if (xmlString != null) {
				DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(new InputSource(new StringReader(xmlString)));
				XPath xpath = XPathFactory.newInstance().newXPath();
				nodes = (NodeList) xpath.evaluate(configXpath, document, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					String configid = node.getAttributes().getNamedItem("id").getNodeValue();
					confignames.add(configid);
				}
			}
			for (String confname : confignames) {
				String categoryXpath = "/listattributes/repository[@name='" + docbaseName + "']/config[@id='" + confname + "']/attribute";
				NodeList nodes2 = null;
				ListConfiguration listconfig = new ListConfiguration(confname);
				XPath xpath = XPathFactory.newInstance().newXPath();
				nodes2 = (NodeList) xpath.evaluate(categoryXpath, document, XPathConstants.NODESET);
				for (int i = 0; i < nodes2.getLength(); i++) {
					Node node = nodes2.item(i);
					// String value = node.getNodeValue();
					// String nodename = node.getNodeName();
					// System.out.println(nodename + " " + value);
					NamedNodeMap attrs = node.getAttributes();
					String type = attrs.getNamedItem("type").getNodeValue();
					String attribute = attrs.getNamedItem("name").getNodeValue();
					Node labelnode = attrs.getNamedItem("label");
					ListAttribute a = null;
					String label = null;
					if (labelnode != null) {
						label = labelnode.getNodeValue();
						System.out.println(type + " " + attribute);
						a = new ListAttribute(type, attribute, label);
					} else {
						a = new ListAttribute(type, attribute);
					}
					listconfig.append(a);
				}
				config.add(listconfig);
			}

			//read "default" attributelist
			String categoryXpath = "/listattributes/config[@id='default']/attribute";
			NodeList nodes2 = null;
			ListConfiguration listconfig = new ListConfiguration("default_all_repo");
			XPath xpath = XPathFactory.newInstance().newXPath();
			nodes2 = (NodeList) xpath.evaluate(categoryXpath, document, XPathConstants.NODESET);
			for (int i = 0; i < nodes2.getLength(); i++) {
				Node node = nodes2.item(i);
				NamedNodeMap attrs = node.getAttributes();
				String type = attrs.getNamedItem("type").getNodeValue();
				String attribute = attrs.getNamedItem("name").getNodeValue();
				Node labelnode = attrs.getNamedItem("label");
				ListAttribute a = null;
				String label = null;
				if (labelnode != null) {
					label = labelnode.getNodeValue();
					System.out.println(type + " " + attribute);
					a = new ListAttribute(type, attribute, label);
				} else {
					a = new ListAttribute(type, attribute);
				}
				listconfig.append(a);
			}
			config.add(listconfig);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}
}
