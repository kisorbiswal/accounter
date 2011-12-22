package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class MarshallIslands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ailinlaplap", "Ailuk", "Arno", "Aur",
				"Bikini", "Ebon", "Enewetak", "Jabat", "Jaluit", "Kili",
				"Kwajalein", "Lae", "Lib", "Likiep", "Majuro", "Maloelap",
				"Mejit", "Mili", "Namorik", "Namu", "Rongelap", "Ujae",
				"Utrik", "Wotho", "Wotje" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.october();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		if (state.equals("Kwajalein")) {
			return "UTC+12:00 Pacific/Kwajalein";
		} else {
			return "UTC+12:00 Pacific/Majuro";
		}
	}

}
