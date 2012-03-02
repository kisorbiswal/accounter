/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ConsoleChatServer extends Thread {

	private MobileMessageHandler messageHandler;
	private static Logger log = Logger.getLogger(ConsoleChatClient.class);

	/**
	 * Creates new Instance
	 */
	public ConsoleChatServer() {
		this.messageHandler = MobileMessageHandler.getInstance();
	}

	public void startChat() {
		try {
			ServerSocket server = new ServerSocket(
					ServerConfiguration.getConsoleChatServerPort());
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
			String user = null;
			try {
				final ObjectOutputStream out = new ObjectOutputStream(
						socket.getOutputStream());
				InputStream inputStream = socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(inputStream);
				out.writeObject("Connection Successfull");
				log.info("Console Chat Server Started.");

				while (socket.isConnected()) {
					user = (String) in.readObject();
					Object readObject = in.readObject();
					String msg = (String) readObject;
					log.info(msg);
					MobileChannelContext context = new MobileChannelContext(
							user, msg, AdaptorType.CHAT,
							AccounterChatServer.NETWORK_TYPE_CONSOLE) {

						@Override
						public void send(String string) {
							try {
								out.writeObject(string);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void changeNetworkId(String networkId) {
							// TODO Auto-generated method stub

						}
					};
					handler.putMessage(context);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handler.logout(user);
			}
		}

	}
}
