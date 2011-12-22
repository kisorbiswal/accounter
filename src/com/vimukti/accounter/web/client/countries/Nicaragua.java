package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Nicaragua extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Atlantico Norte", "Atlantico Sur",
				"Boaco", "Carazo", "Chinandega", "Chontales", "Esteli",
				"Granada", "Jinotega", "Leon", "Madriz", "Managua", "Masaya",
				"Matagalpa", "Nueva Segovia", "Río San Juan", "Rivas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "Nicaragua";
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
		return "UTC-6:00 America/Managua";
	}

}
