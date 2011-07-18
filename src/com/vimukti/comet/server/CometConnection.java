package com.vimukti.comet.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Convenient base class for any implementation of the CometConnection
 * interface. Instances of this class buffer messages until they are committed
 * by the CometServlet etc.
 * 
 */
public class CometConnection {

//	private String stream;

	public CometConnection() {

		this.setMessages(this.createMessages());
		this.setSequence(0);
	}

	/**
	 * Pushes a single object over this comet connection.
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public void push(final ObjectPayload payload) {
		if (payload == null)
			return;

		final long sequence = this.getSequence();
		payload.setSequence(sequence);
		this.pushMessage(payload);
		this.incrementSequence();
	}

	/**
	 * Sends a terminate message to the client. All subsequent pushes will fail
	 * and this session will be terminated.
	 */
	public void terminate() {
		final long sequence = this.getSequence();
		this.pushMessage(new Terminate(sequence));
		this.setTerminated(true);
		this.incrementSequence();
	}

	protected void terminatedGuard() {
		if (this.isTerminated()) {
			throw new IllegalStateException(
					"This comet connect has already been terminated, no further payloads may be sent.");
		}
	}

	/**
	 * This flag tracks whether a connection has been terminated.
	 */
	private boolean terminated;

	protected boolean isTerminated() {
		return this.terminated;
	}

	protected void setTerminated(final boolean terminated) {
		this.terminated = terminated;
	}

	protected void pushMessage(final Message message) {
		// Checker.notNull("parameter:message", message);
		this.terminatedGuard();

		this.getMessages().add(message);
	}

	/**
	 * This list accumulates any messages that will be sent to the client.
	 */
	protected List<Message> messages;

	protected List<Message> getMessages() {
		return this.messages;
	}

	protected void setMessages(final List<Message> messages) {
		this.messages = messages;
	}

	protected List<Message> createMessages() {
		return new ArrayList<Message>();
	}

	/**
	 * A sequence number is included in all payloads as a means to ensure
	 * payloads arent lost or delivered twice.
	 */
	private long sequence;

	public long getSequence() {
		return this.sequence;
	}

	void setSequence(final long sequence) {
		this.sequence = sequence;
	}

	void incrementSequence() {
		this.setSequence(this.getSequence() + 1);
	}

}
