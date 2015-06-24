package com.vimukti.accounter.migration;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CreatableObject;

public class CommonFieldsMigrator {

	public static void migrateCommonFields(CreatableObject obj,
			JSONObject json, MigratorContext context) throws JSONException {
		json.put("createdBy", context.getAdmin());
		json.put("lastModifiedBy", context.getAdmin());
		Timestamp createdDate = obj.getCreatedDate();
		if (createdDate != null) {
			json.put("createdDate", createdDate.getTime());
		}
		Timestamp lastModifiedDate = obj.getLastModifiedDate();
		if (lastModifiedDate != null) {
			json.put("lastModifiedDate", obj.getLastModifiedDate().getTime());
		}
	}
}
