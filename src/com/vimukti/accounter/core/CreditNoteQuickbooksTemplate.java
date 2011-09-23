package com.vimukti.accounter.core;

import java.io.File;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.MiniTemplator;

public class CreditNoteQuickbooksTemplate implements PrintTemplete {
	private CustomerCreditMemo memo;
	private BrandingTheme brandingTheme;
	private int maxDecimalPoints;
	private String companyId;

	Company company;

	public String getTempleteName() {

		String templeteName = brandingTheme.getCreditNoteTempleteName();

		return "templetes" + File.separator + "PlainCredit" + ".html";

	}

	public CreditNoteQuickbooksTemplate(CustomerCreditMemo memo,
			BrandingTheme brandingTheme, Company company, String companyId) {
		this.memo = memo;
		this.brandingTheme = brandingTheme;
		this.company = company;
		this.companyId = companyId;
		this.maxDecimalPoints = getMaxDecimals(memo);
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

			String cmpAdd = "";
			Address cmpTrad = company.getRegisteredAddress();
			if (cmpTrad != null) {

				cmpAdd = forUnusedAddress(cmpTrad.getAddress1(), false)
						+ forUnusedAddress(cmpTrad.getStreet(), false)
						+ forUnusedAddress(cmpTrad.getCity(), false)
						+ forUnusedAddress(cmpTrad.getStateOrProvinence(),
								false)
						+ forUnusedAddress(cmpTrad.getZipOrPostalCode(), false)
						+ forUnusedAddress(cmpTrad.getCountryOrRegion(), false);
			}

			if (cmpAdd.equals("")) {
				// String contactDetails = brandingTheme.getContactDetails() !=
				// null
				// ? brandingTheme
				// .getContactDetails() : this.company.getName();
				cmpAdd = forNullValue(company.getFullName());
			} else {
				cmpAdd = forNullValue(company.getFullName()) + "<br/>" + cmpAdd;
			}

			// TODO For setting the Contact Details
			String contactDetails = forNullValue(brandingTheme
					.getContactDetails());
			if (contactDetails.equalsIgnoreCase("(None Added)")) {
				contactDetails = "";
			}

			t.setVariable("companyName", cmpAdd);
			t.setVariable("companyRegistrationAddress", contactDetails);

			t.setVariable("creditNoteNumber", memo.getNumber());
			t.setVariable("creditNoteDate", memo.getDate().toString());

			int customerNumber = memo.getCustomer().getNumber() == null ? 0
					: Integer.parseInt(memo.getCustomer().getNumber());

			if (customerNumber > 0) {
				t.setVariable("customerNumber", memo.getCustomer().getNumber());
				t.addBlock("customernum");
			}
			t.addBlock("creditHead");

			// for displaying customer name and billing Address
			String customernameAddress = forUnusedAddress(memo.getCustomer()
					.getName(), false);
			Address bill = memo.getBillingAddress();
			if (bill != null) {
				customernameAddress = "<div align=\"left\">&nbsp;"
						+ forUnusedAddress(memo.getCustomer().getName(), false)
						+ forUnusedAddress(bill.getAddress1(), false)
						+ forUnusedAddress(bill.getStreet(), false)
						+ forUnusedAddress(bill.getCity(), false)
						+ forUnusedAddress(bill.getStateOrProvinence(), false)
						+ forUnusedAddress(bill.getZipOrPostalCode(), false)
						+ bill.getCountryOrRegion() + "</div>";
			}

			t.setVariable("customerNameNBillAddress", customernameAddress);
			t.addBlock("billAddress");

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

					t.setVariable("name", item.getItem().getName());
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
			if (memoVal != null && memoVal.trim().length() > 0) {
				t.setVariable("memoText", memoVal);
				t.addBlock("memoblock");
			}

			String subTotal = largeAmountConversation(memo.getNetAmount());
			String vatTotal = largeAmountConversation(memo.getTotal()
					- memo.getNetAmount());
			String total = largeAmountConversation(memo.getTotal());
			if (company.getPreferences().isTrackTax()) {
				t.setVariable("NetAmount", "Net Amount");
				t.setVariable("subTotal", subTotal);
				t.addBlock("subtotal");

//				if (brandingTheme.isShowVatColumn()) {
//					t.setVariable("vatlabel", "Tax ");
//					t.setVariable("vatTotalValue", vatTotal);
//					t.addBlock("VatTotal");
//				}

			}

			t.setVariable("total", total);
			t.addBlock("itemDetails");

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
			String regAdd = "";

			Address reg = company.getRegisteredAddress();
			if (reg.getType() == Address.TYPE_COMPANY) {
				if (reg != null)
					regAdd = "Register Address: "
							+ forUnusedAddress(reg.getAddress1(), true)
							+ forUnusedAddress(reg.getStreet(), true)
							+ forUnusedAddress(reg.getCity(), true)
							+ forUnusedAddress(reg.getStateOrProvinence(), true)
							+ forUnusedAddress(reg.getZipOrPostalCode(), true)
							+ reg.getCountryOrRegion();
			}

			regAdd = company.getFullName()
					+ regAdd
					+ ((company.getRegistrationNumber() != null && !company
							.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
							+ company.getRegistrationNumber()
							: "");

			t.setVariable("compRegNamenNumber", regAdd);
			t.addBlock("regAddress");

			String outPutString = t.getFileString();
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
		original.append("<img src='file:///");
		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ companyId + "/" + brandingTheme.getFileName());
		original.append("'/>");
		return original;
	}

}
