package com.vimukti.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GovTalkError {
	/**
	 * 1..1
	 */
	private String raisedBy;
	/**
	 * 0..1
	 */
	private int number;
	/**
	 * 1..1
	 */
	private String type;
	/**
	 * 0..∞
	 */
	private List<String> texts = new ArrayList<String>();
	/**
	 * 0..∞
	 */
	private List<String> locations = new ArrayList<String>();

	public GovTalkError() {
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
		if (raisedBy != null) {
			XMLElement raisedByElement = new XMLElement("RaisedBy");
			raisedByElement.setContent(raisedBy);
			govTalkErrorElement.addChild(raisedByElement);
		}
		if (number != 0) {
			XMLElement numberElement = new XMLElement("Number");
			numberElement.setContent(Integer.toString(number));
			govTalkErrorElement.addChild(numberElement);
		}
		if (type != null) {
			XMLElement typeElement = new XMLElement("Type");
			typeElement.setContent(type);
			govTalkErrorElement.addChild(typeElement);
		}
		if (texts != null) {
			for (String text : texts) {
				XMLElement textElement = new XMLElement("Text");
				textElement.setContent(text);
				govTalkErrorElement.addChild(textElement);
			}
		}
		if (locations != null) {
			for (String location : locations) {
				XMLElement locationElement = new XMLElement("Location");
				locationElement.setContent(location);
				govTalkErrorElement.addChild(locationElement);
			}
		}
		return govTalkErrorElement;
	}

}
