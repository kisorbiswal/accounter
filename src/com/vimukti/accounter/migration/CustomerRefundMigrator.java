package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.Job;

public class CustomerRefundMigrator extends TransactionMigrator<CustomerRefund> {
	@Override
	public JSONObject migrate(CustomerRefund obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);

		JSONObject jsonAddr = new JSONObject();
		Address addr = obj.getAddress();
		if (addr != null) {
			jsonAddr.put("street", addr.getStreet());
			jsonAddr.put("city", addr.getCity());
			jsonAddr.put("stateOrProvince", addr.getStateOrProvinence());
			jsonAddr.put("zipOrPostalCode", addr.getZipOrPostalCode());
			jsonAddr.put("country", addr.getCountryOrRegion());
			jsonObj.put("address", jsonAddr);
		}
		jsonObj.put("amount", obj.getTotal());
		// PaymentableTransaction
		jsonObj.put("paymentMethod", PicklistUtilMigrator
				.getPaymentMethodIdentifier(obj.getPaymentMethod()));
		Long checkNumber = 0L;
		try {
			checkNumber = Long.parseLong(obj.getCheckNumber());
		} catch (Exception e) {
			// Nothing to do
		}
		jsonObj.put("checkNumber", checkNumber);
		Job job = obj.getJob();
		if (job != null) {
			jsonObj.put("project", context.get("Project", job.getID()));
		}
		jsonObj.put("payee", context.get("Customer", obj.getPayTo().getID()));
		jsonObj.put("account", context.get("Account", obj.getPayFrom().getID()));
		jsonObj.put("toBePrinted", obj.getIsToBePrinted());
		jsonObj.put("memo", obj.getMemo());
		jsonObj.put("date", obj.getDate().getAsDateObject().getTime());
		return jsonObj;
	}
}
