package com.vimukti.comet.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;

public class CometClient {

	public static final String JSONSTREAM = "json";
	public static final String GWTSTREAM = "gwt";

	private static Map<String, CometClient> clients = new HashMap<String, CometClient>();

	public CometClient(String entryPoint) {
		this.serviceEntryPoint = entryPoint;

		initializeMetro(serviceEntryPoint);
	}

	public native void initializeMetro(String host)/*-{
		var self = this;

		$wnd.Meteor
				.registerEventCallback(
						"process",
						function(data) {
							var index = data.indexOf(':');
							var channelName = data.substr(0, index);
							data = data.substring(index + 1);
							try {
								self.@com.vimukti.comet.client.CometClient::dispatch(Ljava/lang/String;Ljava/lang/String;)(data,channelName);
							} catch (e) {
								$wnd.Meteor.log("Exception while processing "
										+ e);
							}
						});
		$wnd.Meteor.registerEventCallback("reset", self
				.@com.vimukti.comet.client.CometClient::reset());
		$wnd.Meteor.registerEventCallback("eof", self
				.@com.vimukti.comet.client.CometClient::terminate());
		$wnd.Meteor
				.registerEventCallback(
						"statuschanged",
						function(status) {
							self.@com.vimukti.comet.client.CometClient::statusChanged(I)(status);
						});
		$wnd.Meteor
				.registerEventCallback(
						"syncstatuschanged",
						function(syncstatus) {
							self.@com.vimukti.comet.client.CometClient::syncStatuschanged(I)(syncstatus);
						});
		//$wnd.Meteor.joinChannel("demo", 10);
		$wnd.Meteor.mode = 'stream';
	}-*/;

	public native void connect()/*-{
								//													$wnd.Meteor.connect();
								}-*/;

	public void statusChanged(int status) {
		for (ICometListener listener : this.callbacks.values()) {
			listener.onStatusChange(status);
		}
	}

	public void syncStatuschanged(int syncstatus) {
		for (ICometListener listener : this.callbacks.values()) {
			listener.onSyncStatusChange(syncstatus);
		}
	}

	public void terminate() {
		for (ICometListener listener : this.callbacks.values()) {
			clients.remove(listener);
			clients.get(this.serviceEntryPoint).stop();
			listener.onTerminate();
		}
	}

	public void reset() {
		for (ICometListener listener : this.callbacks.values()) {
			listener.onReset();
		}
	}

	/**
	 * Invoking this method opens the channel betweeen the client and the
	 * server. The server will periodicly continue to send objects to the client
	 * as they are streamed to the server.
	 */
	public native void start() /*-{
		$wnd.Meteor.connect();
	}-*/;

	public native void stop() /*-{
		//$wnd.alert('stop');
		$wnd.Meteor.disconnect();
	}-*/;

	protected native void disconnect()/*-{
		$wnd.Meteor.disconnect();
	}-*/;

	public void onDisconnect() {
	}

	/**
	 * Restarts or recreates the connection between this client and the server.
	 */
	// protected void restart() {
	// this.disconnect();
	// this.connect();
	// }
	public static String htmlDecode(String plainText) {

		plainText = plainText.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&quot;", "\"").replaceAll("&amp;", "&");

		return plainText;
	}

	/**
	 * 
	 * eventually dispatching the object to the registered callback.
	 * 
	 * */
	void dispatch(String data, String channel) {
		// data = CometClient.htmlDecode(data);

		Object obj = deserialize(data, channel);
		ICometListener listener = this.callbacks.get(channel);
		if (listener != null) {
			listener.onPayload(obj);
		}
	}

	private Object deserialize(String data, String channel) {
		SerializationStreamFactory ssf = factories.get(channel);
		if (ssf == null) {
			// XXX if condition not required.
			// If there is not stream factory then we can assume that it is JSON
			// object, otherwise we can not deserialize it anyway
			// JSONValue obj = JSONParser.parse(data);
			return null;
		} else {
			try {
				// Window.alert(data);
				Object object = ssf.createStreamReader(data).readObject();
				return object;
			} catch (SerializationException e) {
				// e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * The url of the server side portion of this component.
	 */
	private String serviceEntryPoint;

	/**
	 * This callback receives and handles all comet related events.
	 */
	private Map<String, ICometListener> callbacks = new HashMap<String, ICometListener>();

	private Map<String, SerializationStreamFactory> factories = new HashMap<String, SerializationStreamFactory>();

	public String toString() {
		return super.toString() + ", serviceEntryPoint: \""
				+ this.serviceEntryPoint;
	}

	public static void register(String entryPoint, String stream,
			ICometListener cometListener) {

		register(entryPoint, stream, cometListener, null);
	}

	public static void register(String entryPoint, String stream,
			ICometListener cometListener, SerializationStreamFactory factory) {
		CometClient client = clients.get(entryPoint);
		if (client == null) {
			client = new CometClient(entryPoint);
			clients.put(entryPoint, client);
		}
		client.addListener(stream, cometListener, factory);
		client.start();
	}

	private void addListener(String stream, ICometListener cometListener,
			SerializationStreamFactory factory) {
		this.joinChannel(stream);
		this.callbacks.put(stream, cometListener);
		this.factories.put(stream, factory);
	}

	public native void joinChannel(String stream) /*-{
		$wnd.Meteor.joinChannel(stream, 10);
	}-*/;

	public static void unRegister(String entryPoint, String stream) {
		CometClient client = clients.get(entryPoint);
		ICometListener listner = client.callbacks.get(stream);
		client.removeListner(stream, listner);
	}

	public native void leaveChannel(String stream) /*-{
		$wnd.Meteor.leaveChannel(stream);
	}-*/;

	private void removeListner(String stream, ICometListener listner) {
		this.leaveChannel(stream);
		this.callbacks.remove(stream);
		this.factories.remove(stream);
	};
}
