package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.VATItemSummaryServerReport;

@SuppressWarnings("unchecked")
public class VATItemSummaryReport extends AbstractReportView<VATItemSummary> {

	public VATItemSummaryReport() {
		super(false, FinanceApplication.getVendorsMessages().norecordstoshow());
		this.serverReport = new VATItemSummaryServerReport(this);
	}

	@Override
	public void OnRecordClick(VATItemSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils
				.runAction(record, ReportsActionFactory
						.getVaTItemDetailAction());

	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getVATItemSummaryReport(
				start.getTime(), end.getTime(), this);

	}

	@Override
	public void processRecord(VATItemSummary record) {

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 141, "", "");
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 141, "", "");
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public int sort(VATItemSummary obj1, VATItemSummary obj2, int col) {

		switch (col) {
		case 0:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

}
