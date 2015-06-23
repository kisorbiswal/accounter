package com.vimukti.accounter.migration;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;

public class CustomerMigrator implements IMigrator<Customer> {

	@Override
	public JSONObject migrate(Customer obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		PriceLevel priceLevel = obj.getPriceLevel();
		SalesPerson salesPerson = obj.getSalesPerson();
		CustomerGroup customerGroup = obj.getCustomerGroup();
		if (priceLevel != null) {
			jsonObject.put("priceLevel",
					context.get("PriceLevel", priceLevel.getID()));
		}
		if (salesPerson != null) {
			jsonObject.put("salesPerson",
					context.get("SalesPerson", salesPerson.getID()));
		}
		jsonObject.put("customerGroup",
				context.get("CustomerGroup", customerGroup.getID()));
		jsonObject.put("cSTNumber", obj.getCSTno());
		jsonObject.put("taxPayerIdentificationNo", obj.getTINNumber());

		// RelationShip field
		jsonObject.put("identification", obj.getNumber());
		// AutoIdentification , mrOrMs, jobTitle are not found
		jsonObject.put("name", obj.getName());
		jsonObject.put("comments", obj.getMemo());
		jsonObject.put("email", obj.getEmail());
		jsonObject.put("phone", obj.getPhoneNo());
		// MobilePhone and homePhone is not found
		jsonObject.put("fax", obj.getFaxNo());
		JSONObject jsonAddress = new JSONObject();
		Set<Address> address = obj.getAddress();
		for (Address primaryAddress : address) {
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
		if (!address.isEmpty()) {
			jsonObject.put("address", jsonAddress);
		}
		jsonObject.put("inActive", !obj.isActive());

		// BussinessRelationShip Fields
		jsonObject.put("companyName", obj.getCompany().getTradingName());
		jsonObject.put("payeeSince", obj.getPayeeSince());
		jsonObject.put("webAddress", obj.getWebPageAddress());

		// altEmail and altPhone are not found
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
		// emailPreference is not found
		// printOnCheckAs is not found
		// sendTransactionViaEmail is not found
		// sendTransactionViaPrint is not found
		// sendTransactionViaFax is not found
		jsonObject.put("currency", obj.getCurrency().getFormalName());
		jsonObject.put("currencyFactor", obj.getCurrencyFactor());
		Account account = obj.getAccount();
		if (account != null) {
			jsonObject.put("account", context.get("Account", account.getID()));
		}
		jsonObject.put("since", obj.getPayeeSince().getAsDateObject());
		jsonObject.put("creditLimit", obj.getCreditLimit());
		jsonObject.put("bankName", obj.getBankName());
		jsonObject.put("bankAccountNumber", obj.getBankAccountNo());
		jsonObject.put("bankBranch", obj.getBankBranch());
		jsonObject.put("serviceTaxRegistrationNo",
				obj.getServiceTaxRegistrationNo());
		// taxRegistrationNumber is not found
		TAXCode taxCode = obj.getTAXCode();
		if (taxCode != null) {
			jsonObject.put("taxCode", context.get("TaxCode", taxCode.getID()));
		}
		jsonObject.put("paymentMethod", obj.getPaymentMethod());
		jsonObject.put("paymentTerm",
				context.get("PaymentTerm", obj.getPaymentTerm().getID()));
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
