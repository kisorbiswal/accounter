package com.vimukti.accounter.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerConfiguration {

	private static boolean uploadToS3;
	private static String awsSecretKey;
	private static String awsKeyID;
	private static String s3BucketName;
	private static String attachmentsDir;
	private static String serverURL;

	private static String financeDir;

	// private static int mobilePort;
	private static String logsDir;
	private static int webClientPort;
	private static String adminID;
	private static String adminpassword;
	private static String tmpDir;

	public static String getTmpDir() {
		return tmpDir;
	}

	private static String emailAddress;

	private static String mailServerHost;

	private static String emailPassword;

	private static String emailPortNo;

	private static int mainServerPort;
	private static String homeDir;
	private static boolean uploadToRackspace;
	/**
	 * Rackspace container Name
	 */
	private static String rsContainerName;

	public static boolean isDebugMode;
	private static boolean isUnderMaintanance;
	private static String mainServerDbUrl;

	private static String mainServerDomain;
	private static String currentServerDomain;

	public static String getRsContainerName() {
		return rsContainerName;
	}

	public static String getAdminPassword() {
		return adminpassword;
	}

	public static String getAdminID() {
		return adminID;
	}

	public static String getFromAddress() {
		return emailAddress;
	}

	public static String getAttachmentsDir(String companyDBName) {

		if (companyDBName == null) {
			// CompanyDBNmae NEver BE NUll IN MAIN SERVER
			return null;
		}
		return attachmentsDir + companyDBName;
	}

	public static String getAWSKeyID() {
		return awsKeyID;
	}

	public static String getAWSSecretKey() {
		return awsSecretKey;
	}

	public static InetSocketAddress getS3Address() {
		return new InetSocketAddress(getS3BucketName() + ".s3.amazonaws.com",
				80);
	}

	public static String getS3BucketName() {
		return s3BucketName;
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

			/*
			 * mobilePort = Integer.parseInt(prop.getProperty("mobilePort",
			 * "7990"));
			 */
			webClientPort = Integer.parseInt(prop.getProperty("webClientPort",
					"7590"));
			setMainServerPort(Integer.parseInt(prop
					.getProperty("adminPort", "")));

			logsDir = prop.getProperty("logsdir", "./");
			System.setProperty("logsDir", logsDir);
			emailAddress = prop.getProperty("emailAddress", "");
			emailPassword = prop.getProperty("emailPassword", "");
			mailServerHost = prop.getProperty("mailServerHost", "");
			emailPortNo = prop.getProperty("emailPortNo", "");

			if (mainServerDomain.length() < 5) {
				System.err
						.println("Invalid confiuration: mainServerDomain is invalid");
				System.exit(0);
			}

			uploadToS3 = prop.getProperty("uploadToS3", "false").equals("true");
			uploadToRackspace = prop.getProperty("uploadToRackspace", "false")
					.equals("true");
			awsSecretKey = prop.getProperty("awsSecretKey", "");
			awsKeyID = prop.getProperty("awsKeyID", "");
			s3BucketName = prop.getProperty("s3BucketName", "");

			rsContainerName = prop.getProperty("containerName", "bizantra");

			financeDir = prop.getProperty("FinanceDir", "");

			attachmentsDir = prop.getProperty("attachmentsDir", "");

			if (uploadToS3) {
				if (awsKeyID.length() != 20 || awsSecretKey.length() != 40
						|| s3BucketName.length() == 0) {
					System.err.println("Invalid confiuration for S3 options");
					System.exit(0);
				}
			} else {
				if (!new File(attachmentsDir).exists()) {
					System.err
							.println("Invalid configuration for attachment dir");
					System.exit(0);
				}
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
			adminID = prop.getProperty("adminId", "");
			tmpDir = prop.getProperty("tmpDir",
					System.getProperty("java.io.tmpdir", ""));

			setServerURL(prop.getProperty("serverURL", null));
			mainServerDbUrl = prop.getProperty("mainServerDatabaseUrl", null);

		} catch (NumberFormatException ne) {
			System.err
					.println("Invalid configuration for some numeric options");
			System.exit(0);
		}

	}

	public static boolean uploadToS3() {
		return uploadToS3;
	}

	public static boolean uploadToRackSpace() {
		return uploadToRackspace;
	}

	public static int getWebClientPort() {
		return webClientPort;
	}

	public static String getMailHost() {
		return mailServerHost;
	}

	public static String getMailPassword() {
		return emailPassword;
	}

	public static String getMailPort() {
		return emailPortNo;
	}

	/**
	 * Method checks whether the mail is configured or not. If mail configured
	 * then it returns true otherwise it returns false.
	 * 
	 * @return boolean
	 */
	public static boolean isMailConfigured() {
		if (getMailPort().equals("") || getMailHost().equals("")
				|| getMailPassword().equals("") || getFromAddress().equals("")) {
			return false;
		}
		return true;
	}

	public static void setMainServerPort(int mainServerPort) {
		ServerConfiguration.mainServerPort = mainServerPort;
	}

	public static int getMainServerPort() {
		return mainServerPort;
	}

	public static String getHome() {
		if (homeDir == null) {
			StringBuilder sb = new StringBuilder(
					System.getProperty("user.home"));
			sb.append(File.separator);
			sb.append(".bizantra");
			return sb.toString();
		}
		return homeDir;
	}

	public static void setHome(String string) {
		homeDir = string;
	}

	public static void setServerURL(String serverURL) {
		ServerConfiguration.serverURL = serverURL;
	}

	public static String getServerURL() {
		return serverURL;
	}

	public static void setFinanceDir(String financeDir) {
		ServerConfiguration.financeDir = financeDir;
	}

	public static String getFinanceDir() {
		return financeDir;
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
	public static String getMainServerDbUrl() {
		return mainServerDbUrl;
	}

	/**
	 * @return
	 */
	public static String getServerCookieDomain() {
		String[] domainParts = getMainServerDomain().split("\\.");
		int size = domainParts.length;
		if (size < 2) {
			return '.' + getMainServerDomain();
		}
		return '.' + domainParts[size - 2] + '.' + domainParts[size - 1];
	}

	/**
	 * @return the currentServerDomain
	 */
	public static String getCurrentServerDomain() {
		return currentServerDomain;
	}
}
