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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
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
		VendorsActionFactory.getExpensesAction().run(null, true);
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

		Button addExpenseBtn = new Button(FinanceApplication
				.getCompanyMessages().addExpenses());
		addExpenseBtn.addStyleName("addAccountPortlet");
		addExpenseBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
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
		empExpAmtLabel = getAmountLabel(DataUtils.getAmountAsString(employeeExpenseAmount));
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
			if (addExpenseBtn.isEnabled()) {
				addExpenseBtn.getElement().getParentElement().setClassName(
						"ibutton");
				ThemesUtil.addDivToButton(addExpenseBtn, FinanceApplication
						.getThemeImages().button_right_blue_image(),
						"ibutton-right-image");
			}
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
					VendorsActionFactory.getExpensesAction().run(null, true,
							FinanceApplication.getVendorsMessages().cash());
				else if (title.equals(FinanceApplication.getCompanyMessages()
						.creditCardExpenses()))
					VendorsActionFactory.getExpensesAction().run(
							null,
							true,
							FinanceApplication.getVendorsMessages()
									.creditCard());
				else if (title.equals(FinanceApplication.getCompanyMessages()
						.employeeExpenses()))
					VendorsActionFactory.getExpensesAction().run(null, true,
							FinanceApplication.getVendorsMessages().employee());
				else if (title.equals(FinanceApplication.getCompanyMessages()
						.allExpenses()))
					VendorsActionFactory.getExpensesAction().run(null, true);
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
		VendorsActionFactory.getExpensesAction().run(null, true);
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
