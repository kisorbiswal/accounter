package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.User;

public class UserMigrator implements IMigrator<User> {

	@Override
	public JSONObject migrate(User obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject);
		// TODO Auto-generated method stub
		return null;
	}
}