package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.TransactionDepositItem;

public class MakeDepositMigrator extends TransactionMigrator<MakeDeposit> {
	@Override
	public JSONObject migrate(MakeDeposit obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("depositTo",
				context.get("Account", obj.getDepositTo().getID()));
		List<TransactionDepositItem> transactionDepositItems = obj
				.getTransactionDepositItems();
		JSONArray array = new JSONArray();
		for (TransactionDepositItem item : transactionDepositItems) {
			JSONObject depositItemJson = new JSONObject();
			depositItemJson.put("payee",
					context.get("Customer", item.getReceivedFrom().getID()));
			depositItemJson.put("depositFrom",
					context.get("Account", item.getAccount().getID()));
			depositItemJson.put("description", item.getDescription());
			depositItemJson.put("amount", item.getTotal());
			depositItemJson.put("accountClass", context.get("AccountClass",
					item.getAccounterClass().getID()));
			depositItemJson.put("customer",
					context.get("customer", item.getCustomer().getID()));
			depositItemJson.put("project",
					context.get("Project", item.getJob().getID()));
			array.put(depositItemJson);
		}
		jsonObj.put("depositItems", array);
		jsonObj.put("paymentMethod", obj.getPaymentMethod());
		// PaymentSatus TODO
		return jsonObj;
	}
}
