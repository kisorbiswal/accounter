package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GatewayAdditions {
	private String value = "GatewayAdditions Value";
	private String type = "Gateway Addition Type";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IXMLElement toXML() {
		XMLElement gatewayAdditions;
		if (value != null) {
			gatewayAdditions = new XMLElement("GatewayAdditions", value);
		} else {
			gatewayAdditions = new XMLElement("GatewayAdditions");
		}
		if (type != null) {
			gatewayAdditions.setAttribute("type", type);
		}
		return gatewayAdditions;
	}
}
