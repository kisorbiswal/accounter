package com.vimukti.accounter.migration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.FinanceDate;
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
		jsonObject.put("name", obj.getName());
		jsonObject.put("comments", obj.getMemo());
		jsonObject.put("email", obj.getEmail());
		jsonObject.put("phone", obj.getPhoneNo());
		jsonObject.put("fax", obj.getFaxNo());
		// Addresses
		Address shipToAddress = null;
		Address billToAddress = null;
		for (Address primaryAddress : obj.getAddress()) {
			if (primaryAddress.getType() == Address.TYPE_BILL_TO) {
				billToAddress = primaryAddress;
			}
			if (primaryAddress.getType() == Address.TYPE_SHIP_TO) {
				shipToAddress = primaryAddress;
			}
		}
		// SHIP TO
		if (shipToAddress != null) {
			JSONObject selectedAddress = new JSONObject();
			selectedAddress.put("street", shipToAddress.getStreet());
			selectedAddress.put("city", shipToAddress.getCity());
			selectedAddress.put("stateOrProvince",
					shipToAddress.getStateOrProvinence());
			selectedAddress.put("zipOrPostalCode",
					shipToAddress.getZipOrPostalCode());
			selectedAddress.put("country", shipToAddress.getCountryOrRegion());
			jsonObject.put("shipTo", selectedAddress);
		}
		// BILL TO
		if (billToAddress != null) {
			JSONObject billTOaddr = new JSONObject();
			billTOaddr.put("street", billToAddress.getStreet());
			billTOaddr.put("city", billToAddress.getCity());
			billTOaddr.put("stateOrProvince",
					billToAddress.getStateOrProvinence());
			billTOaddr.put("zipOrPostalCode",
					billToAddress.getZipOrPostalCode());
			billTOaddr.put("country", billToAddress.getCountryOrRegion());
			jsonObject.put("billTo", billTOaddr);
		}
		jsonObject.put("inActive", !obj.isActive());

		// BussinessRelationShip Fields
		jsonObject.put("companyName", obj.getCompany().getTradingName());
		FinanceDate payeeSince = obj.getPayeeSince();
		if (payeeSince != null) {
			jsonObject
					.put("payeeSince", payeeSince.getAsDateObject().getTime());
		}
		jsonObject.put("webAddress", obj.getWebPageAddress());
		// contacts
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
					context.get("PaymentTerm", paymentTerms.getID()));
		}
		// currency
		JSONObject currencyJSON = new JSONObject();
		currencyJSON.put("identity", obj.getCurrency().getFormalName());
		jsonObject.put("currency", currencyJSON);
		jsonObject.put("currencyFactor", obj.getCurrencyFactor());
		Account account = obj.getAccount();
		if (account != null) {
			jsonObject.put("account", context.get("Account", account.getID()));
		}
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
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		}
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			jsonObject.put("preferredShippingMethod",
					context.get("ShippingMethod", shippingMethod.getID()));
		}
		Contact primaryContact = obj.getPrimaryContact();
		if (primaryContact != null) {
			jsonObject.put("primaryContact",
					context.get("Contact", primaryContact.getID()));
		}
		jsonObject.put("vATRegistrationNumber", obj.getVATRegistrationNumber());

		jsonObject.put("tDSApplicable", obj.isTdsApplicable());
		// Tds Applicable is enble then only set taxItem
		TAXItem taxItem = obj.getTAXItem();
		if (taxItem != null) {
			jsonObject.put("taxItem", context.get("TAXItem", taxItem.getID()));
		}
		jsonObject.put("openingBalance", obj.getOpeningBalance());
		return jsonObject;
	}

}
