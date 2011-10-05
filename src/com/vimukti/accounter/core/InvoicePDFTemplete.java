package com.vimukti.accounter.core;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.MiniTemplator;

/**
 * this class is used to generate Invoice report in PDF format
 * 
 */
public class InvoicePDFTemplete implements PrintTemplete {
	private Invoice invoice;
	private BrandingTheme brandingTheme;
	private int maxDecimalPoints;
	// private static final String templateFileName = "templetes" +
	// File.separator
	// + "InvoiceTemplete.html";
	private Company company;
	private String companyId;
	private String templateName;

	public InvoicePDFTemplete(Invoice invoice, BrandingTheme brandingTheme,
			Company company, String companyId, String templateName) {
		this.invoice = invoice;
		this.brandingTheme = brandingTheme;
		this.company = company;
		this.companyId = companyId;
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
			String image = getImage();

			// setting logo Image
			if (brandingTheme.isShowLogo()) {
				String logoAlligment = getLogoAlignment();
				t.setVariable("getLogoAlignment", logoAlligment);

				t.setVariable("logoImage", image);
				t.addBlock("showlogo");
			}

			// TODO For setting the Contact Details
			String contactDetails = forNullValue(
					brandingTheme.getContactDetails()).replace("\n", "<br/>");
			if (contactDetails.contains("(None Added)")) {
				contactDetails = "";
			}
			contactDetails = forNullValue(company.getFullName()) + "<br/>"
					+ contactDetails;
			t.setVariable("companyDetails", contactDetails);

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
			Currency primaryCurrency = company.getPreferences()
					.getPrimaryCurrency();
			if (primaryCurrency != null)
				if (primaryCurrency.getFormalName().trim().length() > 0) {
					t.setVariable("currency", primaryCurrency.getFormalName()
							.trim());
					t.addBlock("currency");
				}

			// for getting customer contact name
			String cname = "";
			String phone = "";
			String email = "";
			Customer customer = invoice.getCustomer();
			Set<Contact> contacts = customer.getContacts();
			for (Contact contact : contacts) {
				if (contact.isPrimary()) {
					cname = contact.getName();

					if (contact.getBusinessPhone().trim().length() > 0)
						phone = contact.getBusinessPhone();

					if (contact.getEmail().trim().length() > 0)
						email = contact.getEmail();
				}
			}
			// setting billing address
			Address bill = invoice.getBillingAddress();
			String customerName = forUnusedAddress(invoice.getCustomer()
					.getName(), false);
			if (bill != null) {
				String billAddress = forUnusedAddress(cname, false)
						+ customerName
						+ forUnusedAddress(bill.getAddress1(), false)
						+ forUnusedAddress(bill.getStreet(), false)
						+ forUnusedAddress(bill.getCity(), false)
						+ forUnusedAddress(bill.getStateOrProvinence(), false)
						+ forUnusedAddress(bill.getZipOrPostalCode(), false)
						+ forUnusedAddress(bill.getCountryOrRegion(), false)
						+ forUnusedAddress("Phone : " + phone, false)
						+ forUnusedAddress("Email : " + email, false);

				if (billAddress.trim().length() > 0) {
					t.setVariable("billingAddress", billAddress);
					t.addBlock("billhead");
				}
			} else {
				t.setVariable("billingAddress", customerName);
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
					t.setVariable("VATRate", "Tax Code");
					t.setVariable("VATAmount", "Tax ");
					t.addBlock("vatBlock");
				}
				t.addBlock("showLabels");
			}

			// setting item description quantity, unit price, total price and
			// vat details
			List<TransactionItem> transactionItems = invoice
					.getTransactionItems();
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {
				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				String qty = forZeroAmounts(getDecimalsUsingMaxDecimals(item
						.getQuantity().getValue(), null, maxDecimalPoints));
				String unitPrice = forZeroAmounts(largeAmountConversation(item
						.getUnitPrice()));
				String totalPrice = largeAmountConversation(item.getLineTotal());

				String vatAmount = getDecimalsUsingMaxDecimals(
						item.getVATfraction(), null, 2);

				t.setVariable("name", item.getItem().getName());
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
			String subtotal = largeAmountConversation(invoice.getNetAmount());
			if (company.getPreferences().isTrackTax()) {
				t.setVariable("NetAmount", "Net Amount");
				t.setVariable("subTotal", subtotal);
				t.addBlock("subtotal");
				if (brandingTheme.isShowTaxColumn()) {
					t.setVariable("vatlabel", "Tax ");
					t.setVariable("vatTotal",
							largeAmountConversation((invoice.getTaxTotal())));
					t.addBlock("VatTotal");
				}
			}

			String total = largeAmountConversation(invoice.getTotal());
			t.setVariable("total", total);
			t.setVariable("blankText", invoice.getMemo());

			t.setVariable("payment",
					largeAmountConversation(invoice.getPayments()));
			t.setVariable("balancedue",
					largeAmountConversation(invoice.getBalanceDue()));
			t.addBlock("itemDetails");

			t.setVariable("dueDate", invoice.getDueDate().toString());
			t.addBlock("dueDateDetails");

			boolean hasTermsNpaypalId= false;
			String termsNCondn = forNullValue(
					brandingTheme.getTerms_And_Payment_Advice()).replace("\n",
					"<br/>");

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = "";
			}
			if (termsNCondn.trim().length() > 0) {
				hasTermsNpaypalId= true;
				t.setVariable("termsAndPaymentAdvice", termsNCondn);
				t.addBlock("termsAndAdvice");
			}

			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = "";
			}
			if (paypalEmail.trim().length() > 0) {
				hasTermsNpaypalId= true;
				t.setVariable("email", paypalEmail);
				t.addBlock("paypalemail");
			}

			if(hasTermsNpaypalId)
			{
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
			t.setVariable("bottomMargin",
					String.valueOf(brandingTheme.getBottomMargin()));
			t.setVariable("topMargin",
					String.valueOf(brandingTheme.getTopMargin()));
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

			regestrationAddress = (company.getFullName() + "&nbsp;&nbsp;&nbsp;"
					+ regestrationAddress + ((company.getRegistrationNumber() != null && !company
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
			System.err.println(outPutString);
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

	/*
	 * For Max DecimalPoints
	 */
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

	public String getImage() {

		StringBuffer original = new StringBuffer();
		// String imagesDomain = "/do/downloadFileFromFile?";

		original.append("<img style='width:130px;height:120px' src='file:///");
		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ companyId + "/" + brandingTheme.getFileName());
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

		regestrationAddress = (company.getFullName() + "&nbsp;&nbsp;&nbsp;"
				+ regestrationAddress + ((company.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));
		return regestrationAddress;
	}

}
