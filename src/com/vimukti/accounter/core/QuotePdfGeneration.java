package com.vimukti.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * this class is used to generate Quote pdf files using custom files(odt and
 * docx files)
 * 
 * @author vimukti15
 * 
 */
public class QuotePdfGeneration {

	private Estimate estimate;
	private Company company;
	private BrandingTheme brandingTheme;

	public QuotePdfGeneration(Estimate estimate, Company company,
			BrandingTheme brandingTheme) {
		this.estimate = estimate;
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
			DummyQuote qut = new DummyQuote();
			String title = brandingTheme.getQuoteTitle() == null ? "Quote"
					: brandingTheme.getQuoteTitle().toString();
			qut.setTitle(title);
			qut.setCustomerName(estimate.getCustomer().getName());
			Address billAddress = estimate.getAddress();
			if (billAddress != null) {
				qut.setBillTo(billAddress);
				qut.billTo.setAddress1(forNullValue(billAddress.getAddress1()));
				qut.billTo.setStreet(forNullValue(billAddress.getStreet()));
				qut.billTo.setCity(forNullValue(billAddress.getCity()));
				qut.billTo.setStateOrProvinence(forNullValue(billAddress
						.getStateOrProvinence()));
				qut.billTo.setZipOrPostalCode(forNullValue(billAddress
						.getZipOrPostalCode()));
				qut.billTo.setCountryOrRegion(forNullValue(billAddress
						.getCountryOrRegion()));

			} else {
				qut.setBillTo(new Address());
				qut.billTo.setAddress1("");
				qut.billTo.setStreet("");
				qut.billTo.setCity("");
				qut.billTo.setStateOrProvinence("");
				qut.billTo.setZipOrPostalCode("");
				qut.billTo.setCountryOrRegion("");
			}
			qut.setBillAddress(getBillingAddress());
			qut.setNumber(estimate.getNumber());
			qut.setDeliveryDate(Utility.getDateInSelectedFormat(estimate
					.getDeliveryDate()));
			qut.setExpirationDate(Utility.getDateInSelectedFormat(estimate
					.getExpirationDate()));
			qut.setPhone(estimate.getPhone());
			String status = getStatusString(estimate.getStatus());
			qut.setStatus(status);

			Contact contact = estimate.getContact();
			qut.setContactName(contact != null ? contact.getName() : "");
			qut.setContactNumber(contact != null ? contact.getBusinessPhone()
					: "");
			qut.setContactEmail(contact != null ? contact.getEmail() : "");
			// for primary curreny
			Currency currency = estimate.getCustomer().getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					qut.setCurrency(currency.getFormalName().trim());
				}

			PaymentTerms paymentterm = estimate.getPaymentTerm();
			String payterm = paymentterm != null ? paymentterm.getName() : "";
			qut.setTerms(payterm);

			qut.setShipAddress(getShippingAddress());
			Address shippAdress = estimate.getShippingAdress();
			if (shippAdress != null) {
				qut.setShipTo(shippAdress);
				qut.shipTo.setAddress1(forNullValue(shippAdress.getAddress1()));
				qut.shipTo.setStreet(forNullValue(shippAdress.getStreet()));
				qut.shipTo.setCity(forNullValue(shippAdress.getCity()));
				qut.shipTo.setStateOrProvinence(forNullValue(shippAdress
						.getStateOrProvinence()));
				qut.shipTo.setCountryOrRegion(forNullValue(shippAdress
						.getCountryOrRegion()));
				qut.shipTo.setZipOrPostalCode(forNullValue(shippAdress
						.getZipOrPostalCode()));

			} else {
				qut.setShipTo(new Address());
				qut.shipTo.setAddress1("");
				qut.shipTo.setStreet("");
				qut.shipTo.setCity("");
				qut.shipTo.setStateOrProvinence("");
				qut.shipTo.setCountryOrRegion("");
				qut.shipTo.setZipOrPostalCode("");
			}

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
			List<TransactionItem> transactionItems = estimate
					.getTransactionItems();

			// double currencyFactor = estimate.getCurrencyFactor();
			String symbol = estimate.getCurrency().getSymbol();
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {

				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");

				StringBuffer data = new StringBuffer();
				Quantity quantity = item.getQuantity();
				if (quantity != null) {
					if (item.getItem() != null) {
						if (item.getItem().getType() == Item.TYPE_INVENTORY_PART
								|| item.getItem().getType() == Item.TYPE_INVENTORY_ASSEMBLY) {
							data.append(String.valueOf(quantity.getValue()));
							if (company.getPreferences().isUnitsEnabled()) {
								Unit unit = item.getQuantity().getUnit();
								if (unit != null) {
									data.append(" ");
									data.append(unit.getType());
								}
							}
						} else {
							data.append(String.valueOf(quantity.getValue()));
						}
					}
				}

				String unitPrice = Utility.decimalConversation(
						item.getUnitPrice(), symbol);
				String totalPrice = Utility.decimalConversation(
						item.getLineTotal(), symbol);

				Double vaTfraction = item.getVATfraction();
				String vatAmount = " ";
				if (vaTfraction != null) {
					vatAmount = Utility.decimalConversation(
							item.getVATfraction(), symbol);
				}
				String name = item.getItem() != null ? item.getItem().getName()
						: item.getAccount().getName();

				String discount = Utility.decimalConversation(
						item.getDiscount(), "");

				TAXCode taxCode = item.getTaxCode();
				String vatRate = " ";
				if (taxCode != null) {
					double rate = item.getTaxCode().getSalesTaxRate();
					vatRate = String.valueOf(rate) + " %";
				}
				itemList.add(new ItemList(name, description, data.toString(),
						unitPrice, discount, totalPrice, vatRate, vatAmount));
			}

