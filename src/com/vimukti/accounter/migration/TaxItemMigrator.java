package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXItem;

public class TaxItemMigrator extends TaxMigrator<TAXItem> {

	@Override
	public JSONObject migrate(TAXItem obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("rate", obj.getTaxRate());
		jsonObject.put("taxAgency", obj.getTaxAgency());
		jsonObject.put("description", obj.getTaxAgency());
		return jsonObject;
	}
}
