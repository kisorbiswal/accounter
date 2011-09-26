/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ConsoleChatServer extends Thread {

	private MobileMessageHandler messageHandler;

	/**
	 * Creates new Instance
	 */
	public ConsoleChatServer() {
		this.messageHandler = new MobileMessageHandler();
	}

	public void startChat() {
		try {
			ServerSocket server = new ServerSocket(8080);
			Socket accept = server.accept();
			ObjectOutputStream out = new ObjectOutputStream(
					accept.getOutputStream());
			out.flush();
			InputStream inputStream = accept.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);
			out.writeObject("Connection Successfull");
			System.out.println("Console Chat Server Started.");
			while (true) {
				String readObject = (String) in.readObject();
				System.out.println(readObject);
				String messageReceived = messageHandler.messageReceived(
						ServerConfiguration.getChatUsername(), readObject,
						AdaptorType.CHAT);
				out.writeObject(messageReceived);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		startChat();
	}
}
