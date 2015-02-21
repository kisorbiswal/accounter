package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.TransactionPayBill;

public class PayBillMigrator extends TransactionMigrator<PayBill> {
	@Override
	public JSONObject migrate(PayBill obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		List<TransactionPayBill> transactionPayBill = obj
				.getTransactionPayBill();
		JSONArray array = new JSONArray();
		for (TransactionPayBill tBill : transactionPayBill) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("dueDate", tBill.getDueDate().getAsDateObject());
			jsonObj.put("bill",
					context.get("EnterBill", tBill.getEnterBill().getID()));
			// applyDebits not found in java file
			// TODO
			array.put(jsonObj);
		}
		jsonObject.put("paybillItems", array);

		jsonObject.put("tDS",
				context.get("TaxItem", obj.getTdsTaxItem().getID()));
		jsonObject.put("filterByBillDueOnOrBefore", obj.getBillDueOnOrBefore()
				.getAsDateObject());
		// paymentStatus TODO
		return jsonObject;
	}
}
