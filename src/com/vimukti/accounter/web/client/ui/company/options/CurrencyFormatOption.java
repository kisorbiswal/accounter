package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.i18n.AccounterNumberFormat;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CurrencyFormatOption extends AbstractPreferenceOption {

	private static CurrencyFormatOptionUiBinder uiBinder = GWT
			.create(CurrencyFormatOptionUiBinder.class);
	private static final char CURRENCY_SIGN = '\u00A4';
	@UiField
	TextBox positiveTextBox;

	@UiField
	TextBox negativeTextBox;

	@UiField
	ListBox currencySymbolCombo;

	@UiField
	Label positiveCurrencyFormatLabel;

	@UiField
	ListBox positiveCurrencyFormatCombo;

	@UiField
	Label negativeCurrencyFormatLabel;

	@UiField
	ListBox negativeCurrencyFormatCombo;

	@UiField
	Label decimalSymbolLabel;

	@UiField
	TextBox decimalSymbolTextBox;

	@UiField
	Label noOfDigitsAfterDecimalLabel;

	@UiField
	TextBox noOfDigitsAfterDecimalTextBox;

	@UiField
	Label digitGroupingSymbolLabel;

	@UiField
	TextBox digitGroupingSymbolTextBox;

	@UiField
	Label digitGroupingLabel;

	@UiField
	ListBox digitGroupingCombo;

	@UiField
	Label positiveLabel;

	@UiField
	Label negaLabel;

	@UiField
	Label currencySymbolLabel;

	List<ClientCurrency> currenciesList;

	private int posNum;

	private int negNum;

	private String currencySymbol;

	private String decimalSymbol;

	private String digitGroupSymbol;

	private int digitGroupNum;

	private int noOfDigitsAfterDecimal;

	String[] groups = { "12,34,56,789" };

	String[] posForValue = { "S1D1", "1D1S", "S 1D1", "1D1 S" };
	String[] negForValue = { "(S1D1)", "-1D1S", "S-1D1", "1D1S-", "(1D1S)" };

	interface CurrencyFormatOptionUiBinder extends
			UiBinder<Widget, CurrencyFormatOption> {
	}

	public CurrencyFormatOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.currencyFormat();
	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createControls() {

		positiveLabel.setText(messages.positive());
		negaLabel.setText(messages.negative());
		currencySymbolLabel.setText(messages.currencySymbol());
		positiveCurrencyFormatLabel.setText(messages.postiveCurrencyFormat());
		negativeCurrencyFormatLabel.setText(messages.negativeCurrencyFormat());
		decimalSymbolLabel.setText(messages.decimalSymbol());
		noOfDigitsAfterDecimalLabel.setText(messages.noOfDigitsAfterDecimal());
		digitGroupingSymbolLabel.setText(messages.digitGroupingDecimal());
		digitGroupingLabel.setText(messages.digitGrouping());

		currenciesList = CoreUtils.getCurrencies(getCompany().getCurrencies());
		for (ClientCurrency currency : currenciesList) {
			currencySymbolCombo.addItem(currency.getSymbol());
		}

		currencySymbolCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				ClientCurrency currency = currenciesList
						.get(currencySymbolCombo.getSelectedIndex());
				currencySymbol = currency.getSymbol();
				initPositiveFormatValues();
				initNegativeFormatValues();
				update();
			}
		});
		noOfDigitsAfterDecimalTextBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				noOfDigitsAfterDecimal = Integer
						.valueOf(noOfDigitsAfterDecimalTextBox.getValue());
				update();
			}
		});

		decimalSymbolTextBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				decimalSymbol = decimalSymbolTextBox.getValue();
				initPositiveFormatValues();
				initNegativeFormatValues();
				update();
			}
		});

		digitGroupingSymbolTextBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				digitGroupSymbol = digitGroupingSymbolTextBox.getValue();
				initDigitGroupingValues();
				update();
			}
		});

		positiveCurrencyFormatCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				posNum = positiveCurrencyFormatCombo.getSelectedIndex();
				update();
			}
		});

		negativeCurrencyFormatCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				negNum = negativeCurrencyFormatCombo.getSelectedIndex();
				update();
			}
		});

		digitGroupingCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				digitGroupNum = digitGroupingCombo.getSelectedIndex();
				update();
			}
		});
	}

	protected void update() {
		String value = groups[digitGroupNum].replaceAll(",", digitGroupSymbol);
		value += decimalSymbol;
		for (int i = 0; i < noOfDigitsAfterDecimal; i++) {
			value += "0";
		}

		String pValue = posForValue[posNum].replaceAll("S", currencySymbol)
				.replaceAll("1D1", value);

		String nValue = negForValue[negNum].replaceAll("S", currencySymbol)
				.replaceAll("1D1", value);

		positiveTextBox.setValue(pValue);
		negativeTextBox.setValue(nValue);

	}

	protected void initDigitGroupingValues() {
		digitGroupingCombo.clear();
		for (int i = 0; i < groups.length; i++) {
			String value = groups[i];
			value = value.replaceAll(",", digitGroupSymbol);
			digitGroupingCombo.addItem(value);
		}
		digitGroupingCombo.setSelectedIndex(digitGroupNum);
	}

	private void initPositiveFormatValues() {
		positiveCurrencyFormatCombo.clear();
		List<String> vals = getValues(posForValue);
		for (String s : vals) {
			positiveCurrencyFormatCombo.addItem(s);
		}
		positiveCurrencyFormatCombo.setSelectedIndex(posNum);
	}

	private List<String> getValues(String[] rawData) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < rawData.length; i++) {
			String value = rawData[i];
			value = value.replaceAll("S", currencySymbol);
			value = value.replaceAll("D", decimalSymbol);
			list.add(value);
		}
		return list;
	}

	private void initNegativeFormatValues() {
		negativeCurrencyFormatCombo.clear();
		List<String> vals = getValues(negForValue);
		for (String s : vals) {
			negativeCurrencyFormatCombo.addItem(s);
		}
		negativeCurrencyFormatCombo.setSelectedIndex(negNum);
	}

	@Override
	public void onSave() {
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
		Accounter.setCurrencyFormat();
	}

	private String getDecimalString() {
		String value = "0.";
		for (int i = 0; i < noOfDigitsAfterDecimal; i++) {
			value += "0";
		}
		return value;
	}

	@Override
	public void initData() {
		ClientCompanyPreferences companyPreferences = getCompanyPreferences();
		currencySymbol = getCompanyPreferences().getPrimaryCurrency()
				.getSymbol();
		decimalSymbol = companyPreferences.getDecimalCharacter();
		noOfDigitsAfterDecimal = companyPreferences.getDecimalNumber();
		decimalSymbolTextBox.setValue(decimalSymbol);
		noOfDigitsAfterDecimalTextBox.setValue(String
				.valueOf(noOfDigitsAfterDecimal));
		digitGroupSymbol = companyPreferences.getDigitGroupCharacter();
		digitGroupingSymbolTextBox.setText(digitGroupSymbol);

		String currencyFormat = companyPreferences.getCurrencyFormat();
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

	private int getIndex(String[] values, String pos) {
		for (int i = 0; i < values.length; i++) {
			if (values[i].endsWith(pos)) {
				return i;
			}
		}
		return 0;
	}

}