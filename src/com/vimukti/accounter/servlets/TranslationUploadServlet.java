package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.translate.Key;
import com.vimukti.accounter.web.server.translate.Message;

public class TranslationUploadServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String TRANSLATION_UPLOAD = "/WEB-INF/translate/upload.jsp";

	private List<Message> oldMessages;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp, TRANSLATION_UPLOAD);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String contentType = req.getContentType();
		byte dataBytes[] = null;
		if ((contentType != null)
				&& (contentType.indexOf("multipart/form-data") >= 0)) {
			DataInputStream in = new DataInputStream(req.getInputStream());
			int formDataLength = req.getContentLength();
			dataBytes = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength) {
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
		}
		String file = new String(dataBytes);
		String saveFile = file.substring(file.indexOf("filename=\"") + 10);
		saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
		saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,
				saveFile.indexOf("\""));
		int lastIndex = contentType.lastIndexOf("=");
		String boundary = contentType.substring(lastIndex + 1,
				contentType.length());
		int pos;
		pos = file.indexOf("filename=\"");
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		int boundaryLocation = file.indexOf(boundary, pos) - 4;
		int startPos = ((file.substring(0, pos)).getBytes()).length;
		int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
		saveFile = System.getProperty("java.io.tmpdir") + saveFile;
		File ff = new File(saveFile);
		FileOutputStream fileOut = new FileOutputStream(ff);
		fileOut.write(dataBytes, startPos, (endPos - startPos));
		fileOut.flush();
		fileOut.close();

		oldMessages = new FinanceTool().getAllMessages();
		Map<Key, Message> newMessages = new HashMap<Key, Message>();

		BufferedReader br = new BufferedReader(new FileReader(ff));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] split = line.split("=");
			String keyValue = split[0];
			String value = split[1];

			Key key = new Key();
			key.setKey(keyValue);

			Message message = new Message();
			message.setValue(value);

			Set<Key> keys = new HashSet<Key>();
			keys.add(key);
			message.setKeys(keys);

			newMessages.put(key, message);
		}

		checkMessages(newMessages);

		System.out.println("Completed the Inseting of messages..");

	}

	private void checkMessages(Map<Key, Message> newMessages) {
		Map<Key, Message> messagesToRemove = new HashMap<Key, Message>();
		List<Message> messagesToDelete = new ArrayList<Message>();
		// Key: Value
		for (Entry<Key, Message> entry : newMessages.entrySet()) {
			Key key = entry.getKey();
			Message value = entry.getValue();

			Message oldMessage = getMessageByValue(value.getValue());
			// Check if Value Exists
			if (oldMessage != null) {// Yes
				// Check If Key Same
				boolean keySame = false;
				Iterator<Key> iterator = oldMessage.getKeys().iterator();
				while (iterator.hasNext()) {
					Key next = iterator.next();
					if (next.getKey().equals(key.getKey())) {
						keySame = true;
					}
				}
				if (keySame) {// YES
					// Mark In Use =TRUE
					messagesToRemove.put(key, value);
				} else {// NO
					// Copy All Local Messages from old message to this message
					// and save
					// Set<LocalMessage> localMessages = oldMessage
					// .getLocalMessages();
					// value.setLocalMessages(localMessages);
					Set<Key> keys = oldMessage.getKeys();
					keys.add(key);
					oldMessage.setKeys(keys);
					messagesToRemove.put(key, value);
				}
			} else {// NO
				oldMessage = getMessageByKey(key);
				// Check if Key Exists
				if (oldMessage != null) {// YES
					// Check if Local Messages exist to old key
					Set<Key> keys = new HashSet<Key>();
					Iterator<Key> iterator = oldMessage.getKeys().iterator();
					while (iterator.hasNext()) {
						Key next = iterator.next();
						if (next.getKey().equals(key.getKey())) {
							keys.add(next);
						}
					}
					value.setKeys(keys);
					if (!oldMessage.getLocalMessages().isEmpty()) {// YES
						// Rename old key to a random key
						// and mark in use =false
						// insert new message
						oldMessage.setNotUsed(true);

					} else {// NO
						// Delete old one and insert new one
						oldMessages.remove(oldMessage);
						messagesToDelete.add(oldMessage);
					}

				} else {// NO
					// Just insert the new message
				}
			}
		}

		for (Entry<Key, Message> entry : messagesToRemove.entrySet()) {
			if (newMessages.containsValue(entry.getValue())) {
				newMessages.remove(entry.getKey());
			}
		}

		for (Message message : oldMessages) {
			if (!newMessages.containsValue(message)) {
				message.setNotUsed(true);
			}
		}

		oldMessages.addAll(newMessages.values());

		FinanceTool financeTool = new FinanceTool();
		for (Message message : oldMessages) {
			financeTool.createMessage(message);
		}
		for (Message message : messagesToDelete) {
			financeTool.deleteMessage(message);
		}

	}

	private Message getMessageByKey(Key key) {
		for (Message message : oldMessages) {
			Iterator<Key> iterator = message.getKeys().iterator();
			while (iterator.hasNext()) {
				Key next = iterator.next();
				if (next.getKey().equals(key.getKey())) {
					return message;
				}
			}
		}
		return null;
	}

	private Message getMessageByValue(String value) {
		for (Message message : oldMessages) {
			if (message.getValue().equals(value)) {
				return message;
			}
		}
		return null;
	}

}
