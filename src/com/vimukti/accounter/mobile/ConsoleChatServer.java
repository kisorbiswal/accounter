/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
			Socket socket = null;
			while ((socket = server.accept()) != null) {
				new ConsoleSocketHandler(socket, messageHandler).start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		startChat();
	}

	private static class ConsoleSocketHandler extends Thread {

		private MobileMessageHandler handler;
		private Socket socket;

		public ConsoleSocketHandler(Socket socket, MobileMessageHandler handler) {
			this.handler = handler;
			this.socket = socket;
		}

		public void run() {
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						socket.getOutputStream());
				InputStream inputStream = socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(inputStream);
				out.writeObject("Connection Successfull");
				System.out.println("Console Chat Server Started.");
				while (true) {
					String user = (String) in.readObject();
					String msg = (String) in.readObject();
					System.out.println(msg);
					try {
						String messageReceived = handler.messageReceived(user,
								msg, AdaptorType.CHAT);
						out.writeObject(messageReceived);
					} catch (AccounterMobileException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
