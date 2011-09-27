package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Brazil extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Acre", "Alagoas", "Amapa",
				"Amazonas", "Bahia", "Ceara", "Distrito Federal",
				"Espirito Santo", "Goias", "Maranhao", "Mato Grosso",
				"Mato Grosso do Sul", "Minas Gerais", "Para", "Paraiba",
				"Parana", "Pernambuco", "Piaui", "Rio de Janeiro",
				"Rio Grande do Norte", "Rio Grande do Sul", "Rondonia",
				"Roraima", "Santa Catarina", "Sao Paulo", "Sergipe",
				"Tocantins" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BRR";
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
