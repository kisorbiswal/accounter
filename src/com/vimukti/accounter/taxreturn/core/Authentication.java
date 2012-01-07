package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class Authentication {
	/**
	 * Element cardinality 1..1
	 */
	private String method;
	/**
	 * Element cardinality 0..1
	 */
	private String role = "Role";

	/**
	 * choice 1 Element cardinality 1..1
	 */
	private String value = "Value";
	/**
	 * choice 1 Element cardinality 1..1
	 */
	private Signature signature = new Signature();

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public IXMLElement toXML() {

		XMLElement authenticationElement = new XMLElement("Authentication");

		XMLElement methodElement = new XMLElement("Method", getMethod());
		XMLElement roleElement = new XMLElement("Role", getRole());

		authenticationElement.addChild(methodElement);
		authenticationElement.addChild(roleElement);
		if (getValue() != null) {
			XMLElement valueElement = new XMLElement("Value", getValue());
			authenticationElement.addChild(valueElement);
		}
		if (getSignature() != null) {
			authenticationElement.addChild(getSignature().toXML());
		}

		return authenticationElement;
	}

}
