package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class AASBalancingPayment {
	private ElevenPointTwoDigitNonNegativeDecimalType elevenPointTwoDigitNonNegativeDecimalType;

	public ElevenPointTwoDigitNonNegativeDecimalType getElevenPointTwoDigitNonNegativeDecimalType() {
		return elevenPointTwoDigitNonNegativeDecimalType;
	}

	public void setElevenPointTwoDigitNonNegativeDecimalType(
			ElevenPointTwoDigitNonNegativeDecimalType elevenPointTwoDigitNonNegativeDecimalType) {
		this.elevenPointTwoDigitNonNegativeDecimalType = elevenPointTwoDigitNonNegativeDecimalType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement aASBalancingPayment = new XMLElement("AASBalancingPayment");
		vATDeclarationRequestElement.addChild(aASBalancingPayment);
		if (elevenPointTwoDigitNonNegativeDecimalType != null) {
			elevenPointTwoDigitNonNegativeDecimalType
					.toXML(aASBalancingPayment);
		}
	}
}
