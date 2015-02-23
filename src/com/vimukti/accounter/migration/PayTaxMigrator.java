package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PayTAX;
import com.vimukti.accounter.core.TransactionPayTAX;

public class PayTaxMigrator extends TransactionMigrator<PayTAX> {
	@Override
	public JSONObject migrate(PayTAX obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("payFrom",
				context.get("Account", obj.getPayFrom().getID()));
		jsonObject.put("filterbyTAXreturnenddate", obj
				.getReturnsDueOnOrBefore().getAsDateObject());
		jsonObject.put("taxAgency",
				context.get("TaxAgency", obj.getTaxAgency().getID()));

		List<TransactionPayTAX> transactionPayTAXs = obj.getTransactionPayTAX();
		JSONArray array = new JSONArray();
		for (TransactionPayTAX transactionPayTAX : transactionPayTAXs) {
			JSONObject transactionJson = new JSONObject();
			transactionJson.put("taxDue", transactionPayTAX.getTaxDue());
			transactionJson.put("payment", transactionPayTAX.getAmountToPay());
			transactionJson.put("filedDate", transactionPayTAX.getFiledDate().getAsDateObject());
			//fileTax not found
			array.put(transactionJson);
		}
		jsonObject.put("payTaxItems", array);
		return jsonObject;
	}

}
