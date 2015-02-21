package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Invoice;

public class InvoiceMigrator extends TransactionMigrator<Invoice> {

	@Override
	public JSONObject migrate(Invoice obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);

		jsonObject.put("dueDate", obj.getDueDate().getAsDateObject());
		jsonObject.put("phone", obj.getPhone());

		JSONObject billingAdressJson = new JSONObject();
		Address billingAddress = obj.getBillingAddress();
		billingAdressJson.put("street", billingAddress.getStreet());
		billingAdressJson.put("city", billingAddress.getCity());
		billingAdressJson.put("stateOrProvince",
				billingAddress.getStateOrProvinence());
		billingAdressJson.put("zipOrPostalCode",
				billingAddress.getZipOrPostalCode());
		billingAdressJson.put("country", billingAddress.getCountryOrRegion());
		jsonObject.put("billTo", billingAdressJson);

		JSONObject shippingAdressJson = new JSONObject();
		Address shippingAddress = obj.getBillingAddress();
		shippingAdressJson.put("street", shippingAddress.getStreet());
		shippingAdressJson.put("city", shippingAddress.getCity());
		shippingAdressJson.put("stateOrProvince",
				shippingAddress.getStateOrProvinence());
		shippingAdressJson.put("zipOrPostalCode",
				shippingAddress.getZipOrPostalCode());
		shippingAdressJson.put("country", shippingAddress.getCountryOrRegion());
		jsonObject.put("shipTo", shippingAdressJson);

		jsonObject.put("balanceDue", obj.getBalanceDue());
		jsonObject.put("amountPaid", obj.getPayments());
		jsonObject.put("shippingTerm",
				context.get("ShippingTerm", obj.getShippingTerm().getID()));
		jsonObject.put("shippingMethod",
				context.get("shippingMethod", obj.getShippingMethod().getID()));
		jsonObject.put("deliveryDate", obj.getDeliverydate().getAsDateObject());

		return jsonObject;
	}
}