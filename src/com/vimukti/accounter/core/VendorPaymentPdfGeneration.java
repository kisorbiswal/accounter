package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.ui.DataUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class VendorPaymentPdfGeneration {

	private VendorPrePayment vendorPayment;
	private Company company;

	public VendorPaymentPdfGeneration(VendorPrePayment payment, Company company) {
		this.company = company;
		this.vendorPayment = payment;
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {

			DummyPayment template = new DummyPayment();
			template.setTitle("Payment Receipt");
			template.setName(vendorPayment.getVendor().getName());
			// template.setPayFrom(prePayment.getAccount().getName());
			Address regAdr = company.getRegisteredAddress();

			String regAddress = forAddress(regAdr.getAddress1(), false)
					+ forAddress(regAdr.getStreet(), false)
					+ forAddress(regAdr.getCity(), false)
					+ forAddress(regAdr.getStateOrProvinence(), false)
					+ forAddress(regAdr.getZipOrPostalCode(), false)
					+ forAddress(regAdr.getCountryOrRegion(), true);
			template.setRegisteredAddress(regAddress);
			template.setMemo(vendorPayment.getMemo());
			template.setNumber(vendorPayment.getNumber());
			template.setDate(vendorPayment.getDate().toString());
			template.setPayMethod(vendorPayment.getPaymentMethod());
			template.setCheckNo(vendorPayment.getCheckNumber());
			template.setCurrency(vendorPayment.getVendor() != null ? vendorPayment
					.getVendor().getCurrency().getFormalName()
					: vendorPayment.getCurrency().getFormalName());
			String symbol = vendorPayment.getCurrency().getSymbol();
			template.setAmount(DataUtils.getAmountAsStringInCurrency(
					vendorPayment.getTotal(), symbol));
			context.put("payment", template);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return context;

	}

	public class DummyPayment {

		private String title;
		private String name;
		private String registeredAddress;
		private String memo;
		private String date;
		private String payMethod;
		private String checkNo;
		private String currency;
		private String amount;

		private String number;

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

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
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

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getPayMethod() {
			return payMethod;
		}

		public void setPayMethod(String payMethod) {
			this.payMethod = payMethod;
		}

		public String getCheckNo() {
			return checkNo;
		}

		public void setCheckNo(String checkNo) {
			this.checkNo = checkNo;
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
