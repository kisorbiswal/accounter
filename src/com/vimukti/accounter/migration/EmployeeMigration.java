package com.vimukti.accounter.migration;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.utils.HibernateUtil;

public class EmployeeMigration implements IMigrator<Employee> {

	@Override
	public JSONObject migrate(Employee obj, MigratorContext context)
			throws JSONException {
		JSONObject employee = new JSONObject();
		employee.put("pANorEIN", obj.getPANno());
		EmployeeGroup group = obj.getGroup();
		if (group != null) {
			employee.put("employeeGroup",
					context.get("EmployeeGroup", group.getID()));
		}
		employee.put("designation", obj.getDesignation());
		employee.put("workingLocation", obj.getLocation());
		Session session = HibernateUtil.getCurrentSession();
		Criteria createCriteria = session.createCriteria(PayStructure.class,
				"obj");
		createCriteria.add(Restrictions.eq("company", context.getCompany()
				.getId()));
		createCriteria.add(Restrictions.eq("employee", obj.getID()));
		PayStructure uniqueResult = (PayStructure) createCriteria
				.uniqueResult();
		employee.put("payStructure",
				context.get("PayStructure", uniqueResult.getID()));
		// RelationShip field
		employee.put("identification", obj.getNumber());
		// AutoIdentification , mrOrMs, jobTitle are not found
		employee.put("name", obj.getName());
		employee.put("comments", obj.getMemo());
		employee.put("email", obj.getEmail());
		employee.put("phone", obj.getPhoneNo());
		// MobilePhone and homePhone is not found
		employee.put("fax", obj.getFaxNo());
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
		employee.put("address", jsonAddress);
		employee.put("inActive", !obj.isActive());

		// BussinessRelationShip Fields
		employee.put("companyName", obj.getCompany().getTradingName());
		employee.put("payeeSince", obj.getPayeeSince());
		employee.put("webAddress", obj.getWebPageAddress());
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
		employee.put("contacts", jsonContacts);
		// emailPreference is not found
		// printOnCheckAs is not found
		// sendTransactionViaEmail is not found
		// sendTransactionViaPrint is not found
		// sendTransactionViaFax is not found
		employee.put("currency", obj.getCurrency());
		employee.put("currencyFactor", obj.getCurrencyFactor());
		Account account = obj.getAccount();
		if (account != null) {
			employee.put("account", context.get("Account", account.getID()));
		}
		employee.put("since", obj.getPayeeSince().getAsDateObject());
		employee.put("bankName", obj.getBankName());
		employee.put("bankAccountNumber", obj.getBankAccountNo());
		employee.put("bankBranch", obj.getBankBranch());
		employee.put("serviceTaxRegistrationNo",
				obj.getServiceTaxRegistrationNo());
		// taxRegistrationNumber is not found
		TAXCode taxCode = obj.getTAXCode();
		if (taxCode != null) {
			employee.put("taxCode", context.get("TaxCode", taxCode.getID()));
		}
		employee.put(
				"paymentMethod",
				context.getPickListContext().get("AccountType",
						obj.getPaymentMethod()));
		// shipTo and billTo are not found
		employee.put("vATRegistrationNumber", obj.getVATRegistrationNumber());
		TAXItem taxItem = obj.getTAXItem();
		if (taxItem != null) {
			employee.put("taxItem", context.get("TAXItem", taxItem.getID()));
		}
		// modeOfTransport is not found
		employee.put("openingBalance", obj.getOpeningBalance());
		// journalEntry is not found

		return employee;
	}
}
