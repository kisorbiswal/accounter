package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.VATUncategorisedAmountsServerReport;

@SuppressWarnings("unchecked")
public class VATUncategorisedAmountsReport extends
		AbstractReportView<UncategorisedAmountsReport> {

	@SuppressWarnings("unused")
	private double balance = 0.0;

	public VATUncategorisedAmountsReport() {
		super();
		this.serverReport = new VATUncategorisedAmountsServerReport(this);
	}

	@Override
	public void OnRecordClick(UncategorisedAmountsReport record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(), record
				.getStringId()
				+ "");
	}

	@Override
	public void init() {
		super.init();
	}

	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		balance = 0.0;
		FinanceApplication.createReportService().getUncategorisedAmountsReport(
				start.getTime(), end.getTime(), this);

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

		UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 140, "", "");
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 140, "", "");

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public int sort(UncategorisedAmountsReport obj1,
			UncategorisedAmountsReport obj2, int col) {

		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(), obj2
					.getTransactionType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return UIUtils.compareInt(Integer.parseInt(obj1
					.getTransactionNumber()), Integer.parseInt(obj2
					.getTransactionNumber()));
		case 3:
			return obj1.getSourceName().toLowerCase().compareTo(
					obj2.getSourceName().toLowerCase());
		case 4:
			return obj1.getMemo().toLowerCase().compareTo(
					obj2.getMemo().toLowerCase());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

}