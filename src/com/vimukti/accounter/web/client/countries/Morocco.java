package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Morocco extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Casablanca", "Chaouia-Ouardigha",
				"Doukkala-Abda", "Fes-Boulemane", "Gharb-Chrarda-Beni Hssen",
				"Guelmim", "Marrakech-Tensift-Al Haouz", "Meknes-Tafilalet",
				"Oriental", "Rabat-Sale-Zammour-Zaer", "Souss Massa-Draa",
				"Tadla-Azilal", "Tangier-Tetouan", "Taza-Al Hoceima-Taounate" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MAD";
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
