package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Madagascar extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Antananarivo", "Antsiranana",
				"Fianarantsoa", "Mahajanga", "Toamasina", "Toliary" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MGF";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+3:00 Indian/Antananarivo";
	}

}
