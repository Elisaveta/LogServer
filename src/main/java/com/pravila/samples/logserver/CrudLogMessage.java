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

@Path("/json/log")
public class CrudLogMessage {
	
	private static Logger logger = Logger.getLogger(CrudLogMessage.class);
	static StringWriter stack = new StringWriter();

	@Autowired
	private static LogMessageService logMessageService;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				PersistenceJPAConfig.class);
		logMessageService = ctx.getBean(LogMessageService.class);

		SimpleLogServer server = new SimpleLogServer();
		server.start();
	}

	public String jsonRequest = null;

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

				try {
					LogMessage log = logMessageService.save(message);
					logger.info("Log message created successfully - " + log.getMessage());
				} catch (Exception e) {
					e.printStackTrace(new PrintWriter(stack));
					logger.error("Caught exception; Connection failed to server "
							+ stack.toString());
				}

			} catch (JsonParseException e) {
				e.printStackTrace(new PrintWriter(stack));
				logger.error("Caught exception; Connection failed to server "
						+ stack.toString());
			} catch (JsonMappingException e) {
				e.printStackTrace(new PrintWriter(stack));
				logger.error("Caught exception; Connection failed to server "
						+ stack.toString());
			} catch (IOException e) {
				e.printStackTrace(new PrintWriter(stack));
				logger.error("Caught exception; Connection failed to server "
						+ stack.toString());
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
		executorService = new ThreadPoolExecutor(0, THREAD_POOL_SIZE, 60L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

		RequestRunner requestRunner = new RequestRunner(jsonString);
		executorService.execute(requestRunner);
		return Response.status(202)
				.entity("Request has been received").build();

	}

}
