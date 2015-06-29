package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.VendorCreditMemo;

public class DebitNoteMigrator extends TransactionMigrator<VendorCreditMemo> {

	@Override
	public JSONObject migrate(VendorCreditMemo obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("phone", obj.getPhone());
		// Vendor
		jsonObject.put("payee", context.get("Vendor", obj.getVendor().getID()));
		// Contact
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObject.put("contact", context.get("Contact", contact.getID()));
		}

		return jsonObject;
	}
}