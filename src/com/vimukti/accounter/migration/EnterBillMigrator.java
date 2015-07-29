package com.vimukti.accounter.migration;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PurchaseOrder;

public class EnterBillMigrator extends TransactionMigrator<EnterBill> {

	@Override
	public JSONObject migrate(EnterBill obj, MigratorContext context)
			throws JSONException {
		JSONObject enterBill = super.migrate(obj, context);
		// payee
		enterBill.put("payee", context.get("Vendor", obj.getVendor().getID()));
		// dueDate
		enterBill.put("dueDate", obj.getDueDate().getAsDateObject().getTime());
		enterBill.put("phone", obj.getPhone());
		// billTo
		Address billTo = obj.getVendorAddress();
		if (billTo != null) {
			JSONObject billToJson = new JSONObject();
			billToJson.put("street", billTo.getStreet());
			billToJson.put("city", billTo.getCity());
			billToJson.put("stateOrProvince", billTo.getStateOrProvinence());
			billToJson.put("zipOrPostalCode", billTo.getZipOrPostalCode());
			billToJson.put("country", billTo.getCountryOrRegion());
			enterBill.put("billTo", billTo);
		}
		// PaymentTerm
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			enterBill.put("paymentTerm",
					context.get("PaymentTerm", paymentTerm.getID()));
		}
		// Delivery Date
		enterBill.put("deliveryDate", obj.getDeliveryDate().getAsDateObject()
				.getTime());
		// Contact
		Contact contact = obj.getContact();
		if (contact != null) {
			enterBill.put("contact", context.get("Contact", contact.getID()));
		}
		{
			Set<Estimate> estimates = obj.getEstimates();
			JSONArray array = new JSONArray();
			if (!estimates.isEmpty()) {
				for (Estimate estimate : estimates) {
					if (estimate.getEstimateType() == 1) {
						JSONObject quoteJson = new JSONObject();
						quoteJson
								.put("id",
										context.get("SalesQuotation",
												estimate.getID()));
						array.put(quoteJson);
					}
				}
				if (array.length() > 0) {
					enterBill.put("salesQuotations", array);
				}
			}
		}
		List<PurchaseOrder> purchaseOrders = obj.getPurchaseOrders();
		JSONArray ordersArray = new JSONArray();
		if (!purchaseOrders.isEmpty()) {
			for (PurchaseOrder order : purchaseOrders) {
				JSONObject orderJson = new JSONObject();
				orderJson
						.put("id", context.get("PurchaseOrder", order.getID()));
				ordersArray.put(orderJson);
			}
			if (ordersArray.length() > 0) {
				enterBill.put("purchaseOrders", ordersArray);
			}
		}
		return enterBill;
	}
}
