package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Job;

public class JobMigrator implements IMigrator<Job> {

	@Override
	public JSONObject migrate(Job obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getJobName());
		FinanceDate startDate = obj.getStartDate();
		long time = startDate.getAsDateObject().getTime();
		jsonObject.put("startDate", time);
		FinanceDate endDate = obj.getEndDate();
		if (startDate.after(endDate)) {
			jsonObject.put("endDate", time);
		} else {
			jsonObject.put("endDate", endDate.getAsDateObject().getTime());
		}
		Customer customer = obj.getCustomer();
		if (customer != null) {
			jsonObject.put("customer",
					context.get("Customer", customer.getID()));
		}
		jsonObject.put("status", PicklistUtilMigrator
				.getProjectStatusIdentity(obj.getJobStatus()));
		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}
