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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class MoneyGoingPortlet extends Portlet {

	public double draftInvoiceAmount = 0.00;
	public double overDueInvoiceAmount = 0.00;
	public ClientAccount creditors;

	public Label draftLabel;
	public Label overDueLabel;
	public Label draftAmtLabel;
	public Label overDueAmtLabel;
	private boolean isCreatingBody;

	public MoneyGoingPortlet(ClientPortletConfiguration pc) {
		super(pc, messages.moneyGoingOut(), messages.goToAccountsPayable());
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		ActionFactory.getAccountRegisterAction().run(creditors, true);
	}

	@Override
	public void createBody() {
		if (isCreatingBody) {
			return;
		}
		isCreatingBody = true;
		updateCreditorsAccount();

		HorizontalPanel hPanel = new HorizontalPanel();
		FlexTable fTable = new FlexTable();

		Button addPayableInvoiceBtn = new Button(messages.addBill());
		addPayableInvoiceBtn.addStyleName("addButtonPortlet");
		addPayableInvoiceBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getEnterBillsAction().run(null, true);
			}
		});

		draftLabel = getLabel(messages.billsDue());
		overDueLabel = getLabel(messages.overDueBills());
		overDueLabel.getElement().getStyle().setMarginLeft(10, Unit.PX);

		updateAmounts();

		draftAmtLabel = getAmountLabel(getPrimaryCurrencySymbol() + " "
				+ DataUtils.getAmountAsString(draftInvoiceAmount));
		overDueAmtLabel = getAmountLabel(getPrimaryCurrencySymbol() + " "
				+ DataUtils.getAmountAsString(overDueInvoiceAmount));
		overDueAmtLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);

		fTable.setWidget(0, 0, draftLabel);
		fTable.setWidget(0, 1, overDueLabel);
		fTable.setWidget(1, 0, draftAmtLabel);
		fTable.setWidget(1, 1, overDueAmtLabel);
		fTable.addStyleName("fTablePortlet");

		if (Accounter.getUser().canDoInvoiceTransactions()
				&& getCompany().getPreferences().isKeepTrackofBills()) {
			hPanel.add(addPayableInvoiceBtn);
		} else {
			hPanel.addStyleName("fTable-noButton");
		}
		hPanel.add(fTable);

		body.add(hPanel);

		AccounterAsyncCallback<ArrayList<Double>> callBack = new AccounterAsyncCallback<ArrayList<Double>>() {

			@Override
			public void onException(AccounterException caught) {
				isCreatingBody = false;
				Accounter.showError(messages
						.failedtogetmoneygoingportletvalues());
			}

			@Override
			public void onResultSuccess(final ArrayList<Double> result) {
				if (result != null && result.size() > 0) {
					overDueInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					overDueAmtLabel.setText(getPrimaryCurrencySymbol() + " "
							+ amountAsString(overDueInvoiceAmount));
				}
				if (result != null && result.size() > 0) {
					draftInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					draftAmtLabel.setText(getPrimaryCurrencySymbol() + " "
							+ amountAsString(draftInvoiceAmount));
				}

				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						GraphChart chart = new GraphChart();
						body.add(chart.createAccountPayableChart(result));
						isCreatingBody = false;
					}
				};
				VisualizationUtils.loadVisualizationApi(runnable,
						AnnotatedTimeLine.PACKAGE);
			}
		};
		Accounter.createHomeService().getGraphPointsforAccount(
				GraphChart.ACCOUNTS_PAYABLE_CHART_TYPE, 0, callBack);
	}

	private void updateCreditorsAccount() {
		creditors = getCompany().getAccount(
				getCompany().getAccountsPayableAccount());
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
				if (title.equals(messages.billsDue())) {
					ActionFactory.getBillsAction().run(null, true,
							messages.open());
				} else {
					ActionFactory.getBillsAction().run(null, true,
							messages.overDue());
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
	public void refreshWidget() {

	}

	private void refreshPortlet() {
		this.body.clear();
		createBody();
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		refreshPortlet();
	}
}
