package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class KoreaNorth extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Chagangdo", "Hamgyongbukto",
				"Hamgyongnamdo", "Hwanghaebukto", "Hwanghaenamdo", "Kangwon",
				"Pyonganbukto", "Pyongannamdo", "Pyongyang", "Rason",
				"Yanggang" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KPW";
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
