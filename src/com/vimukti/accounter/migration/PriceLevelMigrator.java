package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PriceLevel;

public class PriceLevelMigrator implements IMigrator<PriceLevel> {

	@Override
	public JSONObject migrate(PriceLevel obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject);
		jsonObject.put("name", obj.getName());
		return jsonObject;
	}
}