package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.AbstractReportView;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class CustomerStatementReport extends
		AbstractReportView<PayeeStatementsList> {
	public int precategory = 1001;
	public long customerId;

	public CustomerStatementReport() {
		this.serverReport = new CustomerStatementServerReport(this);
	}
	
	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeReportRequest(int customer, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		Accounter.createReportService().getCustomerStatement(customer,
				startDate.getDate(), endDate.getDate(), this);
		customerId = customer;
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_CUSTOMER;
	}

	@Override
	public void OnRecordClick(PayeeStatementsList record) {
		record.setStartDate(toolbar.getEndDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactiontype(),
				record.getTransactionId());

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 150, "",
				"", customerId);
	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 150, "",
				"", customerId);
	}

	@Override
	public void printPreview() {

	}

	@Override
	public PayeeStatementsList getObject(PayeeStatementsList parent,
			PayeeStatementsList child) {
		return super.getObject(parent, child);
	}

	@Override
	public int sort(PayeeStatementsList obj1, PayeeStatementsList obj2, int col) {
		//TODO
		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return UIUtils.compareTo(obj1.getTransactionNumber(),
						obj2.getTransactionNumber());

		case 1:
			return UIUtils.compareInt(obj1.getTransactiontype(),
					obj2.getTransactiontype());

		case 3:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 0:
			return UIUtils.compareTo(obj1.getTransactionDate(),
					obj2.getTransactionDate());

		case 4:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());

		}
		return 0;
	}

	@Override
	public String getDefaultDateRange() {
		return Accounter.constants().all();
	}

}
