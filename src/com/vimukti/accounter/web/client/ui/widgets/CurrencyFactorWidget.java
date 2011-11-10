package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class CurrencyFactorWidget extends DynamicForm {

	private CurrencyChangeListener listener;
	private ClientCurrency baseCurrency, selectedCurrencyItem;
	private boolean showFactorField = true;
	private CurrencyFormItem currencyForm;

	public CurrencyFactorWidget(ClientCurrency baseCurrency) {
		this.baseCurrency = baseCurrency;

		setStyleName("currencyTextBox");

		currencyForm = new CurrencyFormItem("Factor :",
				baseCurrency.getFormalName());

		currencyForm.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String factorStr = currencyForm.getValue().toString();
				factorFieldChagned(Double.parseDouble(factorStr));
			}
		});

		setFields(currencyForm);
		currencyForm.hide();

	}

	private void factorFieldChagned(double factor) {
		if (listener != null) {
			listener.currencyChanged(selectedCurrencyItem, factor);

		}
	}

	public void currencyChanged(ClientCurrency selectItem) {
		this.selectedCurrencyItem = selectItem;
		setShowFactorField(selectItem.equals(baseCurrency));

		showFactorField(isShowFactorField());
		if (listener != null) {
			listener.currencyChanged(selectedCurrencyItem, 1);
		}
	}

	private void showFactorField(boolean showFactorField) {
		currencyForm.setDisabled(showFactorField);
		if (selectedCurrencyItem == null) {
			selectedCurrencyItem = Accounter.getCompany().getPreferences()
					.getPrimaryCurrency();
		}
		updateFactorFieldTitle(); // 1<SELCTED currency>=
		double factor = 1;/*
						 * factorFieldDisableStatus ? 1 : getFactorByRPC(
						 * selectItem, baseCurrency);
						 */
		currencyForm.setValue(String.valueOf(factor));

		// hide or show factor fields
		if (showFactorField) {
			currencyForm.hide();
		} else {
			currencyForm.show();
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

	private void updateFactorFieldTitle() {
		StringBuffer sb = new StringBuffer();
		sb.append(1).append(" ").append(selectedCurrencyItem.getFormalName())
				.append(" =");
		currencyForm.setTitle(sb.toString());
	}

	public void setSelectedCurrency(ClientCurrency clientCurrency) {
		setShowFactorField(clientCurrency.equals(baseCurrency));
		selectedCurrencyItem = clientCurrency;
		showFactorField(showFactorField);
	}

	public ClientCurrency getSelectedCurrency() {
		return selectedCurrencyItem;
	}

	public void setCurrencyFactor(double factor) {
		currencyForm.setValue(String.valueOf(factor));
	}

	public double getCurrencyFactor() {
		if (currencyForm.getValue().length() > 0)
			return Double.parseDouble(currencyForm.getValue().toString());
		else
			return 1;
	}

	public void setTabIndex(int index) {
		// currencyCombo.setTabIndex(index);
	}

	public boolean isShowFactorField() {
		return showFactorField;
	}

	public void setShowFactorField(boolean showFactorField) {
		this.showFactorField = showFactorField;
	}

}
