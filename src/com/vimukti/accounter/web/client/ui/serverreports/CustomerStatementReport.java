package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.AbstractReportView;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class CustomerStatementReport extends
		AbstractReportView<PayeeStatementsList> {
	public ClientCompanyPreferences preferences;
	
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
	public String getDefaultDateRange() {
		return Accounter.constants().financialYearToDate();
	}

}
