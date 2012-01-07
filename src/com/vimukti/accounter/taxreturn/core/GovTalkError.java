package com.vimukti.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GovTalkError {
	/**
	 * 1..1
	 */
	private String raisedBy = "Raised By";
	/**
	 * 0..1
	 */
	private int number = 22;
	/**
	 * 1..1
	 */
	private String type = "Gov Talk Error Type";
	/**
	 * 0..∞
	 */
	private List<String> texts = new ArrayList<String>();
	/**
	 * 0..∞
	 */
	private List<String> locations = new ArrayList<String>();

	public GovTalkError() {
		getTexts().add("Gov Talk Error Text");
		getLocations().add("Gov Talk Error Location");
	}

	public String getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getTexts() {
		return texts;
	}

	public void setTexts(List<String> texts) {
		this.texts = texts;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public IXMLElement toXML() {
		XMLElement govTalkErrorElement = new XMLElement("GovTalkError");

		XMLElement raisedByElement = new XMLElement("RaisedBy", raisedBy);
		XMLElement numberElement = new XMLElement("Number",
				Integer.toString(number));
		XMLElement typeElement = new XMLElement("Type", type);

		govTalkErrorElement.addChild(raisedByElement);
		govTalkErrorElement.addChild(numberElement);
		govTalkErrorElement.addChild(typeElement);
		for (String text : texts) {
			XMLElement textElement = new XMLElement("Text", text);
			govTalkErrorElement.addChild(textElement);
		}
		for (String location : locations) {
			XMLElement locationElement = new XMLElement("Location", location);
			govTalkErrorElement.addChild(locationElement);
		}
		return govTalkErrorElement;
	}

}
