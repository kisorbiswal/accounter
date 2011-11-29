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
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.server.FinanceTool;

public class BudgetOverviewReportToolbar extends ReportToolbar {

	private DateItem fromItem = new DateItem();
	private DateItem toItem = new DateItem();

	protected SelectCombo budgetName;
	
	protected List<String> statusList, dateRangeList, yearRangeList;
	private Button updateButton;
	List<String> budgetArray = new ArrayList<String>();
	List<Long> idArray = new ArrayList<Long>();

	private Long isStatus;
	int monthSelected;

	public BudgetOverviewReportToolbar() {
		//createControls();
		createData();
	}
	

	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview.makeReportRequest(isStatus, startDate, endDate,
				monthSelected);

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setEnteredDate(startDate);
		toItem.setEnteredDate(endDate);
		setStartDate(startDate);
		setEndDate(endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {

		dateRangeChanged(defaultDateRange);

	}

	public void createControls() {

		/*final String[] dateRangeArray = { Accounter.messages().january(),
				Accounter.messages().february(),
				Accounter.messages().march(), Accounter.messages().april(),
				Accounter.messages().may(), Accounter.messages().june(),
				Accounter.messages().july(), Accounter.messages().august(),
				Accounter.messages().september(),
				Accounter.messages().october(),
				Accounter.messages().november(),
				Accounter.messages().december() };*/

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
						isStatus = idArray.get(budgetName.getSelectedIndex());
					}
				});

	

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		fromItem.setTitle(Accounter.messages().from());
		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();
		fromItem.setValue(date);

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.setTitle(Accounter.messages().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = (ClientFinanceDate) fromItem.getValue();
				endDate = (ClientFinanceDate) toItem.getValue();
			}
		});
		updateButton = new Button(Accounter.messages().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				changeDates(fromItem.getDate(), toItem.getDate());
				setSelectedDateRange(Accounter.messages().custom());

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
		
		List<ClientBudget> budgetList = null;
		
//		Accounter.createReportService().getBudgetItemsList(Accounter.getCompany().getID(), null, null, 1, new AsyncCallback<ArrayList<ClientBudgetListXXXXXXXX>>() {
//			
//			@Override
//			public void onSuccess(ArrayList<Clien> result) {
//					
//				budgetList=result;
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		for (ClientBudget budget : budgetList) {
			budgetArray.add(budget.getBudgetName());
			idArray.add(budget.getID());
		}
		createControls();

		if (idArray.size() > 0) {
			isStatus = idArray.get(0);
			setStartDate(fromItem.getDate());
			setEndDate(toItem.getDate());
			monthSelected = 1;
			changeDates(fromItem.getDate(), toItem.getDate());
		}
	}
}
