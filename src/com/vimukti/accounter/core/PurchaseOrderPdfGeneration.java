package com.vimukti.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * this class is used to generate Invoice pdf using custom template files(odt
 * and docx format)
 * 
 * @author vimukti15
 * 
 */

public class PurchaseOrderPdfGeneration {

	private PurchaseOrder purchaseOrder;
	private Company company;
	private BrandingTheme brandingTheme;

	public PurchaseOrderPdfGeneration(PurchaseOrder purchaseOrder,
			Company company, BrandingTheme brandingTheme) {
		this.purchaseOrder = purchaseOrder;
		this.company = company;
		this.brandingTheme = brandingTheme;

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
			DummyPurchaseOrder i = new DummyPurchaseOrder();
			String title = brandingTheme.getPurchaseOrderTitle() == null ? "Purchase Order"
					: brandingTheme.getPurchaseOrderTitle().toString();
			i.setTitle(title);
			i.setBillAddress(getBillingAddress());
			Address billAddress = purchaseOrder.getVendorAddress();
			if (billAddress != null) {
				i.setBillTo(billAddress);
				i.billTo.setAddress1(forNullValue(billAddress.getAddress1()));
				i.billTo.setStreet(forNullValue(billAddress.getStreet()));
				i.billTo.setCity(forNullValue(billAddress.getCity()));
				i.billTo.setStateOrProvinence(forNullValue(billAddress
						.getStateOrProvinence()));
				i.billTo.setZipOrPostalCode(forNullValue(billAddress
						.getZipOrPostalCode()));
				i.billTo.setCountryOrRegion(forNullValue(billAddress
						.getCountryOrRegion()));

			} else {
				i.setBillTo(new Address());
				i.billTo.setAddress1("");
				i.billTo.setStreet("");
				i.billTo.setCity("");
				i.billTo.setStateOrProvinence("");
				i.billTo.setZipOrPostalCode("");
				i.billTo.setCountryOrRegion("");
			}
			i.setNumber(purchaseOrder.getNumber());
			i.setDate(Utility.getDateInSelectedFormat(purchaseOrder
					.getDespatchDate()));
			i.setVendorNo(purchaseOrder.getPurchaseOrderNumber());

			// for primary curreny
			Currency currency = purchaseOrder.getVendor().getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					i.setCurrency(currency.getFormalName().trim());
				}

			PaymentTerms paymentterm = purchaseOrder.getPaymentTerm();
			String payterm = paymentterm != null ? paymentterm.getName() : "";
			i.setTerms(payterm);

			 i.setShipAddress(getShippingAddress());
			Address shippAdress = purchaseOrder.getShippingAddress();
			if (shippAdress != null) {
				i.setShipTo(shippAdress);
				// i.shipTo.setShip_customerName(invoice.getCustomer().getName()
				// .trim());
				i.shipTo.setAddress1(forNullValue(shippAdress.getAddress1()));
				i.shipTo.setStreet(forNullValue(shippAdress.getStreet()));
				i.shipTo.setCity(forNullValue(shippAdress.getCity()));
				i.shipTo.setStateOrProvinence(forNullValue(shippAdress
						.getStateOrProvinence()));
				i.shipTo.setCountryOrRegion(forNullValue(shippAdress
						.getCountryOrRegion()));
				i.shipTo.setZipOrPostalCode(forNullValue(shippAdress
						.getZipOrPostalCode()));

			} else {
				i.setShipTo(new Address());
				i.shipTo.setAddress1("");
				i.shipTo.setStreet("");
				i.shipTo.setCity("");
				i.shipTo.setStateOrProvinence("");
				i.shipTo.setCountryOrRegion("");
				i.shipTo.setZipOrPostalCode("");
			}
			i.setVendorName(purchaseOrder.getVendor().getName());

			ShippingMethod shipMtd = purchaseOrder.getShippingMethod();
			String shipMtdName = shipMtd != null ? shipMtd.getName() : "";
			i.setShippingMethod(shipMtdName);

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
			List<TransactionItem> transactionItems = purchaseOrder
					.getTransactionItems();

			double currencyFactor = purchaseOrder.getCurrencyFactor();
			double totalTax = 0.0D;
			String symbol = purchaseOrder.getCurrency().getSymbol();
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {

				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");

				String qty = "";
				if (item.getQuantity() != null) {
					qty = String.valueOf(item.getQuantity().getValue());
				}
				String unitPrice = Utility.decimalConversation(
						item.getUnitPrice(), "");
				String totalPrice = Utility.decimalConversation(
						item.getLineTotal(), "");

				Double vaTfraction = item.getVATfraction();
				String vatAmount = " ";
				if (vaTfraction != null) {
					vatAmount = Utility.decimalConversation(
							item.getVATfraction(), "");
				}
				totalTax = totalTax + item.getVATfraction();
				String itemName = null;
				if (item.getItem() != null) {
					itemName = item.getItem().getName();
					String number = item.getItem().getVendorItemNumber();
					if (number != null && !number.isEmpty()) {
						itemName = number;
					}
				}

				String name = itemName != null ? itemName : item.getAccount()
						.getName();

				String discount = Utility.decimalConversation(
						item.getDiscount(), "");

				TAXCode taxCode = item.getTaxCode();
				String vatRate = " ";
				if (taxCode != null) {
					double rate = item.getTaxCode().getPurchaseTaxRate();
					vatRate = String.valueOf(rate) + " %";
				}
				itemList.add(new ItemList(name, description, qty, unitPrice,
						discount, totalPrice, vatRate, vatAmount));
			}

