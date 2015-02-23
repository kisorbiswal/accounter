package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Estimate;

public class SalesOrderMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("customer",
				context.get("Customer", obj.getCustomer().getID()));
		jsonObj.put("contanct",
				context.get("Contact", obj.getContact().getID()));
		jsonObj.put("phone", obj.getPhone());

		JSONObject jsonShippingAddr = new JSONObject();
		Address shipingAddr = obj.getShippingAdress();
		jsonShippingAddr.put("street", shipingAddr.getStreet());
		jsonShippingAddr.put("city", shipingAddr.getCity());
		jsonShippingAddr.put("stateOrProvince",
				shipingAddr.getStateOrProvinence());
		jsonShippingAddr.put("zipOrPostalCode",
				shipingAddr.getZipOrPostalCode());
		jsonShippingAddr.put("country", shipingAddr.getCountryOrRegion());
		jsonObj.put("shipTo", jsonShippingAddr);

		JSONObject jsonBillingAddr = new JSONObject();
		Address billingAddr = obj.getAddress();
		jsonBillingAddr.put("street", billingAddr.getStreet());
		jsonBillingAddr.put("city", billingAddr.getCity());
		jsonBillingAddr.put("stateOrProvince",
				billingAddr.getStateOrProvinence());
		jsonBillingAddr
				.put("zipOrPostalCode", billingAddr.getZipOrPostalCode());
		jsonBillingAddr.put("country", billingAddr.getCountryOrRegion());
		jsonObj.put("billTo", jsonBillingAddr);
		// quotation not found
		jsonObj.put("customerReference", obj.getReference());
		jsonObj.put("paymentTerm",
				context.get("PaymentTerm", obj.getPaymentTerm().getID()));
		jsonObj.put("shippingMethod",
				context.get("ShippingMethod", obj.getShippingMethod().getID()));
		jsonObj.put("shippingTerm",
				context.get("ShippingTerm", obj.getShippingTerm().getID()));
		jsonObj.put("deliveryDate", obj.getDeliveryDate());
		jsonObj.put("remarks", obj.getMemo());
		return jsonObj;
	}
}
