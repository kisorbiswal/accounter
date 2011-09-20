package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class AbstractCommand extends Command {
	protected static final String INPUT_ATTR = "input";

	protected static final String DATE = "date";
	protected static final String NUMBER = "number";
	protected static final String TEXT = "text";
	protected static final String ADDRESS = "address";
	protected static final String ACTIONS = "actions";
	protected static final String ADDRESS_PROCESS = "addressProcess";
	protected static final String PROCESS_ATTR = "process";
	protected static final String ADDRESS_MESSAGE_ATTR = "addressMessage";
	protected static final String OLD_ADDRESS_ATTR = "oldAddress";
	protected static final String ADDRESS_LINE_ATTR = null;
	protected static final String CONTACT_ATTR = "contact";
	protected static final String OLD_CONTACT_ATTR = "oldContact";
	protected static final String CONTACT_LINE_ATTR = null;
	protected static final String CONTACT_PROCESS = "contactProcess";
	protected static final String CONTACTS = "contact";
	protected static final String TAXCODE = null;
	private static final int TAXCODE_TO_SHOW = 0;

	protected Result text(Context context, String message, String oldText) {
		Result result = context.makeResult();
		result.add(message);
		if (oldText != null) {
			ResultList list = new ResultList(TEXT);
			Record record = new Record(oldText);
			record.add("", oldText);
			list.add(record);
			result.add(list);
		}
		return result;
	}

	protected Result amount(Context context, String message, Double oldAmount) {
		return number(context, message, oldAmount.toString());
	}

	protected Company getCompany() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result number(Context context, String message, String oldNumber) {
		Result result = context.makeResult();
		result.add(message);
		if (oldNumber != null) {
			ResultList list = new ResultList(NUMBER);
			Record record = new Record(oldNumber);
			record.add("", oldNumber);
			list.add(record);
			result.add(list);
		}
		return result;
	}

	protected Result date(Context context, String message, Date date) {
		Result result = context.makeResult();
		result.add(message);
		if (date != null) {
			ResultList list = new ResultList(DATE);
			Record record = new Record(date);
			record.add("", date.toString());
			list.add(record);
			result.add(list);
		}
		return result;
	}

	protected Result addressProcess(Context context) {
		String message = (String) context.getAttribute(ADDRESS_MESSAGE_ATTR);
		Address address = (Address) context.getAttribute(OLD_ADDRESS_ATTR);
		return address(context, message, address);
	}

	protected Result address(Context context, String message, Address oldAddress) {
		context.setAttribute(PROCESS_ATTR, ADDRESS_PROCESS);
		context.setAttribute(ADDRESS_MESSAGE_ATTR, message);
		context.setAttribute(OLD_ADDRESS_ATTR, oldAddress);

		String lineAttr = (String) context.getAttribute(ADDRESS_LINE_ATTR);
		if (lineAttr != null) {
			String input = context.getString();
			context.removeAttribute(ADDRESS_LINE_ATTR);
			if (lineAttr.equals("address1")) {
				oldAddress.setAddress1(input);
			} else if (lineAttr.equals("city")) {
				oldAddress.setCity(input);
			} else if (lineAttr.equals("street")) {
				oldAddress.setStreet(input);
			} else if (lineAttr.equals("stateOrProvinence")) {
				oldAddress.setStateOrProvinence(input);
			} else if (lineAttr.equals("countryOrRegion")) {
				oldAddress.setCountryOrRegion(input);
			}
		} else {
			Object selection = context.getSelection(ADDRESS);
			if (selection != null) {
				if (selection == oldAddress.getAddress1()) {
					context.setAttribute(ADDRESS_LINE_ATTR, "address1");
					return text(context, "Enter Address1",
							oldAddress.getAddress1());
				} else if (selection == oldAddress.getCity()) {
					context.setAttribute(ADDRESS_LINE_ATTR, "city");
					return text(context, "Enter City", oldAddress.getCity());
				} else if (selection == oldAddress.getStreet()) {
					context.setAttribute(ADDRESS_LINE_ATTR, "street");
					return text(context, "Enter Street", oldAddress.getStreet());
				} else if (selection == oldAddress.getStateOrProvinence()) {
					context.setAttribute(ADDRESS_LINE_ATTR, "stateOrProvinence");
					return text(context, "Enter State/Provinence",
							oldAddress.getStateOrProvinence());
				} else if (selection == oldAddress.getCountryOrRegion()) {
					context.setAttribute(ADDRESS_LINE_ATTR, "countryOrRegion");
					return text(context, "Enter Country/Region",
							oldAddress.getCountryOrRegion());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH) {
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(ADDRESS_MESSAGE_ATTR);
					context.removeAttribute(OLD_ADDRESS_ATTR);
					context.removeAttribute(ADDRESS_LINE_ATTR);// No need
					return null;
				}
			}
		}

		ResultList list = new ResultList(ADDRESS);
		Record record = new Record(oldAddress.getAddress1());
		record.add("", "Address1");
		record.add("", oldAddress.getAddress1());
		list.add(record);

		record = new Record(oldAddress.getStreet());
		record.add("", "Streat");
		record.add("", oldAddress.getStreet());
		list.add(record);

		record = new Record(oldAddress.getCity());
		record.add("", "City");
		record.add("", oldAddress.getCity());
		list.add(record);

		record = new Record(oldAddress.getStateOrProvinence());
		record.add("", "State/Provinence");
		record.add("", oldAddress.getStateOrProvinence());
		list.add(record);

		record = new Record(oldAddress.getCountryOrRegion());
		record.add("", "Country/Region");
		record.add("", oldAddress.getCountryOrRegion());
		list.add(record);

		Result result = context.makeResult();
		result.add(message);

		result.add(list);
		result.add("Select any line to edit");

		ResultList finish = new ResultList(ACTIONS);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		finish.add(record);
		return result;
	}

	private Record createTaxCodeRecord(TAXCode taxCode) {
		Record record = new Record(taxCode);
		record.add("", taxCode.getName());
		return record;
	}

	private List<TAXCode> getTaxCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result taxCode(Context context, TAXCode oldTaxCode) {
		Result result = context.makeResult();
		List<TAXCode> codes = getTaxCodes();
		ResultList list = new ResultList(TAXCODE);
		int num = 0;
		if (oldTaxCode != null) {
			list.add(createTaxCodeRecord(oldTaxCode));
			num++;
		}
		for (TAXCode code : codes) {
			if (code != oldTaxCode) {
				list.add(createTaxCodeRecord(code));
				num++;
			}
			if (num == TAXCODE_TO_SHOW) {
				break;
			}
		}

		CommandList commands = new CommandList();
		commands.add("Create New Taxcode");
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	protected Result contactProcess(Context context) {
		String message = (String) context.getAttribute(CONTACT_ATTR);
		Contact contact = (Contact) context.getAttribute(OLD_CONTACT_ATTR);
		return contact(context, message, contact);
	}

	/**
	 * 
	 * @param context
	 * @param message
	 * @param oldContact
	 * @return {@link Result}
	 */
	protected Result contact(Context context, String message, Contact oldContact) {
		context.setAttribute(PROCESS_ATTR, CONTACT_PROCESS);
		context.setAttribute(CONTACT_ATTR, message);
		context.setAttribute(OLD_CONTACT_ATTR, oldContact);

		String lineAttr = (String) context.getAttribute(CONTACT_LINE_ATTR);
		if (lineAttr != null) {
			String input = context.getString();
			context.removeAttribute(CONTACT_LINE_ATTR);
			if (lineAttr.equals("contactName")) {
				oldContact.setName(input);
			} else if (lineAttr.equals("title")) {
				oldContact.setTitle(input);
			} else if (lineAttr.equals("businessPhone")) {
				oldContact.setBusinessPhone(input);
			} else if (lineAttr.equals("email")) {
				oldContact.setEmail(input);
			}
		} else {
			Object selection = context.getSelection(CONTACTS);
			if (selection != null) {

				if (selection.equals("isActive")) {
					oldContact.setPrimary(!oldContact.isPrimary());
				} else if (selection == oldContact.getName()) {
					context.setAttribute(CONTACT_LINE_ATTR, "contactName");
					return text(context, "Enter conatactName",
							oldContact.getName());
				} else if (selection == oldContact.getTitle()) {
					context.setAttribute(CONTACT_LINE_ATTR, "title");
					return text(context, "Enter Title", oldContact.getTitle());
				} else if (selection == oldContact.getBusinessPhone()) {
					context.setAttribute(CONTACT_LINE_ATTR, "businessPhone");
					return text(context, "Enter Businessphone Number ",
							oldContact.getBusinessPhone());
				} else if (selection == oldContact.getEmail()) {
					context.setAttribute(CONTACT_LINE_ATTR, "email");
					return text(context, "Enter Email", oldContact.getEmail());
				} else {
					selection = context.getSelection(ACTIONS);
					if (selection == ActionNames.FINISH) {
						context.removeAttribute(PROCESS_ATTR);
						context.removeAttribute(CONTACT_ATTR);
						context.removeAttribute(OLD_ADDRESS_ATTR);
						context.removeAttribute(CONTACT_LINE_ATTR);// No need
						return null;
					}
				}
			}
		}
		ResultList list = new ResultList(CONTACTS);
		Record record = new Record("isActive");
		record.add("", "IsActive");
		record.add("", oldContact.isPrimary());
		list.add(record);

		record = new Record(oldContact.getName());
		record.add("", "contactName");
		record.add("", oldContact.getName());
		list.add(record);

		record = new Record(oldContact.getTitle());
		record.add("", "title");
		record.add("", oldContact.getTitle());
		list.add(record);

		record = new Record(oldContact.getBusinessPhone());
		record.add("", "businessPhone");
		record.add("", oldContact.getBusinessPhone());
		list.add(record);

		record = new Record(oldContact.getEmail());
		record.add("", "email");
		record.add("", oldContact.getEmail());
		list.add(record);

		Result result = context.makeResult();
		result.add(message);

		result.add(list);
		result.add("Select any line to edit");

		ResultList finish = new ResultList(ACTIONS);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		finish.add(record);
		return result;
	}

	protected void create(IAccounterServerCore obj, Context context) {
		// User user = context.getUser();
		// Session session = context.getSession();
		// try {
		// new FinanceTool().createServerObject(obj, user, session);
		// } catch (AccounterException e) {
		// e.printStackTrace();
		// }
	}
}
