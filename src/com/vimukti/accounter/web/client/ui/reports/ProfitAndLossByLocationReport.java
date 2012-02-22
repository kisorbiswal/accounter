package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.ProfitAndLossByLocationServerReport;

public class ProfitAndLossByLocationReport extends
		AbstractReportView<ProfitAndLossByLocation> {

	private int category_type;

	public ProfitAndLossByLocationReport(int category_type) {
		this.category_type = category_type;
		ProfitAndLossByLocationServerReport.locations = Accounter.getCompany()
				.getLocations();
		ProfitAndLossByLocationServerReport.classes = Accounter.getCompany()
				.getAccounterClasses();
		ProfitAndLossByLocationServerReport.jobs = Accounter.getCompany()
				.getJobs();
		int numcolumns = 0;
		if (category_type == ProfitAndLossByLocationServerReport.JOB) {
			numcolumns = ProfitAndLossByLocationServerReport.jobs.size() + 2;
		} else if (category_type == ProfitAndLossByLocationServerReport.LOCATION) {
			numcolumns = ProfitAndLossByLocationServerReport.locations.size() + 2;
		} else {
			numcolumns = ProfitAndLossByLocationServerReport.classes.size() + 2;
		}
		ProfitAndLossByLocationServerReport.noColumns = numcolumns;
		this.serverReport = new ProfitAndLossByLocationServerReport(this,
				category_type);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getProfitAndLossByLocationReport(
				category_type, start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(ProfitAndLossByLocation p) {
		TrialBalance record = getTrailBalance(p);
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				ActionFactory.getTransactionDetailByAccountAction());

	}

	public void OnClick(ProfitAndLossByLocation pAndLossByLocation,
			int rowIndex, int cellIndex) {
		TrialBalance record = getTrailBalance(pAndLossByLocation);
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				ActionFactory.getTransactionDetailByAccountAction());
	}

	private TrialBalance getTrailBalance(ProfitAndLossByLocation p) {
		TrialBalance record = new TrialBalance();
		record.setAccountId(p.getAccountId());
		record.setAccountType(p.getAccountType());
		record.setAccountName(p.getAccountName());
		record.setAccountNumber(p.getAccountNumber());
		return record;
	}

	@Override
	public void restoreView(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

	@Override
	public void print() {
		int reportType = 0;
		if (category_type == 1) {
			reportType = 161;
		} else if (category_type == 2) {
			reportType = 153;
		} else {
			reportType = 189;
		}
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				reportType, "", "");
	}

	@Override
	public void exportToCsv() {
		int reportType = 0;
		if (category_type == 1) {
			reportType = 161;
		} else if (category_type == 2) {
			reportType = 153;
		} else {
			reportType = 189;
		}
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				reportType, "", "");
	}
}
