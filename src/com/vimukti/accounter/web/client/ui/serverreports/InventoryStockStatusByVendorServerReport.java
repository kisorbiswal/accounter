package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryStockStatusDetail;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public class InventoryStockStatusByVendorServerReport extends
		AbstractFinaneReport<InventoryStockStatusDetail> {
	private String sectionName;

	public InventoryStockStatusByVendorServerReport(
			IFinanceReport<InventoryStockStatusDetail> view) {
		this.reportView = view;
	}

	public InventoryStockStatusByVendorServerReport(long startDate,
			long endDate, int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.description(), messages.reportPts(),
				messages.onHand(), messages.onSO(), messages.forAssemblies(),
				messages.available(), messages.onPO(), messages.nextDeliv(),
				messages.salesPerWeek() };
	}

	@Override
	public String getTitle() {
		return messages.inventoryStockStatusByVendor();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.description(), messages.reportPts(),
				messages.onHand(), messages.onSO(), messages.forAssemblies(),
				messages.available(), messages.onPO(), messages.nextDeliv(),
				messages.salesPerWeek() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER };
	}

	@Override
	public void processRecord(InventoryStockStatusDetail record) {

		if (sectionDepth == 0) {
			this.sectionName = record.getPreferVendor();
			addSection(new String[] { sectionName }, new String[] {},
					new int[] {});
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getPreferVendor())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(InventoryStockStatusDetail record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getItemDesc();
		case 1:
			return record.getReorderPts();
		case 2:
			return record.getOnHand();
		case 3:
			return record.getOnSalesOrder();
		case 4:
			return record.getAssemblies();
		case 5:
			return record.getAvilability();
		case 6:
			return record.getOrderOnPo();
		case 7:
			return DateUtills.getDateAsString(record.getNextDeliveryDate());
		case 8:
			return record.getSalesPerWeek();
		}
		return null;
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 1:
			return 80;
		case 2:
		case 3:
			return 60;
		case 4:
			return 85;
		case 5:
			return 70;
		case 6:
			return 60;
		case 7:
			return 80;
		case 8:
			return 75;
		}
		return -1;
	}

	@Override
	public ClientFinanceDate getStartDate(InventoryStockStatusDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(InventoryStockStatusDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
