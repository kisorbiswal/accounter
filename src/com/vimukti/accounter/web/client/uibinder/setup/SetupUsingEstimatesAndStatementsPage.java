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
	Label EstimatesYesText;
	@UiField
	RadioButton EstimatesYes;
	@UiField
	RadioButton EstimatesNo;
	@UiField
	Label BillingStatements;
	@UiField
	Label SomeExampleText;
	@UiField
	HTML SomeExampleList;
	@UiField
	Label BillingQuestion;
	@UiField
	RadioButton StatementYes;
	@UiField
	Label StatementYesText;
	@UiField
	RadioButton StatementsNo;

	interface SetupUsingEstimatesAndStatementsPageUiBinder extends
			UiBinder<Widget, SetupUsingEstimatesAndStatementsPage> {
	}

	public SetupUsingEstimatesAndStatementsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		// TODO Auto-generated method stub

	}

}
