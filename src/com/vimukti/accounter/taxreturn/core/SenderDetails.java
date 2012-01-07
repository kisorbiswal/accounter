package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class SenderDetails {
	/**
	 * Element cardinality 0..1
	 */
	private IDAuthentication iDAuthentication = new IDAuthentication();
	/**
	 * Element cardinality 0..1
	 */
	private long x509Certificate = 988764;
	/**
	 * Element cardinality 0..1
	 */
	private String emailAddress = "***REMOVED***";

	public IDAuthentication getiDAuthentication() {
		return iDAuthentication;
	}

	public void setiDAuthentication(IDAuthentication iDAuthentication) {
		this.iDAuthentication = iDAuthentication;
	}

	public long getX509Certificate() {
		return x509Certificate;
	}

	public void setX509Certificate(long x509Certificate) {
		this.x509Certificate = x509Certificate;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public IXMLElement toXML() {
		XMLElement senderDatailsElement = new XMLElement("SenderDetails");

		XMLElement x509CertificateElement = new XMLElement("x509Certificate",
				Long.toString(getX509Certificate()));
		XMLElement emailAddressElement = new XMLElement("EmailAddress",
				getEmailAddress());

		senderDatailsElement.addChild(getiDAuthentication().toXML());
		senderDatailsElement.addChild(x509CertificateElement);
		senderDatailsElement.addChild(emailAddressElement);

		return senderDatailsElement;
	}
}
