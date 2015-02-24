package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Estimate;

public class CreditsMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);

		Contact contact = obj.getContact();
		if (contact != null) {
			JSONObject jsonContact = new JSONObject();
			jsonContact.put("isPrimary", contact.isPrimary());
			jsonContact.put("contactName", contact.getName());
			jsonContact.put("title", contact.getTitle());
			jsonContact.put("businessPhone", contact.getBusinessPhone());
			jsonContact.put("email", contact.getEmail());
			jsonObj.put("contact", jsonContact);
		}
		jsonObj.put("payeee",
				context.get("Customer", obj.getCustomer().getID()));

		return jsonObj;
	}
}
