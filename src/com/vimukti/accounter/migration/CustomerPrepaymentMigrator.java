package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CustomerPrePayment;

public class CustomerPrepaymentMigrator implements
		IMigrator<CustomerPrePayment> {
	@Override
	public JSONObject migrate(CustomerPrePayment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("project", context.get("Project", obj.getJob().getID()));
		jsonObject.put(
				"paymentMethod",
				context.getPickListContext().get(
						"PaymentMethod",
						PicklistUtilMigrator.getPaymentMethodIdentifier(obj
								.getPaymentMethod())));
		jsonObject.put("credit", context.get("CustomerCredit", obj
				.getCreditsAndPayments().getID()));
		try {
			jsonObject.put("chequeNumber", Long.valueOf(obj.getCheckNumber()));
		} catch (Exception e) {
			// Nothing to do
		}
		jsonObject.put("memo", obj.getMemo());
		JSONObject adressJson = new JSONObject();
		Address billingAddress = obj.getAddress();
		adressJson.put("street", billingAddress.getStreet());
		adressJson.put("city", billingAddress.getCity());
		adressJson
				.put("stateOrProvince", billingAddress.getStateOrProvinence());
		adressJson.put("zipOrPostalCode", billingAddress.getZipOrPostalCode());
		adressJson.put("country", billingAddress.getCountryOrRegion());
		jsonObject.put("address", adressJson);
		jsonObject.put("credit", context.get("CustomerCredit", obj
				.getCreditsAndPayments().getID()));
		jsonObject.put("amount", obj.getNetAmount());
		return jsonObject;
	}
}