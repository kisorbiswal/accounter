package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class NetVAT {
	private ElevenPointTwoDigitNonNegativeDecimalType elevenPointTwoDigitNonNegativeDecimalType;

	public ElevenPointTwoDigitNonNegativeDecimalType getElevenPointTwoDigitNonNegativeDecimalType() {
		return elevenPointTwoDigitNonNegativeDecimalType;
	}

	public void setElevenPointTwoDigitNonNegativeDecimalType(
			ElevenPointTwoDigitNonNegativeDecimalType elevenPointTwoDigitNonNegativeDecimalType) {
		this.elevenPointTwoDigitNonNegativeDecimalType = elevenPointTwoDigitNonNegativeDecimalType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement netVatElement = new XMLElement("NetVAT");
		vATDeclarationRequestElement.addChild(netVatElement);
		if (elevenPointTwoDigitNonNegativeDecimalType != null) {
			elevenPointTwoDigitNonNegativeDecimalType.toXML(netVatElement);
		}
	}
}
