package com.vimukti.comet.server;

/**
 * An interface that unifies all the possible server messages to be sent to the
 * client.
 */
public interface Message {
	int getCommand();

	String getData();

	String getStream();
}
