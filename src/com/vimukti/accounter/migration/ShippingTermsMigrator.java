package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.ShippingTerms;

public class ShippingTermsMigrator implements IMigrator<ShippingTerms> {

	@Override
	public JSONObject migrate(ShippingTerms obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject);
		jsonObject.put("name", obj.getName());
		jsonObject.put("description", obj.getDescription());
		return jsonObject;
	}
}