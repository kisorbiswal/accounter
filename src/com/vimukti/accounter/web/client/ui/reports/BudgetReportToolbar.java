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
	protected SelectCombo budgetCombo, budgetType;
	protected List<String> statusList, dateRangeList;
	private Button updateButton;
	List<String> budgetArray = new ArrayList<String>();
	List<Long> idArray = new ArrayList<Long>();

	private Long isStatus;

	public BudgetReportToolbar() {

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

		String[] dateRangeArray = { Accounter.constants().accountVScustom(),
				Accounter.constants().accountVSmonths(),
				Accounter.constants().accountVSquaters(),
				Accounter.constants().accountVSyears() };

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

		budgetType = new SelectCombo(Accounter.constants().budgetType());
		budgetType.setHelpInformation(true);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		budgetType.initCombo(dateRangeList);
		budgetType.setDefaultValue(dateRangeArray[0]);
		budgetType
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
