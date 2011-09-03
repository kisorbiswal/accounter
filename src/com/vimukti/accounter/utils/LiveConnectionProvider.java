package com.vimukti.accounter.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;

public class LiveConnectionProvider extends ConnectionProvider {

	class ConnectionEvent {

		private static final long IDLE_CONNECTION_TIMEOUT = 60 * 1000;
		String database;
		Connection con;
		long lastUsedAt;
		int noOfTimeUsed;
		boolean inUse;

		public boolean isAlive() {
			return lastUsedAt > (System.currentTimeMillis() - IDLE_CONNECTION_TIMEOUT);
		}
	}

	private static final int MAX_CONNECTIONS = 20;

	private static final String LOCAL_DATABASE = "accounter";

	private Set<ConnectionEvent> allocations = new HashSet<ConnectionEvent>();

//	private static Logger LOG = Logger.getLogger(LiveConnectionProvider.class);
	private String user;
	/**
	 * We maintain password in static field because 1) In Server mode it will be
	 * static always. 2) In Desktop Mode it should be changed per each user
	 * logged in.
	 */
	private String pass;
	private String url;
	private String driver;

	public void close() throws HibernateException {

	}

	public void closeConnection(Connection conn) throws SQLException {
//		LOG.info("Closing Connection: " + conn.hashCode());
		releaseConnection(conn);
	}

	private synchronized void releaseConnection(Connection conn)
			throws SQLException {
//		LOG.info("Releasing Connection: " + conn.hashCode());
		ConnectionEvent event = getEvent(conn);
		if (event == null) {
			return;
		}
		event.inUse = false;
		checkOnCloseConnectionsIfNeeded();
	}

	private void checkOnCloseConnectionsIfNeeded() throws SQLException {
		if (!(allocations.size() > MAX_CONNECTIONS)) {
			return;
		}
		ConnectionEvent lastUsed = null;
		for (ConnectionEvent event : allocations) {
			if (event.inUse) {
//				LOG.info(event.hashCode() + " In useing");
				continue;
			} else {
				if (lastUsed == null) {
					lastUsed = event;
				} else {
					if (lastUsed.lastUsedAt > event.lastUsedAt) {
						lastUsed = event;
					}
				}
			}
		}
		if (lastUsed != null) {
//			LOG.info(lastUsed.con.hashCode() + " In removing");
			lastUsed.con.close();
			allocations.remove(lastUsed);
		}
	}

	private ConnectionEvent getEvent(Connection conn) {
		for (ConnectionEvent event : allocations) {
			if (event.con == conn) {
				return event;
			}
		}
		return null;
	}

	public void configure(Properties props) throws HibernateException {
		user = props.getProperty(Environment.USER);
		pass = props.getProperty(Environment.PASS);
		url = props.getProperty(Environment.URL);
		driver = props.getProperty(Environment.DRIVER);
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public synchronized Connection getConnection() throws SQLException {

		ConnectionEvent event = getUnusedEvent();
		if (event == null) {
			event = createEvent();
//			LOG.info(event.con.hashCode() + " Created Connection");
//			System.out.println("New Connection: " + event.con.hashCode());
		}

		return event.con;
	}

	private ConnectionEvent createEvent() throws SQLException {
		ConnectionEvent event = new ConnectionEvent();
		event.database = getDBName();
		event.inUse = true;
		event.lastUsedAt = System.currentTimeMillis();
		String url = getUrl(event.database, "localhost");
		event.con = DriverManager.getConnection(url, user, pass);
		event.con.setAutoCommit(false);
		event.con
				.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		allocations.add(event);
		return event;
	}

	private ConnectionEvent getUnusedEvent() {
		String name = getDBName();
		ConnectionEvent freeEvent = null;
		for (ConnectionEvent event : allocations) {
			if (event.inUse || !event.database.equals(name)) {
				continue;
			} else {
				freeEvent = event;
				break;
			}
		}
		if (freeEvent != null) {
			if (!freeEvent.isAlive()) {
				try {
					freeEvent.con.close();
				} catch (SQLException e) {
				}
				allocations.remove(freeEvent);
				return getUnusedEvent();
			} else {
				freeEvent.lastUsedAt = System.currentTimeMillis();
			}
		}
		return freeEvent;
	}

	public boolean supportsAggressiveRelease() {
		return false;
	}

	/**
	 * This method replace existing dbname with companydbname in url. Check if
	 * localserver no need to do anything,just return same url
	 * 
	 * @param name
	 * @param ipAddress
	 * @return
	 */
	String getUrl(String name, String ipAddress) {
		String url = this.url.replaceAll(LOCAL_DATABASE, name);
		url = url.replace("localhost", ipAddress);
		return url;
	}

}
