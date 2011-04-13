package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PriorVATReturnsServerReport;

@SuppressWarnings("unchecked")
public class PriorVATReturnsReport extends AbstractReportView<VATSummary> {

	@SuppressWarnings("unused")
	private int row = -1;
	private String vatAgency;

	public PriorVATReturnsReport() {
		super(false, FinanceApplication.getReportsMessages()
				.pleaseSelectVATAgencyAndEndingDateToViewReport());
		isVATSummaryReport = true;
		this.serverReport = new PriorVATReturnsServerReport(this);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void OnRecordClick(VATSummary record) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_PRIOR_VATRETURN;
	}

	@Override
	public void makeReportRequest(String vatAgency, ClientFinanceDate end) {
		this.row = -1;
		FinanceApplication.createReportService().getPriorReturnVATSummary(
				vatAgency, end.getTime(), this);
		this.vatAgency = vatAgency;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		removeLoadingImage();

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
				.valueOf(endDate.getTime())), 136, "", "", vatAgency);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public int sort(VATSummary obj1, VATSummary obj2, int col) {

		// switch (col) {
		// case 0:
		// return obj1.getName().toLowerCase().compareTo(
		// obj2.getName().toLowerCase());
		// case 1:
		// return UIUtils.compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 147, "", "");
	}

}
