package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class NumberFormatOption extends AbstractPreferenceOption {

	private static NumberFormatOptionUiBinder uiBinder = GWT
			.create(NumberFormatOptionUiBinder.class);

	interface NumberFormatOptionUiBinder extends
			UiBinder<Widget, NumberFormatOption> {
	}

	public NumberFormatOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@UiField
	RadioButton normalFormatRadioButton;
	@UiField
	RadioButton withInParathesisFormatRadioButton;
	@UiField
	RadioButton minusWithInParanthesisRadioButton;
	@UiField
	RadioButton withInTrailingMinusRadioButton;
	@UiField
	Label negativeNoheaderLabel;
	@UiField
	Label normalFormatCommentLabel;
	@UiField
	Label withInParathesisFormatCommentLabel;
	@UiField
	Label minusWithInParanthesisCommentLabel;
	@UiField
	Label withInTrailingMinusCommentLabel;
	@UiField
	ListBox decimalDigitLimitCombo;
	@UiField
	Label decimalHeaderLabel;
	@UiField
	Label comboLabel;
	@UiField
	Label commentLabel;

	@Override
	public String getTitle() {
		return messages.numberFormat();
	}

	@Override
	public void onSave() {
		if (normalFormatRadioButton.getValue()) {
			getCompanyPreferences().setNegativeNumberShownType(
					ClientCompanyPreferences.NEGATIVE_NUMBER_NORMAL);
		} else if (withInParathesisFormatRadioButton.getValue()) {
			getCompanyPreferences()
					.setNegativeNumberShownType(
							ClientCompanyPreferences.NEGATIVE_NUMBER_WITHIN_PARENTHESES);
		} else if (minusWithInParanthesisRadioButton.getValue()) {
			getCompanyPreferences()
					.setNegativeNumberShownType(
							ClientCompanyPreferences.NEGATIVE_NUMBER_MINUS_WITHIN_PARENTHESES);
		} else if (withInTrailingMinusRadioButton.getValue()) {
			getCompanyPreferences()
					.setNegativeNumberShownType(
							ClientCompanyPreferences.NEGATIVE_NUMBER_WITH_TRAILING_MINUS);
		} else {
			getCompanyPreferences().setNegativeNumberShownType(
					ClientCompanyPreferences.NEGATIVE_NUMBER_NORMAL);
		}

		if (decimalDigitLimitCombo.getSelectedIndex() != -1) {
			getCompanyPreferences().setDecimalNumber(
					decimalDigitLimitCombo.getSelectedIndex());
		}
	}

	@Override
	public String getAnchor() {
		return messages.numberFormat();
	}

	@Override
	public void createControls() {
		negativeNoheaderLabel.setText(messages.negativeNumberFormat());
		normalFormatRadioButton.setText(messages.normalFormat());
		normalFormatCommentLabel.setText(messages.normalFormatComment());
		withInParathesisFormatRadioButton.setText(messages
				.negativeNumberWithInTheParathesis());
		withInParathesisFormatCommentLabel.setText(messages
				.negativeNumberWithInTheParathesisComment());
		minusWithInParanthesisRadioButton.setText(messages
				.negativeNumberMinusWithInTheParathesis());
		minusWithInParanthesisCommentLabel.setText(messages
				.negativeNumberMinusWithInTheParathesisComment());
		withInTrailingMinusRadioButton.setText(messages
				.numberWithTrailingMinus());
		withInTrailingMinusCommentLabel.setText(messages
				.numberWithTrailingMinusComment());

		decimalHeaderLabel.setText(messages.decimalDigitLimit());
		comboLabel.setText(messages.decimalDigitLimit());
		for (int i = 0; i < 10; i++) {
			decimalDigitLimitCombo.addItem(String.valueOf(i));
		}
	}

	@Override
	public void initData() {
		if (getCompanyPreferences().getNegativeNumberShownType() == ClientCompanyPreferences.NEGATIVE_NUMBER_NORMAL) {
			normalFormatRadioButton.setValue(true);
		} else if (getCompanyPreferences().getNegativeNumberShownType() == ClientCompanyPreferences.NEGATIVE_NUMBER_WITHIN_PARENTHESES) {
			withInParathesisFormatRadioButton.setValue(true);
		} else if (getCompanyPreferences().getNegativeNumberShownType() == ClientCompanyPreferences.NEGATIVE_NUMBER_MINUS_WITHIN_PARENTHESES) {
			minusWithInParanthesisRadioButton.setValue(true);
		} else if (getCompanyPreferences().getNegativeNumberShownType() == ClientCompanyPreferences.NEGATIVE_NUMBER_WITH_TRAILING_MINUS) {
			withInTrailingMinusRadioButton.setValue(true);
		} else {
			normalFormatRadioButton.setValue(true);
		}

		decimalDigitLimitCombo.setSelectedIndex(getCompanyPreferences()
				.getDecimalNumber());
	}
}
