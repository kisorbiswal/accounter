package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class CurrencyExchangeRateTable extends EditTable<CurrencyExchangeRate> {

	@Override
	protected void initColumns() {
		addColumn(new TextEditColumn<CurrencyExchangeRate>() {
			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.currency();
			}

			@Override
			protected String getValue(CurrencyExchangeRate row) {
				return row.getCurrencyName();
			}

			@Override
			protected void setValue(CurrencyExchangeRate row, String value) {
				// TODO Auto-generated method stub

			}
		});

		addColumn(new AmountColumn<CurrencyExchangeRate>(null, false) {

			@Override
			protected String getColumnName() {
				return messages.exchangeRate();
			}

			@Override
			protected Double getAmount(CurrencyExchangeRate row) {
				return row.getExchangeRate();
			}

			@Override
			protected void setAmount(CurrencyExchangeRate row, Double value) {
				row.setExchangeRate(value);
			}
		});
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
