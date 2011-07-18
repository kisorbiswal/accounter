package com.vimukti.comet.client;

public interface ICometListener {

	public static final int COMET_NOTCONNECTED = 0;
	public static final int COMET_CONNECTING = 1;
	public static final int COMET_FRAMELOADTIMEOUT = 3;
	public static final int COMET_REGISTERED = 4;
	public static final int COMET_RECEIVING = 5;
	public static final int COMET_FINISHED = 6;

	/**
	 * This method is invoked each time an string payload is received from the
	 * server
	 * 
	 * @param object
	 */
	void onPayload(Object payload);

	/**
	 * This method is invoked when the server disconnects or terminates a comet
	 * session.
	 */
	void onTerminate();

	void onStatusChange(int status);

	void onReset();

	void onSyncStatusChange(int status);

}
