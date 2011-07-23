package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByItemDetailServerReport;

@SuppressWarnings("unchecked")
public class PurchaseByItemDetailReport extends
		AbstractReportView<SalesByCustomerDetail> {
	public String byCustomerDetail;

	public PurchaseByItemDetailReport() {
		this.serverReport = new PurchaseByItemDetailServerReport(this);
	}

	@Override
	public void OnRecordClick(SalesByCustomerDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getType(), record
					.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesByCustomerDetail byCustomerDetail = (SalesByCustomerDetail) this.data;
		if (byCustomerDetail == null) {
			Accounter.createReportService().getPurchasesByItemDetail(
					start.getTime(), end.getTime(), this);
		} else if (byCustomerDetail.getItemName() != null) {
			Accounter.createReportService().getPurchasesByItemDetail(
					byCustomerDetail.getItemName(), start.getTime(),
					end.getTime(), this);
		}
		this.byCustomerDetail = byCustomerDetail.getItemName();
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
		if (byCustomerDetail == null) {

			UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
					.getTime())), Integer.parseInt(String.valueOf(endDate
					.getTime())), 133, "", "");
		} else {
			UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
					.getTime())), Integer.parseInt(String.valueOf(endDate
					.getTime())), 133, "", "", byCustomerDetail);
		}
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {

		int ret = obj1.getItemName().toLowerCase().compareTo(
				obj2.getItemName().toLowerCase());
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
			return obj1.getItemName().toLowerCase().compareTo(
					obj2.getItemName().toLowerCase());

		case 4:
			return UIUtils
					.compareDouble(obj1.getQuantity(), obj2.getQuantity());
		case 5:
			return UIUtils.compareDouble(obj1.getUnitPrice(), obj2
					.getUnitPrice());

		case 6:
			return UIUtils
					.compareDouble(obj1.getDiscount(), obj2.getDiscount());

		case 7:
			UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	public void exportToCsv() {
		if (byCustomerDetail == null) {
			UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
					.getTime())), Integer.parseInt(String.valueOf(endDate
					.getTime())), 133, "", "");
		} else {
			UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
					.getTime())), Integer.parseInt(String.valueOf(endDate
					.getTime())), 133, "", "", byCustomerDetail);
		}
	}
}
