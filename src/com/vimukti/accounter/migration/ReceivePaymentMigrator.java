package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TransactionReceivePayment;

public class ReceivePaymentMigrator extends TransactionMigrator<ReceivePayment> {
	@Override
	public JSONObject migrate(ReceivePayment obj, MigratorContext context)
			throws JSONException {

		JSONObject jsonObj = super.migrate(obj, context);
		// Deposit In Account
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			JSONObject account = new JSONObject();
			account.put("name", depositIn.getName());
			jsonObj.put("depositIn", account);
		}
		// Amount Received
		jsonObj.put("amountReceived", obj.getAmount());
		// TDS Amount
		jsonObj.put("tDSAmount", obj.getTdsTotal());

		List<TransactionReceivePayment> transactionReceivePayment = obj
				.getTransactionReceivePayment();
		JSONArray array = new JSONArray();
		for (TransactionReceivePayment item : transactionReceivePayment) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("dueDate", item.getDueDate().getAsDateObject()
					.getTime());
			jsonObject.put("invoice",
					context.get("Invoice", item.getInvoice().getID()));
			Account account = item.getDiscountAccount();
			if (account != null) {
				JSONObject itemAccont = new JSONObject();
				itemAccont.put("name", account.getName());
				jsonObject.put("discountAccount", account);
			}

			JSONObject jsonWritOffObject = new JSONObject();
			Account writeOffAccount = item.getWriteOffAccount();
			if (writeOffAccount != null) {
				JSONObject writeOfAccount = new JSONObject();
				writeOfAccount.put("name", writeOffAccount.getName());
				jsonWritOffObject.put("writeoffAccount", writeOfAccount);
			}
			jsonWritOffObject.put("amount", item.getWriteOff());
			jsonObject.put("writeOff", jsonWritOffObject);
			array.put(jsonObject);
		}
		jsonObj.put("paymentItems", array);

		// PaymentableTransaction
		jsonObj.put("paymentMethod", PicklistUtilMigrator
				.getPaymentMethodIdentifier(obj.getPaymentMethod()));
		try {
			Long chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObj.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		return jsonObj;
	}
}
