package com.vimukti.accounterbb.core;

import com.vimukti.accounterbb.result.Result;

/**
 * Contains all the methods regarding Connection and their messages
 * 
 */
public interface ConnectionListener {

	public abstract void connectionEstablished();

	public abstract void messageReceived(Result result);

	public abstract void messageSent(String msg);

	public abstract void connectionClosed(boolean fromRemote);
}
