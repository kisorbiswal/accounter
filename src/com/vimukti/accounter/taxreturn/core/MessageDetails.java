package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class MessageDetails {
	/**
	 * UnicodeNameString Element cardinality 1..1
	 */
	private String clazz = "messageDetails Class";
	/**
	 * Element cardinality 1..1
	 */
	private String qualifier = "Qualifier";
	/**
	 * Element cardinality 0..1
	 */
	private String function = "function";
	/**
	 * Element cardinality 0..1
	 */
	private String transactionID = "Transaction_ID";
	/**
	 * Element cardinality 0..1
	 */
	private String auditID = "Audit_ID";
	/**
	 * Element cardinality 0..1
	 */
	private String correlationID = "Correlation_ID";
	/**
	 * Element cardinality 0..1 PollInterval?
	 */
	private ResponseEndPoint responseEndPoint = new ResponseEndPoint();
	/**
	 * Element cardinality 0..1
	 */
	private String transformation = "Transformation";
	/**
	 * Element cardinality 0..1
	 */
	private int gatewayTest = 800;
	/**
	 * Element cardinality 0..1
	 */
	private String gatewayTimestamp = "Date And Time";

	public MessageDetails() {

	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getAuditID() {
		return auditID;
	}

	public void setAuditID(String auditID) {
		this.auditID = auditID;
	}

	public String getCorrelationID() {
		return correlationID;
	}

	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
	}

	public ResponseEndPoint getResponseEndPoint() {
		return responseEndPoint;
	}

	public void setResponseEndPoint(ResponseEndPoint responseEndPoint) {
		this.responseEndPoint = responseEndPoint;
	}

	public String getTransformation() {
		return transformation;
	}

	public void setTransformation(String transformation) {
		this.transformation = transformation;
	}

	public int getGatewayTest() {
		return gatewayTest;
	}

	public void setGatewayTest(int gatewayTest) {
		this.gatewayTest = gatewayTest;
	}

	public String getGatewayTimestamp() {
		return gatewayTimestamp;
	}

	public void setGatewayTimestamp(String gatewayTimestamp) {
		this.gatewayTimestamp = gatewayTimestamp;
	}

	public IXMLElement toXML() {
		XMLElement messageDetailselement = new XMLElement("MessageDetails");

		if (clazz != null) {
			XMLElement classElement = new XMLElement("Class", clazz);
			messageDetailselement.addChild(classElement);
		}
		if (qualifier != null) {
			XMLElement qualifierElement = new XMLElement("Qualifier", qualifier);
			messageDetailselement.addChild(qualifierElement);
		}
		if (function != null) {
			XMLElement functionElement = new XMLElement("Function", function);
			messageDetailselement.addChild(functionElement);
		}
		if (transactionID != null) {
			XMLElement transactionIdElement = new XMLElement("TransactionID",
					transactionID);
			messageDetailselement.addChild(transactionIdElement);
		}
		if (auditID != null) {
			XMLElement auditIDElement = new XMLElement("AuditID", auditID);
			messageDetailselement.addChild(auditIDElement);
		}

		if (correlationID != null) {
			XMLElement correlationIDElement = new XMLElement("CorrelationID",
					correlationID);
			messageDetailselement.addChild(correlationIDElement);
		}
		if (responseEndPoint != null) {
			messageDetailselement.addChild(responseEndPoint.toXML());
		}
		if (transformation != null) {
			XMLElement transformationElement = new XMLElement("Transformation",
					transformation);
			messageDetailselement.addChild(transformationElement);
		}
		if (gatewayTest != 0) {
			XMLElement gateWayTestElement = new XMLElement("GatewayTest",
					Integer.toString(gatewayTest));
			messageDetailselement.addChild(gateWayTestElement);
		}
		if (gatewayTimestamp != null) {
			XMLElement gatewayTimeStampElement = new XMLElement(
					"GatewayTimestamp", gatewayTimestamp);
			messageDetailselement.addChild(gatewayTimeStampElement);
		}
		return messageDetailselement;
	}

}
