package com.akkineni.jersey.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.akkineni.jersey.CustomerResource;

public class JerseyApplicaton extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public JerseyApplicaton() {
		super();
		singletons.add(new CustomerResource());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}