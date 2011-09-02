package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class SalesByLocationDetailsServerReport extends
		AbstractFinaneReport<SalesByLocationDetails> {
	private String sectionName = "";
	private double accountBalance = 0.0D;

	private String currentsectionName = "";

	public SalesByLocationDetailsServerReport(
			IFinanceReport<SalesByLocationDetails> reportView) {
		this.reportView = reportView;
	}

	public SalesByLocationDetailsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", Global.get().constants().date(),
				Global.get().constants().type(),
				Global.get().constants().number(), Global.get().Account(),
				Global.get().constants().prouductOrService(),
				Global.get().constants().amount(),
				Global.get().constants().balance() };
	}

	@Override
	public String getTitle() {
		return Accounter.messages().getSalesByLocationDetails(
				Global.get().Location());
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", Global.get().constants().date(),
				Global.get().constants().type(),
				Global.get().constants().number(), Global.get().Account(),
				Global.get().constants().prouductOrService(),
				Global.get().constants().amount(),
				Global.get().constants().balance() };
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 225;
		case 1:
		case 5:
			return 115;
		case 2:
			return 100;
		case 4:
			return 150;
		default:
			return 75;
		}
	}

	@Override
	public void processRecord(SalesByLocationDetails record) {
		if (sectionDepth == 0) {
			this.sectionName = getRecordSectionName(record);
			addSection(new String[] { sectionName }, new String[] { "" },
					new int[] { 6 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(getRecordSectionName(record))) {
				endSection();
			} else {
				return;
			}
		} else if (sectionDepth == 2) {
			addSection(new String[] { "", "" }, new String[] { "", "", "", "",
					constants.total() }, new int[] { 6 });
		}

		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	private String getRecordSectionName(SalesByLocationDetails record) {
		return record.getLocationName() == null ? "Not Specified" : record
				.getLocationName();

	}

	@Override
	public Object getColumnData(SalesByLocationDetails record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return UIUtils
					.dateAsString(new ClientFinanceDate(record.getDate()));
		case 2:
			return ReportUtility.getTransactionName(record.getType());
		case 3:
			return record.getNumber();
		case 4:
			return record.getAccount();
		case 5:
			return record.getProuductOrService();
		case 6:
			return record.getAmount();
		case 7:
			if (!currentsectionName.equals(getRecordSectionName(record))) {
				currentsectionName = getRecordSectionName(record);
				accountBalance = 0.0D;
			}
			double amount = record.getAmount();
			accountBalance += amount;
			return (accountBalance);
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(SalesByLocationDetails obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(SalesByLocationDetails obj) {
		return obj.getEndDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
		this.currentsectionName = "";
	}

}
