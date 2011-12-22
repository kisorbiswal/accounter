package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Venezuela extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Anzoategui", "Apure",
				"Aragua", "Barinas", "Bolivar", "Carabobo", "Cojedes",
				"Delta Amacuro", "Distrito Capital", "Falcon", "Guarico",
				"Lara", "Merida", "Miranda", "Monagas", "Nueva Esparta",
				"Portuguesa", "Sucre", "Tachira", "Trujillo", "Vargas",
				"Yaracuy", "Zulia" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "VEF";
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
		return "UTC-4:30 America/Caracas";
	}

}
