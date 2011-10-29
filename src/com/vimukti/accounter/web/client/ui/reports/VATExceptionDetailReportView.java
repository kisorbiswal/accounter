package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.VATExceptionServerReport;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VATExceptionDetailReportView extends AbstractReportView<VATDetail> {

	public VATExceptionDetailReportView() {
		super(false, Accounter.constants().noRecordsToShow());
		this.serverReport = new VATExceptionServerReport(this);

	}

	@Override
	public void initData() {

		Object data = getData();
		if (data != null) {

			List<VATDetail> vatDetail = (List<VATDetail>) data;
			DateRangeReportToolbar dateRangeReportToolbar = (DateRangeReportToolbar) this.toolbar;
			dateRangeReportToolbar.fromItem.setEnteredDate(this.startDate);
			dateRangeReportToolbar.toItem.setEnteredDate(this.endDate);
			dateRangeReportToolbar.fromItem.setDisabled(true);
			dateRangeReportToolbar.toItem.setDisabled(true);

			this.serverReport.initRecords(vatDetail);

		}
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void OnRecordClick(VATDetail record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		Accounter.createReportService().getVATExceptionDetailReport(start, end,
				this);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 166, "",
				"");
	}

	@Override
	public void printPreview() {

	}

	public int sort(VATDetail obj1, VATDetail obj2, int col) {
		int ret = obj1.getBoxName().compareTo(obj2.getBoxName());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getTransactionName().compareTo(
					obj2.getTransactionName());
		case 1:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 2:
			return UIUtils.compareInt(
					Integer.parseInt(obj1.getTransactionNumber()),
					Integer.parseInt(obj2.getTransactionNumber()));
		case 3:
			return obj1.getPayeeName().compareTo(obj2.getPayeeName());
		case 4:
			return UIUtils.compareDouble(obj1.getVatRate(), obj2.getVatRate());
		case 5:
			return UIUtils.compareDouble(obj1.getNetAmount(),
					obj2.getNetAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 138, "",
				"");
	}
}
