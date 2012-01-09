package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class NetECAcquisitions {
	private ThirteenDigitIntegerType thirteenDigitIntegerType;

	public ThirteenDigitIntegerType getThirteenDigitIntegerType() {
		return thirteenDigitIntegerType;
	}

	public void setThirteenDigitIntegerType(
			ThirteenDigitIntegerType thirteenDigitIntegerType) {
		this.thirteenDigitIntegerType = thirteenDigitIntegerType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement netECAcquisitionsElement = new XMLElement(
				"NetECAcquisitions");
		vATDeclarationRequestElement.addChild(netECAcquisitionsElement);
		if (thirteenDigitIntegerType != null) {
			thirteenDigitIntegerType.toXML(netECAcquisitionsElement);
		}
	}
}
