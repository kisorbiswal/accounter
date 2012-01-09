package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class TotalVAT {
	private ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType;

	public ElevenPointTwoDigitDecimalType getElevenPointTwoDigitDecimalType() {
		return elevenPointTwoDigitDecimalType;
	}

	public void setElevenPointTwoDigitDecimalType(
			ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType) {
		this.elevenPointTwoDigitDecimalType = elevenPointTwoDigitDecimalType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement totalVATElement = new XMLElement("TotalVAT");
		vATDeclarationRequestElement.addChild(totalVATElement);
		if (elevenPointTwoDigitDecimalType != null) {
			elevenPointTwoDigitDecimalType.toXML(totalVATElement);
		}
	}
}
