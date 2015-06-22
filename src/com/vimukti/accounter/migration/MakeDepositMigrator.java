package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TransactionDepositItem;

public class MakeDepositMigrator extends TransactionMigrator<MakeDeposit> {
	@Override
	public JSONObject migrate(MakeDeposit obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObj);
		Account depositTo = obj.getDepositTo();
		if (depositTo != null) {
			jsonObj.put("depositTo", context.get("Account", depositTo.getID()));
		}
		List<TransactionDepositItem> transactionDepositItems = obj
				.getTransactionDepositItems();
		JSONArray array = new JSONArray();
		for (TransactionDepositItem item : transactionDepositItems) {
			JSONObject depositItemJson = new JSONObject();
			Payee receivedFrom = item.getReceivedFrom();
			if (receivedFrom != null) {
				depositItemJson.put("payee",
						context.get("Customer", receivedFrom.getID()));
			}
			Account account = item.getAccount();
			if (account != null) {
				depositItemJson.put("depositFrom",
						context.get("Account", account.getID()));
			}
			depositItemJson.put("description", item.getDescription());
			depositItemJson.put("amount", item.getTotal());
			depositItemJson.put("accountClass", context.get("AccountClass",
					item.getAccounterClass().getID()));
			Customer customer = item.getCustomer();
			if (customer != null) {
				depositItemJson.put("customer",
						context.get("customer", customer.getID()));
			}
			Job job = item.getJob();
			if (job != null) {
				depositItemJson.put("project", context.get("Job", job.getID()));
			}
			array.put(depositItemJson);
		}
		jsonObj.put("depositItems", array);
		jsonObj.put(
				"paymentMethod",
				context.getPickListContext().get(
						"PaymentMethod",
						PicklistUtilMigrator.getPaymentMethodIdentifier(obj
								.getPaymentMethod())));
		jsonObj.put("paymentStatus", "Issued");
		return jsonObj;
	}
}
