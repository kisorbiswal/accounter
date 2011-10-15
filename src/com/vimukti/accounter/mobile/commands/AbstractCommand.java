package com.vimukti.accounter.mobile.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractCommand extends Command {
	protected static final String INPUT_ATTR = "input";
	protected static final String ACCOUNT_TYPE = "Account Type";
	protected static final String DATE = "dates";
	protected static final String NUMBER = "number";
	protected static final String TEXT = "text";
	protected static final String ADDRESS = "address";
	protected static final String ACTIONS = "actions";
	protected static final String ADDRESS_PROCESS = "addressProcess";
	protected static final String PROCESS_ATTR = "process";
	protected static final String ADDRESS_MESSAGE_ATTR = "addressMessage";
	protected static final String OLD_ADDRESS_ATTR = "oldAddress";
	protected static final String ACCOUNTS_PROCESS = "accountsProcess";
	protected static final String ADDRESS_LINE_ATTR = null;
	protected static final String CONTACT_ATTR = "contact";
	protected static final String OLD_CONTACT_ATTR = "oldContact";
	protected static final String CONTACT_LINE_ATTR = null;
	protected static final String CONTACT_PROCESS = "contactProcess";
	protected static final String CONTACTS = "contact";
	protected static final String TAXCODE = "taxCode";
	private static final int TAXCODE_TO_SHOW = 5;
	protected static final int INCOMEACCOUNTS_TO_SHOW = 5;
	protected static final int VENDORS_TO_SHOW = 3;
	protected static final int ITEMGROUPS_TO_SHOW = 5;
	protected static final String PAYMENT_METHOD = "Payment method";
	private static final int PAYMENTMETHODS_TO_SHOW = 5;
	protected static final int VALUES_TO_SHOW = 5;
	protected static final String ACTIVE = "isActive";
	protected static final String MEMO = "memo";
	protected static final String ORDER_NO = "orderNo";
	protected static final String VIEW_BY = "ViewBy";

	protected static final int STATUS_NOT_ISSUED = 0;
	protected static final int STATUS_PARTIALLY_PAID = 1;
	protected static final int STATUS_ISSUED = 2;
	protected static final int STATUS_VOIDED = 3;
	protected static final int ALL = 4;

	private static final String CONTACT_ACTIONS = "contactActions";

	private static final String REQUIREMENT_NAME = "requirmentName";
	private static final String ADDRESS_ACTIONS = "addressActions";
	private static final String RECORDS_START_INDEX = "recordsStrartIndex";
	protected static final String PAGENATION = null;

	private IGlobal global;
	private AccounterConstants constants;
	private AccounterMessages messages;

	public AbstractCommand() {
		try {
			global = new ServerGlobal();
		} catch (IOException e) {
			e.printStackTrace();
		}
		constants = global.constants();
		messages = global.messages();
	}

	protected Company getCompany() {
		return null;

	}

	protected <T> List<T> pagination(Context context, ActionNames selection,
			List<Record> actions, List<T> records, List<T> skipRecords,
			int recordsToShow) {
		if (selection != null && selection == ActionNames.PREV_PAGE) {
			Integer index = (Integer) context.getAttribute(RECORDS_START_INDEX);
			Integer lastPageSize = (Integer) context
					.getAttribute("LAST_PAGE_SIZE");
			context.setAttribute(RECORDS_START_INDEX,
					index
							- (recordsToShow + (lastPageSize == null ? 0
									: lastPageSize)));
		} else if (selection == null || selection != ActionNames.NEXT_PAGE) {
			context.setAttribute(RECORDS_START_INDEX, 0);
		}

		int num = skipRecords.size();
		Integer index = (Integer) context.getAttribute(RECORDS_START_INDEX);
		if (index == null) {
			index = 0;
		}
		List<T> result = new ArrayList<T>();
		for (int i = index; i < records.size(); i++) {
			if (num == recordsToShow) {
				break;
			}
			T r = records.get(i);
			if (skipRecords.contains(r)) {
				continue;
			}
			num++;
			result.add(r);
		}
		context.setAttribute("LAST_PAGE_SIZE", result.size());
		index += result.size();
		context.setAttribute(RECORDS_START_INDEX, index);

		if (records.size() > index) {
			Record inActiveRec = new Record(ActionNames.NEXT_PAGE);
			inActiveRec.add("", "Next Page");
			actions.add(inActiveRec);
		}

		if (index > recordsToShow) {
			Record inActiveRec = new Record(ActionNames.PREV_PAGE);
			inActiveRec.add("", "Prev Page");
			actions.add(inActiveRec);
		}
		return result;
	}

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

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	protected Result amountOptionalRequirement(Context context,
			ResultList list, Object selection, String name, String displayString) {

		Requirement req = get(name);
		Double balance = (Double) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);

		if (attribute.equals(name)) {
			String order = context.getSelection(name);
			if (order == null) {
				order = context.getNumber();
			}
			balance = Double.parseDouble(order);
			req.setValue(balance);
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (selection != null) {
			if (selection == "balance") {
				context.setAttribute(INPUT_ATTR, name);
				return amount(context, displayString, balance);
			}
		}

		Record balanceRecord = new Record("balance");
		balanceRecord.add("Name", "balance");
		balanceRecord.add("Value", balance);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return null;
	}

	protected Result amount(Context context, String message, Double oldAmount) {
		return number(context, message,
				oldAmount != null ? oldAmount.toString() : null);
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

	protected Result dateOptionalRequirement(Context context, ResultList list,
			String name, String displayString, Object selection) {
		Requirement req = get(name);
		Date dueDate = req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);

		if (attribute.equals(name)) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			dueDate = date;
			req.setValue(dueDate);
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (selection != null) {
			if (selection == name) {
				context.setAttribute(INPUT_ATTR, name);
				return date(context, displayString, dueDate);
			}
		}
		Record dueDateRecord = new Record(name);
		dueDateRecord.add("Name", name);
		dueDateRecord.add("Value", dueDate.toString());
		list.add(dueDateRecord);
		return null;
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

	protected Result numberRequirement(Context context, String reqName,
			String displayString) {
		Requirement customerNumReq = get(reqName);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(reqName)) {
			customerNumReq.setValue(context.getNumber());
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (!customerNumReq.isDone()) {
			context.setAttribute(INPUT_ATTR, reqName);
			return number(context, displayString, null);
		}

		return null;
	}

	protected Result nameRequirement(Context context, String reqName,
			String displayString) {
		Requirement requirement = get(reqName);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(reqName)) {
			input = context.getString();
			requirement.setValue(input);
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (!requirement.isDone()) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayString, null);
		}
		return null;
	}

	protected Result addressProcess(Context context) {
		String message = (String) context.getAttribute(ADDRESS_MESSAGE_ATTR);
		Address address = (Address) context.getAttribute(OLD_ADDRESS_ATTR);
		String requirementName = (String) context
				.getAttribute(REQUIREMENT_NAME);
		return address(context, message, requirementName, address);
	}

	protected Result address(Context context, String message,
			String requirementName, Address oldAddress) {
		context.setAttribute(PROCESS_ATTR, ADDRESS_PROCESS);
		context.setAttribute(ADDRESS_MESSAGE_ATTR, message);
		context.setAttribute(OLD_ADDRESS_ATTR, oldAddress);
		context.setAttribute(REQUIREMENT_NAME, requirementName);

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
				if (selection == "Address1") {
					context.setAttribute(ADDRESS_LINE_ATTR, "address1");
					return text(context, "Enter Address1",
							oldAddress.getAddress1());
				} else if (selection == "City") {
					context.setAttribute(ADDRESS_LINE_ATTR, "city");
					return text(context, "Enter City", oldAddress.getCity());
				} else if (selection == "Street") {
					context.setAttribute(ADDRESS_LINE_ATTR, "street");
					return text(context, "Enter Street", oldAddress.getStreet());
				} else if (selection == "State/Provinence") {
					context.setAttribute(ADDRESS_LINE_ATTR, "stateOrProvinence");
					return text(context, "Enter State/Provinence",
							oldAddress.getStateOrProvinence());
				} else if (selection == "Country/Region") {
					context.setAttribute(ADDRESS_LINE_ATTR, "countryOrRegion");
					return text(context, "Enter Country/Region",
							oldAddress.getCountryOrRegion());
				}
			} else {
				selection = context.getSelection(ADDRESS_ACTIONS);
				if (selection == ActionNames.FINISH) {
					get(requirementName).setValue(oldAddress);
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(ADDRESS_MESSAGE_ATTR);
					context.removeAttribute(OLD_ADDRESS_ATTR);
					context.removeAttribute(ADDRESS_LINE_ATTR);// No need
					return null;
				}
			}
		}

		ResultList list = new ResultList(ADDRESS);
		Record record = new Record("Address1");
		record.add("", "Address1");
		record.add("", oldAddress.getAddress1());
		list.add(record);

		record = new Record("Street");
		record.add("", "Street");
		record.add("", oldAddress.getStreet());
		list.add(record);

		record = new Record("City");
		record.add("", "City");
		record.add("", oldAddress.getCity());
		list.add(record);

		record = new Record("State/Provinence");
		record.add("", "State/Provinence");
		record.add("", oldAddress.getStateOrProvinence());
		list.add(record);

		record = new Record("Country/Region");
		record.add("", "Country/Region");
		record.add("", oldAddress.getCountryOrRegion());
		list.add(record);

		Result result = context.makeResult();
		result.add(message);

		result.add(list);
		result.add("Select any line to edit");

		ResultList finish = new ResultList(ADDRESS_ACTIONS);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		finish.add(record);
		result.add(finish);
		return result;
	}

	private Record createTaxCodeRecord(TAXCode taxCode) {
		Record record = new Record(taxCode);
		record.add("", taxCode.getName() + "-" + taxCode.getSalesTaxRate());
		return record;
	}

	private List<TAXCode> getTaxCodes(Company company) {
		return new ArrayList<TAXCode>(company.getTaxCodes());

	}

	protected Result taxCode(Context context, TAXCode oldTaxCode) {
		List<TAXCode> codes = getTaxCodes(context.getCompany());

		Result result = context.makeResult();
		result.add("Select Taxcode");

		ResultList list = new ResultList(TAXCODE);

		if (oldTaxCode != null) {
			list.add(createTaxCodeRecord(oldTaxCode));
		}
		ActionNames selection = context.getSelection(TAXCODE);

		List<Record> actions = new ArrayList<Record>();

		List<TAXCode> pagination = pagination(context, selection, actions,
				codes, new ArrayList<TAXCode>(), TAXCODE_TO_SHOW);

		for (TAXCode term : pagination) {
			list.add(createTaxCodeRecord(term));
		}

		for (Record record : actions) {
			list.add(record);
		}
		result.add(list);
		CommandList commands = new CommandList();
		commands.add("Create New Taxcode");
		result.add(commands);
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
		String requirementName = (String) context
				.getAttribute(REQUIREMENT_NAME);
		return contact(context, message, requirementName, contact);
	}

	/**
	 * 
	 * @param context
	 * @param message
	 * @param customerContact
	 * @param oldContact
	 * @return {@link Result}
	 */
	protected Result contact(Context context, String message,
			String requirementName, Contact oldContact) {
		context.setAttribute(PROCESS_ATTR, CONTACT_PROCESS);
		context.setAttribute(CONTACT_ATTR, message);
		context.setAttribute(OLD_CONTACT_ATTR, oldContact);
		context.setAttribute(REQUIREMENT_NAME, requirementName);

		String lineAttr = (String) context.getAttribute(CONTACT_LINE_ATTR);
		if (lineAttr != null) {
			String input = context.getString();
			context.removeAttribute(CONTACT_LINE_ATTR);
			if (lineAttr.equals("contactName")) {
				if (oldContact == null) {
					oldContact = new Contact();
					Requirement requirement = get(requirementName);
					Set<Contact> contacts = requirement.getValue();
					if (contacts == null) {
						contacts = new HashSet<Contact>();
						requirement.setValue(contacts);
					}
					contacts.add(oldContact);
					context.setAttribute(OLD_CONTACT_ATTR, oldContact);
				}
				oldContact.setName(input);
			} else if (lineAttr.equals("title")) {
				oldContact.setTitle(input);
			} else if (lineAttr.equals("businessPhone")) {
				if (input == null) {
					input = context.getNumber();
				}
				oldContact.setBusinessPhone(input);
			} else if (lineAttr.equals("email")) {
				oldContact.setEmail(input);
			}

		}
		Object selection = context.getSelection(CONTACT_ACTIONS);
		if (selection == ActionNames.FINISH) {
			context.removeAttribute(PROCESS_ATTR);
			context.removeAttribute(CONTACT_ATTR);
			context.removeAttribute(OLD_ADDRESS_ATTR);
			context.removeAttribute(CONTACT_LINE_ATTR);// No need
			return null;

		} else {
			selection = context.getSelection(CONTACTS);
			if (oldContact == null) {
				selection = "Contact Name";
			}

			if (selection != null) {
				if (selection.equals("isActive")) {
					oldContact.setPrimary(!oldContact.isPrimary());
				} else if (selection == "Contact Name") {
					context.setAttribute(CONTACT_LINE_ATTR, "contactName");
					return text(context, "Enter conatactName",
							oldContact != null ? oldContact.getName() : null);
				} else if (selection == "Title") {
					context.setAttribute(CONTACT_LINE_ATTR, "title");
					return text(context, "Enter Title", oldContact.getTitle());
				} else if (selection == "BusinessPhone") {
					context.setAttribute(CONTACT_LINE_ATTR, "businessPhone");
					return text(context, "Enter Businessphone Number ",
							oldContact.getBusinessPhone());
				} else if (selection == "Email") {
					context.setAttribute(CONTACT_LINE_ATTR, "email");
					return text(context, "Enter Email", oldContact.getEmail());
				}
			}
		}

		ResultList list = new ResultList(CONTACTS);
		Record record = new Record("isActive");
		record.add("", "IsActive");
		record.add("", oldContact.isPrimary());
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
		result.add(message);

		result.add(list);
		result.add("Select any line to edit");

		ResultList finish = new ResultList(CONTACT_ACTIONS);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		finish.add(record);
		result.add(finish);
		return result;
	}

	protected Result createSupplierRequirement(Context context,
			ResultList list, String requirementName) {
		Requirement supplierReq = get(requirementName);
		Vendor vendor = context.getSelection("suppliers");

		if (vendor != null) {
			supplierReq.setValue(vendor);
		}

		Vendor value = supplierReq.getValue();
		Object selection = context.getSelection("values");
		if (!supplierReq.isDone() || (value == selection)) {
			return vendors(context);
		}

		Record supplierRecord = new Record(value);
		supplierRecord.add("", "Supplier");
		supplierRecord.add("", value.getName());
		list.add(supplierRecord);

		return null;
	}

	protected Result vendors(Context context) {
		Result result = context.makeResult();

		ResultList supplierList = new ResultList("suppliers");

		Object last = context.getLast(RequirementType.VENDOR);
		List<Vendor> skipVendors = new ArrayList<Vendor>();
		if (last != null) {
			supplierList.add(createVendorRecord((Vendor) last));
			skipVendors.add((Vendor) last);
		}
		List<Vendor> vendors = getVendors(true, context.getCompany());

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<Vendor> pagination = pagination(context, selection, actions,
				vendors, skipVendors, VENDORS_TO_SHOW);

		for (Vendor vendor : pagination) {
			supplierList.add(createVendorRecord(vendor));
		}

		int size = supplierList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Supplier");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create New Vendor");

		result.add(message.toString());
		result.add(supplierList);
		result.add(actions);
		result.add(commandList);
		return result;
	}

	private Record createVendorRecord(Vendor last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		record.add(" ,Balance", last.getBalance());
		return record;
	}

	protected Result stringOptionalRequirement(Context context,
			ResultList list, Object selection, String name, String displayName) {
		Requirement req = get(name);
		String memo = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(name)) {
			String input = context.getSelection(TEXT);
			if (input == null) {
				input = context.getString();
			}
			memo = input;
			req.setValue(memo);
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (selection != null)
			if (selection == name) {
				context.setAttribute(INPUT_ATTR, name);
				return text(context, displayName, memo);
			}

		Record memoRecord = new Record(name);
		memoRecord.add("", name);
		memoRecord.add("", memo);
		list.add(memoRecord);
		return null;
	}

	protected Result numberOptionalRequirement(Context context,
			ResultList list, Object selection, String name, String displayName) {
		Requirement req = get(name);
		String number = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(name)) {
			String input = context.getNumber();
			if (input == null) {
				input = context.getString();
			}
			number = input;
			req.setValue(number);
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (selection != null)
			if (selection == name) {
				context.setAttribute(INPUT_ATTR, name);
				return text(context, displayName, number);
			}

		Record numberRecord = new Record(name);
		numberRecord.add("Name", name);
		numberRecord.add("Value", number);
		list.add(numberRecord);
		return null;
	}

	protected Result orderNoRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(ORDER_NO);
		String orderNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(ORDER_NO)) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			orderNo = order;
			req.setValue(orderNo);
			context.setAttribute(INPUT_ATTR, "optional");
		}

		if (selection == orderNo) {
			context.setAttribute(INPUT_ATTR, ORDER_NO);
			return number(context, "Enter Order number", orderNo);
		}

		Record orderNoRecord = new Record(orderNo);
		orderNoRecord.add("Name", "Order No");
		orderNoRecord.add("Value", orderNo);
		list.add(orderNoRecord);
		return null;
	}

	protected Result dateRequirement(Context context, ResultList list,
			Object selection) {

		Requirement dateReq = get(DATE);
		Date transDate = context.getDate();
		if (!dateReq.isDone()) {
			if (transDate == null) {
				// context.setAttribute(INPUT_ATTR, DATE);
				return date(context, "Enter Date", transDate);
			} else {
				dateReq.setValue(transDate);
			}
			Record transDateRecord = new Record(transDate);
			transDateRecord.add("Name", "Date");
			transDateRecord.add("Value", transDate.toString());
			list.add(transDateRecord);
		}

		return null;
	}

	protected List<Vendor> getVendors(boolean isActive, Company company) {

		ArrayList<Vendor> vendors = new ArrayList<Vendor>(company.getVendors());
		ArrayList<Vendor> result = new ArrayList<Vendor>();
		if (!vendors.isEmpty())
			for (Vendor vendor : vendors) {
				if (isActive) {
					if (vendor.isActive()) {
						result.add(vendor);
					}
				} else {
					result.add(vendor);
				}
			}

		return result;
	}

	protected Result paymentMethodRequirement(Context context, ResultList list,
			Object selection) {
		Object payamentMethodObj = context.getSelection("Payment method");
		Requirement paymentMethodReq = get("paymentMethod");
		String paymentmethod = (String) paymentMethodReq.getValue();
		if (payamentMethodObj != null) {
			paymentmethod = (String) payamentMethodObj;
			paymentMethodReq.setValue(paymentmethod);
		}
		if (selection != null)
			if (selection == "paymentMethod") {
				context.setAttribute(INPUT_ATTR, "paymentmethod");
				return paymentMethod(context, paymentmethod);

			}

		Record paymentTermRecord = new Record("paymentMethod");
		paymentTermRecord.add("Name", "paymentMethod");
		paymentTermRecord.add("Value", paymentmethod);
		list.add(paymentTermRecord);
		return null;
	}

	protected Result paymentMethodRequirement(Context context) {
		Requirement paymentMethodReq = get(PAYMENT_METHOD);
		String paymentMethod = context.getSelection(PAYMENT_METHOD);
		if (paymentMethod != null) {
			paymentMethodReq.setValue(paymentMethod);
		}
		if (!paymentMethodReq.isDone()) {
			return paymentMethod(context, null);
		}
		return null;
	}

	protected Result paymentMethod(Context context, String oldpaymentmethod) {
		List<String> paymentmethods = getpaymentmethod();
		Result result = context.makeResult();
		result.add("Select PaymentMethod");

		ResultList list = new ResultList(PAYMENT_METHOD);
		Object last = context.getLast(RequirementType.PAYMENT_METHOD);
		int num = 0;
		if (last != null) {
			list.add(createPayMentMethodRecord((String) oldpaymentmethod));
			num++;
		}
		for (String paymentmethod : paymentmethods) {
			if (paymentmethod != (String) last) {
				list.add(createPayMentMethodRecord(paymentmethod));
				num++;
			}
			if (num == PAYMENTMETHODS_TO_SHOW) {
				break;
			}
		}
		result.add(list);
		Record more = new Record("MORE");
		more.add("", "MORE");
		list.add(more);
		return result;
	}

	protected List<String> getstatusmethod() {
		List<String> list = new ArrayList<String>();
		list.add("Open");
		list.add("Completed");
		list.add("Cancelled");
		return list;
	}

	protected Record createStatusMethodRecord(String statusMethod) {
		Record record = new Record(statusMethod);
		record.add("Name", "Status Method");
		record.add("value", statusMethod);
		return record;
	}

	protected Record createPayMentMethodRecord(String paymentMethod) {
		Record record = new Record(paymentMethod);
		record.add("Payment Method Name", paymentMethod);
		return record;
	}

	private List<String> getpaymentmethod() {
		List<String> list = new ArrayList<String>();
		list.add("Cash");
		list.add("Check");
		list.add("Credit Card");
		list.add("Direct Debit");
		list.add("Master Card");
		list.add("Standing Order");
		list.add("Online Banking");
		list.add("Switch/Maestro");
		return list;
	}

	protected void create(IAccounterServerCore obj, Context context) {
		// User user = context.getUser();
		Session session = context.getHibernateSession();
		Transaction beginTransaction = session.beginTransaction();
		session.save(obj);
		beginTransaction.commit();

		// try {
		// new FinanceTool().createServerObject(obj, user, session);
		// } catch (AccounterException e) {
		// e.printStackTrace();
		// }
	}

	protected Result isActiveRequirement(Context context, Object selection) {
		Requirement isActiveReq = get(ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == isActive) {
			// context.setAttribute(INPUT_ATTR, ACTIVE);
			// isActive = !isActive;
			isActiveReq.setValue(true);
		}

		return null;
	}

	protected Result viewTypeRequirement(Context context, ResultList list,
			Object selection) {
		Object viewType = context.getSelection(VIEW_BY);
		Requirement viewReq = get(VIEW_BY);
		String view = viewReq.getValue();

		if (selection == view) {
			return viewTypes(context, view);

		}
		if (viewType != null) {
			view = (String) viewType;
			viewReq.setValue(view);
		}

		Record viewtermRecord = new Record(view);
		viewtermRecord.add("Name", "viewType");
		viewtermRecord.add("Value", view);
		list.add(viewtermRecord);
		return null;
	}

	protected Result accountTypesRequirement(Context context, Object selection,
			ResultList list) {
		Object viewType = context.getSelection(ACCOUNT_TYPE);
		Requirement viewReq = get(ACCOUNT_TYPE);
		String view = viewReq.getValue();
		if (viewType == null) {
			viewType = view;
		}

		if (selection == viewType) {
			context.setAttribute(INPUT_ATTR, ACCOUNT_TYPE);
			return accTypes(context, view);

		}
		if (viewType != null) {
			view = (String) viewType;
			viewReq.setValue(view);
		}

		Record viewtermRecord = new Record(view);
		viewtermRecord.add("Name", ACCOUNT_TYPE);
		viewtermRecord.add(":", view);
		list.add(viewtermRecord);

		return null;
	}

	protected Result accTypes(Context context, String view) {
		ResultList list = new ResultList(ACCOUNT_TYPE);
		Result result = null;
		List<String> viewTypes = getAccTypes();
		result = context.makeResult();
		result.add("Select Account Type");

		int num = 0;
		if (view != null) {
			list.add(createViewTypeRecord(view));
			num++;
		}
		for (String v : viewTypes) {
			if (v != view) {
				list.add(createViewTypeRecord(v));
				num++;
			}
			if (num == 5) {
				break;
			}

		}

		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add("Slect an Account(s).");
		} else {
			result.add("You don't have Account.");
		}
		result.add(list);
		CommandList commands = new CommandList();
		commands.add("Create New Account");

		return result;
	}

	protected Result viewTypes(Context context, String view) {
		ResultList list = new ResultList("viewslist");
		Result result = null;
		List<String> viewTypes = getViewTypes();
		result = context.makeResult();
		result.add("Select View Type");

		int num = 0;
		if (view != null) {
			list.add(createViewTypeRecord(view));
			num++;
		}
		for (String v : viewTypes) {
			if (v != view) {
				list.add(createViewTypeRecord(v));
				num++;
			}
			if (num == 0) {
				break;
			}

		}

		result.add(list);

		return result;
	}

	protected List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add(Accounter.constants().issued());
		list.add(Accounter.constants().notIssued());
		list.add(Accounter.constants().Voided());
		list.add(Accounter.constants().all());

		return list;
	}

	protected List<String> getAccTypes() {
		List<String> list = new ArrayList<String>();
		list.add("Income");
		list.add("OtherIncome");
		list.add("Expense");
		list.add("OtherExpense");
		list.add("CostOfGoodSold");
		list.add("Cash");
		list.add("OtherAssets");

		list.add("CreditCard");
		list.add("FixedAssets");

		return list;
	}

	protected Record createViewTypeRecord(String view) {
		Record record = new Record(view);
		record.add("Name", "");
		record.add("Value", view);
		return record;
	}

	protected Result activeRequirement(Context context, Object selection,
			ResultList list) {
		Requirement isActiveReq = get(ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == ACTIVE) {
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This account is Active";
		} else {
			activeString = "This account is InActive";
		}
		Record isActiveRecord = new Record(ACTIVE);
		isActiveRecord.add(":", activeString);
		list.add(isActiveRecord);
		return null;
	}

	protected AccounterConstants getConstants() {
		return constants;
	}

	protected AccounterMessages getMessages() {
		return messages;
	}
}
