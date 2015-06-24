package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TransactionReceivePayment;

public class ReceivePaymentMigrator extends TransactionMigrator<ReceivePayment> {
	@Override
	public JSONObject migrate(ReceivePayment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("depositIn",
				context.get("Account", obj.getDepositIn().getID()));
		jsonObj.put("amountReceived", obj.getAmount());
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
			jsonObject.put("discountAccount",
					context.get("Account", item.getDiscountAccount().getID()));

			JSONObject jsonWritOffObject = new JSONObject();
			jsonWritOffObject.put("writeoffAccount",
					context.get("Account", item.getWriteOffAccount().getID()));
			jsonWritOffObject.put("amount", item.getWriteOff());
			jsonObject.put("writeOff", jsonWritOffObject);
			// applyCredites not found
			// TODO
			array.put(jsonObject);
		}
		jsonObj.put("paymentItems", array);
		jsonObj.put("customerBalance", obj.getCustomerBalance());

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
