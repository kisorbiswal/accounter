package com.vimukti.accounter.core;

import java.io.File;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.MiniTemplator;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class QuotePdfTemplate implements PrintTemplete {

	private final Estimate estimate;
	private final BrandingTheme brandingTheme;
	private final Company company;
	private final String templateName;

	public QuotePdfTemplate(Estimate estimate, BrandingTheme brandingTheme,
			Company company, String templateName) {
		this.estimate = estimate;
		this.brandingTheme = brandingTheme;
		this.company = company;
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

			String invNumber = forNullValue(estimate.getNumber());
			if (invNumber.trim().length() > 0) {

				t.setVariable("invoiceNumber", invNumber);
				t.addBlock("invNumberHead");
			}

			// setting estimate delivery date

			String invDate = Utility.getDateInSelectedFormat(estimate
					.getDeliveryDate());
			if (invDate.trim().length() > 0) {

				t.setVariable("deliveryDate", invDate);

			}

			// setting estimate expiration date

			String expirationDate = Utility.getDateInSelectedFormat(estimate
					.getExpirationDate());
			if (invDate.trim().length() > 0) {

				t.setVariable("expirationDate", expirationDate);

			}

			// setting phone number
			String phone = estimate.getPhone();
			if (phone.trim().length() > 0) {
				t.setVariable("phone", phone);
				t.addBlock("phoneBlock");
			}

			// setting invoice customer number

			String invCustomerNum = forNullValue(estimate.getCustomer()
					.getNumber());
			if (invCustomerNum.trim().length() > 0) {
				t.setVariable("invoiceCustomerNumber", invCustomerNum);
				t.addBlock("invCustNumHead");
			}

			// setting payment terms
			PaymentTerms paymentterm = estimate.getPaymentTerm();
			String payterm = paymentterm != null ? paymentterm.getName() : "";
			if (payterm.trim().length() > 0) {

				t.setVariable("paymentTerms", payterm);

				t.addBlock("paymentTermsBlock");
			}
			// set status
			int statusId = estimate.getStatus();
			String status = getStatusString(statusId);
			t.setVariable("status", status);

			// for primary curreny
			Currency currency = estimate.getCustomer().getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					t.setVariable("currency", currency.getFormalName().trim());
					t.addBlock("currency");
				}

			// for customer VAT Registration Number
			String vatRegistrationNumber = estimate.getCustomer()
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
			String phoneStr = "";
			boolean hasPhone = false;
			Customer customer = estimate.getCustomer();
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
			Contact selectedContact = estimate.getContact();
			if (selectedContact != null) {
				cname = selectedContact.getName().trim();
				if (selectedContact.getBusinessPhone().trim().length() > 0)
					phoneStr = selectedContact.getBusinessPhone();
				if (phoneStr.trim().length() > 0) {
					// If phone variable has value, then only we need to display
					// the text 'phone'
					hasPhone = true;
				}
			}

			// setting billing address
			Address bill = estimate.getAddress();
			String customerName = forUnusedAddress(estimate.getCustomer()
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
					billAddress.append(forUnusedAddress("Phone : " + phoneStr,
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
			Address shpAdres = estimate.getShippingAdress();
			if (shpAdres != null) {
				shipAddress = forUnusedAddress(
						estimate.getCustomer().getName(), false)
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

			// for checking the show Column Headings
			if (brandingTheme.isShowColumnHeadings()) {

				if (company.getPreferences().isTrackTax()
						&& brandingTheme.isShowTaxColumn()) {
					t.addBlock("vatBlock");
				}
				if (company.getPreferences().isTrackDiscounts()) {

					t.addBlock("discountBlock");
				}
				t.addBlock("showLabels");
			}

			// setting item description quantity, unit price, total price and
			// vat details
			List<TransactionItem> transactionItems = estimate
					.getTransactionItems();

			double currencyFactor = estimate.getCurrencyFactor();

			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {
				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");
				String qty = "";
				if (item.getQuantity() != null) {
					qty = String.valueOf(item.getQuantity().getValue());
				}
				String unitPrice = Utility.decimalConversation(item
						.getUnitPrice() / currencyFactor);
				String totalPrice = Utility.decimalConversation(item
						.getLineTotal() / currencyFactor);

				String vatAmount = Utility.decimalConversation(item
						.getVATfraction() / currencyFactor);

				String name = item.getItem() != null ? item.getItem().getName()
						: item.getAccount().getName();
				t.setVariable("name", name);
				t.setVariable("description", description);
				t.setVariable("quantity", qty);
				t.setVariable("itemUnitPrice", unitPrice);

				if (company.getPreferences().isTrackDiscounts()) {
					// if Discounts is enabled in Company Preferences, then
					// only we need to show Discount Column
					t.setVariable("discount",
							Utility.decimalConversation(item.getDiscount()));
					t.addBlock("discountValueBlock");
				}
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
				t.addBlock("quoteRecord");
			}

			// for displaying sub total, vat total, total
			String subtotal = Utility.decimalConversation(estimate
					.getNetAmount() / currencyFactor);
			if (company.getPreferences().isTrackTax()) {

				t.setVariable("subTotal", subtotal);
				t.addBlock("subtotal");
				if (brandingTheme.isShowTaxColumn()) {
					t.setVariable(
							"vatTotal",
							Utility.decimalConversation((estimate.getTaxTotal() / currencyFactor)));
					t.addBlock("VatTotal");
				}
			}

			String total = Utility.decimalConversation(estimate.getTotal()
					/ currencyFactor);
			t.setVariable("total", total);

			// if (estimate.getMemo().trim().length() > 0) {
			t.setVariable("blankText", estimate.getMemo());
			// t.addBlock("memoblock");
			// }
			t.addBlock("itemDetails");
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
			t.setVariable("title", brandingTheme.getQuoteTitle());
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
			System.err.println(outPutString);
			return outPutString;

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
		t.setVariable("i18_Invoice_Number", messages.quoteNo());
		t.setVariable("i18_Status", messages.status());
		t.setVariable("i18_DeliveryDate", messages.deliveryDate());
		t.setVariable("i18_ExpirationDate", messages.expirationDate());
		t.setVariable("i18_Phone", messages.phone());
		t.setVariable("i18_Customer_Number",
				messages.payeeNumber(messages.customer()));
		t.setVariable("i18_Bill_To", messages.billTo());
		t.setVariable("i18_Ship_To", messages.shipTo());
		t.setVariable("i18_Sales_Person", messages.salesPerson());
		t.setVariable("i18_Payment_Terms", messages.paymentTerms());
		// t.setVariable("i18_Payment_Terms", messages.paymentTerms());
		// t.setVariable("i18_Due_Date", messages.dueDate());
		t.setVariable("i18_Customer_TAX_Registration_Number",
				messages.customerTaxRegNo(messages.customer()));
		t.setVariable("i18_Currency", messages.currency());
		t.setVariable("i18_Name", messages.name());
		t.setVariable("i18_Description", messages.description());
		t.setVariable("i18_Qty", messages.quantity());
		t.setVariable("i18_Unit_Price", messages.unitPrice());
		t.setVariable("i18_Discount", messages.discount());
		t.setVariable("i18_Total_Price", messages.totalPrice());
		t.setVariable("i18_TOTAL", messages.total());
		t.setVariable("i18_Sub_Total", messages.subTotal());
		t.setVariable("i18_tax", messages.tax());
		t.setVariable("i18_NetAmount", messages.netAmount());
		t.setVariable("i18_VATRate", messages.taxCode());
		t.setVariable("i18_VATAmount", messages.tax());
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
		return "Quote_" + this.estimate.getNumber();
	}

	@Override
	public String getFooter() {
		Location location = estimate.getLocation();
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
		String locCompanyName = null;
		if (location != null) {
			try {
				if (location.getCompanyName() != null)
					locCompanyName = location.getCompanyName();
			} catch (Exception e) {

			}
		} else {
			locCompanyName = company.getTradingName();
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

		case ClientEstimate.STATUS_APPLIED:
			return messages.closed();

		default:
			break;
		}
		return "";
	}

}