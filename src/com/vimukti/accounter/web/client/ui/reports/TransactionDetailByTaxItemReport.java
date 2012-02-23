package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
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
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesTaxLiability taxLiability = (SalesTaxLiability) data;
		if (taxLiability == null) {
			Accounter.createReportService().getTransactionDetailByTaxItem(
					start, end, this);
		} else if (taxLiability.getTaxItemName() != null) {
			Accounter.createReportService().getTransactionDetailByTaxItem(
					taxLiability.getTaxItemName(), taxLiability.getStartDate(),
					taxLiability.getEndDate(), this);

		}
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		String taxItemName = this.data != null ? ((SalesTaxLiability) data)
				.getTaxItemName() : "";
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 114, "",
				"", taxItemName);

	}

	@Override
	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 114, "",
				"");
	}

	@Override
	public void printPreview() {

	}
	
	@Override
	public void restoreView(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

}