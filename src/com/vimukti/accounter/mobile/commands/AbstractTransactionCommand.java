package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;

public abstract class AbstractTransactionCommand extends AbstractCommand {
	private static final int ITEMS_TO_SHOW = 5;
	private static final int CUSTOMERS_TO_SHOW = 5;
	private static final int PAYMENTTERMS_TO_SHOW = 0;
	private static final int CONTACTS_TO_SHOW = 5;
	protected static final String PAYMENT_TERMS = "paymentTerms";
	protected static final int ACCOUNTS_TO_SHOW = 5;
	protected static final Object TRANSACTION_ITEM_PROCESS = null;
	protected static final String OLD_TRANSACTION_ITEM_ATTR = null;
	private static final String ITEM_DETAILS = null;
	private static final String ITEM_PROPERTY_ATTR = null;
	private static final int PAYMENTMETHODS_TO_SHOW = 5;
	private static final String PAYMENT_MENTHOD = "Payment method";
	private static final int PAYEES_TO_SHOW = 5;
	protected static final int EXPENSES_TO_SHOW = 5;

	protected Result itemsRequirement(Context context) {
		Requirement itemsReq = get("items");
		List<TransactionItem> transactionItems = context.getSelections("items");
		if (!itemsReq.isDone()) {
			if (transactionItems.size() > 0) {
				itemsReq.setValue(transactionItems);
			} else {
				return items(context);
			}
		}
		if (transactionItems != null && transactionItems.size() > 0) {
			List<TransactionItem> items = itemsReq.getValue();
			items.addAll(transactionItems);
		}
		return null;
	}

