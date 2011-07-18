package com.vimukti.comet.server;

public class ObjectPayload implements Message {

	public ObjectPayload(final String data, String stream) {
		this.data = data;
		this.stream = stream;
	}

	public int getCommand() {
		return Constants.OBJECT_PAYLOAD;
	}

	private String data;

	public String getData() {
		return data;
	}

	private long sequence;
	private String stream;

	public long getSequence() {
		return this.sequence;
	}

	void setSequence(final long sequence) {
		this.sequence = sequence;
	}

	public String getStream() {
		return stream;
	}

}