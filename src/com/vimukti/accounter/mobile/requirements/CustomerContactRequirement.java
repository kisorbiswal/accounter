package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AbstractRequirement;
import com.vimukti.accounter.web.client.core.ClientContact;

public class CustomerContactRequirement extends
		AbstractRequirement<ClientContact> {

	private static final String PROCESS_ATTR = "processAttr";
	private static final String OLD_CONTACT_ATTR = "oldContact";
	private static final String CONTACT_LINE_ATTR = "contactLineAttr";
	private static final String CONTACT_ACTIONS = "contactActions";

	public CustomerContactRequirement(String requirementName) {
		super(requirementName, null, null, true, true);
		setDefaultValue(new ArrayList<ClientContact>());
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String attribute = context.getSelection(VALUES);
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(getName())) {
				Result result = contactProcess(context);
				if (result != null) {
					return result;
				}
				attribute = getName();
			}
		}

		Object selection = context.getSelection(getName() + ACTIONS);
		if (selection == ActionNames.ADD_MORE_CONTACTS) {
			return contactProcess(context);
		} else if (selection == ActionNames.FINISH) {
			context.setAttribute(INPUT_ATTR, "");
			attribute = null;
		}

		ClientContact contact = context.getSelection(getName());
		if (contact != null) {
			Result res = contact(context, contact);
			if (res != null) {
				return res;
			}
		}

		List<ClientContact> clientContacts = getValue();
		String attr = (String) context.getAttribute(INPUT_ATTR);
		if (attr != getName()) {
			if (attribute == null || !attribute.equals(getName())) {
				Record e = new Record(getName());
				e.add("", "Contacts");
				e.add("", clientContacts.size() + " Contact(s)");
				list.add(e);
				return null;
			}
		}
		context.setAttribute(INPUT_ATTR, getName());
		Result result = new Result();

		ResultList contactsList = new ResultList(getName());
		if (clientContacts.isEmpty()) {
			addFirstMessage(context, "There are no contacts.");
			// return contactProcess(context);
		} else {
			result.add("All Contacts");
		}
		for (ClientContact clientContact : clientContacts) {
			Record record = new Record(clientContact);
			record.add("",
					clientContact.getName() + "-" + clientContact.getTitle()
							+ "-" + clientContact.getBusinessPhone() + "-"
							+ clientContact.getEmail());
			contactsList.add(record);
		}
		result.add(contactsList);
		ResultList actionsList = new ResultList(getName() + ACTIONS);
		Record e = new Record(ActionNames.ADD_MORE_CONTACTS);
		e.add("", "Add Contact");
		actionsList.add(e);
		e = new Record(ActionNames.FINISH);
		e.add("", "Finish");
		actionsList.add(e);
		result.add(actionsList);
		return result;
	}

	private Result contact(Context context, ClientContact oldContact) {
		context.setAttribute(PROCESS_ATTR, getName());
		context.setAttribute(OLD_CONTACT_ATTR, oldContact);

		String lineAttr = (String) context.getAttribute(CONTACT_LINE_ATTR);
		if (lineAttr != null) {
			String input = context.getString();
			context.removeAttribute(CONTACT_LINE_ATTR);
			if (lineAttr.equals("contactName")) {
				if (oldContact == null) {
					oldContact = new ClientContact();
					List<ClientContact> contacts = getValue();
					contacts.add(oldContact);
					context.setAttribute(OLD_CONTACT_ATTR, oldContact);
				}
				oldContact.setName(input);
			} else if (lineAttr.equals("title")) {
				oldContact.setTitle(input);
			} else if (lineAttr.equals("businessPhone")) {
				input = context.getNumber();
				oldContact.setBusinessPhone(input);
			} else if (lineAttr.equals("email")) {
				oldContact.setEmail(input);
			}

		}

		Object selection = context.getSelection(CONTACT_ACTIONS);

		if (selection == ActionNames.FINISH) {
			context.removeAttribute(PROCESS_ATTR);
			context.removeAttribute(OLD_CONTACT_ATTR);
			context.removeAttribute(CONTACT_LINE_ATTR);// No need
			return null;

		} else if (selection == ActionNames.DELETE_CONTACT) {
			List<ClientContact> contacts = getValue();
			contacts.remove(oldContact);
			context.removeAttribute(PROCESS_ATTR);
			context.removeAttribute(OLD_CONTACT_ATTR);
			context.removeAttribute(CONTACT_LINE_ATTR);// No need
			return null;
		} else {
			selection = context.getSelection(CONTACT_LINE_ATTR);
			if (oldContact == null) {
				selection = "Contact Name";
			}
			if (selection != null) {
				if (selection.equals("isPrimary")) {
					List<ClientContact> clientContacts = getValue();
					for (ClientContact cc : clientContacts) {
						cc.setPrimary(false);
					}
					oldContact.setPrimary(!oldContact.isPrimary());
				} else if (selection == "Contact Name") {
					context.setAttribute(CONTACT_LINE_ATTR, "contactName");
					return show(context, "Enter conatactName",
							oldContact != null ? oldContact.getName() : null);
				} else if (selection == "Title") {
					context.setAttribute(CONTACT_LINE_ATTR, "title");
					return show(context, "Enter Title", oldContact.getTitle());
				} else if (selection == "BusinessPhone") {
					context.setAttribute(CONTACT_LINE_ATTR, "businessPhone");
					return show(context, "Enter Businessphone Number ",
							oldContact.getBusinessPhone());
				} else if (selection == "Email") {
					context.setAttribute(CONTACT_LINE_ATTR, "email");
					return show(context, "Enter Email", oldContact.getEmail());
				}
			}
		}

		ResultList list = new ResultList(CONTACT_LINE_ATTR);
		Record record = new Record("isPrimary");
		record.add("", oldContact.isPrimary() ? "It is primary contact."
				: "This is not primary contact");
		list.add(record);

		record = new Record("Contact Name");
		record.add("", "contactName");
		record.add("", oldContact.getName());
		list.add(record);

		record = new Record("Title");
		record.add("", "title");
		record.add("", oldContact.getTitle());
		list.add(record);

		record = new Record("BusinessPhone");
		record.add("", "businessPhone");
		record.add("", oldContact.getBusinessPhone());
		list.add(record);

		record = new Record("Email");
		record.add("", "email");
		record.add("", oldContact.getEmail());
		list.add(record);

		Result result = context.makeResult();
		result.add(list);

		ResultList finish = new ResultList(CONTACT_ACTIONS);
		record = new Record(ActionNames.DELETE_CONTACT);
		record.add("", "Delete");
		finish.add(record);

		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		finish.add(record);

		result.add(finish);

		return result;
	}

	private Result contactProcess(Context context) {
		ClientContact contact = (ClientContact) context
				.getAttribute(OLD_CONTACT_ATTR);
		return contact(context, contact);
	}
}
