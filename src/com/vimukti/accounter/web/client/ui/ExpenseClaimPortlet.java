package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;

public class ExpenseClaimPortlet extends DashBoardPortlet {

	public double allExpensesAmount = 0.00;
	public double cashExpenseAmount = 0.00;
	public double employeeExpenseAmount = 0.0;
	public double ccExpenseAmount = 0.00;

	public Label allExpAmtLabel;
	public Label cashExpAmtLabel;
	public Label empExpAmtLabel;
	public Label ccExpAmtLabel;

	public ExpenseClaimPortlet(String title) {
		super(title);
	}

	@Override
	public String getGoToText() {
		return FinanceApplication.getCompanyMessages().goToExpenseCliams();
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		HistoryTokenUtils.setPresentToken(VendorsActionFactory
				.getExpensesAction(null), null);
		VendorsActionFactory.getExpensesAction(null).run(null, true);
	}

	@Override
	public Cursor getTitleCursor() {
		return Cursor.POINTER;
	}

	@Override
	public TextDecoration getTitleDecoration() {
		return TextDecoration.UNDERLINE;
	}

	@Override
	public void createBody() {
		VerticalPanel vPanel = new VerticalPanel();
		FlexTable fTable = new FlexTable();

		AccounterButton addExpenseBtn = new AccounterButton(FinanceApplication
				.getCompanyMessages().addExpenses());
		addExpenseBtn.addStyleName("addAccountPortlet");
		addExpenseBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HistoryTokenUtils.setPresentToken(VendorsActionFactory
						.getRecordExpensesAction(), null);
				VendorsActionFactory.getRecordExpensesAction().run(null, true);
			}
		});

		Label allExpLabel = getLabel(FinanceApplication.getCompanyMessages()
				.allExpenses());
		Label cashExpLabel = getLabel(FinanceApplication.getCompanyMessages()
				.cashExpenses());
		cashExpLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		Label empExpLabel = getLabel(FinanceApplication.getCompanyMessages()
				.employeeExpenses());
		Label ccExpLabel = getLabel(FinanceApplication.getCompanyMessages()
				.creditCardExpenses());
		ccExpLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);

		updateAmounts();

		allExpAmtLabel = getAmountLabel(DataUtils
				.getAmountAsString(allExpensesAmount));
		cashExpAmtLabel = getAmountLabel(DataUtils
				.getAmountAsString(cashExpenseAmount));
		cashExpAmtLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		empExpAmtLabel = getAmountLabel(DataUtils
				.getAmountAsString(employeeExpenseAmount));
		ccExpAmtLabel = getAmountLabel(DataUtils
				.getAmountAsString(ccExpenseAmount));
		ccExpAmtLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);

		fTable.setWidget(0, 0, allExpLabel);
		fTable.setWidget(0, 1, cashExpLabel);
		fTable.setWidget(1, 0, allExpAmtLabel);
		fTable.setWidget(1, 1, cashExpAmtLabel);
		fTable.setWidget(2, 0, empExpLabel);
		fTable.setWidget(2, 1, ccExpLabel);
		fTable.setWidget(3, 0, empExpAmtLabel);
		fTable.setWidget(3, 1, ccExpAmtLabel);

		if (FinanceApplication.getUser().canDoInvoiceTransactions()) {
			vPanel.add(addExpenseBtn);
			addExpenseBtn.enabledButton();
		}
		vPanel.add(fTable);

		body.add(vPanel);
	}

	private void updateAmounts() {
		AsyncCallback<List<Double>> callBack = new AsyncCallback<List<Double>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Accounter.showError("Failed to get Expense totals");
			}

			@Override
			public void onSuccess(List<Double> result) {
				if (result != null && result.size() != 0) {
					cashExpenseAmount = result.get(0);
					ccExpenseAmount = result.get(1);
					employeeExpenseAmount = result.get(2);
					allExpensesAmount = result.get(3);
					updateAmountLabels();
				}
			}
		};
		FinanceApplication.createHomeService().getGraphPointsforAccount(4, 0,
				callBack);
	}

	private Label getLabel(final String title) {
		final Label label = new Label(title);
		label.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				label.getElement().getStyle().setCursor(Cursor.POINTER);
				label.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		label.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				label.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
			}
		});
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				label.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
				if (title.equals(FinanceApplication.getCompanyMessages()
						.cashExpenses()))
					History.newItem("cashExpenses");
				// VendorsActionFactory.getExpensesAction(
				// FinanceApplication.getVendorsMessages().cash())
				// .run(null, true);
				else if (title.equals(FinanceApplication.getCompanyMessages()
						.creditCardExpenses()))
					History.newItem("creditCardExpenses");
				// VendorsActionFactory.getExpensesAction(
				// FinanceApplication.getVendorsMessages()
				// .creditCard()).run(null, true);
				else if (title.equals(FinanceApplication.getCompanyMessages()
						.employeeExpenses()))
					History.newItem("employeeExpenses");
				// VendorsActionFactory.getExpensesAction(
				// FinanceApplication.getVendorsMessages().employee())
				// .run(null, true);
				else if (title.equals(FinanceApplication.getCompanyMessages()
						.allExpenses()))
					History.newItem(VendorsActionFactory
							.getExpensesAction(null).getHistoryToken());
				// VendorsActionFactory.getExpensesAction(null)
				// .run(null, true);
			}
		});
		label.getElement().getStyle().setMarginLeft(10, Unit.PX);
		label.getElement().getStyle().setMarginTop(10, Unit.PX);
		return label;
	}

	Label getAmountLabel(String title) {
		Label label = new Label(title);
		label.addStyleName("amountLabelPortlet");
		label.getElement().getStyle().setMarginLeft(10, Unit.PX);
		return label;
	}

	@Override
	public void titleClicked() {
		HistoryTokenUtils.setPresentToken(VendorsActionFactory
				.getExpensesAction(null), null);
		VendorsActionFactory.getExpensesAction(null).run(null, true);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}

	public void updateAmountLabels() {
		cashExpAmtLabel.setText(DataUtils.getAmountAsString(cashExpenseAmount));
		ccExpAmtLabel.setText(DataUtils.getAmountAsString(ccExpenseAmount));
		empExpAmtLabel.setText(DataUtils
				.getAmountAsString(employeeExpenseAmount));

		allExpAmtLabel.setText(DataUtils.getAmountAsString(allExpensesAmount));
	}

}
