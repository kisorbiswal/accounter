package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Peru extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Ancash", "Apurimac",
				"Arequipa", "Ayacucho", "Cajamarca", "Callao", "Cusco",
				"Huancavelica", "Huanuco", "Ica", "Junin", "La Libertad",
				"Lambayeque", "Lima Provincias", "Loreto", "Madre de Dios",
				"Moquegua", "Pasco", "Piura", "Puno", "San Martin", "Tacna",
				"Tumbes", "Ucayali" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "PEN";
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
