package com.akkineni.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.EventFilter;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.akkineni.rest.domain.Customer;

public class StaxParserHelper {

	public static final List<Customer> createCustomerList(InputStream is) {
		ArrayList<Customer> custList = new ArrayList<Customer>();
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader xmlStreamReader = inputFactory
					.createXMLStreamReader(is);
			while (xmlStreamReader.hasNext()) {
				int event = xmlStreamReader.next();
				if (event == XMLStreamConstants.START_DOCUMENT) {
				}
				if (event == XMLStreamConstants.START_ELEMENT) {
					System.out.println(xmlStreamReader.getLocalName());
					if (xmlStreamReader.getLocalName() == "customer") {
						try {
							JAXBContext ctx = JAXBContext
									.newInstance(Customer.class);
							Unmarshaller m = ctx.createUnmarshaller();
							Customer cust = (Customer) m
									.unmarshal(xmlStreamReader);
							custList.add(cust);
						} catch (JAXBException e) {
							e.printStackTrace();
						}
					}
				}
				if (event == XMLStreamConstants.ATTRIBUTE) {
				}
				if (event == XMLStreamConstants.CHARACTERS) {
				}
				if (event == XMLStreamConstants.COMMENT) {
				}
				if (event == XMLStreamConstants.END_DOCUMENT) {
				}
				if (event == XMLStreamConstants.END_ELEMENT) {
				}
				if (event == XMLStreamConstants.NAMESPACE) {
				}
				if (event == XMLStreamConstants.PROCESSING_INSTRUCTION) {
				}
				if (event == XMLStreamConstants.SPACE) {
				}
			}
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
			System.out.println("FactoryConfigurationError" + e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			System.out.println("XMLStreamException" + e.getMessage());
		}
		return custList;
	}

	public static final Customer createCustomerFromInputStream(InputStream is) {
		Customer cust = null;
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader xmlStreamReader = inputFactory
					.createXMLStreamReader(is);
			while (xmlStreamReader.hasNext()) {
				int event = xmlStreamReader.next();
				if (event == XMLStreamConstants.START_DOCUMENT) {
				}
				if (event == XMLStreamConstants.START_ELEMENT) {
					System.out.println(xmlStreamReader.getLocalName());
					if (xmlStreamReader.getLocalName() == "customer") {
						try {
							JAXBContext ctx = JAXBContext
									.newInstance(Customer.class);
							Unmarshaller m = ctx.createUnmarshaller();
							cust = (Customer) m.unmarshal(xmlStreamReader);
						} catch (JAXBException e) {
							e.printStackTrace();
						}
					}
				}
				if (event == XMLStreamConstants.ATTRIBUTE) {
				}
				if (event == XMLStreamConstants.CHARACTERS) {
				}
				if (event == XMLStreamConstants.COMMENT) {
				}
				if (event == XMLStreamConstants.END_DOCUMENT) {
				}
				if (event == XMLStreamConstants.END_ELEMENT) {
				}
				if (event == XMLStreamConstants.NAMESPACE) {
				}
				if (event == XMLStreamConstants.PROCESSING_INSTRUCTION) {
				}
				if (event == XMLStreamConstants.SPACE) {
				}
			}
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
			System.out.println("FactoryConfigurationError" + e.getMessage());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			System.out.println("XMLStreamException" + e.getMessage());
		}
		return cust;
	}

	public static final Customer createCustomerFromInputStream2(InputStream is) {
		Customer cust = null;
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			EventFilter filter = new EventFilter() {
				@Override
				public boolean accept(XMLEvent event) {
					return event.isStartElement();
				}
			};
			XMLEventReader eventReader = inputFactory.createXMLEventReader(is);
			XMLEventReader filteredEventReader = inputFactory
					.createFilteredReader(eventReader, filter);

			StartElement startElement = (StartElement) filteredEventReader
					.nextEvent();

			System.out.println("Start Element: " + startElement.getName());
			if (startElement.getName().getLocalPart()
					.equalsIgnoreCase("customer")) {
				try {
					JAXBContext ctx = JAXBContext.newInstance(Customer.class);
					Unmarshaller m = ctx.createUnmarshaller();
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					cust = (Customer) m.unmarshal(eventReader);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}

			filteredEventReader.close();
			eventReader.close();

		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cust;
	}

	protected void outputCustomer(OutputStream os, Customer cust)
			throws IOException {
		PrintStream writer = new PrintStream(os);
		writer.println("<customer id=\"" + cust.getId() + "\">");
		writer.println("	<firstName>" + cust.getFirstName() + "</firstName>");
		writer.println("	<lastName>" + cust.getLastName() + "</lastName>");
		writer.println("	<street>" + cust.getStreet() + "</street>");
		writer.println("	<city>" + cust.getCity() + "</city>");
		writer.println("	<state>" + cust.getState() + "</state>");
		writer.println("	<zip>" + cust.getZip() + "</zip>");
		writer.println("	<country>" + cust.getCountry() + "</country>");
		writer.println("</customer>");
	}
}
