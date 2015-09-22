package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PayTAX;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TransactionPayTAX;

public class PayTaxMigrator extends TransactionMigrator<PayTAX> {
	@Override
	public JSONObject migrate(PayTAX obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		// filterbyTAXreturnenddate
		FinanceDate returnsDueOnOrBefore = obj.getReturnsDueOnOrBefore();
		if (returnsDueOnOrBefore != null) {
			jsonObject.put("filterbyTAXreturnenddate", returnsDueOnOrBefore
					.getAsDateObject().getTime());
		}
		// Tax Agency
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
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
					.getAsDateObject().getTime());
			// fileTax not found
			array.put(transactionJson);
		}
		jsonObject.put("payTaxItems", array);
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "Cash");
		}
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
		jsonObject.put("number", obj.getNumber());
		return jsonObject;
	}

}
