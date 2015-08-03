package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.EmployeeGroup;

public class EmployeeGroupMigrator implements IMigrator<EmployeeGroup> {

	@Override
	public JSONObject migrate(EmployeeGroup obj, MigratorContext context)
			throws JSONException {
		JSONObject employeeGroup = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, employeeGroup, context);
		employeeGroup.put("name", obj.getName());
		return employeeGroup;
	}
}