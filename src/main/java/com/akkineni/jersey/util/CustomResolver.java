package com.akkineni.jersey.util;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.akkineni.jersey.domain.Customer;

@Provider
@Produces("application/xml")
public class CustomResolver implements ContextResolver<JAXBContext> {

	private JAXBContext ctx;

	public CustomResolver() {
		super();
		try {
			this.ctx = JAXBContext.newInstance(Customer.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JAXBContext getContext(Class<?> type) {
		if (type.equals(Customer.class)) {
			System.out.println("****************************" + ctx.hashCode());
			return ctx;
		} else {
			return null;
		}
	}

}
