package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class CashOrAccuralAccountingSummaryOption extends
		AbstractPreferenceOption {

	private static CashOrAccuralAccountingSummaryOptionUiBinder uiBinder = GWT
			.create(CashOrAccuralAccountingSummaryOptionUiBinder.class);
	@UiField
	Label accountingMethodForSummaryLabel;
	@UiField
	RadioButton cashRadioButton;
	@UiField
	RadioButton accuralRadioButton;

	interface CashOrAccuralAccountingSummaryOptionUiBinder extends
			UiBinder<Widget, CashOrAccuralAccountingSummaryOption> {
	}

	public CashOrAccuralAccountingSummaryOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {

	}

	public void createControls() {
		accountingMethodForSummaryLabel.setText(messages
				.getDefaultAccountingMethodForSummary());
		cashRadioButton.setName(messages.cashoraccural());
		cashRadioButton.setHTML(messages.cash());
		accuralRadioButton.setName(messages.cashoraccural());
		accuralRadioButton.setHTML(messages.accrual());
	}

	@Override
	public String getTitle() {
		return messages.cashOrAccural();
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		return messages.cashOrAccural();
	}

}
