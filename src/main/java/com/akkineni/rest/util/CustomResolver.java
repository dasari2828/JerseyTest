package com.akkineni.rest.util;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.akkineni.rest.domain.Customer;
import com.akkineni.schema.custom.InvoiceType;

@Provider
@Produces("application/xml")
public class CustomResolver implements ContextResolver<JAXBContext> {

	private JAXBContext customerCtx;
	private JAXBContext invoiceContext;

	public CustomResolver() {
		super();
		try {
			this.customerCtx = JAXBContext.newInstance(Customer.class);
			this.invoiceContext = JAXBContext
					.newInstance("com.akkineni.schema.custom");

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JAXBContext getContext(Class<?> type) {
		JAXBContext ctx = null;
		if (type.equals(Customer.class)) {
			ctx = this.customerCtx;
		} else if (type.equals(InvoiceType.class)) {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@2");
			ctx = this.invoiceContext;
		} else {
		}

		return ctx;
	}
}
