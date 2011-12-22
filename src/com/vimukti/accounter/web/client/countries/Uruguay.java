package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Uruguay extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Artigas", "Canelones", "Cerro Largo",
				"Colonia", "Durazno", "Flores", "Florida", "Lavalleja",
				"Maldonado", "Montevideo", "Paysandu", "Rio Negro", "Rivera",
				"Rocha", "Salto", "San Jose", "Soriano", "Tacuarembo",
				"Treinta y Tres" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "UYU";
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
		return "UTC-3:00 America/Montevideo";
	}

}
