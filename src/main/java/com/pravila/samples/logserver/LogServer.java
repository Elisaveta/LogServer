package com.pravila.samples.logserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.pravila.samples.logserver.config.PersistenceJPAConfig;
import com.sun.jersey.api.core.PackagesResourceConfig;

/**
 * Hello world!
 * 
 */
public class LogServer extends PackagesResourceConfig {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.println("Hello World!");
//		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ApplicationContext context = new AnnotationConfigApplicationContext(PersistenceJPAConfig.class);
		SimpleLogServer  server = (SimpleLogServer) context.getBean("simpleLogServer");
		server.start();
	}
}
