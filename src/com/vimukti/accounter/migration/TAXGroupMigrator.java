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

		List<TAXItem> taxItems = obj.getTAXItems();
		JSONArray array = new JSONArray();
		for (TAXItem item : taxItems) {
			array.put(context.get("TAXItem", item.getID()));
		}
		jsonObject.put("taxGroupItems", array);
		return jsonObject;
	}
}
