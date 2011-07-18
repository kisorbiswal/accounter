package com.vimukti.comet.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class CometManager {

	static Logger log = Logger.getLogger(CometManager.class);
	static int numberofusers = 0;

	// static Set<String> activeUserIds = new HashSet<String>();

	/**
	 * Map of UserID and the Queue
	 */
	private static HashMap<String, Map<String, CommandQueue<ObjectPayload>>> map = new HashMap<String, Map<String, CommandQueue<ObjectPayload>>>();

	private static HashMap<String, Class<?>> streamVsRPCInterfaceMap = new HashMap<String, Class<?>>();
	private static IComentConnectionListener connectionListener;
	private static CommandQueue<ObjectPayload> queue;

	/**
	 * Returns a Queue of a particular key
	 * 
	 * @param key
	 * @return
	 */
	static CommandQueue<ObjectPayload> getQueue(String sessionID,
			String identityID) {
		Map<String, CommandQueue<ObjectPayload>> queues = map.get(identityID);
		if (queues != null) {
			return queues.get(sessionID);
		}
		return null;
	}

	/**
	 * Returns CometStream that is used to send JSONSerializable objects. For
	 * other objects you must call setRPCServiceInterface before sending any
	 * objects.
	 * 
	 * This method creates the stream if one does not exist. From 2nd time
	 * onwards it will return the same stream.
	 * 
	 * @param stream
	 *            Name of the stream
	 * @return
	 */
	public static CometStream getStream(String userID, String stream) {
		Map<String, CommandQueue<ObjectPayload>> queues = map.get(userID);
		if (queues == null) {
			return null;
		}
		StreamCommandQueue str = new StreamCommandQueue(stream, queues.values());
		str.setRPCServiceInterface(streamVsRPCInterfaceMap.get(stream));
		return str;
	}

	/**
	 * Initialise a stream with name and RPC interface
	 * 
	 * @param stream
	 * @param cls
	 */
	public static void initStream(String stream, Class<?> cls) {
		streamVsRPCInterfaceMap.put(stream, cls);
	}

	public static void initStream(String sessionID, String userID, String stream) {
		Map<String, CommandQueue<ObjectPayload>> queues = map.get(userID);
		if (queues == null) {
			queues = new HashMap<String, CommandQueue<ObjectPayload>>();
			map.put(userID, queues);
		}
		CommandQueue<ObjectPayload> queue = queues.get(sessionID);
		if (queue == null) {
			queue = new CommandQueue<ObjectPayload>();
			queues.put(sessionID, queue);
		} else {
			queue.clear();
		}
		StreamCommandQueue str = new StreamCommandQueue(stream, queues.values());
		str.setRPCServiceInterface(streamVsRPCInterfaceMap.get(stream));
	}

	public static void destroyStream(String sessionID, String userID) {
		Map<String, CommandQueue<ObjectPayload>> queues = map.get(userID);
		if (queues != null) {
			queues.remove(sessionID);
		}
		if (queues.size() == 0) {
			map.remove(userID);
		}
	}

	public static void setConnectionListener(IComentConnectionListener listener) {
		connectionListener = listener;
	}

	public static void onConnectionEsatablished(String identityID,
			String companyName) {
		log.info("Connection established with identity: " + identityID);

		if (connectionListener != null) {
			connectionListener.onConnectionEstablished(identityID, companyName);
		}
	}
}
