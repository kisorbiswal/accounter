package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class China extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Anhui", "Aomen", "Beijing",
				"Chongqing", "Fujian", "Gansu", "Guangdong", "Guangxi",
				"Guizhou", "Hainan", "Hebei", "Heilongjiang", "Henan",
				"Hongkong", "Hubei", "Hunan", "Jiangsu", "Jiangxi", "Jilin",
				"Liaoning", "Neimenggu", "Ningxia", "Qinghai", "Shaanxi",
				"Shandong", "Shanghai", "Shanxi", "Sichuan", "Tianjin",
				"Xinjiang", "Xizang", "Yunnan", "Zhejiang" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CNY";
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
