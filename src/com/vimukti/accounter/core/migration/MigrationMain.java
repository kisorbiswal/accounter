package com.vimukti.accounter.core.migration;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class MigrationMain {
	private static Logger log = Logger.getLogger(MigrationMain.class);

	public static void main(String[] args) {
		ServerConfiguration.init(null);
		initLogger();
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			InventoryMigration migration = new InventoryMigration();
			migration.start();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
		log.info("Migration Completed...!");
	}

	protected static String getArgument(String[] args, String name) {
		for (int i = 0; i < args.length - 1; i++) {
			String arg = args[i];
			if (arg.equals(name)) {
				return args[i + 1];
			}
		}
		return null;
	}

	private static void initLogger() {
		File logsdir = new File("logs");
		if (!logsdir.exists()) {
			logsdir.mkdirs();
		}
		String path = new File(logsdir, "serverlog").getAbsolutePath();
		try {
			Layout layout = new PatternLayout("%d %x %C{1}.%M- %m%n");
			Logger.getRootLogger().addAppender(
					new DailyRollingFileAppender(layout, path,
							"'.'yyyy-MM-dd-a"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getLogger("org.hibernate").setLevel(Level.INFO);
		Logger.getLogger("com.vimukti").setLevel(Level.INFO);

	}

}
