package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GatewayValidation {
	/**
	 * 1..1
	 */
	private String processed = "Processed";
	/**
	 * 1..1
	 */
	private String result = "result";

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public IXMLElement toXML() {
		XMLElement gatewayValidationElement = new XMLElement(
				"GatewayValidation");
		XMLElement processedElement = new XMLElement("Processed", processed);
		XMLElement resultElement = new XMLElement("Result", result);
		gatewayValidationElement.addChild(processedElement);
		gatewayValidationElement.addChild(resultElement);
		return gatewayValidationElement;
	}
}
