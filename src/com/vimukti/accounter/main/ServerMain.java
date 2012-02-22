package com.vimukti.accounter.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.hibernate.Session;

import com.vimukti.accounter.core.ServerMaintanance;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.mail.EmailManager;
import com.vimukti.accounter.main.upload.AttachmentFileServer;
import com.vimukti.accounter.mobile.AccounterChatServer;
import com.vimukti.accounter.mobile.AccounterMobileException;
import com.vimukti.accounter.mobile.ConsoleChatServer;
import com.vimukti.accounter.mobile.MobileServer;
import com.vimukti.accounter.mobile.store.CommandsFactory;
import com.vimukti.accounter.mobile.store.PatternStore;
import com.vimukti.accounter.services.SubscryptionTool;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.RecurringTool;

public class ServerMain extends Main {
	private static boolean isDebug;

	public static void main(String[] args) throws Exception {

		isDebug = checkDebugMode(args);
		ServerConfiguration.isDebugMode = isDebug;
		String configFile = getArgument(args, "-config");
		ServerConfiguration.init(configFile);
		initLogger();

		Session session = HibernateUtil.openSession();
		try {
			ServerMaintanance maintanance = (ServerMaintanance) session.get(
					ServerMaintanance.class, 1L);
			if (maintanance != null) {
				ServerConfiguration.setUnderMaintainance(maintanance
						.isUnderMaintanance());
			}
			FinanceTool.createViews();

			if (ServerConfiguration.isLoadMessages()) {
				loadAccounterMessages();
			}

			loadFeatures();
		} finally {
			session.close();
		}
		AttachmentFileServer.getInstance().start();
		EmailManager.getInstance().start();
		// EMailMonitor.getInstance().start();
		Global.set(new ServerGlobal());

		// Creating Email Log listener
		createMailLogListener();

		stratChatServers();

		startRecurringTimer();

		startSubscriptionExpireTimer();

		JettyServer.start(ServerConfiguration.getMainServerPort());
		JettyServer.jettyServer.join();

	}

	private static void loadFeatures() {
		Session session = HibernateUtil.getCurrentSession();
		Subscription premium = Subscription
				.getInstance(Subscription.PREMIUM_USER);
		Set<String> premiumFeatures = new HashSet<String>();
		premiumFeatures.add("create company");
		premiumFeatures.add("brnading theme0");
		premiumFeatures.add("import bank statements");
		premiumFeatures.add("user activity");
		premiumFeatures.add("history");
		premiumFeatures.add("attachments");
		premiumFeatures.add("class");
		premiumFeatures.add("location");
		premiumFeatures.add("billable exenses");
		premiumFeatures.add("credtis and charges");
		premiumFeatures.add("merging");
		premiumFeatures.add("job costing");
		premiumFeatures.add("encryption");
		premiumFeatures.add("invite users");
		premium.setFeatures(premiumFeatures);

		Subscription before = Subscription
				.getInstance(Subscription.BEFORE_PAID_FETURE);
		session.save(premium);

		Set<String> beforeFeatures = new HashSet<String>();
		beforeFeatures.add("create company");
		beforeFeatures.add("brnading theme0");
		beforeFeatures.add("import bank statements");
		beforeFeatures.add("user activity");
		beforeFeatures.add("history");
		beforeFeatures.add("attachments");
		beforeFeatures.add("class");
		beforeFeatures.add("location");
		beforeFeatures.add("billable exenses");
		beforeFeatures.add("credtis and charges");
		beforeFeatures.add("merging");
		beforeFeatures.add("job costing");
		beforeFeatures.add("encryption");
		beforeFeatures.add("invite users");
		before.setFeatures(beforeFeatures);

		session.save(before);
	}

	private static void startSubscriptionExpireTimer() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				new SubscryptionTool().start();
			}
		}, 0, 24 * 60 * 1000);
	}

	private static void createMailLogListener() {

		String fileName = ServerConfiguration.getEmailLogger();
		if (fileName != null) {

			File file = new File(fileName);
			if (file.exists()) {
				new MailLogTailerListener(file);
			}

		}

	}

	private static void startRecurringTimer() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				new RecurringTool().start();
			}
		}, 0, 60 * 1000);
	}

	private static void loadAccounterMessages() throws IOException {
		String fileName = AccounterMessages.class.getName();
		fileName = fileName.replace('.', '/');
		fileName = fileName + ".properties";

		String defaultFileName = (AccounterMessages.class.getName().replace(
				'.', '/')) + ".properties";
		ClassLoader classLoader = AccounterMessages.class.getClassLoader();
		InputStream is = null;
		InputStreamReader reader = null;
		try {
			is = classLoader.getResourceAsStream(fileName);
			if (is == null) {
				is = classLoader.getResourceAsStream(defaultFileName);
				if (is == null) {
					throw new FileNotFoundException(
							"Could not find any properties files matching the given class.");
				}
			}
			new MessageLoader(is).loadMessages();
			System.out.println("Completed the Inseting of messages..");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not found.", e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (is != null) {
				is.close();
			}
		}

	}

	private static void stratChatServers() throws AccounterMobileException {
		boolean isEnableCommands = false;
		if (ServerConfiguration.isEnableConsoleChatServer()) {
			new ConsoleChatServer().start();
			isEnableCommands = true;
		}

		if (ServerConfiguration.isEnableIMChatServer()) {
			new AccounterChatServer().start();
			isEnableCommands = true;
		}

		if (ServerConfiguration.isEnableMobileChatServer()) {
			new MobileServer().strat();
			isEnableCommands = true;
		}

		if (isEnableCommands) {
			CommandsFactory.INSTANCE.reload();
			PatternStore.INSTANCE.reload();
		}
	}

	private static void initLogger() {
		File logsdir = new File("logs");
		if (!logsdir.exists()) {
			logsdir.mkdirs();
		}
		String path = new File(logsdir, "serverlog").getAbsolutePath();
		try {
			if (isDebug) {
				BasicConfigurator.configure(new ConsoleAppender());
			} else {
				Layout layout = new PatternLayout("%d %x %C{1}.%M- %m%n");
				Logger.getRootLogger().addAppender(
						new DailyRollingFileAppender(layout, path,
								"'.'yyyy-MM-dd-a"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getLogger("org.hibernate").setLevel(Level.INFO);
		Logger.getLogger("com.vimukti").setLevel(Level.INFO);

	}
}