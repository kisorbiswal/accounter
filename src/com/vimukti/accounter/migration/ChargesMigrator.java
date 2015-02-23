package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Estimate;

public class ChargesMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("customer",
				context.get("Customer", obj.getCustomer().getID()));
		jsonObj.put("remarks", obj.getMemo());
		return jsonObj;
	}

}
