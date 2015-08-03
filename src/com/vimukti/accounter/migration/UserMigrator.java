package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.User;

public class UserMigrator implements IMigrator<User> {

	@Override
	public JSONObject migrate(User obj, MigratorContext context)
			throws JSONException {
		User createdBy = context.getCompany().getCreatedBy();
		if (createdBy == obj) {
			return null;
		}
		Client accClient = obj.getClient();
		JSONObject userJson = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, userJson, context);
		userJson.put("firstName", accClient.getFirstName());
		userJson.put("userName", obj.getName());
		userJson.put("email", accClient.getEmailId());
		userJson.put("profile", context.get("Profile", obj.getID()));
		return userJson;
	}
}