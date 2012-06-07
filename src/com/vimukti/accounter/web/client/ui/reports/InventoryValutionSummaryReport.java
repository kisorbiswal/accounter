package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.NumberReportInput;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.InventoryValutionSummaryServerReport;

public class InventoryValutionSummaryReport extends
		AbstractReportView<InventoryValutionSummary> {
	private long warehouseId = 0;

	public InventoryValutionSummaryReport() {
		this.getElement().setId("inventory-valuation-summary");
		this.serverReport = new InventoryValutionSummaryServerReport(this);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		makeReportRequest(getWarehouseId(), start, end);
	}

	public void makeReportRequest(long wareHouseId,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		grid.removeAllRows();
		grid.addLoadingImagePanel();
		setWarehouseId(wareHouseId);
		Accounter.createReportService().getInventoryValutionSummary(
				wareHouseId, startDate, endDate, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_WAREHOUSE;
	}

	@Override
	public void OnRecordClick(InventoryValutionSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				InventoryReportsAction.valuationDetails(record.getId()));
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), REPORT_TYPE_INVENTORY_VALUTION_SUMMARY,
				new NumberReportInput(getWarehouseId()));
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

}
