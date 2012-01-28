package com.vimukti.accounter.core;

public class ReceivePaymentPdfGeneration {

	private ReceivePayment receive;
	private Company company;

	public ReceivePaymentPdfGeneration(ReceivePayment receive, Company company) {
		this.receive = receive;
		this.company = company;
	}
}
