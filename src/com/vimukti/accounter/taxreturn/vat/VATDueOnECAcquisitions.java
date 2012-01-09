package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class VATDueOnECAcquisitions {
	private ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType;

	public ElevenPointTwoDigitDecimalType getElevenPointTwoDigitDecimalType() {
		return elevenPointTwoDigitDecimalType;
	}

	public void setElevenPointTwoDigitDecimalType(
			ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType) {
		this.elevenPointTwoDigitDecimalType = elevenPointTwoDigitDecimalType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement vATDueOnECAcquisitionsElement = new XMLElement(
				"VATDueOnECAcquisitions");
		vATDeclarationRequestElement.addChild(vATDueOnECAcquisitionsElement);
		if (elevenPointTwoDigitDecimalType != null) {
			elevenPointTwoDigitDecimalType.toXML(vATDueOnECAcquisitionsElement);
		}
	}
}
