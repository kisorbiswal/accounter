package com.vimukti.accounter.main;

import java.io.BufferedReader;
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

import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.translate.Key;
import com.vimukti.accounter.web.server.translate.Message;

public class MessageLoader {
	private InputStreamReader reader;
	private static List<Message> oldMessages;

	public MessageLoader(InputStream inputStream) throws Exception {
		oldMessages = new FinanceTool().getAllMessages();
		this.reader = new InputStreamReader(inputStream, "utf-8");
	}

	public void loadMessages() throws Exception {
		Map<Key, Message> newMessages = new HashMap<Key, Message>();
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

	}

	private static void checkMessages(Map<Key, Message> newMessages) {
		Session session = HibernateUtil.openSession();
		List<Message> removed = new ArrayList<Message>();
		List<Message> addedMessages = new ArrayList<Message>();
		for (Entry<Key, Message> entry : newMessages.entrySet()) {
			Key key = entry.getKey();
			Message value = entry.getValue();
			boolean isInserted = false;
			Message messageByMessage = getMessageByValue(value.getValue());
			if (messageByMessage == null) {
				isInserted = true;
			} else {
				Key kk = null;
				for (Key k : messageByMessage.getKeys()) {
					if (k.equals(key)) {
						kk = k;
						break;
					}
				}
				if (kk != null) {
					value.getKeys().remove(key);
					value.getKeys().add(kk);
				} else {
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
						for (Key k : m.getKeys()) {
							if (k.equals(key)) {
								m.getKeys().remove(k);
								value.getKeys().remove(key);
								value.getKeys().add(k);
								break;
							}
						}
					} else {
						if (!m.getValue().equals(value.getValue())) {
							removed.add(m);
							for (Key k : m.getKeys()) {
								if (k.equals(key)) {
									value.getKeys().remove(key);
									value.getKeys().add(k);
								}
							}
						} else {
							isInserted = false;
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
		oldMessages.addAll(addedMessages);
		try {
			org.hibernate.Transaction transaction = session.beginTransaction();
			for (Message message : removed) {
				if (message != null) {
					session.delete(message);
					oldMessages.remove(message);
				}
			}
			for (Message message : oldMessages) {
				session.saveOrUpdate(message);
			}
			transaction.commit();

		} finally {
			session.close();
		}
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
}
