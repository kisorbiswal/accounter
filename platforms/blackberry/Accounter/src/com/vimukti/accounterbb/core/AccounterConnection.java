package com.vimukti.accounterbb.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SecureConnection;

import net.rim.device.api.ui.UiApplication;

import com.vimukti.accounterbb.result.Result;
import com.vimukti.accounterbb.utils.ConnectionUtils;
import com.vimukti.json.me.JSONObjectVimukti;

/**
 * This class is used to open the Socket Connection
 * 
 */
public class AccounterConnection extends Thread {
	public static final int PORT = 9084;
	// private static final String url = "mobile.accounterlive.com";

	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private SecureConnection connection;
	private ConnectionListener listener;
	private boolean isConnected;

	private String url;

	private boolean shouldConnect = true;

	private String message;

	public AccounterConnection(ConnectionListener listener) {
		this.isConnected = false;
		this.url = "ssl://www.accounterlive.com:" + PORT;
		this.setListener(listener);
		this.start();
	}

	public AccounterConnection(ConnectionListener listener, String domain) {
		this.isConnected = false;
		this.url = "ssl://" + domain + ":" + PORT;
		this.setListener(listener);
		this.start();
	}

	public void run() {
		while (true) {
			try {
				if (!isConnected) {
					if (shouldConnect()) {

						connection = ConnectionUtils.getConnection(url);
						shouldConnect = false;
						isConnected = true;
						inputStream = connection.openDataInputStream();
						outputStream = connection.openDataOutputStream();
						fireConnected();
					}
				} else {
					String msg = getMessage();
					if (msg != null) {
						byte[] mes = msg.getBytes("UTF-8");
						outputStream.writeInt(mes.length);
						outputStream.write(mes);
						outputStream.flush();
						// Just to make it null in thread safe way
						messageSent(msg);
						int size = inputStream.readInt();
						byte[] array = new byte[size];
						int index = 0;
						while (size > 0) {
							int read = inputStream.read(array, index, size);
							if (read != -1) {
								size -= read;
								index += read;
							} else {
								throw new RuntimeException("Invalid Data");
							}
						}
						messageReceived(array);
					} else {
						synchronized (this) {
							this.wait();
						}
					}
				}
			} catch (Exception e) {
				if (connection != null) {

					try {
						connection.close();
						connection = null;

					} catch (Exception ee) {

					}
				}
				fireConnectionClosed(isConnected);
				isConnected = false;
				shouldConnect = false;
			}
		}
	}

	private synchronized void messageSent(final String msg) {
		this.message = null;
		if (listener == null) {
			return;
		}
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				listener.messageSent(msg);
			}
		});
	}

	private synchronized String getMessage() {
		return message;
	}

	private void fireConnected() {
		if (listener == null) {
			return;
		}
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				listener.connectionEstablished();
			}
		});
	}

	private synchronized boolean shouldConnect() {
		return shouldConnect;
	}

	public synchronized void connect() {
		shouldConnect = true;
	}

	private void messageReceived(byte[] message) throws Exception {

		String string = new String(message, "UTF-8");
		JSONObjectVimukti jsonObject = new JSONObjectVimukti(string);
		ResultDecoder decoder = new ResultDecoder(jsonObject);
		Result result = decoder.decode();
		fireMessageReceived(result);
	}

	private void fireMessageReceived(final Result result) {
		if (listener == null) {
			return;
		}
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				listener.messageReceived(result);
			}
		});
	}

	public synchronized void sendMessage(String msg) {
		message = msg;
		this.notify();
		// messageSent(msg);

	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
			fireConnectionClosed(false);
		} catch (Exception e) {
		}
	}

	private void fireConnectionClosed(final boolean fromRemote) {
		if (listener == null) {
			return;
		}
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				listener.connectionClosed(fromRemote);
			}
		});
	}

	public void setListener(ConnectionListener listener) {
		this.listener = listener;
	}

}
