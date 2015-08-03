package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.User;

public class ProfileMigrator implements IMigrator<User> {

	@Override
	public JSONObject migrate(User obj, MigratorContext context)
			throws JSONException {
		User createdBy = context.getCompany().getCreatedBy();
		if (createdBy == obj) {
			return null;
		}
		JSONObject profile = new JSONObject();
		profile.put("permissionSet", context.get("PermissionSet", obj.getID()));
		return profile;
	}
}