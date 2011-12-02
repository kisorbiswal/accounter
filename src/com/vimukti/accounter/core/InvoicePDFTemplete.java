package com.vimukti.accounter.core;

import java.io.File;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.MiniTemplator;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * this class is used to generate Invoice report in PDF format
 * 
 */
public class InvoicePDFTemplete implements PrintTemplete {
	private final Invoice invoice;
	private final BrandingTheme brandingTheme;
	private final int maxDecimalPoints;
	// private static final String templateFileName = "templetes" +
	// File.separator
	// + "InvoiceTemplete.html";
	private final Company company;
	private final String templateName;

	public InvoicePDFTemplete(Invoice invoice, BrandingTheme brandingTheme,
			Company company, String templateName) {
		this.invoice = invoice;
		this.brandingTheme = brandingTheme;
		this.company = company;
		this.maxDecimalPoints = getMaxDecimals(invoice);
		this.templateName = templateName;

	}

	public String getTempleteName() {

		return "templetes" + File.separator + templateName + ".html";
	}

	@Override
	public String getPdfData() {
		String outPutString = "";
		MiniTemplator t;

		// TODO for displaying the company address

		try {
			t = new MiniTemplator(getTempleteName());

			externalizeStrings(t);

			String image = getImage();

			// setting logo Image
			if (brandingTheme.isShowLogo()) {
				String logoAlligment = getLogoAlignment();
				t.setVariable("getLogoAlignment", logoAlligment);

				t.setVariable("logoImage", image);
				t.addBlock("showlogo");
			}

			// setting invoice number

			String invNumber = forNullValue(invoice.getNumber());
			if (invNumber.trim().length() > 0) {

				t.setVariable("invoiceNumber", invNumber);
				t.addBlock("invNumberHead");
			}

			// setting invoice date

			String invDate = invoice.getDate().toString();
			if (invDate.trim().length() > 0) {

				t.setVariable("invoiceDate", invDate);
				t.addBlock("invDateHead");
			}

			// setting invoice order number
			String invOrderNum = forNullValue(invoice.getOrderNum());
			if (invOrderNum.trim().length() > 0) {

				t.setVariable("invoiceOrderNumber", invOrderNum);
				t.addBlock("invOrderNumHead");
			}

			// setting invoice customer number

			String invCustomerNum = forNullValue(invoice.getCustomer()
					.getNumber());
			if (invCustomerNum.trim().length() > 0) {
				t.setVariable("invoiceCustomerNumber", invCustomerNum);
				t.addBlock("invCustNumHead");
			}

			// setting payment terms
			PaymentTerms paymentterm = invoice.getPaymentTerm();
			String payterm = paymentterm != null ? paymentterm.getName() : "";
			if (payterm.trim().length() > 0) {

				t.setVariable("paymentTerms", payterm);

				t.addBlock("paymentTermsBlock");
			}

			// for primary curreny
			Currency currency = invoice.getCustomer().getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					t.setVariable("currency", currency.getFormalName().trim());
					t.addBlock("currency");
				}

			// for customer VAT Registration Number
			String vatRegistrationNumber = invoice.getCustomer()
					.getVATRegistrationNumber();

			if (company.getCountryPreferences().isVatAvailable()
					&& company.getPreferences().isTrackTax()) {

				String val = vatRegistrationNumber == null ? " "
						: vatRegistrationNumber;

				if (val.trim().length() > 0) {
					t.setVariable("customerVATNumber", val);
					t.addBlock("customerVat");
				}
			}

			// for getting customer contact name
			String cname = "";
			String phone = "";
			boolean hasPhone = false;
			Customer customer = invoice.getCustomer();
			// Set<Contact> contacts = customer.getContacts();
			// for (Contact contact : contacts) {
			// if (contact.isPrimary()) {
			// cname = contact.getName().trim();
			//
			// if (contact.getBusinessPhone().trim().length() > 0)
			// phone = contact.getBusinessPhone();
			// if (phone.trim().length() > 0) {
			// hasPhone = true;
			// }
			//
			// }
			// }

			// To get the selected contact name form Invoice
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
			String customerName = forUnusedAddress(invoice.getCustomer()
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
					billAddress.append(forUnusedAddress("Phone : " + phone,
							false));
				}

				String billAddres = billAddress.toString();

				if (billAddres.trim().length() > 0) {
					t.setVariable("billingAddress", billAddres);
					t.addBlock("billhead");
				}
			} else {
				// If there is no Bill Address, then display only customer and
				// contact name
				StringBuffer contact = new StringBuffer();
				contact = contact.append(forUnusedAddress(cname, false)
						+ customerName);
				t.setVariable("billingAddress", contact.toString());
				t.addBlock("billhead");
			}

