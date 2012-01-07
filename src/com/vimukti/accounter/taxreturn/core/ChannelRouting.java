package com.vimukti.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

;

public class ChannelRouting {
	/**
	 * 1..1
	 */
	private Channel channel = new Channel();
	/**
	 * 0..∞
	 */
	private List<String> iDs = new ArrayList<String>();
	/**
	 * 1..1
	 */
	private String timestamp = "Date and time";

	public ChannelRouting() {
		getiDs().add("Id");
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public List<String> getiDs() {
		return iDs;
	}

	public void setiDs(List<String> iDs) {
		this.iDs = iDs;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
