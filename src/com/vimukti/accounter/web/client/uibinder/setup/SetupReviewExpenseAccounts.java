package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SetupReviewExpenseAccounts extends AbstractSetupPage {

	private static SetupReviewExpenseAccountsUiBinder uiBinder = GWT
			.create(SetupReviewExpenseAccountsUiBinder.class);

	interface SetupReviewExpenseAccountsUiBinder extends
			UiBinder<Widget, SetupReviewExpenseAccounts> {
	}

	public SetupReviewExpenseAccounts() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@UiField
	VerticalPanel viewPanel;
	@UiField
	Label ExpensesInfo;
	@UiField
	HTML RecommendedInfo;
	@UiField
	Label AccountName;
	@UiField
	Label Type;
	@UiField
	CheckBox ExpensesClick;
	@UiField
	Button RestoreButton;
	@UiField
	HTML ExpensesLink;
	@UiField
	HTML ExpensesNote;

	public SetupReviewExpenseAccounts(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	protected void createControls() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

}
