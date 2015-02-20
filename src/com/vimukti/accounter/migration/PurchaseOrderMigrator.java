package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;

public class PurchaseOrderMigrator extends TransactionMigrator<PurchaseOrder> {

	@Override
	public JSONObject migrate(PurchaseOrder obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);

		jsonObject.put("phone", obj.getPhone());
		jsonObject.put("toBeEmailed", obj.isToBeEmailed());
		jsonObject.put("toBePrinted", obj.isToBePrinted());
		jsonObject.put("deliveryDate", obj.getDeliveryDate().getAsDateObject());
		jsonObject
				.put("vendor", context.get("Vendor", obj.getVendor().getID()));
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObject.put("contact", contact.getID());
		}
		Address shippingAddress = obj.getShippingAddress();
		if (shippingAddress != null) {
			jsonObject.put("shipTo",
					context.get("Address", shippingAddress.getId()));
		}
		Address vendorAddress = obj.getVendorAddress();
		if (vendorAddress != null) {
			jsonObject.put("billTo",
					context.get("Address", vendorAddress.getId()));
		}
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			jsonObject.put("paymentTerm",
					context.get("PaymentTerm", paymentTerm.getID()));
		}
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			jsonObject.put("shippingMethod",
					context.get("ShippingMethod", shippingMethod.getID()));
		}
		ShippingTerms shippingTerms = obj.getShippingTerms();
		if (shippingTerms != null) {
			jsonObject.put("shippingTerm",
					context.get("ShippingTerm", shippingTerms.getID()));
		}
		return jsonObject;
	}
}
