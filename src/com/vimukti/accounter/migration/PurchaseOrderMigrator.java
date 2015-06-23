package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTransaction;

public class PurchaseOrderMigrator extends TransactionMigrator<PurchaseOrder> {

	@Override
	public JSONObject migrate(PurchaseOrder obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);

		jsonObject.put("phone", obj.getPhone());
		jsonObject.put("toBeEmailed", obj.isToBeEmailed());
		jsonObject.put("toBePrinted", obj.isToBePrinted());
		jsonObject.put("deliveryDate", obj.getDeliveryDate().getAsDateObject());
		jsonObject.put("payee", context.get("Vendor", obj.getVendor().getID()));
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObject.put("contact", context.get("Contact", contact.getID()));
		}
		Address shippingAddress = obj.getShippingAddress();
		if (shippingAddress != null) {
			JSONObject addressJSON = new JSONObject();
			addressJSON.put("street", shippingAddress.getStreet());
			addressJSON.put("city", shippingAddress.getCity());
			addressJSON.put("stateOrProvince",
					shippingAddress.getStateOrProvinence());
			addressJSON.put("zipOrPostalCode",
					shippingAddress.getZipOrPostalCode());
			addressJSON.put("country", shippingAddress.getCountryOrRegion());
			jsonObject.put("billTo", shippingAddress);
		}
		Address vendorAddress = obj.getVendorAddress();
		if (vendorAddress != null) {
			JSONObject addressJSON = new JSONObject();
			addressJSON.put("street", vendorAddress.getStreet());
			addressJSON.put("city", vendorAddress.getCity());
			addressJSON.put("stateOrProvince",
					vendorAddress.getStateOrProvinence());
			addressJSON.put("zipOrPostalCode",
					vendorAddress.getZipOrPostalCode());
			addressJSON.put("country", vendorAddress.getCountryOrRegion());
			jsonObject.put("billTo", vendorAddress);
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
		if (obj.getStatus() == ClientTransaction.STATUS_CANCELLED) {
			jsonObject.put("purchaseOrderstatus", context.getPickListContext()
					.get("PurchaseOrderStatus", "Cancelled"));
		}
		return jsonObject;
	}
}
