package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Customer;

public class CustomerTdsEnableMigrator implements IMigrator<Customer> {

	boolean isResetTds;

	public CustomerTdsEnableMigrator(boolean enableTds) {
		this.isResetTds = enableTds;
	}

	@Override
	public JSONObject migrate(Customer obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", context.get("Customer", obj.getID()));
		if (isResetTds) {
			jsonObject.put("tDSApplicable", obj.isWillDeductTDS());
		} else {
			jsonObject.put("tDSApplicable", isResetTds);
		}
		return jsonObject;
	}
}
