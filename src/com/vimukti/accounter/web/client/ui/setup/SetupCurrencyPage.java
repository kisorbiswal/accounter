package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.CustomLabel;

public class SetupCurrencyPage extends AbstractSetupPage {
	public static final String[] currencyList = new String[] {};
	private ListBox baseCurrencyList;

	@Override
	public String getHeader() {
		return accounterConstants.setSupportedCurrencies();
	}

	@Override
	public VerticalPanel getPageBody() {
		VerticalPanel container = new VerticalPanel();
		HorizontalPanel baseCurrencyHorPanel = new HorizontalPanel();
		container.add(baseCurrencyHorPanel);

		// CustomLabel baseCurrencyLabel = new CustomLabel(
		// accounterConstants.primaryCurrency());
		// baseCurrencyHorPanel.add(baseCurrencyLabel);
		//
		// baseCurrencyList = new ListBox();
		// for (int i = 0; i < currencyList.length; i++)
		// baseCurrencyList.addItem(currencyList[i]);
		//
		// baseCurrencyHorPanel.add(baseCurrencyList);
		//
		// CustomLabel supportingCurrenciesLabel = new CustomLabel(
		// accounterConstants.supportingCurrencies());
		// container.add(supportingCurrenciesLabel);
		//
		// // add currencies grid
		// CurrenciesGrid currenciesGrid = new CurrenciesGrid();
		// TODO:: create list of currencies and add them to the grid

		// container.add(currenciesGrid);

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
	protected void onBack() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNext() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
