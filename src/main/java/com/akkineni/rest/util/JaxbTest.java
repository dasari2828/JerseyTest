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

public class JaxbTest {

	public static void main(String args[]) {
		try {
			JAXBContext jaxbCtx = JAXBContext
					.newInstance("com.akkineni.nitwians.types.books");
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
					if (reader.getLocalName() == "Collection") {
						Collection coll = (Collection) unmarshaller
								.unmarshal(reader);

						// Books books = coll.getBooks();
						//
						// for (BookType type : books.getBook()) {
						// System.out.println(type);
						// System.out.println(type.getName());
						// }
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
