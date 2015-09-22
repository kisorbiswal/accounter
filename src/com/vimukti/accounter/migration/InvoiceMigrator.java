package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;

public class InvoiceMigrator extends TransactionMigrator<Invoice> {

	@Override
	public JSONObject migrate(Invoice obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("payee",
				context.get("Customer", obj.getCustomer().getID()));
		// DueDate
		jsonObject.put("dueDate", obj.getDueDate().getAsDateObject().getTime());
		// BillTo
		Address billTo = obj.getBillingAddress();
		if (billTo != null) {
			JSONObject billToJson = new JSONObject();
			billToJson.put("street", billTo.getStreet());
			billToJson.put("city", billTo.getCity());
			billToJson.put("stateOrProvince", billTo.getStateOrProvinence());
			billToJson.put("zipOrPostalCode", billTo.getZipOrPostalCode());
			billToJson.put("country", billTo.getCountryOrRegion());
			jsonObject.put("billTo", billToJson);
		}

		// shipTo
		Address shipTo = obj.getBillingAddress();
		if (shipTo != null) {
			JSONObject shipToJson = new JSONObject();
			shipToJson.put("street", shipTo.getStreet());
			shipToJson.put("city", shipTo.getCity());
			shipToJson.put("stateOrProvince", shipTo.getStateOrProvinence());
			shipToJson.put("zipOrPostalCode", shipTo.getZipOrPostalCode());
			shipToJson.put("country", shipTo.getCountryOrRegion());
			jsonObject.put("shipTo", shipToJson);
		}

		// shipping Term
		ShippingTerms shippingTerm = obj.getShippingTerm();
		if (shippingTerm != null) {
			jsonObject.put("shippingTerm",
					context.get("ShippingTerm", shippingTerm.getID()));
		}

		// ShippingMethod
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			jsonObject.put("shippingMethod",
					context.get("ShippingMethod", shippingMethod.getID()));
		}

		// delivery Date
		jsonObject.put("deliveryDate", obj.getDeliverydate().getAsDateObject()
				.getTime());

		// Sales Quotations
		List<Estimate> estimates = obj.getEstimates();
		if (estimates != null) {
			JSONArray array = new JSONArray();
			for (Estimate estimate : estimates) {
				if (estimate.getEstimateType() == 6) {
					JSONObject quoteJson = new JSONObject();
					quoteJson.put("id",
							context.get("SalesOrder", estimate.getID()));
					array.put(quoteJson);
				}
			}
			if (array.length() > 0) {
				jsonObject.put("salesOrders", array);
			}
		}

		// Setting object PaymentTerm
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			jsonObject.put("paymentTerm",
					context.get("PaymentTerm", paymentTerm.getID()));
		}

		// Contact
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObject.put("contact", context.get("Contact", contact.getID()));

		}
		super.setJSONObj(jsonObject);
		return jsonObject;
	}
}
