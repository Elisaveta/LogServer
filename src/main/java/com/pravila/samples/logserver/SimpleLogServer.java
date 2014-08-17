package com.pravila.samples.logserver;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.Lifecycle;

import com.pravila.samples.logserver.persistence.model.LogMessage;
import com.sun.jersey.simple.container.SimpleServerFactory;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/json/log")
public class SimpleLogServer implements Lifecycle {

	@SuppressWarnings("unused")
	private static volatile Closeable endPoint;
	/** SmsService URI. */
	@SuppressWarnings("unused")
	private String uri;
	/** Random Number Generator. */
	@SuppressWarnings("unused")
	private static Random randomGenerator;
	@SuppressWarnings("unused")
	private static boolean running = false;
	
	public SimpleLogServer() {
	}
	
	
	@Override
	public void start() {
		System.out.println("Starting logServer...");
		try {
			SimpleLogServer.endPoint = SimpleServerFactory
					.create("http://127.0.0.1:8001");
			SimpleLogServer.running = true;
			SimpleLogServer.randomGenerator = new Random();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@POST
	@Path("/post")
	@Consumes("application/json")
	@Produces("application/json")
	public javax.ws.rs.core.Response getLogMessage(String jsonString) {
		System.out.println(" dojde request");
		System.out.println(jsonString);
		ObjectMapper mapper = new ObjectMapper();
		LogItem item;
		try {
			item = mapper.readValue(jsonString, LogItem.class);
			LogMessage message = new LogMessage();
			message.setClassName(item.getClassName());
			message.setDate(item.getDate());
			message.setLevel(item.getLevel());
			message.setMessage(item.getMessage());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return javax.ws.rs.core.Response.status(201).build();

	}

}
