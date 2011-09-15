package com.vimukti.accounter.web.client.ui.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomLabel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.CurrenciesGrid;

public class SetupCurrencyPage extends AbstractSetupPage {
	// public static final String[] currencyList = new String[] {};
	private CurrencyCombo baseCurrencyListCombo;
	private List<ClientCurrency> clientCurrenciesList;
	private Set<ClientCurrency> clientCurrenciesSet;
	private CurrenciesGrid currenciesGrid;

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
		baseCurrencyListCombo = new CurrencyCombo(Accounter.constants()
				.primaryCurrency());
		baseCurrencyListCombo.initCombo(UIUtils.getCurrenciesList());

		currenciesGrid = new CurrenciesGrid();
		currenciesGrid.init();
		currenciesGrid.setRecords(UIUtils.getCurrenciesList());

		baseCurrencyListCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {

					@Override
					public void selectedComboBoxItem(ClientCurrency selectItem) {
						// currenciesGrid.setPrimaryCurrency(
						// baseCurrencyListCombo
						// .getSelectedValue());
					}
				});
		currencyformDynamicForm.setFields(baseCurrencyListCombo);
		container.add(currencyformDynamicForm);

		CustomLabel supportingCurrenciesLabel = new CustomLabel(
				"Supporting Currencies");
		container.add(supportingCurrenciesLabel);

		// add currencies grid

		// TODO:: create list of currencies and add them to the grid

		container.add(currenciesGrid);
		container.addStyleName("setuppage_body");
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

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
