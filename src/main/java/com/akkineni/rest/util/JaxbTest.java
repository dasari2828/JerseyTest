package com.akkineni.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import com.akkineni.schema.custom.InvoiceType;

public class JaxbTest {

	public static void main(String args[]) {
		try {
			JAXBContext jaxbCtx = JAXBContext
					.newInstance("com.akkineni.schema.custom");
			Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
			// unmarshaller.unmarshal(new File("testInput.xml"));
			XMLInputFactory factory = XMLInputFactory.newFactory();

			XMLStreamReader reader = factory
					.createXMLStreamReader(new FileInputStream(new File(
							"src/main/webapp/xml/input.xml")));

			while (reader.hasNext()) {
				int event = reader.next();
				System.out.println(event);
				if (event == XMLEvent.START_DOCUMENT) {
					String str = reader.getText();
					System.out.println(str);
				}

				if (event == XMLEvent.START_ELEMENT) {
					System.out.println(reader.getLocalName());
					if (reader.getLocalName() == "InvoiceType") {
						InvoiceType invoice = (InvoiceType) unmarshaller
								.unmarshal(reader);

						System.out.println(invoice.getBooks().getBook().get(0)
								.getName());
					}
				}

			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
}
