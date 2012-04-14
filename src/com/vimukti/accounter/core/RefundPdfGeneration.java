package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.DataUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class RefundPdfGeneration {

	private final CustomerRefund refund;
	private final Company company;

	public RefundPdfGeneration(CustomerRefund refund, Company company) {
		this.refund = refund;
		this.company = company;
	}

	public IContext assignValues(IContext context, IXDocReport report) {
		try {
			CustomerRefundTemplate template = new CustomerRefundTemplate();
			template.setTitle(Global.get().Customer() + " Refund");
			template.setPayTo(refund.getPayTo().getName());
			template.setPayFrom(refund.getPayFrom().getName());
			template.setNumber(refund.getNumber());

			Address address = refund.getAddress();
			String customerNAddress = "";
			if (address != null) {
				customerNAddress = forAddress(address.getAddress1(), false)
						+ forAddress(address.getStreet(), false)
						+ forAddress(address.getCity(), false)
						+ forAddress(address.getStateOrProvinence(), false)
						+ forAddress(address.getZipOrPostalCode(), false)
						+ forAddress(address.getCountryOrRegion(), true);
			} else {
				customerNAddress = "";
			}
			template.setCustomerNAddress(refund.getPayTo().getName()
					+ customerNAddress);

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

			Currency currency = refund.getPayTo() != null ? refund.getPayTo()
					.getCurrency() : refund.getCurrency();
			String symbol = currency.getSymbol();

			template.setRefundAmount(DataUtils.getAmountAsStringInCurrency(
					refund.getTotal(), symbol));
			template.setMemo(refund.getMemo());
			template.setCurrency(currency.getFormalName());

			context.put("refund", template);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
		private String memo;
		private String currency;
		private String customerNAddress;
		private String number;

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

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getCustomerNAddress() {
			return customerNAddress;
		}

		public void setCustomerNAddress(String customerNAddress) {
			this.customerNAddress = customerNAddress;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
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
