package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerPrePayment;

public class CustomerPrepaymentMigrator extends
		TransactionMigrator<CustomerPrePayment> {
	@Override
	public JSONObject migrate(CustomerPrePayment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		// customer
		Customer customer = obj.getCustomer();
		if (customer != null) {
			jsonObject.put("payee", context.get("Customer", customer.getID()));
		}
		// address
		Address billingAddress = obj.getAddress();
		if (billingAddress != null) {
			JSONObject adressJson = new JSONObject();
			adressJson.put("street", billingAddress.getStreet());
			adressJson.put("city", billingAddress.getCity());
			adressJson.put("stateOrProvince",
					billingAddress.getStateOrProvinence());
			adressJson.put("zipOrPostalCode",
					billingAddress.getZipOrPostalCode());
			adressJson.put("country", billingAddress.getCountryOrRegion());
			jsonObject.put("address", adressJson);
		}
		// Account
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			JSONObject account = new JSONObject();
			account.put("name", depositIn.getName());
			jsonObject.put("account", account);
		}
		// Amount
		jsonObject.put("amount", obj.getNetAmount());

		// paymentMethod
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "Cash");
		}
		try {
			String checkNumber = obj.getCheckNumber();
			if (checkNumber != null) {
				jsonObject.put("chequeNumber", Long.valueOf(checkNumber));
			}
		} catch (Exception e) {
			// Nothing to do
		}
		return jsonObject;
	}
}