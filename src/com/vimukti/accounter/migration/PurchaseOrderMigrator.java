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
		// Phone
		jsonObject.put("phone", obj.getPhone());
		// DeliveryDate
		jsonObject.put("deliveryDate", obj.getDeliveryDate().getAsDateObject()
				.getTime());
		// Payee
		jsonObject.put("payee", context.get("Vendor", obj.getVendor().getID()));
		// Contact
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObject.put("contact", context.get("Contact", contact.getID()));
		}
		// shipTo
		Address shipTo = obj.getShippingAddress();
		if (shipTo != null) {
			JSONObject shipToJson = new JSONObject();
			shipToJson.put("street", shipTo.getStreet());
			shipToJson.put("city", shipTo.getCity());
			shipToJson.put("stateOrProvince", shipTo.getStateOrProvinence());
			shipToJson.put("zipOrPostalCode", shipTo.getZipOrPostalCode());
			shipToJson.put("country", shipTo.getCountryOrRegion());
			jsonObject.put("shipTo", shipTo);
		}
		// BillTo
		Address billTo = obj.getVendorAddress();
		if (billTo != null) {
			JSONObject billToJson = new JSONObject();
			billToJson.put("street", billTo.getStreet());
			billToJson.put("city", billTo.getCity());
			billToJson.put("stateOrProvince", billTo.getStateOrProvinence());
			billToJson.put("zipOrPostalCode", billTo.getZipOrPostalCode());
			billToJson.put("country", billTo.getCountryOrRegion());
			jsonObject.put("billTo", billTo);
		}
		// PaymentTerm
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			JSONObject paymentTermJson = new JSONObject();
			paymentTermJson.put("name", paymentTerm.getName());
			jsonObject.put("paymentTerm", paymentTermJson);
		}
		// shippingMethod
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			jsonObject.put("shippingMethod",
					context.get("ShippingMethod", shippingMethod.getID()));
		}
		// shippingTerms
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
