package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXItem;

public class TaxItemMigrator implements IMigrator<TAXItem> {

	@Override
	public JSONObject migrate(TAXItem obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rate", obj.getTaxRate());
		jsonObject.put("taxAgency", obj.getTaxAgency());
		jsonObject.put("description", obj.getTaxAgency());
		jsonObject.put("name", obj.getName());
		jsonObject.put("isInactive", !obj.isActive());
		return null;
	}
}
