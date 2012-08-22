package com.vimukti.accounter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vimukti.accounter.result.Result;
import com.vimukti.accounter.result.ResultJsonDeserializer;

/**
 * This class is used to open the Socket Connection
 * 
 */
public class AccounterConnection extends Thread {
	public static final int PORT = 9084;
	// private static final String url = "mobile.accounterlive.com";

	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Socket socket;
	private ConnectionListener listener;
	private boolean isConnected;

	private String url;

	private boolean shouldConnect = true;

	private String message;
	private Activity activity;
	private Gson gson;

	public AccounterConnection(ConnectionListener listener, Activity activity,
			String url) {
		this.isConnected = false;
		this.url = url;
		this.setListener(listener);
		this.start();
		this.activity = activity;

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Result.class, new ResultJsonDeserializer());
		builder.registerTypeAdapter(Result.class, new JsonSerializer<Result>() {

			@Override
			public JsonElement serialize(Result arg0, Type arg1,
					JsonSerializationContext arg2) {
				return null;
			}
		});
		gson = builder.create();
	}

	public void run() {
		while (true) {
			try {
				if (!isConnected) {
					if (shouldConnect()) {
						socket = SSLSocketFactory.getDefault().createSocket(
								url, PORT);
						shouldConnect = false;
						isConnected = true;
						inputStream = new DataInputStream(socket
								.getInputStream());
						outputStream = new DataOutputStream(socket
								.getOutputStream());
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
				try {
					socket.close();
					socket = null;
					isConnected = false;
				} catch (Exception ee) {

				}
				fireConnectionClosed(true);
			}
		}
	}

	// private void connecting() {
	// activity.runOnUiThread(new Runnable() {
	//
	// public void run() {
	// listener.connecting();
	// }
	// });
	// }

	private synchronized void messageSent(final String msg) {
		this.message = null;
		if (listener == null) {
			return;
		}

		activity.runOnUiThread(new Runnable() {

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
		activity.runOnUiThread(new Runnable() {

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
		Result result = null;
		try {
			result = gson.fromJson(string, Result.class);
		} catch (Exception e) {
			closeConnection();
			return;
		}

		fireMessageReceived(result);
	}

	private void fireMessageReceived(final Result result) {
		if (listener == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {

			public void run() {
				listener.messageReceived(result);
			}
		});
	}

	public synchronized void sendMessage(String msg) {
		message = msg;
		this.notify();
	}

	public void closeConnection() {
		try {
			if (socket != null) {
				socket.close();
			}
			fireConnectionClosed(false);
		} catch (Exception e) {
		}
	}

	private void fireConnectionClosed(final boolean fromRemote) {
		if (listener == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {

			public void run() {
				listener.connectionClosed(fromRemote);
			}
		});
	}

	public void setListener(ConnectionListener listener) {
		this.listener = listener;
	}

}
