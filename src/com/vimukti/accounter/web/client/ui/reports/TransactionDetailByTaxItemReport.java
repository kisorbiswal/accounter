package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.TransactionDetailByTaxItemServerReport;

@SuppressWarnings("unchecked")
public class TransactionDetailByTaxItemReport extends
		AbstractReportView<TransactionDetailByTaxItem> {

	public TransactionDetailByTaxItemReport() {
		this.serverReport = new TransactionDetailByTaxItemServerReport(this);
	}

	@Override
	public void OnRecordClick(TransactionDetailByTaxItem record) {
		ReportsRPC.openTransactionView(record.getTransactionType(), record
				.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesTaxLiability taxLiability = (SalesTaxLiability) data;
		if (taxLiability == null) {
			Accounter.createReportService()
					.getTransactionDetailByTaxItem(start.getTime(),
							end.getTime(), this);
		} else if (taxLiability.getTaxAgencyName() != null) {
			Accounter.createReportService()
					.getTransactionDetailByTaxItem(
							taxLiability.getTaxAgencyName(), start.getTime(),
							end.getTime(), this);

		}
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 114, "", "");

		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 114, "", "");
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

}