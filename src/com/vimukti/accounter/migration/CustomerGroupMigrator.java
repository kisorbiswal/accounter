package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CustomerGroup;

public class CustomerGroupMigrator implements IMigrator<CustomerGroup> {

	@Override
	public JSONObject migrate(CustomerGroup obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", obj.getName());
		return jsonObject;
	}
}