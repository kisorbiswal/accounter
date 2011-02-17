package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;

public class CreateStatementToolBar extends ReportToolbar {

	private CustomerCombo customerCombo;
	ClientCustomer selectedCusotmer = null;
	private StatementReport statementReport;

	@SuppressWarnings("unchecked")
	public CreateStatementToolBar(AbstractReportView reportView) {
		this.reportview = reportView;
		createControls();
	}

	public void createControls() {

		customerCombo = new CustomerCombo("Choose Customer", false);
		statementReport = new StatementReport();
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						if (selectItem != null) {
							selectedCusotmer = selectItem;
//							statementReport.precategory = 1001;
							// itemSelectionHandler.onItemSelectionChanged(
							// TYPE_ACCRUAL, new ClientFinanceDate(),
							// new ClientFinanceDate());
							reportRequest();
						}

					}
				});

		if (UIUtils.isMSIEBrowser()) {
			customerCombo.setWidth("200px");
		}
		addItems(customerCombo);
		customerCombo.setSelectedItem(1);
		selectedCusotmer = customerCombo.getSelectedValue();
		reportRequest();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.ReportToolbar#changeDates
	 * (java.util.Date, java.util.Date)
	 */
	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		reportRequest();
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
	}

	public void reportRequest() {
		reportview.makeReportRequest(selectedCusotmer.getStringID(),
				new ClientFinanceDate());
	}

}
