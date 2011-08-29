package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

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
	@UiField
	HTML billingStatements;
	@UiField
	Label stmt_header;

	interface SetupUsingEstimatesAndStatementsPageUiBinder extends
			UiBinder<Widget, SetupUsingEstimatesAndStatementsPage> {
	}

	public SetupUsingEstimatesAndStatementsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}


	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants
				.wanttoCreateEstimatesInAccounter());
		estimatesYes.setText(accounterConstants.yes());
		estimatesNo.setText(accounterConstants.no());
		billingStatements.setHTML(accounterMessages.statementDescription());
		someExampleText.setText(accounterConstants.statementSomeExample());
		someExampleList.setHTML(accounterMessages.statementlist());
		billingQuestion.setText(accounterMessages.statementQuestion());
		statementYes.setText(accounterConstants.yes());
		statementsNo.setText(accounterConstants.no());
		stmt_header.setText(accounterConstants.doyouWantToUseStatements());
	}

	@Override
	public boolean canShow() {
		return true;
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
	protected boolean validate() {
		if ((!(estimatesYes.getValue() || estimatesNo.getValue()))
				&& (!(statementYes.getValue() || statementsNo.getValue()))) {
			Accounter.showError(accounterMessages
					.pleaseEnter(accounterConstants.details()));
			return false;
		} else if (!(estimatesYes.getValue() || estimatesNo.getValue())) {
			Accounter.showError(accounterMessages
					.pleaseEnter(accounterConstants.wanttoCreateEstimatesInAccounter()));
			return false;
		} else if (!(statementYes.getValue() || statementsNo.getValue())) {
			Accounter.showError(accounterMessages
					.pleaseEnter(accounterConstants.doyouWantToUseStatements()));
			return false;
		} else {
			return true;
		}
	}
}
