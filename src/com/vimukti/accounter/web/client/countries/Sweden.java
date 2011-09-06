package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Sweden extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Blekinge", "Dalarna", "Gävleborg",
				"Gotland", "Halland", "Jämtland", "Jönköping", "Kalmar",
				"Kronoberg", "Norrbotten", "Örebro", "Östergötland", "Skåne",
				"Södermanland", "Stockholm", "Uppsala", "Värmland",
				"Västerbotten", "Västernorrland", "Västmanland",
				"Västra Götaland" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SEK";
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
