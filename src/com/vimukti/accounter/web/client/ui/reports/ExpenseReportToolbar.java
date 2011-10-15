package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.Accounter;
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
	private int status;

	public ExpenseReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.constants().all();
	}

	private void createControls() {
		String[] statusArray;
		if (ClientCompanyPreferences.get().isHaveEpmloyees()
				&& ClientCompanyPreferences.get().isTrackEmployeeExpenses()) {
			statusArray = new String[] { Accounter.constants().allExpenses(),
					Accounter.constants().cash(),
					Accounter.constants().creditCard(),
					Accounter.constants().employee() };
		} else {
			statusArray = new String[] { Accounter.constants().allExpenses(),
					Accounter.constants().cash(),
					Accounter.constants().creditCard() };
		}

		String[] dateRangeArray = { Accounter.constants().all(),
				Accounter.constants().thisWeek(),
				Accounter.constants().thisMonth(),
				Accounter.constants().lastWeek(),
				Accounter.constants().lastMonth(),
				Accounter.constants().thisFinancialYear(),
				Accounter.constants().lastFinancialYear(),
				Accounter.constants().thisFinancialQuarter(),
				Accounter.constants().lastFinancialQuarter(),
				Accounter.constants().financialYearToDate(),
				Accounter.constants().custom() };

		expenseCombo = new SelectCombo(Accounter.constants().expenseRealtedTo());
		expenseCombo.setHelpInformation(true);
		statusList = new ArrayList<String>();
		for (int i = 0; i < statusArray.length; i++) {
			statusList.add(statusArray[i]);
		}
		expenseCombo.initCombo(statusList);
		expenseCombo.setComboItem(statusArray[0]);
		expenseCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.toString().equals(
								Accounter.constants().allExpenses())) {
							/*
							 * status 0 used to get all expenses like Cash,
							 * Credit Card
							 */

							status = 0;
						} else if (selectItem.toString().equals(
								Accounter.constants().cash())) {
							status = ClientTransaction.TYPE_CASH_EXPENSE;
						} else if (selectItem.toString().equals(
								Accounter.constants().creditCard())) {
							status = ClientTransaction.TYPE_CREDIT_CARD_EXPENSE;
						} else if (selectItem.toString().equals(
								Accounter.constants().employee())) {
							status = ClientTransaction.TYPE_EMPLOYEE_EXPENSE;
						}

						ClientFinanceDate startDate = fromItem.getDate();
						ClientFinanceDate endDate = toItem.getDate();
						reportview
								.makeReportRequest(status, startDate, endDate);

					}
				});

		dateRangeCombo = new SelectCombo(Accounter.constants().dateRange());
		dateRangeCombo.setHelpInformation(true);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeCombo.initCombo(dateRangeList);
		dateRangeCombo.setDefaultValue(dateRangeArray[0]);
		dateRangeCombo
				.setComboItem(Accounter.constants().financialYearToDate());
		dateRangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						dateRangeChanged(dateRangeCombo.getSelectedValue());

					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		fromItem.setTitle(Accounter.constants().from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();

		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.setTitle(Accounter.constants().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = (ClientFinanceDate) fromItem.getValue();
				endDate = (ClientFinanceDate) toItem.getValue();
			}
		});
		updateButton = new Button(Accounter.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				changeDates(fromItem.getDate(), toItem.getDate());
				dateRangeCombo.setDefaultValue(Accounter.constants().custom());
				dateRangeCombo.setComboItem(Accounter.constants().custom());
				setSelectedDateRange(Accounter.constants().custom());

			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		Button printButton = new Button(Accounter.constants().print());
		// printButton.setTop(2);
		// printButton.setWidth(40);
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		// if (UIUtils.isMSIEBrowser()) {
		// dateRangeCombo.setWidth("170px");
		// expenseCombo.setWidth("90px");
		// }
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
		dateRangeCombo.setComboItem(defaultDateRange);
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
