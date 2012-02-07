package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionSummary;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class InventoryValutionSummaryServerReport extends
		AbstractFinaneReport<InventoryValutionSummary> {

	public InventoryValutionSummaryServerReport(
			IFinanceReport<InventoryValutionSummary> view) {
		this.reportView = view;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", messages.description(), messages.onHand(),
				messages.avgCost(), messages.assetsTotal(),
				messages.percentOfToTAsset(), messages.salesPrice(),
				messages.retailCost(), messages.perOfTotRetail() };
	}

	@Override
	public String getTitle() {
		return messages.inventoryValutionSummary();
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", messages.description(), messages.onHand(),
				messages.avgCost(), messages.assetsTotal(),
				messages.percentOfToTAsset(), messages.salesPrice(),
				messages.retailCost(), messages.perOfTotRetail() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_PERCENTAGE, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_PERCENTAGE };
	}

	@Override
	public void processRecord(InventoryValutionSummary record) {
		if (sectionDepth == 0) {
			addSection(new String[] { record.getItemName() },
					new String[] { getMessages().total() }, new int[] { 2, 4,
							5, 7, 8 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(InventoryValutionSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return record.getItemDescription();
		case 2:
			return record.getOnHand();
		case 3:
			return record.getAvgCost();
		case 4:
			return record.getAssetValue();
		case 5:
			return record.getPercentOfTotAsset();
		case 6:
			return record.getSalesPrice();
		case 7:
			return record.getRetailValue();
		case 8:
			return record.getPercentOfTotRetail();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(InventoryValutionSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(InventoryValutionSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
