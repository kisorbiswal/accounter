package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
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
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObj.put("contact", context.get("Contact", contact.getID()));
		}
		Customer customer = obj.getCustomer();
		if (customer != null) {
			jsonObj.put("payee", context.get("Customer", customer.getID()));
		}

		return jsonObj;
	}
}
