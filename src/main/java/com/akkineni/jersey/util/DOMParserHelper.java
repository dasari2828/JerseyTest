package com.akkineni.jersey.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.akkineni.jersey.domain.Customer;

public class DOMParserHelper {

	public Customer readDOMParser(InputStream is) {
		Customer cus = new Customer();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			Element root = doc.getDocumentElement();
			if (root.getAttribute("id") != null
					&& !root.getAttribute("id").trim().equals("")) {
				cus.setId(Integer.valueOf(root.getAttribute("id")));
			}
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = (Node) nodes.item(i);
				if (node instanceof Element) {
					Element eElement = (Element) node;
					if (eElement.getTagName().equals("firstName")) {
						cus.setFirstName(eElement.getTextContent());
					}
					if (eElement.getTagName().equals("lastName")) {
						cus.setLastName(eElement.getTextContent());
					}
					if (eElement.getTagName().equals("street")) {
						cus.setStreet(eElement.getTextContent());
					}
					if (eElement.getTagName().equals("city")) {
						cus.setCity(eElement.getTextContent());
					}
					if (eElement.getTagName().equals("zip")) {
						cus.setZip(eElement.getTextContent());
					}
					if (eElement.getTagName().equals("country")) {
						cus.setCountry(eElement.getTextContent());
					}
					if (eElement.getTagName().equals("state")) {
						cus.setState(eElement.getTextContent());
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cus;
	}
}
