package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Currency;

public class CurrencyMigrator implements IMigrator<Currency> {

	@Override
	public JSONObject migrate(Currency obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", obj.getName());
		jsonObject.put("symbol", obj.getSymbol());
		return jsonObject;
	}

}
