package com.vimukti.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.Global;

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

public class InvoicePdfGeneration {

	private Invoice invoice;
	private Company company;
	private BrandingTheme brandingTheme;
	private CompanyPreferences preferences;

	public InvoicePdfGeneration(Invoice invoice, Company company,
			BrandingTheme brandingTheme) {
		this.invoice = invoice;
		this.company = company;
		this.brandingTheme = brandingTheme;
		preferences = company.getPreferences();
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
			String title = brandingTheme.getOverDueInvoiceTitle() == null ? "Invoice"
					: brandingTheme.getOverDueInvoiceTitle().toString();
			i.setTitle(title);
			i.setBillAddress(getBillingAddress());

			// Contact selectedContact = invoice.getContact();
			// if (selectedContact != null) {
			// billTo.setContactName(selectedContact.getName() != null ?
			// selectedContact
			// .getName().trim() : "");
			// } else {
			// billTo.setContactName("");
			// }
			//
			// billTo.setBill_customerName(invoice.getCustomer().getName().trim());
			Address billAddress = invoice.getBillingAddress();
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

			Address shippAdress = invoice.getShippingAdress();
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

			i.setInvoiceNumber(invoice.getNumber());
			i.setInvoiceDate(Utility.getDateInSelectedFormat(invoice.getDate()));

			Customer customer = invoice.getCustomer();
			// for customer details
			i.setCstNumber(customer.getCSTno() == null ? "" : customer
					.getCSTno());
			i.setServiceTaxRegistrationNumber(customer
					.getServiceTaxRegistrationNo() == null ? "" : customer
					.getServiceTaxRegistrationNo());
			i.setTaxpayerIdentificationNumber(customer.getTINNumber() == null ? ""
					: customer.getTINNumber());
			i.setTaxRegistrationNumber(customer.getVATRegistrationNumber() == null ? ""
					: customer.getVATRegistrationNumber());
			i.setCustomerTaxCode(customer.getTAXCode() == null ? "" : customer
					.getTAXCode().getName());

			// for primary curreny
			Currency currency = customer.getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					i.setCurrency(currency.getFormalName().trim());
				}

			PaymentTerms paymentterm = invoice.getPaymentTerm();
			String payterm = paymentterm != null ? paymentterm.getName() : "";
			i.setTerms(payterm);

			i.setDueDate(Utility.getDateInSelectedFormat(invoice.getDueDate()));
			i.setShipAddress(getShippingAddress());

			ShippingMethod shipMtd = invoice.getShippingMethod();
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
			headersMetaData.addFieldAsList("item.className");
			headersMetaData.addFieldAsList("item.itemVatRate");
			headersMetaData.addFieldAsList("item.itemVatAmount");
			report.setFieldsMetadata(headersMetaData);
			List<ItemList> itemList = new ArrayList<ItemList>();
			List<TransactionItem> transactionItems = invoice
					.getTransactionItems();

			// double currencyFactor = invoice.getCurrencyFactor();
			String symbol = invoice.getCurrency().getSymbol();
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
				String name = item.getItem() != null ? item.getItem().getName()
						: item.getAccount().getName();
				String discount = "";
				if (preferences.isTrackDiscounts()
						&& preferences.isDiscountPerDetailLine()) {
					discount = Utility.decimalConversation(item.getDiscount(),
							"");
				}

				TAXCode taxCode = item.getTaxCode();
				String vatRate = " ";
				if (taxCode != null) {
					double valRate1 = item.getTaxCode().getSalesTaxRate();
					vatRate = String.valueOf(valRate1) + " %";
				}

