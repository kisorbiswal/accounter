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

		XMLElement classElement = new XMLElement("Class", getClazz());
		XMLElement qualifierElement = new XMLElement("Qualifier",
				getQualifier());
		XMLElement functionElement = new XMLElement("Function", getFunction());
		XMLElement transactionIdElement = new XMLElement("TransactionID",
				getTransactionID());
		XMLElement auditIDElement = new XMLElement("AuditID", getAuditID());
		XMLElement correlationIDElement = new XMLElement("CorrelationID",
				getCorrelationID());
		XMLElement transformationElement = new XMLElement("Transformation",
				getTransformation());
		XMLElement gateWayTestElement = new XMLElement("GatewayTest",
				Integer.toString(getGatewayTest()));
		XMLElement gatewayTimeStampElement = new XMLElement("GatewayTimestamp",
				getGatewayTimestamp());

		messageDetailselement.addChild(classElement);
		messageDetailselement.addChild(qualifierElement);
		messageDetailselement.addChild(functionElement);
		messageDetailselement.addChild(transactionIdElement);
		messageDetailselement.addChild(auditIDElement);
		messageDetailselement.addChild(correlationIDElement);
		messageDetailselement.addChild(getResponseEndPoint().toXML());
		messageDetailselement.addChild(transformationElement);
		messageDetailselement.addChild(gateWayTestElement);
		messageDetailselement.addChild(gatewayTimeStampElement);

		return messageDetailselement;
	}

}
