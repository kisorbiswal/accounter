package com.vimukti.accounter.main;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.SessionManager;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.SessionHandler;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.Resource;

public class JettyServer {
	public static Server jettyServer;

	public static void start(int port) throws Exception {
		jettyServer = new Server();
		Connector connector = new SelectChannelConnector();
		if (ServerConfiguration.isLocal()) {
			connector.setHost("127.0.0.1");
		}
		connector.setPort(port);

		SessionHandler sessionHandler = new SessionHandler();

		WebAppContext webappcontext = new WebAppContext(null, sessionHandler,
				null, null);

		webappcontext.setContextPath("/");
		// webappcontext.setWar("webapp");
		Resource resource = Resource.newClassPathResource("war");

		webappcontext.setBaseResource(resource);

		SessionManager sessionManager = sessionHandler.getSessionManager();
		String maindomain = ServerConfiguration.getServerDomainName();
		if (maindomain != null && maindomain.startsWith("www")) {
			maindomain = maindomain.replaceAll("www\\.", "");
			sessionManager.setSessionDomain("." + maindomain);
		}
		sessionManager.setSessionPath("/");

		jettyServer.setConnectors(new Connector[] { connector });
		webappcontext.setClassLoader(JettyServer.class.getClassLoader());

		webappcontext.setAttribute("documentDomain",
				ServerConfiguration.getServerDomainName());



		// for max post data
		webappcontext.getServletContext().getContextHandler()
				.setMaxFormContentSize(10000000);

		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[] { webappcontext,
				new DefaultHandler() });

		jettyServer.setHandler(handlers);

		jettyServer.start();

	}

	public static void stop() {
		try {
			jettyServer.stop();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
