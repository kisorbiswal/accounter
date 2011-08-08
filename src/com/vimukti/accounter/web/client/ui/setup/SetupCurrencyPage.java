package com.vimukti.accounter.web.client.ui.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomLabel;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.CurrenciesGrid;

public class SetupCurrencyPage extends AbstractSetupPage {
	// public static final String[] currencyList = new String[] {};
	private CurrencyCombo baseCurrencyListCombo;
	private List<ClientCurrency> clientCurrenciesList;
	private Set<ClientCurrency> clientCurrenciesSet;

	@Override
	public String getHeader() {
		return accounterConstants.setSupportedCurrencies();
	}

	@Override
	public VerticalPanel getPageBody() {
		VerticalPanel container = new VerticalPanel();
		DynamicForm currencyformDynamicForm = new DynamicForm();
		// HorizontalPanel baseCurrencyHorPanel = new HorizontalPanel();

		// CustomLabel baseCurrencyLabel = new CustomLabel("Primary Currency");
		// baseCurrencyHorPanel.add(baseCurrencyLabel);
		clientCurrenciesList = new ArrayList<ClientCurrency>();
		clientCurrenciesSet = Accounter.getCompany().getCurrencies();
		clientCurrenciesList.addAll(clientCurrenciesSet);
		baseCurrencyListCombo = new CurrencyCombo("Primary Currency");
		baseCurrencyListCombo.initCombo(clientCurrenciesList);

		currencyformDynamicForm.setFields(baseCurrencyListCombo);
		container.add(currencyformDynamicForm);
		
		CustomLabel supportingCurrenciesLabel = new CustomLabel(
				"Supporting Currencies");
		container.add(supportingCurrenciesLabel);

		// add currencies grid
		CurrenciesGrid currenciesGrid = new CurrenciesGrid();
		// TODO:: create list of currencies and add them to the grid

		container.add(currenciesGrid);

		return container;
	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doShow() {
		return true;
	}

}
