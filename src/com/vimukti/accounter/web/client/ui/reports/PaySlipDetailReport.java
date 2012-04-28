package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.NumberReportInput;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PaySlipDetailServerReport;

public class PaySlipDetailReport extends AbstractReportView<PaySlipDetail> {

	public PaySlipDetailReport() {
		this.serverReport = new PaySlipDetailServerReport(this) {
			@Override
			protected String getAmountAsString(PaySlipDetail detail,
					double amount) {
				if (detail == null) {
					return DataUtils.getAmountAsStringInPrimaryCurrency(amount);
				}
				if (detail.getType() == 1) {
					return detail.getAmount()
							+ ClientPayHead.getCalculationPeriod(detail
									.getPeriodType());
				} else {
					return DataUtils.getAmountAsStringInPrimaryCurrency(detail
							.getAmount());
				}
			}
		};
	}

	@Override
	public void initData() {
		if (data != null) {
			PaySlipDetail headSummary = (PaySlipDetail) data;
			EmployeeReportToolbar employeeToolBar = (EmployeeReportToolbar) this.toolbar;
			employeeToolBar.setEmployee(headSummary.getEmployeeId());
		}
		super.initData();
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		EmployeeReportToolbar employeeToolBar = (EmployeeReportToolbar) this.toolbar;
		long employeeId = employeeToolBar.getSelectedEmployee() == null ? data == null ? 0
				: ((PaySlipDetail) data).getEmployeeId()
				: employeeToolBar.getSelectedEmployee().getID();
		Accounter.createPayrollService().getPaySlipDetail(employeeId, start,
				end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_EMPLOYEE;
	}

	@Override
	public void OnRecordClick(PaySlipDetail record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void export(int generationType) {
		EmployeeReportToolbar employeeToolBar = (EmployeeReportToolbar) this.toolbar;
		long employeeId = employeeToolBar.getSelectedEmployee() == null ? data == null ? 0
				: ((PaySlipDetail) data).getEmployeeId()
				: employeeToolBar.getSelectedEmployee().getID();
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), REPORT_TYPE_PAYSLIP_DETAIL,
				new NumberReportInput(employeeId));
	}

}