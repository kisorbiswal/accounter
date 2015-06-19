package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Job;

public class JobMigrator implements IMigrator<Job> {

	@Override
	public JSONObject migrate(Job obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject);
		jsonObject.put("name", obj.getJobName());
		jsonObject.put("startDate", obj.getStartDate().getAsDateObject());
		jsonObject.put("endDate", obj.getEndDate().getAsDateObject());
		jsonObject.put("customer",
				context.get("Customer", obj.getCustomer().getID()));
		jsonObject.put(
				"status",
				context.getPickListContext().get("ProjectStatus",
						obj.getJobStatus()));
		return jsonObject;
	}
}