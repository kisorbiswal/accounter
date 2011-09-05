package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class Brazil implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Acre", "Alagoas", "Amapá",
				"Amazonas", "Bahia", "Ceará", "Distrito Federal",
				"Espírito Santo", "Goiás", "Maranhão", "Mato Grosso",
				"Mato Grosso do Sul", "Minas Gerais", "Pará", "Paraíba",
				"Paraná", "Pernambuco", "Piauí", "Rio de Janeiro",
				"Rio Grande do Norte", "Rio Grande do Sul", "Rondônia",
				"Roraima", "Santa Catarina", "São Paulo", "Sergipe",
				"Tocantins" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganizationType[] getOrganizationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		// TODO Auto-generated method stub
		return null;
	}

}
