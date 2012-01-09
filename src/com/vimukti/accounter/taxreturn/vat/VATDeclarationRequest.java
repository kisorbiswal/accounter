package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class VATDeclarationRequest {
	private VATDueOnOutputs vatDueOnOutputs;

	public VATDueOnOutputs getVatDueOnOutputs() {
		return vatDueOnOutputs;
	}

	public void setVatDueOnOutputs(VATDueOnOutputs vatDueOnOutputs) {
		this.vatDueOnOutputs = vatDueOnOutputs;
	}

	public void toXML(XMLElement iRenvelopeElement) {
		XMLElement vATDeclarationRequestElement = new XMLElement(
				"VATDeclarationRequest");
		iRenvelopeElement.addChild(vATDeclarationRequestElement);
		if (vatDueOnOutputs != null) {
			vatDueOnOutputs.toXML(vATDeclarationRequestElement);
		}
	}

}
