package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;

public class TaxItemMigrator extends TaxMigrator<TAXItem> {

	@Override
	public JSONObject migrate(TAXItem obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("rate", obj.getTaxRate());
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			JSONObject agency = new JSONObject();
			agency.put("name", taxAgency.getName());
			jsonObject.put("taxAgency", agency);
		}
		jsonObject.put("description", obj.getDescription());
		return jsonObject;
	}
}
