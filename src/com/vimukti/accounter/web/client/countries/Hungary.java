package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Hungary extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bács-Kiskun", "Baranya", "Békés",
				"Borsod-Abaúj-Zemplén", "Budapest", "Csongrád", "Fejér",
				"Gyor-Moson-Sopron", "Hajdú-Bihar", "Heves",
				"Jász-Nagykun-Szolnok", "Komárom-Esztergom", "Nógrád", "Pest",
				"Somogy", "Szabolcs-Szatmár-Bereg", "Tolna", "Vas", "Veszprém",
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