			context.put("item", itemList);
			String total = Utility.decimalConversation(
					purchaseOrder.getTotal(), symbol);

			i.setTotal(total);
			String netAmount = Utility.decimalConversation(
					purchaseOrder.getNetAmount(), symbol);
			i.setNetAmount(netAmount);

			i.setMemo(purchaseOrder.getMemo());
			String termsNCondn = forNullValue(brandingTheme
					.getTerms_And_Payment_Advice());

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = " ";
			}
			i.setAdviceTerms(termsNCondn);

			i.setTaxTotal(Utility.decimalConversation(totalTax, symbol));
			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = " ";
			}
			i.setEmail(paypalEmail);
			i.setStatus(getStatusString(purchaseOrder.getStatus()));
			 i.setRegistrationAddress(getRegistrationAddress());
			Address regAddress1 = company.getRegisteredAddress();
			if (regAddress1 != null) {
				i.setRegAddress(regAddress1);
				i.regAddress
						.setAddress1(forNullValue(regAddress1.getAddress1()));
				i.regAddress.setStreet(forNullValue(regAddress1.getStreet()));
				i.regAddress.setCity(forNullValue(regAddress1.getCity()));
				i.regAddress.setStateOrProvinence(forNullValue(regAddress1
						.getStateOrProvinence()));
				i.regAddress.setCountryOrRegion(forNullValue(regAddress1
						.getCountryOrRegion()));
				i.regAddress.setZipOrPostalCode(forNullValue(regAddress1
						.getZipOrPostalCode()));
			} else {
				i.setRegAddress(new Address());
				i.regAddress.setAddress1(company.getTradingName());
				i.regAddress.setStreet(company.getRegistrationNumber());
				i.regAddress.setCity("");
				i.regAddress.setStateOrProvinence(company.getPreferences()
						.getPhone());
				i.regAddress.setCountryOrRegion(company.getCountry());
				i.regAddress.setZipOrPostalCode("");
			}

			context.put("logo", logo);
			context.put("purchaseOrder", i);
			context.put("companyImg", footerImg);

			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getStatusString(int status) {
		switch (status) {
		case ClientTransaction.STATUS_OPEN:
			return Global.get().messages().open();
		case ClientTransaction.STATUS_COMPLETED:
			return Global.get().messages().completed();
		case ClientTransaction.STATUS_CANCELLED:
			return Global.get().messages().cancelled();
		default:
			break;
		}
		return "";
	}

	private String getRegistrationAddress() {
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();

		if (reg != null)
			regestrationAddress = ("Registered Address: " + reg.getAddress1()
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

	public String getImage() {
		StringBuffer original = new StringBuffer();

		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ company.getId() + "/" + brandingTheme.getFileName());

		return original.toString();

	}

	private String getBillingAddress() {
		// To get the selected contact name form Invoice
		String cname = "";
		String phone = "";
		boolean hasPhone = false;
		Contact selectedContact = purchaseOrder.getContact();
		if (selectedContact != null) {
			cname = selectedContact.getName().trim();
			if (selectedContact.getBusinessPhone().trim().length() > 0)
				phone = selectedContact.getBusinessPhone();
			if (phone.trim().length() > 0) {
				// If phone variable has value, then only we need to display
				// the text 'phone'
				hasPhone = true;
			}
		}

		// setting billing address
		Address bill = purchaseOrder.getVendorAddress();
		String customerName = forUnusedAddress(purchaseOrder.getVendor()
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
			// If there is no Bill Address, then display only customer and
			// contact name
			StringBuffer contact = new StringBuffer();
			contact = contact.append(forUnusedAddress(cname, false)
					+ customerName);
			return contact.toString();
		}
		return "";
	}

	private String getShippingAddress() {
		// setting shipping address
		String shipAddress = "";
		Address shpAdres = purchaseOrder.getShippingAddress();
		if (shpAdres != null) {
			shipAddress = forUnusedAddress(purchaseOrder.getVendor().getName(),
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

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
	}

	public class DummyPurchaseOrder {

		private String title;
		private String number;
		private String vendorNo;
		private String date;
		private String currency;
		private String terms;
		 private String billAddress;
		 private String shipAddress;
		private String vendorName;
		private String shippingMethod;
		private String total;
		private String netAmount;
		private String memo;
		private String adviceTerms;
		private String email;
		 private String registrationAddress;
		private String status;
		private Address billTo;
		private Address regAddress;
		private Address shipTo;
		private String taxTotal;

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
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

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRegistrationAddress() {
			return registrationAddress;
		}

		public void setRegistrationAddress(String registrationAddress) {
			this.registrationAddress = registrationAddress;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getVendorName() {
			return vendorName;
		}

		public void setVendorName(String vendorName) {
			this.vendorName = vendorName;
		}

		public String getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(String netAmount) {
			this.netAmount = netAmount;
		}

		public String getVendorNo() {
			return vendorNo;
		}

		public void setVendorNo(String vendorNo) {
			this.vendorNo = vendorNo;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Address getBillTo() {
			return billTo;
		}

		public void setBillTo(Address billTo) {
			this.billTo = billTo;
		}

		public Address getRegAddress() {
			return regAddress;
		}

		public void setRegAddress(Address regAddress) {
			this.regAddress = regAddress;
		}

		public Address getShipTo() {
			return shipTo;
		}

		public void setShipTo(Address shipTo) {
			this.shipTo = shipTo;
		}

		public String getTaxTotal() {
			return taxTotal;
		}

		public void setTaxTotal(String taxTotal) {
			this.taxTotal = taxTotal;
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
