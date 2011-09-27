package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Poland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Dolnoslaskie", "Kujawsko-Pomorskie",
				"Lodzkie", "Lubelskie", "Lubuskie", "Malopolskie",
				"Mazowieckie", "Opolskie", "Podkarpackie", "Podlaskie",
				"Pomorskie", "Slaskie", "Swietokrzyskie",
				"Warminsko-Mazurskie", "Wielkopolskie", "Zachodnio-Pomorskie" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PLZ";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
