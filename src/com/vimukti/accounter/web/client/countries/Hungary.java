package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Hungary extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bacs-Kiskun", "Baranya", "Bekes",
				"Borsod-Abauj-Zemplen", "Budapest", "Csongrad", "Fejer",
				"Gyor-Moson-Sopron", "Hajdu-Bihar", "Heves",
				"Jász-Nagykun-Szolnok", "Komarom-Esztergom", "Nograd", "Pest",
				"Somogy", "Szabolcs-Szatmar-Bereg", "Tolna", "Vas", "Veszprem",
				"Zala" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "HUF";
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
