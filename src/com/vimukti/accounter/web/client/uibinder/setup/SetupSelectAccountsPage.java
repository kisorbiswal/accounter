package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SetupSelectAccountsPage extends AbstractSetupPage implements
		HasText {

	private static SetupReviewExpenseAccountsUiBinder uiBinder = GWT
			.create(SetupReviewExpenseAccountsUiBinder.class);

	interface SetupReviewExpenseAccountsUiBinder extends
			UiBinder<Widget, SetupSelectAccountsPage> {
	}

	public SetupSelectAccountsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@UiField
	VerticalPanel viewPanel;
	@UiField
	Label expensesInfo;
	@UiField
	HTML recommendedInfo;
	@UiField
	Label accountName;
	@UiField
	Label type;
	@UiField
	CheckBox expensesClick;
	@UiField
	Button restoreButton;
	@UiField
	HTML expensesLink;
	@UiField
	HTML expensesNote;
	@UiField
	HTML reviewHead;
	@UiField
	Label headerLabel;

	public SetupSelectAccountsPage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.reviewIncomeAndExpensesAccounts());
		
		expensesInfo.setText(accounterConstants.expenseInformation());
		recommendedInfo.setText(accounterMessages.recommendedAccounts());
		Label accountName;
		Label type;
		CheckBox expensesClick;
		restoreButton.setText(accounterConstants.restoreRecommendations());
		expensesLink.setText(accounterMessages.whyshoudIUseRecommended());
		expensesNote.setText(accounterMessages.recommendedNote());
		reviewHead
				.setText(accounterConstants.reviewIncomeAndExpensesAccounts());
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
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doShow() {
		return true;
	}

}
