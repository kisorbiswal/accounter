package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Iceland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Austurland", "Höfuðborgarsvæði",
				"Norðurland eystra", "Norðurland vestra", "Suðurland",
				"Suðurnes", "Vestfirðir", "Vesturland" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ISK";
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
