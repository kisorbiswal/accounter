package com.vimukti.comet.server;

import java.io.NotSerializableException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.mortbay.util.ajax.Continuation;

import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;

public class CommandQueue<T> extends LinkedList<ObjectPayload> {

	private static int queueCounter = 0;

	Logger log = Logger.getLogger(CommandQueue.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long lastDequeued = System.currentTimeMillis();
	private Continuation continuation;
	private Class<?> rpcInterface;
	private SerializationPolicy policy = new SerializationPolicy() {

		public boolean shouldDeserializeFields(final Class<?> clazz) {
			throw new UnsupportedOperationException("shouldDeserializeFields");
		}

		public boolean shouldSerializeFields(final Class<?> clazz) {
			return Object.class != clazz;
		}

		public void validateDeserialize(final Class<?> clazz) {
			throw new UnsupportedOperationException("validateDeserialize");
		}

		public void validateSerialize(final Class<?> clazz) {
		}
	};

	private int no;

	public CommandQueue() {
		this.no = ++queueCounter;
	}

	@Override
	public ObjectPayload poll() {
		this.lastDequeued = System.currentTimeMillis();
		log.info("Removing from Queue: " + no + " size:" + this.size());
		return super.poll();
	}

	long getLastDequed() {
		return this.lastDequeued;
	}

	synchronized void process() {
		if (this.continuation != null) {
			this.continuation.resume();
		}
	}

	synchronized void setContinuation(Continuation con) {
		this.continuation = con;
	}

	@Override
	public boolean isEmpty() {
		this.lastDequeued = System.currentTimeMillis();
		return super.isEmpty();
	}

	void put(Object obj, String stream) throws NotSerializableException {
		if(stream==null){
			return ;
		}
		String data = null;
		try {
			Method serviceMethod = getMethodForObject(obj);
			data = RPC
					.encodeResponseForSuccess(serviceMethod, obj, this.policy);
		} catch (Exception e) {
			e.printStackTrace();
			// For any exception we have same response
			throw new NotSerializableException();
		}

		ObjectPayload payload = new ObjectPayload(data, stream);
		super.add(payload);
		log.info("Added to Queue: " + no + " size:" + this.size());
		this.process();
	}

	private Method getMethodForObject(Object obj) throws SecurityException,
			NoSuchMethodException {
		return this.rpcInterface.getMethod("get"
				+ obj.getClass().getSimpleName());
	}

	void setRPCServiceInterface(Class<?> cls) {
		this.rpcInterface = cls;
	}
}
