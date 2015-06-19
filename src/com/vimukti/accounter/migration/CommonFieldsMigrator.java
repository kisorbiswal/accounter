package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CreatableObject;

public class CommonFieldsMigrator {

	public static void migrateCommonFields(CreatableObject obj, JSONObject json)
			throws JSONException {
		json.put("createdBy", obj.getCreatedBy());
		json.put("lastModifiedBy", obj.getLastModifier());
		json.put("createdDate", obj.getCreatedDate());
		json.put("lastModifiedDate", obj.getLastModifiedDate());
	}
}
