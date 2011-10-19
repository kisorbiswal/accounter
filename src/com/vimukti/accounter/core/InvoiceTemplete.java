package com.vimukti.accounter.core;

import com.vimukti.accounter.main.ServerConfiguration;

/**
 * 
 * @author Narendra nnvd
 * 
 */
public class InvoiceTemplete extends TemplateBuilder implements ITemplate {

	private Invoice invoice;
	private String columnHeading, columnDataHtml, vatColumnHeading,
			subTotalHtml, vatTotalHtml, totalHtml, vatRegNumberHtml,
			initFooterTableHtml, sortCodeHtml, bankAccountNumberHtml,
			footerEndingHtml;
	private int maxDecimalPoints;
	private BrandingTheme brandingTheme;

	public InvoiceTemplete(Invoice invoice, BrandingTheme brandingTheme,
			String footerImageUrl, String stylefile) {
		super(invoice.getCompany());
		this.invoice = invoice;
		this.maxDecimalPoints = getMaxDecimals(invoice);
		this.brandingTheme = brandingTheme;
		imgUrl = footerImageUrl;
		style1 = stylefile;
		init();
	}

	private StringBuffer getImage() {
		StringBuffer original = new StringBuffer();
		// String imagesDomain = "/do/downloadFileFromFile?";
		original.append("<img src='file:///");
		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ company.getDisplayName() + "/" + brandingTheme.getFileName());
		original.append("'/>");
		return original;
	}

	private int getFontSize(int i) {
		int size = Integer.valueOf(brandingTheme.getFontSize()
				.replace("pt", "")) + i;
		return size;
	}

	private String getUnits() {
		String units = null;
		if (brandingTheme.getPageSizeType() == 1) {
			units = "cm";
		} else {
			units = "in";
		}
		return units;
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

	/**
	 * HEADER
	 */
	@Override
	public void initHeader() {

		String cmpAdd = "<br/><br/><br/><br/><br/>";
		Address registeredAddress = company.getRegisteredAddress();
		if (registeredAddress != null) {
			cmpAdd = ("<br><font style=\"font-family:"
					+ brandingTheme.getFont()
					+ "; color:#504040;\">"
					+ forUnusedAddress(registeredAddress.getAddress1(), false)
					+ forUnusedAddress(registeredAddress.getStreet(), false)
					+ forUnusedAddress(registeredAddress.getCity(), false)
					+ forUnusedAddress(
							registeredAddress.getStateOrProvinence(), false)
					+ forUnusedAddress(registeredAddress.getZipOrPostalCode(),
							false)
					+ forUnusedAddress(registeredAddress.getCountryOrRegion(),
							false) + "</font></br>");
		}

		if (cmpAdd.equals("<br/><br/><br/><br/><br/>")) {
			String contactDetails = brandingTheme.getContactDetails() != null ? brandingTheme
					.getContactDetails() : this.company.getTradingName();
			cmpAdd = ("<p style=\"font-family:" + brandingTheme.getFont()
					+ "; font-size:" + getFontSize(6) + "pt;\"><strong> "
					+ forNullValue(TemplateBuilder.getCmpName())
					+ "</strong></font>" + "<table><tr><td>"
					+ contactDetails.replace(",", ",</td></tr><tr><td>") + "</td></tr></table> </p>");
		} else {
			cmpAdd = ("<p style=\"font-family:" + brandingTheme.getFont()
					+ "; font-size:" + getFontSize(6) + "pt;\"><strong> "
					+ forNullValue(TemplateBuilder.getCmpName())
					+ "</strong></font>" + cmpAdd + "</p>");
		}

		String header1 = "";
		String imgCode = "<table width=\"100%\" cellpadding=\"25px\"><tr><td align=\""
				+ getLogoAlignment()
				+ "\">"
				+ getImage()
				+ "</td></tr></table>";
		if (brandingTheme.isShowLogo()) {
			header1 = header1 + imgCode;
		}

		headerHtml = (header1
				+ "<table style=\"width: 100%; height: 100%;\" cellspacing=\"10\"><tr><td style=\"height:"
				+ getUnits()
				+ "\"><p><br></p></td></tr><tr style=\"width:100%\"><td></td><td align=\"center;\" style=\"width:40%\"><div class=\"gwt-HTML\" style=\" margin-right: 43px;\"><p align=\"center\" style=\"font-family:"
				+ brandingTheme.getFont()
				+ "; font-size:"
				+ getFontSize(9)
				+ "pt;\"><font color=\"black\"><strong>Invoice</strong></font></p><div></td><td></td></tr><tr><td style=\"vertical-align: top;\" align=\"left\"><div class=\"gwt-HTML\" style=\" margin-left: 43px;\">"
				+ cmpAdd
				+ "</div></td><td></td><td style=\"vertical-align: top; width:30%\" align=\"right\"><table style=\"width:100%; font-size: "
				+ getFontSize(0)
				+ "pt; border-collapse: collapse; table-layout: fixed; font-family: "
				+ brandingTheme.getFont()
				+ ";\"><colgroup><col></colgroup><tr><td style=\"border: 1px ridge grey; padding: 6px; background: #f2f2f2; font-weight: bold; \"> Invoice Number</td><td align=\"right\" width=\"30%\" style=\"border: 1px ridge grey; padding: 6px;\">"
				+ forNullValue(invoice.getNumber())
				+ "</td></tr><tr><td style=\"border: 1px ridge grey; padding: 6px; background: #f2f2f2; font-weight: bold; \">Invoice Date</td><td align=\"right\" width=\"57%\" style=\"border: 1px ridge grey; padding: 6px;\">"
				+ invoice.getDate()
				+ "</td></tr><tr><td style=\"border: 1px ridge grey; padding: 6px; background: #f2f2f2; font-weight: bold;\"> Order Number</td><td align=\"right\" width=\"57%\" style=\"border: 1px ridge grey; padding: 6px;\">"
				+ forNullValue(invoice.getOrderNum())
				+ "</td></tr><tr><td style=\"border: 1px ridge grey; padding: 6px; background: #f2f2f2; font-weight: bold; \">Customer Number</td><td align=\"right\" width=\"57%\" style=\"border: 1px ridge grey; padding: 6px;\">"
				+ forNullValue(invoice.getCustomer().getNumber()) + "</td></tr></table></div></div></td></tr></table>");

	}

	public String getHeader() {
		return this.headerHtml;
	}

	/**
	 * BODY
	 */
	@Override
	public void initBody() {
		String addressHtml;
		String detailsHtml;
		String itemsHtml;

		String billAdrs = "<div align=\"left\">&nbsp;"
				+ forUnusedAddress(invoice.getCustomer().getName(), false)
				+ "</div>";
		String shpAdrs1 = "<br/><br/><br/><br/><br/><br/><br/>";
		Address bill = invoice.getBillingAddress();
		if (bill != null) {
			billAdrs = "<div align=\"left\">&nbsp;"
					+ forUnusedAddress(invoice.getCustomer().getName(), false)
					+ forUnusedAddress(bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ bill.getCountryOrRegion() + "</div>";
		}

		Address shpAdres = invoice.getShippingAdress();
		if (shpAdres != null) {
			shpAdrs1 = "<div align=\"left\">&nbsp;"
					+ forUnusedAddress(invoice.getCustomer().getName(), false)
					+ forUnusedAddress(shpAdres.getAddress1(), false)
					+ forUnusedAddress(shpAdres.getStreet(), false)
					+ forUnusedAddress(shpAdres.getCity(), false)
					+ forUnusedAddress(shpAdres.getStateOrProvinence(), false)
					+ forUnusedAddress(shpAdres.getZipOrPostalCode(), false)
					+ shpAdres.getCountryOrRegion() + "</div>";
		}

		addressHtml = "<table style=\"width: 100%; height: 100%;\"  cellspacing=\"10\"><tr><td style=\"vertical-align: top;\" align=\"left\"><table style=\"width: 280px; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><p class=\"fontSetting\"><center>Bill To</center></p></td></tr><tr><td align=\"left\" style=\"vertical-align: top;padding: 6px;height:105px;\"><p class=\"fontSetting\">"
				+ billAdrs
				+ "</p></td></tr></table></td><td style=\"vertical-align: top;\" align=\"right\"><table style=\"width: 300px; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><center class=\"fontSetting\">Ship To</center></td></tr><tr><td align=\"left\" style=\"vertical-align: top;padding: 6px;height:105px;\"><p class=\"fontSetting\">"
				+ shpAdrs1 + "</p></td></tr></table></td></tr></table>";

		SalesPerson salesPerson = invoice.getSalesPerson();
		String salesPersname = salesPerson != null ? ((salesPerson
				.getFirstName() != null ? salesPerson.getFirstName() : "") + (salesPerson
				.getLastName() != null ? salesPerson.getLastName() : ""))
				: "";

		ShippingMethod shipMtd = invoice.getShippingMethod();
		String shipMtdName = shipMtd != null ? shipMtd.getName() : "";

		PaymentTerms paymentterm = invoice.getPaymentTerm();
		String payterm = paymentterm != null ? paymentterm.getName() : "";

		detailsHtml = ("<table class=\"ShiftBottom\" style=\"width: 100%; height: 100%;\"><tr><td style=\"vertical-align: top;\" align=\"left\"><table style=\"width: 100%; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><center><p class=\"fontSetting\">Sales Person</p></center></td><td class=\"gridHeaderBackGround\"><center><p class=\"fontSetting\">Shipping Method</p></center></td><td class=\"gridHeaderBackGround\" width=\"22%\"><center><p class=\"fontSetting\">Payment Terms</p></center></td></tr><tr><td align=\"center\"><p class=\"fontSetting\">"
				+ salesPersname
				+ "</p><br/><br/><br/>"
				+ "</td><td align=\"center\"><p class=\"fontSetting\">"
				+ shipMtdName
				+ "</p><br/><br/><br/>"
				+ "</td><td align=\"center\" width=\"22%\"><p class=\"fontSetting\">"
				+ payterm + "</p><br/><br/><br/>" + "</td></tr></table></td></tr></table>");

		String recordsHtml = "<tr class=\"item-row\"><td class=\"description\"><div></div></td><td class=\"qty\"><div></div></td><td><div class=\"cost\"></div></td><td><div class=\"price\"></div></td><td class=\"vatRate\"><span ></span></td><td class=\"vatAmount\"><div></div></td></tr>";
		if (!invoice.getTransactionItems().isEmpty()) {
			recordsHtml = "";
			for (TransactionItem item : invoice.getTransactionItems()) {
				String vatColumnsData = "<td style=\"padding: 6px;\" align=\"right\" class=\"vatRate\"><p class=\"fontSetting\"><span >"
						+ Utility.getVATItemRate(item.getTaxCode(), true)
						+ "%</span></p></td><td style=\"padding: 6px;\" align=\"right\" class=\"vatAmount\"><div><p class=\"fontSetting\">"
						+ getDecimalsUsingMaxDecimals(item.getVATfraction(),
								null, 2) + "</p></div></td>";
				recordsHtml = recordsHtml
						+ "<tr class=\"item-row\"><td style=\"padding: 6px;\" class=\"description\"><div><p class=\"fontSetting\">"
						+ forNullValue(item.getDescription())
						+ "</p></div></td><td style=\"padding: 6px;\" align=\"right\" class=\"qty\"><div><p class=\"fontSetting\">"
						+ (forZeroAmounts(getDecimalsUsingMaxDecimals(item
								.getQuantity().getValue(), null,
								maxDecimalPoints)))
						+ "</p></div></td><td style=\"padding: 6px;\" align=\"right\"><div class=\"cost\"><p class=\"fontSetting\">"
						+ (forZeroAmounts(largeAmountConversation(item
								.getUnitPrice())))
						+ "</p></div></td><td style=\"padding: 6px;\" align=\"right\"><div class=\"price\"><p class=\"fontSetting\">"
						+ largeAmountConversation(item.getLineTotal())
						+ "</p></div></td>";
				if (brandingTheme.isShowTaxColumn()) {
					recordsHtml = recordsHtml + vatColumnsData + "</tr>";
				} else {
					recordsHtml = recordsHtml + "</tr>";
				}
			}
		}
		vatColumnHeading = "<th><p class=\"fontSetting\">"
				+ getVendorString("VAT Rate", "Tax Rate")
				+ "</p></th><th><p class=\"fontSetting\">"
				+ getVendorString("VAT Amount", "Tax Amount") + "</p></th>";
		columnHeading = "<tr><th><p class=\"fontSetting\">Description</p></th><th><p class=\"fontSetting\">Qty</p></th><th><p class=\"fontSetting\">Unit Price</p></th><th><p class=\"fontSetting\">Total Price</p></th>";
		if (brandingTheme.isShowTaxColumn()) {
			columnHeading = columnHeading + vatColumnHeading + "</tr>";
			subTotalHtml = (recordsHtml
					+ "<tr><td class=\"blank\" > </td><td class=\"blank\" > </td><td class=\"blank\" > </td><td class=\"blank\" style=\"padding: 5px;\"><p class=\"fontSetting\">"
					+ forNullValue(invoice.getMemo())
					+ "</p></td><td class=\"total-line, item-column\" ><p class=\"fontSetting\">&nbsp;&nbsp;Sub Total</p></td><td class=\"total-value, item-column\"><div id=\"subtotal\"><p class=\"fontSetting\" style=\"padding: 6px;\" align=\"right\">"
					+ largeAmountConversation(invoice.getNetAmount()) + "</p></div></td></tr>");
			totalHtml = "<tr><td class=\"blank\" > </td><td class=\"blank\" > </td><td class=\"blank\" > </td><td class=\"blank\" > </td><td class=\"total-line balance, item-column\"><p class=\"fontSetting\">&nbsp;&nbsp;TOTAL</p></td><td class=\"total-value, item-column\" style=\"padding: 6px;\" align=\"right\"><div class=\"total\"><p class=\"fontSetting\">"
					+ largeAmountConversation(invoice.getTotal())
					+ "</p></div></td></tr></table>";

			vatTotalHtml = "<tr><td class=\"blank\" ></td><td class=\"blank\" > </td><td class=\"blank\" > </td><td class=\"blank\" > </td> <td class=\"total-line, item-column\" ><p class=\"fontSetting\">&nbsp;&nbsp;"
					+ getVendorString("VAT Total", "Tax Total")
					+ "</p></td><td class=\"total-value, item-column\" style=\"padding: 6px;\" align=\"right\"><div id=\"total\"><p class=\"fontSetting\">"
					+ largeAmountConversation((invoice.getTotal() - invoice
							.getNetAmount())) + "</p></div></td></tr>";

		} else {
			columnHeading = columnHeading + "</tr>";
			subTotalHtml = (recordsHtml
					+ "<tr><td class=\"blank\" > </td><td class=\"blank\" style=\"padding: 5px;\"><p class=\"fontSetting\">"
					+ forNullValue(invoice.getMemo())
					+ "</p></td><td class=\"total-line, item-column\" ><p class=\"fontSetting\">&nbsp;&nbsp;Sub Total</p></td><td class=\"total-value, item-column\"><div id=\"subtotal\"><p class=\"fontSetting\" style=\"padding: 6px;\" align=\"right\">"
					+ largeAmountConversation(invoice.getNetAmount()) + "</p></div></td></tr>");

			totalHtml = "<tr><td class=\"blank\" > </td><td class=\"blank\" > </td><td class=\"total-line balance, item-column\"><p class=\"fontSetting\">&nbsp;&nbsp;TOTAL</p></td><td class=\"total-value, item-column\" style=\"padding: 6px;\" align=\"right\"><div class=\"total\"><p class=\"fontSetting\">"
					+ largeAmountConversation(invoice.getTotal())
					+ "</p></div></td></tr></table>";

			vatTotalHtml = "<tr><td class=\"blank\" > </td><td class=\"blank\" > </td> <td class=\"total-line, item-column\" ><p class=\"fontSetting\">&nbsp;&nbsp;VAT Total</p></td><td class=\"total-value, item-column\" style=\"padding: 6px;\" align=\"right\"><div id=\"total\"><p class=\"fontSetting\">"
					+ largeAmountConversation((invoice.getTotal() - invoice
							.getNetAmount())) + "</p></div></td></tr>";
		}
		itemsHtml = ("<table class=\"removeHeadBorder\" id=\"items\">");

		if (brandingTheme.isShowTaxColumn()) {
			columnDataHtml = subTotalHtml + vatTotalHtml + totalHtml;
		} else {
			columnDataHtml = subTotalHtml + totalHtml;
		}

		if (brandingTheme.isShowColumnHeadings()) {
			itemsHtml = itemsHtml + columnHeading + columnDataHtml;
		} else {
			itemsHtml = itemsHtml + columnDataHtml;
		}

		String paymentTermHtml = "<table><tr><td style=\"height:85px\"></td></tr><tr style=\"align:left; left-margin:20px; font-family:"
				+ brandingTheme.getFont()
				+ "; font-size:"
				+ getFontSize(2)
				+ "pt;\"><td><strong>Due Date:</strong><td></tr><tr style=\"align:left; font-family:"
				+ brandingTheme.getFont()
				+ "; font-size:"
				+ getFontSize(0)
				+ "pt><td>"
				+ brandingTheme.getTerms_And_Payment_Advice()
				+ "</td></tr></table>";

		bodyHtml = addressHtml + detailsHtml + itemsHtml + paymentTermHtml;

	}

	private String getVendorString(String forUk, String forUs) {
		return company.getAccountingType() == company.ACCOUNTING_TYPE_US ? forUs
				: forUk;
	}

	/**
	 * FOOTER
	 */
	@Override
	public void initFooter() {

		String regAdd = "&nbsp;";

		Address tradingAddress = company.getTradingAddress();
		if (tradingAddress != null) {
			regAdd = ("&nbsp;Registered Address: "
					+ tradingAddress.getAddress1()
					+ forUnusedAddress(tradingAddress.getStreet(), true)
					+ forUnusedAddress(tradingAddress.getCity(), true)
					+ forUnusedAddress(tradingAddress.getStateOrProvinence(),
							true)
					+ forUnusedAddress(tradingAddress.getZipOrPostalCode(),
							true)
					+ forUnusedAddress(tradingAddress.getCountryOrRegion(),
							true) + ".");
		}

		regAdd = (company.getTradingName() + regAdd + ((company
				.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));

		initFooterTableHtml = "<table style=\"width: 100%; height: 100%; table-layout:fixed;\" border=\"0\"><tr><td><table style=\"width: 100%; height: 100%; font-family:"
				+ brandingTheme.getFont()
				+ "; color:#505050;\" ><tr><td style=\"vertical-align: top;\" align=\"left\" colspan=\"2\"><table style=\"font-size:"
				+ getFontSize(4)
				+ "pt; width: 105%; height: 100%; margin-left:56px;  border-color: #E1E1E1; border-collapse:collapse;\" border=\"1\"><colgroup><col></colgroup><tr>";
		vatRegNumberHtml = "<td><center>"
				+ getVendorString("VAT No: ", "Tax No: ")
				+ forNullValue(company.getPreferences()
						.getVATregistrationNumber()) + "</center></td>";
		sortCodeHtml = "<td><center>Sort Code: "
				+ forNullValue(company.getSortCode()) + "</center></td>";
		bankAccountNumberHtml = "<td><center>Bank Account No: "
				+ forNullValue(company.getBankAccountNo())
				+ "</center></td></tr></table></td></tr>";
		regAdd = "<tr><td style=\"vertical-align: top;\" align=\"center\"><table  style=\"width: 100%; height: 100%; padding:3.5px\" ><tr><td align=\"right\" style=\"vertical-align: top;\"<table style=\"margin-left:60px;font-size:"
				+ getFontSize(4)
				+ "pt;width: 95%; height: 37px;  border : 1px solid #E1E1E1;\"><colgroup><col></colgroup><tr><td><center>"
				+ regAdd + "</center></td></tr></table></td></tr>";

		footerEndingHtml = "</table></td></tr></table></td><td width=\"10%\" style=\"vertical-align: bottom;\"><div align=\"right\" style=\"margin-bottom:14px;\"><img src=\"footerimage\" width=\"67.5px\" height=\"25px\"></div></td></tr><tr><td style=\"height:"
				+ brandingTheme.getBottomMargin()
				+ getUnits()
				+ "\"><p><br></p></td></tr></table>";

		if (brandingTheme.isShowRegisteredAddress()) {
			footerEndingHtml = regAdd + footerEndingHtml;
		}
		if (brandingTheme.isShowTaxNumber()) {
			footerHtml = initFooterTableHtml + vatRegNumberHtml + sortCodeHtml
					+ bankAccountNumberHtml + footerEndingHtml;
		} else {
			footerHtml = initFooterTableHtml + sortCodeHtml
					+ bankAccountNumberHtml + footerEndingHtml;
		}

	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	@Override
	public String getBody() {
		String body = "<html><head><style>.fontSetting{font-family:"
				+ brandingTheme.getFont() + "; font-size:" + getFontSize(0)
				+ "pt;}.headerFontSetting{font-family:"
				+ brandingTheme.getFont() + "; font-size:" + getFontSize(6)
				+ "pt;}</style><link rel='stylesheet' type='text/css' href='"
				+ style1 + "' media='all'></link></head><body>" + this.bodyHtml
				+ "</body></html>";
		body = body.replace("<br>", "<br/>");
		return body;
	}

	@Override
	public String getFooter() {
		this.footerHtml = this.footerHtml.replace("footerimage", imgUrl);
		return this.footerHtml;
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

	private static String insertCommas(String str) {

		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	@Override
	public String getFileName() {
		return "Invoice_" + this.invoice.getNumber();
	}

	private String largeAmountConversation(double amount) {
		String amt = Utility.decimalConversation(amount);
		amt = getDecimalsUsingMaxDecimals(0.0, amt, 2);
		return (amt);
	}

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
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

}