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
			template.setCustomerNAddress(getAddress());
			template.setRegisteredAddress(getRegisteredAddress());
			template.setDate(refund.getDate().toString());
			template.setPaymentMethod(refund.getPaymentMethod());
			template.setChequeOrRefNo(refund.getCheckNumber());

			Currency currency = refund.getCurrency();
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

	private String getAddress() {
		Address bill = refund.getAddress();
		StringBuffer billAddress = new StringBuffer();
		if (bill != null) {
			billAddress = billAddress.append(forUnusedAddress(
					bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ forUnusedAddress(bill.getCountryOrRegion(), false));
			String billAddres = billAddress.toString();

			if (billAddres.trim().length() > 0) {
				return billAddres;
			}
		}
		return "";
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return ", " + add;
		} else {
			if (add != null && !add.equals(""))
				return add + "\n";
		}
		return "";
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	private String getRegisteredAddress() {
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();

		if (reg != null) {
			regestrationAddress = (reg.getAddress1()
					+ forUnusedAddress(reg.getStreet(), true)
					+ forUnusedAddress(reg.getCity(), true)
					+ forUnusedAddress(reg.getStateOrProvinence(), true)
					+ forUnusedAddress(reg.getZipOrPostalCode(), true)
					+ forUnusedAddress(reg.getCountryOrRegion(), true) + ".");
		} else {
			regestrationAddress = (company.getTradingName() + " "
					+ regestrationAddress + ((company.getRegistrationNumber() != null && !company
					.getRegistrationNumber().equals("")) ? "\n Company Registration No: "
					+ company.getRegistrationNumber()
					: ""));
		}
		String phoneStr = forNullValue(company.getPreferences().getPhone());
		if (phoneStr.trim().length() > 0) {
			regestrationAddress = regestrationAddress
					+ Global.get().messages().phone() + " : " + phoneStr + ",";
		}
		String website = forNullValue(company.getPreferences().getWebSite());

		if (website.trim().length() > 0) {
			regestrationAddress = regestrationAddress
					+ Global.get().messages().webSite() + " : " + website;
		}

		return regestrationAddress;

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
