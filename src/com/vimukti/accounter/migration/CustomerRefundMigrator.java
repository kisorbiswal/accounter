package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CustomerRefund;

public class CustomerRefundMigrator extends TransactionMigrator<CustomerRefund> {
	@Override
	public JSONObject migrate(CustomerRefund obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);

		Address addr = obj.getAddress();
		if (addr != null) {
			JSONObject jsonAddr = new JSONObject();
			jsonAddr.put("street", addr.getStreet());
			jsonAddr.put("city", addr.getCity());
			jsonAddr.put("stateOrProvince", addr.getStateOrProvinence());
			jsonAddr.put("zipOrPostalCode", addr.getZipOrPostalCode());
			jsonAddr.put("country", addr.getCountryOrRegion());
			jsonObj.put("address", jsonAddr);
		}
		jsonObj.put("amount", obj.getTotal());
		// paymentMethod
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObj.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		}
		// Check Number
		Long checkNumber = 0L;
		try {
			checkNumber = Long.parseLong(obj.getCheckNumber());
		} catch (Exception e) {
			// Nothing to do
		}
		jsonObj.put("checkNumber", checkNumber);
		// PayTo
		jsonObj.put("payee", context.get("Customer", obj.getPayTo().getID()));
		// Account
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			JSONObject account = new JSONObject();
			account.put("name", payFrom.getName());
			jsonObj.put("payFrom", account);
		}
		jsonObj.put("toBePrinted", obj.getIsToBePrinted());
		jsonObj.put("paymentStatus", PicklistUtilMigrator
				.getPaymentStatusIdentifier(obj.getStatus()));
		return jsonObj;
	}
}
