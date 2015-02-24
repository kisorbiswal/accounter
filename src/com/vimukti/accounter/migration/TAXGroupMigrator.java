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
		jsonObject.put("name", obj.getName());
		List<TAXItem> taxItems = obj.getTAXItems();
		JSONArray array = new JSONArray();
		for (TAXItem item : taxItems) {
			JSONObject jsonItem = new JSONObject();
			jsonObject.put("rate", item.getTaxRate());
			jsonObject.put("taxAgency", item.getTaxAgency());
			jsonObject.put("description", item.getTaxAgency());
			jsonObject.put("name", item.getName());
			jsonObject.put("isInactive", !item.isActive());
			array.put(jsonItem);
		}
		jsonObject.put("taxGroupItems", array);
		return jsonObject;
	}

}
