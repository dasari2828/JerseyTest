package com.akkineni.jersey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.akkineni.jersey.domain.Customer;

@Path("/customers")
public class CutomerResource {

	private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
	private AtomicInteger idCounter = new AtomicInteger();

	private final Logger LOGGER = Logger.getLogger(CutomerResource.class
			.getName());

	@GET
	@Produces("text/plain")
	public String getClichedMessage() {
		LOGGER.fine("getClichedMessage");
		return "Hello World";
	}

	@GET
	@Path("{id}")
	@Produces("application/xml")
	public StreamingOutput getCustomer(@PathParam("id") int id) {
		System.out.println(id);
		System.out.println(customerDB.size() + customerDB.toString());
		final Customer customer = customerDB.get(id);
		System.out.println(customer);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException,
					WebApplicationException {
				outputCustomer(outputStream, customer);
			}
		};
	}

	@POST
	@Consumes("application/xml")
	public Response createCustomer(InputStream is) {
		Customer customer = readDOMParser(is);
		customer.setId(idCounter.incrementAndGet());
		customerDB.put(customer.getId(), customer);
		System.out.println("Created customer " + customer.getId());
		return Response.created(URI.create("/customers/" + customer.getId()))
				.build();
	}

	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public void updateCustomer(@PathParam("id") int id, InputStream is) {
		Customer update = readIteratorCustomer(is);
		System.out.println(update.toString());
		Customer current = customerDB.get(id);
		if (current == null)
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		current.setFirstName(update.getFirstName());
		current.setLastName(update.getLastName());
		current.setStreet(update.getStreet());
		current.setState(update.getState());
		current.setZip(update.getZip());
		current.setCountry(update.getCountry());
	}

	protected void outputCustomer(OutputStream os, Customer cust)
			throws IOException {
		PrintStream writer = new PrintStream(os);
		writer.println("<customer id=\"" + cust.getId() + "\">");
		writer.println("	<first-name>" + cust.getFirstName() + "</first-name>");
		writer.println("	<last-name>" + cust.getLastName() + "</last-name>");
		writer.println("	<street>" + cust.getStreet() + "</street>");
		writer.println("	<city>" + cust.getCity() + "</city>");
		writer.println("	<state>" + cust.getState() + "</state>");
		writer.println("	<zip>" + cust.getZip() + "</zip>");
		writer.println("	<country>" + cust.getCountry() + "</country>");
		writer.println("</customer>");
	}

	protected Customer readCustomer(InputStream is) {
		Customer cust = new Customer();
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader xmlStreamReader = inputFactory
					.createXMLStreamReader(is);
			while (xmlStreamReader.hasNext()) {
				int event = xmlStreamReader.next();

				if (event == XMLStreamConstants.START_DOCUMENT) {
					System.out.println("Event Type:START_DOCUMENT");
					System.out.println("Document Encoding:"
							+ xmlStreamReader.getEncoding());
					System.out.println("XML Version:"
							+ xmlStreamReader.getVersion());
				}
				if (event == XMLStreamConstants.START_ELEMENT) {
					System.out.println("Event Type: START_ELEMENT");

					System.out.println("Element Prefix:"
							+ xmlStreamReader.getPrefix());
					System.out.println("Element Local Name:"
							+ xmlStreamReader.getLocalName());
					System.out.println("Namespace URI:"
							+ xmlStreamReader.getNamespaceURI());

					for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
						System.out.println("Attribute Prefix:"
								+ xmlStreamReader.getAttributePrefix(i));
						System.out.println("Attribute Namespace:"
								+ xmlStreamReader.getAttributeNamespace(i));
						System.out.println("Attribute Local Name:"
								+ xmlStreamReader.getAttributeLocalName(i));
						System.out.println("Attribute Value:"
								+ xmlStreamReader.getAttributeValue(i));
					}

				}
				if (event == XMLStreamConstants.ATTRIBUTE) {
					System.out.println("Event Type:ATTRIBUTE");
				}

				if (event == XMLStreamConstants.CHARACTERS) {
					System.out.println("Event Type: CHARACTERS");
					System.out.println("Text:" + xmlStreamReader.getText());
				}

				if (event == XMLStreamConstants.COMMENT) {
					System.out.println("Event Type:COMMENT");
					System.out.println("Comment Text:"
							+ xmlStreamReader.getText());
				}

				if (event == XMLStreamConstants.END_DOCUMENT) {
					System.out.println("Event Type:END_DOCUMENT");
				}

				if (event == XMLStreamConstants.END_ELEMENT) {
					System.out.println("Event Type: END_ELEMENT");
				}

				if (event == XMLStreamConstants.NAMESPACE) {
					System.out.println("Event Type:NAMESPACE");
				}

				if (event == XMLStreamConstants.PROCESSING_INSTRUCTION) {
					System.out.println("Event Type: PROCESSING_INSTRUCTION");
					System.out.println("PI Target:"
							+ xmlStreamReader.getPITarget());
					System.out
							.println("PI Data:" + xmlStreamReader.getPIData());
				}

				if (event == XMLStreamConstants.SPACE) {
					System.out.println("Event Type: SPACE");
					System.out.println("Text:" + xmlStreamReader.getText());

				}
			}
		} catch (FactoryConfigurationError e) {
			System.out.println("FactoryConfigurationError" + e.getMessage());
		} catch (XMLStreamException e) {
			System.out.println("XMLStreamException" + e.getMessage());
		}

		return cust;
	}

	protected Customer readIteratorCustomer(InputStream is) {
		Customer cust = new Customer();
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();

			XMLEventReader eventReader = inputFactory.createXMLEventReader(is);

			while (eventReader.hasNext()) {

				XMLEvent event = eventReader.nextEvent();

				if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
					StartElement startElement = event.asStartElement();
					System.out.println(startElement.toString());
					System.out.println(startElement.getName().getLocalPart());
				}
				// handle more event types here...
			}

		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return cust;
	}

	public final static String getEventTypeString(int eventType) {
		switch (eventType) {
		case XMLEvent.START_ELEMENT:
			return "START_ELEMENT";
		case XMLEvent.END_ELEMENT:
			return "END_ELEMENT";
		case XMLEvent.PROCESSING_INSTRUCTION:
			return "PROCESSING_INSTRUCTION";
		case XMLEvent.CHARACTERS:
			return "CHARACTERS";
		case XMLEvent.COMMENT:
			return "COMMENT";
		case XMLEvent.START_DOCUMENT:
			return "START_DOCUMENT";
		case XMLEvent.END_DOCUMENT:
			return "END_DOCUMENT";
		case XMLEvent.ENTITY_REFERENCE:
			return "ENTITY_REFERENCE";
		case XMLEvent.ATTRIBUTE:
			return "ATTRIBUTE";
		case XMLEvent.DTD:
			return "DTD";
		case XMLEvent.CDATA:
			return "CDATA";
		case XMLEvent.SPACE:
			return "SPACE";
		}
		return "UNKNOWN_EVENT_TYPE , " + eventType;
	}

	public Customer readDOMParser(InputStream is) {

		Customer cus = new Customer();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			Element root = doc.getDocumentElement();
			System.out.println("Root element :" + root.getNodeName());
			if (root.getAttribute("id") != null
					&& !root.getAttribute("id").trim().equals("")) {
				cus.setId(Integer.valueOf(root.getAttribute("id")));
			}

			NodeList nodes = root.getChildNodes();

			for (int i = 0; i < nodes.getLength(); i++) {

				Node node = (Node) nodes.item(i);

				if (node instanceof Element) {
					Element eElement = (Element) node;
					if (eElement.getTagName().equals("first-name")) {
						cus.setFirstName(eElement.getTextContent());
					}

					if (eElement.getTagName().equals("last-name")) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(cus);

		return cus;

	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
}
