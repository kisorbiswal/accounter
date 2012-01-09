package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class IRenvelope {

	private IRheader iRheader;

	private VATDeclarationRequest vATDeclarationRequest;

	public IRheader getiRheader() {
		return iRheader;
	}

	public void setiRheader(IRheader iRheader) {
		this.iRheader = iRheader;
	}

	public VATDeclarationRequest getvATDeclarationRequest() {
		return vATDeclarationRequest;
	}

	public void setvATDeclarationRequest(
			VATDeclarationRequest vATDeclarationRequest) {
		this.vATDeclarationRequest = vATDeclarationRequest;
	}

	public void toXML(XMLElement element) {
		XMLElement iRenvelopeElement = new XMLElement("IRenvelope");
		element.addChild(iRenvelopeElement);

		if (iRheader != null) {
			iRheader.toXML(iRenvelopeElement);
		}
		if (vATDeclarationRequest != null) {
			vATDeclarationRequest.toXML(iRenvelopeElement);
		}
	}
}
