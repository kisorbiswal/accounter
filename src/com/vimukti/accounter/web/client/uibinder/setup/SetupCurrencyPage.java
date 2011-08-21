/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.grids.CurrenciesGrid;

/**
 * @author Administrator
 * 
 */
public class SetupCurrencyPage extends AbstractSetupPage {

	private static SetupCurrencyPageUiBinder uiBinder = GWT
			.create(SetupCurrencyPageUiBinder.class);
	@UiField
	Label headerLabel;
	@UiField
	Label primaryCurrenyLabel;
	@UiField
	ListBox primaryCurrencyListBox;
	@UiField
	VerticalPanel currencyListGridPanel;
	private CurrenciesGrid currenciesGrid;
	// private Set<ClientCurrency> currencySet;
	private List<ClientCurrency> currenciesList;

	interface SetupCurrencyPageUiBinder extends
			UiBinder<Widget, SetupCurrencyPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupCurrencyPage() {
		initWidget(uiBinder.createAndBindUi(this));
		currenciesList = new ArrayList<ClientCurrency>();
		createControls();
	}

	@Override
	protected void onLoad() {
		primaryCurrencyListBox.setSelectedIndex(currenciesList
				.indexOf(preferences.getBaseCurrency()));
	}

	@Override
	protected void onSave() {
		if (primaryCurrencyListBox.getSelectedIndex() != -1)
			preferences.setPrimaryCurrency(currenciesList
					.get(primaryCurrencyListBox.getSelectedIndex()));
	}

	@Override
	protected void createControls() {
		// currencySet = Accounter.getCompany().getCurrencies();
		currenciesList = UIUtils.getCurrenciesList();
		headerLabel.setText(accounterConstants.setSupportedCurrencies());
		primaryCurrenyLabel.setText(accounterConstants.primaryCurrency());
		// currenciesList.addAll(currencySet);

		for (int i = 0; i < currenciesList.size(); i++) {
			primaryCurrencyListBox.addItem(currenciesList.get(i).getName());
		}

		currenciesGrid = new CurrenciesGrid();
		currenciesGrid.init();
		currenciesGrid.setRecords(UIUtils.getCurrenciesList());
		currencyListGridPanel.add(currenciesGrid);
	}

	@Override
s	public boolean canShow() {
		return true;
	}

}
