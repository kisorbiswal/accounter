package com.vimukti.accounter.web.client.ui.widgets;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CurrencyWidget extends DynamicForm {

	private CurrencyChangeListener listener;

	private TextItem factorField;
	private CurrencyCombo currencyCombo;
	private LabelItem baseCurrencyLbl;

	private ClientCurrency baseCurrency;
	private List<ClientCurrency> currencies;

	public CurrencyWidget(List<ClientCurrency> currencies,
			final ClientCurrency baseCurrency) {
		this.currencies = currencies;
		this.baseCurrency = baseCurrency;
		setNumCols(3);

		currencyCombo = new CurrencyCombo("Currency : ");
		currencyCombo.initCombo(currencies);

		currencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {

					@Override
					public void selectedComboBoxItem(ClientCurrency selectItem) {
						currencyChanged(selectItem);

					}

				});

		factorField = new TextItem();
		factorField.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getSource() == CurrencyWidget.this.factorField) {
					String factorStr = CurrencyWidget.this.factorField
							.getValue().toString();
					factorFieldChagned(Double.parseDouble(factorStr));
				}
			}

		});
		baseCurrencyLbl = new LabelItem();
		baseCurrencyLbl.setTitle(baseCurrency.getName());
		setFields(currencyCombo, factorField, baseCurrencyLbl);
	}

	private void factorFieldChagned(double factor) {
		if (listener != null) {
			listener.currencyChanged(currencyCombo.getSelectedValue(), factor);
		}
	}

	private void currencyChanged(ClientCurrency selectItem) {

		boolean factorFieldDisableStatus = selectItem.getName().equals(
				baseCurrency.getName());
		factorField.setDisabled(factorFieldDisableStatus);
		updateFactorFieldTitle(); // 1<SELCTED currency>=
		double factor = getFactorByRPC(selectItem.getName(),
				baseCurrency.getName());
		factorField.setValue(String.valueOf(factor));
		if (listener != null) {
			listener.currencyChanged(selectItem, factor);
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
		currencyCombo.initCombo(currencies);
	}

	private void updateFactorFieldTitle() {
		ClientCurrency currency = currencyCombo.getSelectedValue();

		StringBuffer sb = new StringBuffer();
		sb.append(' ').append(1).append(currency.getName()).append('=');
		factorField.setTitle(sb.toString());
	}

	public void setSelectedCurrency(ClientCurrency clientCurrency) {
		currencyCombo.setSelected(clientCurrency.getName());
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
}
