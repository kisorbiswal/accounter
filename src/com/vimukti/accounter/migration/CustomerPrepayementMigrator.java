package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CustomerPrePayment;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.Location;

public class CustomerPrepayementMigrator implements
		IMigrator<CustomerPrePayment> {

	@Override
	public JSONObject migrate(CustomerPrePayment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();

		Location location = obj.getLocation();
		AccounterClass accounterClass = obj.getAccounterClass();
		Address address = obj.getAddress();
		Job job = obj.getJob();

		jsonObject.put("date", obj.getDate().getAsDateObject());
		jsonObject.put("notes", obj.getMemo());
		jsonObject.put("chequeNumber", obj.getCheckNumber());
		jsonObject.put("amount", obj.getTotal());

		jsonObject.put("depositIn",
				context.get("Account", obj.getDepositIn().getID()));
		jsonObject.put("payee",
				context.get("Customer", obj.getCustomer().getID()));
		jsonObject.put(
				"paymentMethod",
				context.getPickListContext().get("PaymentMethod",
						obj.getPaymentMethod()));

		if (location != null) {
			jsonObject.put("location",
					context.get("Location", location.getID()));
		}
		if (accounterClass != null) {
			jsonObject.put("accountClass",
					context.get("AccountClass", accounterClass.getID()));
		}
		if (job != null) {
			jsonObject.put("project", context.get("Project", job.getID()));
		}

		if (address != null) {
			JSONObject jSONAddress = new JSONObject();
			jSONAddress.put("street", address.getStreet());
			jSONAddress.put("city", address.getCity());
			jSONAddress.put("stateOrProvince", address.getStateOrProvinence());
			jSONAddress.put("zipOrPostalCode", address.getZipOrPostalCode());
			jSONAddress.put("country", address.getCountryOrRegion());

			jsonObject.put("address", jSONAddress);
		}

		return jsonObject;
	}
}
