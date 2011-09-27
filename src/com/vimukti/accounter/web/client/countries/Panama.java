package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Panama extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bocas del Toro", "Chiriqui", "Cocle",
				"Colon", "Darien", "Embera", "Herrera", "Kuna de Madungandi",
				"Kuna de Wargandi", "Kuna Yala", "Los Santos", "Ngobe Bugle",
				"Panama", "Veraguas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PAB";
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
