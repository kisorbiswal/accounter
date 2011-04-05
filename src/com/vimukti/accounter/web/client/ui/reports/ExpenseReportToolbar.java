package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class ExpenseReportToolbar extends ReportToolbar {
	private DateItem fromItem;
	private DateItem toItem;
	protected SelectCombo expenseCombo, dateRangeCombo;
	protected List<String> statusList, dateRangeList;
	private Button updateButton;
	public static int EMPLOYEE = 1;
	public static int CASH = 2;
	public static int CREDITCARD = 3;
	private int status = ClientTransaction.TYPE_EMPLOYEE_EXPENSE;

	public ExpenseReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.getReportsMessages().all();
	}

	private void createControls() {
		String[] statusArray = {
				FinanceApplication.getReportsMessages().employee(),
				FinanceApplication.getReportsMessages().cash(),
				FinanceApplication.getReportsMessages().creditCard() };

		String[] dateRangeArray = {
				FinanceApplication.getReportsMessages().all(),
				FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),
				FinanceApplication.getReportsMessages().financialYearToDate(),
				FinanceApplication.getReportsMessages().custom() };

		expenseCombo = new SelectCombo(FinanceApplication.getReportsMessages()
				.expenseRealtedTo());
		expenseCombo.setHelpInformation(true);
		statusList = new ArrayList<String>();
		for (int i = 0; i < statusArray.length; i++) {
			statusList.add(statusArray[i]);
		}
		expenseCombo.initCombo(statusList);
		expenseCombo.setDefaultValue(statusArray[0]);
		expenseCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (expenseCombo.getSelectedValue().equals(
								FinanceApplication.getReportsMessages()
										.employee())) {
							status = ClientTransaction.TYPE_EMPLOYEE_EXPENSE;
						} else if (expenseCombo.getSelectedValue().equals(
								FinanceApplication.getReportsMessages().cash())) {
							status = ClientTransaction.TYPE_CASH_EXPENSE;
						} else
							status = ClientTransaction.TYPE_CREDIT_CARD_EXPENSE;
						ClientFinanceDate startDate = fromItem.getDate();
						ClientFinanceDate endDate = toItem.getDate();
						reportview
								.makeReportRequest(status, startDate, endDate);

					}
				});

		dateRangeCombo = new SelectCombo(FinanceApplication
				.getReportsMessages().dateRange());
		dateRangeCombo.setHelpInformation(true);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeCombo.initCombo(dateRangeList);
		dateRangeCombo.setDefaultValue(dateRangeArray[0]);
		dateRangeCombo.setComboItem(FinanceApplication.getReportsMessages().all());
		dateRangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						dateRangeChanged(dateRangeCombo.getSelectedValue());

					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setDatethanFireEvent(FinanceApplication.getStartDate());
		fromItem.setTitle(FinanceApplication.getReportsMessages().from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		ClientFinanceDate date = Utility.getLastandOpenedFiscalYearEndDate();

		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.setTitle(FinanceApplication.getReportsMessages().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = (ClientFinanceDate) fromItem.getValue();
				endDate = (ClientFinanceDate) toItem.getValue();
			}
		});
		updateButton = new Button(FinanceApplication.getReportsMessages()
				.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				changeDates(fromItem.getDate(), toItem.getDate());
				dateRangeCombo.setDefaultValue(FinanceApplication
						.getReportsMessages().custom());
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.custom());

			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		Button printButton = new Button(FinanceApplication.getReportsMessages()
				.print());
		// printButton.setTop(2);
		// printButton.setWidth(40);
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		if (UIUtils.isMSIEBrowser()) {
			dateRangeCombo.setWidth("170px");
			expenseCombo.setWidth("90px");
		}
		addItems(expenseCombo, dateRangeCombo, fromItem, toItem);
		add(updateButton);
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview.makeReportRequest(status, startDate, endDate);

		// itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
		// endDate);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeCombo.setValueMap(dateRanages);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeCombo.setDefaultValue(defaultDateRange);
		dateRangeChanged(defaultDateRange);
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setEnteredDate(startDate);
			toItem.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}
	}
}
