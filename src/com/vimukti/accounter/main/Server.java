package com.vimukti.accounter.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mortbay.jetty.SessionIdManager;

public abstract class Server implements Runnable {

	public static final int PROTOCOL_VERSION = 2;
	private static Logger LOG = Logger.getLogger(Server.class);
	protected static Server server;

	private String serverID;

	public static final String KEY_STORE_PASSWORD = "***REMOVED***";
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	public static final String KEY_STORE_ALIAS = "bizantra";

	// put for local server only 3
	protected static final int PROCESS_COUNT = 20;

	public static final int SERVER_STATUS_NOT_CONNECTED = 0;

	public static final int SERVER_STATUS_CONNECTING = 1;

	public static final int SERVER_STATUS_CONNECTED_SYNCRONIZING = 2;

	public static final int SERVER_STATUS_CONNECTED_ACTIVE = 3;

	public static final int SERVER_STATUS_SUSPENDED = 4;

	public static final int SERVER_STATUS_NEEDUPDATE = 5;

	public static final int SERVER_STATUS_EXPIRED = 6;
	public static final int EXCEEDED_USAGE_LIMIT = 7;
	public static final int SERVER_STATUS_DELETED_ACCOUNT = 8;
	private static final long WAIT_TIME_FOR_PROCESS = 60 * 1000;
	public static final String LOCAL_DATABASE = "accounter";
	public static final long TOTALSIZE = 1 * 1024 * 1024 * 1024;
	public static final int SERVER_STATUS_ACCOUNT_LOCKED = 9;

	public static ThreadLocal<Boolean> threadLocal = new ThreadLocal<Boolean>();

	private Map<String, HashSet<String>> identitySessions = new HashMap<String, HashSet<String>>();

	public Server() {
		// server = this;
	}

	public Server(boolean create) throws Exception {
		// server = this;
		// initKeyStore();
	}

	/**
	 * This thread keeps monitoring the queue and increases or decreases Packet
	 * Processor threads on demand
	 * 
	 * But for default implementation it will just start some 5 threads
	 */
	@Override
	public void run() {

		try {

			// starting reminder thread
			afterStartBasicConfigurations();

		} catch (Exception e1) {
			e1.printStackTrace();
			LOG.error("Exception", e1);
		}
	}

	protected void afterStartBasicConfigurations() {

	}

	/**
	 * Returns the ID of the server
	 * 
	 * @return
	 */
	public String getServerID() {
		return this.serverID;
	}

	private static String getContainerName(String companyName) {
		// Changed by Rajesh. We have decided to upload all files into single
		// container so that we can share them among servers
		return ServerConfiguration.getRsContainerName();
		// + "-" + companyName;
	}

	public static Server getInstance() {
		try {
			return server;
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return null;
	}

	public boolean isUKVersion() {
		return true;
	}

	public abstract void startServer();

	public void onTerminate() {

	}

	public void addSeesionIdOfIdentity(String collaberId, String sessionId) {
		HashSet<String> sessionIds;
		if (identitySessions.containsKey(collaberId)) {
			sessionIds = identitySessions.get(collaberId);
		} else {
			sessionIds = new HashSet<String>();
		}
		sessionIds.add(sessionId);
		identitySessions.put(collaberId, sessionIds);
		LOG.info(collaberId + "sessions count"
				+ identitySessions.get(collaberId).size());
	}

	/**
	 * this method invalidate the all sessions of the identity
	 * 
	 * @param collaberId
	 */
	public void invalidateAllSessionIds(String collaberId) {
		SessionIdManager sessionIdManager = JettyServer.jettyServer
				.getSessionIdManager();
		if (identitySessions.containsKey(collaberId)) {
			for (String sessionId : identitySessions.get(collaberId)) {
				sessionIdManager.invalidateAll(sessionId);
			}
			identitySessions.remove(collaberId);
		} else {
			return;
		}

	}

}