package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByItemDetailServerReport;

public class SalesByItemDetailReport extends
		AbstractReportView<SalesByCustomerDetail> {

	public long bycustomerDetail;

	public SalesByItemDetailReport() {
		this.serverReport = new SalesByItemDetailServerReport(this);
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
			Accounter.createReportService().getSalesByItemDetail(start, end,
					this);
			return;
		} else if (byCustomerDetail.getItemName() != null) {
			Accounter.createReportService().getSalesByItemDetail(
					byCustomerDetail.getItemid(),
					byCustomerDetail.getStartDate(),
					byCustomerDetail.getEndDate(), this);
		}
		this.bycustomerDetail = byCustomerDetail.getTransactionId();
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		long itemId = this.data != null ? ((SalesByCustomerDetail) this.data)
				.getItemid() : 0;
		if (itemId == 0) {
			UIUtils.generateReportPDF(
					Integer.parseInt(String.valueOf(startDate.getDate())),
					Integer.parseInt(String.valueOf(endDate.getDate())), 124,
					"", "", "");
		} else {
			UIUtils.generateReportPDF(
					Integer.parseInt(String.valueOf(startDate.getDate())),
					Integer.parseInt(String.valueOf(endDate.getDate())), 124,
					"", "", itemId);
		}

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {

		int ret = obj1.getItemName().toLowerCase()
				.compareTo(obj2.getItemName().toLowerCase());
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
			return obj1.getItemName().toLowerCase()
					.compareTo(obj2.getItemName().toLowerCase());
		case 4:
			return UIUtils.compareTo(obj1.getQuantity(), obj2.getQuantity());
		case 5:
			return UIUtils.compareDouble(obj1.getUnitPrice(),
					obj2.getUnitPrice());

		case 6:
			return UIUtils
					.compareDouble(obj1.getDiscount(), obj2.getDiscount());

		case 7:
			UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	public void exportToCsv() {
		if (bycustomerDetail == 0) {
			UIUtils.exportReport(
					Integer.parseInt(String.valueOf(startDate.getDate())),
					Integer.parseInt(String.valueOf(endDate.getDate())), 124,
					"", "");
		} else {
			UIUtils.exportReport(
					Integer.parseInt(String.valueOf(startDate.getDate())),
					Integer.parseInt(String.valueOf(endDate.getDate())), 124,
					"", "", bycustomerDetail);
		}
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
