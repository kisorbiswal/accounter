package com.vimukti.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class ChannelRouting {
	/**
	 * 1..1
	 */
	private Channel channel = new Channel();
	/**
	 * 0..∞
	 */
	private List<ID> iDs = new ArrayList<ID>();
	/**
	 * 1..1
	 */
	private String timestamp = "Date and time";

	public ChannelRouting() {
		getiDs().add(new ID());
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public List<ID> getiDs() {
		return iDs;
	}

	public void setiDs(List<ID> iDs) {
		this.iDs = iDs;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public IXMLElement toXML() {
		XMLElement channelRoutingElement = new XMLElement("ChannelRouting");

		channelRoutingElement.addChild(channel.toXML());
		for (ID id : iDs) {
			channelRoutingElement.addChild(id.toXML());
		}
		XMLElement timestampElement = new XMLElement("Timestamp", timestamp);
		channelRoutingElement.addChild(timestampElement);
		return channelRoutingElement;
	}
}
