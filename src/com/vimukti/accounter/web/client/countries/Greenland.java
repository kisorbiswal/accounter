package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Greenland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aasiaat", "Ammassalik",
				"Illoqqortoormiut", "Ilulissat", "Ivittuut", "Kangaatsiaq",
				"Maniitsoq", "Nanortalik", "Narsaq", "Nuuk", "Paamiut",
				"Qaanaaq", "Qaqortoq", "Qasigiannguit", "Qeqertarsuaq",
				"Sisimiut", "Udenfor kommunal inddeling", "Upernavik",
				"Uummannaq"

		};
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "DKK";
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
