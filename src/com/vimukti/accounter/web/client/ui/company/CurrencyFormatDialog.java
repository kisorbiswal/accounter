package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.i18n.AccounterNumberFormat;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CurrencyFormatDialog extends BaseDialog {

	private static final char CURRENCY_SIGN = '\u00A4';

	private TextItem positiveTextItem, negativeTextItem, decimalSymbolItem,
			digitGroupingSymbolItem, currencySymbolItem,
			noOfDigitsAfterDecimalText;
	private SelectCombo positiveCurrencyFormatCombo,
			negativeCurrencyFormatCombo, digitGroupingCombo;

	private int posNum;

	private int negNum;

	private String currencySymbol;

	private String decimalSymbol;

	private String digitGroupSymbol;

	private int digitGroupNum;

	private int noOfDigitsAfterDecimal;

	String[] groups = { "12,34,56,789" };

	private ClientCompanyPreferences preferences;

	String[] posForValue = { "S1D1", "1D1S", "S 1D1", "1D1 S" };
	String[] negForValue = { "(S1D1)", "-S1D1", "S-1D1", "S1D1-", "(1D1S)",
			"-1D1S", "1D1-S", "1D1S-", "1D1S-", "-S 1D1", "1D1 S-", "S 1D1-",
			"S -1D1", "1D1- S", "(S 1D1)", "(1D1 S)" };

	public CurrencyFormatDialog(String string) {
		super(string);
		this.preferences = Accounter.getCompany().getPreferences();
		createControl();
		initData(preferences);
	}

	private void createControl() {

		VerticalPanel allPanels = new VerticalPanel();
		allPanels.setWidth("100%");

		CaptionPanel panel = new CaptionPanel(messages.exampleFormat());
		DOM.setStyleAttribute(panel.getElement(), "border", "1px solid #ccc");

		Label example = new Label(messages.currencyFormat());
		example.setStyleName("currency_format_label");

		DynamicForm formatForm = new DynamicForm();
		formatForm.setNumCols(4);

		positiveTextItem = new TextItem(messages.positive());
		positiveTextItem.setDisabled(true);
		negativeTextItem = new TextItem(messages.negative());
		negativeTextItem.setDisabled(true);

		formatForm.setFields(positiveTextItem, negativeTextItem);

		DynamicForm form = new DynamicForm();
		form.setNumCols(2);
		form.addStyleName("currency_format_align");

		currencySymbolItem = new TextItem(messages.currencySymbol());
		currencySymbolItem.setValue(getCompanyPreferences()
				.getPrimaryCurrency().getSymbol());
		currencySymbolItem.setDisabled(true);
		positiveCurrencyFormatCombo = new SelectCombo(
				messages.postiveCurrencyFormat(), false);
		positiveCurrencyFormatCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						posNum = positiveCurrencyFormatCombo.getSelectedIndex();
						update();
					}
				});

		negativeCurrencyFormatCombo = new SelectCombo(
				messages.negativeCurrencyFormat(), false);
		negativeCurrencyFormatCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						negNum = negativeCurrencyFormatCombo.getSelectedIndex();
						update();
					}
				});

		decimalSymbolItem = new TextItem(messages.decimalSymbol());
		decimalSymbolItem.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				decimalSymbol = decimalSymbolItem.getValue();
				initPositiveFormatValues();
				initNegativeFormatValues();
				update();
			}
		});
		noOfDigitsAfterDecimalText = new TextItem(
				messages.noOfDigitsAfterDecimal());
		noOfDigitsAfterDecimalText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				noOfDigitsAfterDecimal = Integer
						.valueOf(noOfDigitsAfterDecimalText.getValue());
				update();
			}
		});

		digitGroupingSymbolItem = new TextItem(messages.digitGroupingDecimal());
		digitGroupingSymbolItem.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				digitGroupSymbol = digitGroupingSymbolItem.getValue();
				initDigitGroupingValues();
				update();
			}
		});
		digitGroupingCombo = new SelectCombo(messages.digitGrouping(), false);
		digitGroupingCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						digitGroupNum = digitGroupingCombo.getSelectedIndex();
						update();
					}
				});
		form.setFields(currencySymbolItem, positiveCurrencyFormatCombo,
				negativeCurrencyFormatCombo, decimalSymbolItem,
				noOfDigitsAfterDecimalText, digitGroupingSymbolItem,
				digitGroupingCombo);

		panel.setContentWidget(formatForm);

		allPanels.add(panel);
		allPanels.add(form);
		bodyLayout.add(allPanels);

	}

	public void initData(ClientCompanyPreferences preferences) {
		currencySymbol = preferences.getPrimaryCurrency().getSymbol();
		decimalSymbol = preferences.getDecimalCharacter();
		noOfDigitsAfterDecimal = preferences.getDecimalNumber();
		if (decimalSymbol != null) {
			decimalSymbolItem.setValue(decimalSymbol);
		}

		if (noOfDigitsAfterDecimal != 0) {
			noOfDigitsAfterDecimalText.setValue(String
					.valueOf(noOfDigitsAfterDecimal));
		}

		digitGroupSymbol = preferences.getDigitGroupCharacter();

		if (digitGroupSymbol != null) {
			digitGroupingSymbolItem.setValue(digitGroupSymbol);
		}

		String currencyFormat = preferences.getCurrencyFormat();
		String[] split = currencyFormat.split(";");
		String positive = split[0];// ¤#,##0.00

		String pv = positive.replaceAll(String.valueOf(CURRENCY_SIGN), "");// ¤#,##
		pv = pv.replaceAll(" ", "");// #,##0.00

		String pos = positive.replace(pv, "1D1");// ¤1.1
		pos = pos.replace(CURRENCY_SIGN, 'S');
		posNum = getIndex(posForValue, pos);

		String negative = split[1];// (¤#,##0.00)
		String neg = negative.replace(pv, "1D1");// (¤1.1)
		neg = neg.replace(CURRENCY_SIGN, 'S');
		negNum = getIndex(negForValue, neg);

		initPositiveFormatValues();
		initNegativeFormatValues();
		initDigitGroupingValues();
		update();
	}

	private String getDecimalString() {
		String value = "0.";
		for (int i = 0; i < noOfDigitsAfterDecimal; i++) {
			value += "0";
		}
		return value;
	}

	protected void initDigitGroupingValues() {
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < groups.length; i++) {
			String value = groups[i];
			value = value.replaceAll(",",
					AccounterNumberFormat.quoteReplacement(digitGroupSymbol));
			values.add(value);
		}
		digitGroupingCombo.initCombo(values);
		digitGroupingCombo.setSelectedItem(digitGroupNum);
	}

	protected void update() {
		String value = groups[digitGroupNum].replaceAll(",",
				AccounterNumberFormat.quoteReplacement(digitGroupSymbol));
		value += decimalSymbol;
		for (int i = 0; i < noOfDigitsAfterDecimal; i++) {
			value += "0";
		}

		String pValue = posForValue[posNum].replaceAll("S",
				AccounterNumberFormat.quoteReplacement(currencySymbol))
				.replaceAll("1D1", value);

		String nValue = negForValue[negNum].replaceAll("S",
				AccounterNumberFormat.quoteReplacement(currencySymbol))
				.replaceAll("1D1", (value));

		positiveTextItem.setValue(pValue);
		negativeTextItem.setValue(nValue);
	}

	protected void initNegativeFormatValues() {
		List<String> vals = getValues(negForValue);
		negativeCurrencyFormatCombo.initCombo(vals);
		negativeCurrencyFormatCombo.setSelectedItem(negNum);
	}

	protected void initPositiveFormatValues() {
		List<String> vals = getValues(posForValue);
		positiveCurrencyFormatCombo.initCombo(vals);
		positiveCurrencyFormatCombo.setSelectedItem(posNum);
	}

	private List<String> getValues(String[] rawData) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < rawData.length; i++) {
			String value = rawData[i];
			value = value.replaceAll("S",
					AccounterNumberFormat.quoteReplacement(currencySymbol));
			value = value.replaceAll("D",
					AccounterNumberFormat.quoteReplacement(decimalSymbol));
			list.add(value);
		}
		return list;
	}

	private ClientCompanyPreferences getCompanyPreferences() {
		return getCompany().getPreferences();
	}

	private int getIndex(String[] values, String pos) {
		for (int i = 0; i < values.length; i++) {
			if (values[i].endsWith(pos)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	protected boolean onOK() {
		ClientCompanyPreferences companyPreferences = getCompanyPreferences();
		companyPreferences.setDecimalNumber(noOfDigitsAfterDecimal);
		companyPreferences.setDecimalCharacte(decimalSymbol);
		companyPreferences.setDigitGroupCharacter(digitGroupSymbol);

		String value = "#,##";

		value += getDecimalString();

		String pValue = posForValue[posNum].replaceAll("S",
				String.valueOf(CURRENCY_SIGN)).replaceAll("1D1", value);

		String nValue = negForValue[negNum].replaceAll("S",
				String.valueOf(CURRENCY_SIGN)).replaceAll("1D1", value);

		String currencyPattern = pValue + ";" + nValue;
		companyPreferences.setCurrencyFormat(currencyPattern);// @#,##0.00 S1D1
		Global.get().getFormater().setCurrencyFormat(companyPreferences);
		this.removeFromParent();
		return true;
	}

	@Override
	protected boolean onCancel() {
		this.removeFromParent();
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