			// setting shipping address
			String shipAddress = "";
			Address shpAdres = invoice.getShippingAdress();
			if (shpAdres != null) {
				shipAddress = forUnusedAddress(invoice.getCustomer().getName(),
						false)
						+ forUnusedAddress(shpAdres.getAddress1(), false)
						+ forUnusedAddress(shpAdres.getStreet(), false)
						+ forUnusedAddress(shpAdres.getCity(), false)
						+ forUnusedAddress(shpAdres.getStateOrProvinence(),
								false)
						+ forUnusedAddress(shpAdres.getZipOrPostalCode(), false)
						+ forUnusedAddress(shpAdres.getCountryOrRegion(), false);
			}
			if (shipAddress.trim().length() > 0) {
				t.setVariable("shippingAddress", shipAddress);
				t.addBlock("shiphead");
			}

			// setting salesPerson name
			boolean hasSalesNshippingMethod = false;
			SalesPerson salesPerson = invoice.getSalesPerson();
			String salesPersname = salesPerson != null ? ((salesPerson
					.getFirstName() != null ? salesPerson.getFirstName() : "") + (salesPerson
					.getLastName() != null ? salesPerson.getLastName() : ""))
					: "";

			if (salesPersname.trim().length() > 0) {
				hasSalesNshippingMethod = true;
				t.setVariable("salesPersonName", salesPersname);
				t.addBlock("salesperson");
				t.addBlock("salespersonhead");
			}
			// setting shippingmethod name

			ShippingMethod shipMtd = invoice.getShippingMethod();
			String shipMtdName = shipMtd != null ? shipMtd.getName() : "";

			if (shipMtdName.trim().length() > 0) {
				hasSalesNshippingMethod = true;
				t.setVariable("shippingMethodName", shipMtdName);
				t.addBlock("shippingmethod");
				t.addBlock("shippingmethodhead");
			}
			if (hasSalesNshippingMethod) {
				t.addBlock("salesNShipping");
			}
			// for checking the show Column Headings
			if (brandingTheme.isShowColumnHeadings()) {

				if (company.getPreferences().isTrackTax()
						&& brandingTheme.isShowTaxColumn()) {
					t.addBlock("vatBlock");
				}
				t.addBlock("showLabels");
			}

			// setting item description quantity, unit price, total price and
			// vat details
			List<TransactionItem> transactionItems = invoice
					.getTransactionItems();

			// List<Estimate> estimates = invoice.getEstimates();
			// if (estimates != null) {
			// for (Estimate estimate : estimates) {
			// for (TransactionItem item : estimate.getTransactionItems()) {
			// transactionItems.add(item);
			// }
			// }
			// }
			//
			// List<SalesOrder> salesOrders = invoice.getSalesOrders();
			// if (salesOrders != null) {
			// for (SalesOrder salesOrder : salesOrders) {
			// for (TransactionItem item : salesOrder
			// .getTransactionItems()) {
			// transactionItems.add(item);
			// }
			// }
			// }

