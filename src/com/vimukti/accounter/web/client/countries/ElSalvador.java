package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class ElSalvador extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ahuachapán", "Cabañas",
				"Chalatenango", "Cuscatlán", "La Libertad", "La Paz",
				"La Unión", "Morazán", "San Miguel", "San Salvador",
				"Santa Ana", "San Vicente", "Sonsonate", "Usulután" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SVC";
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
		return "UTC-6:00 America/El_Salvador";
	}

}
