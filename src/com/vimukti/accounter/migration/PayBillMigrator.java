package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.Vendor;

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
			FinanceDate dueDate = tBill.getDueDate();
			if (dueDate != null) {
				jsonObj.put("dueDate", dueDate.getAsDateObject().getTime());
			}
			EnterBill enterBill = tBill.getEnterBill();
			if (enterBill != null) {
				jsonObj.put("bill", context.get("EnterBill", enterBill.getID()));
			}
			jsonObj.put("payment", tBill.getPayment());

			// discountAccount
			Account discountAccount = tBill.getDiscountAccount();
			if (discountAccount != null) {
				JSONObject discountAccountJson = new JSONObject();
				JSONObject account = new JSONObject();
				account.put("name", discountAccount.getName());
				discountAccountJson.put("discountAccount", account);
				discountAccountJson.put("discountAmount",
						tBill.getCashDiscount());
				jsonObj.put("discountAccount", discountAccountJson);
			}

			array.put(jsonObj);
		}
		jsonObject.put("paybillItems", array);

		TAXItem tdsTaxItem = obj.getTdsTaxItem();
		if (tdsTaxItem != null) {
			jsonObject.put("tDS", context.get("TAXItem", tdsTaxItem.getID()));
		}
		jsonObject.put("filterByBillDueOnOrBefore", obj.getBillDueOnOrBefore()
				.getAsDateObject().getTime());
		Vendor vendor = obj.getVendor();
		if (vendor != null) {
			jsonObject.put("payee", context.get("Vendor", vendor.getID()));
		}
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			JSONObject account = new JSONObject();
			account.put("name", payFrom.getName());
			jsonObject.put("account", account);
		}
		jsonObject.put("paymentMethod", PicklistUtilMigrator
				.getPaymentMethodIdentifier(obj.getPaymentMethod()));
		jsonObject.put("toBePrinted", obj.isToBePrinted());
		Long chequeNumber = null;
		try {
			chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObject.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		return jsonObject;
	}
}
