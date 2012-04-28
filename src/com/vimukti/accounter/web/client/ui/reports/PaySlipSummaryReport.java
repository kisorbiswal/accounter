package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.StringReportInput;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.core.reports.PaySlipSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PaySlipSummaryServerReport;

public class PaySlipSummaryReport extends AbstractReportView<PaySlipSummary> {

	public PaySlipSummaryReport() {
		this.serverReport = new PaySlipSummaryServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createPayrollService().getPaySlipSummary(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(PaySlipSummary record) {
		PaySlipDetail payslipDetail = getPaySlipDetail(record);
		payslipDetail.setStartDate(toolbar.getStartDate());
		payslipDetail.setEndDate(toolbar.getEndDate());
		payslipDetail.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(payslipDetail, new PaySlipDetailReportAction());
	}

	private PaySlipDetail getPaySlipDetail(PaySlipSummary record) {
		PaySlipDetail payslipDetail = new PaySlipDetail();
		payslipDetail.setEmployeeId(record.getEmployeeId());
		return payslipDetail;
	}

	@Override
	public void export(int generationType) {
		String accountName = data != null ? ((PaySlipSummary) data).getName()
				: null;
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), REPORT_TYPE_PAYSLIP_SUMMARY,
				new StringReportInput(accountName));
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
}
