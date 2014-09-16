package com.pravila.samples.logserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.Lifecycle;

import com.sun.jersey.simple.container.SimpleServerFactory;

/**
 * 
 * @author Elisaveta Manasieva
 * @version 1.0.0 class which init and start log server
 * 
 */
public class SimpleLogServer implements Lifecycle {

	private static Logger logger = Logger.getLogger(SimpleLogServer.class);
	static StringWriter stack = new StringWriter();

	private static Properties configProp = new Properties();
	InputStream in = null;

	public SimpleLogServer() {
		in = SimpleLogServer.class.getClass().getResourceAsStream(
				"/config.properties");
		try {
			configProp.load(in);
			in.close();
		} catch (IOException e) {
			logger.error("Caught IOException " + stack.toString());
		}
	}

	/**
	 * method that start server to listen for requests
	 */
	@Override
	public void start() {

		try {
			logger.info("Server starting...");
			SimpleServerFactory.create(configProp.getProperty("server.host")
					+ ":" + configProp.getProperty("server.port"));
			logger.info("Server started!");
		} catch (IllegalArgumentException e) {
			logger.error("Caught IllegalArgumentException exception "
					+ stack.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Caught IOException exception " + stack.toString());
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

	/**
	 * project main class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		SimpleLogServer server = new SimpleLogServer();
		server.start();
		new LogMessageHandler();
	}

}
