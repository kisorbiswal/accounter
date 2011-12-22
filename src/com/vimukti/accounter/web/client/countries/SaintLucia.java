package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class SaintLucia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Anse-la-Raye", "Canaries",
				"Castries", "Choiseul", "Dennery", "Gros Inlet", "Laborie",
				"Micoud", "Soufriere", "Vieux Fort" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XCD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/St_Lucia";
	}

}
