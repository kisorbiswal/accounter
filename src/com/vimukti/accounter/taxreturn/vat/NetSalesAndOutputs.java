package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class NetSalesAndOutputs {
	private ThirteenDigitIntegerType thirteenDigitIntegerType;

	public ThirteenDigitIntegerType getThirteenDigitIntegerType() {
		return thirteenDigitIntegerType;
	}

	public void setThirteenDigitIntegerType(
			ThirteenDigitIntegerType thirteenDigitIntegerType) {
		this.thirteenDigitIntegerType = thirteenDigitIntegerType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement netSalesAndOutputsElement = new XMLElement(
				"NetSalesAndOutputs");
		vATDeclarationRequestElement.addChild(netSalesAndOutputsElement);
		if (thirteenDigitIntegerType != null) {
			thirteenDigitIntegerType.toXML(netSalesAndOutputsElement);
		}
	}
}
