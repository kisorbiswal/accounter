package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CustomerCreditMemo;

public class CreditMemoMigrator extends TransactionMigrator<CustomerCreditMemo> {
	@Override
	public JSONObject migrate(CustomerCreditMemo obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		Address billingAddress = obj.getBillingAddress();
		if (billingAddress != null) {
			JSONObject jsonAddress = new JSONObject();
			jsonAddress.put("street", billingAddress.getStreet());
			jsonAddress.put("city", billingAddress.getCity());
			jsonAddress.put("stateOrProvince",
					billingAddress.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode",
					billingAddress.getZipOrPostalCode());
			jsonAddress.put("country", billingAddress.getCountryOrRegion());
			jsonObj.put("billTo", jsonAddress);
		}
		// invoice and credit is not found
		if (obj.getContact() != null) {
			jsonObj.put("contact",
					context.get("Contact", obj.getContact().getID()));
		}
		jsonObj.put("payee", context.get("Customer", obj.getCustomer().getID()));
		return jsonObj;
	}
}
