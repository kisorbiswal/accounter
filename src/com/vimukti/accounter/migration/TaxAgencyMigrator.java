package com.vimukti.accounter.migration;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXAgency;

public class TaxAgencyMigrator implements IMigrator<TAXAgency> {

	@Override
	public JSONObject migrate(TAXAgency obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", obj.getName());
		// Setting Purchase Liability Account of company
		jsonObject.put("purchaseLiabilityAccount",context.get("Account", obj.getPurchaseLiabilityAccount().getID()));
		// Setting Sales Liability Account of Company
		jsonObject.put("salesLiability Account", context.get("Account", obj.getSalesLiabilityAccount().getID()));
		// Setting Filed Liability Account of Company
		jsonObject.put("filedLiabilityAccount", context.get("Account",obj.getFiledLiabilityAccount().getID()));
		// TaxType is A PickList TODO
		jsonObject.put("taxType", obj.getTaxType());
		//This is Property not found
		//jsonObject.put("offsetSalesTaxFromPurchaseTax", null);
		jsonObject.put("isInactive", !obj.isActive());
		jsonObject.put("lastFileTaxDate",obj.getLastTAXReturnDate().getAsDateObject());
		//Setting object PaymentTerm  
		jsonObject.put("paymentTerm", context.get("PaymentTerm", obj.getPaymentTerm().getID()));
		return jsonObject;
	}

}