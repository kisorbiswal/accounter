package com.vimukti.accounter.core;

import java.io.File;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.DataUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;

public class CustomerPaymentPdfGeneration {

	private final CustomerPrePayment prePayment;
	private final Company company;

	public CustomerPaymentPdfGeneration(CustomerPrePayment prePayment,
			Company company) {
		this.prePayment = prePayment;
		this.company = company;
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		DummyPayment template = new DummyPayment();
		IImageProvider footerImg = new ClassPathImageProvider(
				CustomerPaymentPdfGeneration.class, "templetes"
						+ File.separator + "footer-print-img.jpg");
		template.setTitle("Payment Receipt");
		template.setName(prePayment.getCustomer().getName());
		// template.setPayFrom(prePayment.getAccount().getName());
		Address regAdr = company.getRegisteredAddress();

		String regAddress = forAddress(regAdr.getAddress1(), false)
				+ forAddress(regAdr.getStreet(), false)
				+ forAddress(regAdr.getCity(), false)
				+ forAddress(regAdr.getStateOrProvinence(), false)
				+ forAddress(regAdr.getZipOrPostalCode(), false)
				+ forAddress(regAdr.getCountryOrRegion(), true);
		template.setRegisteredAddress(regAddress);
		template.setDate(prePayment.getDate().toString());
		template.setPaymentMethod(prePayment.getPaymentMethod());
		template.setChequeOrRefNo(prePayment.getCheckNumber());
		template.setCustomerName(Global.get().Customer());
		String symbol = prePayment.getCurrency().getSymbol();
		template.setAmount(DataUtils.getAmountAsStringInCurrency(
				prePayment.getTotal(), symbol));
		context.put("payment", template);
		context.put("companyImg", footerImg);
		return context;

	}

	public class DummyPayment {

		private String name;
		private String customerName;
		private String registeredAddress;
		private String date;
		private String paymentMethod;
		private String chequeOrRefNo;
		private String amount;
		private String title;

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

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
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
