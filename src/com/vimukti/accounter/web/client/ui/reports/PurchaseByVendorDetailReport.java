package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByVendorDetailServerReport;

public class PurchaseByVendorDetailReport extends
		AbstractReportView<SalesByCustomerDetail> {

	public String byCustomerDetail;

	public PurchaseByVendorDetailReport() {
		this.serverReport = new PurchaseByVendorDetailServerReport(this);
	}

	@Override
	public void OnRecordClick(SalesByCustomerDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getType(),
					record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesByCustomerDetail byCustomerDetail = (SalesByCustomerDetail) this.data;
		if (byCustomerDetail == null) {
			Accounter.createReportService().getPurchasesByVendorDetail(start,
					end, this);
			return;
		} else if (byCustomerDetail.getName() != null) {
			Accounter.createReportService().getPurchasesByVendorDetail(
					byCustomerDetail.getName(),
					byCustomerDetail.getStartDate(),
					byCustomerDetail.getEndDate(), this);
		}
		this.byCustomerDetail = byCustomerDetail.getName();
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		String vendorName = this.data != null ? ((SalesByCustomerDetail) this.data)
				.getName() : "";
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 131, "",
				"", vendorName);
	}

	@Override
	public void printPreview() {

	}

	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {

		int ret = obj1.getName().toLowerCase()
				.compareTo(obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
			// case 4:
			// return obj1.getDueDate().compareTo(obj2.getDueDate());
		case 4:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	public void exportToCsv() {

		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 131, "",
				"");
	}

}
