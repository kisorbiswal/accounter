package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.reports.InventoryStockStatusDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
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
		return new String[] { "", messages.itemDescription(),
				messages.reportPts(), messages.onHand(),
				messages.onSalesOrder(), messages.forAssemblies(),
				messages.available(), messages.order(), messages.orderOnPo(),
				messages.nextDeliveryDate(), messages.salesPerWeek() };
	}

	@Override
	public String getTitle() {
		return messages.inventoryStockStatusByVendor();
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", messages.itemDescription(),
				messages.reportPts(), messages.onHand(),
				messages.onSalesOrder(), messages.forAssemblies(),
				messages.available(), messages.order(), messages.orderOnPo(),
				messages.nextDeliveryDate(), messages.salesPerWeek() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER };
	}

	@Override
	public void processRecord(InventoryStockStatusDetail record) {
		ClientPayee payee = payee = Accounter.getCompany().getPayee(
				record.getPreferVendor());
		if (sectionDepth == 0) {
			this.sectionName = payee.getName();
			addSection(sectionName, "", new int[] { 3, 4, 5, 6, 8 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(payee.getName())) {
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
		case 1:
			return record.getItemDesc();
		case 2:
			return record.getOnHand();
		case 3:
			return record.getPreferVendor();
		case 4:
			return record.getOnHand();
		case 5:
			return record.getOnSalesOrder();
		case 6:
			return record.getAssemblies();
		case 7:
			return record.getAvilability();
		case 8:
			return record.getOredr();
		case 9:
			return record.getOrderOnPo();
		case 10:
			return DateUtills.getDateAsString(record.getNextDeliveryDate());
		case 11:
			return record.getSalesPerWeek();
		}
		return null;
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
