package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;

public class CreditsMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObj.put("contact", context.get("Contact", contact.getID()));
		}
		Customer customer = obj.getCustomer();
		if (customer != null) {
			jsonObj.put("payee", context.get("Customer", customer.getID()));
		}
		super.setJSONObj(jsonObj);
		return jsonObj;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.CREDITS));
	}
}
