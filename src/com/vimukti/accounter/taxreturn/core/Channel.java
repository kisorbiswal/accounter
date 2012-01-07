package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class Channel {
	/**
	 * 1..1
	 */
	private String uRI = "Uri";
	/**
	 * 1..1
	 */
	private String name = "Channel Name";
	/**
	 * 0..1
	 */
	private String product = "Channel Product";
	/**
	 * 0..1
	 */
	private String version = "Channel Version";

	public String getuRI() {
		return uRI;
	}

	public void setuRI(String uRI) {
		this.uRI = uRI;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public IXMLElement toXML() {
		XMLElement channelElement = new XMLElement("Channel");

		XMLElement uriElement = new XMLElement("URI", uRI);
		XMLElement nameElement = new XMLElement("Name", name);
		XMLElement productElement = new XMLElement("Product", product);
		XMLElement versionElement = new XMLElement("Version", version);

		channelElement.addChild(uriElement);
		channelElement.addChild(nameElement);
		channelElement.addChild(productElement);
		channelElement.addChild(versionElement);

		return channelElement;
	}
}
