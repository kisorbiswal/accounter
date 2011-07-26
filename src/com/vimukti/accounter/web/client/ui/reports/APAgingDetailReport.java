package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.APAgingDetailServerReport;

/**
 * Modified By Ravi Kiran.G
 * 
 */
@SuppressWarnings("unchecked")
public class APAgingDetailReport extends AbstractReportView<AgedDebtors> {

	public long byCustomerDetail;

	public APAgingDetailReport() {
		this.serverReport = new APAgingDetailServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		DummyDebitor byCustomerDetail = (DummyDebitor) this.data;
		if (byCustomerDetail == null) {
			Accounter.createReportService().getAgedCreditors(start.getTime(),
					new ClientFinanceDate().getTime(), this);
		} else if (byCustomerDetail.getDebitorName() != null) {
			Accounter.createReportService().getAgedCreditors(
					byCustomerDetail.getDebitorName(), start.getTime(),
					new ClientFinanceDate().getTime(), this);
		}
		this.byCustomerDetail = byCustomerDetail.getTransactionId();
	}

	@Override
	public void OnRecordClick(AgedDebtors record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getType(),
					record.getTransactionId());
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		if (byCustomerDetail == 0) {
			UIUtils.generateReportPDF(
					Integer.parseInt(String.valueOf(startDate.getTime())),
					Integer.parseInt(String.valueOf(endDate.getTime())), 128,
					"", "");
		} else {
			UIUtils.generateReportPDF(
					Integer.parseInt(String.valueOf(startDate.getTime())),
					Integer.parseInt(String.valueOf(endDate.getTime())), 128,
					"", "", byCustomerDetail);
		}

	}

	public int sort(AgedDebtors obj1, AgedDebtors obj2, int col) {

		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {

		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 1:
			return UIUtils.compareTo(obj1.getDate(), obj2.getDate());
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());

		case 3:
			// return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
			// Integer.parseInt(obj2.getNumber()));
			return UIUtils.compareTo(obj1.getNumber(), obj2.getNumber());
			// case 4:
			// return obj1.getDueDate().compareTo(obj2.getDueDate());
		case 4:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 5:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	public void exportToCsv() {
		if (byCustomerDetail == 0) {
			UIUtils.exportReport(
					Integer.parseInt(String.valueOf(startDate.getTime())),
					Integer.parseInt(String.valueOf(endDate.getTime())), 128,
					"", "");
		} else {
			UIUtils.exportReport(
					Integer.parseInt(String.valueOf(startDate.getTime())),
					Integer.parseInt(String.valueOf(endDate.getTime())), 128,
					"", "", byCustomerDetail);
		}
	}

}
