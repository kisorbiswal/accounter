package com.vimukti.accounter.web.client.ui.company.options;

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
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CurrencyFormatOption extends AbstractPreferenceOption {

	private static CurrencyFormatOptionUiBinder uiBinder = GWT
			.create(CurrencyFormatOptionUiBinder.class);

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
	ListBox noOfDigitsAfterDecimalTextBox;

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

	String[] groups = { "123456789", "123D456D789", "123456D789",
			"12D34D56D789" };

	String[] posForValue = { "S1D1", "1D1S", "S 1D1", "1D1 S" };

	String[] postiveFormats = { "SN", "NS", "S N", "N S" };

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
	public void onSave() {
		// TODO Auto-generated method stub

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
		ClientCurrency currency = currenciesList.get(currencySymbolCombo
				.getSelectedIndex());
		initPositiveFormatValues(currency.getSymbol());
		currencySymbolCombo.setSelectedIndex(0);
		currencySymbolCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				ClientCurrency currency = currenciesList
						.get(currencySymbolCombo.getSelectedIndex());
				currencySymbol = currency.getSymbol();
				initPositiveFormatValues(currencySymbol);
				update();
			}
		});
		for (int i = 0; i < 6; i++) {
			noOfDigitsAfterDecimalTextBox.addItem(String.valueOf(i));
		}

		decimalSymbolTextBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				decimalSymbol = decimalSymbolTextBox.getValue();
				update();
			}
		});

		noOfDigitsAfterDecimalTextBox.setSelectedIndex(0);
		currencySymbolCombo.setSelectedIndex(0);

		digitGroupingSymbolTextBox.setText(",");
		digitGroupSymbol = ",";
		initDigitGroupingValues();

		digitGroupingSymbolTextBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				int i = digitGroupingCombo.getSelectedIndex();
				digitGroupSymbol = digitGroupingSymbolTextBox.getValue();
				initDigitGroupingValues();
				digitGroupingCombo.setSelectedIndex(i);
			}
		});

		positiveCurrencyFormatCombo.setSelectedIndex(0);
		negativeCurrencyFormatCombo.setSelectedIndex(0);

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

		noOfDigitsAfterDecimalTextBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				noOfDigitsAfterDecimal = noOfDigitsAfterDecimalTextBox
						.getSelectedIndex();
				update();
			}
		});

	}

	protected void update() {
		String posStr = postiveFormats[posNum];
		String value = "";

		value = posStr.replaceAll("S", currencySymbol);
		String replaceAll = groups[digitGroupNum].replaceAll("D",
				digitGroupSymbol);
		value = posStr.replaceAll("N", replaceAll);

		value += decimalSymbol;
		for (int i = 0; i < noOfDigitsAfterDecimal; i++) {
			value += "0";
		}

		positiveTextBox.setValue(value);

	}

	protected void initDigitGroupingValues() {
		digitGroupingCombo.clear();
		for (int i = 0; i < groups.length; i++) {
			String value = groups[i];
			value = value.replaceAll("D", digitGroupSymbol);
			digitGroupingCombo.addItem(value);
		}
	}

	private void initPositiveFormatValues(String symbol) {
		positiveCurrencyFormatCombo.clear();
		for (int i = 0; i < postiveFormats.length; i++) {
			String value = postiveFormats[i];
			value = value.replaceAll("S", symbol);
			value = value.replaceAll("N", "1.1");
			positiveCurrencyFormatCombo.addItem(value);
		}

	}

	@Override
	public void initData() {

	}
}