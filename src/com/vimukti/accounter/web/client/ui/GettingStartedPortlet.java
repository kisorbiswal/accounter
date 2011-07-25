package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.settings.SettingsActionFactory;

public class GettingStartedPortlet extends DashBoardPortlet {
	private VerticalPanel mainPanel;
	private HTML minHtml, allHtml;
	private Label moreLabel;

	Anchor accountReceivable, accountPayable, banking, expences, customer,
			vendor, inviteUser, createBankAcc, financeCategories;

	public GettingStartedPortlet(String title) {
		super(title);
	}

	@Override
	public void createBody() {
		mainPanel = new VerticalPanel();
		// <li> <a href=''><font color='green'>Watch the Getting Started
		// tour.</font></a>
		// <li>Set up repeating invoices for those invoices you regularly send
		// or receive.
		// <li><a href=''><font color='green'>Create a budget</font></a> for
		// your organisation so that you can compare with actual expenditure
		// throughout the year.
		accountReceivable = getAnchor(Accounter.getCompanyMessages()
				.accountReceivable());
		accountPayable = getAnchor(Accounter.getCompanyMessages()
				.accountPayable());
		banking = getAnchor(Accounter.getCompanyMessages()
				.bankingTransactions());
		expences = getAnchor(Accounter.getCompanyMessages().expenseClaims());
		customer = getAnchor("customers");
		vendor = getAnchor("vendors");
		inviteUser = getAnchor("Invite Other User");
		createBankAcc = getAnchor("Create any additional bank accounts");
		financeCategories = getAnchor("finance categories");
		// minHtml = new HTML(
		// "<p>Now you are ready to start using Accounter on a regular basis to record and report on normal business transcations. There is <a href='http://help.accounter.com'><font color='green'>full online help</font></a> and tips on each screen in Accounter if you need it. It's really up to you what you do next.</p><ul><li>Add "
		// + accountReceivable
		// + " and "
		// + accountPayable
		// + " invoices, "
		// + banking
		// + " and "
		// + expences
		// +
		// ".<li>Add to <a href=''><font color='green'>customers</font></a> or <a href=''><font color='green'>vendors</font></a> the people you regularly transact with.</ul>");
		// VerticalPanel vPanel = new VerticalPanel();
		// HorizontalPanel hPanel1 = new HorizontalPanel();
		// HorizontalPanel hPanel2 = new HorizontalPanel();
		// HorizontalPanel hPanel3 = new HorizontalPanel();
		// HorizontalPanel hPanel4 = new HorizontalPanel();
		// HorizontalPanel hPanel5 = new HorizontalPanel();
		allHtml = new HTML(
				"<p>Now you are ready to start using Accounter on a regular basis to record and report on normal business transcations. There is <a href=''><font color='green'>full online help</font></a> and tips on each screen in Accounter if you need it. It's really up to you what you do next.</p><ul><li>Add "
						+ accountReceivable
						+ " and "
						+ accountPayable
						+ " invoices, "
						+ banking
						+ " and "
						+ expences
						+ ".<li>Add to "
						+ customer
						+ " or "
						+ vendor
						+ " the people you regularly transact with.<li>"
						+ inviteUser
						+ " such as your accountant or financial adviser to access your organisation.<li>"
						+ createBankAcc
						+ " you want to use in Accounter.<li>You can see "
						+ financeCategories
						+ " to check which accounts are effected by your transactions.</ul>");
		// hPanel1.add(new HTML("<li>Add "));
		// if (FinanceApplication.getUser().canDoInvoiceTransactions()) {
		// hPanel1.add(accountReceivable);
		// hPanel1.add(new HTML(" and "));
		// hPanel1.add(accountPayable);
		// hPanel1.add(new HTML(" invoices, "));
		// }
		// hPanel1.add(banking);
		// hPanel1.add(new HTML(" and "));
		// hPanel1.add(expences);
		// hPanel1.setSpacing(5);
		//
		// hPanel2.add(new HTML("<li>Add to "));
		// hPanel2.add(customer);
		// hPanel2.add(new HTML(" or "));
		// hPanel2.add(vendor);
		// hPanel2.add(new HTML(" the people you regularly transact with."));
		// hPanel2.setSpacing(5);
		//
		// hPanel3.add(new HTML("<li>You can"));
		// hPanel3.add(inviteUser);
		// hPanel3
		// .add(new HTML(
		// "such as your accountant or financial adviser to access your organisation."));
		// hPanel3.setSpacing(5);
		//
		// hPanel4.add(new HTML("<li>You can"));
		// hPanel4.add(createBankAcc);
		// hPanel4.add(new HTML("to use in Accounter."));
		// hPanel4.setSpacing(5);
		//
		// hPanel5.add(new HTML("<li>You can see "));
		// hPanel5.add(financeCategories);
		// hPanel5.add(new HTML(
		// "to check which accounts are effected by your transactions."));
		// hPanel5.setSpacing(5);

		// moreLabel = new Label("More");
		// moreLabel.addStyleName("lesslabel");
		// moreLabel.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// if (moreLabel.getText().equals("Less")) {
		// moreLabel.setText("More");
		// if (allHtml.isAttached()) {
		// mainPanel.remove(allHtml);
		// }
		// mainPanel.remove(moreLabel);
		// mainPanel.add(minHtml);
		// mainPanel.add(moreLabel);
		//
		// } else {
		// moreLabel.setText("Less");
		// if (minHtml.isAttached()) {
		// mainPanel.remove(minHtml);
		// }
		// mainPanel.remove(moreLabel);
		// mainPanel.add(allHtml);
		// mainPanel.add(moreLabel);
		// }
		//
		// }
		// });
		// vPanel.add(allHtml);
		// if (FinanceApplication.getUser().canSeeInvoiceTransactions())
		// vPanel.add(hPanel1);
		// vPanel.add(hPanel2);
		// if (FinanceApplication.getUser().isCanDoUserManagement())
		// vPanel.add(hPanel3);
		// if (FinanceApplication.getUser().canDoBanking())
		// vPanel.add(hPanel4);
		// if (FinanceApplication.getUser().canSeeInvoiceTransactions())
		// vPanel.add(hPanel5);
		// mainPanel.add(paraHtml);
		// mainPanel.add(minHtml);
		mainPanel.add(allHtml);
		// mainPanel.add(accountReceivable);
		// mainPanel.add(moreLabel);
		body.add(mainPanel);

	}

