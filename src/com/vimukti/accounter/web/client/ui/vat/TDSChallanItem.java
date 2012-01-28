package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class TDSChallanItem extends SimplePanel {

	private final HTML html;
	ClientTDSChalanDetail challan;

	public TDSChallanItem(ClientTDSChalanDetail challan) {
		this.challan = challan;
		this.html = new HTML();
		this.add(html);
		initItem();
	}

	/**
	 * this method creates the html code for the array
	 * 
	 * @param challan
	 */
	private void initItem() {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<br>");
		sb.appendHtmlConstant("<table width='100%' class='historyItem'>");

		sb.appendHtmlConstant("<tr>");
		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Challan Serial No : "
				+ challan.getChalanSerialNumber());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Bank BSR Code : " + challan.getBankBsrCode());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Nature of payment : " + challan.getPaymentSection());
		sb.appendHtmlConstant("</td>");
		sb.appendHtmlConstant("</tr>");

		sb.appendHtmlConstant("<tr>");
		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Date on Tax paid : "
				+ UIUtils.getDateByCompanyType(new ClientFinanceDate(challan
						.getDateTaxPaid())));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Payment method : "
				+ (challan.getPaymentMethod() != null ? challan
						.getPaymentMethod() : ""));
		sb.appendHtmlConstant("</td>");

		if ((challan.getPaymentMethod() != null && challan.getPaymentMethod()
				.equals(Accounter.getMessages().cheque()))
				|| challan.getCheckNumber() != 0) {
			sb.appendHtmlConstant("<td>");
			sb.appendEscaped("Cheque/Ref No : " + challan.getCheckNumber());
			sb.appendHtmlConstant("</td>");
		}
		sb.appendHtmlConstant("</tr>");

		sb.appendHtmlConstant("<tr>");
		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Income Tax : " + challan.getIncomeTaxAmount());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Surcharge paid : " + challan.getSurchangePaidAmount());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Education cess : " + challan.getEducationCessAmount());
		sb.appendHtmlConstant("</td>");
		sb.appendHtmlConstant("</tr>");

		sb.appendHtmlConstant("<tr>");
		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Interest paid : " + challan.getInterestPaidAmount());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Other amount paid : " + challan.getOtherAmount());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped("Total : " + challan.getTotal());
		sb.appendHtmlConstant("</td>");
		sb.appendHtmlConstant("</tr>");

		sb.appendHtmlConstant("</table>");

		sb.appendHtmlConstant("<table border='1'>");
		if (challan.getTdsTransactionItems().size() > 1) {
			appendItemsHeader(sb);
		}

		int srNo = 0;
		for (ClientTDSTransactionItem item : challan.getTdsTransactionItems()) {
			srNo++;
			appendItemDetails(sb, item, srNo);
		}
		sb.appendHtmlConstant("</table>");
		html.setHTML(sb.toSafeHtml());

	}

	private void appendItemDetails(SafeHtmlBuilder sb,
			ClientTDSTransactionItem item, int srNo) {

		sb.appendHtmlConstant("<tr>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(String.valueOf(srNo));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(String.valueOf(item.getDeducteeCode()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(Accounter.getCompany().getVendor(item.getVendor())
				.getTaxId());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(Accounter.getCompany().getVendor(item.getVendor())
				.getName());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(UIUtils.getDateByCompanyType(new ClientFinanceDate(
				challan.getDateTaxPaid())));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getTotalAmount()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(challan.isBookEntry() ? "Y" : "N");
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getTdsAmount()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getSurchargeAmount()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getEduCess()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getTotalTax()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getTotalTax()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(UIUtils.getDateByCompanyType(new ClientFinanceDate(
				item.getTransactionDate())));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(String.valueOf(item.getTaxRate()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(item.getRemark());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("</tr>");

	}

	private void appendItemsHeader(SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<tr>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Sr.No");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Deductee code (01-Company,02-Other than Company)");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("PAN of the deductee");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Name of the deductee");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Date of Payment / Credit");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Amount paid / credited Rs.");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Paid by book entry or otherwise");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("TDS Rs.");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Surcharge Rs.");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Education Cess Rs.");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Total Tax deducted");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Total Tax deposited");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Date of deduction");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Rate at which deducted");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped("Remark(Reason for non-Deduction/lower deduction/higher deduction/threshold)");
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("</tr>");
	}
}
