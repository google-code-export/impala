

import java.io.File;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class StartJetty {
	
	public static void main(String[] args) {

		if (args.length < 3) {
			System.out.println("Insufficient arguments supplied");
			usage();
			System.exit(1);
		}

		try {

			int port = getPort(args[0]);
			File configLocation = getContextLocation(args[1]);
			String contextPath = getContextPath(args[2]);

			Server server = new Server();

			Connector connector = new SelectChannelConnector();
			connector.setPort(port);
			server.setConnectors(new Connector[] { connector });

			HandlerCollection handlers = new HandlerCollection();
			ContextHandlerCollection contexts = new ContextHandlerCollection();

			WebAppContext webAppContext = new WebAppContext(configLocation.getAbsolutePath(), contextPath);
			contexts.addHandler(webAppContext);

			RequestLogHandler requestLogHandler = new RequestLogHandler();
			handlers.setHandlers(new Handler[] { contexts, new DefaultHandler(), requestLogHandler });
			server.setHandler(handlers);

			server.setStopAtShutdown(true);
			server.setSendServerVersion(true);

			try {
				server.start();
				server.join();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			usage();
			System.exit(-1);
		}
	}

	static String getContextPath(String arg) {
		if (!arg.startsWith("/")) {
			arg = "/" + arg;
		}
		return arg;
	}

	static File getContextLocation(String arg) {
		File file = new File(arg);
		if (!file.exists()) {
			throw new IllegalArgumentException("Invalid context directory: " + file.getAbsolutePath());
		}
		return file;
	}

	static int getPort(String arg) {
		int port = 8080;
		try {
			port = Integer.parseInt(arg);
		}
		catch (NumberFormatException e1) {
			throw new IllegalArgumentException("Invalid port: " + arg);
		}
		return port;
	}

	private static void usage() {
		System.out.println(StartJetty.class.getName() + " [port] [context directory] [path]");
	}
}
