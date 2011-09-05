package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class Argentina implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Buenos Aires", "Catamarca", "Chaco",
				"Chubut", "Córdoba", "Corrientes", "Distrito Federal",
				"Entre Ríos", "Formosa", "Jujuy", "La Pampa", "La Rioja",
				"Mendoza", "Misiones", "Neuquén", "Río Negro", "Salta",
				"San Juan", "San Luis", "Santa Cruz", "Santa Fé",
				"Santiago del Estero", "Tierra del Fuego", "Tucumán" };
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
