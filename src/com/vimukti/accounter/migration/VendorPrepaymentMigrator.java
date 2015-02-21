package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.VendorPrePayment;

public class VendorPrepaymentMigrator extends
		TransactionMigrator<VendorPrePayment> {
	@Override
	public JSONObject migrate(VendorPrePayment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);

		JSONObject jsonAddr = new JSONObject();
		Address addr = obj.getAddress();
		jsonAddr.put("street", addr.getStreet());
		jsonAddr.put("city", addr.getCity());
		jsonAddr.put("stateOrProvince", addr.getStateOrProvinence());
		jsonAddr.put("zipOrPostalCode", addr.getZipOrPostalCode());
		jsonAddr.put("country", addr.getCountryOrRegion());
		jsonObject.put("address", jsonAddr);
		jsonObject.put("payFrom",
				context.get("Account", obj.getPayFrom().getID()));
		jsonObject.put("amount", obj.getTotal());

		// BillPayment
		jsonObject.put("payee", context.get("Vendor", obj.getVendor().getID()));
		jsonObject.put("date", obj.getDate().getAsDateObject());
		jsonObject.put("paymentMethod", obj.getPaymentMethod());
		// billPaymentAmount not found
		jsonObject.put("checkNumber", obj.getCheckNumber());
		// PayementSatus TODO
		return jsonObject;
	}
}
