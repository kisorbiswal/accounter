package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ItemActualCostDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ItemActualCostDetailServerReport;

public class ItemActualCostDetailReport extends
		AbstractReportView<ItemActualCostDetail> {
	private long itemId;
	private boolean isActualcostDetail;

	public ItemActualCostDetailReport(boolean isActualcostDetail, long itemId) {
		this.itemId = itemId;
		this.isActualcostDetail = isActualcostDetail;
		this.serverReport = new ItemActualCostDetailServerReport(this,
				isActualcostDetail);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getItemActualCostDetail(start, end,
				itemId, isActualcostDetail, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(ItemActualCostDetail record) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void print() {
		String customerName = this.data != null ? ((ItemActualCostDetail) this.data)
				.getCustomerName() : "";
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 187, "",
				"", customerName);
	}

	@Override
	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 187, "",
				"");
	}
}
