package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.VendorCreditMemo;

public class DebitNoteMigration extends TransactionMigrator<VendorCreditMemo> {

	@Override
	public JSONObject migrate(VendorCreditMemo obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("phone", obj.getPhone());
		jsonObject.put(
				"payee",
				context.get("Vendor",
						context.get("Vendor", obj.getVendor().getID())));
		jsonObject.put("contact",
				context.get("Contact", obj.getContact().getID()));
		return jsonObject;
	}
}