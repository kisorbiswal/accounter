package com.vimukti.accounter.migration;

import java.util.List;

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
		String type = super.setJSONObj(jsonObj);
		// BasedOn CompanySettings we split Transactions into sub lists like
		// with tax,without discount etc. So for these childrens also we are
		// splitting
		String childKey = "transactionItems-CreditItem";
		List<Long> list = context.getChildrenMap().get(childKey);
		super.addChildrenBasedOnType(type, list, childKey);
		context.getChildrenMap().remove(childKey);
		return jsonObj;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.CREDITS));
	}
}
