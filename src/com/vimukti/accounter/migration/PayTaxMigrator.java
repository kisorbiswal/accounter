package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PayTAX;
import com.vimukti.accounter.core.TAXAgency;
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
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			jsonObject.put("taxAgency",
					context.get("TaxAgency", taxAgency.getID()));
			jsonObject
					.put("payee", context.get("TaxAgency", taxAgency.getID()));
		}

		List<TransactionPayTAX> transactionPayTAXs = obj.getTransactionPayTAX();
		JSONArray array = new JSONArray();
		for (TransactionPayTAX transactionPayTAX : transactionPayTAXs) {
			JSONObject transactionJson = new JSONObject();
			transactionJson.put("taxDue", transactionPayTAX.getTaxDue());
			transactionJson.put("payment", transactionPayTAX.getAmountToPay());
			transactionJson.put("filedDate", transactionPayTAX.getFiledDate()
					.getAsDateObject());
			// fileTax not found
			array.put(transactionJson);
		}
		jsonObject.put("payTaxItems", array);
		jsonObject.put("paymentMethod", PicklistUtilMigrator
				.getPaymentMethodIdentifier(obj.getPaymentMethod()));
		try {
			Long chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObject.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		jsonObject.put("payment", obj.getTotal());
		jsonObject.put("memo", obj.getMemo());
		// jsonObject.put("isReconciled", "");
		jsonObject.put("toBePrinted", true);
		jsonObject.put("account",
				context.get("Account", obj.getPayFrom().getID()));
		jsonObject.put("paymentNumber", obj.getNumber());
		return jsonObject;
	}

}
