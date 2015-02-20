package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.PaymentTerms;

public class EnterBillMigrator extends TransactionMigrator<EnterBill> {

	@Override
	public JSONObject migrate(EnterBill obj, MigratorContext context)
			throws JSONException {
		JSONObject enterBill = super.migrate(obj, context);
		enterBill.put("payee", context.get("Vendor", obj.getVendor().getID()));
		enterBill.put("isReconciled", "");
		enterBill.put("dueDate", obj.getDueDate().getAsDateObject());
		enterBill.put("phone", obj.getPhone());
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
			enterBill.put("billTo", vendorAddress);
		}
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			enterBill.put("paymentTerm",
					context.get("PaymentTerm", paymentTerm.getID()));
		}
		enterBill.put("deliveryDate", obj.getDeliveryDate().getAsDateObject());
		Contact contact = obj.getContact();
		if (contact != null) {
			enterBill.put("contact", contact.getID());
		}
		return enterBill;
	}
}
