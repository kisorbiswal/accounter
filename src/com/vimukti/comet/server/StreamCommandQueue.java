package com.vimukti.comet.server;

import java.io.NotSerializableException;
import java.util.Collection;

public class StreamCommandQueue implements CometStream {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stream;
	private Collection<CommandQueue<ObjectPayload>> queues;

	StreamCommandQueue(String stream,
			Collection<CommandQueue<ObjectPayload>> queues) {
		this.stream = stream;
		this.queues = queues;
	}

	// @Override
	// public void put(JSONSerializable obj) {
	// queue.put(obj,stream);
	// }

	@Override
	public void put(Object obj) throws NotSerializableException {
		for (CommandQueue<ObjectPayload> queue : queues) {
			queue.put(obj, stream);
		}
	}

	void setRPCServiceInterface(Class<?> cls) {
		for (CommandQueue<ObjectPayload> queue : queues) {
			queue.setRPCServiceInterface(cls);
		}
	}

}
