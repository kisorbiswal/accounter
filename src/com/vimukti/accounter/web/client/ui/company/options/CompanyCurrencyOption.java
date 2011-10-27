/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.CoreUtils;

/**
 * @author Administrator
 * 
 */
public class CompanyCurrencyOption extends AbstractPreferenceOption {

	@UiField
	Label primaryCurrenyLabel;
	@UiField
	ListBox primaryCurrencyListBox;
	@UiField
	CheckBox isEnableMultiCurrencyCheckBox;

	private List<ClientCurrency> currenciesList = new ArrayList<ClientCurrency>();

	private static CompanyCurrencyOptionUiBinder uiBinder = GWT
			.create(CompanyCurrencyOptionUiBinder.class);

	interface CompanyCurrencyOptionUiBinder extends
			UiBinder<Widget, CompanyCurrencyOption> {
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
	public CompanyCurrencyOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public CompanyCurrencyOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public void createControls() {
		primaryCurrenyLabel.setText(constants.primaryCurrency());
		currenciesList = CoreUtils.getCurrencies(getCompany().getCurrencies());
		for (ClientCurrency currency : currenciesList) {
			primaryCurrencyListBox.addItem(currency.getFormalName() + "\t"
					+ currency.getDisplayName());
		}
		isEnableMultiCurrencyCheckBox
				.setText(constants.isMultiCurrencyEnable());
	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

	@Override
	public String getTitle() {
		return constants.currency();
	}

	@Override
	public void initData() {
		for (int i = 0; i < currenciesList.size(); i++) {
			if (getCompanyPreferences().getPrimaryCurrency() != null) {
				if (getCompanyPreferences().getPrimaryCurrency().equals(
						currenciesList.get(i).getFormalName())) {
					primaryCurrencyListBox.setSelectedIndex(i);
				}
			} else {
				if (getCompany().getCountryPreferences().getPreferredCurrency()
						.equals(currenciesList.get(i).getFormalName())) {
					primaryCurrencyListBox.setSelectedIndex(i);
				}
			}

		}
		isEnableMultiCurrencyCheckBox.setValue(getCompanyPreferences()
				.isEnableMultiCurrency());
	}

	@Override
	public void onSave() {
		if (primaryCurrencyListBox.getSelectedIndex() != -1) {
			getCompanyPreferences().setPrimaryCurrency(
					currenciesList.get(
							primaryCurrencyListBox.getSelectedIndex())
							.getFormalName());
		}
		getCompanyPreferences().setEnableMultiCurrency(
				isEnableMultiCurrencyCheckBox.getValue());
	}

}
