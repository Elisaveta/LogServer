package com.pravila.samples.logserver;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

import org.springframework.context.Lifecycle;

import com.sun.jersey.simple.container.SimpleServerFactory;

public class SimpleLogServer implements Lifecycle {

	@SuppressWarnings("unused")
	private static volatile Closeable endPoint;
	/** URI. */
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

}
