package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class BudgetReportToolbar extends ReportToolbar implements
		AsyncCallback<ArrayList<ClientBudget>> {

	private DateItem fromItem;
	private DateItem toItem;
	protected SelectCombo budgetCombo;
	protected SelectCombo budgetYear;
	protected SelectCombo budgetMonth;
	protected List<String> statusList, dateRangeList, yearRangeList;
	private Button updateButton;
	List<String> budgetArray = new ArrayList<String>();
	List<Long> idArray = new ArrayList<Long>();

	private Long isStatus;

	int budgetToolbarType;

	public BudgetReportToolbar(int i) {
		budgetToolbarType = i;
		Accounter.createHomeService().getBudgetList(this);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		reportview.makeReportRequest(isStatus, startDate, endDate);

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

		String[] dateRangeArray = { Accounter.constants().january(),
				Accounter.constants().february(),
				Accounter.constants().march(), Accounter.constants().april(),
				Accounter.constants().may(), Accounter.constants().june(),
				Accounter.constants().july(), Accounter.constants().august(),
				Accounter.constants().september(),
				Accounter.constants().october(),
				Accounter.constants().november(),
				Accounter.constants().december() };

		budgetCombo = new SelectCombo(Accounter.constants().budget());
		budgetCombo.setHelpInformation(true);
		statusList = new ArrayList<String>();
		for (String str : budgetArray) {
			statusList.add(str);
		}
		if (budgetArray.size() < 1) {
			statusList.add(Accounter.constants().emptyValue());
		}
		budgetCombo.initCombo(statusList);
		budgetCombo.setSelected(statusList.get(0));
		budgetCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						isStatus = idArray.get(budgetCombo.getSelectedIndex());
					}
				});

		budgetMonth = new SelectCombo(Accounter.constants().budgetMonth());
		budgetMonth.setHelpInformation(true);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		budgetMonth.initCombo(dateRangeList);
		budgetMonth.setDefaultValue(dateRangeArray[0]);
		budgetMonth
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.endsWith(Accounter.constants()
								.accountVScustom())) {
							fromItem.setDisabled(false);
							toItem.setDisabled(false);
						} else {
							fromItem.setDisabled(true);
							toItem.setDisabled(true);
						}

					}
				});

		budgetYear = new SelectCombo(Accounter.constants().budget() + " "
				+ Accounter.constants().year());
		budgetYear.setHelpInformation(true);
		yearRangeList = new ArrayList<String>();
		for (int i = 1990; i < 2030; i++) {
			yearRangeList.add(Integer.toString(i));
		}
		budgetYear.initCombo(yearRangeList);
		budgetYear.setDefaultValue(yearRangeList.get(0));
		budgetYear
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.endsWith(Accounter.constants()
								.accountVScustom())) {
							fromItem.setDisabled(false);
							toItem.setDisabled(false);
						} else {
							fromItem.setDisabled(true);
							toItem.setDisabled(true);
						}

					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		fromItem.setTitle(Accounter.constants().from());
		fromItem.setDisabled(true);

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		toItem.setDisabled(true);
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
				budgetMonth.setDefaultValue(Accounter.constants().custom());
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
			budgetMonth.setWidth("170px");
			budgetYear.setWidth("170px");
			budgetCombo.setWidth("90px");
		}
		if (budgetToolbarType == 2)
			addItems(budgetCombo, budgetMonth);
		else if (budgetToolbarType == 3)
			addItems(budgetCombo, budgetYear);
		else if (budgetToolbarType == 4)
			addItems(budgetCombo, budgetYear);
		else
			addItems(budgetCombo, fromItem, toItem);

		add(updateButton);
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);

	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(ArrayList<ClientBudget> result) {

		for (ClientBudget budget : result) {
			budgetArray.add(budget.getBudgetName());
			idArray.add(budget.getID());
		}
		createControls();
	}
}
