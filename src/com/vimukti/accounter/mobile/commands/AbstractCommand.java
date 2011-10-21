package com.vimukti.accounter.mobile.commands;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.IAccounterServerCore;
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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

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
	protected static final String VAT_RETURN = "vatReturn";
	protected static final int STATUS_NOT_ISSUED = 0;
	protected static final int STATUS_PARTIALLY_PAID = 1;
	protected static final int STATUS_ISSUED = 2;
	protected static final int STATUS_VOIDED = 3;
	protected static final int ALL = 4;
	private static final String VAT_RETURNS = "vatReturns";
	private static final String CONTACT_ACTIONS = "contactActions";
	protected static final String ACCOUNT = "Account";
	private static final String REQUIREMENT_NAME = "requirmentName";
	private static final String ADDRESS_ACTIONS = "addressActions";
	private static final String RECORDS_START_INDEX = "recordsStrartIndex";
	protected static final String PAGENATION = null;
	protected static final String DEPOSIT_OR_TRANSFER_TO = "depositOrTransferTo";
	private static final String SHIPPING_TERMS = "shippingTerms";
	protected static final String SHIPPING_METHODS = "shippingMethods";
	private static final String VENDOR = "vendor";
	private static final String SUPPLIERS = "suppliers";
	private IGlobal global;
	private AccounterConstants constants;
	private AccounterMessages messages;
	protected static final String AMOUNTS_INCLUDE_TAX = "Amounts Include tax";

	public AbstractCommand() {
		try {
			global = new ServerGlobal();
		} catch (IOException e) {
			e.printStackTrace();
		}
		constants = global.constants();
		messages = global.messages();
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
	 * @param name
	 * @return
	 */
	protected Result amountOptionalRequirement(Context context,
			ResultList list, Object selection, String reqName,
			String displayString, String name) {

		Requirement req = get(reqName);
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
			if (selection == reqName) {
				context.setAttribute(INPUT_ATTR, name);
				return amount(context, displayString, balance);
			}
		}

		Record balanceRecord = new Record(reqName);
		balanceRecord.add("Name", name);
		balanceRecord.add("Value", balance);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return null;
	}

	protected Result vendorOptionalRequirement(Context context,
			ResultList list, Object selection, String reqName,
			String displayString, String name) {
		Object vendorObj = context.getSelection(SUPPLIERS);
		if (vendorObj instanceof ActionNames) {
			vendorObj = null;
			selection = reqName;
		}
		
		Requirement vendorReq = get(reqName);
		ClientVendor vendor = (ClientVendor) vendorReq.getValue();

		if (vendorObj != null) {
			vendor = (ClientVendor) vendorObj;
			vendorReq.setValue(vendor);
		}

		if (selection != null)
			if (selection.equals(reqName)) {
				context.setAttribute(INPUT_ATTR, reqName);
				return vendors(context);

			}

		Record vendorRecord = new Record(reqName);
		vendorRecord.add("", name);
		vendorRecord.add("", vendor == null ? "" : vendor.getName());
		list.add(vendorRecord);

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
			String reqName, String name, String displayString, Object selection) {
		Requirement req = get(reqName);
		ClientFinanceDate dueDate = req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);

		if (attribute.equals(reqName)) {
			ClientFinanceDate date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			dueDate = date;
			req.setValue(dueDate);
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (selection != null) {
			if (selection == reqName) {
				context.setAttribute(INPUT_ATTR, reqName);
				return date(context, displayString, dueDate);
			}
		}
		Record dueDateRecord = new Record(reqName);
		dueDateRecord.add("", name);
		dueDateRecord.add("", getDateAsString(dueDate));
		list.add(dueDateRecord);
		return null;
	}

	protected Result date(Context context, String message,
			ClientFinanceDate date) {
		Result result = context.makeResult();
		result.add(message);
		if (date != null) {
			ResultList list = new ResultList(DATE);
			Record record = new Record(date);
			record.add("", getDateAsString(date));
			list.add(record);
			result.add(list);
		}
		return result;
	}

	protected Result numberRequirement(Context context, ResultList list,
			String reqName, String displayString, String name) {
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
		Object selection = context.getSelection("values");
		String num = (String) customerNumReq.getValue();
		if (selection != null && selection == reqName) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayString, num);
		}

		Record numberRecord = new Record(name);
		numberRecord.add("", name);
		numberRecord.add("", num);
		list.add(numberRecord);
		return null;
	}

	protected Result nameRequirement(Context context, ResultList list,
			String reqName, String name, String displayString) {
		Requirement requirement = get(reqName);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(reqName)) {
			input = context.getString();
			requirement.setValue(input);
			context.setAttribute(INPUT_ATTR, "");
		}
		if (!requirement.isDone()) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayString, null);
		}

		Object selection = context.getSelection("values");
		String customerName = requirement.getValue();
		if (selection != null && selection.equals(reqName)) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayString, customerName);
		}

		Record nameRecord = new Record(reqName);
		nameRecord.add("", name);
		nameRecord.add("", customerName);
		list.add(nameRecord);
		return null;
	}

	protected Result addressProcess(Context context) {
		String message = (String) context.getAttribute(ADDRESS_MESSAGE_ATTR);
		ClientAddress address = (ClientAddress) context
				.getAttribute(OLD_ADDRESS_ATTR);
		String requirementName = (String) context
				.getAttribute(REQUIREMENT_NAME);
		return address(context, message, requirementName, address);
	}

	protected Result address(Context context, String message,
			String requirementName, ClientAddress oldAddress) {
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
		record.add("", getConstants().address1());
		record.add("", oldAddress.getAddress1());
		list.add(record);

		record = new Record("Street");
		record.add("", getConstants().streetAddress1());
		record.add("", oldAddress.getStreet());
		list.add(record);

		record = new Record("City");
		record.add("", getConstants().city());
		record.add("", oldAddress.getCity());
		list.add(record);

		record = new Record("State/Provinence");
		record.add("", getConstants().stateOrProvince());
		record.add("", oldAddress.getStateOrProvinence());
		list.add(record);

		record = new Record("Country/Region");
		record.add("", getConstants().countryRegion());
		record.add("", oldAddress.getCountryOrRegion());
		list.add(record);

		Result result = context.makeResult();
		result.add(message);

		result.add(list);
		result.add(getConstants().select());

		ResultList finish = new ResultList(ADDRESS_ACTIONS);
		record = new Record(ActionNames.FINISH);
		record.add("", getConstants().finish());
		finish.add(record);
		result.add(finish);
		return result;
	}

	private Record createTaxCodeRecord(ClientTAXCode taxCode) {
		Record record = new Record(taxCode);
		record.add("", taxCode.getName() + "-" + taxCode.getSalesTaxRate());
		return record;
	}

	protected Result taxCode(Context context, ClientTAXCode oldTaxCode) {
		List<ClientTAXCode> codes = getClientCompany().getTaxCodes();

		Result result = context.makeResult();
		result.add(getMessages().pleaseSelect(getConstants().taxCode()));

		ResultList list = new ResultList(TAXCODE);

		if (oldTaxCode != null) {
			list.add(createTaxCodeRecord(oldTaxCode));
		}
		ActionNames selection = context.getSelection(TAXCODE);

		List<Record> actions = new ArrayList<Record>();

		List<ClientTAXCode> pagination = pagination(context, selection,
				actions, codes, new ArrayList<ClientTAXCode>(), TAXCODE_TO_SHOW);

		for (ClientTAXCode term : pagination) {
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
		ClientContact contact = (ClientContact) context
				.getAttribute(OLD_CONTACT_ATTR);
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
			String requirementName, ClientContact oldContact) {
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
					oldContact = new ClientContact();
					Requirement requirement = get(requirementName);
					Set<ClientContact> contacts = requirement.getValue();
					if (contacts == null) {
						contacts = new HashSet<ClientContact>();
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
			get("contact").setValue(oldContact);
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
			ResultList list, String requirementName, String name) {
		Requirement supplierReq = get(requirementName);
		ClientVendor vendor = context.getSelection(SUPPLIERS);

		if (vendor != null) {
			supplierReq.setValue(vendor);
		}

		ClientVendor value = supplierReq.getValue();
		Object selection = context.getSelection("values");
		if (!supplierReq.isDone() || (value == selection)) {
			return vendors(context);
		}

		Record supplierRecord = new Record(value);
		supplierRecord.add("", name);
		supplierRecord.add("", value.getName());
		list.add(supplierRecord);

		return null;
	}

	protected Result vendors(Context context) {
		Result result = context.makeResult();

		ResultList supplierList = new ResultList(SUPPLIERS);

		Object last = context.getLast(RequirementType.VENDOR);
		List<ClientVendor> skipVendors = new ArrayList<ClientVendor>();
		if (last != null) {
			supplierList.add(createVendorRecord((ClientVendor) last));
			skipVendors.add((ClientVendor) last);
		}
		List<ClientVendor> vendors = getVendors(true);

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<ClientVendor> pagination = pagination(context, selection, actions,
				vendors, skipVendors, VENDORS_TO_SHOW);

		for (ClientVendor vendor : pagination) {
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

	protected Record createVendorRecord(ClientVendor last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		record.add(" ,Balance", last.getBalance());
		return record;
	}

	protected Result stringOptionalRequirement(Context context,
			ResultList list, Object selection, String reqName, String name,
			String displayName) {
		Requirement req = get(reqName);
		String memo = req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(reqName)) {
			String input = context.getSelection(TEXT);
			if (input == null) {
				input = context.getString();
			}
			memo = input;
			req.setValue(memo);
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (selection != null)
			if (selection == reqName) {
				context.setAttribute(INPUT_ATTR, reqName);
				return text(context, displayName, memo);
			}

		Record memoRecord = new Record(reqName);
		memoRecord.add("", name);
		memoRecord.add("", memo);
		list.add(memoRecord);
		return null;
	}

	protected Result numberOptionalRequirement(Context context,
			ResultList list, Object selection, String reqName, String name,
			String displayName) {
		Requirement req = get(reqName);
		String number = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(reqName)) {
			String input = context.getNumber();
			if (input == null) {
				input = context.getString();
			}
			number = input;
			req.setValue(number);
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (selection != null)
			if (selection == reqName) {
				context.setAttribute(INPUT_ATTR, reqName);
				return text(context, displayName, number);
			}

		Record numberRecord = new Record(reqName);
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
			Object selection, String reqName, String displayName, String name) {

		Requirement requirement = get(reqName);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(reqName)) {
			requirement.setValue(context.getDate());
		}
		if (!requirement.isDone()) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayName, null);
		}

		ClientFinanceDate transDate = requirement.getValue();
		if (selection != null && selection.equals(reqName)) {
			context.setAttribute(INPUT_ATTR, reqName);
			return date(context, displayName, transDate);
		}

		Record nameRecord = new Record(reqName);
		nameRecord.add("", name);
		nameRecord.add("", getDateAsString(transDate));
		list.add(nameRecord);
		return null;
	}

	protected List<ClientVendor> getVendors(Boolean isActive) {
		ArrayList<ClientVendor> vendors = getClientCompany().getVendors();
		if (isActive == null) {
			return vendors;
		}
		ArrayList<ClientVendor> result = new ArrayList<ClientVendor>();
		for (ClientVendor vendor : vendors) {
			if (vendor.isActive() == isActive) {
				result.add(vendor);
			}
		}
		return result;
	}

	protected Result stringListOptionalRequirement(Context context,
			ResultList list, Object selection, String requirementName,
			String displayName, List<String> values, String title,
			int itemsToView) {
		Object payamentMethodObj = context.getSelection(requirementName);
		Requirement paymentMethodReq = get(requirementName);
		String value = paymentMethodReq.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute != null)
			if (attribute.equals(requirementName)) {
				if (payamentMethodObj != null) {
					value = (String) payamentMethodObj;
					paymentMethodReq.setValue(value);
				}
			}
		if (selection != null) {
			if (selection == requirementName) {
				context.setAttribute(INPUT_ATTR, requirementName);
				return stringList(context, requirementName, value, values,
						itemsToView, title);

			}
		}
		Record paymentTermRecord = new Record(requirementName);
		paymentTermRecord.add("", displayName);
		paymentTermRecord.add("", value);
		list.add(paymentTermRecord);
		return null;
	}

	private Result stringList(Context context, String requirementName,
			String oldValue, List<String> values, int itemsToView, String title) {
		Result result = context.makeResult();

		ResultList list = new ResultList(requirementName);

		List<String> skippayMents = new ArrayList<String>();

		if (oldValue != null) {
			Record record = new Record(oldValue);
			record.add("", oldValue);
			list.add(record);
			skippayMents.add((String) oldValue);
		}

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<String> pagination = pagination(context, selection, actions,
				values, skippayMents, itemsToView);

		for (String paymentMethod : pagination) {
			Record record = new Record(paymentMethod);
			record.add("", paymentMethod);
			list.add(record);
		}

		int size = list.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(title);
		}

		result.add(message.toString());
		result.add(list);
		result.add(actions);

		return result;
	}

	protected Result paymentMethodOptionalRequirement(Context context,
			ResultList list, Object selection) {
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

	protected Result paymentMethodRequirement(Context context, ResultList list,
			String requirementName, String name) {
		Requirement paymentMethodReq = get(requirementName);
		String paymentMethod = context.getSelection(requirementName);

		if (paymentMethod != null) {
			paymentMethodReq.setValue(paymentMethod);
		}

		String value = paymentMethodReq.getValue();
		Object selection = context.getSelection("values");

		if (!paymentMethodReq.isDone() || (value == selection)) {
			return paymentMethod(context, null);
		}

		Record supplierRecord = new Record(value);
		supplierRecord.add("", name);
		supplierRecord.add("", value);
		list.add(supplierRecord);

		return null;
	}

	protected Result paymentMethod(Context context, String oldpaymentmethod) {
		List<String> paymentmethods = getpaymentmethod();
		Result result = context.makeResult();
		ResultList list = new ResultList(PAYMENT_METHOD);
		Object last = context.getLast(RequirementType.PAYMENT_METHOD);

		List<String> skippayMents = new ArrayList<String>();

		if (last != null) {
			list.add(createPayMentMethodRecord((String) oldpaymentmethod));
			skippayMents.add((String) last);
		}

		List<String> paymentMethods = paymentmethods;

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<String> pagination = pagination(context, selection, actions,
				paymentMethods, skippayMents, PAYMENTMETHODS_TO_SHOW);

		for (String paymentMethod : pagination) {
			list.add(createPayMentMethodRecord(paymentMethod));
		}

		int size = list.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Payment Method");
		}

		result.add(message.toString());
		result.add(list);
		result.add(actions);

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

	protected void create(IAccounterCore coreObject, Context context) {
		try {
			String clientClassSimpleName = coreObject.getObjectType()
					.getClientClassSimpleName();

			OperationContext opContext = new OperationContext(context
					.getCompany().getID(), coreObject, context.getIOSession()
					.getUserEmail());
			opContext.setArg2(clientClassSimpleName);
			new FinanceTool().create(opContext);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
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
		Requirement viewReq = get(VIEW_BY);
		String view = viewReq.getValue();

		if (selection == view) {
			return viewTypes(context, view);

		}
		if (selection != null) {
			view = (String) selection;
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

	protected void booleanOptionalRequirement(Context context,
			Object selection, ResultList list, String reqName,
			String trueString, String falseString) {
		Requirement isActiveReq = get(reqName);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == reqName) {
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}

		Record isActiveRecord = new Record(reqName);
		isActiveRecord.add("", isActive ? trueString : falseString);
		list.add(isActiveRecord);
	}

	protected AccounterConstants getConstants() {
		return constants;
	}

	protected AccounterMessages getMessages() {
		return messages;
	}

	protected Result paymentTermsRequirement(Context context, ResultList list,
			String name, String string) {
		Requirement paymentTermsReq = get("paymentTerm");
		ClientPaymentTerms paymentTerms = context.getSelection("paymentTerm");

		if (paymentTerms != null) {
			paymentTermsReq.setValue(paymentTerms);
		}

		ClientPaymentTerms paymentTerm = paymentTermsReq.getValue();
		Object selection = context.getSelection("values");

		if (!paymentTermsReq.isDone() || (paymentTerm == selection)) {
			return getPaymentTermsResult(context);
		}

		Record paymentTermsRecord = new Record(paymentTerm);
		paymentTermsRecord.add("", "PaymentTerms");
		paymentTermsRecord.add("", paymentTerm.getName());
		list.add(paymentTermsRecord);

		return null;
	}

	private Result getPaymentTermsResult(Context context) {
		Result result = context.makeResult();
		ResultList paymentTermsList = new ResultList("paymentTerm");

		Object last = context.getLast(RequirementType.PAYMENT_TERMS);
		List<ClientPaymentTerms> skipPaymentTermList = new ArrayList<ClientPaymentTerms>();
		if (last != null) {
			paymentTermsList
					.add(createPaymentTermRecord((ClientPaymentTerms) last));
			skipPaymentTermList.add((ClientPaymentTerms) last);
		}

		List<ClientPaymentTerms> paymentTerms = getClientCompany()
				.getPaymentsTerms();
		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");
		List<ClientPaymentTerms> pagination = pagination(context, selection,
				actions, paymentTerms, skipPaymentTermList, VALUES_TO_SHOW);

		for (ClientPaymentTerms terms : pagination) {
			paymentTermsList.add(createPaymentTermRecord(terms));
		}

		int size = paymentTermsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the PaymentTerm");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(paymentTermsList);
		result.add(commandList);
		result.add("Select the Payment Term");

		return result;
	}

	private Record createPaymentTermRecord(ClientPaymentTerms paymentTerm) {
		Record record = new Record(paymentTerm);
		record.add("Name", paymentTerm.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param requirementName
	 * @return {@link Result}
	 */
	protected Result accountRequirement(Context context, ResultList list,
			String requirementName, String name,
			ListFilter<ClientAccount> filter) {
		Requirement accountReq = get(requirementName);
		ClientAccount clientAccount = context.getSelection(requirementName);

		if (clientAccount != null) {
			accountReq.setValue(clientAccount);
		}

		ClientAccount account = accountReq.getValue();
		Object selection = context.getSelection("values");
		if (!accountReq.isDone() || (account == selection)) {
			return accounts(context, requirementName, filter);
		}

		Record supplierRecord = new Record(account);
		supplierRecord.add("", name);
		supplierRecord.add("", account.getName());
		list.add(supplierRecord);

		return null;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param requirementName
	 * @return {@link Result}
	 */
	protected Result vatReturnRequirement(Context context, ResultList list,
			String requirementName) {
		Requirement vatReturnReq = get(VAT_RETURN);
		String vatReturn = context.getSelection(VAT_RETURNS);

		if (vatReturn != null) {
			vatReturnReq.setValue(vatReturn);
		}

		String value = vatReturnReq.getValue();
		Object selection = context.getSelection("values");
		if (!vatReturnReq.isDone() || (value == selection)) {
			return getVatReturnResult(context);
		}

		Record record = new Record(value);
		record.add("", "VAT Return : ");
		record.add("", value);
		list.add(record);

		return null;

	}

	/**
	 * 
	 * @param context
	 * @return {@link Result}
	 */
	protected Result getVatReturnResult(Context context) {
		Result result = context.makeResult();
		ResultList vatReturnsList = new ResultList(VAT_RETURNS);

		Object last = context.getLast(RequirementType.VAT_RETURN);
		if (last != null) {
			vatReturnsList.add(createVatReturnRecord((String) last));
		}

		List<String> vatReturns = getVatReturns(context.getHibernateSession());
		for (int i = 0; i < VALUES_TO_SHOW && i < vatReturns.size(); i++) {
			String vatReturn = vatReturns.get(i);
			if (vatReturn != last) {
				vatReturnsList.add(createVatReturnRecord((String) vatReturn));
			}
		}

		int size = vatReturnsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Vat Return");
		}

		result.add(message.toString());
		result.add(vatReturnsList);
		result.add("Select the Vat Return");

		return result;
	}

	/**
	 * 
	 * @param vatReturn
	 * @return {@link Record}
	 */
	private Record createVatReturnRecord(String vatReturn) {
		Record record = new Record(vatReturn);
		record.add("Name", vatReturn);
		return record;
	}

	/**
	 * 
	 * @param session
	 * @return {@link List}
	 */
	private List<String> getVatReturns(Session session) {

		ArrayList<String> vatReturnList = new ArrayList<String>();
		vatReturnList.add("UK VAT");
		vatReturnList.add("VAT 3(Ireland)");

		return vatReturnList;
	}

	/**
	 * 
	 * @param context
	 * @param filter
	 * @return {@link Result}
	 */
	protected Result accounts(Context context, String requirementName,
			ListFilter<ClientAccount> filter) {
		Result result = context.makeResult();

		ResultList supplierList = new ResultList(requirementName);

		Object last = context.getLast(RequirementType.ACCOUNT);
		List<ClientAccount> skipAccounts = new ArrayList<ClientAccount>();
		if (last != null) {
			supplierList.add(createAccountRecord((ClientAccount) last));
			skipAccounts.add((ClientAccount) last);
		}
		List<ClientAccount> accounts = Utility.filteredList(filter,
				getClientCompany().getAccounts());
		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<ClientAccount> pagination = pagination(context, selection,
				actions, accounts, skipAccounts, VALUES_TO_SHOW);

		for (ClientAccount account : pagination) {
			supplierList.add(createAccountRecord(account));
		}

		int size = supplierList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Account");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create New Account");

		result.add(message.toString());
		result.add(supplierList);
		result.add(actions);
		result.add(commandList);
		return result;
	}

	protected Record createAccountRecord(ClientAccount last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		record.add("Number", last.getNumber());
		record.add("Balance", last.getCurrentBalance());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param reqName
	 * @param displayName
	 * @param name
	 * @return {@link Result}
	 */
	protected Result amountRequirement(Context context, ResultList list,
			String reqName, String displayName, String name) {

		Requirement amountReq = get(reqName);
		String input = (String) context.getAttribute(INPUT_ATTR);

		if (input.equals(reqName)) {
			amountReq.setValue(context.getNumber());
			context.setAttribute(INPUT_ATTR, "optional");
		}
		if (!amountReq.isDone()) {
			context.setAttribute(INPUT_ATTR, reqName);
			return number(context, displayName, null);
		}
		Object selection = context.getSelection("values");
		String amount = (String) amountReq.getValue();
		if (selection != null && selection == reqName) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayName, amount);
		}

		Record amountRecord = new Record(reqName);
		amountRecord.add("", name);
		amountRecord.add("", amount);
		list.add(amountRecord);
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	protected Result accountsOptionalRequirement(Context context,
			ResultList list, Object selection, String reqName) {

		Object accountObj = context.getSelection(ACCOUNT);
		if (accountObj instanceof ActionNames) {
			accountObj = null;
			selection = reqName;
		}

		Requirement accountReq = get(reqName);
		if (accountObj != null) {
			accountReq.setValue(accountObj);
		}
		ClientAccount account = (ClientAccount) accountReq.getValue();
		if (selection != null)
			if (selection == reqName) {
				context.setAttribute(INPUT_ATTR, reqName);
				return account(context, account);
			}
		Record customerGroupRecord = new Record(reqName);
		customerGroupRecord.add("Name", reqName);
		customerGroupRecord.add(
				"Value",
				account == null ? "" : account.getName() + "-"
						+ account.getNumber());
		list.add(customerGroupRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param oldAccount
	 * @return
	 */
	private Result account(Context context, ClientAccount oldAccount) {
		Result result = context.makeResult();
		result.add("Select Account");
		Object last = context.getLast(RequirementType.ACCOUNT);
		ResultList list = new ResultList(ACCOUNT);
		List<ClientAccount> skipAccounts = new ArrayList<ClientAccount>();
		if (oldAccount != null) {
			list.add(createAccountRecord(oldAccount));
			skipAccounts.add((ClientAccount) last);

		}
		List<ClientAccount> accountsList = getAccountsList(context);

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<ClientAccount> pagination = pagination(context, selection,
				actions, accountsList, skipAccounts, VALUES_TO_SHOW);

		for (ClientAccount account : pagination) {
			list.add(createAccountRecord(account));
		}

		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Account");
		result.add(commandList);

		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private List<ClientAccount> getAccountsList(Context context) {
		List<ClientAccount> accounts = getClientCompany().getAccounts();
		List<ClientAccount> list = new ArrayList<ClientAccount>(accounts.size());
		for (ClientAccount acc : accounts) {
			if (acc.getIsActive())
				list.add(acc);
		}
		return list;
	}

	protected Result statusRequirement(Context context, ResultList list,
			String reqName, String name) {
		Requirement statusReq = get(reqName);
		String statuses = context.getSelection("statusmethods");

		if (statuses != null) {
			statusReq.setValue(statuses);
		}
		String status = statusReq.getValue();
		Object selection = context.getSelection("values");

		if (!statusReq.isDone() || status == selection) {
			return statusPrevious(context, null);
		}
		Record paymentTermsRecord = new Record(status);
		paymentTermsRecord.add("", name);
		paymentTermsRecord.add("", status);
		list.add(paymentTermsRecord);

		return null;
	}

	private Result statusPrevious(Context context, String oldstatus) {
		List<String> statusmethods = getstatusmethod();
		Result result = context.makeResult();
		result.add("Select Status");

		ResultList list = new ResultList("statusmethods");
		int num = 0;
		if (oldstatus != null) {
			list.add(createStatusMethodRecord(oldstatus));
			num++;
		}
		for (String stats : statusmethods) {
			if (stats != oldstatus) {
				list.add(createStatusMethodRecord(stats));
				num++;
			}
		}
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	protected Result shippingTermsRequirement(Context context, ResultList list,
			Object selection) {
		Object shippingObj = context.getSelection(SHIPPING_TERMS);
		Requirement shippingTermsReq = get("shippingTerms");
		ClientShippingTerms shippingTerm = (ClientShippingTerms) shippingTermsReq
				.getValue();

		if (selection != null)
			if (selection == "Shipping Terms") {
				context.setAttribute(INPUT_ATTR, "Shipping Terms");
				return shippingTerms(context, shippingTerm);
			}
		if (shippingObj != null) {
			shippingTerm = (ClientShippingTerms) shippingObj;
			shippingTermsReq.setValue(shippingTerm);
		}

		Record shippingTermRecord = new Record("Shipping Terms");
		shippingTermRecord.add("Name", getConstants().shippingTerms());
		shippingTermRecord.add("Value", shippingTerm.getName());
		list.add(shippingTermRecord);
		return null;
	}

	private Result shippingTerms(Context context,
			ClientShippingTerms oldshippingTerm) {
		List<ClientShippingTerms> shippingTerms = getShippingTerms();
		Result result = context.makeResult();
		result.add("Select ShippingTerms");

		ResultList list = new ResultList(SHIPPING_TERMS);
		int num = 0;
		if (oldshippingTerm != null) {
			list.add(createShippingTermRecord(oldshippingTerm));
			num++;
		}
		for (ClientShippingTerms term : shippingTerms) {
			if (term != oldshippingTerm) {
				list.add(createShippingTermRecord(term));
				num++;
			}
			if (num == VALUES_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create ShippingTerms");
		result.add(commandList);
		return result;
	}

	private Record createShippingTermRecord(ClientShippingTerms oldshippingTerm) {
		Record record = new Record(oldshippingTerm);
		record.add("Name", oldshippingTerm.getName());
		record.add("Desc", oldshippingTerm.getDescription());
		return record;
	}

	private List<ClientShippingTerms> getShippingTerms() {
		// TODO Auto-generated method stub
		return getClientCompany().getShippingTerms();
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	protected Result shippingMethodRequirement(Context context,
			ResultList list, Object selection) {
		Object shippingMethodObj = context.getSelection(SHIPPING_METHODS);
		Requirement shippingMethodReq = get(SHIPPING_METHODS);
		ClientShippingMethod shippingMethods = (ClientShippingMethod) shippingMethodReq
				.getValue();

		if (selection != null) {
			if (selection == SHIPPING_METHODS) {
				context.setAttribute(INPUT_ATTR, SHIPPING_METHODS);
				return shippingMethods(context, shippingMethods);
			}
		}
		if (shippingMethodObj != null) {
			shippingMethods = (ClientShippingMethod) shippingMethodObj;
			shippingMethodReq.setValue(shippingMethods);
		}

		Record shippingTermRecord = new Record(SHIPPING_METHODS);
		shippingTermRecord.add("", getConstants().shippingMethod());
		shippingTermRecord.add("", shippingMethods.getName());
		list.add(shippingTermRecord);
		return null;
	}

	private Result shippingMethods(Context context,
			ClientShippingMethod oldShippingMethods) {
		List<ClientShippingMethod> shippingMethods = getShippingMethods();
		Result result = context.makeResult();
		result.add(getMessages().pleaseSelect(getConstants().shippingMethod()));

		ResultList list = new ResultList(SHIPPING_METHODS);
		int num = 0;
		if (oldShippingMethods != null) {
			list.add(createShippingMethodsRecord(oldShippingMethods));
			num++;
		}
		for (ClientShippingMethod term : shippingMethods) {
			if (term != oldShippingMethods) {
				list.add(createShippingMethodsRecord(term));
				num++;
			}
			if (num == VALUES_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create ShippingMethods");
		result.add(commandList);
		return result;
	}

	private Record createShippingMethodsRecord(
			ClientShippingMethod oldShippingMethods) {
		Record record = new Record(oldShippingMethods);
		record.add("Name", oldShippingMethods.getName());
		record.add("Desc", oldShippingMethods.getDescription());
		return record;
	}

	private List<ClientShippingMethod> getShippingMethods() {
		return getClientCompany().getShippingMethods();
	}

	public String getDateAsString(ClientFinanceDate date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(getClientCompany()
				.getPreferences().getDateFormat());
		return format.format(date.getDateAsObject());
	}

	public Result closeCommand() {
		markDone();
		Result result = new Result(getMessages().pleaseEnter(
				getConstants().command()));
		return result;
	}

	protected boolean isValidEmail(String email) {
		return (email
				.matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"));
	}

}
