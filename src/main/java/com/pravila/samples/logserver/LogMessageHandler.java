package com.pravila.samples.logserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.pravila.samples.logserver.config.PersistenceJPAConfig;
import com.pravila.samples.logserver.persistence.LogMessage;
import com.pravila.samples.logserver.persistence.LogMessageService;

/**
 * 
 * @author Elisaveta Manasieva
 * @version 1.0.0
 * class which handles logging requests from client 	
 *
 */
@Path("/json/log")
public class LogMessageHandler {

	private static Logger logger = Logger.getLogger(LogMessageHandler.class);
	private static StringWriter stack = new StringWriter();

	@Autowired
	private static LogMessageService logMessageService;
	ApplicationContext ctx;

	public LogMessageHandler() {
		 ctx = new AnnotationConfigApplicationContext(
				PersistenceJPAConfig.class);
		logMessageService = ctx.getBean(LogMessageService.class);
		executorService = new ThreadPoolExecutor(0, THREAD_POOL_SIZE, 60L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	}

	public class RequestRunner implements Runnable {
		public String request = null;

		public RequestRunner(String request) {
			this.request = request;
		}

		public void run() {
			ObjectMapper mapper = new ObjectMapper();
			LogItem item;
			try {
				item = mapper.readValue(request, LogItem.class);
				LogMessage message = new LogMessage();
				message.setClassName(item.getClassName());
				message.setDate(item.getDate());
				message.setLevel(item.getLevel());
				message.setMessage(item.getMessage());

				logMessageService.save(message);
			} catch (JsonParseException e) {
				e.printStackTrace(new PrintWriter(stack));
				logger.error("Caught JsonParseException exception "
						+ stack.toString());
			} catch (JsonMappingException e) {
				e.printStackTrace(new PrintWriter(stack));
				logger.error("Caught JsonMappingException exception "
						+ stack.toString());
			} catch (IOException e) {
				e.printStackTrace(new PrintWriter(stack));
				logger.error("Caught IOException exception " + stack.toString());
			}
		}
	}

	private ExecutorService executorService;
	private final int THREAD_POOL_SIZE = 100;

	@POST
	@Path("/post")
	@Consumes("application/json")
	@Produces("application/json")
	@Transactional
	public Response getLogMessage(String jsonString) {
		RequestRunner requestRunner = new RequestRunner(jsonString);
		executorService.execute(requestRunner);
		return Response.status(201).entity(jsonString).build();

	}

}
