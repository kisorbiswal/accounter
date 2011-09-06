package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CzechRepublic extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Jihoceský", "Jihomoravský",
				"Karlovarský", "Královéhradecký", "Liberecký",
				"Moravskoslezský", "Olomoucký", "Pardubický", "Plzenský",
				"Prag", "Stredoceský", "Ústecký", "Vysocina", "Zlínský" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CSK";
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
