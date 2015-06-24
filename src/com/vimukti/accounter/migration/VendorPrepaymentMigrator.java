package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.VendorPrePayment;

public class VendorPrepaymentMigrator extends
		TransactionMigrator<VendorPrePayment> {
	@Override
	public JSONObject migrate(VendorPrePayment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);

		Address addr = obj.getAddress();
		if (addr != null) {
			JSONObject jsonAddr = new JSONObject();
			jsonAddr.put("street", addr.getStreet());
			jsonAddr.put("city", addr.getCity());
			jsonAddr.put("stateOrProvince", addr.getStateOrProvinence());
			jsonAddr.put("zipOrPostalCode", addr.getZipOrPostalCode());
			jsonAddr.put("country", addr.getCountryOrRegion());
			jsonObject.put("address", jsonAddr);
		}
		jsonObject.put("payFrom",
				context.get("Account", obj.getPayFrom().getID()));
		jsonObject.put("amount", obj.getTotal());

		// BillPayment
		jsonObject.put("payee", context.get("Vendor", obj.getVendor().getID()));
		jsonObject.put("date", obj.getDate().getAsDateObject().getTime());
		jsonObject.put("paymentMethod", PicklistUtilMigrator
				.getPaymentMethodIdentifier(obj.getPaymentMethod()));
		// billPaymentAmount not found
		Long chequeNumber = null;
		try {
			chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObject.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		jsonObject.put("memo", obj.getMemo());
		return jsonObject;
	}
}
