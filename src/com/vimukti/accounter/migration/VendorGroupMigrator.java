package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.VendorGroup;

public class VendorGroupMigrator implements IMigrator<VendorGroup> {

	@Override
	public JSONObject migrate(VendorGroup obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", obj.getName());
		return jsonObject;
	}
}