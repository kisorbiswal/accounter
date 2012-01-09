package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

public class VATDeclarationRequest {
	private double vatDueOnOutputs;
	private double vatDueOnECAcquisitions;
	private double totalVAT;
	private double vatReclaimedOnInputs;
	private double netVAT;
	private int netSalesAndOutputs;
	private int netPurchasesAndInputs;
	private int netECSupplies;
	private int netECAcquisitions;
	private int aasBalancingPayment;
	private String extensionPart;
	private YesNoType finalReturn;

	public double getVatDueOnOutputs() {
		return vatDueOnOutputs;
	}

	public void setVatDueOnOutputs(double vatDueOnOutputs) {
		this.vatDueOnOutputs = vatDueOnOutputs;
	}

	public double getVatDueOnECAcquisitions() {
		return vatDueOnECAcquisitions;
	}

	public void setVatDueOnECAcquisitions(double vatDueOnECAcquisitions) {
		this.vatDueOnECAcquisitions = vatDueOnECAcquisitions;
	}

	public double getTotalVAT() {
		return totalVAT;
	}

	public void setTotalVAT(double totalVAT) {
		this.totalVAT = totalVAT;
	}

	public double getVatReclaimedOnInputs() {
		return vatReclaimedOnInputs;
	}

	public void setVatReclaimedOnInputs(double vatReclaimedOnInputs) {
		this.vatReclaimedOnInputs = vatReclaimedOnInputs;
	}

	public double getNetVAT() {
		return netVAT;
	}

	public void setNetVAT(double netVAT) {
		this.netVAT = netVAT;
	}

	public int getNetSalesAndOutputs() {
		return netSalesAndOutputs;
	}

	public void setNetSalesAndOutputs(int netSalesAndOutputs) {
		this.netSalesAndOutputs = netSalesAndOutputs;
	}

	public int getNetPurchasesAndInputs() {
		return netPurchasesAndInputs;
	}

	public void setNetPurchasesAndInputs(int netPurchasesAndInputs) {
		this.netPurchasesAndInputs = netPurchasesAndInputs;
	}

	public int getNetECSupplies() {
		return netECSupplies;
	}

	public void setNetECSupplies(int netECSupplies) {
		this.netECSupplies = netECSupplies;
	}

	public int getAasBalancingPayment() {
		return aasBalancingPayment;
	}

	public void setAasBalancingPayment(int aasBalancingPayment) {
		this.aasBalancingPayment = aasBalancingPayment;
	}

	public int getNetECAcquisitions() {
		return netECAcquisitions;
	}

	public void setNetECAcquisitions(int netECAcquisitions) {
		this.netECAcquisitions = netECAcquisitions;
	}

	public String getExtensionPart() {
		return extensionPart;
	}

	public void setExtensionPart(String extensionPart) {
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
		if (vatDueOnOutputs != 0) {
			XMLElement vATDueOnOutputsElement = new XMLElement(
					"VATDueOnOutputs");
			vATDueOnOutputsElement.setContent(Double.toString(vatDueOnOutputs));
			vATDeclarationRequestElement.addChild(vATDueOnOutputsElement);
		}
		if (vatDueOnECAcquisitions != 0) {
			XMLElement vATDueOnECAcquisitionsElement = new XMLElement(
					"VATDueOnECAcquisitions");
			vATDueOnECAcquisitionsElement.setContent(Double
					.toString(vatDueOnECAcquisitions));
			vATDeclarationRequestElement
					.addChild(vATDueOnECAcquisitionsElement);
		}
		if (totalVAT != 0) {
			XMLElement totalVATElement = new XMLElement("TotalVAT");
			totalVATElement.setContent(Double.toString(totalVAT));
			vATDeclarationRequestElement.addChild(totalVATElement);
		}
		if (vatReclaimedOnInputs != 0) {
			XMLElement vatReclaimedOnInputsElement = new XMLElement(
					"VATReclaimedOnInputs");
			vatReclaimedOnInputsElement.setContent(Double
					.toString(vatReclaimedOnInputs));
			vATDeclarationRequestElement.addChild(vatReclaimedOnInputsElement);
		}
		if (netVAT != 0) {
			XMLElement netVATElement = new XMLElement("NetVAT");
			netVATElement.setContent(Double.toString(netVAT));
			vATDeclarationRequestElement.addChild(netVATElement);
		}
		if (netSalesAndOutputs != 0) {
			XMLElement netSalesAndOutputsElement = new XMLElement(
					"NetSalesAndOutputs");
			netSalesAndOutputsElement.setContent(Integer
					.toString(netSalesAndOutputs));
			vATDeclarationRequestElement.addChild(netSalesAndOutputsElement);
		}
		if (netPurchasesAndInputs != 0) {
			XMLElement netPurchasesAndInputsElement = new XMLElement(
					"NetPurchasesAndInputs");
			netPurchasesAndInputsElement.setContent(Integer
					.toString(netPurchasesAndInputs));
			vATDeclarationRequestElement.addChild(netPurchasesAndInputsElement);
		}
		if (netECSupplies != 0) {
			XMLElement netECSuppliesElement = new XMLElement("NetECSupplies");
			netECSuppliesElement.setContent(Integer.toString(netECSupplies));
			vATDeclarationRequestElement.addChild(netECSuppliesElement);
		}
		if (netECAcquisitions != 0) {
			XMLElement netECAcquisitionsElement = new XMLElement(
					"NetECAcquisitions");
			netECAcquisitionsElement.setContent(Integer
					.toString(netECAcquisitions));
			vATDeclarationRequestElement.addChild(netECAcquisitionsElement);
		}
		if (aasBalancingPayment != 0) {
			XMLElement aasBalancingPaymentElement = new XMLElement(
					"AASBalancingPayment");
			aasBalancingPaymentElement.setContent(Integer
					.toString(aasBalancingPayment));
			vATDeclarationRequestElement.addChild(aasBalancingPaymentElement);
		}
		if (extensionPart != null) {
			XMLElement extensionPartElement = new XMLElement("ExtensionPart");
			extensionPartElement.setContent(extensionPart);
			vATDeclarationRequestElement.addChild(extensionPartElement);
		}
		if (finalReturn != null) {
			finalReturn.toXML(vATDeclarationRequestElement, "FinalReturn");
		}
	}

}
