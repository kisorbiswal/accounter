package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.server.FinanceTool;
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
		String fileName = "C:\\Users\\vimukti22\\Desktop\\test.properties";// req.getParameter("datafile");

		oldMessages = new FinanceTool().getAllMessages();
		List<Message> newMessages = new ArrayList<Message>();

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		while ((line = br.readLine()) != null) {
			Message message = new Message();
			String[] split = line.split("=");
			String key = split[0];
			String value = split[1];
			message.setKey(key);
			message.setValue(value);
			newMessages.add(message);
		}

		checkMessages(newMessages);

	}

	private void checkMessages(List<Message> newMessages) {
		List<Message> messagesToRemove = new ArrayList<Message>();
		// Key: Value
		for (Message message : newMessages) {
			String key = message.getKey();
			String value = message.getValue();

			Message oldMessage = getMessageByValue(value);
			// Check if Value Exists
			if (oldMessage != null) {// Yes
				// Check If Key Same
				if (oldMessage.getKey().equals(key)) {// YES
					// Mark In Use =TRUE
					oldMessage.setNotUsed(false);
					messagesToRemove.add(message);
				} else {// NO
					// Copy All Local Messages from old message to this message
					// and save
					message.setLocalMessages(oldMessage.getLocalMessages());
				}
			} else {// NO
				oldMessage = getMessageByKey(key);
				// Check if Key Exists
				if (oldMessage != null) {// YES
					// Check if Local Messages exist to old key
					if (!oldMessage.getLocalMessages().isEmpty()) {// YES
						// Rename old key to a random key
						// and mark in use =false
						// insert new message
						oldMessage.setKey(key);
						oldMessage.setNotUsed(true);

					} else {// NO
						// Delete old one and insert new one
						oldMessages.remove(oldMessage);
					}
				} else {// NO
					// Just insert the new message
				}
			}
		}

		for (Message message : messagesToRemove) {
			if (newMessages.contains(message)) {
				newMessages.remove(message);
			}
		}

		for (Message message : oldMessages) {
			if (!newMessages.contains(message)) {
				message.setNotUsed(true);
			}
		}

		oldMessages.addAll(newMessages);

		FinanceTool financeTool = new FinanceTool();
		for (Message message : oldMessages) {
			financeTool.createMessage(message);
		}

	}

	private Message getMessageByKey(String key) {
		for (Message message : oldMessages) {
			if (message.getKey().equals(key)) {
				return message;
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
