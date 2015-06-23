package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.WriteCheck;

public class WriteCheckMigrator extends TransactionMigrator<WriteCheck> {
	@Override
	public JSONObject migrate(WriteCheck obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("inFavourOf", obj.getInFavourOf());
		jsonObj.put("ammount", obj.getAmount());

		// checkInDetail
		jsonObj.put("date", obj.getDate().getAsDateObject());
		try {
			jsonObj.put("chequeNumber", Long.parseLong(obj.getCheckNumber()));
		} catch (NumberFormatException nfe) {

		}
		jsonObj.put("memo", obj.getMemo());
		jsonObj.put("paymentMethod", PicklistUtilMigrator
				.getPaymentMethodIdentifier(obj.getPaymentMethod()));
		jsonObj.put("toBePrinted", obj.isToBePrinted());
		return jsonObj;
	}
}
