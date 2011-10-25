package com.vimukti.accounter.web.client.ui.widgets;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CurrencyWidget extends DynamicForm {

	// private VerticalPanel widgetPanel;

	private CurrencyChangeListener listener;

	private TextItem factorField;
	private CurrencyCombo currencyCombo;
	private LabelItem baseCurrencyLbl;

	private ClientCurrency baseCurrency, selectedCurrencyItem;
	private List<ClientCurrency> currencies;

	public CurrencyWidget(List<ClientCurrency> currencies,
			ClientCurrency baseCurrency) {
		this.currencies = currencies;
		this.baseCurrency = baseCurrency;
		// setNumCols(3);
		currencyCombo = new CurrencyCombo("Currency :");
		currencyCombo.initCombo(currencies);
		if (baseCurrency != null) {
			currencyCombo.setSelected(baseCurrency.getFormalName());
		}
		currencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {

					@Override
					public void selectedComboBoxItem(ClientCurrency selectItem) {
						currencyChanged(selectItem);
					}
				});

		factorField = new TextItem("Factor :");
		factorField.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String factorStr = factorField.getValue().toString();
				factorFieldChagned(Double.parseDouble(factorStr));
			}
		});
		baseCurrencyLbl = new LabelItem();
		if (baseCurrency != null) {
			baseCurrencyLbl.setTitle(baseCurrency.getFormalName());
		}
		setStyleName("currencyTextBox");
		setWidth("100%");
		setNumCols(4);
		getCellFormatter().setWidth(0, 0, "200");
		setFields(currencyCombo, new LabelItem());
	}

	private void factorFieldChagned(double factor) {
		if (listener != null) {
			ClientCurrency selectedValue = currencyCombo.getSelectedValue();
			listener.currencyChanged(selectedValue, factor);

		}
	}

	public void currencyChanged(ClientCurrency selectItem) {
		this.selectedCurrencyItem = selectItem;
		boolean showFactorField = baseCurrency == null ? true : selectItem
				.equals(baseCurrency);

		showFactorField(showFactorField);
		if (listener != null) {
			listener.currencyChanged(selectedCurrencyItem, 1);
		}
	}

	private void showFactorField(boolean showFactorField) {
		factorField.setDisabled(showFactorField);
		updateFactorFieldTitle(); // 1<SELCTED currency>=
		double factor = 1;/*
						 * factorFieldDisableStatus ? 1 : getFactorByRPC(
						 * selectItem, baseCurrency);
						 */
		factorField.setValue(String.valueOf(factor));

		// hide or show factor fields
		if (showFactorField) {
			// hide the fields
			remove(factorField);
			remove(baseCurrencyLbl);
		} else {
			// show the fields
			if (!getFormItems().contains(factorField)) {
				setFields(factorField, baseCurrencyLbl);
			}
		}

	}

	private double getFactorByRPC(String foreginCurrencyCode,
			String baseCurrencyCode) {
		// TODO GET currency factor BY RPC
		return 0;
	}

	public void setListener(CurrencyChangeListener listener) {
		this.listener = listener;
	}

	public void setCurrencies(List<ClientCurrency> currencies) {
		this.currencies = currencies;
		currencyCombo.initCombo(this.currencies);
	}

	private void updateFactorFieldTitle() {
		ClientCurrency currency = currencyCombo.getSelectedValue();
		StringBuffer sb = new StringBuffer();
		sb.append(' ').append(1).append(currency.getFormalName()).append('=');
		factorField.setTitle(sb.toString());
	}

	public void setSelectedCurrency(ClientCurrency clientCurrency) {
		currencyCombo.setSelected(clientCurrency.getFormalName());
		boolean showFactorField = baseCurrency == null ? true : clientCurrency
				.equals(baseCurrency);
		showFactorField(showFactorField);

	}

	public ClientCurrency getSelectedCurrency() {
		return currencyCombo.getSelectedValue();
	}

	public void setCurrencyFactor(double factor) {
		factorField.setValue(String.valueOf(factor));
	}

	public double getCurrencyFactor() {
		return Double.parseDouble(factorField.getValue().toString());
	}

	public void setTabIndex(int index) {
		currencyCombo.setTabIndex(index);

	}
}
