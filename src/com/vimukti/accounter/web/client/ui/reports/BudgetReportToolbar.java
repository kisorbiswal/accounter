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

public class BudgetReportToolbar extends ReportToolbar {

	private DateItem fromItem;
	private DateItem toItem;
	protected SelectCombo budgetCombo, budgetType;
	protected List<String> statusList, dateRangeList;
	private Button updateButton;
	public static int EMPLOYEE = 1;
	public static int CASH = 2;
	public static int CREDITCARD = 3;
	private int status;

	public BudgetReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.constants().all();
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		// TODO Auto-generated method stub

	}

	public void createControls() {

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

		String[] dateRangeArray = { Accounter.constants().custom(),
				Accounter.constants().accountVSmonths(),
				Accounter.constants().accountVSquaters(),
				Accounter.constants().accountVSyears() };

		budgetCombo = new SelectCombo(Accounter.constants().budget());
		budgetCombo.setHelpInformation(true);
		statusList = new ArrayList<String>();
		for (int i = 0; i < statusArray.length; i++) {
			statusList.add(statusArray[i]);
		}
		budgetCombo.initCombo(statusList);
		budgetCombo.setComboItem(statusArray[0]);
		budgetCombo
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

		budgetType = new SelectCombo(Accounter.constants().budgetType());
		budgetType.setHelpInformation(true);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		budgetType.initCombo(dateRangeList);
		budgetType.setDefaultValue(dateRangeArray[0]);
		budgetType.setComboItem(Accounter.constants().all());
		budgetType
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						dateRangeChanged(budgetType.getSelectedValue());

					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		fromItem.setTitle(Accounter.constants().from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		ClientFinanceDate date = Accounter.getCompany()
				.getLastandOpenedFiscalYearEndDate();

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
				budgetType.setDefaultValue(Accounter.constants().custom());
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

		if (UIUtils.isMSIEBrowser()) {
			budgetType.setWidth("170px");
			budgetCombo.setWidth("90px");
		}
		addItems(budgetCombo, budgetType, fromItem, toItem);
		add(updateButton);
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);

	}

}
