package com.vimukti.accounter.core;

/**
 * 
 * @author Narendra nnvd
 * 
 */
public class InvoiceTemplete extends TemplateBuilder implements ITemplate {

	private Invoice invoice;
	private int maxDecimalPoints;

	public InvoiceTemplete(Invoice invoice, String footerImageUrl,
			String stylefile) {
		this.invoice = invoice;
		this.maxDecimalPoints = getMaxDecimals(invoice);
		imgUrl = footerImageUrl;
		style1 = stylefile;
		init();
	}

	/**
	 * HEADER
	 */
	@Override
	public void initHeader() {

		String cmpAdd = "<br/><br/><br/><br/><br/>";
		for (Address cmpTrad : company.getAddresses()) {
			if (cmpTrad.getType() == Address.TYPE_COMPANY_REGISTRATION) {
				if (cmpTrad != null)
					cmpAdd = ("<br><font style=\"font-family:sans-serif; color:#504040;\">&nbsp;"
							+ forUnusedAddress(cmpTrad.getAddress1(), false)
							+ forUnusedAddress(cmpTrad.getStreet(), false)
							+ forUnusedAddress(cmpTrad.getCity(), false)
							+ forUnusedAddress(cmpTrad.getStateOrProvinence(),
									false)
							+ forUnusedAddress(cmpTrad.getZipOrPostalCode(),
									false)
							+ forUnusedAddress(cmpTrad.getCountryOrRegion(),
									false) + "</font></br>");
			}
		}

		cmpAdd = ("<p><font style=\"font-family:sans-serif;\" size=\"6px\"><strong> "
				+ forNullValue(TemplateBuilder.getCmpName())
				+ "</strong></font>" + cmpAdd + "</p>");

		headerHtml = ("<table style=\"width: 100%; height: 100%;\" cellspacing=\"10\" ><tr><td style=\"vertical-align: top;\" align=\"left\"><div class=\"gwt-HTML\" style=\" margin-left: 43px;\">"
				+ cmpAdd
				+ "</div></td><td style=\"vertical-align: top;\" align=\"right\"><div class=\"gwt-HTML\" style=\" margin-right: 43px;\"><p ><font color=\"black\" style=\"font-family:sans-serif;\" size=\"6px\"><strong>Invoice</strong></font></p><div><table style=\"width:300px;font-size: 13px; border-collapse: collapse; table-layout: fixed; font-family: sans-serif;\"><colgroup><col></colgroup><tr><td style=\"border: 1px ridge grey; padding: 6px; background: #f2f2f2; font-weight: bold; \"> Invoice Number</td><td align=\"right\" width=\"57%\" style=\"border: 1px ridge grey; padding: 6px;\">"
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

		addressHtml = "<table style=\"width: 100%; height: 100%;\"  cellspacing=\"10\"><tr><td style=\"vertical-align: top;\" align=\"left\"><table style=\"width: 280px; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><center>Bill To</center></td></tr><tr><td align=\"left\" style=\"vertical-align: top;padding: 6px;height:105px;\">"
				+ billAdrs
				+ "</td></tr></table></td><td style=\"vertical-align: top;\" align=\"right\"><table style=\"width: 300px; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><center>Ship To</center></td></tr><tr><td align=\"left\" style=\"vertical-align: top;padding: 6px;height:105px;\">"
				+ shpAdrs1 + "</td></tr></table></td></tr></table>";

		SalesPerson salesPerson = invoice.getSalesPerson();
		String salesPersname = salesPerson != null ? ((salesPerson
				.getFirstName() != null ? salesPerson.getFirstName() : "") + (salesPerson
				.getLastName() != null ? salesPerson.getLastName() : ""))
				: "";

		ShippingMethod shipMtd = invoice.getShippingMethod();
		String shipMtdName = shipMtd != null ? shipMtd.getName() : "";

		PaymentTerms paymentterm = invoice.getPaymentTerm();
		String payterm = paymentterm != null ? paymentterm.getName() : "";

		detailsHtml = ("<table class=\"ShiftBottom\" style=\"width: 100%; height: 100%;\"><tr><td style=\"vertical-align: top;\" align=\"left\"><table style=\"width: 100%; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><center>Sales Person</center></td><td class=\"gridHeaderBackGround\"><center>Shipping Method</center></td><td class=\"gridHeaderBackGround\" width=\"22%\"><center>Payment Terms</center></td></tr><tr><td align=\"center\">"
				+ salesPersname
				+ "<br/><br/><br/>"
				+ "</td><td align=\"center\">"
				+ shipMtdName
				+ "<br/><br/><br/>"
				+ "</td><td align=\"center\" width=\"22%\">"
				+ payterm
				+ "<br/><br/><br/>" + "</td></tr></table></td></tr></table>");

		String recordsHtml = "<tr class=\"item-row\"><td class=\"description\"><div></div></td><td class=\"qty\"><div></div></td><td><div class=\"cost\"></div></td><td><div class=\"price\"></div></td><td class=\"vatRate\"><span ></span></td><td class=\"vatAmount\"><div></div></td></tr>";
		if (!invoice.getTransactionItems().isEmpty()) {
			recordsHtml = "";
			for (TransactionItem item : invoice.getTransactionItems()) {
				recordsHtml = recordsHtml
						+ "<tr class=\"item-row\"><td style=\"padding: 6px;\" class=\"description\"><div>"
						+ forNullValue(item.getDescription())
						+ "</div></td><td style=\"padding: 6px;\" align=\"right\" class=\"qty\"><div>"
						+ (forZeroAmounts(getDecimalsUsingMaxDecimals(item
								.getQuantity(), null, maxDecimalPoints)))
						+ "</div></td><td style=\"padding: 6px;\" align=\"right\"><div class=\"cost\">"
						+ (forZeroAmounts(largeAmountConversation(item
								.getUnitPrice())))
						+ "</div></td><td style=\"padding: 6px;\" align=\"right\"><div class=\"price\">"
						+ largeAmountConversation(item.getLineTotal())
						+ "</div></td><td style=\"padding: 6px;\" align=\"right\" class=\"vatRate\"><span >"
						+ Utility.getVATItemRate(item.getTaxCode(), true)
						+ "%</span></td><td style=\"padding: 6px;\" align=\"right\" class=\"vatAmount\"><div>"
						+ getDecimalsUsingMaxDecimals(item.getVATfraction(),
								null, 2) + "</div></td></tr>";
			}
		}
		itemsHtml = ("<table id=\"items\"><tr><th>Description</th><th>Qty</th><th>Unit Price</th><th>Total Price</th><th>VAT Rate</th><th>VAT Amount</th></tr>"
				+ recordsHtml
				+ "</table><table id=\"totals\"><tr><td class=\"blank\" style=\"padding: 5px;\">"
				+ forNullValue(invoice.getMemo())
				+ "</td><td class=\"total-line\" >&nbsp;&nbsp;Sub Total</td><td class=\"total-value\"><div id=\"subtotal\">"
				+ largeAmountConversation(invoice.getNetAmount())
				+ "&nbsp;&nbsp;</div></td></tr><tr><td class=\"blank\" ></td> <td class=\"total-line\" >&nbsp;&nbsp;VAT Total</td><td class=\"total-value\"><div id=\"total\">"
				+ largeAmountConversation((invoice.getTotal() - invoice
						.getNetAmount()))
				+ "&nbsp;&nbsp;</div></td></tr><tr><td class=\"blank\" > </td><td class=\"total-line balance\">&nbsp;&nbsp;TOTAL</td><td class=\"total-value balance\"><div id=\"due\">"
				+ largeAmountConversation(invoice.getTotal()) + "&nbsp;&nbsp;</div></td></tr></table>");

		bodyHtml = addressHtml + detailsHtml + itemsHtml;

	}

	/**
	 * FOOTER
	 */
	@Override
	public void initFooter() {

		String regAdd = "&nbsp;";

		for (Address reg : company.getAddresses()) {
			if (reg.getType() == Address.TYPE_COMPANY) {
				if (reg != null)
					regAdd = (",&nbsp;Register Address: "
							+ forUnusedAddress(reg.getAddress1(), true)
							+ forUnusedAddress(reg.getStreet(), true)
							+ forUnusedAddress(reg.getCity(), true)
							+ forUnusedAddress(reg.getStateOrProvinence(), true)
							+ forUnusedAddress(reg.getZipOrPostalCode(), true)
							+ reg.getCountryOrRegion() + ".");
			}
		}

		regAdd = (company.getName() + regAdd + ((company
				.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));

		footerHtml = ("<table style=\"width: 100%; height: 100%; table-layout:fixed;\" border=\"0\" ><tr><td><table style=\"width: 100%; height: 100%; font-family:sans-serif; color:#505050;\" ><tr><td style=\"vertical-align: top;\" align=\"left\" colspan=\"2\"><table style=\"font-size:13px; width: 105%; height: 100%; margin-left:56px;  border-color: #E1E1E1; border-collapse:collapse;\" border=\"1\"><colgroup><col></colgroup><tr><td><center>VAT No : "
				+ forNullValue(company.getPreferences()
						.getVATregistrationNumber())
				+ "</center></td><td><center>Sort Code : "
				+ forNullValue(company.getSortCode())
				+ "</center></td><td><center>Bank Account No :"
				+ forNullValue(company.getBankAccountNo())
				+ "</center></td></tr></table></td></tr><tr><td style=\"vertical-align: top;\" align=\"center\"><table  style=\"width: 100%; height: 100%; padding:3.5px\" ><tr><td align=\"right\" style=\"vertical-align: top;\"<table style=\"margin-left:60px;font-size:13px;width: 95%; height: 37px;  border : 1px solid #E1E1E1;\"><colgroup><col></colgroup><tr><td><center>"
				+ regAdd + "</center></td></tr></table></td></tr></table></td></tr></table></td><td width=\"10%\" style=\"vertical-align: bottom;\"><div align=\"right\" style=\"margin-bottom:14px;\"><img src=\"footerimage\" width=\"67.5px\" height=\"25px\"></div></td></tr></table>");

	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	@Override
	public String getBody() {
		String body = "<html><head><link rel='stylesheet' type='text/css' href='"
				+ style1
				+ "' media='all'></link></head><body>"
				+ this.bodyHtml
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
				return add + ",&nbsp;";
		} else {
			if (add != null && !add.equals(""))
				return add + "<br/>&nbsp;";
		}
		return "";
	}

}