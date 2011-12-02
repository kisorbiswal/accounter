package com.vimukti.accounter.core.migration;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.vimukti.accounter.main.ServerConfiguration;

public class MigrationMain {
	public static void main(String[] args) {
		ServerConfiguration.init(null);
		initLogger();
		MakeDepositMigration mdm = new MakeDepositMigration();
		mdm.start();
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
