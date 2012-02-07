package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.serverreports.InventoryValuationDetailsServerReport;

public class InventoryValuationDetailsReport extends
		AbstractReportView<InventoryValutionDetail> {
	long id;

	public InventoryValuationDetailsReport(long id) {
		this.serverReport = new InventoryValuationDetailsServerReport(this);
		this.id = id;

	}

	@Override
	public void print() {
		super.print();
	}

	@Override
	public void exportToCsv() {
		super.exportToCsv();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(InventoryValutionDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransType(),
				record.getTransactionId());
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getInventoryValutionDetail(id, start,
				end, this);
	}

}
