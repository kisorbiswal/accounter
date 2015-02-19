package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;

public class AccounterClassMigrator implements IMigrator<AccounterClass> {

	@Override
	public JSONObject migrate(AccounterClass obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", obj.getclassName());
		jsonObject.put("description", obj.getPath());
		jsonObject.put("subClassOf", obj.getParent());
		return jsonObject;
	}
}