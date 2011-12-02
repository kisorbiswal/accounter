package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;

public class NewContactCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (!string.isEmpty()) {
			Customer customer = (Customer) CommandUtils.getServerObjectById(
					Long.parseLong(string), AccounterCoreType.CUSTOMER);
			get("Customer").setValue(customer);
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "Contact created successfully";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement("Name", "Please enter name", "Name",
				false, true));

		list.add(new StringRequirement("Title", "Please enter title", "Title",
				true, true));

		list.add(new PhoneRequirement("BusinessPhone",
				"Please enter Business Phone", "Phone", true, true));

		list.add(new EmailRequirement("Email", "Please enter Email", "Email",
				true, true));

		list.add(new CustomerRequirement("Customer", "Please select customer",
				"Related Customer", false, true, null) {

			@Override
			protected List<Customer> getLists(Context context) {
				return new ArrayList<Customer>(context.getCompany()
						.getCustomers());
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientContact contact = new ClientContact();
		String name = get("Name").getValue();
		String title = get("Title").getValue();
		String phone = get("BusinessPhone").getValue();
		String email = get("Email").getValue();
		contact.setName(name);
		contact.setTitle(title);
		contact.setBusinessPhone(phone);
		contact.setEmail(email);
		Customer customer = get("Customer").getValue();
		ClientCustomer clientCustomer = (ClientCustomer) CommandUtils
				.getClientObjectById(customer.getID(),
						AccounterCoreType.CUSTOMER, getCompanyId());
		clientCustomer.addContact(contact);
		create(clientCustomer, context);
		return null;
	}
}
