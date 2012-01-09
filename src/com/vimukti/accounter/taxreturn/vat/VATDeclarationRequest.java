package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class VATDeclarationRequest {
	private VATDueOnOutputs vatDueOnOutputs;
	private VATDueOnECAcquisitions vatDueOnECAcquisitions;
	private TotalVAT totalVAT;
	private VATReclaimedOnInputs vatReclaimedOnInputs;
	private NetVAT netVAT;
	private NetSalesAndOutputs netSalesAndOutputs;
	private NetPurchasesAndInputs netPurchasesAndInputs;
	private NetECSupplies netECSupplies;
	private NetECAcquisitions netECAcquisitions;
	private AASBalancingPayment aasBalancingPayment;
	private ExtensionPart extensionPart;
	private YesNoType finalReturn;

	public VATDueOnOutputs getVatDueOnOutputs() {
		return vatDueOnOutputs;
	}

	public void setVatDueOnOutputs(VATDueOnOutputs vatDueOnOutputs) {
		this.vatDueOnOutputs = vatDueOnOutputs;
	}

	public VATDueOnECAcquisitions getVatDueOnECAcquisitions() {
		return vatDueOnECAcquisitions;
	}

	public void setVatDueOnECAcquisitions(
			VATDueOnECAcquisitions vatDueOnECAcquisitions) {
		this.vatDueOnECAcquisitions = vatDueOnECAcquisitions;
	}

	public TotalVAT getTotalVAT() {
		return totalVAT;
	}

	public void setTotalVAT(TotalVAT totalVAT) {
		this.totalVAT = totalVAT;
	}

	public VATReclaimedOnInputs getVatReclaimedOnInputs() {
		return vatReclaimedOnInputs;
	}

	public void setVatReclaimedOnInputs(
			VATReclaimedOnInputs vatReclaimedOnInputs) {
		this.vatReclaimedOnInputs = vatReclaimedOnInputs;
	}

	public NetVAT getNetVAT() {
		return netVAT;
	}

	public void setNetVAT(NetVAT netVAT) {
		this.netVAT = netVAT;
	}

	public NetSalesAndOutputs getNetSalesAndOutputs() {
		return netSalesAndOutputs;
	}

	public void setNetSalesAndOutputs(NetSalesAndOutputs netSalesAndOutputs) {
		this.netSalesAndOutputs = netSalesAndOutputs;
	}

	public NetPurchasesAndInputs getNetPurchasesAndInputs() {
		return netPurchasesAndInputs;
	}

	public void setNetPurchasesAndInputs(
			NetPurchasesAndInputs netPurchasesAndInputs) {
		this.netPurchasesAndInputs = netPurchasesAndInputs;
	}

	public NetECSupplies getNetECSupplies() {
		return netECSupplies;
	}

	public void setNetECSupplies(NetECSupplies netECSupplies) {
		this.netECSupplies = netECSupplies;
	}

	public NetECAcquisitions getNetECAcquisitions() {
		return netECAcquisitions;
	}

	public void setNetECAcquisitions(NetECAcquisitions netECAcquisitions) {
		this.netECAcquisitions = netECAcquisitions;
	}

	public AASBalancingPayment getAasBalancingPayment() {
		return aasBalancingPayment;
	}

	public void setAasBalancingPayment(AASBalancingPayment aasBalancingPayment) {
		this.aasBalancingPayment = aasBalancingPayment;
	}

	public ExtensionPart getExtensionPart() {
		return extensionPart;
	}

	public void setExtensionPart(ExtensionPart extensionPart) {
		this.extensionPart = extensionPart;
	}

	public YesNoType getFinalReturn() {
		return finalReturn;
	}

	public void setFinalReturn(YesNoType finalReturn) {
		this.finalReturn = finalReturn;
	}

	public void toXML(XMLElement iRenvelopeElement) {
		XMLElement vATDeclarationRequestElement = new XMLElement(
				"VATDeclarationRequest");
		iRenvelopeElement.addChild(vATDeclarationRequestElement);
		if (vatDueOnOutputs != null) {
			vatDueOnOutputs.toXML(vATDeclarationRequestElement);
		}
		if (vatDueOnECAcquisitions != null) {
			vatDueOnECAcquisitions.toXML(vATDeclarationRequestElement);
		}
		if (totalVAT != null) {
			totalVAT.toXML(vATDeclarationRequestElement);
		}
		if (vatReclaimedOnInputs != null) {
			vatReclaimedOnInputs.toXML(vATDeclarationRequestElement);
		}
		if (netVAT != null) {
			netVAT.toXML(vATDeclarationRequestElement);
		}
		if (netSalesAndOutputs != null) {
			netSalesAndOutputs.toXML(vATDeclarationRequestElement);
		}
		if (netPurchasesAndInputs != null) {
			netPurchasesAndInputs.toXML(vATDeclarationRequestElement);
		}
		if (netECSupplies != null) {
			netECSupplies.toXML(vATDeclarationRequestElement);
		}
		if (netECAcquisitions != null) {
			netECAcquisitions.toXML(vATDeclarationRequestElement);
		}
		if (aasBalancingPayment != null) {
			aasBalancingPayment.toXML(vATDeclarationRequestElement);
		}
		if (extensionPart != null) {
			extensionPart.toXML(vATDeclarationRequestElement);
		}
		if (finalReturn != null) {
			finalReturn.toXML(vATDeclarationRequestElement, "FinalReturn");
		}
	}
}
