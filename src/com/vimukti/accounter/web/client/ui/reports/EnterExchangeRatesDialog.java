package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class EnterExchangeRatesDialog extends BaseDialog {

	private CurrencyExchangeRateTable table;
	private DateItem enteredDateItem;

	public EnterExchangeRatesDialog() {
		super(messages.enterExchangeRates());
		createControls();
		center();
	}

	private void createControls() {
		this.enteredDateItem = new DateItem(messages.enterDate());
		enteredDateItem.setEnteredDate(new ClientFinanceDate());
		enteredDateItem.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				updateExchangeRates(date);
			}
		});
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setFields(enteredDateItem);
		updateExchangeRates(enteredDateItem.getDate());
		this.table = new CurrencyExchangeRateTable();
		this.table.addStyleName("exchange-table");
		VerticalPanel panel = new VerticalPanel();
		panel.add(dynamicForm);
		panel.add(table);
		setBodyLayout(panel);
	}

	protected void updateExchangeRates(ClientFinanceDate date) {
		AccounterAsyncCallback<List<CurrencyExchangeRate>> callback = new AccounterAsyncCallback<List<CurrencyExchangeRate>>() {

			@Override
			public void onException(AccounterException exception) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResultSuccess(List<CurrencyExchangeRate> result) {
				if (table != null) {
					if (result == null && result.isEmpty()) {
						return;
					} else {
						table.setAllRows(result);
					}
				}
			}
		};
		Accounter.createReportService().getExchangeRatesOfDate(date.getDate(),
				callback);
	}

	@Override
	protected boolean onOK() {
		UnRealisedExchangeLossesAndGainsAction action = new UnRealisedExchangeLossesAndGainsAction();
		List<CurrencyExchangeRate> allExchangeRates = table.getAllRows();
		Map<Long, Double> map = new HashMap<Long, Double>();
		for (CurrencyExchangeRate rate : allExchangeRates) {
			map.put(rate.getCurrencyId(), rate.getExchangeRate());
		}
		action.setEnterDate(enteredDateItem.getDate());
		action.run(map, false);
		return true;
	}

	@Override
	public void setFocus() {

	}

}
