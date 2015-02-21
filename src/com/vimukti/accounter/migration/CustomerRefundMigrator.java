package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.Utility;

public class CustomerRefundMigrator extends TransactionMigrator<CustomerRefund> {
	@Override
	public JSONObject migrate(CustomerRefund obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);

		JSONObject jsonAddr = new JSONObject();
		Address addr = obj.getAddress();
		jsonAddr.put("street", addr.getStreet());
		jsonAddr.put("city", addr.getCity());
		jsonAddr.put("stateOrProvince", addr.getStateOrProvinence());
		jsonAddr.put("zipOrPostalCode", addr.getZipOrPostalCode());
		jsonAddr.put("country", addr.getCountryOrRegion());
		jsonObj.put("address", jsonAddr);
		jsonObj.put("amount", obj.getTotal());
		// PaymentableTransaction
		jsonObj.put("paymentMethod", obj.getPaymentMethod());
		jsonObj.put("checkNumber", obj.getCheckNumber());
		// PaymentStatus TODO
		return jsonObj;
	}
}
