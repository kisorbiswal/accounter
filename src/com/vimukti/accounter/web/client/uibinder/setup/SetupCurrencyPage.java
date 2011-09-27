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
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.grids.CurrenciesGrid;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;

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
	// @UiField
	VerticalPanel currencyListGridPanel;
	private CurrenciesGrid currenciesGrid;
	// private Set<ClientCurrency> currencySet;
	private List<ClientCurrency> currenciesList = new ArrayList<ClientCurrency>();

	private String selectedCuntry;

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
		createControls();
	}

	@Override
	protected void createControls() {
		// currenciesList = UIUtils.getCurrenciesList();
		headerLabel.setText(accounterConstants.selectCurrency());
		primaryCurrenyLabel.setText(accounterConstants.primaryCurrency());
		currenciesList = CoreUtils.getCurrencies();
		for (ClientCurrency currency : currenciesList) {
			primaryCurrencyListBox.addItem(currency.getFormalName() + "\t"
					+ currency.getDisplayName());
		}
		currenciesGrid = new CurrenciesGrid();
		currenciesGrid.init();
		currenciesGrid.setRecords(currenciesList);
		// currencyListGridPanel.add(currenciesGrid);
	}

	@Override
	public void setCountryChanges() {
		if (getCountry() != null) {
			for (int i = 0; i < currenciesList.size(); i++) {
				if (CountryPreferenceFactory.get(getCountry())
						.getPreferredCurrency().equals(
								currenciesList.get(i).getFormalName())) {
					primaryCurrencyListBox.setSelectedIndex(i);
				}
			}
		}
	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected void onLoad() {
		setCountryChanges();
		ClientCurrency primaryCurrency = preferences.getPrimaryCurrency();
		String country = Accounter.getCompany().getTradingAddress()
				.getCountryOrRegion();
		if (primaryCurrency == null || !selectedCuntry.equals(country)) {
			this.selectedCuntry = country;
			for (int index = 0; index < currenciesList.size(); index++) {
				// if (currenciesList.get(index).getCountryName()
				// .equals(selectedCuntry)) {
				// primaryCurrencyListBox.setSelectedIndex(index);
				// }
			}
		}
	}

	@Override
	protected void onSave() {
		if (primaryCurrencyListBox.getSelectedIndex() != -1) {
			preferences.setPrimaryCurrency(currenciesList
					.get(primaryCurrencyListBox.getSelectedIndex()));
		}
	}

	@Override
	protected boolean validate() {
		if (primaryCurrencyListBox.getSelectedIndex() == -1) {
			Accounter.showError(accounterMessages
					.pleaseselectvalidtransactionGrid(accounterConstants
							.currency()));
			return false;
		} else {
			return true;
		}
	}

}
