package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class NetPurchasesAndInputs {
	private ThirteenDigitIntegerType thirteenDigitIntegerType;

	public ThirteenDigitIntegerType getThirteenDigitIntegerType() {
		return thirteenDigitIntegerType;
	}

	public void setThirteenDigitIntegerType(
			ThirteenDigitIntegerType thirteenDigitIntegerType) {
		this.thirteenDigitIntegerType = thirteenDigitIntegerType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement netPurchasesAndInputsElement = new XMLElement(
				"NetPurchasesAndInputs");
		vATDeclarationRequestElement.addChild(netPurchasesAndInputsElement);
		if (thirteenDigitIntegerType != null) {
			thirteenDigitIntegerType.toXML(netPurchasesAndInputsElement);
		}
	}

}