			context.put("item", itemList);
			String total = Utility.decimalConversation(estimate.getTotal(),
					symbol);
			String netAmount = Utility.decimalConversation(
					estimate.getNetAmount(), symbol);
			qut.setTotal(total);
			qut.setNetAmount(netAmount);
			qut.setTaxTotal(Utility.decimalConversation(estimate.getTaxTotal(),
					symbol));
			qut.setMemo(estimate.getMemo());
			String termsNCondn = forNullValue(brandingTheme
					.getTerms_And_Payment_Advice());

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = " ";
			}
			qut.setAdviceTerms(termsNCondn);

			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = " ";
			}
			qut.setEmail(paypalEmail);

			Address regAddress1 = company.getRegisteredAddress();
			if (regAddress1 != null) {
				qut.setRegAddress(regAddress1);
				qut.regAddress.setAddress1(forNullValue(regAddress1
						.getAddress1()));
				qut.regAddress.setStreet(forNullValue(regAddress1.getStreet()));
				qut.regAddress.setCity(forNullValue(regAddress1.getCity()));
				qut.regAddress.setStateOrProvinence(forNullValue(regAddress1
						.getStateOrProvinence()));
				qut.regAddress.setCountryOrRegion(forNullValue(regAddress1
						.getCountryOrRegion()));
				qut.regAddress.setZipOrPostalCode(forNullValue(regAddress1
						.getZipOrPostalCode()));
			} else {
				qut.setRegAddress(new Address());
				qut.regAddress.setAddress1(company.getTradingName());
				qut.regAddress.setStreet(company.getRegistrationNumber());
				qut.regAddress.setCity("");
				qut.regAddress.setStateOrProvinence(company.getPreferences()
						.getPhone());
				qut.regAddress.setCountryOrRegion(company.getCountry());
				qut.regAddress.setZipOrPostalCode("");
			}

			qut.setRegistrationAddress(getRegistrationAddress());

			context.put("logo", logo);
			context.put("quote", qut);
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
			regestrationAddress = ("Registered Address: " + reg.getAddress1()
					+ forUnusedAddress(reg.getStreet(), true)
					+ forUnusedAddress(reg.getCity(), true)
					+ forUnusedAddress(reg.getStateOrProvinence(), true)
					+ forUnusedAddress(reg.getZipOrPostalCode(), true)
					+ forNullValue(reg.getCountryOrRegion()) + ".");

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
		Contact selectedContact = estimate.getContact();
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
		Address bill = estimate.getAddress();
		String customerName = forUnusedAddress(
				estimate.getCustomer().getName(), false);
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
		Address shpAdres = estimate.getShippingAdress();
		if (shpAdres != null) {
			shipAddress = forUnusedAddress(estimate.getCustomer().getName(),
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
				return add + ", ";
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

	public class DummyQuote {

		private String title;
		private String number;
		private String deliveryDate;
		private String expirationDate;
		private String phone;
		private String status;
		private String currency;
		private String terms;
		private String dueDate;
		private String billAddress;
		private String shipAddress;
		private String total;
		private String netAmount;
		private String memo;
		private String adviceTerms;
		private String email;
		private String registrationAddress;
		private Address billTo;
		private Address regAddress;
		private Address shipTo;
		private String taxTotal;
		private String contactName;
		private String contactNumber;
		private String contactEmail;
		private String customerName;

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

		public String getDeliveryDate() {
			return deliveryDate;
		}

		public void setDeliveryDate(String deliveryDate) {
			this.deliveryDate = deliveryDate;
		}

		public String getExpirationDate() {
			return expirationDate;
		}

		public void setExpirationDate(String expirationDate) {
			this.expirationDate = expirationDate;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(String netAmount) {
			this.netAmount = netAmount;
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

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

		public String getContactEmail() {
			return contactEmail;
		}

		public void setContactEmail(String contactEmail) {
			this.contactEmail = contactEmail;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
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

	/**
	 * this method is used to get the Status String value based on int value
	 */
	private String getStatusString(int status) {
		AccounterMessages messages = Global.get().messages();
		switch (status) {
		case ClientEstimate.STATUS_OPEN:
			return messages.open();

		case ClientEstimate.STATUS_ACCECPTED:
			return messages.accepted();

		case ClientEstimate.STATUS_CLOSE:
			return messages.closed();

		case ClientEstimate.STATUS_REJECTED:
			return messages.rejected();

		case ClientEstimate.STATUS_COMPLETED:
			return messages.closed();

		default:
			break;
		}
		return "";
	}
}
