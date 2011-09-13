package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

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

	private void initData() {
		
	}

	private void createControls() {
		accountingMethodForSummaryLabel.setText(Accounter.constants().getDefaultAccountingMethodForSummary());
		cashRadioButton.setText(Accounter.constants().cash());
		accuralRadioButton.setText(Accounter.constants().accrual());
	}

	@Override
	public String getTitle() {
		return "CashOrAccuralAccountingSummaryOption";
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

}
