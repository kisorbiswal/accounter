package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SetupUsingEstimatesAndStatementsPage extends AbstractSetupPage {
	private static final String ESTIMATES = "Estimates";
	private VerticalPanel mainPanel;
	private HTML titleHtml, descrptionHtml, subTitleHtml, html, html2, html3;
	private RadioButton yesRadioButton, noRadioButton;

	@Override
	public String getHeader() {
		return this.accounterConstants.wanttoCreateEstimatesInAccounter();
	}

	@Override
	public VerticalPanel getPageBody() {

		yesRadioButton = new RadioButton(ESTIMATES, accounterConstants.yes());
		mainPanel.add(yesRadioButton);

		noRadioButton = new RadioButton(ESTIMATES, accounterConstants.no());
		mainPanel.add(yesRadioButton);

		titleHtml = new HTML("Using Statements in Accounter");
		titleHtml.setStyleName("BOLD");

		descrptionHtml = new HTML(
				"Billing statements are send to customers to list charges accumulated over a period of time .Statements may be sent at regular intervals,as in a monthly statement,or when a customer payment is past due.");
		subTitleHtml = new HTML("  Some example:");
		html = new HTML(
				".An attorney invoices a client for multiple services provided .if the invoice is not paid,the attorney can then send the client a reminder statement");
		html2 = new HTML(
				". A gym sned each member a monthly statement that includes fees and any overdue payments or finance charges");

		html3 = new HTML("Do you want to use billing statements in Acconter ? ");

		return new VerticalPanel();
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return true;
	}

}
