package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Estimate;

public class CreditsMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		if (obj.getContact() != null) {
			jsonObj.put("contact",
					context.get("Contact", obj.getContact().getID()));
		}
		jsonObj.put("payee", context.get("Customer", obj.getCustomer().getID()));
		jsonObj.put("transactionType", "Credit");
		return jsonObj;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.CREDITS));
	}
}
