package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.ShippingMethod;

public class ShippingMethodMigrator implements IMigrator<ShippingMethod> {

	@Override
	public JSONObject migrate(ShippingMethod obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject);
		jsonObject.put("name", obj.getName());
		jsonObject.put("description", obj.getDescription());
		return jsonObject;
	}
}