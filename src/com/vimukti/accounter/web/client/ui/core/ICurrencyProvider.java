package com.vimukti.accounter.web.client.ui.core;

public interface ICurrencyProvider {

	public Double getAmountInTransactionCurrency(Double amount);

	public Double getAmountInBaseCurrency(Double amount);
}
