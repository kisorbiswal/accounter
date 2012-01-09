package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class NetECSupplies {
	private ThirteenDigitIntegerType thirteenDigitIntegerType;

	public ThirteenDigitIntegerType getThirteenDigitIntegerType() {
		return thirteenDigitIntegerType;
	}

	public void setThirteenDigitIntegerType(
			ThirteenDigitIntegerType thirteenDigitIntegerType) {
		this.thirteenDigitIntegerType = thirteenDigitIntegerType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement netECSuppliesElement = new XMLElement("NetECSupplies");
		vATDeclarationRequestElement.addChild(netECSuppliesElement);
		if (thirteenDigitIntegerType != null) {
			thirteenDigitIntegerType.toXML(netECSuppliesElement);
		}
	}
}
