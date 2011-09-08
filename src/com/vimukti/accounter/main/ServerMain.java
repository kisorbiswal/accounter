package com.vimukti.accounter.main;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.admin.core.AdminUser;
import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.core.ServerMaintanance;
import com.vimukti.accounter.mail.EmailManager;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;

public class ServerMain extends Main {
	private static boolean isDebug;

	public static void main(String[] args) throws Exception {

		isDebug = checkDebugMode(args);
		ServerConfiguration.isDebugMode = isDebug;
		String configFile = getArgument(args, "-config");
		ServerConfiguration.init(configFile);
		initLogger();

		Session session = HibernateUtil
				.openSession(Server.LOCAL_DATABASE, true);

		ServerMaintanance maintanance = (ServerMaintanance) session.get(
				ServerMaintanance.class, 1L);
		if (maintanance != null) {
			ServerConfiguration.setUnderMaintainance(maintanance
					.isUnderMaintanance());
		}
		createAdminUser();

		session.close();

		EmailManager.getInstance().start();

		Global.set(new ServerGlobal());

		// session.close();

		JettyServer.start(ServerConfiguration.getMainServerPort());
		JettyServer.jettyServer.join();

	}

	private static void createAdminUser() {
		Session currentSession = HibernateUtil.getCurrentSession();
		Object load = currentSession.get(AdminUser.class, 1l);
		if (load != null) {
			return;
		}
		Transaction beginTransaction = currentSession.beginTransaction();
		AdminUser user = new AdminUser();
		user.setEmailId(ServerConfiguration.getAdminID());
		user.setPassword(ServerConfiguration.getAdminPassword());
		user.setName("Admin");
		currentSession.saveOrUpdate(user);
		beginTransaction.commit();
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