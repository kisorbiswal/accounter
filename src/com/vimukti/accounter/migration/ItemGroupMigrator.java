package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.ItemGroup;

public class ItemGroupMigrator implements IMigrator<ItemGroup> {

	@Override
	public JSONObject migrate(ItemGroup obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", obj.getName());
		return jsonObject;
	}
}