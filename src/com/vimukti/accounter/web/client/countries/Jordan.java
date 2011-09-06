package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Jordan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "'Ajlun", "al-'Aqabah", "al-Balqa'",
				"al-Karak", "al-Mafraq", "'Amman", "at-Tafilah", "az-Zarqa'",
				"Irbid", "Jaraš", "Ma'an", "Madaba" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "JOD";
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
