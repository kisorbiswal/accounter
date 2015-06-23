package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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
		jsonObj.put("transactionType",
				context.getPickListContext().get("TransactionType", "Charge"));
		return jsonObj;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.CHARGES));
	}
}
