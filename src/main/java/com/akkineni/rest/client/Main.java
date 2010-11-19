package com.akkineni.rest.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String baseUri = "http://localhost:9998/";
			final Map<String, String> initParams = new HashMap<String, String>();
			initParams.put("com.sun.jersey.config.property.packages",
					"com.akkineni.jersey");
			System.out.println("Starting grizzly...");
			// SelectorThread threadSelector;
			// threadSelector = GrizzlyWebContainerFactory.create(baseUri,
			// initParams);
			System.out
					.println(String
							.format("Jersey app started with WADL available at %sapplication.wadl\n” + “Try out %shelloworld\nHit enter to stop it...",
									baseUri, baseUri));
			System.in.read();
			// threadSelector.stopEndpoint();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}