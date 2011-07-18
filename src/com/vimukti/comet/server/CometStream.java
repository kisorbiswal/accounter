package com.vimukti.comet.server;

import java.io.NotSerializableException;

public interface CometStream {

	/**
	 * Used for sending object's that are not JSONSerializable
	 * 
	 * @param obj
	 * @param method
	 */
	public void put(Object obj) throws NotSerializableException;
}
