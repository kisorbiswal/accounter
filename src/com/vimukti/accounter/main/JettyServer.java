package com.vimukti.accounter.main;

import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.JDBCSessionIdManager;
import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {
	public static Server jettyServer;

	public static void start(int port) throws Exception {
		jettyServer = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(port);

		SessionHandler sessionHandler = new SessionHandler();

		SecurityHandler securityHandler = new ConstraintSecurityHandler();

		WebAppContext webappcontext = new WebAppContext(sessionHandler,
				securityHandler, null, null);

		webappcontext.setContextPath("/");
		LocalConnector wsConnector=new LocalConnector();
		webappcontext.setAttribute("wsConnector", wsConnector);
		// webappcontext.setWar("webapp");

		webappcontext.getSessionHandler().getSessionManager()
				.setSessionIdPathParameterName("none");

		Resource resource = Resource.newClassPathResource("war");

		webappcontext.setBaseResource(resource);

		jettyServer.setConnectors(new Connector[] { connector,wsConnector });
		webappcontext.setClassLoader(JettyServer.class.getClassLoader());

		webappcontext.setAttribute("documentDomain",
				ServerConfiguration.getMainServerDomain());

		// for max post data
		webappcontext.getServletContext().getContextHandler()
				.setMaxFormContentSize(10000000);

		// HandlerCollection handlers = new HandlerCollection();
		// handlers.setHandlers(new Handler[] { webappcontext,
		// new DefaultHandler() });
		jettyServer.setHandler(webappcontext);

		if (ServerConfiguration.getSessionDbUrl() != null) {
			setJDBCSessionManager(webappcontext.getSessionHandler());
		}

		jettyServer.start();

	}

	/**
	 * @param sessionHandler
	 */
	private static void setJDBCSessionManager(SessionHandler sessionHandler) {

		// ----------JDBCSessionIdManager-----------
		JDBCSessionIdManager idMgr = new JDBCSessionIdManager(jettyServer);
		String hostName = ServerConfiguration.getCurrentServerDomain();
		idMgr.setWorkerName(hostName.replaceAll("\\.", "_"));
		idMgr.setDriverInfo(System.getProperty("db.driver"),
				ServerConfiguration.getSessionDbUrl());
		idMgr.setScavengeInterval(60);
		jettyServer.setSessionIdManager(idMgr);

		// -----------JDBCSessionManager-------------
		JDBCSessionManager jdbcMgr = new JDBCSessionManager();
		AbstractSessionManager sessionManager = (AbstractSessionManager) sessionHandler
				.getSessionManager();

		sessionManager.getSessionCookieConfig().setDomain(
				ServerConfiguration.getServerCookieDomain());

		jdbcMgr.setSessionTrackingModes(sessionManager
				.getDefaultSessionTrackingModes());

		jdbcMgr.setHttpOnly(sessionManager.getHttpOnly());
		jdbcMgr.setCheckingRemoteSessionIdEncoding(sessionManager
				.isCheckingRemoteSessionIdEncoding());
		jdbcMgr.setMaxInactiveInterval(sessionManager.getMaxInactiveInterval());
		jdbcMgr.setNodeIdInSessionId(sessionManager.isNodeIdInSessionId());
		jdbcMgr.setRefreshCookieAge(sessionManager.getRefreshCookieAge());
		// jdbcMgr.setSaveInterval(sessionHandler.getS)
		jdbcMgr.getSessionCookieConfig().setSecure(
				sessionManager.getSessionCookieConfig().isSecure());
		jdbcMgr.getSessionCookieConfig().setComment(
				sessionManager.getSessionCookieConfig().getComment());
		jdbcMgr.getSessionCookieConfig().setDomain(
				sessionManager.getSessionCookieConfig().getDomain());
		jdbcMgr.getSessionCookieConfig().setMaxAge(
				sessionManager.getSessionCookieConfig().getMaxAge());
		jdbcMgr.getSessionCookieConfig().setName(
				sessionManager.getSessionCookieConfig().getName());
		jdbcMgr.getSessionCookieConfig().setPath(
				sessionManager.getSessionCookieConfig().getPath());

		jdbcMgr.setSessionIdManager(jettyServer.getSessionIdManager());

		sessionHandler.setSessionManager(jdbcMgr);

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
