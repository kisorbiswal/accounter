package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public class BudgetOverviewReportToolbar extends ReportToolbar {

	protected SelectCombo budgetName;

	protected List<String> statusList, dateRangeList, yearRangeList;
	private Button updateButton;
	List<String> budgetArray = new ArrayList<String>();
	List<Long> idArray = new ArrayList<Long>();

	private Long budgetId;
	int monthSelected;

	public BudgetOverviewReportToolbar() {
		createData();
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (budgetId != null) {
			reportview.makeReportRequest(budgetId, startDate, endDate);
		}

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		setStartDate(startDate);
		setEndDate(endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeChanged(defaultDateRange);
	}

	public void createControls() {

		budgetName = new SelectCombo(Accounter.messages().budget());
		budgetName.setHelpInformation(true);
		statusList = new ArrayList<String>();
		for (String str : budgetArray) {
			statusList.add(str);
		}
		if (budgetArray.size() < 1) {
			statusList.add("");
		}
		budgetName.initCombo(statusList);
		budgetName.setSelected(statusList.get(0));
		budgetName
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						budgetId = idArray.get(budgetName.getSelectedIndex());
						changeDates(startDate, endDate);
					}
				});

		updateButton = new Button(Accounter.messages().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeDates(startDate, endDate);
			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		Button printButton = new Button(Accounter.messages().print());
		// printButton.setTop(2);
		// printButton.setWidth(40);
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		addItems(budgetName);

		add(updateButton);
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);

	}

	public void createData() {

		Accounter.createHomeService().getBudgetList(
				new AsyncCallback<ArrayList<ClientBudget>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<ClientBudget> budgetList) {
						if (budgetList == null) {
							budgetList = new ArrayList<ClientBudget>();
						}
						for (ClientBudget budget : budgetList) {
							budgetArray.add(budget.getBudgetName());
							idArray.add(budget.getID());
						}
						createControls();

						if (idArray.size() > 0) {
							budgetId = idArray.get(0);
							monthSelected = 1;
							changeDates(new ClientFinanceDate(),
									new ClientFinanceDate());
						} else {
							changeDates(new ClientFinanceDate(),
									new ClientFinanceDate());
						}
					}
				});

	}
}
