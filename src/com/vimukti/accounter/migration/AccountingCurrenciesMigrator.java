package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Currency;

public class AccountingCurrenciesMigrator implements
		IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject commonSettings = new JSONObject();
		commonSettings.put("id", context.get(CompanyMigrator.COMMON_SETTINGS,
				CompanyMigrator.COMMON_SETTINGS_OLD_ID));
		JSONArray currencies = new JSONArray();
		for (Currency currency : context.getCompany().getCurrencies()) {
			JSONObject currencyJson = new JSONObject();
			currencyJson.put("identity", currency.getFormalName());
			currencies.put(currencyJson);
		}
		commonSettings.put("accountingCurrencies", currencies);
		return commonSettings;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}
