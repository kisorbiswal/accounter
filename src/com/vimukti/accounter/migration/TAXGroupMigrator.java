package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;

public class TAXGroupMigrator implements IMigrator<TAXGroup> {

	@Override
	public JSONObject migrate(TAXGroup obj, MigratorContext context)
			throws JSONException {

		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject);
		jsonObject.put("name", obj.getName());
		jsonObject.put("isInactive", !obj.isActive());
		jsonObject.put("isTaxGroup", true);

		List<TAXItem> taxItems = obj.getTAXItems();
		JSONArray array = new JSONArray();
		for (TAXItem item : taxItems) {
			TaxItemMigrator taxItemMigrator = new TaxItemMigrator();
			JSONObject jsonItem = taxItemMigrator.migrate(item, context);
			array.put(jsonItem);
		}
		jsonObject.put("taxGroupItems", array);
		return jsonObject;
	}
}
