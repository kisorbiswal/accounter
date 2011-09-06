package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Croatia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bjelovar-Bilogora",
				"Dubrovnik-Neretva", "Grad Zagreb", "Istra", "Karlovac",
				"Koprivnica-Križevci", "Krapina-Zagorje", "Lika-Senj",
				"Medimurje", "Osijek-Baranja", "Požega-Slavonija",
				"Primorje-Gorski Kotar", "Šibenik-Knin", "Sisak-Moslavina",
				"Slavonski Brod-Posavina", "Split-Dalmacija", "Varaždin",
				"Virovitica-Podravina", "Vukovar-Srijem", "Zadar", "Zagreb" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "HRK";
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
