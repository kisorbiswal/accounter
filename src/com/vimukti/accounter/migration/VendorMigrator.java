package com.vimukti.accounter.migration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorGroup;

public class VendorMigrator implements IMigrator<Vendor> {

	@Override
	public JSONObject migrate(Vendor obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		VendorGroup vendorGroup = obj.getVendorGroup();
		if (vendorGroup != null) {
			jsonObject.put("vendorGroup",
					context.get("VendorGroup", vendorGroup.getID()));
		}

		TAXItem taxItem2 = obj.getTAXItem();
		if (taxItem2 != null) {
			jsonObject.put("vendorTDSCode",
					context.get("TaxtItem", taxItem2.getID()));
		}

		// RelationShip field
		jsonObject.put("identification", obj.getVendorNumber());
		jsonObject.put("name", obj.getName());
		jsonObject.put("comments", obj.getMemo());
		jsonObject.put("email", obj.getEmail());
		jsonObject.put("phone", obj.getPhoneNo());
		jsonObject.put("fax", obj.getFaxNo());
		JSONObject jsonAddress = new JSONObject();
		for (Address primaryAddress : obj.getAddress()) {
			if (primaryAddress.isSelected()) {
				jsonAddress.put("street", primaryAddress.getStreet());
				jsonAddress.put("city", primaryAddress.getCity());
				jsonAddress.put("stateOrProvince",
						primaryAddress.getStateOrProvinence());
				jsonAddress.put("zipOrPostalCode",
						primaryAddress.getZipOrPostalCode());
				jsonAddress.put("country", primaryAddress.getCountryOrRegion());
			}
		}
		jsonObject.put("address", jsonAddress);
		jsonObject.put("inActive", !obj.isActive());

		// BussinessRelationShip Fields
		jsonObject.put("companyName", obj.getCompany().getTradingName());
		jsonObject.put("payeeSince", obj.getPayeeSince());
		jsonObject.put("webAddress", obj.getWebPageAddress());
		JSONArray jsonContacts = new JSONArray();
		for (Contact contact : obj.getContacts()) {
			JSONObject jsonContact = new JSONObject();
			jsonContact.put("isPrimary", contact.isPrimary());
			jsonContact.put("contactName", contact.getName());
			jsonContact.put("title", contact.getTitle());
			jsonContact.put("businessPhone", contact.getBusinessPhone());
			jsonContact.put("email", contact.getEmail());
			jsonContacts.put(jsonContact);
		}
		jsonObject.put("contacts", jsonContacts);
		PaymentTerms paymentTerms = obj.getPaymentTerms();
		if (paymentTerms != null) {
			jsonObject.put("paymentTerm",
					context.get("PaymentTerms", paymentTerms.getID()));
		}
		if (obj.getCurrency() != null) {
			jsonObject.put("currency", obj.getCurrency().getName());
		}
		jsonObject.put("currencyFactor", obj.getCurrencyFactor());
		Account account = obj.getAccount();
		if (account != null) {
			jsonObject.put("account", context.get("Account", account.getID()));
		}
		jsonObject
				.put("since", obj.getPayeeSince().getAsDateObject().getTime());
		jsonObject.put("creditLimit", obj.getCreditLimit());
		jsonObject.put("bankName", obj.getBankName());
		jsonObject.put("bankAccountNumber", obj.getBankAccountNo());
		jsonObject.put("bankBranch", obj.getBankBranch());
		jsonObject.put("serviceTaxRegistrationNo",
				obj.getServiceTaxRegistrationNo());
		jsonObject.put("taxId", obj.getTaxId());
		TAXCode taxCode = obj.getTAXCode();
		if (taxCode != null) {
			jsonObject.put("taxCode", context.get("TaxCode", taxCode.getID()));
		}
		jsonObject.put("paymentMethod", obj.getPaymentMethod());
		jsonObject.put("tDSApplicable", obj.isTdsApplicable());
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			jsonObject.put("preferredShippingMethod",
					context.get("ShippingMethod", shippingMethod.getID()));
		}
		jsonObject.put("primaryContact",
				context.get("Contact", obj.getPrimaryContact().getID()));
		jsonObject.put("vATRegistrationNumber", obj.getVATRegistrationNumber());
		TAXItem taxItem = obj.getTAXItem();
		if (taxItem != null) {
			jsonObject.put("taxItem", context.get("TAXItem", taxItem.getID()));
		}
		jsonObject.put("openingBalance", obj.getOpeningBalance());
		return jsonObject;
	}

}
