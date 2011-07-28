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
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;

public class MoneyGoingPortlet extends DashBoardPortlet {

	public double draftInvoiceAmount = 0.00;
	public double overDueInvoiceAmount = 0.00;
	public ClientAccount creditors;

	public Label draftLabel;
	public Label overDueLabel;
	public Label draftAmtLabel;
	public Label overDueAmtLabel;

	public MoneyGoingPortlet(String title) {
		super(title);
	}

	@Override
	public String getGoToText() {
		return Accounter.constants().goToAccountsPayable();
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		HistoryTokenUtils.setPresentToken(
				BankingActionFactory.getAccountRegisterAction(), creditors);
		BankingActionFactory.getAccountRegisterAction().run(creditors, true);
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
		updateCreditorsAccount();

		HorizontalPanel hPanel = new HorizontalPanel();
		FlexTable fTable = new FlexTable();

		AccounterButton addPayableInvoiceBtn = new AccounterButton(Accounter
				.constants().addPayableInvoice());
		addPayableInvoiceBtn.addStyleName("addButtonPortlet");
		addPayableInvoiceBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HistoryTokenUtils.setPresentToken(
						VendorsActionFactory.getEnterBillsAction(), null);
				VendorsActionFactory.getEnterBillsAction().run(null, true);
			}
		});

		draftLabel = getLabel(Accounter.constants().draftInvoices());
		overDueLabel = getLabel(Accounter.constants()
				.overDueInvoices());
		overDueLabel.getElement().getStyle().setMarginLeft(10, Unit.PX);

		updateAmounts();

		draftAmtLabel = getAmountLabel(String.valueOf(draftInvoiceAmount));
		overDueAmtLabel = getAmountLabel(String.valueOf(overDueInvoiceAmount));
		overDueAmtLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);

		fTable.setWidget(0, 0, draftLabel);
		fTable.setWidget(0, 1, overDueLabel);
		fTable.setWidget(1, 0, draftAmtLabel);
		fTable.setWidget(1, 1, overDueAmtLabel);
		fTable.addStyleName("fTablePortlet");

		if (Accounter.getUser().canDoInvoiceTransactions()) {
			hPanel.add(addPayableInvoiceBtn);
			addPayableInvoiceBtn.enabledButton();
		}
		hPanel.add(fTable);

		body.add(hPanel);

		AsyncCallback<List<Double>> callBack = new AsyncCallback<List<Double>>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter
							.showError("Failed to get money going portlet values");
				}
			}

			@Override
			public void onSuccess(final List<Double> result) {
				if (result != null && result.size() > 0) {
					overDueInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					overDueAmtLabel.setText(DataUtils
							.getAmountAsString(overDueInvoiceAmount));
				}
				if (result != null && result.size() > 0) {
					draftInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					draftAmtLabel.setText(DataUtils
							.getAmountAsString(draftInvoiceAmount));
				}

				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						GraphChart chart = new GraphChart();
						body.add(chart.createAccountPayableChart(result));
					}
				};
				VisualizationUtils.loadVisualizationApi(runnable,
						AnnotatedTimeLine.PACKAGE);

				// ScrollPanel panel = new ScrollPanel();
				// GraphChart chart = new GraphChart(
				// GraphChart.ACCOUNTS_PAYABLE_CHART_TYPE, UIUtils
				// .getMaxValue(result), 1200, 150, result);
				// // chart.setChartSize(1200, 150);
				// panel.add(chart);
				// panel.setSize("456px", "185px");
				// panel.getElement().getStyle().setPaddingTop(5, Unit.PX);
				// body.add(panel);
				// chart.update();
			}
		};
		Accounter.createHomeService().getGraphPointsforAccount(
				GraphChart.ACCOUNTS_PAYABLE_CHART_TYPE, 0, callBack);
	}

	private void updateCreditorsAccount() {
		List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		if (Accounter.getCompany() != null) {
			accounts = Accounter.getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY);
		}
		for (ClientAccount account : accounts) {
			if (account.getName().equals("Creditors")) {
				creditors = account;
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
				if (title
						.equals(Accounter.constants().draftInvoices())) {
					VendorsActionFactory.getBillsAction().run(null, true,
							Accounter.constants().open());
				} else {
					VendorsActionFactory.getBillsAction().run(null, true,
							Accounter.constants().overDue());
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
		HistoryTokenUtils.setPresentToken(
				BankingActionFactory.getAccountRegisterAction(), creditors);
		BankingActionFactory.getAccountRegisterAction().run(creditors, true);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}
}
