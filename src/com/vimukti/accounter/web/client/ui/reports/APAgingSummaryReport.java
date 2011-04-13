package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.APAgingSummaryServerReport;

@SuppressWarnings("unchecked")
public class APAgingSummaryReport extends AbstractReportView<DummyDebitor> {

	public APAgingSummaryReport() {
		this.serverReport = new APAgingSummaryServerReport(this);
	}

	@Override
	public void OnRecordClick(DummyDebitor record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, ReportsActionFactory
				.getAorpAgingDetailAction());

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getCreditors(start.getTime(),
				new ClientFinanceDate().getTime(), this);

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 127, "", "");

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	public int sort(DummyDebitor obj1, DummyDebitor obj2, int col) {
		switch (col) {
		case 0:
			return obj1.getDebitorName().toLowerCase().compareTo(
					obj2.getDebitorName().toLowerCase());

		case 1:
			return UIUtils.compareDouble((obj1.getDebitdays_in30() + obj1
					.getDebitdays_incurrent()),
					(obj2.getDebitdays_in30() + obj2.getDebitdays_incurrent()));
		case 2:
			return UIUtils.compareDouble(obj1.getDebitdays_in60(), obj2
					.getDebitdays_in60());
		case 3:
			return UIUtils.compareDouble(obj1.getDebitdays_in90(), obj2
					.getDebitdays_in90());
		case 4:
			return UIUtils.compareDouble(obj1.getDebitdays_inolder(), obj2
					.getDebitdays_inolder());
		case 5:
			return UIUtils.compareDouble((obj1.getDebitdays_in30()
					+ obj1.getDebitdays_in60() + obj1.getDebitdays_in90()
					+ obj1.getDebitdays_inolder() + obj1
					.getDebitdays_incurrent()), (obj2.getDebitdays_in30()
					+ obj2.getDebitdays_in60() + obj2.getDebitdays_in90()
					+ obj2.getDebitdays_inolder() + obj2
					.getDebitdays_incurrent()));
		}
		return 0;
	}

	@Override
	public DummyDebitor getObject(DummyDebitor parent, DummyDebitor child) {
		return null;
	}

	public void exportToCsv() {
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 127, "", "");
	}

}
