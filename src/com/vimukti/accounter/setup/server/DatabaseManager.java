package com.vimukti.accounter.setup.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.setup.client.core.DatabaseConnection;

/**
 * It will be using for only Desktop Application.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class DatabaseManager {

	public static final String DB_FILE = "dbconfig.xml";

	private static DatabaseManager manager = null;

	public static DatabaseManager getInstance() {
		if (manager == null) {
			manager = new DatabaseManager();
		}
		return manager;
	}

	public static boolean isDBConfigured() {
		if (!ServerConfiguration.isDesktopApp()) {
			return true;
		}
		File file = new File(ServerConfiguration.getAttachmentsDir(), DB_FILE);
		return file.exists();
	}

	public void saveDBConnection(DatabaseConnection conn) throws IOException {

		String attachmentsDir = ServerConfiguration.getAttachmentsDir();
		File dbConfig = new File(attachmentsDir, DB_FILE);
		if (!dbConfig.exists()) {
			dbConfig.createNewFile();
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(dbConfig));

		DbConfig config = new DbConfig();
		config.type = getDbNameByType(conn.getDbType());
		config.url = getURL(conn);
		config.driver = getDriver(conn.getDbType());
		config.dialect = getDialect(conn.getDbType());
		config.username = conn.getUsername();
		config.password = conn.getPassword();
		config.schema = conn.getSchema();
		config.poolsize = 15;
		XStream xStream = getXStream();
		String xml = xStream.toXML(config);

		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.newLine();
		out.write(xml);
		out.close();
	}

	public void loadDbConfig() throws FileNotFoundException {
		String attachmentsDir = ServerConfiguration.getAttachmentsDir();
		File dbConfig = new File(attachmentsDir, "dbconfig.xml");
		if (!dbConfig.exists()) {
			return;
		}
		BufferedReader reader = new BufferedReader(new FileReader(dbConfig));
		XStream xStream = getXStream();
		DbConfig config = (DbConfig) xStream.fromXML(reader);

		System.setProperty("db.driver", config.driver);
		System.setProperty("db.url", config.url);
		System.setProperty("db.user", config.username);
		System.setProperty("db.pass", config.password);
		System.setProperty("dialect", config.dialect);

	}

	private XStream getXStream() {
		XStream xStream = new XStream(new DomDriver());
		xStream.alias("accounter-database-config", DbConfig.class);
		xStream.aliasField("database-type", DbConfig.class, "type");
		xStream.aliasField("schema-name", DbConfig.class, "schema");
		xStream.aliasField("driver-class", DbConfig.class, "driver");
		xStream.aliasField("pool-size", DbConfig.class, "poolize");
		return xStream;
	}

	private String getDbNameByType(int dbType) {
		switch (dbType) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "postgresql";
		case DatabaseConnection.DB_TYPE_MYSQL:
			return "mysql";
		case DatabaseConnection.DB_TYPE_H2:
			return "h2";
		}
		return "";
	}

	public String getDriver(int dbType) {
		switch (dbType) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "org.postgresql.Driver";
		case DatabaseConnection.DB_TYPE_MYSQL:
			return "com.mysql.jdbc.Driver";
		}
		return "";
	}

	public String getURL(DatabaseConnection conn) {
		switch (conn.getDbType()) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "jdbc:postgresql://" + conn.getHostname() + ":"
					+ conn.getPort() + "/" + conn.getDbName();
		case DatabaseConnection.DB_TYPE_MYSQL:
			return "jdbc:mysql://" + conn.getHostname() + ":" + conn.getPort()
					+ "/" + conn.getDbName();
		}
		return "";
	}

	public String getURLWithoutDB(DatabaseConnection conn) {
		switch (conn.getDbType()) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "jdbc:postgresql://" + conn.getHostname() + ":"
					+ conn.getPort();
		case DatabaseConnection.DB_TYPE_MYSQL:
			return "jdbc:mysql://" + conn.getHostname() + ":" + conn.getPort();
		}
		return "";
	}

	public String getDialect(int dbType) {
		switch (dbType) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "org.hibernate.dialect.AccounterPgSQLDialect";
		case DatabaseConnection.DB_TYPE_MYSQL:
			return "org.hibernate.dialect.AccounterMySQLDialect";
		case DatabaseConnection.DB_TYPE_H2:
			return "org.hibernate.dialect.H2Dialect";
		}
		return "";
	}

	class DbConfig {
		String type;
		String url;
		String driver;
		String dialect;
		String username;
		String password;
		String schema;
		int poolsize;
	}

}
