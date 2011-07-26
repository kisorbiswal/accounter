package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.ECSalesListServerReport;

@SuppressWarnings("unchecked")
public class ECSalesListReport extends AbstractReportView<ECSalesList> {

	public ECSalesListReport() {
		this.serverReport = new ECSalesListServerReport(this);
	}

	@Override
	public void OnRecordClick(ECSalesList record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsActionFactory.getECSalesListDetailAction().run(record, true);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getECSalesListReport(start.getTime(),
				end.getTime(), this);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getTime())),
				Integer.parseInt(String.valueOf(endDate.getTime())), 142, "",
				"");

	}

	@Override
	public void printPreview() {

	}

	public int sort(ECSalesList obj1, ECSalesList obj2, int col) {
		switch (col) {
		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getTime())),
				Integer.parseInt(String.valueOf(endDate.getTime())), 142, "",
				"");
	}
}