				String className = "";
				// if (preferences.isClassTrackingEnabled()) {
				// if (preferences.isClassPerDetailLine()) {
				if (item.getAccounterClass() != null) {
					className = forNullValue(item.getAccounterClass().getName());
				}
				// }
				// }
				itemList.add(new ItemList(name, description, qty, unitPrice,
						className, discount, totalPrice, vatRate, vatAmount));
			}

			context.put("item", itemList);
			String total = Utility.decimalConversation(invoice.getTotal(),
					symbol);

			i.setTotal(total);
			String netAmount = Utility.decimalConversation(
					invoice.getNetAmount(), symbol);
			i.setNetAmount(netAmount);

			i.setPayment(Utility.decimalConversation(invoice.getPayments(),
					symbol));
			i.setBalancedue(Utility.decimalConversation(
					invoice.getBalanceDue(), symbol));
			String orderNum = invoice.getOrderNum() == null ? "" : invoice
					.getOrderNum().trim();
			i.setOrderNumber(orderNum);

			i.setMemo(invoice.getMemo());
			String termsNCondn = forNullValue(brandingTheme
					.getTerms_And_Payment_Advice());

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = " ";
			}
			i.setAdviceTerms(termsNCondn);

			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = " ";
			}
			i.setEmail(paypalEmail);

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
			// if (preferences.isJobTrackingEnabled()) {
			// if (invoice.getJob() != null) {
			// i.setJob(invoice.getJob().getJobName());
			// } else {
			// i.setJob("");
			// }
			// } else {
			// i.setJob("");
			// }

			// if (preferences.isLocationTrackingEnabled()) {
			// Location location = invoice.getLocation();
			// if (location != null) {
			// i.setLocation(location.getName());
			// } else {
			// i.setLocation("");
			// }
			// } else {
			// i.setLocation("");
			// }
			i.setCustomerName(invoice.getCustomer().getName());

			List<TransactionItem> items = invoice.getTransactionItems();
			if (preferences.isTrackDiscounts()
					&& (preferences.isDiscountPerDetailLine() == false)) {
				double discount = 0;

				for (TransactionItem trItem : items) {
					if (trItem != null) {
						Double discountValue = trItem.getDiscount();
						if (discountValue != null) {
							discount = discountValue.doubleValue();
							if (discount != 0.0D)
								break;
							else
								continue;
						}
					}
				}

				String discountVal = Utility.decimalConversation(discount,
						symbol);
				i.setDiscount(discountVal);

			} else {
				i.setDiscount("");
			}
			// if (preferences.isClassTrackingEnabled()
			// && (!preferences.isClassPerDetailLine() == false)) {
			// String accounterClass = "";
			// for (TransactionItem trItem : items) {
			// if (trItem.getAccounterClass() != null) {
			// if (trItem.getAccounterClass() != null) {
			// accounterClass = trItem.getAccounterClass()
			// .getclassName();
			// }
			// if (accounterClass != null) {
			// break;
			// } else {
			// continue;
			// }
			// }
			//
			// }
			// i.setClassName(accounterClass);
			// } else {
			// i.setClassName("");
			// }
			i.setTaxTotal(Utility.decimalConversation(invoice.getTaxTotal(),
					symbol));
			i.setDeliveryDate(Utility.getDateInSelectedFormat(invoice
					.getDeliverydate()));
			if (invoice.getShippingTerm() != null) {
				i.setShippingTerms(invoice.getShippingTerm().getName());
			} else {
				i.setShippingTerms("");
			}

			context.put("logo", logo);
			context.put("invoice", i);
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

		original.append(ServerConfiguration.getAttachmentsDir()
				+ File.separator + company.getId() + File.separator
				+ brandingTheme.getFileName());

		return original.toString();

	}

	private String getBillingAddress() {
		// To get the selected contact name form Invoice
		String cname = "";
		String phone = "";
		boolean hasPhone = false;
		Contact selectedContact = invoice.getContact();
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
		Address bill = invoice.getBillingAddress();
		String customerName = forUnusedAddress(invoice.getCustomer().getName(),
				false);
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

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
	}

	public class DummyInvoice {

		private String title;
		private String invoiceNumber;
		private String invoiceDate;
		private String orderNumber;
		private String currency;
		private String terms;
		private String dueDate;
		private String billAddress;
		private String shipAddress;
		private String shippingMethod;
		private String total;
		private String payment;
		private String balancedue;
		private String memo;
		private String adviceTerms;
		private String email;
		private String registrationAddress;
		private String discount;
		private String netAmount;
		private String customerName;
		// private String job;
		// private String className;
		// private String location;
		private String deliveryDate;
		private String shippingTerms;
		private String cstNumber;
		private String serviceTaxRegistrationNumber;
		private String taxpayerIdentificationNumber;
		private String taxRegistrationNumber;
		private String customerTaxCode;
		private Address billTo;
		private Address regAddress;
		private Address shipTo;
		private String taxTotal;

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

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getOrderNumber() {
			return orderNumber;
		}

		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}

		public String getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(String netAmount) {
			this.netAmount = netAmount;
		}

		// public String getJob() {
		// return job;
		// }
		//
		// public void setJob(String job) {
		// this.job = job;
		// }
		//
		// public String getClassName() {
		// return className;
		// }
		//
		// public void setClassName(String className) {
		// this.className = className;
		// }
		//
		// public String getLocation() {
		// return location;
		// }
		//
		// public void setLocation(String location) {
		// this.location = location;
		// }

		public String getDeliveryDate() {
			return deliveryDate;
		}

		public void setDeliveryDate(String deliveryDate) {
			this.deliveryDate = deliveryDate;
		}

		public String getShippingTerms() {
			return shippingTerms;
		}

		public void setShippingTerms(String shippingTerms) {
			this.shippingTerms = shippingTerms;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getCstNumber() {
			return cstNumber;
		}

		public void setCstNumber(String cstNumber) {
			this.cstNumber = cstNumber;
		}

		public String getServiceTaxRegistrationNumber() {
			return serviceTaxRegistrationNumber;
		}

		public void setServiceTaxRegistrationNumber(
				String serviceTaxRegistrationNumber) {
			this.serviceTaxRegistrationNumber = serviceTaxRegistrationNumber;
		}

		public String getTaxpayerIdentificationNumber() {
			return taxpayerIdentificationNumber;
		}

		public void setTaxpayerIdentificationNumber(
				String taxpayerIdentificationNumber) {
			this.taxpayerIdentificationNumber = taxpayerIdentificationNumber;
		}

		public String getTaxRegistrationNumber() {
			return taxRegistrationNumber;
		}

		public void setTaxRegistrationNumber(String taxRegistrationNumber) {
			this.taxRegistrationNumber = taxRegistrationNumber;
		}

		public String getCustomerTaxCode() {
			return customerTaxCode;
		}

		public void setCustomerTaxCode(String customerTaxCode) {
			this.customerTaxCode = customerTaxCode;
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

		private String className;

		ItemList(String name, String description, String quantity,
				String itemUnitPrice, String className, String discount,
				String itemTotalPrice, String itemVatRate, String itemVatAmount) {
			this.name = name;
			this.description = description;
			this.quantity = quantity;
			this.itemUnitPrice = itemUnitPrice;
			this.className = className;
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

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

	}

}
