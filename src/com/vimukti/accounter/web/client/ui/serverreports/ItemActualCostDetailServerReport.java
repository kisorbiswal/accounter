package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.ItemActualCostDetail;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class ItemActualCostDetailServerReport extends
		AbstractFinaneReport<ItemActualCostDetail> {
	private String sectionName = "";
	private String customerName = "";
	private boolean isActualCostDetail;

	public ItemActualCostDetailServerReport(
			IFinanceReport<ItemActualCostDetail> reportView,
			boolean isActualCostDetail) {
		this.reportView = reportView;
		this.isActualCostDetail = isActualCostDetail;
	}

	public ItemActualCostDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().itemName(), getMessages().type(),
				getMessages().date(), getMessages().number(),
				Global.get().customer(), getMessages().memo(),
				getMessages().quantity(), getMessages().amount() };
	}

	@Override
	public String getTitle() {
		if (isActualCostDetail)
			return messages.itemActualCostDetail();
		else
			return messages.itemActualRevenueDetail();
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().itemName(), getMessages().type(),
				getMessages().date(), getMessages().number(),
				Global.get().customer(), getMessages().memo(),
				getMessages().quantity(), getMessages().amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(ItemActualCostDetail record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "" }, new String[] { "", "", "", "",
					getMessages().total() }, new int[] { 7 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getItemName();
			addSection(new String[] { sectionName }, new String[] { "", "",
					getMessages().reportTotal(sectionName) }, new int[] { 7 });

		} else if (sectionDepth == 2) {
			this.customerName = record.getCustomerName();
			addSection(new String[] { "", customerName }, new String[] { "",
					"", "", "", "", getMessages().reportTotal(customerName) },
					new int[] { 7 });
		} else if (sectionDepth == 3) {
			if (!customerName.equals(record.getCustomerName())) {
				endSection();
			}
			if (!sectionName.equals(record.getItemName())) {
				if (!customerName.equals(record.getCustomerName())) {
					endSection();
				} else {
					endSection();
					endSection();
				}
			}
			if (customerName.equals(record.getCustomerName())
					&& sectionName.equals(record.getItemName())) {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	@Override
	public Object getColumnData(ItemActualCostDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getItemName();
		case 1:
			return Utility.getTransactionName(record.getType());
		case 2:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 4:
			return record.getCustomerName();
		case 5:
			return record.getMemo();
		case 6:
			return record.getQuantity();
		case 7:
			return record.getAmount();
		default:
			break;
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(ItemActualCostDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(ItemActualCostDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub
	}

}
