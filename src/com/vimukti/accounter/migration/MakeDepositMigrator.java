package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TransactionDepositItem;

public class MakeDepositMigrator extends TransactionMigrator<MakeDeposit> {
	@Override
	public JSONObject migrate(MakeDeposit obj, MigratorContext context)
			throws JSONException {
		// super Calling
		JSONObject jsonObj = super.migrate(obj, context);

		// deposit To
		Account depositTo = obj.getDepositTo();
		if (depositTo != null) {
			JSONObject account = new JSONObject();
			account.put("name", depositTo.getName());
			jsonObj.put("depositTo", account);
		}
		List<TransactionDepositItem> transactionDepositItems = obj
				.getTransactionDepositItems();
		JSONArray array = new JSONArray();
		for (TransactionDepositItem item : transactionDepositItems) {
			JSONObject depositItemJson = new JSONObject();
			Payee receivedFrom = item.getReceivedFrom();
			if (receivedFrom != null) {
				long payeeId = 0;
				if (context.get("Customer", receivedFrom.getID()) != null) {
					payeeId = context.get("Customer", receivedFrom.getID());
				}
				if (context.get("Vendor", receivedFrom.getID()) != null) {
					payeeId = context.get("Vendor", receivedFrom.getID());
				}
				depositItemJson.put("payee", payeeId);
			}
			Account account = item.getAccount();
			if (account != null) {
				depositItemJson.put("depositFrom",
						context.get("Account", account.getID()));
			}
			depositItemJson.put("description", item.getDescription());
			depositItemJson.put("amount", item.getTotal());
			AccounterClass accounterClass = item.getAccounterClass();
			if (accounterClass != null) {
				depositItemJson.put("accountClass",
						context.get("AccountClass", accounterClass.getID()));
			}
			Customer customer = item.getCustomer();
			if (customer != null) {
				depositItemJson.put("customer",
						context.get("Customer", customer.getID()));
			}
			Job job = item.getJob();
			if (job != null) {
				depositItemJson.put("project", context.get("Job", job.getID()));
			}
			array.put(depositItemJson);
		}
		jsonObj.put("depositItems", array);
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObj.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObj.put("paymentMethod", "Cash");
		}
		return jsonObj;
	}
}
