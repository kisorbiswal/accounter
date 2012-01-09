package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class VATReclaimedOnInputs {
	private ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType;

	public ElevenPointTwoDigitDecimalType getElevenPointTwoDigitDecimalType() {
		return elevenPointTwoDigitDecimalType;
	}

	public void setElevenPointTwoDigitDecimalType(
			ElevenPointTwoDigitDecimalType elevenPointTwoDigitDecimalType) {
		this.elevenPointTwoDigitDecimalType = elevenPointTwoDigitDecimalType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement vATReclaimedOnInputs = new XMLElement("VATReclaimedOnInputs");
		vATDeclarationRequestElement.addChild(vATReclaimedOnInputs);
		if (elevenPointTwoDigitDecimalType != null) {
			elevenPointTwoDigitDecimalType.toXML(vATReclaimedOnInputs);
		}
	}
}
