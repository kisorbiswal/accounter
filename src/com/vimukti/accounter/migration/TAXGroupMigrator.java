package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;

public class TAXGroupMigrator extends TaxMigrator<TAXGroup> {

	@Override
	public JSONObject migrate(TAXGroup obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("isTaxGroup", true);
		JSONArray taxItems = new JSONArray();
		List<TAXItem> tItems = obj.getTAXItems();
		if (!tItems.isEmpty()) {
			for (TAXItem item : tItems) {
				taxItems.put(context.get("Tax", item.getID()));
			}
			jsonObject.put("taxGroupItems", taxItems);
		} else {
			return null;
		}
		return jsonObject;
	}
}