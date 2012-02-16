package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.UnBilledCostsByJob;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.UnBilledCostsByJobServerReport;

public class UnBilledCostsByJobReport extends
		AbstractReportView<UnBilledCostsByJob> {
	public UnBilledCostsByJobReport() {
		this.serverReport = new UnBilledCostsByJobServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getUnBilledCostsByJob(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(UnBilledCostsByJob record) {
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void print() {
		String customerName = this.data != null ? ((UnBilledCostsByJob) this.data)
				.getCustomerName() : "";
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 186, "",
				"", customerName);
	}

	@Override
	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 186, "",
				"");
	}
}
