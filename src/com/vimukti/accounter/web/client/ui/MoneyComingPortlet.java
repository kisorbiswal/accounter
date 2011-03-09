package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;

public class MoneyComingPortlet extends DashBoardPortlet {

	public double draftInvoiceAmount = 0.0;
	public double overDueInvoiceAmount = 0.0;
	public ClientAccount debitors;

	public Label draftLabel;
	public Label overDueLabel;
	public Label draftAmtLabel;
	public Label overDueAmtLabel;

	public MoneyComingPortlet(String title) {
		super(title);
	}

	@Override
	public String getGoToText() {
		return FinanceApplication.getCompanyMessages().goToAccountReceivable();
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		BankingActionFactory.getAccountRegisterAction().run(debitors, true);
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
		updateDebitorsAccount();

		HorizontalPanel hPanel = new HorizontalPanel();
		FlexTable fTable = new FlexTable();

		Button addReceivableInvoiceBtn = new Button(FinanceApplication
				.getCompanyMessages().addReceivableInvoice());
		addReceivableInvoiceBtn.addStyleName("addButtonPortlet");
		addReceivableInvoiceBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CustomersActionFactory.getNewInvoiceAction().run(null, true);
			}
		});

		draftLabel = getLabel(FinanceApplication.getCompanyMessages()
				.draftInvoices());
		overDueLabel = getLabel(FinanceApplication.getCompanyMessages()
				.overDueInvoices());
		overDueLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);

		updateAmounts();

		draftAmtLabel = getAmountLabel(String.valueOf(draftInvoiceAmount));
		overDueAmtLabel = getAmountLabel(String.valueOf(overDueInvoiceAmount));
		overDueAmtLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);

		fTable.setWidget(0, 0, draftLabel);
		fTable.setWidget(0, 1, overDueLabel);
		fTable.setWidget(1, 0, draftAmtLabel);
		fTable.setWidget(1, 1, overDueAmtLabel);
		fTable.addStyleName("fTablePortlet");

		hPanel.add(addReceivableInvoiceBtn);
		hPanel.add(fTable);

		body.add(hPanel);

		AsyncCallback<List<Double>> callBack = new AsyncCallback<List<Double>>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter
						.showError("Failed to get Account Receivable chart values");
			}

			@Override
			public void onSuccess(List<Double> result) {
				overDueInvoiceAmount = result.get(result.size() - 1);
				result.remove(result.size() - 1);
				draftInvoiceAmount = result.get(result.size() - 1);
				result.remove(result.size() - 1);
				draftAmtLabel.setText(String.valueOf(draftInvoiceAmount));
				overDueAmtLabel.setText(String.valueOf(overDueInvoiceAmount));

				GraphChart chart = new GraphChart(
						GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE, UIUtils
								.getMaxValue(result), 400, 150, result);
				body.add(chart);
				chart.update();
			}
		};
		FinanceApplication.createHomeService().getGraphPointsforAccount(
				GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE, 0, callBack);

	}

	private void updateDebitorsAccount() {
		List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		if (FinanceApplication.getCompany() != null) {
			accounts = FinanceApplication.getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_ASSET);
		}
		for (ClientAccount account : accounts) {
			if (account.getName().equals("Debtors")) {
				debitors = account;
				break;
			}
		}
	}

	Label getLabel(final String title) {
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
						.draftInvoices()))
					CustomersActionFactory.getInvoicesAction().run(null, true);
				else {
					CustomersActionFactory.getInvoicesAction()
							.run(
									null,
									true,
									FinanceApplication.getCustomersMessages()
											.overDue());
				}

			}
		});
		return label;
	}

	Label getAmountLabel(String title) {
		Label label = new Label(title);
		label.addStyleName("amountLabelPortlet");
		return label;
	}

	private void updateAmounts() {

	}

	@Override
	public void titleClicked() {
		BankingActionFactory.getAccountRegisterAction().run(debitors, true);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}
}
