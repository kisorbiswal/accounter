package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionDetail;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public class InventoryValuationDetailsServerReport extends
		AbstractFinaneReport<InventoryValutionDetail> {
	private String sectionName = "";

	public InventoryValuationDetailsServerReport(
			IFinanceReport<InventoryValutionDetail> view) {
		this.reportView = view;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.type(), messages.date(),
				messages.name(), messages.name(), messages.quantity(),
				messages.cost(), messages.onHand(), messages.avgCost(),
				messages.assetsTotal() };
	}

	@Override
	public String getTitle() {
		return messages.inventoryValuationDetails();
	}

	@Override
	public String[] getColunms() {

		return new String[] { messages.type(), messages.date(),
				messages.name(), messages.name(), messages.quantity(),
				messages.cost(), messages.onHand(), messages.avgCost(),
				messages.assetsTotal() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };

	}

	@Override
	public void processRecord(InventoryValutionDetail record) {
		if (sectionDepth == 0) {
			this.sectionName = record.getItemName();
			addSection(
					new String[] { sectionName },
					new String[] { getMessages().total() + record.getItemName() },
					new int[] { 6, 8 });
			// addSection(sectionName, "", new int[] { 5 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getItemName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(InventoryValutionDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return ReportUtility.getTransactionName(record.getTransType());
		case 1:
			return DateUtills.getDateAsString(record.getTransactionDate());
		case 2:
			return record.getPayeeName();
		case 3:
			return record.getTransactionNo();
		case 4:
			return record.getCost();
		case 5:
			return record.getOnHand();
		case 7:
			return record.getCost();
		case 8:
			return record.getQuantity() * record.getCost();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(InventoryValutionDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(InventoryValutionDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
