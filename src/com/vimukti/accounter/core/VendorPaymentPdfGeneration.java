package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.Global;

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
			template.setTitle(Global.get().Vendor() + " Prepayment");
			template.setName(vendorPayment.getVendor().getName());
			template.setVendorNBillingAddress(getBillingAddress());
			template.setRegisteredAddress(getRegisteredAddress());
			template.setMemo(vendorPayment.getMemo());
			template.setNumber(vendorPayment.getNumber());
			template.setDate(Utility.getDateInSelectedFormat(vendorPayment
					.getDate()));
			template.setPayMethod(vendorPayment.getPaymentMethod());
			template.setCheckNo(vendorPayment.getCheckNumber());
			template.setCurrency(vendorPayment.getVendor() != null ? vendorPayment
					.getVendor().getCurrency().getFormalName()
					: vendorPayment.getCurrency().getFormalName());
			String symbol = vendorPayment.getCurrency().getSymbol();
			template.setTdsTotal(Utility.decimalConversation(
					vendorPayment.getTdsTotal(), symbol));
			template.setVendorPayment(Utility.decimalConversation(
					vendorPayment.getTotal() - vendorPayment.getTdsTotal(),
					symbol));
			template.setTotal(Utility.decimalConversation(
					vendorPayment.getTotal(), symbol));
			context.put("payment", template);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return context;

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

	private String getBillingAddress() {
		String cname = "";
		String phone = "";
		boolean hasPhone = false;

		Address bill = vendorPayment.getAddress();
		String customerName = forUnusedAddress(vendorPayment.getVendor()
				.getName(), false);
		StringBuffer billAddress = new StringBuffer();
		if (bill != null) {
			billAddress = billAddress.append(forUnusedAddress(cname, false)
					+ customerName
					+ forUnusedAddress(bill.getAddress1(), false)
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
			StringBuffer contact = new StringBuffer();
			contact = contact.append(forUnusedAddress(cname, false)
					+ customerName);
			return contact.toString();
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

	public class DummyPayment {

		private String title;
		private String name;
		private String registeredAddress;
		private String memo;
		private String date;
		private String payMethod;
		private String checkNo;
		private String currency;
		private String vendorPayment;
		private String vendorNBillingAddress;
		private String tdsTotal;
		private String total;

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

		public String getVendorNBillingAddress() {
			return vendorNBillingAddress;
		}

		public void setVendorNBillingAddress(String vendorNBillingAddress) {
			this.vendorNBillingAddress = vendorNBillingAddress;
		}

		public String getTdsTotal() {
			return tdsTotal;
		}

		public void setTdsTotal(String tdsTotal) {
			this.tdsTotal = tdsTotal;
		}

		public String getVendorPayment() {
			return vendorPayment;
		}

		public void setVendorPayment(String vendorPayment) {
			this.vendorPayment = vendorPayment;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
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