	@Override
	public String getGoToText() {
		return "Hide Getting Started";
	}

	@Override
	public void goToClicked() {
		// TODO Auto-generated method stub
		this.setVisible(false);
		// Header.changeHelpBarContent("Show Getting Started");
	}

	@Override
	public void helpClicked() {

	}

	public Anchor getAnchor(final String title) {
		Anchor link = new Anchor(title);
		// link.getElement().getStyle().setCursor(Cursor.POINTER);
		link.getElement().getStyle().setColor("green");
		link.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (title.equals(Accounter.getCompanyMessages()
						.accountReceivable())) {
					if (Accounter.getUser().canDoInvoiceTransactions())
						CustomersActionFactory.getNewInvoiceAction().run(null,
								true);
				} else if (title.equals(Accounter.getCompanyMessages()
						.accountPayable())) {
					if (Accounter.getUser().canDoInvoiceTransactions())
						VendorsActionFactory.getEnterBillsAction().run(null,
								true);
				} else if (title.equals(Accounter.getCompanyMessages()
						.bankingTransactions()))
					CompanyActionFactory.getChartOfAccountsAction(
							ClientAccount.TYPE_BANK).run(null, true);
				else if (title.equals(Accounter.getCompanyMessages()
						.expenseClaims())) {
					if (Accounter.getUser().canDoInvoiceTransactions())
						VendorsActionFactory.getExpensesAction(null).run(null,
								true);
				} else if (title.equals("customers"))
					CustomersActionFactory.getNewCustomerAction().run(null,
							true);
				else if (title.equals("vendors"))
					VendorsActionFactory.getNewVendorAction().run(null, true);
				else if (title.equals("Invite Other User")) {
					if (Accounter.getUser().isCanDoUserManagement())
						SettingsActionFactory.getInviteUserAction().run(null,
								true);
				} else if (title.equals("Create any additional bank accounts")) {
					if (Accounter.getUser().canDoBanking())
						BankingActionFactory.getNewBankAccountAction().run(
								null, true);
				} else if (title.equals("finance categories")) {
					if (Accounter.getUser().canSeeInvoiceTransactions())
						CompanyActionFactory.getChartOfAccountsAction().run(
								null, true);
				}

			}
		});
		return link;
	}

}
