package com.vimukti.accounter.core;

import java.io.File;
import java.util.Set;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.MiniTemplator;

/**
 * this class is used to generate PDF file for Credit note Object
 * 
 * @author vimukti28
 * 
 */
public class CreditNotePDFTemplete implements PrintTemplete {
	private CustomerCreditMemo memo;
	private BrandingTheme brandingTheme;
	private int maxDecimalPoints;
	private String templateName;

	Company company;
	private String companyId;

	public String getTempleteName() {
		return "templetes" + File.separator + templateName + ".html";
	}

	public CreditNotePDFTemplete(CustomerCreditMemo memo,
			BrandingTheme brandingTheme, Company company, String companyId,
			String templateName) {
		this.memo = memo;
		this.brandingTheme = brandingTheme;
		this.company = company;
		this.companyId = companyId;
		this.maxDecimalPoints = getMaxDecimals(memo);
		this.templateName = templateName;
	}

	public String getFileName() {
		return "CreditNote_Templete_" + memo.getNumber();
	}

	@Override
	public String getPdfData() {

		try {
			MiniTemplator t = new MiniTemplator(getTempleteName());
			// setting logo Image
			if (brandingTheme.isShowLogo()) {
				String logoAlligment = getLogoAlignment();
				t.setVariable("getLogoAlignment", logoAlligment);

				String image = getImage().toString();
				t.setVariable("logoImage", image);
				t.addBlock("showlogo");
			}

			t.setVariable("creditNoteNumber", memo.getNumber());
			t.setVariable("creditNoteDate", memo.getDate().toString());

			int customerNumber = memo.getCustomer().getNumber() == null ? 0
					: Integer.parseInt(memo.getCustomer().getNumber());

			if (customerNumber > 0) {
				t.setVariable("customerNumber", memo.getCustomer().getNumber());
				// t.addBlock("customernum");
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
			Customer customer = memo.getCustomer();
			Set<Contact> contacts = customer.getContacts();
			for (Contact contact : contacts) {
				if (contact.isPrimary()) {
					cname = contact.getName();

					if (contact.getBusinessPhone().trim().length() > 0)
						phone = contact.getBusinessPhone();

				}
			}
			// setting billing address
			Address bill = memo.getBillingAddress();
			String customerName = forUnusedAddress(
					memo.getCustomer().getName(), false);
			if (bill != null) {
				String customernameAddress = forUnusedAddress(cname, false)
						+ customerName
						+ forUnusedAddress(bill.getAddress1(), false)
						+ forUnusedAddress(bill.getStreet(), false)
						+ forUnusedAddress(bill.getCity(), false)
						+ forUnusedAddress(bill.getStateOrProvinence(), false)
						+ forUnusedAddress(bill.getZipOrPostalCode(), false)
						+ forUnusedAddress(bill.getCountryOrRegion(), false)
						+ forUnusedAddress("Phone : " + phone, false);

				if (customernameAddress.trim().length() > 0) {
					t.setVariable("customerNameNBillAddress",
							customernameAddress);
					t.addBlock("creditHead");

				}
			} else {
				t.setVariable("customerNameNBillAddress", customerName);
				t.addBlock("creditHead");
			}

			// t.setVariable("description", "Description");
			// t.setVariable("qty", "Qty");
			// t.setVariable("unitPrice", "Unit Price");
			// t.setVariable("totalPrice", "Total Price");

			// for checking to show column headings
			if (brandingTheme.isShowColumnHeadings()) {
				// for checking to show tax columns

				if (company.getPreferences().isTrackTax()
						&& brandingTheme.isShowTaxColumn()) {
					t.setVariable("VATRate", "Tax Code");
					t.setVariable("VATAmount", "Tax ");
					t.addBlock("vatBlock");
				}
				t.addBlock("showLabels");

			}

			// for displaying the credit item details
			if (!memo.getTransactionItems().isEmpty()) {

				for (TransactionItem item : memo.getTransactionItems()) {

					String description = forNullValue(item.getDescription());
					String qty = forZeroAmounts(getDecimalsUsingMaxDecimals(
							item.getQuantity().getValue(), null,
							maxDecimalPoints));
					String unitPrice = forZeroAmounts(largeAmountConversation(item
							.getUnitPrice()));
					String totalPrice = largeAmountConversation(item
							.getLineTotal());
					String vatRate = item.getTaxCode().getName();
					String vatAmount = getDecimalsUsingMaxDecimals(
							item.getVATfraction(), null, 2);

					String name = "";
					if (item.type == TransactionItem.TYPE_ITEM)
						name = item.getItem().getName();
					if (item.type == TransactionItem.TYPE_ACCOUNT)
						name = item.getAccount().getName();

					t.setVariable("name", name);
					t.setVariable("discount",
							largeAmountConversation(item.getDiscount()));

					t.setVariable("description", description);
					t.setVariable("quantity", qty);
					t.setVariable("unitPrice", unitPrice);
					t.setVariable("itemTotalPrice", totalPrice);

					if (company.getPreferences().isTrackTax()
							&& brandingTheme.isShowTaxColumn()) {
						t.setVariable("itemVatRate", vatRate);
						t.setVariable("itemVatAmount", vatAmount);
						t.addBlock("vatValueBlock");
					}
					t.addBlock("itemRecord");
				}
			}
			// for displaying the total price details

			String memoVal = forNullValue(memo.getMemo());
			String subTotal = largeAmountConversation(memo.getNetAmount());
			String vatTotal = largeAmountConversation(memo.getTotal()
					- memo.getNetAmount());
			String total = largeAmountConversation(memo.getTotal());

			t.setVariable("memoText", memoVal);
			if (company.getPreferences().isTrackTax()) {
				t.setVariable("NetAmount", "Net Amount");
				t.setVariable("subTotal", subTotal);
				t.addBlock("subtotal");

				// if (brandingTheme.isShowVatColumn()) {
				// t.setVariable("vatlabel", "Tax ");
				// t.setVariable("vatTotalValue", vatTotal);
				// t.addBlock("VatTotal");
				// }

			}
			t.setVariable("total", total);
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

			// for Vat String
			String vatString = "Tax No: "
					+ forNullValue(company.getPreferences()
							.getVATregistrationNumber());
			if (company.getPreferences().getVATregistrationNumber() != null) {
				if (company.getPreferences().getVATregistrationNumber()
						.length() > 0) {

					// if (brandingTheme.isShowTaxNumber()) {
					t.setVariable("vatCode", vatString);
					t.addBlock("vatCodeDetails");
				}
			}

			// for sortCode
			String sortCode = " Sort Code: "
					+ forNullValue(company.getSortCode());
			if (company.getSortCode() != null) {
				if (company.getSortCode().length() > 0) {
					t.setVariable("sortCode", sortCode);
					t.addBlock("sortCodeDetails");
				}
			}

			// for BankAccountNumber
			String bankAccountNum = "Bank Account No: "
					+ forNullValue(company.getBankAccountNo());
			if (company.getBankAccountNo() != null) {
				if (company.getBankAccountNo().length() > 0) {
					t.setVariable("bankAccount", bankAccountNum);
					t.addBlock("bankAccountDetails");
				}
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

			String outPutString = t.getFileString();
			System.err.println(outPutString);
			return outPutString;
		} catch (Exception e) {
			System.err.println("credit memeo err......." + e.getMessage()
					+ "..." + e.getStackTrace());
		}
		return "";
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return add + ",&nbsp;";
		} else {
			if (add != null && !add.equals(""))
				return add + "<br/>&nbsp;";
		}
		return "";
	}

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
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

	/*
	 * For Max DecimalPoints
	 */
	private int getMaxDecimals(CustomerCreditMemo memo) {
		String qty;
		String max;
		int temp = 0;
		for (TransactionItem item : memo.getTransactionItems()) {
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

	private String getLogoAlignment() {
		String logoAlignment = null;
		if (brandingTheme.getLogoAlignmentType() == 1) {
			logoAlignment = "left";
		} else {
			logoAlignment = "right";
		}
		return logoAlignment;
	}

	private StringBuffer getImage() {
		StringBuffer original = new StringBuffer();
		// String imagesDomain = "/do/downloadFileFromFile?";

		original.append("<img style='width:130px;height:120px'  src='file:///");
		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ companyId + "/" + brandingTheme.getFileName());
		original.append("'/>");
		return original;
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

		// TODO For setting the Contact Details
		String contactDetails = forNullValue(brandingTheme.getContactDetails());
		if (contactDetails.contains("(None Added)")) {
			contactDetails = "";
		}

		regestrationAddress = (contactDetails
				+ "<br/><hr width = 100%>&nbsp;&nbsp;&nbsp;"
				+ company.getFullName() + "<br/>&nbsp;&nbsp;&nbsp;"
				+ regestrationAddress + ((company.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));
		return regestrationAddress;
	}

}
