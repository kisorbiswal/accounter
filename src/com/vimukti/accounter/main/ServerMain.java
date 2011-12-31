package com.vimukti.accounter.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.vimukti.accounter.mail.EmailManager;
import com.vimukti.accounter.main.upload.AttachmentFileServer;
import com.vimukti.accounter.mobile.AccounterChatServer;
import com.vimukti.accounter.mobile.AccounterMobileException;
import com.vimukti.accounter.mobile.ConsoleChatServer;
import com.vimukti.accounter.mobile.MobileServer;
import com.vimukti.accounter.mobile.store.CommandsFactory;
import com.vimukti.accounter.mobile.store.PatternStore;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.RecurringTool;
import com.vimukti.accounter.web.server.translate.Key;
import com.vimukti.accounter.web.server.translate.Message;

public class ServerMain extends Main {
	private static boolean isDebug;
	private static List<Message> oldMessages;

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
		} finally {
			session.close();
		}
		AttachmentFileServer.getInstance().start();
		EmailManager.getInstance().start();
		// EMailMonitor.getInstance().start();
		Global.set(new ServerGlobal());

		if (ServerConfiguration.isLoadMessages()) {
			loadAccounterMessages();
		}

		stratChatServers();

		startRecurringTimer();

		JettyServer.start(ServerConfiguration.getMainServerPort());
		JettyServer.jettyServer.join();

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
		oldMessages = new FinanceTool().getAllMessages();
		Map<Key, Message> newMessages = new HashMap<Key, Message>();

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
			reader = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String[] split = line.split("=");
				if (split.length < 2) {
					continue;
				}
				String keyValue = split[0];
				String value = split[1];

				Key key = new Key();
				key.setKey(keyValue.trim());

				Message message = new Message();
				message.setValue(value);

				Set<Key> keys = new HashSet<Key>();
				keys.add(key);
				message.setKeys(keys);

				boolean insert = true;
				for (Entry<Key, Message> entry : newMessages.entrySet()) {
					Key k = entry.getKey();
					Message v = entry.getValue();
					if (v.getValue().equals(message.getValue())) {
						v.getKeys().add(key);
						insert = false;
						break;
					}
				}

				for (Key key2 : newMessages.keySet()) {
					if (key2.equals(key)) {
						insert = false;
						break;
					}
				}

				if (insert) {
					newMessages.put(key, message);
				}
			}

			checkMessages(newMessages);

			System.out.println("Completed the Inseting of messages..");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not found.", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (is != null) {
				is.close();
			}
		}

	}

	private static void checkMessages(Map<Key, Message> newMessages) {
		Session session = HibernateUtil.openSession();
		List<Message> removed = new ArrayList<Message>();//
		List<Message> addedMessages = new ArrayList<Message>();
		for (Entry<Key, Message> entry : newMessages.entrySet()) {
			Key key = entry.getKey();
			Message value = entry.getValue();
			boolean isInserted = false;
			Message messageByMessage = getMessageByValue(value.getValue());
			if (messageByMessage == null) {
				isInserted = true;
			} else {
				boolean keySame = false;
				for (Key k : messageByMessage.getKeys()) {
					if (k.equals(key)) {
						keySame = true;
						break;
					}
				}
				if (!keySame) {
					messageByMessage.getKeys().add(key);
				}
				isInserted = false;
			}

			List<Message> messageByKey = getMessageByKey(key);
			if (messageByKey.isEmpty()) {
				isInserted = true;
			} else {
				for (Message m : messageByKey) {
					if (!m.getLocalMessages().isEmpty()) {
						m.getKeys().remove(key);
					} else {
						if (!m.getValue().equals(value.getValue())) {
							removed.add(m);
							isInserted = true;
						}
					}
				}
			}
			if (isInserted) {
				addedMessages.add(value);
			}

		}

		for (Message message : oldMessages) {
			boolean contains = false;
			for (Message newMessage : newMessages.values()) {
				if (newMessage.getValue().equals(message.getValue())) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				message.setNotUsed(true);
			}
		}
		try {
			org.hibernate.Transaction transaction = session.beginTransaction();
			for (Message message : removed) {
				if (message != null) {
					session.delete(message);
					oldMessages.remove(message);
				}
			}
			oldMessages.addAll(addedMessages);
			for (Message message : oldMessages) {
				session.saveOrUpdate(message);
			}
			transaction.commit();
		} finally {
			session.close();
		}
		//
		// Map<Key, Message> messagesToRemove = new HashMap<Key, Message>();
		// List<Message> messagesToDelete = new ArrayList<Message>();
		// // Key: Value
		// for (Entry<Key, Message> entry : newMessages.entrySet()) {
		// Key key = entry.getKey();
		// Message value = entry.getValue();
		//
		// if (value.getValue().equals("*Example1   :")) {
		// System.out.println("vb");
		// }
		//
		// Message oldMessage = getMessageByValue(value.getValue());
		// // Check if Value Exists
		// if (oldMessage != null) {// Yes
		// // Check If Key Same
		// boolean keySame = false;
		// Iterator<Key> iterator = oldMessage.getKeys().iterator();
		// while (iterator.hasNext()) {
		// Key next = iterator.next();
		// if (next.getKey().equals(key.getKey())) {
		// keySame = true;
		// break;
		// }
		// }
		// if (keySame) {// YES
		// // Mark In Use =TRUE
		// messagesToRemove.put(key, value);
		// } else {// NO
		// // Copy All Local Messages from old message to this message
		// // and save
		// // Set<LocalMessage> localMessages = oldMessage
		// // .getLocalMessages();
		// // value.setLocalMessages(localMessages);
		// Set<Key> keys = oldMessage.getKeys();
		// keys.add(key);
		// oldMessage.setKeys(keys);
		// messagesToRemove.put(key, value);
		// }
		// } else {// NO
		// List<Message> oldMessagesByKey = getMessageByKey(key);
		// // Check if Key Exists
		// if (!oldMessagesByKey.isEmpty()) {// YES
		// // Check if Local Messages exist to old key
		// for (Message oldMessageByKey : oldMessagesByKey) {
		//
		// if (!oldMessageByKey.getLocalMessages().isEmpty()) {// YES
		// // Rename old key to a random key
		// // and mark in use =false
		// // insert new message
		// Set<Key> keys = new HashSet<Key>();
		// Iterator<Key> iterator = oldMessageByKey.getKeys()
		// .iterator();
		// while (iterator.hasNext()) {
		// Key next = iterator.next();
		// if (next.getKey().equals(key.getKey())) {
		// keys.add(next);
		// }
		// }
		// value.setKeys(keys);
		//
		// oldMessageByKey.getKeys().remove(key);
		// oldMessageByKey.setNotUsed(true);
		// session.saveOrUpdate(oldMessageByKey);
		// oldMessages.remove(oldMessageByKey);
		// } else {// NO
		// // Delete old one and insert new one
		// oldMessages.remove(oldMessageByKey);
		// messagesToDelete.add(oldMessageByKey);
		// }
		// }
		// } else {// NO
		// // Just insert the new message
		// }
		// }
		// }

		// for (Entry<Key, Message> entry : messagesToRemove.entrySet()) {
		// if (newMessages.containsValue(entry.getValue())) {
		// newMessages.remove(entry.getKey());
		// }
		// }
		//
		// for (Message message : oldMessages) {
		// if (!newMessages.containsValue(message)) {
		// message.setNotUsed(true);
		// }
		// }

		// oldMessages.addAll(newMessages.values());

		// try {
		// org.hibernate.Transaction deleteTransaction = session
		// .beginTransaction();
		// for (Message message : removed) {
		// if (message != null) {
		// session.delete(message);
		// }
		// }
		// deleteTransaction.commit();
		//
		// org.hibernate.Transaction transaction = session.beginTransaction();
		// for (Message message : oldMessages) {
		// if (message.getId() == 0) {
		// Query messageQuery = session.getNamedQuery(
		// "getMessageByValue").setParameter("value",
		// message.getValue());
		// for (Key key : message.getKeys()) {
		// Query keyQuery = session.getNamedQuery("getKeyByValue")
		// .setParameter("value", key.getKey());
		// Key duplicateKey = (Key) keyQuery.uniqueResult();
		// if (duplicateKey != null
		// && key.getId() != duplicateKey.getId()) {
		// message.getKeys().remove(key);
		// message.getKeys().add(duplicateKey);
		// }
		// }
		// Message oldMessage = (Message) messageQuery.uniqueResult();
		// if (oldMessage != null) {
		// Set<Key> keys = oldMessage.getKeys();
		// keys.addAll(message.getKeys());
		// oldMessage.setKeys(keys);
		// message = oldMessage;
		// }
		// }
		// session.saveOrUpdate(message);
		//
		// }
		// transaction.commit();
		// } finally {
		// session.close();
		// }

	}

	private static List<Message> getMessageByKey(Key key) {
		List<Message> oldMessagesByKey = new ArrayList<Message>();
		for (Message message : oldMessages) {
			Iterator<Key> iterator = message.getKeys().iterator();
			while (iterator.hasNext()) {
				Key next = iterator.next();
				if (next.getKey().equals(key.getKey())) {
					oldMessagesByKey.add(message);
				}
			}
		}
		return oldMessagesByKey;
	}

	private static Message getMessageByValue(String value) {
		for (Message message : oldMessages) {
			if (message.getValue().equals(value)) {
				return message;
			}
		}
		return null;
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