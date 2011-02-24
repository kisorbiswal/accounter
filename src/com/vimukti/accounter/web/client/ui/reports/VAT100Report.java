package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientVATAgency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.VAT100ServerReport;

@SuppressWarnings("unchecked")
public class VAT100Report extends AbstractReportView<VATSummary> {
	@SuppressWarnings("unused")
	private String sectionName = "";
	@SuppressWarnings("unused")
	private int row = -1;
	private String vatAgency;

	protected Double box3amount = 0.0D;
	protected Double box4amount = 0.0D;

	public VAT100Report() {
		super(false, "No records to show");
		this.serverReport = new VAT100ServerReport(this);
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions(FinanceApplication.getReportsMessages()
				.all(), FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),
				FinanceApplication.getReportsMessages().financialYearToDate(),
				FinanceApplication.getReportsMessages().lastVATQuarter(),
				FinanceApplication.getReportsMessages().lastVATQuarterToDate(),
				FinanceApplication.getReportsMessages().custom());

		// Make rpc request for default VAT Agency and default DateRange
		List<ClientTAXAgency> vatAgencies = FinanceApplication.getCompany()
				.getTaxAgencies();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equalsIgnoreCase(
					FinanceApplication.getReportsMessages()
							.hmCustomsExciseVAT())) {
				ClientFinanceDate date = new ClientFinanceDate();
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				ClientFinanceDate startDate = new ClientFinanceDate(date
						.getYear(), startMonth, 1);
				ClientFinanceDate start = startDate;
				ClientFinanceDate end = date;
				makeReportRequest(vatAgency.getStringID(), start, end);
				break;
			}
		}
	}

	@Override
	public void OnRecordClick(VATSummary record) {
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				"",
				UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "-"
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()) };
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE_VATAGENCY;
	}

	@Override
	public void makeReportRequest(String vatAgency,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		// row = -1;
		// this.sectionName = "";
		FinanceApplication.createReportService().getVAT100Report(vatAgency,
				startDate.getTime(), endDate.getTime(), this);
		this.vatAgency = vatAgency;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

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
				.valueOf(endDate.getTime())), 137, "", "", vatAgency);

		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 137, "", "", vatAgency);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public int sort(VATSummary obj1, VATSummary obj2, int col) {

		// switch (col) {
		// case 0:
		// return obj1.getName().compareTo(obj2.getName());
		// case 1:
		// return UIUtils.compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
	}

}
