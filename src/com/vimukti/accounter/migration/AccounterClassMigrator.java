package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;

public class AccounterClassMigrator implements IMigrator<AccounterClass> {

	@Override
	public JSONObject migrate(AccounterClass obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getclassName());
		jsonObject.put("description", obj.getPath());
		AccounterClass parent = obj.getParent();
		if (parent != null) {
			jsonObject.put("subClassOf",
					context.get("AccounterClass", parent.getID()));
		}
		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}