			double currencyFactor = invoice.getCurrencyFactor();

			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {
				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");
				String qty = "";
				if (item.getQuantity() != null) {
					qty = forZeroAmounts(getDecimalsUsingMaxDecimals(item
							.getQuantity().getValue(), null, maxDecimalPoints));
				}
				String unitPrice = forZeroAmounts(largeAmountConversation(item
						.getUnitPrice() / currencyFactor));
				String totalPrice = largeAmountConversation(item.getLineTotal()
						/ currencyFactor);

				String vatAmount = getDecimalsUsingMaxDecimals(
						item.getVATfraction() / currencyFactor, null, 2);

				String name = item.getItem() != null ? item.getItem().getName()
						: item.getAccount().getName();
				t.setVariable("name", name);
				t.setVariable("discount",
						largeAmountConversation(item.getDiscount()));
				t.setVariable("description", description);
				t.setVariable("quantity", qty);
				t.setVariable("itemUnitPrice", unitPrice);
				t.setVariable("discount",
						largeAmountConversation(item.getDiscount()));
				t.setVariable("itemTotalPrice", totalPrice);

				if (company.getPreferences().isTrackTax()
						&& brandingTheme.isShowTaxColumn()) {
					if (item.getTaxCode() != null) {
						String vatRate = item.getTaxCode().getName();
						t.setVariable("itemVatRate", vatRate);
					}

					t.setVariable("itemVatAmount", vatAmount);
					t.addBlock("vatValueBlock");
				}
				t.addBlock("invoiceRecord");
			}

			// for displaying sub total, vat total, total
			String subtotal = largeAmountConversation(invoice.getNetAmount()
					/ currencyFactor);
			if (company.getPreferences().isTrackTax()) {

				t.setVariable("subTotal", subtotal);
				t.addBlock("subtotal");
				if (brandingTheme.isShowTaxColumn()) {
					t.setVariable(
							"vatTotal",
							largeAmountConversation((invoice.getTaxTotal() / currencyFactor)));
					t.addBlock("VatTotal");
				}
			}

			String total = largeAmountConversation(invoice.getTotal()
					/ currencyFactor);
			t.setVariable("total", total);
			t.setVariable("blankText", invoice.getMemo());

			t.setVariable("payment",
					largeAmountConversation(invoice.getPayments()
							/ currencyFactor));
			t.setVariable("balancedue",
					largeAmountConversation(invoice.getBalanceDue()
							/ currencyFactor));
			t.addBlock("itemDetails");

			t.setVariable("dueDate", invoice.getDueDate().toString());
			t.addBlock("dueDateDetails");

