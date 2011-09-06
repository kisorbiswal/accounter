package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Botswana extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bobonong", "Boteti", "Chobe",
				"Francistown", "Gaborone", "Ghanzi", "Jwaneng",
				"Kgalagadi North", "Kgalagadi South", "Kgatleng", "Kweneng",
				"Lobatse", "Mahalapye", "Ngamiland", "Ngwaketse", "North East",
				"Okavango", "Orapa", "Selibe Phikwe", "Serowe-Palapye",
				"South East", "Sowa", "Tutume" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "BWP";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().april();
	}

}
