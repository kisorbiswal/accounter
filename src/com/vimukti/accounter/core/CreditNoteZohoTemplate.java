package com.vimukti.accounter.core;

import java.io.File;

import org.hibernate.Session;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.MiniTemplator;

public class CreditNoteZohoTemplate implements PrintTemplete {
	private CustomerCreditMemo memo;
	private BrandingTheme brandingTheme;
	private int maxDecimalPoints;
	// private static final String templateFileName = "templetes" +
	// File.separator
	// + "CreditMemoTemplete.html";

	Company company;

	public CreditNoteZohoTemplate(CustomerCreditMemo memo,
			BrandingTheme brandingTheme, Company company) {
		this.memo = memo;
		this.brandingTheme = brandingTheme;
		this.company = company;
		this.maxDecimalPoints = getMaxDecimals(memo);
	}

	public String getFileName() {
		return "CreditNote_Templete_" + memo.getNumber();
	}

	public String getTempleteName() {

		String templeteName = brandingTheme.getCreditNoteTempleteName();

		return "templetes" + File.separator + "ModernCredit" + ".html";

	}

	@Override
	public String getPdfData() {
		MiniTemplator t;
		try {
			t = new MiniTemplator(getTempleteName());

			try {

				String cmpAdd = "";
				Address cmpTrad = company.getTradingAddress();
				if (cmpTrad.getType() == Address.TYPE_COMPANY_REGISTRATION) {
					if (cmpTrad != null)
						cmpAdd = forUnusedAddress(cmpTrad.getAddress1(), false)
								+ forUnusedAddress(cmpTrad.getStreet(), false)
								+ forUnusedAddress(cmpTrad.getCity(), false)
								+ forUnusedAddress(
										cmpTrad.getStateOrProvinence(), false)
								+ forUnusedAddress(
										cmpTrad.getZipOrPostalCode(), false)
								+ forUnusedAddress(
										cmpTrad.getCountryOrRegion(), false);
				}

				if (cmpAdd.equals("")) {
					// String contactDetails = brandingTheme.getContactDetails()
					// != null
					// ? brandingTheme
					// .getContactDetails() : this.company.getName();
					cmpAdd = forNullValue(company.getTradingName());
				} else {
					cmpAdd = forNullValue(company.getTradingName()) + "<br/>"
							+ cmpAdd;
				}

				String companyName = forNullValue(company.getFullName());
				t.setVariable("companyName", companyName);
				t.setVariable("companyRegistrationAddress", cmpAdd);

				t.setVariable("creditNoteNumber", memo.getNumber());
				t.setVariable("creditNoteDate", memo.getDate().toString());
				t.setVariable("customerNumber", memo.getCustomer().getNumber());

				// for displaying customer name and billing Address
				String customernameAddress = "";
				Address bill = memo.getBillingAddress();
				if (bill != null) {
					customernameAddress = forUnusedAddress(memo.getCustomer()
							.getName(), false)
							+ forUnusedAddress(bill.getAddress1(), false)
							+ forUnusedAddress(bill.getStreet(), false)
							+ forUnusedAddress(bill.getCity(), false)
							+ forUnusedAddress(bill.getStateOrProvinence(),
									false)
							+ forUnusedAddress(bill.getZipOrPostalCode(), false)
							+ bill.getCountryOrRegion();
				}

				t.setVariable("customerNameNBillAddress", customernameAddress);
				t.addBlock("creditHead");
				// for checking to show column headings
				if (brandingTheme.isShowColumnHeadings()) {
					// for checking to show tax columns
					if (brandingTheme.isShowTaxColumn()) {

						String vatrate = getVendorString("VAT Rate", "Tax Rate");
						String vatamount = getVendorString("VAT Amount",
								"Tax Amount");

						t.setVariable("vatRate", vatrate);
						t.setVariable("vatAmount", vatamount);

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
						String vatRate = String.valueOf(Utility.getVATItemRate(
								item.getTaxCode(), true));
						String vatAmount = getDecimalsUsingMaxDecimals(
								item.getVATfraction(), null, 2);

						t.setVariable("description", description);
						t.setVariable("quantity", qty);
						t.setVariable("unitPrice", unitPrice);
						t.setVariable("itemTotalPrice", totalPrice);

						if (brandingTheme.isShowTaxColumn()) {
							t.setVariable("itemVatRate", vatRate);
							t.setVariable("itemVatAmount", vatAmount);
							t.addBlock("vatValueBlock");
						}

						t.addBlock("itemRecord");
					}
				}
				// for displaying the total price details

				String memoVal = memo.getMemo();
				if (memoVal != null && memoVal.trim().length() > 0) {
					t.setVariable("memoText", memoVal);
					t.addBlock("memoblock");
				}
				String vatString = getVendorString("VAT Total ", "Tax Total ");
				String subTotal = largeAmountConversation(memo.getNetAmount());
				String vatTotal = largeAmountConversation(memo.getTotal()
						- memo.getNetAmount());
				String total = largeAmountConversation(memo.getTotal());

				t.setVariable("memoText", memoVal);
				t.setVariable("subTotal", subTotal);
				if (brandingTheme.isShowTaxColumn()) {
					t.setVariable("vatlabel", vatString);
					t.setVariable("vatTotalValue", vatTotal);
					t.addBlock("VatTotal");
				}

				t.setVariable("total", total);
				t.addBlock("itemDetails");

				String regAdd = "";

				Address reg = company.getRegisteredAddress();
				if (reg.getType() == Address.TYPE_COMPANY) {
					if (reg != null)
						regAdd = "Register Address: "
								+ forUnusedAddress(reg.getAddress1(), true)
								+ forUnusedAddress(reg.getStreet(), true)
								+ forUnusedAddress(reg.getCity(), true)
								+ forUnusedAddress(reg.getStateOrProvinence(),
										true)
								+ forUnusedAddress(reg.getZipOrPostalCode(),
										true) + reg.getCountryOrRegion();
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
				return ", " + add;
		} else {
			if (add != null && !add.equals(""))
				return add + "<br/>";
		}
		return "";
	}

	private String getVendorString(String forUk, String forUs) {
		return company.getAccountingType() == company.ACCOUNTING_TYPE_US ? forUs
				: forUk;

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
		if (brandingTheme.getPageSizeType() == 1) {
			logoAlignment = "left";
		} else {
			logoAlignment = "right";
		}
		return logoAlignment;
	}

	private StringBuffer getImage() {
		StringBuffer original = new StringBuffer();
		// String imagesDomain = "/do/downloadFileFromFile?";
		Session session = HibernateUtil.getCurrentSession();
		// BizantraCompany bizantraCompany = (BizantraCompany) session.get(
		// BizantraCompany.class, 1L);
		original.append("<img src='file:///");
		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ company.getAccountingType() + "/"
				+ brandingTheme.getFileName());
		original.append("'/>");
		return original;
	}

}