			boolean hasTermsNpaypalId = false;
			String termsNCondn = forNullValue(
					brandingTheme.getTerms_And_Payment_Advice()).replace("\n",
					"<br/>");

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = "";
			}
			if (termsNCondn.trim().length() > 0) {
				hasTermsNpaypalId = true;
				t.setVariable("termsAndPaymentAdvice", termsNCondn);
				t.addBlock("termsAndAdvice");
			}

			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = "";
			}
			if (paypalEmail.trim().length() > 0) {
				hasTermsNpaypalId = true;
				t.setVariable("email", paypalEmail);
				t.addBlock("paypalemail");
			}

			if (hasTermsNpaypalId) {
				t.addBlock("termsNpaypalId");
			}
			// for Vat String
			String vatString = "Tax No: "
					+ forNullValue(company.getPreferences()
							.getVATregistrationNumber());
			if (company.getPreferences().getVATregistrationNumber().length() > 0) {
				if (brandingTheme.isShowTaxNumber()) {
					t.setVariable("vatNum", vatString);
					t.addBlock("vat");
				}
			}

			// TODO for sortCode
			String sortCode = " Sort Code: "
					+ forNullValue(company.getSortCode());
			if (company.getSortCode() != null) {
				if (company.getSortCode().length() > 0) {
					t.setVariable("sortCode", sortCode);
					t.addBlock("sortcode");
				}
			}

			// TODO for BankAccountNumber
			String bankAccountNum = "Bank Account No: "
					+ forNullValue(company.getBankAccountNo());
			if (company.getBankAccountNo() != null) {
				if (company.getBankAccountNo().length() > 0) {
					t.setVariable("BankAccountNo", bankAccountNum);
					t.addBlock("bankAccountNum");
				}
			}

			// setting the theme styles
			t.setVariable("fontStyle", brandingTheme.getFont());
			t.setVariable("font", brandingTheme.getFontSize());
			t.setVariable(
					"bottomMargin",
					NumberFormat.getInstance().format(
							brandingTheme.getBottomMargin()));
			t.setVariable(
					"topMargin",
					NumberFormat.getInstance().format(
							brandingTheme.getTopMargin()));
			t.setVariable("title", brandingTheme.getOverDueInvoiceTitle());
			// t.setVariable("addressPadding",
			// String.valueOf(brandingTheme.getAddressPadding()));

			if (brandingTheme.isShowLogo()) {
				t.addBlock("logo");
			}

			// TODO for displaying regestration address and Company Registration
			// Number
			String regestrationAddress = "";
			Address reg = company.getRegisteredAddress();

			if (reg != null)
				regestrationAddress = ("&nbsp;Registered Address: "
						+ reg.getAddress1()
						+ forUnusedAddress(reg.getStreet(), true)
						+ forUnusedAddress(reg.getCity(), true)
						+ forUnusedAddress(reg.getStateOrProvinence(), true)
						+ forUnusedAddress(reg.getZipOrPostalCode(), true)
						+ forUnusedAddress(reg.getCountryOrRegion(), true) + ".");

			regestrationAddress = (company.getTradingName()
					+ "&nbsp;&nbsp;&nbsp;" + regestrationAddress + ((company
					.getRegistrationNumber() != null && !company
					.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
					+ company.getRegistrationNumber()
					: ""));

			if (regestrationAddress != null
					&& regestrationAddress.trim().length() > 0) {
				if (brandingTheme.isShowRegisteredAddress()) {
					// t.setVariable("tradingName", trName);
					// t.setVariable("regestrationAddress",
					// regestrationAddress);
					// t.addBlock("regestrationAddress");
				}
			}
			t.addBlock("theme");

			outPutString = t.getFileString();
			return outPutString;
			// OutputStream outputstream = new ByteArrayOutputStream();
			//
			// java.io.InputStream inputStream = new ByteArrayInputStream(
			// outPutString.getBytes());
			// InputStreamReader reader = new InputStreamReader(inputStream);

			// OutputStream templeteOutputStream = new
			// create output stream
			// then create input stream
			// /pass that input stream to render method

			// converter.generatePDF(template, sos, reader);

		} catch (Exception e) {
			System.err.println(e.getMessage() + "..." + e.getStackTrace()
					+ "..." + e.getLocalizedMessage());
		}
		return "";
	}

	private void externalizeStrings(MiniTemplator t) {
		AccounterMessages messages = Global.get().messages();
		Map<String, String> variables = t.getVariables();
		System.out.println(variables);
		t.setVariable("i18_Invoice_Number", messages.invoiceNo());
		t.setVariable("i18_Invoice_Date", messages.invoiceDate());
		t.setVariable("i18_Order_Number", messages.orderNumber());
		t.setVariable("i18_Customer_Number",
				messages.payeeNumber(messages.customer()));
		t.setVariable("i18_Bill_To", messages.billTo());
		t.setVariable("i18_Ship_To", messages.shipTo());
		t.setVariable("i18_Sales_Person", messages.salesPerson());
		t.setVariable("i18_Shipping_Method", messages.shippingMethod());
		t.setVariable("i18_Payment_Terms", messages.paymentTerms());
		t.setVariable("i18_Due_Date", messages.dueDate());
		t.setVariable("i18_Customer_TAX_Registration_Number",
				messages.customerTaxRegNo(messages.customer()));
		t.setVariable("i18_Currency", messages.currency());
		t.setVariable("i18_Name", messages.name());
		t.setVariable("i18_Description", messages.description());
		t.setVariable("i18_Qty", messages.qty());
		t.setVariable("i18_Unit_Price", messages.unitPrice());
		t.setVariable("i18_Discount", messages.discount());
		t.setVariable("i18_Total_Price", messages.totalPrice());
		t.setVariable("i18_TOTAL", messages.total());
		t.setVariable("i18_Payments", messages.payments());
		t.setVariable("i18_Balance_Due", messages.balanceDue());
		t.setVariable("i18_Sub_Total", messages.subTotal());
		t.setVariable("i18_tax", messages.tax());
		t.setVariable("i18_NetAmount", messages.netAmount());
		t.setVariable("i18_VATRate", messages.taxCode());
		t.setVariable("i18_VATAmount", messages.tax());
	}

	/*
	 * For Max DecimalPoints
	 */
	private int getMaxDecimals(Invoice inv) {
		String qty;
		String max;
		int temp = 0;
		for (TransactionItem item : inv.getTransactionItems()) {

			// qty = String.valueOf(item.getQuantity());
			qty = NumberFormat.getNumberInstance().format(
					item.getQuantity().getValue());

			max = qty.substring(qty.indexOf(".") + 1);
			if (!max.equals("0")) {
				if (temp < max.length()) {
					temp = max.length();
				}
			}

		}
		return temp;
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return ", " + add;
		} else {
			if (add != null && !add.equals(""))
				return add + "<br/>";
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
				qty = NumberFormat.getInstance().format(quantity);
			else
				qty = amount;
			max = qty.substring(qty.indexOf(".") + 1);
			if (maxDecimalPoint > max.length()) {
				for (int i = max.length(); maxDecimalPoint != i; i++) {
					qty = qty + "0";
				}
			}
		} else {
			qty = NumberFormat.getInstance().format(quantity);
			// qty = String.valueOf((long) quantity);
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

	public String getImage() {

		StringBuffer original = new StringBuffer();
		// String imagesDomain = "/do/downloadFileFromFile?";

		original.append("<img style='width:90px;height:90px' src='file:///");
		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ company.getId() + "/" + brandingTheme.getFileName());
		original.append("'/>");

		if (original.toString().contains("null")) {
			return "";
		}
		return original.toString();

	}

	@Override
	public String getFileName() {
		return "Invoice_" + this.invoice.getNumber();
	}

	@Override
	public String getFooter() {
		Location location = invoice.getLocation();
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();

		if (reg != null)
			regestrationAddress = (reg.getAddress1()
					+ forUnusedAddress(reg.getStreet(), true)
					+ forUnusedAddress(reg.getCity(), true)
					+ forUnusedAddress(reg.getStateOrProvinence(), true)
					+ forUnusedAddress(reg.getZipOrPostalCode(), true)
					+ forUnusedAddress(reg.getCountryOrRegion(), true) + ".");

		// TODO For setting the Contact Details
		// String contactDetails =
		// forNullValue(brandingTheme.getContactDetails())
		// .replace("\n", "<br/>");
		String contactDetails = forNullValue(brandingTheme.getContactDetails());
		if (contactDetails.contains("(None Added)")) {
			contactDetails = "";
		}
		regestrationAddress = (contactDetails
				+ "<br/><hr width = 100%>&nbsp;&nbsp;&nbsp;"
				+ (location == null ? company.getTradingName() : location
						.getCompanyName() == null ? company.getTradingName()
						: location.getCompanyName())
				+ "<br/>&nbsp;&nbsp;&nbsp;"
				+ (location == null ? regestrationAddress : location
						.getAddress() == null ? regestrationAddress : location
						.getAddress()) + ((company.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));

		return regestrationAddress;
	}
}
