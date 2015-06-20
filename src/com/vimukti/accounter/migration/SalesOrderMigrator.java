package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.SalesOrder;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;

public class SalesOrderMigrator extends TransactionMigrator<SalesOrder> {
	@Override
	public JSONObject migrate(SalesOrder obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("payee",
				context.get("BusinessRelationship", obj.getCustomer().getID()));
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObj.put("contanct", context.get("Contanct", contact.getID()));
		}
		Estimate estimate = obj.getEstimate();
		if (estimate != null) {
			jsonObj.put("quotation",
					context.get("SalesQuotation", estimate.getID()));
		}
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
		Address billingAddr = obj.getBillingAddress();
		jsonBillingAddr.put("street", billingAddr.getStreet());
		jsonBillingAddr.put("city", billingAddr.getCity());
		jsonBillingAddr.put("stateOrProvince",
				billingAddr.getStateOrProvinence());
		jsonBillingAddr
				.put("zipOrPostalCode", billingAddr.getZipOrPostalCode());
		jsonBillingAddr.put("country", billingAddr.getCountryOrRegion());
		jsonObj.put("billTo", jsonBillingAddr);
		jsonObj.put("customerReference", obj.getReference());

		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			jsonObj.put("paymentTerm",
					context.get("PaymentTerm", paymentTerm.getID()));
		}
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			jsonObj.put("shippingMethod",
					context.get("ShippingMethod", shippingMethod.getID()));
		}
		ShippingTerms shippingTerm = obj.getShippingTerm();
		if (shippingTerm != null) {
			jsonObj.put("shippingTerm",
					context.get("ShippingTerm", shippingTerm.getID()));
		}
		jsonObj.put("deliveryDate", obj.getDueDate());
		jsonObj.put("remarks", obj.getMemo());
		//salesOrderStatus not found in SaleOrder.java
		return jsonObj;
	}
}
