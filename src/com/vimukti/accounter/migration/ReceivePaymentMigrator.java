package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TransactionCreditsAndPayments;
import com.vimukti.accounter.core.TransactionReceivePayment;

public class ReceivePaymentMigrator extends TransactionMigrator<ReceivePayment> {
	@Override
	public JSONObject migrate(ReceivePayment obj, MigratorContext context)
			throws JSONException {

		JSONObject receivePayment = super.migrate(obj, context);
		receivePayment.put("payee",
				context.get("Customer", obj.getCustomer().getID()));
		// Deposit In Account
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			receivePayment.put("depositIn",
					context.get("Account", depositIn.getID()));
		}
		// Amount Received
		receivePayment.put("amountReceived", obj.getAmount());
		// TDS Amount
		receivePayment.put("tDSAmount", obj.getTdsTotal());

		List<TransactionReceivePayment> transactionReceivePayment = obj
				.getTransactionReceivePayment();
		{
			JSONArray receivePaymentItems = new JSONArray();
			for (TransactionReceivePayment item : transactionReceivePayment) {
				JSONObject receivePaymentItem = new JSONObject();
				if (item.getInvoice() == null) {
					receivePayment = null;
					return null;
				}
				receivePaymentItem.put("invoice",
						context.get("Invoice", item.getInvoice().getID()));
				Account account = item.getDiscountAccount();
				if (account != null) {
					receivePaymentItem.put("discountAccount",
							context.get("Account", account.getID()));
					receivePaymentItem.put("discountAmount",
							item.getCashDiscount());
				}

				Account writeOffAccount = item.getWriteOffAccount();
				if (writeOffAccount != null) {
					receivePaymentItem.put("writeoffAccount",
							context.get("Account", writeOffAccount.getID()));
					receivePaymentItem
							.put("writeoffAmount", item.getWriteOff());

				}
				// Apply Credits
				{
					JSONObject applyCredit = new JSONObject();
					JSONArray applyCreditItems = new JSONArray();
					for (TransactionCreditsAndPayments ac : item
							.getTransactionCreditsAndPayments()) {
						JSONObject applyCreditItem = new JSONObject();
						applyCreditItem.put("credit", context.get(
								"CustomerCredit", ac.getCreditsAndPayments()
										.getID()));
						applyCreditItem.put("amountToUse", ac.getAmountToUse());
						applyCreditItems.put(applyCreditItem);
					}
					applyCredit.put("creditItems", applyCreditItems);
					receivePayment.put("applyCredits", applyCredit);
				}
				receivePaymentItem.put("payment", item.getPayment());
				receivePaymentItems.put(receivePaymentItem);
			}
			receivePayment.put("paymentItems", receivePaymentItems);
		}
		// PaymentableTransaction
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			receivePayment.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		} else {
			receivePayment.put("paymentMethod", "Cash");
		}
		try {
			Long chequeNumber = Long.valueOf(obj.getCheckNumber());
			receivePayment.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		return receivePayment;
	}
}
