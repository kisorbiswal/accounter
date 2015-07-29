package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;

public class CustomerMigrator implements IMigrator<Customer> {

	@Override
	public JSONObject migrate(Customer obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		PriceLevel priceLevel = obj.getPriceLevel();
		SalesPerson salesPerson = obj.getSalesPerson();
		CustomerGroup customerGroup = obj.getCustomerGroup();
		jsonObject.put("identification", obj.getNumber());
		if (priceLevel != null) {
			jsonObject.put("priceLevel",
					context.get("PriceLevel", priceLevel.getID()));
		}
		if (salesPerson != null) {
			jsonObject.put("salesPerson",
					context.get("SalesPerson", salesPerson.getID()));
		}
		if (customerGroup != null) {
			jsonObject.put("customerGroup",
					context.get("CustomerGroup", customerGroup.getID()));
		}
		jsonObject.put("cSTNumber", obj.getCSTno());

		jsonObject.put("taxPayerIdentificationNo", obj.getTINNumber());
		// AutoIdentification , mrOrMs, jobTitle are not found
		jsonObject.put("name", obj.getName());
		jsonObject.put("comments", obj.getMemo());
		jsonObject.put("email", obj.getEmail());
		jsonObject.put("phone", obj.getPhoneNo());
		// MobilePhone and homePhone is not found
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

		// altEmail and altPhone are not found
		JSONArray jsonContacts = new JSONArray();
		Map<String, List<Long>> childrenMap = context.getChildrenMap();
		String key = "contacts-Contact";
		List<Long> list = childrenMap.get(key);
		if (list == null) {
			list = new ArrayList<Long>();
			childrenMap.put(key, list);
		}
		for (Contact contact : obj.getContacts()) {
			JSONObject jsonContact = new JSONObject();
			jsonContact.put("contactName", contact.getName());
			jsonContact.put("title", contact.getTitle());
			jsonContact.put("businessPhone", contact.getBusinessPhone());
			jsonContact.put("email", contact.getEmail());
			jsonContacts.put(jsonContact);
			list.add(contact.getID());
		}
		jsonObject.put("contacts", jsonContacts);
		// emailPreference is not found
		// printOnCheckAs is not found
		// sendTransactionViaEmail is not found
		// sendTransactionViaPrint is not found
		// sendTransactionViaFax is not found

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

		// taxRegistrationNumber is not found
		TAXCode taxCode = obj.getTAXCode();
		if (taxCode != null) {
			jsonObject.put("taxCode", context.get("TaxCode", taxCode.getID()));
		}
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "Cash");
		}
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			jsonObject.put("paymentTerm",
					context.get("PaymentTerm", paymentTerm.getID()));
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
		jsonObject.put("openingBalance", obj.getOpeningBalance());

		return jsonObject;
	}
}
