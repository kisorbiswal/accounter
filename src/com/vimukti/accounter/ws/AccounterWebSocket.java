package com.vimukti.accounter.ws;

import java.io.IOException;

import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

class AccounterWebSocket implements OnTextMessage {

	/**
	 * 
	 */
	private final WebSocketHandler webSocketHandler;

	/**
	 * @param webSocketHandler
	 */
	AccounterWebSocket(WebSocketHandler webSocketHandler) {
		this.webSocketHandler = webSocketHandler;
	}

	private Connection connection;

	@Override
	public void onClose(int closeCode, String message) {
		this.webSocketHandler.users.remove(this);

	}

	@Override
	public void onOpen(Connection connection) {
		this.webSocketHandler.users.add(this);
		this.connection = connection;

	}

	@Override
	public void onMessage(String req) {
		LocalConnector connector = webSocketHandler.getConnector();
		try {
			int index = req.indexOf(':');
			if (index == -1) {
				return;
			}
			int id = Integer.parseInt(req.substring(0, index));
			req = req.substring(index + 1);
			ByteArrayBuffer responses = connector.getResponses(
					new ByteArrayBuffer(req, "UTF-8"), true);
			String resp = responses.toString("UTF-8");
			connection.sendMessage(id + ":" + resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}