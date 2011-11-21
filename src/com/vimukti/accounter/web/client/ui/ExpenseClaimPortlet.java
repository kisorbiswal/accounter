package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

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
		return Accounter.messages().goToExpenseCliams();
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		ActionFactory.getExpensesAction(null).run(null, true);
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

		Button addExpenseBtn = new Button(Accounter.messages().addExpenses());
		addExpenseBtn.addStyleName("addAccountPortlet");
		addExpenseBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getRecordExpensesAction().run(null, true);
			}
		});

		Label allExpLabel = getLabel(Accounter.messages().allExpenses());
		Label cashExpLabel = getLabel(Accounter.messages().cashExpenses());
		cashExpLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		Label empExpLabel = getLabel(Accounter.messages().employeeExpenses());
		Label ccExpLabel = getLabel(Accounter.messages().creditCardExpenses());
		ccExpLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);

		updateAmounts();

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

		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vPanel.add(addExpenseBtn);
		}
		vPanel.add(fTable);

		body.add(vPanel);
	}

	private void updateAmounts() {
		AccounterAsyncCallback<ArrayList<Double>> callBack = new AccounterAsyncCallback<ArrayList<Double>>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.messages()
						.failedtogetExpensetotals());
			}

			@Override
			public void onResultSuccess(ArrayList<Double> result) {
				if (result != null && result.size() != 0) {
					cashExpenseAmount = result.get(0);
					ccExpenseAmount = result.get(1);
					employeeExpenseAmount = result.get(2);
					allExpensesAmount = result.get(3);
					updateAmountLabels();
				}
			}
		};
		Accounter.createHomeService().getGraphPointsforAccount(4, 0, callBack);
	}

	private Label getLabel(final String title) {
		final Label label = new Label(title);

		label.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				label.getElement().getStyle().setCursor(Cursor.POINTER);
				label.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		label.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				label.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				label.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);

				if (title.equals(Accounter.messages().cashExpenses())) {
					ActionFactory.getExpensesAction(
							Accounter.messages().cash()).run(null, true);
				} else if (title.equals(Accounter.messages()
						.creditCardExpenses())) {
					ActionFactory.getExpensesAction(
							Accounter.messages().creditCard()).run(null, true);
				} else if (title.equals(Accounter.messages()
						.employeeExpenses())) {
					ActionFactory.getExpensesAction(
							Accounter.messages().employee()).run(null, true);
				} else if (title.equals(Accounter.messages().allExpenses())) {
					ActionFactory.getExpensesAction(null).run(null, true);
				}
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
		ActionFactory.getExpensesAction(null).run(null, true);
	}

	@Override
	public void refreshWidget() {
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

}
