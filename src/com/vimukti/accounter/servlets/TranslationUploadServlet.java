package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.translate.LocalMessage;
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
		saveFile = "C:/translation/" + saveFile;
		File ff = new File(saveFile);
		FileOutputStream fileOut = new FileOutputStream(ff);
		fileOut.write(dataBytes, startPos, (endPos - startPos));
		fileOut.flush();
		fileOut.close();

		oldMessages = new FinanceTool().getAllMessages();
		List<Message> newMessages = new ArrayList<Message>();

		BufferedReader br = new BufferedReader(new FileReader(ff));
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

		System.out.println("Completed the Inseting of messages..");

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
					Set<LocalMessage> localMessages = oldMessage
							.getLocalMessages();
					for (LocalMessage localMessage : localMessages) {
						localMessage.setMessage(message);
					}
					message.setLocalMessages(localMessages);
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
						oldMessage.setKey(key + value);
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
