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

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
import com.vimukti.accounter.web.client.core.Features;
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

			loadSubscriptionFeatures();
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

	private static void loadSubscriptionFeatures() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction beginTransaction = session.beginTransaction();
		Subscription instance = Subscription
				.getInstance(Subscription.BEFORE_PAID_FETURE);
		if (instance == null) {
			instance = new Subscription();
			instance.setType(1);
			session.save(instance);
		}
		instance = Subscription.getInstance(Subscription.FREE_CLIENT);
		if (instance == null) {
			instance = new Subscription();
			instance.setType(2);
			session.save(instance);
		}
		instance = Subscription.getInstance(Subscription.PREMIUM_USER);
		if (instance == null) {
			instance = new Subscription();
			instance.setType(3);
			session.save(instance);
		}
		beginTransaction.commit();
		beginTransaction = session.beginTransaction();
		Subscription premium = Subscription
				.getInstance(Subscription.PREMIUM_USER);
		Set<String> premiumFeatures = new HashSet<String>();
		premiumFeatures.add(Features.CRESTE_COMPANY);
		premiumFeatures.add(Features.BRANDING_THEME);
		premiumFeatures.add(Features.IMPORT_BANK_STATEMENTS);
		premiumFeatures.add(Features.USER_ACTIVITY);
		premiumFeatures.add(Features.HISTORY);
		premiumFeatures.add(Features.ATTACHMENTS);
		premiumFeatures.add(Features.CLASS);
		premiumFeatures.add(Features.LOCATION);
		premiumFeatures.add(Features.BILLABLE_EXPENSE);
		premiumFeatures.add(Features.CREDITS_CHARGES);
		premiumFeatures.add(Features.MERGING);
		premiumFeatures.add(Features.JOB_COSTING);
		premiumFeatures.add(Features.ENCRYPTION);
		premiumFeatures.add(Features.INVITE_USERS);
		premium.setFeatures(premiumFeatures);

		Subscription before = Subscription
				.getInstance(Subscription.BEFORE_PAID_FETURE);
		session.saveOrUpdate(premium);

		Set<String> beforeFeatures = new HashSet<String>();
		beforeFeatures.add(Features.CRESTE_COMPANY);
		beforeFeatures.add(Features.BRANDING_THEME);
		beforeFeatures.add(Features.IMPORT_BANK_STATEMENTS);
		beforeFeatures.add(Features.USER_ACTIVITY);
		beforeFeatures.add(Features.HISTORY);
		beforeFeatures.add(Features.ATTACHMENTS);
		beforeFeatures.add(Features.CLASS);
		beforeFeatures.add(Features.LOCATION);
		beforeFeatures.add(Features.BILLABLE_EXPENSE);
		beforeFeatures.add(Features.CREDITS_CHARGES);
		beforeFeatures.add(Features.MERGING);
		beforeFeatures.add(Features.JOB_COSTING);
		beforeFeatures.add(Features.ENCRYPTION);
		beforeFeatures.add(Features.INVITE_USERS);
		before.setFeatures(beforeFeatures);

		session.saveOrUpdate(before);
		beginTransaction.commit();
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
				Layout layout = new PatternLayout(
						"%d{dd MMM yyyy HH:mm:ss} %p [%c{1}] - %m %n");
				Logger.getRootLogger().addAppender(
						new DailyRollingFileAppender(layout, path,
								"'.'yyyy-MM-dd-a"));
				Appender appender = Logger.getRootLogger().getAppender(
						"console");
				appender.setLayout(layout);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getLogger("org.hibernate").setLevel(Level.INFO);
		Logger.getLogger("com.vimukti").setLevel(Level.INFO);

	}
}