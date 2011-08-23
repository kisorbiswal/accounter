package com.vimukti.accounter.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerConfiguration {

	public static final String PRESENT_CODE_VERSION = "0.9.1.21";
	private static boolean uploadToS3;
	private static String awsSecretKey;
	private static String awsKeyID;
	private static String s3BucketName;
	private static String attachmentsDir;
	private static boolean enforceHTTPS;
	private static String serverURL;
	private static String link;

	private static String serverDomainName;
	private static String helperDomain;
	private static String financeDir;
	// an alternate domain that is required by the client
	private static int websServerPort;

	public static int getPort() {
		return websServerPort;
	}

	public static String getProtocol() {
		return protocol;
	}

	// protocol for the web client http/https
	private static String protocol;

	// private static int mobilePort;
	private static String logsDir;
	private static int webClientPort;
	private static String adminID;
	private static String adminpassword;
	private static int maxUser;
	private static boolean allowSignUp;
	private static String tmpDir;

	public static String getTmpDir() {
		return tmpDir;
	}

	private static String emailAddress;

	private static String mailServerHost;

	private static String emailPassword;

	private static String emailPortNo;

	private static boolean isLoginHTTPS;
	private static String mainServer;
	private static int mainServerPort;
	private static String helpUrl;
	private static String homeDir;
	private static boolean isLocal;
	private static boolean uploadToRackspace;
	/**
	 * Rackspace container Name
	 */
	private static String rsContainerName;

	public static boolean isDebugMode;
	private static boolean isUnderMaintanance;

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

	public static String getServerDomainName() {
		return serverDomainName;
	}

	static void init() {
		// A port at which the web server will be listening
		websServerPort = 8080;

		// Logs folder for Desktop Client
		StringBuffer sb = new StringBuffer();
		sb.append(getHome());
		sb.append(File.separatorChar);
		sb.append("logs");
		logsDir = sb.toString();

		new File(logsDir).mkdirs();

		System.setProperty("logsDir", logsDir);
		serverDomainName = "127.0.0.1";

		setMainServerPort(8890);

		helpUrl = "http://www.bizantrahelp.vimukti.com/";
		helperDomain = "";

		isLocal = true;

		tmpDir = System.getProperty("java.io.tmpdir", "");
		File file = new File(tmpDir, "BizantraLocal");
		file.mkdirs();
		tmpDir = file.getAbsolutePath();

		System.setProperty("db.driver", "org.h2.Driver");
		System.setProperty("dialect",
				"com.bizantra.server.storage.internal.CollaberH2Dialect");

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
			serverDomainName = prop.getProperty("serverDomainName", "");
			setMainServer(prop.getProperty("mainServer", ""));

			protocol = prop.getProperty("protocol", "http");
			// A port at which the web server will be listening
			websServerPort = Integer.parseInt(prop.getProperty(
					"websServerPort", "8080"));

			if (!protocol.matches("^(http|https)")) {

				System.err
						.println("Invalid configuration for Protocal  valid args are http (or) https");
				System.exit(0);
			}

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
			helpUrl = prop.getProperty("helpurl", "");
			mailServerHost = prop.getProperty("mailServerHost", "");
			emailPortNo = prop.getProperty("emailPortNo", "");
			// For helper domains.
			helperDomain = prop.getProperty("helperDomain", "");
			try {
				isLoginHTTPS = Boolean.parseBoolean(prop.getProperty(
						"isLoginHTTPS", ""));
			} catch (Exception e) {
				isLoginHTTPS = false;
			}

			try {
				enforceHTTPS = Boolean.parseBoolean(prop.getProperty(
						"enforceHTTPS", ""));
			} catch (Exception e) {
				enforceHTTPS = false;
			}

			allowSignUp = prop.getProperty("allowSignUp", "false").equals(
					"true");

			if (serverDomainName.length() < 5) {
				System.err
						.println("Invalid confiuration: serverDomainName is invalid");
				System.exit(0);
			}

			uploadToS3 = prop.getProperty("uploadToS3", "false").equals("true");
			uploadToRackspace = prop.getProperty("uploadToRackspace", "false")
					.equals("true");
			isLocal = prop.getProperty("isLocal", "false").equals("true");
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
				if (!isLocal() && !new File(attachmentsDir).exists()) {
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

			tmpDir = prop.getProperty("tmpDir",
					System.getProperty("java.io.tmpdir", ""));

			setServerURL(prop.getProperty("serverURL", null));
			setLink(prop.getProperty("link", null));

		} catch (NumberFormatException ne) {
			System.err
					.println("Invalid configuration for some numeric options");
			System.exit(0);
		}

	}

	public static boolean isLocal() {
		return isLocal;
	}

	public static boolean isAllowSignUp() {
		return allowSignUp;
	}

	public static int getMaxUser() {
		return maxUser;
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

	public static String getHelpUrl() {
		return helpUrl;
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

	/**
	 * For getting the https value.
	 * 
	 * @return boolean
	 */
	public static boolean enforceHTTS() {
		return enforceHTTPS;
	}

	// public static boolean isLiveServer() {
	// return isLiveServer;
	// }

	public static boolean isLoginHTTPS() {
		return isLoginHTTPS;
	}

	/**
	 * returns the JS files domain.
	 * 
	 * @return {@link String}
	 */
	public static String getHelperDomain() {
		return helperDomain;
	}

	public static void setMainServer(String mainServer) {
		ServerConfiguration.mainServer = mainServer;
	}

	public static String getMainServer() {
		return mainServer;
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

	public static void setLink(String link) {
		ServerConfiguration.link = link;
	}

	public static String getLink() {
		return link;
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
}
