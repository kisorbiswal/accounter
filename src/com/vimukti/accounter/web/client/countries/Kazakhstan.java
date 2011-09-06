package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Kazakhstan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Akmechet", "Almaty", "Aqmola",
				"Aqtöbe", "Atyrau", "Batis Kazakstan", "Mankistau",
				"Ontüstik Kazakstan", "Pavlodar", "Qaragandy", "Qostanay",
				"Sigis Kazakstan", "Soltüstik Kazakstan", "Taraz" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KZT";
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
