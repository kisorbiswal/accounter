package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Yemen extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abyan", "ad-Dali'", "Aden",
				"al-Bayda", "al-H?udaydah", "al-Jawf", "al-Mahrah",
				"al-Mahwit", "Amanah al-'Asmah", "Amran", "Ðamar", "Hadramaut",
				"Hajjah", "Ibb", "Lahij", "Ma'rib", "Raymah", "Šabwah",
				"Sa'dah", "San'a", "Ta'izz" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "YER";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().january();
	}



	@Override
	public boolean isVatAvailable() {
		return false;
	}


	@Override
	public boolean isSalesTaxAvailable() {
		return true;
	}


	@Override
	public boolean isServiceTaxAvailable() {
		return false;
	}

	
}
