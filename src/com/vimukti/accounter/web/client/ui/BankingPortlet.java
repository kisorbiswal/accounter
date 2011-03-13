package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class BankingPortlet extends DashBoardPortlet {

	List<ClientAccount> bankAccounts = new ArrayList<ClientAccount>();

	public BankingPortlet(String title) {
		super(title);
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		CompanyActionFactory.getChartOfAccountsAction(ClientAccount.TYPE_BANK)
				.run(null, true);
	}

	@Override
	public String getGoToText() {
		return FinanceApplication.getCompanyMessages().gotoBanking();
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
		if (FinanceApplication.getCompany() != null)
			bankAccounts = FinanceApplication.getCompany().getAccounts(
					ClientAccount.TYPE_BANK);
		if (bankAccounts == null || bankAccounts.size() == 0) {
			// for (int i = 0; i < 4; i++) {
			Button addAccount = new Button(FinanceApplication
					.getCompanyMessages().addBankAccount());
			addAccount.addStyleName("addAccountPortlet");
			addAccount.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					BankingActionFactory.getNewBankAccountAction().run(null,
							true);
				}
			});

			body.add(addAccount);
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					GraphChart chart = new GraphChart();
					body.add(chart.createLineChart(null));
				}
			};
			VisualizationUtils
					.loadVisualizationApi(runnable, LineChart.PACKAGE);
			// GraphChart chart = new GraphChart(
			// GraphChart.BANK_ACCOUNT_CHART_TYPE);
			// body.add(chart);
			// chart.update();
			// }
		} else {
			for (final ClientAccount account : bankAccounts) {
				HorizontalPanel hPanel = new HorizontalPanel();
				final Label accountLabel = new Label(account.getName());
				accountLabel.addStyleName("label-banking");
				accountLabel.addMouseOverHandler(new MouseOverHandler() {

					@Override
					public void onMouseOver(MouseOverEvent event) {
						accountLabel.getElement().getStyle().setCursor(
								Cursor.POINTER);
						accountLabel.getElement().getStyle().setTextDecoration(
								TextDecoration.UNDERLINE);
					}
				});
				accountLabel.addMouseOutHandler(new MouseOutHandler() {

					@Override
					public void onMouseOut(MouseOutEvent event) {
						accountLabel.getElement().getStyle().setTextDecoration(
								TextDecoration.NONE);
					}
				});
				accountLabel.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						BankingActionFactory.getAccountRegisterAction().run(
								account, true);
					}
				});
				Label amountLabel = new Label(String.valueOf(account
						.getTotalBalance()));
				amountLabel.setStyleName("tool-box");
				amountLabel.addStyleName("label-banking");
				hPanel.add(accountLabel);
				hPanel.add(amountLabel);
				body.add(hPanel);
				AsyncCallback<List<Double>> callBack = new AsyncCallback<List<Double>>() {

					@Override
					public void onFailure(Throwable caught) {
						Accounter
								.showError("Failed to get Bank account chart values");
					}

					@Override
					public void onSuccess(final List<Double> result) {
						Runnable runnable = new Runnable() {

							@Override
							public void run() {
								GraphChart chart = new GraphChart();
								body.add(chart.createLineChart(result));
							}
						};
						VisualizationUtils.loadVisualizationApi(runnable,
								LineChart.PACKAGE);
						// GraphChart chart = new GraphChart(
						// GraphChart.BANK_ACCOUNT_CHART_TYPE, UIUtils
						// .getMaxValue(result), 400, 150, result);
						// body.add(chart);
						// chart.update();
					}
				};
				FinanceApplication.createHomeService()
						.getGraphPointsforAccount(
								GraphChart.BANK_ACCOUNT_CHART_TYPE,
								Long.valueOf(account.getNumber()), callBack);
			}
		}
	}

	@Override
	public void titleClicked() {
		CompanyActionFactory.getChartOfAccountsAction(ClientAccount.TYPE_BANK)
				.run(null, true);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}

}
