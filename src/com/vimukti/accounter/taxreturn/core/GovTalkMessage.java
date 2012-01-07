package com.vimukti.accounter.taxreturn.core;

import java.io.OutputStream;

import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLWriter;

public class GovTalkMessage {
	/**
	 * Element cardinality 1..1
	 */
	private String envelopVersion;
	/**
	 * Element cardinality 1..1
	 */
	private Header header = new Header();
	/**
	 * Element cardinality 1..1
	 */
	private GovtTalkDetails govtTalkDetails = new GovtTalkDetails();
	/**
	 * Element cardinality 0..1
	 */
	private Body body = new Body();

	public String getEnvelopVersion() {
		return envelopVersion;
	}

	public void setEnvelopVersion(String envelopVersion) {
		this.envelopVersion = envelopVersion;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public GovtTalkDetails getGovtTalkDetails() {
		return govtTalkDetails;
	}

	public void setGovtTalkDetails(GovtTalkDetails govtTalkDetails) {
		this.govtTalkDetails = govtTalkDetails;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public void toXML(OutputStream stream, boolean body) throws Exception {
		XMLWriter writer = new XMLWriter(stream);

		XMLElement element = new XMLElement("GovTalkMessage");
		element.setAttribute("xmlns", "http://www.govtalk.gov.uk/CM/envelope");

		XMLElement envelopElement = new XMLElement("EnvelopVersion",
				getEnvelopVersion());

		element.addChild(envelopElement);
		element.addChild(getHeader().toXML());
		// element.addChild(getGovtTalkDetails().toXML());
		if (body) {
			// element.addChild(getBody().toXML());
		} else {
			element.addChild(new XMLElement("Body"));
		}
		writer.write(element);
		stream.flush();
		stream.close();
	}

}
