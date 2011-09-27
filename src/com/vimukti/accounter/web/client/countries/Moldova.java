package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Moldova extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Anenii Noi", "Balti", "Basarabeasca",
				"Briceni", "Cahul", "Calarasi", "Camenca", "Cantemir",
				"Causeni", "Chisinau", "Cimislia", "Criuleni", "Donduseni",
				"Drochia", "Dubasari municipiu", "Edinet", "Falesti",
				"Floresti", "Gagauzia", "Glodeni", "Grigoriopol", "Hincesti",
				"Ialoveni", "Leova", "Nisporeni", "Ocnita", "Orhei", "Rezina",
				"Ribnita", "Riscani", "Singerei", "Slobozia", "Soldanesti",
				"Soroca", "Stefan Voda", "Straseni", "Taraclia", "Telenesti",
				"Tighina", "Tiraspol", "Ungheni" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MDL";
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
