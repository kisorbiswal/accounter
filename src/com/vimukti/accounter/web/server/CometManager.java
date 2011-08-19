package com.vimukti.accounter.web.server;

import java.util.HashMap;
import java.util.Map;

import net.zschech.gwt.comet.server.CometSession;

import org.apache.log4j.Logger;

public class CometManager {

	static Logger log = Logger.getLogger(CometManager.class);
	static int numberofusers = 0;

	// static Set<String> activeUserIds = new HashSet<String>();

	/**
	 * Map of UserID and the Queue
	 */
	private static HashMap<String, Map<String, CometSession>> map = new HashMap<String, Map<String, CometSession>>();

	// private static CommandQueue<ObjectPayload> queue;

	/**
	 * Returns a Queue of a particular key
	 * 
	 * @param key
	 * @return
	 */
	static CometSession getQueue(String sessionID, String emailID) {
		Map<String, CometSession> queues = map.get(emailID);
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
	public static CometStream getStream(String emailId, String stream) {
		Map<String, CometSession> queues = map.get(emailId);
		if (queues == null) {
			return null;
		}
		StreamCommandQueue str = new StreamCommandQueue(queues.values());
		// str.setRPCServiceInterface(streamVsRPCInterfaceMap.get(stream));
		return str;
	}

	public static void initStream(String sessionID, String emailID,
			CometSession cometSession) {
		Map<String, CometSession> queues = map.get(emailID);
		if (queues == null) {
			queues = new HashMap<String, CometSession>();
			map.put(emailID, queues);
		}
		queues.put(sessionID, cometSession);
	}

	public static void destroyStream(String sessionID, String emailID) {
		Map<String, CometSession> queues = map.get(emailID);
		if (queues != null) {
			queues.remove(sessionID);
		}
		if (queues == null || queues.size() == 0) {
			map.remove(emailID);
		}
	}

}
