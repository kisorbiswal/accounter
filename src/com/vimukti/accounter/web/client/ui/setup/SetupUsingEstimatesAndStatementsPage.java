package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SetupUsingEstimatesAndStatementsPage extends AbstractSetupPage {
	private static final String ESTIMATES = "Estimates";
	private static final String STATEMENTS = "Statements";
	private VerticalPanel mainPanel;
	private HTML titleHtml, descrptionHtml, subTitleHtml, html, html2, html3;
	private RadioButton yesRadioButton, noRadioButton, stateMentYesRadioButton,
			statementNoRadioButton;

	@Override
	public String getHeader() {
		return this.accounterConstants.wanttoCreateEstimatesInAccounter();
	}

	@Override
	public VerticalPanel getPageBody() {

		mainPanel = new VerticalPanel();

		yesRadioButton = new RadioButton(ESTIMATES, accounterConstants.yes());
		mainPanel.add(yesRadioButton);

		noRadioButton = new RadioButton(ESTIMATES, accounterConstants.no());
		mainPanel.add(noRadioButton);

		titleHtml = new HTML("Using Statements in Accounter");
		titleHtml.setStyleName("BOLD");
		descrptionHtml = new HTML(
				"Billing statements are send to customers to list charges accumulated over a period of time .Statements may be sent at regular intervals,as in a monthly statement,or when a customer payment is past due.");
		subTitleHtml = new HTML("  Some example:");
		html = new HTML(
				".An attorney invoices a client for multiple services provided .if the invoice is not paid,the attorney can then send the client a reminder statement");
		html2 = new HTML(
				". A gym sned each member a monthly statement that includes fees and any overdue payments or finance charges");

		// statements
		html3 = new HTML("Do you want to use billing statements in Acconter ? ");

		stateMentYesRadioButton = new RadioButton(STATEMENTS,
				accounterConstants.yes());

		statementNoRadioButton = new RadioButton(STATEMENTS, accounterConstants
				.no());

		mainPanel.add(descrptionHtml);
		mainPanel.add(subTitleHtml);
		mainPanel.add(html);
		mainPanel.add(html2);
		mainPanel.add(html3);
		mainPanel.add(stateMentYesRadioButton);
		mainPanel.add(statementNoRadioButton);

		mainPanel.addStyleName("setuppage_body");
		return mainPanel;

	}

	@Override
	public void onLoad() {
		boolean doyouwantEstimates = preferences.isDoyouwantEstimates();
		boolean doyouwanrstatements = preferences.isDoyouwanrstatements();
		// estimates
		if (doyouwantEstimates) {
			yesRadioButton.setValue(true);
		} else {
			noRadioButton.setValue(true);
		}
		// statements
		if (doyouwanrstatements) {
			stateMentYesRadioButton.setValue(true);
		} else {
			statementNoRadioButton.setValue(true);
		}
	}

	@Override
	public void onSave() {

		boolean yesEstmatesvalue = yesRadioButton.getValue();
		boolean yesStatementvalue = stateMentYesRadioButton.getValue();
		// Estimates
		if (yesEstmatesvalue) {
			preferences.setDoyouwantEstimates(true);
		} else {
			preferences.setDoyouwantEstimates(false);
		}
		// Statements
		if (yesStatementvalue) {
			preferences.setDoyouwantEstimates(true);
		} else {
			preferences.setDoyouwantEstimates(false);
		}
	}

	@Override
	public boolean doShow() {
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
