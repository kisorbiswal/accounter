package com.vimukti.accounter.core;

import java.io.File;

import com.vimukti.accounter.web.client.ui.DataUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;

public class RefundPdfGeneration {

	private final CustomerRefund refund;
	private final Company company;

	public RefundPdfGeneration(CustomerRefund refund, Company company) {
		this.refund = refund;
		this.company = company;
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		CustomerRefundTemplate template = new CustomerRefundTemplate();
		IImageProvider footerImg = new ClassPathImageProvider(
				InvoicePdfGeneration.class, "templetes" + File.separator
						+ "footer-print-img.jpg");
		template.setTitle("Refund");
		template.setPayTo(refund.getPayTo().getName());
		template.setPayFrom(refund.getPayFrom().getName());
		Address regAdr = company.getRegisteredAddress();

		String regAddress = forAddress(regAdr.getAddress1(), false)
				+ forAddress(regAdr.getStreet(), false)
				+ forAddress(regAdr.getCity(), false)
				+ forAddress(regAdr.getStateOrProvinence(), false)
				+ forAddress(regAdr.getZipOrPostalCode(), false)
				+ forAddress(regAdr.getCountryOrRegion(), true);
		template.setRegisteredAddress(regAddress);
		template.setDate(refund.getDate().toString());
		template.setPaymentMethod(refund.getPaymentMethod());
		template.setChequeOrRefNo(refund.getCheckNumber());
		String symbol = refund.getCurrency().getSymbol();
		template.setRefundAmount(DataUtils.getAmountAsStringInCurrency(
				refund.getTotal(), symbol));
		context.put("refund", template);
		context.put("companyImg", footerImg);
		return context;

	}

	public class CustomerRefundTemplate {

		private String payTo;
		private String payFrom;
		private String registeredAddress;
		private String date;
		private String paymentMethod;
		private String chequeOrRefNo;
		private String refundAmount;
		private String title;

		public String getPayTo() {
			return payTo;
		}

		public void setPayTo(String payTo) {
			this.payTo = payTo;
		}

		public String getPayFrom() {
			return payFrom;
		}

		public void setPayFrom(String payFrom) {
			this.payFrom = payFrom;
		}

		public String getRegisteredAddress() {
			return registeredAddress;
		}

		public void setRegisteredAddress(String registeredAddress) {
			this.registeredAddress = registeredAddress;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public String getChequeOrRefNo() {
			return chequeOrRefNo;
		}

		public void setChequeOrRefNo(String chequeOrRefNo) {
			this.chequeOrRefNo = chequeOrRefNo;
		}

		public String getRefundAmount() {
			return refundAmount;
		}

		public void setRefundAmount(String refundAmount) {
			this.refundAmount = refundAmount;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	}

	private String forAddress(String address, boolean isFooter) {
		if (address == null) {
			return "";
		}
		if (address.trim().length() == 0) {
			return "";
		}
		if (isFooter) {
			return address + "." + "\n";
		} else {
			return address + "," + "\n";
		}
	}

}