	protected Result transactionItemProcess(Context context) {
		TransactionItem transactionItem = (TransactionItem) context
				.getAttribute(OLD_TRANSACTION_ITEM_ATTR);
		Result result = transactionItem(context, transactionItem);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement itemsReq = get("items");
				List<TransactionItem> transItems = itemsReq.getValue();
				transItems.remove(transactionItem);
				context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
			}
		}
		return result;
	}

	protected Result transactionItem(Context context,
			TransactionItem transactionItem) {
		context.setAttribute(PROCESS_ATTR, TRANSACTION_ITEM_PROCESS);
		context.setAttribute(OLD_TRANSACTION_ITEM_ATTR, transactionItem);

		String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ITEM_PROPERTY_ATTR);
			if (lineAttr.equals("quantity")) {
				transactionItem.getQuantity().setValue(context.getDouble());
			} else if (lineAttr.equals("unitPrice")) {
				transactionItem.setUnitPrice(context.getDouble());
			} else if (lineAttr.equals("discount")) {
				transactionItem.setDiscount(context.getDouble());
			} else if (lineAttr.equals("taxCode")) {
				TAXCode taxCode = context.getSelection(TAXCODE);
				transactionItem.setTaxCode(taxCode);
			}
		} else {
			Object selection = context.getSelection(ITEM_DETAILS);
			if (selection != null) {
				if (selection == transactionItem.getQuantity()) {
					context.setAttribute(ITEM_PROPERTY_ATTR, "quantity");
					return amount(context, "Enter Quantity", transactionItem
							.getQuantity().getValue());
				} else if (selection.equals("unitPrice")) {
					context.setAttribute(ITEM_PROPERTY_ATTR, "unitPrice");
					return amount(context, "Enter Unitprice",
							transactionItem.getUnitPrice());
				} else if (selection.equals("discount")) {
					context.setAttribute(ITEM_PROPERTY_ATTR, "discount");
					return amount(context, "Enter Discount",
							transactionItem.getDiscount());
				} else if (selection == transactionItem.getTaxCode().getName()) {
					context.setAttribute(ITEM_PROPERTY_ATTR, "taxCode");
					return taxCode(context, transactionItem.getTaxCode());
				} else if (selection.equals("Tax")) {
					transactionItem.setTaxable(!transactionItem.isTaxable());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH) {
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}

		ResultList list = new ResultList(ITEM_DETAILS);
		Record record = new Record(transactionItem.getQuantity());
		record.add("", "Quantity");
		record.add("", transactionItem.getQuantity());
		list.add(record);

		record = new Record("unitPrice");
		record.add("", "Unit Price");
		record.add("", transactionItem.getUnitPrice());
		list.add(record);

		record = new Record("discount");
		record.add("", "Discount %");
		record.add("", transactionItem.getDiscount());
		list.add(record);

		Company company = getCompany();
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			record = new Record(transactionItem.getTaxCode().getName());
			record.add("", "VatCode");
			record.add("", transactionItem.getTaxCode().getName());
			list.add(record);
		} else {
			record = new Record("Tax");
			record.add("", "Tax");
			if (transactionItem.isTaxable()) {
				record.add("", "Taxable");
			} else {
				record.add("", "Non-Taxable");
			}
			list.add(record);
		}

		Result result = context.makeResult();
		result.add("Item details");
		result.add("Item Name :" + transactionItem.getItem().getName());
		result.add(list);
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			result.add("Item Vat :" + transactionItem.getVATfraction());
		}
		result.add("Item Total :" + transactionItem.getLineTotal());

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", "Delete");
		actions.add(record);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		actions.add(record);
		result.add(actions);
		return result;
	}

	protected Result shipToRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("shipTo");
		Address shipTo = (Address) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("shipTo")) {
			Address input = context.getAddress();
			if (input == null) {
				input = context.getAddress();
			}
			shipTo = input;
			req.setValue(shipTo);
		}

		if (selection == shipTo) {
			context.setAttribute(INPUT_ATTR, "shipTo");
			return address(context, "Ship to Address", shipTo);
		}

		Record shipToRecord = new Record(shipTo);
		shipToRecord.add("Name", "Ship To");
		shipToRecord.add("Value", shipTo.toString());
		list.add(shipToRecord);
		return null;
	}

	protected Result items(Context context) {
		Result result = context.makeResult();
		List<Item> items = getItems(context.getHibernateSession());
		ResultList list = new ResultList("items");
		Object last = context.getLast(RequirementType.ITEM);
		int num = 0;
		if (last != null) {
			list.add(creatItemRecord((Item) last));
			num++;
		}
		Requirement itemsReq = get("items");
		List<TransactionItem> transItems = itemsReq.getValue();
		List<Item> availableItems = new ArrayList<Item>();
		for (TransactionItem transactionItem : transItems) {
			availableItems.add(transactionItem.getItem());
		}
		for (Item item : items) {
			if (item != last || !availableItems.contains(item)) {
				list.add(creatItemRecord(item));
				num++;
			}
			if (num == ITEMS_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add("Slect an Item(s).");
		} else {
			result.add("You don't have Items.");
		}

		result.add(list);
		CommandList commands = new CommandList();
		commands.add("Create New Item");
		return result;
	}

	protected Result customerRequirement(Context context) {
		Requirement customerReq = get("customer");
		Customer customer = context.getSelection("customers");
		if (customer != null) {
			customerReq.setValue(customer);
		}
		if (!customerReq.isDone()) {
			return customers(context);
		}
		return null;
	}

	protected Result paymentMethodRequirement(Context context) {
		Requirement paymentMethodReq = get(PAYMENT_MENTHOD);
		String paymentMethod = context.getSelection(PAYMENT_MENTHOD);
		if (paymentMethod != null) {
			paymentMethodReq.setValue(paymentMethod);
		}
		if (!paymentMethodReq.isDone()) {
			return paymentMethod(context, null);
		}
		return null;
	}

	protected Result customers(Context context) {
		Result result = context.makeResult();
		ResultList customersList = new ResultList("customers");

		Object last = context.getLast(RequirementType.CUSTOMER);
		int num = 0;
		if (last != null) {
			customersList.add(createCustomerRecord((Customer) last));
			num++;
		}
		List<Customer> customers = getCustomers(context.getHibernateSession());
		for (Customer customer : customers) {
			if (customer != last) {
				customersList.add(createCustomerRecord(customer));
				num++;
			}
			if (num == CUSTOMERS_TO_SHOW) {
				break;
			}
		}
		int size = customersList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Customer");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(customersList);
		result.add(commandList);
		result.add("Type for Customer");
		return result;
	}

	protected Result paymentTermRequirement(Context context, ResultList list,
			Object selection) {
		Object payamentObj = context.getSelection(PAYMENT_TERMS);
		Requirement paymentReq = get("paymentTerms");
		PaymentTerms paymentTerm = (PaymentTerms) paymentReq.getValue();

		if (selection == paymentTerm) {
			return paymentTerms(context, paymentTerm);

		}
		if (payamentObj != null) {
			paymentTerm = (PaymentTerms) payamentObj;
			paymentReq.setValue(paymentTerm);
		}

		Record paymentTermRecord = new Record(paymentTerm);
		paymentTermRecord.add("Name", "Payment Terms");
		paymentTermRecord.add("Value", paymentTerm.getName());
		list.add(paymentTermRecord);
		return null;
	}

	protected Result billToRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("billTo");
		Address billTo = (Address) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("billTo")) {
			Address input = context.getSelection("address");
			if (input == null) {
				input = context.getAddress();
			}
			billTo = input;
			req.setValue(billTo);
		}

		if (selection == billTo) {
			context.setAttribute(INPUT_ATTR, "billTo");
			return address(context, "Bill to Address", billTo);
		}

		Record billToRecord = new Record(billTo);
		billToRecord.add("Name", "Bill To");
		billToRecord.add("Value", billTo.toString());
		list.add(billToRecord);
		return null;
	}

	protected Result contactRequirement(Context context, ResultList list,
			Object selection, Payee payee) {
		Object contactObj = context.getSelection(CONTACTS);
		Requirement contactReq = get("contact");
		Contact contact = (Contact) contactReq.getValue();
		if (selection == contact) {
			return contactList(context, payee, contact);

		}
		if (contactObj != null) {
			contact = (Contact) contactObj;
			contactReq.setValue(contact);
		}

		Record contactRecord = new Record(contact);
		contactRecord.add("Name", "Customer Contact");
		contactRecord.add("Value", contact.getName());
		list.add(contactRecord);
		return null;
	}

	private Record creatItemRecord(Item item) {
		Record record = new Record(item);
		record.add("Name", item.getName());
		record.add("Tax Code", item.getTaxCode().getName());
		return record;
	}

	protected Record createCustomerRecord(Customer customer) {
		Record record = new Record(customer);
		record.add("Name", customer.getName());
		record.add("Balance", customer.getBalance());
		return record;
	}

	protected Record createContactRecord(Contact contact) {
		Record record = new Record(contact);
		record.add("Name", contact.getName());
		return record;
	}

	protected Record createPaymentTermRecord(PaymentTerms paymentTerms) {
		Record record = new Record(paymentTerms);
		record.add("Name", paymentTerms.getName());
		record.add("Desc", paymentTerms.getDescription());
		return record;
	}

	private List<Item> getItems(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Customer> getCustomers(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<PaymentTerms> getPaymentTerms() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result paymentTerms(Context context, PaymentTerms oldPaymentTerms) {
		List<PaymentTerms> paymentTerms = getPaymentTerms();
		Result result = context.makeResult();
		result.add("Select PaymentTerms");

		ResultList list = new ResultList(PAYMENT_TERMS);
		int num = 0;
		if (oldPaymentTerms != null) {
			list.add(createPaymentTermRecord(oldPaymentTerms));
			num++;
		}
		for (PaymentTerms term : paymentTerms) {
			if (term != oldPaymentTerms) {
				list.add(createPaymentTermRecord(term));
				num++;
			}
			if (num == PAYMENTTERMS_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create PaymentTerms");
		result.add(commandList);
		return result;
	}

	protected Result contactList(Context context, Payee customer,
			Contact oldContact) {
		Set<Contact> contacts = customer.getContacts();
		ResultList list = new ResultList(CONTACTS);
		int num = 0;
		if (oldContact != null) {
			list.add(createContactRecord(oldContact));
			num++;
		}
		for (Contact contact : contacts) {
			if (contact != oldContact) {
				list.add(createContactRecord(contact));
				num++;
			}
			if (num == CONTACTS_TO_SHOW) {
				break;
			}
		}

		Result result = context.makeResult();
		result.add("Select " + customer.getName() + "'s Contact");
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Contact");
		result.add(commandList);

		return result;
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

		CommandList commandList = new CommandList();
		commandList.add("Create Paymentmethods");
		result.add(commandList);
		return result;
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

	protected Record createPayMentMethodRecord(String paymentMethod) {
		Record record = new Record(paymentMethod);
		record.add("Name", "Payment Method");
		record.add("value", paymentMethod);
		return record;
	}

	protected Result depositeOrTransferTo(Context context) {
		Requirement transferedReq = get("depositOrTransferTo");
		Account account = context.getSelection("depositOrTransferTo");
		if (!transferedReq.isDone()) {
			if (account != null) {
				transferedReq.setValue(account);
			} else {
				return accounts(context);
			}
		}
		if (account != null) {
			transferedReq.setValue(account);

		}
		return null;
	}

	protected Result accounts(Context context) {
		Result result = context.makeResult();
		ResultList list = new ResultList("depositOrTransferTo");

		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			list.add(createAccountRecord((Account) last));
			num++;
		}

		List<Account> transferAccountList = getAccounts(context
				.getHibernateSession());
		for (Account account : transferAccountList) {
			if (account != last) {
				list.add(createAccountRecord(account));
				num++;
			}
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}

		if (list.size() > 0) {
			result.add("Slect an Account.");
		}
		result.add(list);
		CommandList commands = new CommandList();
		commands.add("Create New Account");
		return result;
	}

	private List<Account> getAccounts(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Record createAccountRecord(Account last) {
		Record record = new Record(last);
		record.add("Account Number", last.getNumber());
		record.add("Account Name", last.getName());
		record.add("Account Type", getAccountTypeString(last.getType()));
		return record;
	}

	protected String getAccountTypeString(int accountType) {

		String accountTypeName = null;
		switch (accountType) {
		case ClientAccount.TYPE_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_INCOME;
			break;
		case ClientAccount.TYPE_OTHER_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_INCOME;
			break;
		case ClientAccount.TYPE_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_EXPENSE;
			break;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_EXPENSE;
			break;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			accountTypeName = AccounterClientConstants.TYPE_COST_OF_GOODS_SOLD;
			break;
		case ClientAccount.TYPE_CASH:
			accountTypeName = AccounterClientConstants.TYPE_CASH;
			break;
		case ClientAccount.TYPE_BANK:
			accountTypeName = AccounterClientConstants.TYPE_BANK;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_ASSET;
			break;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_INVENTORY_ASSET;
			break;
		case ClientAccount.TYPE_OTHER_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_ASSET;
			break;
		case ClientAccount.TYPE_FIXED_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_FIXED_ASSET;
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			accountTypeName = AccounterClientConstants.TYPE_CREDIT_CARD;
			break;
		case ClientAccount.TYPE_PAYPAL:
			accountTypeName = AccounterClientConstants.TYPE_PAYPAL;
			break;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_PAYROLL_LIABILITY;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_LIABILITY;
			break;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_LONG_TERM_LIABILITY;
			break;
		case ClientAccount.TYPE_EQUITY:
			accountTypeName = AccounterClientConstants.TYPE_EQUITY;
			break;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_RECEIVABLE;
			break;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_PAYABLE;
			break;

		}
		return accountTypeName;
	}

	protected Result phoneRequirement(Context context, ResultList list,
			String selection) {
		Result result = context.makeResult();
		Requirement req = get("phone");
		String phoneNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("phone")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			phoneNo = order;
			req.setValue(phoneNo);
		}

		if (selection == phoneNo) {
			context.setAttribute(INPUT_ATTR, "phone");
			return number(context, "Enter Phone number", phoneNo);
		}

		Record cashSaleNoRec = new Record(phoneNo);
		cashSaleNoRec.add("Name", "Phone Number");
		cashSaleNoRec.add("Value", phoneNo);
		list.add(cashSaleNoRec);
		result.add(list);
		return result;
	}

	protected Result payeeRequirement(Context context) {
		Requirement payeeReq = get("payee");
		Payee payee = context.getSelection("payees");
		if (payee != null) {
			payeeReq.setValue(payee);
		}
		if (!payeeReq.isDone()) {
			return payee(context);
		}
		return null;
	}

	private Result payee(Context context) {
		Result result = context.makeResult();
		ResultList payeeList = new ResultList("payees");

		Object last = context.getLast(RequirementType.PAYEE);
		int num = 0;
		if (last != null) {
			payeeList.add(createPayeeRecord((Payee) last));
			num++;
		}
		List<Payee> payees = getPayees(context.getHibernateSession());
		for (Payee payee : payees) {
			if (payee != last) {
				payeeList.add(createPayeeRecord(payee));
				num++;
			}
			if (num == PAYEES_TO_SHOW) {
				break;
			}
		}
		int size = payeeList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Payee");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(payeeList);
		result.add(commandList);
		result.add("Type of Payee");
		return result;
	}

	private List<Payee> getPayees(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Record createPayeeRecord(Payee payee) {
		Record record = new Record(payee);
		record.add("Name", payee.getName());
		record.add("Balance", payee.getBalance());
		return record;
	}

	private Result accountsRequirement(Context context) {
		Requirement itemsReq = get("accounts");
		List<TransactionItem> transactionItems = context
				.getSelections("accounts");
		if (!itemsReq.isDone()) {
			if (transactionItems.size() > 0) {
				itemsReq.setValue(transactionItems);
			} else {
				return accountItems(context);
			}
		}
		if (transactionItems != null && transactionItems.size() > 0) {
			List<TransactionItem> items = itemsReq.getValue();
			items.addAll(transactionItems);
		}
		return null;
	}

	private Result accountItems(Context context) {
		Result result = context.makeResult();
		List<Item> items = getItems(context.getHibernateSession());
		ResultList list = new ResultList("accounts");
		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			list.add(creatAccountItemRecord((Item) last));
			num++;
		}
		Requirement itemsReq = get("items");
		List<TransactionItem> transItems = itemsReq.getValue();
		List<Item> availableItems = new ArrayList<Item>();
		for (TransactionItem transactionItem : transItems) {
			availableItems.add(transactionItem.getItem());
		}
		for (Item item : items) {
			if (item != last || !availableItems.contains(item)) {
				list.add(creatAccountItemRecord(item));
				num++;
			}
			if (num == ACCOUNTS_TO_SHOW) {
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

	private Record creatAccountItemRecord(Item last) {
		Record record = new Record(last);
		record.add("Account Name", last.getName());
		record.add("Account Type", getAccountTypeString(last.getType()));
		return record;
	}

}
