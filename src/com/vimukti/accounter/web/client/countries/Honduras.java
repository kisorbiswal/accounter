package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Honduras extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Atlantida", "Choluteca", "Colon",
				"Comayagua", "Copan", "Cortes", "Distrito Central",
				"El Paraíso", "Francisco Morazan", "Gracias a Dios",
				"Intibuca", "Islas de la Bahia", "La Paz", "Lempira",
				"Ocotepeque", "Olancho", "Santa Barbara", "Valle", "Yoro" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "HNL";
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
		return "UTC-6:00 America/Tegucigalpa";
	}

}
