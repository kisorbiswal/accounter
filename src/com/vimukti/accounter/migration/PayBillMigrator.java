package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TransactionCreditsAndPayments;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.Vendor;

public class PayBillMigrator extends TransactionMigrator<PayBill> {
	@Override
	public JSONObject migrate(PayBill obj, MigratorContext context)
			throws JSONException {
		JSONObject payBill = super.migrate(obj, context);
		List<TransactionPayBill> transactionPayBill = obj
				.getTransactionPayBill();
		{
			JSONArray payBillItems = new JSONArray();
			for (TransactionPayBill tBill : transactionPayBill) {
				JSONObject payBillItem = new JSONObject();
				EnterBill enterBill = tBill.getEnterBill();
				if (enterBill != null) {
					payBillItem.put("bill",
							context.get("EnterBill", enterBill.getID()));
				} else {
					return null;
				}
				payBillItem.put("payment", tBill.getPayment());
				// discountAccount
				Account discountAccount = tBill.getDiscountAccount();
				if (discountAccount != null) {
					payBillItem.put("discountAccount",
							context.get("Account", discountAccount.getID()));
					payBillItem.put("discountAmount", tBill.getCashDiscount());
				}
				// Apply Debits
				{
					JSONObject applyDebit = new JSONObject();
					JSONArray applyDebitItems = new JSONArray();
					for (TransactionCreditsAndPayments ac : tBill
							.getTransactionCreditsAndPayments()) {
						JSONObject applyDebitItem = new JSONObject();
						applyDebitItem.put("credit", context.get("debitNote",
								ac.getCreditsAndPayments().getID()));
						applyDebitItem.put("amountToUse", ac.getAmountToUse());
						applyDebitItems.put(applyDebitItem);
					}
					payBillItem.put("applyDebits", applyDebit);
				}
				payBillItems.put(payBillItem);
			}
			payBill.put("payBillItems", payBillItems);
		}
		TAXItem tdsTaxItem = obj.getTdsTaxItem();
		if (tdsTaxItem != null) {
			payBill.put("tDS", context.get("Tax", tdsTaxItem.getID()));
		}
		payBill.put("tDSAmount", obj.getTdsTotal());
		payBill.put("filterByBillDueOnOrBefore", obj.getBillDueOnOrBefore()
				.getAsDateObject().getTime());
		Vendor vendor = obj.getVendor();
		if (vendor != null) {
			payBill.put("payee", context.get("Vendor", vendor.getID()));
		}
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			payBill.put("account", context.get("Account", payFrom.getID()));
		}
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			payBill.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		} else {
			payBill.put("paymentMethod", "Cash");
		}
		payBill.put("toBePrinted", obj.isToBePrinted());
		Long chequeNumber = null;
		try {
			chequeNumber = Long.valueOf(obj.getCheckNumber());
			payBill.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		return payBill;
	}
}
