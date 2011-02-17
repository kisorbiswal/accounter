package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class AmountsDueToVendorReport extends
		AbstractReportView<AmountsDueToVendor> {

	@SuppressWarnings("unused")
	private String sectionName = "";

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				UIUtils.getVendorString(FinanceApplication.getVendorsMessages()
						.supplierName(), FinanceApplication
						.getVendorsMessages().vendorName()),
				FinanceApplication.getReportsMessages().active(),
				FinanceApplication.getReportsMessages().city(),
				FinanceApplication.getReportsMessages().state(),
				FinanceApplication.getReportsMessages().zipCode(),
				FinanceApplication.getReportsMessages().phone(),
				FinanceApplication.getReportsMessages().balance() };
	}

	@Override
	public String getTitle() {
		return UIUtils.getVendorString(FinanceApplication.getVendorsMessages()
				.amountDueToSupplier(), FinanceApplication.getVendorsMessages()
				.amountDueToVendor());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getAmountsDueToVendor(
				start.getTime(), end.getTime(), this);
	}

	@Override
	public void OnRecordClick(AmountsDueToVendor record) {
		// nothing to do
	}

	@Override
	public Object getColumnData(AmountsDueToVendor record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getIsActive() ? FinanceApplication
					.getReportsMessages().yes() : FinanceApplication
					.getReportsMessages().no();
		case 2:
			return record.getCity();
		case 3:
			return record.getState();
		case 4:
			return record.getZip();
		case 5:
			return record.getPhone();
		case 6:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public void processRecord(AmountsDueToVendor record) {
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().total(),
					new int[] { 6 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(AmountsDueToVendor obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(AmountsDueToVendor obj) {
		return obj.getStartDate();
	}

}
