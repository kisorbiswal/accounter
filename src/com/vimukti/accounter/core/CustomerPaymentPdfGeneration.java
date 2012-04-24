package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.DataUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
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
		try {
			DummyPayment template = new DummyPayment();
			template.setTitle(Global.get().Customer() + " Prepayment");
			template.setName(prePayment.getCustomer().getName());
			template.setNumber(prePayment.getNumber());
			template.setCustomerNAddress(getAddress());
			template.setRegisteredAddress(getRegistrationAddress());
			template.setDate(prePayment.getDate().toString());
			template.setPayMethod(prePayment.getPaymentMethod());
			template.setCheckNo(prePayment.getCheckNumber());
			template.setCustomerName(Global.get().Customer());
			Currency currency = prePayment.getCurrency();
			String symbol = currency.getSymbol();
			template.setAmount(DataUtils.getAmountAsStringInCurrency(
					prePayment.getTotal(), symbol));
			template.setCurrency(currency.getFormalName());
			template.setMemo(prePayment.getMemo());
			context.put("payment", template);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getRegistrationAddress() {
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
			regestrationAddress = (company.getTradingName() + " , "
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

	private String getAddress() {
		// To get the selected contact name form Invoice
		String phone = "";
		boolean hasPhone = false;

		Address bill = prePayment.getAddress();
		String customerName = forUnusedAddress(prePayment.getCustomer()
				.getName(), false);
		StringBuffer billAddress = new StringBuffer();
		if (bill != null) {
			billAddress = billAddress.append(forUnusedAddress(
					bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ forUnusedAddress(bill.getCountryOrRegion(), false));
			if (hasPhone) {
				billAddress.append(forUnusedAddress("Phone : " + phone, false));
			}
			String billAddres = billAddress.toString();
			if (billAddres.trim().length() > 0) {
				return billAddres;
			}
		} else {
			return customerName;
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

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
	}

	public class DummyPayment {

		private String name;
		private String customerName;
		private String registeredAddress;
		private String date;
		private String payMethod;
		private String checkNo;
		private String amount;
		private String title;
		private String number;
		private String customerNAddress;
		private String memo;
		private String currency;

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

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getCustomerNAddress() {
			return customerNAddress;
		}

		public void setCustomerNAddress(String customerNAddress) {
			this.customerNAddress = customerNAddress;
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
