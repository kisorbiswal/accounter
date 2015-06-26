package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;

public class ChargesMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		Customer customer = obj.getCustomer();
		if (customer != null) {
			jsonObj.put("customer", context.get("Customer", customer.getID()));
		}
		jsonObj.put("remarks", obj.getMemo());
		return jsonObj;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.CHARGES));
	}
}
