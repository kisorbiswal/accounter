package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class VATDueOnOutputs {
	private ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType;

	public ElevenPointTwoDigitDecimalType getElevenPointTwoDigitDecimalType() {
		return elevenPointTwoDigitDecimalType;
	}

	public void setElevenPointTwoDigitDecimalType(
			ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType) {
		this.elevenPointTwoDigitDecimalType = elevenPointTwoDigitDecimalType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement vATDueOnOutputsElement = new XMLElement("VATDueOnOutputs");
		vATDeclarationRequestElement.addChild(vATDueOnOutputsElement);
		if (elevenPointTwoDigitDecimalType != null) {
			elevenPointTwoDigitDecimalType.toXML(vATDueOnOutputsElement);
		}
	}
}
