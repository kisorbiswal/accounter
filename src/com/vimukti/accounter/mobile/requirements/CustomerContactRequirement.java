package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientContact;

public class CustomerContactRequirement extends
		AbstractRequirement<ClientContact> {

	private static final String PROCESS_ATTR = "processAttr";
	private static final String OLD_CONTACT_ATTR = "oldContact";
	private static final String CONTACT_LINE_ATTR = "contactLineAttr";
	private static final String CONTACT_ACTIONS = "contactActions";
	private static final String CONTACT_NAME = "contactName";
	private static final String TITLE = "title";
	private static final String BUSINESS_PHONE = "businessPhone";
	private static final String EMAIL = "email";
	private static final String IS_PRIMARY = "isPrimary";

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
				e.add("", getConstants().contacts());
				e.add("", clientContacts.size() + getConstants().contacts());
				list.add(e);
				return null;
			}
		}
		context.setAttribute(INPUT_ATTR, getName());
		Result result = new Result();

		ResultList contactsList = new ResultList(getName());
		if (clientContacts.isEmpty()) {
			addFirstMessage(context,
					getMessages().thereAreNo(getConstants().contacts()));
			// return contactProcess(context);
		} else {
			result.add(getMessages().all(getConstants().contact()));
		}
		for (ClientContact clientContact : clientContacts) {
			Record record = new Record(clientContact);
			record.add("", clientContact.toString());
			contactsList.add(record);
		}
		result.add(contactsList);
		ResultList actionsList = new ResultList(getName() + ACTIONS);
		Record e = new Record(ActionNames.ADD_MORE_CONTACTS);
		e.add("", getMessages().addNew(getConstants().contact()));
		actionsList.add(e);
		e = new Record(ActionNames.FINISH);
		e.add("", getConstants().finish());
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
			if (lineAttr.equals(CONTACT_NAME)) {
				if (oldContact == null) {
					oldContact = new ClientContact();
					List<ClientContact> contacts = getValue();
					contacts.add(oldContact);
					context.setAttribute(OLD_CONTACT_ATTR, oldContact);
				}
				oldContact.setName(input);
			} else if (lineAttr.equals(TITLE)) {
				oldContact.setTitle(input);
			} else if (lineAttr.equals(BUSINESS_PHONE)) {
				input = context.getNumber();
				oldContact.setBusinessPhone(input);
			} else if (lineAttr.equals(EMAIL)) {
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
				selection = CONTACT_NAME;
			}
			if (selection != null) {
				if (selection.equals(IS_PRIMARY)) {
					List<ClientContact> clientContacts = getValue();
					for (ClientContact cc : clientContacts) {
						cc.setPrimary(false);
					}
					oldContact.setPrimary(!oldContact.isPrimary());
				} else if (selection == CONTACT_NAME) {
					context.setAttribute(CONTACT_LINE_ATTR, CONTACT_NAME);
					String contact = oldContact != null ? oldContact.getName()
							: null;
					return show(
							context,
							getMessages().pleaseEnter(
									getConstants().contactName()), contact,
							contact);
				} else if (selection == TITLE) {
					context.setAttribute(CONTACT_LINE_ATTR, TITLE);
					String title = oldContact != null ? oldContact.getTitle()
							: null;
					return show(context,
							getMessages().pleaseEnter(getConstants().title()),
							title, title);
				} else if (selection == BUSINESS_PHONE) {
					context.setAttribute(CONTACT_LINE_ATTR, BUSINESS_PHONE);
					String phone = oldContact != null ? oldContact
							.getBusinessPhone() : null;
					return show(
							context,
							getMessages().pleaseEnter(
									getConstants().businessPhone()), phone,
							phone);
				} else if (selection == EMAIL) {
					context.setAttribute(CONTACT_LINE_ATTR, EMAIL);
					String email = oldContact != null ? oldContact.getEmail()
							: null;
					return show(context,
							getMessages().pleaseEnter(getConstants().email()),
							email, email);
				}
			}
		}

		ResultList list = new ResultList(CONTACT_LINE_ATTR);
		Record record = new Record(IS_PRIMARY);
		record.add("", oldContact.isPrimary() ? getConstants().primary()
				: getConstants().notPrimary());
		list.add(record);

		record = new Record(CONTACT_NAME);
		record.add("", getConstants().contactName());
		record.add("", oldContact.getName());
		list.add(record);

		record = new Record(TITLE);
		record.add("", getConstants().title());
		record.add("", oldContact.getTitle());
		list.add(record);

		record = new Record(BUSINESS_PHONE);
		record.add("", getConstants().businessPhone());
		record.add("", oldContact.getBusinessPhone());
		list.add(record);

		record = new Record(EMAIL);
		record.add("", getConstants().email());
		record.add("", oldContact.getEmail());
		list.add(record);

		Result result = context.makeResult();
		result.add(list);

		ResultList finish = new ResultList(CONTACT_ACTIONS);
		record = new Record(ActionNames.DELETE_CONTACT);
		record.add("", getConstants().delete());
		finish.add(record);

		record = new Record(ActionNames.FINISH);
		record.add("", getConstants().finish());
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
