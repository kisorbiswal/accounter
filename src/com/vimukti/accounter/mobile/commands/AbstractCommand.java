package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

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
	protected static final int INCOMEACCOUNTS_TO_SHOW = 5;
	protected static final int VENDORS_TO_SHOW = 5;
	protected static final int ITEMGROUPS_TO_SHOW = 5;
	protected static final String PAYMENT_METHOD = "Payment method";
	private static final int PAYMENTMETHODS_TO_SHOW = 5;

	protected static final String MEMO = "memo";

	protected static final String ORDER_NO = "orderNo";

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
			Double order = context.getSelection(name);
			if (order == null) {
				order = context.getDouble();
			}
			balance = order;
			req.setValue(balance);
		}

		if (selection == balance) {
			context.setAttribute(INPUT_ATTR, name);
			return amount(context, displayString, balance);
		}

		Record balanceRecord = new Record(balance);
		balanceRecord.add("Name", name);
		balanceRecord.add("Value", balance);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
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

	protected Result dateOptionalRequirement(Context context, ResultList list,
			String name, String displayString, Object selection) {
		Requirement req = get(name);
		Date dueDate = (Date) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(name)) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			dueDate = date;
			req.setValue(dueDate);
		}
		if (selection == dueDate) {
			context.setAttribute(INPUT_ATTR, name);
			return date(context, displayString, dueDate);
		}

		Record dueDateRecord = new Record(dueDate);
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
		return getCompany().getTaxCodes();

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

	protected Result createSupplierRequirement(Context context) {
		Requirement supplierReq = get("supplier");
		Vendor vendor = context.getSelection("suppliers");
		if (vendor != null) {
			supplierReq.setValue(vendor);
		}
		if (!supplierReq.isDone()) {
			return vendors(context);
		}
		return null;
	}

	protected Result vendors(Context context) {
		Result result = context.makeResult();
		ResultList supplierList = new ResultList("suppliers");

		Object last = context.getLast(RequirementType.VENDOR);
		int num = 0;
		if (last != null) {
			supplierList.add(createVendorRecord((Vendor) last));
			num++;
		}
		List<Vendor> vendors = getVendors();
		for (Vendor vendor : vendors) {
			if (vendor != last) {
				supplierList.add(createVendorRecord(vendor));
				num++;
			}
			if (num == VENDORS_TO_SHOW) {
				break;
			}
		}
		int size = supplierList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Supplier");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(supplierList);
		result.add(commandList);
		result.add("Type for Supplier");
		return result;
	}

	private Record createVendorRecord(Vendor last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		record.add("Balance", last.getBalance());
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
		}

		if (selection == memo) {
			context.setAttribute(attribute, name);
			return text(context, displayName, memo);
		}

		Record memoRecord = new Record(memo);
		memoRecord.add("Name", name);
		memoRecord.add("Value", memo);
		list.add(memoRecord);
		return null;
	}

	protected Result numberOptionalRequirement(Context context,
			ResultList list, Object selection, String name, String displayName) {
		Requirement req = get(name);
		String number = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(name)) {
			String input = context.getSelection(TEXT);
			if (input == null) {
				input = context.getString();
			}
			number = input;
			req.setValue(number);
		}

		if (selection == number) {
			context.setAttribute(attribute, name);
			return text(context, displayName, number);
		}

		Record numberRecord = new Record(number);
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
		Date transDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(DATE)) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			transDate = date;
			dateReq.setValue(transDate);
		}
		if (selection == transDate) {
			context.setAttribute(INPUT_ATTR, DATE);
			return date(context, "Enter Date", transDate);
		}

		Record transDateRecord = new Record(transDate);
		transDateRecord.add("Name", "Date");
		transDateRecord.add("Value", transDate.toString());
		list.add(transDateRecord);
		return null;
	}

	protected List<Customer> getCustomers(Boolean isActive) {
		ArrayList<Customer> customers = getCompany().getCustomers();
		ArrayList<Customer> result = new ArrayList<Customer>();
		for (Customer customer : customers) {
			if (isActive) {
				if (customer.isActive()) {
					result.add(customer);
				}
			} else {
				result.add(customer);
			}
		}
		return null;
	}

	private List<Vendor> getVendors() {
		return getCompany().getVendors();
	}

	protected List<Vendor> getVendors(boolean isActive) {
		ArrayList<Vendor> vendors = getCompany().getVendors();
		ArrayList<Vendor> result = new ArrayList<Vendor>();

		for (Vendor vendor : vendors) {
			if (isActive) {
				if (vendor.isActive()) {
					result.add(vendor);
				}
			} else {
				result.add(vendor);
			}
		}

		return null;
	}

	protected Result paymentMethodRequirement(Context context, ResultList list,
			String selection) {
		Object payamentMethodObj = context.getSelection("paymentmethod");
		Requirement paymentMethodReq = get("paymentmethod");
		String paymentmethod = (String) paymentMethodReq.getValue();

		if (selection == paymentmethod) {
			return paymentMethod(context, selection);

		}
		if (payamentMethodObj != null) {
			paymentmethod = (String) payamentMethodObj;
			paymentMethodReq.setValue(paymentmethod);
		}

		Record paymentTermRecord = new Record(paymentmethod);
		paymentTermRecord.add("Name", "ayment Method");
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

		ResultList list = new ResultList("paymentmethod");
		int num = 0;
		if (oldpaymentmethod != null) {
			list.add(createPayMentMethodRecord(oldpaymentmethod));
			num++;
		}
		for (String paymentmethod : paymentmethods) {
			if (paymentmethod != oldpaymentmethod) {
				list.add(createPayMentMethodRecord(paymentmethod));
				num++;
			}
			if (num == PAYMENTMETHODS_TO_SHOW) {
				break;
			}
		}
		result.add(list);
		return result;
	}

	protected Record createPayMentMethodRecord(String paymentMethod) {
		Record record = new Record(paymentMethod);
		record.add("Name", "Payment Method");
		record.add("value", paymentMethod);
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
		// Session session = context.getSession();
		// try {
		// new FinanceTool().createServerObject(obj, user, session);
		// } catch (AccounterException e) {
		// e.printStackTrace();
		// }
	}
}
