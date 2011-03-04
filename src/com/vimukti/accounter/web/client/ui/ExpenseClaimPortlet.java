package com.vimukti.accounter.web.client.ui;

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
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;

public class ExpenseClaimPortlet extends DashBoardPortlet {

	public double allExpensesAmount = 0.0;
	public double cashExpenseAmount = 0.0;
	public double employeeExpenseAmount = 0.0;
	public double ccExpenseAmount = 0.0;

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

		Label allExpAmtLabel = getAmountLabel(String.valueOf(allExpensesAmount));
		Label cashExpAmtLabel = getAmountLabel(String
				.valueOf(cashExpenseAmount));
		cashExpAmtLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		Label empExpAmtLabel = getAmountLabel(String
				.valueOf(employeeExpenseAmount));
		Label ccExpAmtLabel = getAmountLabel(String.valueOf(ccExpenseAmount));
		ccExpAmtLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);

		fTable.setWidget(0, 0, allExpLabel);
		fTable.setWidget(0, 1, cashExpLabel);
		fTable.setWidget(1, 0, allExpAmtLabel);
		fTable.setWidget(1, 1, cashExpAmtLabel);
		fTable.setWidget(2, 0, empExpLabel);
		fTable.setWidget(2, 1, ccExpLabel);
		fTable.setWidget(3, 0, empExpAmtLabel);
		fTable.setWidget(3, 1, ccExpAmtLabel);

		vPanel.add(addExpenseBtn);
		vPanel.add(fTable);

		body.add(vPanel);
	}

	private void updateAmounts() {

	}

	private Label getLabel(String title) {
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
		super.titleClicked();
	}
}
