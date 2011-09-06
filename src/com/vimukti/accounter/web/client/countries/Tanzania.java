package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Tanzania extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Arusha", "Dar es Salaam", "Dodoma",
				"Iringa", "Kagera", "Kigoma", "Kilimanjaro", "Lindi",
				"Manyara", "Mara", "Mbeya", "Morogoro", "Mtwara", "Mwanza",
				"Pwani", "Rukwa", "Ruvuma", "Shinyanga", "Singida", "Tabora",
				"Tanga", "Zanzibar and Pemba" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TZS";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().july();
	}

}
