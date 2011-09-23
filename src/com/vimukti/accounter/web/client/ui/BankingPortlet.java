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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

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
		ActionFactory.getChartOfAccountsAction(ClientAccount.TYPE_BANK).run(
				null, true);
	}

	@Override
	public String getGoToText() {
		return Accounter.constants().gotoBanking();
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
		if (getCompany() != null) {
			bankAccounts = getCompany().getActiveBankAccounts(
					ClientAccount.TYPE_BANK);
		}
		Button addAccount = new Button(Accounter.messages().addBankAccount(
				Global.get().Account().trim()));
		addAccount.addStyleName("add account_portlet");
		if (Accounter.getUser().canDoBanking()) {
			body.add(addAccount);
		}

		if (bankAccounts == null || bankAccounts.size() == 0) {
			// for (int i = 0; i < 4; i++) {
			addAccount.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ActionFactory.getNewBankAccountAction().run(null, true);
				}
			});
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					GraphChart chart = new GraphChart();
					body.add(chart.createBankingChart(null));
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
			// final ScrollPanel panel = new ScrollPanel();
			int i = 0;
			for (final ClientAccount account : bankAccounts) {
				HorizontalPanel hPanel = new HorizontalPanel();
				final Label accountLabel = new Label(account.getName());
				// accountLabel.setWidth((account.getName().length() * 5) +
				// "px");
				accountLabel.addStyleName(Accounter.constants().labelBanking());
				accountLabel.addMouseOverHandler(new MouseOverHandler() {

					@Override
					public void onMouseOver(MouseOverEvent event) {
						accountLabel.getElement().getStyle()
								.setCursor(Cursor.POINTER);
						accountLabel.getElement().getStyle()
								.setTextDecoration(TextDecoration.UNDERLINE);
					}
				});
				accountLabel.addMouseOutHandler(new MouseOutHandler() {

					@Override
					public void onMouseOut(MouseOutEvent event) {
						accountLabel.getElement().getStyle()
								.setTextDecoration(TextDecoration.NONE);
					}
				});
				accountLabel.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						ActionFactory.getAccountRegisterAction().run(account,
								true);
					}
				});
				Label amountLabel = new Label(
						DataUtils.getAmountAsString(account.getTotalBalance()));
				// amountLabel.setStyleName("tool-box");
				amountLabel.addStyleName("label-banking");
				// amountLabel.getElement().getStyle().setMarginLeft(295,
				// Unit.PX);
				hPanel.add(accountLabel);
				hPanel.add(amountLabel);
				hPanel.setCellHorizontalAlignment(amountLabel,
						HasHorizontalAlignment.ALIGN_RIGHT);
				hPanel.setWidth("100%");
				body.add(hPanel);
				AccounterAsyncCallback<ArrayList<Double>> callBack = new AccounterAsyncCallback<ArrayList<Double>>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.messages()
								.failedtogetBankaccountchartvalues(
										Global.get().account()));
					}

					@Override
					public void onResultSuccess(final ArrayList<Double> result) {
						Runnable runnable = new Runnable() {

							@Override
							public void run() {
								// if (result.size() != 0) {
								// ClientAccount account = FinanceApplication
								// .getCompany().getAccountByNumber(
								// result.get(
								// result.size() - 1)
								// .longValue());
								// result
								// .remove(result
								// .get(result.size() - 1));
								// }
								for (int i = 0; i < body.getWidgetCount(); i++) {
									if (body.getWidget(i) instanceof HorizontalPanel) {
										HorizontalPanel hPanel = (HorizontalPanel) body
												.getWidget(i);
										if (hPanel.getWidget(0) instanceof Label) {
											if (((Label) hPanel.getWidget(0))
													.getText().equals(
															account.getName())) {
												GraphChart chart = new GraphChart();
												body.insert(
														chart.createBankingChart(result),
														++i);
											}
										}
									}
								}
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
				try {
					Accounter.createHomeService().getGraphPointsforAccount(
							GraphChart.BANK_ACCOUNT_CHART_TYPE,
							Long.valueOf(account.getNumber()), callBack);
					i++;
				} catch (Exception e) {
					System.err.println(e);
				}

			}
			// body.add(panel);
			addAccount.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ActionFactory.getNewBankAccountAction().run(null, true);
				}
			});
		}
	}

	@Override
	public void titleClicked() {
		ActionFactory.getChartOfAccountsAction(ClientAccount.TYPE_BANK).run(
				null, true);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}

}
