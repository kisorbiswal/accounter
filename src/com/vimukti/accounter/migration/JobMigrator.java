package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Job;

public class JobMigrator implements IMigrator<Job> {

	@Override
	public JSONObject migrate(Job obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getJobName());
		jsonObject.put("startDate", obj.getStartDate().getAsDateObject()
				.getTime());
		jsonObject.put("endDate", obj.getEndDate().getAsDateObject().getTime());
		Customer customer = obj.getCustomer();
		if (customer != null) {
			jsonObject.put("customer",
					context.get("Customer", customer.getID()));
		}
		jsonObject.put("status", PicklistUtilMigrator
				.getProjectStatusIdentity(obj.getJobStatus()));
		return jsonObject;
	}
}
