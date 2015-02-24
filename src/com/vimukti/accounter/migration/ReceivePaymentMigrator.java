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
		jsonObj.put("depositIn", obj.getDepositIn());
		jsonObj.put("amountReceived", obj.getAmount());
		jsonObj.put("tDSAmount", obj.getTdsTotal());
		List<TransactionReceivePayment> transactionReceivePayment = obj
				.getTransactionReceivePayment();
		JSONArray array = new JSONArray();
		for (TransactionReceivePayment item : transactionReceivePayment) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("dueDate", item.getDueDate().getAsDateObject());
			jsonObject.put("invoice",
					context.get("Invoice", item.getInvoice().getID()));
			jsonObject.put("invoiceAmount", item.getInvoiceAmount());
			jsonObject.put("discountAccount",
					context.get("Account", item.getDiscountAccount().getID()));

			JSONObject jsonWritOffObject = new JSONObject();
			jsonWritOffObject.put("writeoffAccount",
					context.get("Account", item.getWriteOffAccount().getID()));
			jsonWritOffObject.put("amount", item.getWriteOff());
			jsonObject.put("WriteOff", jsonWritOffObject);
			// applyCredites not found
			// TODO
			array.put(jsonObject);
		}
		jsonObj.put("paymentItems", array);
		jsonObj.put("customerBalance", obj.getCustomerBalance());

		// PaymentableTransaction
		jsonObj.put("paymentMethod", obj.getPaymentMethod());
		jsonObj.put("checkNumber", obj.getCheckNumber());
		return jsonObj;
	}
}
