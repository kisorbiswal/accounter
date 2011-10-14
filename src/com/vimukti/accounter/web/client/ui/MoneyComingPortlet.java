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
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;

public class MoneyComingPortlet extends DashBoardPortlet {

	public double draftInvoiceAmount = 0.00;
	public double overDueInvoiceAmount = 0.00;
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
		return Accounter.messages().goToAccountReceivable(
				Global.get().Accounts());
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		ActionFactory.getAccountRegisterAction().run(debitors, true);
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
		hPanel.setWidth("100%");

		hPanel.addHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				// TODO Auto-generated method stub

			}
		}, ResizeEvent.getType());
		Button addReceivableInvoiceBtn = new Button(Accounter.constants()
				.addReceivableInvoice());
		addReceivableInvoiceBtn.addStyleName("addButtonPortlet");
		addReceivableInvoiceBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getNewInvoiceAction().run(null, true);
			}
		});

		draftLabel = getLabel(Accounter.constants().invoicesDue());
		overDueLabel = getLabel(Accounter.constants().overDueInvoices());
		overDueLabel.getElement().getStyle().setMarginLeft(10, Unit.PX);

		updateAmounts();

		draftAmtLabel = getAmountLabel(DataUtils
				.getAmountAsString(draftInvoiceAmount));
		overDueAmtLabel = getAmountLabel(DataUtils
				.getAmountAsString(overDueInvoiceAmount));
		overDueAmtLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);

		fTable.setWidget(0, 0, draftLabel);
		fTable.setWidget(0, 1, overDueLabel);
		fTable.setWidget(1, 0, draftAmtLabel);
		fTable.setWidget(1, 1, overDueAmtLabel);
		fTable.addStyleName("fTablePortlet");

		if (Accounter.getUser().canDoInvoiceTransactions()) {
			hPanel.add(addReceivableInvoiceBtn);

		}

		hPanel.add(fTable);

		body.add(hPanel);

		AccounterAsyncCallback<ArrayList<Double>> callBack = new AccounterAsyncCallback<ArrayList<Double>>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.messages()
						.failedtogetAccountReceivablechartvalues(
								Global.get().account()));
			}

			@Override
			public void onResultSuccess(final ArrayList<Double> result) {
				if (result != null && result.size() > 0) {
					overDueInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					overDueAmtLabel
							.setText(amountAsString(overDueInvoiceAmount));
				}
				if (result != null && result.size() > 0) {
					draftInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					draftAmtLabel.setText(amountAsString(draftInvoiceAmount));
				}

				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						GraphChart chart = new GraphChart();
						body.add(chart.createAccountReceivableChart(result));
					}
				};
				VisualizationUtils.loadVisualizationApi(runnable,
						ColumnChart.PACKAGE);

				// GraphChart chart = new GraphChart(
				// GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE, UIUtils
				// .getMaxValue(result), 400, 150, result);
				// body.add(chart);
				// chart.update();
			}
		};
		Accounter.createHomeService().getGraphPointsforAccount(
				GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE, 0, callBack);

	}

	private void updateDebitorsAccount() {
		debitors = getCompany().getAccount(
				getCompany().getAccountsReceivableAccountId());
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
				if (title.equals(Accounter.constants().invoicesDue())) {
					ActionFactory.getInvoicesAction(null).run(null, true);
				} else {
					ActionFactory.getInvoicesAction(InvoiceListView.OVER_DUE)
							.run(null, true);
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
		ActionFactory.getAccountRegisterAction().run(debitors, true);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}
}
