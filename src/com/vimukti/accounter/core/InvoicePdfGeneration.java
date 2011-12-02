package com.vimukti.accounter.core;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.main.ServerConfiguration;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class InvoicePdfGeneration {

	private Invoice invoice;
	private Company company;
	private BrandingTheme brandingTheme;
	private InputStream in;
	private IXDocReport report;
	private int maxDecimalPoints;

	public InvoicePdfGeneration(Invoice invoice, Company company,
			BrandingTheme brandingTheme) {
		this.invoice = invoice;
		this.company = company;
		this.brandingTheme = brandingTheme;
		this.maxDecimalPoints = getMaxDecimals(invoice);

		

	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {

			IImageProvider logo = new ClassPathImageProvider(
					InvoicePdfGeneration.class, getImage());
			IImageProvider footerImg = new ClassPathImageProvider(
					InvoicePdfGeneration.class, "templetes" + File.separator
							+ "footer-print-img.jpg");

			FieldsMetadata imgMetaData = new FieldsMetadata();
			imgMetaData.addFieldAsImage("logo");
			imgMetaData.addFieldAsImage("companyImg");
			report.setFieldsMetadata(imgMetaData);

			// assigning the original values
			DummyInvoice i = new DummyInvoice();
			i.setBillAddress(getBillingAddress());
			i.setInvoiceNumber(invoice.getNumber());
			i.setInvoiceDate(invoice.getDate().toString());
			i.setCurrency("IND");
			i.setTerms("On Receipt");
			i.setDueDate(invoice.getDueDate().toString());
			i.setShipAddress(getShippingAddress());
			i.setSalesPersonName(getSalesPersonName());
			i.setShippingMethod("");

			// for transactions

			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("item.name");
			headersMetaData.addFieldAsList("item.description");
			headersMetaData.addFieldAsList("item.quantity");
			headersMetaData.addFieldAsList("item.itemUnitPrice");
			headersMetaData.addFieldAsList("item.discount");
			headersMetaData.addFieldAsList("item.itemTotalPrice");
			headersMetaData.addFieldAsList("item.itemVatRate");
			headersMetaData.addFieldAsList("item.itemVatAmount");
			report.setFieldsMetadata(headersMetaData);
			List<ItemList> itemList = new ArrayList<ItemList>();
			List<TransactionItem> transactionItems = invoice
					.getTransactionItems();
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {
				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				String discount = largeAmountConversation(item.getDiscount());
				String qty = forZeroAmounts(getDecimalsUsingMaxDecimals(item
						.getQuantity().getValue(), null, maxDecimalPoints));
				String unitPrice = forZeroAmounts(largeAmountConversation(item
						.getUnitPrice()));
				String totalPrice = largeAmountConversation(item.getLineTotal());

				String vatAmount = getDecimalsUsingMaxDecimals(
						item.getVATfraction(), null, 2);
				String vatRate = item.getTaxCode().getName();

				itemList.add(new ItemList(item.getItem().getName(),
						description, qty, unitPrice, discount, totalPrice,
						vatRate, vatAmount));

			}

			context.put("item", itemList);
			String total = largeAmountConversation(invoice.getTotal());

			i.setTotal(total);
			i.setPayment(largeAmountConversation(invoice.getPayments()));
			i.setBalancedue(largeAmountConversation(invoice.getBalanceDue()));

			String termsNCondn = forNullValue(
					brandingTheme.getTerms_And_Payment_Advice());

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = " ";
			}
			i.setAdviceTerms(termsNCondn);

			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = " ";
			}
			i.setEmail(paypalEmail);

			i.setVatNum(getVatNumber());
			i.setSortCode(getSortCode());
			i.setBankAccountNo(getBankAccountNumbere());
			i.setRegestrationAddress(getRegistrationAddress());

			context.put("logo", logo);
			context.put("inv", i);
			context.put("companyImg", footerImg);

			

			
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		} 
return null;
	}

	private String getRegistrationAddress() {
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();

		if (reg != null)
			regestrationAddress = ("Registered Address: "
					+ reg.getAddress1()
					+ forUnusedAddress(reg.getStreet(), true)
					+ forUnusedAddress(reg.getCity(), true)
					+ forUnusedAddress(reg.getStateOrProvinence(), true)
					+ forUnusedAddress(reg.getZipOrPostalCode(), true)
					+ forUnusedAddress(reg.getCountryOrRegion(), true) + ".");

		regestrationAddress = (company.getTradingName() + " "
				+ regestrationAddress + ((company.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "\n Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));

		if (regestrationAddress != null
				&& regestrationAddress.trim().length() > 0) {
			if (brandingTheme.isShowRegisteredAddress()) {
				return regestrationAddress;
			}
		}
		return "";
	}

	private String getSortCode() {
		String sortCode = " Sort Code: " + forNullValue(company.getSortCode());
		if (company.getSortCode() != null) {
			if (company.getSortCode().length() > 0) {
				return sortCode;
			}
		}
		return "";
	}

	private String getBankAccountNumbere() {
		String bankAccountNum = "Bank Account No: "
				+ forNullValue(company.getBankAccountNo());
		if (company.getBankAccountNo() != null) {
			if (company.getBankAccountNo().length() > 0) {
				return bankAccountNum;
			}
		}
		return "";
	}

	private String getVatNumber() {
		String vatString = "Tax No: "
				+ forNullValue(company.getPreferences()
						.getVATregistrationNumber());
		if (company.getPreferences().getVATregistrationNumber().length() > 0) {
			if (brandingTheme.isShowTaxNumber()) {
				return vatString;
			}
		}
		return "";
	}

	private int getMaxDecimals(Invoice inv) {
		String qty;
		String max;
		int temp = 0;
		for (TransactionItem item : inv.getTransactionItems()) {
			qty = String.valueOf(item.getQuantity());
			max = qty.substring(qty.indexOf(".") + 1);
			if (!max.equals("0")) {
				if (temp < max.length()) {
					temp = max.length();
				}
			}

		}
		return temp;
	}

	private String getSalesPersonName() {
		SalesPerson salesPerson = invoice.getSalesPerson();
		String salesPersname = salesPerson != null ? ((salesPerson
				.getFirstName() != null ? salesPerson.getFirstName() : "") + (salesPerson
				.getLastName() != null ? salesPerson.getLastName() : ""))
				: "";

		if (salesPersname.trim().length() > 0) {
			return salesPersname;
		}

		return "";
	}

	public String getImage() {
		StringBuffer original = new StringBuffer();

		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ company.getId() + "/" + brandingTheme.getFileName());

		return original.toString();

	}

	private String getBillingAddress() {
		// for getting customer contact name
		String cname = "";
		String phone = "";
		Customer customer = invoice.getCustomer();
		Set<Contact> contacts = customer.getContacts();
		for (Contact contact : contacts) {
			if (contact.isPrimary()) {
				cname = contact.getName().trim();

				if (contact.getBusinessPhone().trim().length() > 0)
					phone = contact.getBusinessPhone();

			}
		}
		// setting billing address
		Address bill = invoice.getBillingAddress();
		String customerName = forUnusedAddress(invoice.getCustomer().getName(),
				false);
		if (bill != null) {
			String billAddress = forUnusedAddress(cname, false) + customerName
					+ forUnusedAddress(bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ forUnusedAddress(bill.getCountryOrRegion(), false)
					+ forUnusedAddress("Phone : " + phone, false);

			if (billAddress.trim().length() > 0) {
				return billAddress;
			}
		} else {
			return customerName;
		}
		return "";
	}

	private String getShippingAddress() {
		// setting shipping address
		String shipAddress = "";
		Address shpAdres = invoice.getShippingAdress();
		if (shpAdres != null) {
			shipAddress = forUnusedAddress(invoice.getCustomer().getName(),
					false)
					+ forUnusedAddress(shpAdres.getAddress1(), false)
					+ forUnusedAddress(shpAdres.getStreet(), false)
					+ forUnusedAddress(shpAdres.getCity(), false)
					+ forUnusedAddress(shpAdres.getStateOrProvinence(), false)
					+ forUnusedAddress(shpAdres.getZipOrPostalCode(), false)
					+ forUnusedAddress(shpAdres.getCountryOrRegion(), false);
		}
		if (shipAddress.trim().length() > 0) {
			return shipAddress;
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

	private String getLogoAlignment() {
		String logoAlignment = null;
		if (brandingTheme.getLogoAlignmentType() == 1) {
			logoAlignment = "left";
		} else {
			logoAlignment = "right";
		}
		return logoAlignment;
	}

	private String getDecimalsUsingMaxDecimals(double quantity, String amount,
			int maxDecimalPoint) {
		String qty = "";
		String max;
		if (maxDecimalPoint != 0) {
			if (amount == null)
				qty = String.valueOf(quantity);
			else
				qty = amount;
			max = qty.substring(qty.indexOf(".") + 1);
			if (maxDecimalPoint > max.length()) {
				for (int i = max.length(); maxDecimalPoint != i; i++) {
					qty = qty + "0";
				}
			}
		} else {
			qty = String.valueOf((long) quantity);
		}

		String temp = qty.contains(".") ? qty.replace(".", "-").split("-")[0]
				: qty;
		return insertCommas(temp)
				+ (qty.contains(".") ? "."
						+ qty.replace(".", "-").split("-")[1] : "");
	}

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
	}

	private static String insertCommas(String str) {

		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	private String largeAmountConversation(double amount) {
		String amt = Utility.decimalConversation(amount);
		amt = getDecimalsUsingMaxDecimals(0.0, amt, 2);
		return (amt);
	}

	public class DummyInvoice {

		private String invoiceNumber;
		private String invoiceDate;
		private String currency;
		private String terms;
		private String dueDate;
		private String billAddress;
		private String shipAddress;
		private String salesPersonName;
		private String shippingMethod;
		private String total;
		private String payment;
		private String balancedue;
		private String memo;
		private String adviceTerms;
		private String email;
		private String vatNum;
		private String sortCode;
		private String BankAccountNo;
		private String regestrationAddress;

		public String getInvoiceNumber() {
			return invoiceNumber;
		}

		public void setInvoiceNumber(String invoiceNumber) {
			this.invoiceNumber = invoiceNumber;
		}

		public String getInvoiceDate() {
			return invoiceDate;
		}

		public void setInvoiceDate(String invoiceDate) {
			this.invoiceDate = invoiceDate;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getDueDate() {
			return dueDate;
		}

		public void setDueDate(String dueDate) {
			this.dueDate = dueDate;
		}

		
		public String getSalesPersonName() {
			return salesPersonName;
		}

		public void setSalesPersonName(String salesPersonName) {
			this.salesPersonName = salesPersonName;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getPayment() {
			return payment;
		}

		public void setPayment(String payment) {
			this.payment = payment;
		}

		public String getBalancedue() {
			return balancedue;
		}

		public void setBalancedue(String balancedue) {
			this.balancedue = balancedue;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getVatNum() {
			return vatNum;
		}

		public void setVatNum(String vatNum) {
			this.vatNum = vatNum;
		}

		public String getSortCode() {
			return sortCode;
		}

		public void setSortCode(String sortCode) {
			this.sortCode = sortCode;
		}

		public String getBankAccountNo() {
			return BankAccountNo;
		}

		public void setBankAccountNo(String bankAccountNo) {
			BankAccountNo = bankAccountNo;
		}

		public String getRegestrationAddress() {
			return regestrationAddress;
		}

		public void setRegestrationAddress(String regestrationAddress) {
			this.regestrationAddress = regestrationAddress;
		}

		public String getShippingMethod() {
			return shippingMethod;
		}

		public void setShippingMethod(String shippingMethod) {
			this.shippingMethod = shippingMethod;
		}

		public String getBillAddress() {
			return billAddress;
		}

		public void setBillAddress(String billAddress) {
			this.billAddress = billAddress;
		}

		public String getShipAddress() {
			return shipAddress;
		}

		public void setShipAddress(String shipAddress) {
			this.shipAddress = shipAddress;
		}

		public String getTerms() {
			return terms;
		}

		public void setTerms(String terms) {
			this.terms = terms;
		}

		public String getAdviceTerms() {
			return adviceTerms;
		}

		public void setAdviceTerms(String adviceTerms) {
			this.adviceTerms = adviceTerms;
		}

		
	}

	public class ItemList {
		private String name;
		private String description;
		private String quantity;
		private String itemUnitPrice;
		private String discount;
		private String itemTotalPrice;
		private String itemVatRate;
		private String itemVatAmount;

		ItemList(String name, String description, String quantity,
				String itemUnitPrice, String discount, String itemTotalPrice,
				String itemVatRate, String itemVatAmount) {
			this.name = name;
			this.description = description;
			this.quantity = quantity;
			this.itemUnitPrice = itemUnitPrice;
			this.discount = discount;
			this.itemTotalPrice = itemTotalPrice;
			this.itemVatRate = itemVatRate;
			this.itemVatAmount = itemVatAmount;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public String getItemUnitPrice() {
			return itemUnitPrice;
		}

		public void setItemUnitPrice(String itemUnitPrice) {
			this.itemUnitPrice = itemUnitPrice;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getItemTotalPrice() {
			return itemTotalPrice;
		}

		public void setItemTotalPrice(String itemTotalPrice) {
			this.itemTotalPrice = itemTotalPrice;
		}

		public String getItemVatRate() {
			return itemVatRate;
		}

		public void setItemVatRate(String itemVatRate) {
			this.itemVatRate = itemVatRate;
		}

		public String getItemVatAmount() {
			return itemVatAmount;
		}

		public void setItemVatAmount(String itemVatAmount) {
			this.itemVatAmount = itemVatAmount;
		}

	}
}
