package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.reports.ExpensePortletToolBar;
import com.vimukti.accounter.web.client.ui.reports.PortletToolBar;

public class ExpenseClaimPortlet extends Portlet {

	private PortletToolBar toolBar;
	private VerticalPanel graphPanel;

	public double allExpensesAmount = 0.00;
	public double cashExpenseAmount = 0.00;
	public double employeeExpenseAmount = 0.0;
	public double ccExpenseAmount = 0.00;

	public Label allExpAmtLabel;
	public Label cashExpAmtLabel;
	public Label empExpAmtLabel;
	public Label ccExpAmtLabel;

	public ExpenseClaimPortlet(ClientPortletConfiguration pc) {
		super(pc, messages.expenses(), "", "100%");
		this.setConfiguration(pc);
	}

	@Override
	public void helpClicked() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void goToClicked() {
		ActionFactory.getExpensesAction(null).run(null, true);
	}

	@Override
	public void createBody() {
		VerticalPanel vPanel = new VerticalPanel();
		FlexTable fTable = new FlexTable();

		Button addExpenseBtn = new Button(Accounter.messages().addExpenses());
		addExpenseBtn.addStyleName("addAccountPortlet");
		addExpenseBtn.addClickHandler(new ClickHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getRecordExpensesAction().run(null, true);
			}
		});

		Anchor allExpLabel = new Anchor(messages.allExpenses());
		allExpLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ExpenseList expenseList = new ExpenseList();
				expenseList.setStartDate(toolBar.getStartDate());
				expenseList.setEndDate(toolBar.getEndDate());
				expenseList.setDateRange(toolBar.getSelectedDateRange());
				UIUtils.runAction(expenseList,
						ActionFactory.getExpenseReportAction());
			}
		});

		Anchor cashExpLabel = new Anchor(messages.cashExpenses());
		cashExpLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ExpenseList expenseList = new ExpenseList();
				expenseList.setStartDate(toolBar.getStartDate());
				expenseList.setEndDate(toolBar.getEndDate());
				expenseList.setDateRange(toolBar.getSelectedDateRange());
				expenseList
						.setTransactionType(ClientTransaction.TYPE_CASH_EXPENSE);
				UIUtils.runAction(expenseList,
						ActionFactory.getExpenseReportAction());
			}
		});
		cashExpLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		// Label empExpLabel =
		// getLabel(Accounter.messages().employeeExpenses());
		Anchor ccExpLabel = new Anchor(messages.creditCardExpenses());
		ccExpLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ExpenseList expenseList = new ExpenseList();
				expenseList.setStartDate(toolBar.getStartDate());
				expenseList.setEndDate(toolBar.getEndDate());
				expenseList.setDateRange(toolBar.getSelectedDateRange());
				expenseList
						.setTransactionType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
				UIUtils.runAction(expenseList,
						ActionFactory.getExpenseReportAction());

			}
		});
		ccExpLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);

		Label empExpLabel = new Label();

		allExpAmtLabel = getAmountLabel(getPrimaryCurrencySymbol() + " "
				+ amountAsString(allExpensesAmount));
		cashExpAmtLabel = getAmountLabel(getPrimaryCurrencySymbol() + " "
				+ amountAsString(cashExpenseAmount));
		cashExpAmtLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		empExpAmtLabel = getAmountLabel(getPrimaryCurrencySymbol() + " "
				+ amountAsString(employeeExpenseAmount));
		ccExpAmtLabel = getAmountLabel(getPrimaryCurrencySymbol() + " "
				+ amountAsString(ccExpenseAmount));
		ccExpAmtLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		fTable.addStyleName("expense_label_tabel");
		fTable.setWidget(0, 0, allExpLabel);
		fTable.setWidget(1, 0, allExpAmtLabel);

		fTable.setWidget(0, 1, cashExpLabel);
		fTable.setWidget(1, 1, cashExpAmtLabel);

		fTable.setWidget(2, 1, ccExpLabel);
		fTable.setWidget(3, 1, ccExpAmtLabel);

		// These should be enabled when user select to track employee expenses.
		if (ClientCompanyPreferences.get().isHaveEpmloyees()
				&& ClientCompanyPreferences.get().isTrackEmployeeExpenses()) {
			fTable.setWidget(2, 0, empExpLabel);
			fTable.setWidget(3, 0, empExpAmtLabel);
		}
		HorizontalPanel topPanel = new HorizontalPanel();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			topPanel.add(addExpenseBtn);
		}
		vPanel.add(fTable);
		toolBarInitilization();
		graphPanel = new VerticalPanel();
		topPanel.add(toolBar);
		this.body.add(topPanel);
		this.body.add(graphPanel);
		this.body.add(vPanel);
	}

	Label getAmountLabel(String title) {
		Label label = new Label(title);
		label.addStyleName("amountLabelPortlet");
		label.getElement().getStyle().setMarginLeft(10, Unit.PX);
		return label;
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}

	public void updateAmountLabels() {
		cashExpAmtLabel.setText(getPrimaryCurrencySymbol() + " "
				+ amountAsString(cashExpenseAmount));
		ccExpAmtLabel.setText(getPrimaryCurrencySymbol() + " "
				+ amountAsString(ccExpenseAmount));
		empExpAmtLabel.setText(getPrimaryCurrencySymbol() + " "
				+ amountAsString(employeeExpenseAmount));

		allExpAmtLabel.setText(getPrimaryCurrencySymbol() + " "
				+ amountAsString(allExpensesAmount));
	}

	private void toolBarInitilization() {
		toolBar = new ExpensePortletToolBar() {
			@Override
			protected void initData() {
				if (ExpenseClaimPortlet.this.getConfiguration().getPortletKey() != null) {
					setDefaultDateRange(ExpenseClaimPortlet.this
							.getConfiguration().getPortletKey());
				} else {
					setDefaultDateRange(messages.financialYearToDate());
				}
			}

			@Override
			protected void refreshPortletData(String selectItem) {
				ExpenseClaimPortlet.this.clearGraph();
				dateRangeItemCombo.setSelected(selectItem);
				ExpenseClaimPortlet.this.getConfiguration().setPortletKey(
						selectItem);
				dateRangeChanged(selectItem);
				ExpenseClaimPortlet.this.updateData(startDate.getDate(),
						endDate.getDate());
			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				ExpenseClaimPortlet.this.updateData(startDate.getDate(),
						endDate.getDate());
			}
		};
	}

	protected void refreshPortletData(String selectItem) {

	}

	public void updateData(long startDate, long endDate) {

		AccounterAsyncCallback<ExpensePortletData> callback = new AccounterAsyncCallback<ExpensePortletData>() {

			@Override
			public void onResultSuccess(ExpensePortletData result) {
				if (result != null) {
					updateLabelsData(result);
					updateGraphData(result);
				} else {
					Label label = new Label(messages.noRecordsToShow());
					graphPanel.add(label);
					label.addStyleName("no_records_label");
					graphPanel.setCellHorizontalAlignment(label,
							HasAlignment.ALIGN_CENTER);
				}
				completeInitialization();
			}

			@Override
			public void onException(AccounterException exception) {
				completeInitialization();
			}
		};

		Accounter.createHomeService().getAccountsAndValues(startDate, endDate,
				callback);
	}

	protected void updateLabelsData(ExpensePortletData result) {
		cashExpenseAmount = result.getCashExpenseTotal();
		ccExpenseAmount = result.getCreditCardExpensesTotal();
		allExpensesAmount = result.getAllExpensesTotal();
		updateAmountLabels();
	}

	protected void updateGraphData(ExpensePortletData result) {
		final List<String> accountsNames = new ArrayList<String>();
		final List<Double> accountBalances = new ArrayList<Double>();
		Iterator<AccountDetail> iterator = result.getAccountDetails()
				.iterator();
		int i = 0;
		double amount = 0;
		while (iterator.hasNext()) {
			AccountDetail accountDetail = iterator.next();
			Double accountBalance = accountDetail.getAmount();
			if (i < 11) {
				accountsNames.add(accountDetail.getName());
				accountBalances.add(accountBalance);
			} else {
				amount = amount + accountBalance;
				if (i == result.getAccountDetails().size() - 2) {
					accountsNames.add(messages.other());
					accountBalances.add(amount);
				}
			}
			i++;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				GraphChart chart = new GraphChart();
				chart.expenseAccountNames = accountsNames;
				graphPanel
						.add(chart.createAccountExpenseChart(accountBalances));
			}
		};
		VisualizationUtils.loadVisualizationApi(runnable,
				AnnotatedTimeLine.PACKAGE);
	}

	public void clearGraph() {
		graphPanel.clear();
	}

	@Override
	public void refreshClicked() {
		// TODO Auto-generated method stub

	}

}
