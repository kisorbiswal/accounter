package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SetupUsingEstimatesAndStatementsPage extends AbstractSetupPage {

	private static SetupUsingEstimatesAndStatementsPageUiBinder uiBinder = GWT
			.create(SetupUsingEstimatesAndStatementsPageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	RadioButton estimatesYes;
	@UiField
	RadioButton estimatesNo;
	@UiField
	Label billingStatements;
	@UiField
	Label someExampleText;
	@UiField
	HTML someExampleList;
	@UiField
	Label billingQuestion;
	@UiField
	RadioButton statementYes;
	@UiField
	RadioButton statementsNo;
	@UiField
	Label headerLabel;

	interface SetupUsingEstimatesAndStatementsPageUiBinder extends
			UiBinder<Widget, SetupUsingEstimatesAndStatementsPage> {
	}

	public SetupUsingEstimatesAndStatementsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	public void onLoad() {
		boolean doyouwantEstimates = preferences.isDoyouwantEstimates();
		boolean doyouwanrstatements = preferences.isDoyouwantstatements();
		// estimates
		if (doyouwantEstimates) {
			estimatesYes.setValue(true);
		} else {
			estimatesNo.setValue(true);
		}
		// statements
		if (doyouwanrstatements) {
			statementYes.setValue(true);
		} else {
			statementsNo.setValue(true);
		}
	}

	@Override
	public void onSave() {

		boolean yesEstmatesvalue = estimatesYes.getValue();
		boolean yesStatementvalue = statementYes.getValue();
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
	protected void createControls() {
		headerLabel.setText(accounterConstants
				.wanttoCreateEstimatesInAccounter());
		estimatesYes.setText(accounterConstants.yes());
		estimatesNo.setTitle(accounterConstants.no());
		billingStatements.setText(accounterConstants.statementDescription());
		someExampleText.setText(accounterConstants.statementSomeExample());
		someExampleList.setText(accounterMessages.statementlist());
		billingQuestion.setText(accounterMessages.statementQuestion());
		statementYes.setTitle(accounterConstants.yes());
		statementsNo.setTitle(accounterConstants.no());
	}

	@Override
	public boolean doShow() {
		return true;
	}
}
