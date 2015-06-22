package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.Vendor;

public class CreditCardExpenseMigrator extends
		TransactionMigrator<CreditCardCharge> {

	@Override
	public JSONObject migrate(CreditCardCharge creditCardCharge,
			MigratorContext context) throws JSONException {
		JSONObject creditCardChargeJSON = super.migrate(creditCardCharge,
				context);
		Contact contact = creditCardCharge.getContact();
		if (contact != null) {
			creditCardChargeJSON.put("contact",
					context.get("Contact", contact.getID()));

		}
		Vendor vendor = creditCardCharge.getVendor();
		if (vendor != null) {
			creditCardChargeJSON.put("payee",
					context.get("Vendor", vendor.getID()));

		}
		creditCardChargeJSON.put("paymentMethod", context.getPickListContext()
				.get("PaymentMethod", creditCardCharge.getPaymentMethod()));
		Long chequeNumber = null;
		try {
			chequeNumber = Long.valueOf(creditCardCharge.getCheckNumber());
			creditCardChargeJSON.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		creditCardChargeJSON.put("memo", creditCardCharge.getMemo());
		creditCardChargeJSON.put("account",
				context.get("Account", creditCardCharge.getPayFrom().getID()));
		return creditCardChargeJSON;

	}
}
