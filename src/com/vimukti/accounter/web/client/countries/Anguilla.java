package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Anguilla extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		return null;
	}

	@Override
	public String getPreferredCurrency() {

		return "XCD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {

		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.messages().april();
	}

}
