package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class BankingAndOtherFinancialDetailsPage extends
		AbstractCompanyInfoPanel {

	private static BankingAndOtherFinancialDetailsPageUiBinder uiBinder = GWT
			.create(BankingAndOtherFinancialDetailsPageUiBinder.class);

	interface BankingAndOtherFinancialDetailsPageUiBinder extends
			UiBinder<Widget, BankingAndOtherFinancialDetailsPage> {
	}

	public BankingAndOtherFinancialDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button button;

	public BankingAndOtherFinancialDetailsPage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

}
