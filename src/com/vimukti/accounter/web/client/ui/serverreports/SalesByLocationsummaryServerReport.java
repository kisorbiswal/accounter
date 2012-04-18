package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class SalesByLocationsummaryServerReport extends
		AbstractFinaneReport<SalesByLocationSummary> {
	private String sectionName = "";
	private String currentsectionName = "";
	private double accountBalance = 0.0D;
	private boolean isLocation;
	private boolean isCustomer;

	public SalesByLocationsummaryServerReport(
			IFinanceReport<SalesByLocationSummary> reportView,
			boolean isLocation, boolean isCustomer) {
		this.reportView = reportView;
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;

	}

	public SalesByLocationsummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", Global.get().messages().total() };
	}

	@Override
	public String getTitle() {
		String actionsting = null;
		if (isCustomer) {
			if (isLocation) {
				actionsting = messages.salesByLocationSummary(Global.get()
						.Location());
			} else {
				actionsting = messages.salesByClassSummary();
			}
		} else {
			if (isLocation) {
				actionsting = messages.purchasesbyLocationSummary(Global.get()
						.Location());
			} else {
				actionsting = messages.purchasesbyClassSummary();
			}
		}
		return actionsting;
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", Global.get().messages().total() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(SalesByLocationSummary record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "" }, new String[] { getMessages()
					.total() }, new int[] { 1 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 150;
		case 1:
			return 300;
		}
		return -1;
	}

	@Override
	public Object getColumnData(SalesByLocationSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getRecordSectionName(record);
		case 1:
			return record.getTotal();
		}
		return null;
	}

	private String getRecordSectionName(SalesByLocationSummary record) {
		return record.getLocationName() == null ? getMessages().notSpecified()
				: record.getLocationName();
	}

	@Override
	public ClientFinanceDate getStartDate(SalesByLocationSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(SalesByLocationSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub
	}

}
