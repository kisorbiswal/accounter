package com.vimukti.accounter.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ServerConfiguration {

	private static String attachmentsDir;
	private static String logsDir;
	private static String adminpassword;
	private static String tmpDir;

	public static String getTmpDir() {
		return tmpDir;
	}

	private static boolean isUnderMaintanance;
	private static String sessionDbUrl;

	private static String mainServerDomain;
	private static String currentServerDomain;
	private static String chatUsername;
	private static String chatpassword;
	private static int mainServerPort;
	public static boolean isDebugMode;

	public static String getAdminPassword() {
		return adminpassword;
	}

	public static String getAttachmentsDir(String companyDBName) {

		if (companyDBName == null) {
			// CompanyDBNmae NEver BE NUll IN MAIN SERVER
			return null;
		}
		return attachmentsDir + companyDBName;
	}

	public static String getMainServerDomain() {
		return mainServerDomain;
	}

	static void init(String config) {
		PropertyParser prop = new PropertyParser();
		try {
			prop.loadFile(config == null ? "config/config.ini" : config);
			// prop.loadFile(keyFile == null ? "collaberconfig.ini" : keyFile);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find server config file: " + e);
			// LOG.error("Unable to find server config file: ", e);
		} catch (IOException e) {
			// LOG.error("Unable to find server config file: ", e);
			System.out.println("Unable to find server config file: " + e);
		}

		try {
			mainServerDomain = prop.getProperty("mainServerDomain", "");
			currentServerDomain = prop.getProperty("currentServerDomain", null);

			mainServerPort = Integer.parseInt(prop.getProperty(
					"mainServerPort", null));
			/*
			 * mobilePort = Integer.parseInt(prop.getProperty("mobilePort",
			 * "7990"));
			 */

			logsDir = prop.getProperty("logsdir", "./");
			System.setProperty("logsDir", logsDir);

			if (mainServerDomain.length() < 5) {
				System.err
						.println("Invalid confiuration: mainServerDomain is invalid");
				System.exit(0);
			}

			attachmentsDir = prop.getProperty("attachmentsDir", "");

			if (!new File(attachmentsDir).exists()) {
				System.err.println("Invalid configuration for attachment dir");
				System.exit(0);
			}
			String databaseUrl = prop.getProperty("databaseUrl", null);
			String databaseDriver = prop.getProperty("databaseDriver", null);
			String username = prop.getProperty("username", null);
			String password = prop.getProperty("password", null);
			String dialect = prop.getProperty("dialect", null);
			if (databaseUrl == null || username == null || password == null
					|| dialect == null) {
				System.err.println("Invalid configuration for database");
			}
			System.setProperty("db.driver", databaseDriver);
			System.setProperty("db.url", databaseUrl);
			System.setProperty("db.user", username);
			System.setProperty("db.pass", password);
			System.setProperty("dialect", dialect);
			adminpassword = prop.getProperty("adminpassword", "");
			tmpDir = prop.getProperty("tmpDir",
					System.getProperty("java.io.tmpdir", ""));

			sessionDbUrl = prop.getProperty("sessionDbUrl", null);
			chatUsername = prop.getProperty("chatUsername", null);
			chatpassword = prop.getProperty("chatPassword", null);

		} catch (NumberFormatException ne) {
			System.err
					.println("Invalid configuration for some numeric options");
			System.exit(0);
		}

	}

	public static String getAttachmentsDir() {
		return attachmentsDir;
	}

	public static boolean isUnderMaintainance() {
		return isUnderMaintanance;
	}

	public static void setUnderMaintainance(boolean value) {
		isUnderMaintanance = value;
	}

	/**
	 * @return
	 */
	public static String getAccountsDir() {
		return "config/accounts";
	}

	/**
	 * @return
	 */
	public static String getSessionDbUrl() {
		return sessionDbUrl;
	}

	/**
	 * @return
	 */
	public static String getServerCookieDomain() {
		String[] domainParts = getMainServerDomain().split("\\.");
		int size = domainParts.length;
		if (size < 2) {
			return "";
		}
		return '.' + domainParts[size - 2] + '.' + domainParts[size - 1];
	}

	/**
	 * @return the currentServerDomain
	 */
	public static String getCurrentServerDomain() {
		return currentServerDomain;
	}

	/**
	 * @return the chatUsername
	 */
	public static String getChatUsername() {
		return chatUsername;
	}

	/**
	 * @param chatUsername
	 *            the chatUsername to set
	 */
	public static void setChatUsername(String chatUsername) {
		ServerConfiguration.chatUsername = chatUsername;
	}

	/**
	 * @return the chatpassword
	 */
	public static String getChatpassword() {
		return chatpassword;
	}

	/**
	 * @param chatpassword
	 *            the chatpassword to set
	 */
	public static void setChatpassword(String chatpassword) {
		ServerConfiguration.chatpassword = chatpassword;
	}

	public static int getMainServerPort() {
		return mainServerPort;
	}
}
