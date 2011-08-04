package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.TrialBalanceServerReport;

public class TrialBalanceReport extends AbstractReportView<TrialBalance> {

	public TrialBalanceReport() {
		this.serverReport = new TrialBalanceServerReport(this);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				ActionFactory.getTransactionDetailByAccountAction());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getTrialBalance(start, end, this);
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
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 113, "",
				"");
	}

	@Override
	public void printPreview() {

	}

	public int sort(TrialBalance obj1, TrialBalance obj2, int col) {
		if (ClientCompanyPreferences.get().getUseAccountNumbers() == true) {
			switch (col) {
			case 0:
				return obj1.getAccountName().toLowerCase()
						.compareTo(obj2.getAccountName().toLowerCase());
			case 1:
				return obj1.getAccountNumber().toLowerCase()
						.compareTo(obj2.getAccountNumber().toLowerCase());
			case 2:
				return UIUtils.compareDouble(obj1.getDebitAmount(),
						obj2.getDebitAmount());
			case 3:
				return UIUtils.compareDouble(obj1.getCreditAmount(),
						obj2.getCreditAmount());
			}
			return 0;
		} else {
			switch (col) {
			case 0:
				return obj1.getAccountName().toLowerCase()
						.compareTo(obj2.getAccountName().toLowerCase());
			case 1:
				return (Integer) null;
			case 2:
				return UIUtils.compareDouble(obj1.getDebitAmount(),
						obj2.getDebitAmount());
			case 3:
				return UIUtils.compareDouble(obj1.getCreditAmount(),
						obj2.getCreditAmount());
			}
			return 0;
		}

	}

	public void exportToCsv() {

		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 113, "",
				"");
	}

}
