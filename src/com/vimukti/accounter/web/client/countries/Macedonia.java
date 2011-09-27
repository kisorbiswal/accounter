package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Macedonia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Berovo", "Bitola", "Brod", "Debar",
				"Delcevo", "Demir Hisar", "Gevgelija", "Gostivar", "Kavadarci",
				"Kicevo", "Kocani", "Kratovo", "Kriva Palanka", "Krusevo",
				"Kumanovo", "Negotino", "Ohrid", "Prilep", "Probistip",
				"Radovis", "Resen", "Skopje", "Stip", "Struga", "Strumica",
				"Sveti Nikole", "Tetovo", "Valandovo", "Veles", "Vinica" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MKD";
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
