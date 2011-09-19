package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
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

public abstract class AbstractTransactionCommand extends AbstractCommand {
	private static final int ITEMS_TO_SHOW = 5;
	private static final int CUSTOMERS_TO_SHOW = 5;
	private static final int PAYMENTTERMS_TO_SHOW = 0;
	private static final int CONTACTS_TO_SHOW = 5;
	protected static final String CONTACTS = "contacts";
	protected static final String PAYMENT_TERMS = "paymentTerms";
	protected static final Object TRANSACTION_ITEM_PROCESS = null;
	protected static final String OLD_TRANSACTION_ITEM_ATTR = null;
	private static final String ITEM_DETAILS = null;
	private static final String ITEM_PROPERTY_ATTR = null;
	protected static final String TAXCODE = null;
	private static final int TAXCODE_TO_SHOW = 0;

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
		ActionNames actionName = context.getSelection(ACTIONS);
		Result result = transactionItem(context, transactionItem);
		if (result == null) {
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

	protected Result items(Context context) {
		Result result = context.makeResult();
		List<Item> items = getItems(context.getSession());
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

	protected Result customers(Context context) {
		Result result = context.makeResult();
		ResultList customersList = new ResultList("customers");

		Object last = context.getLast(RequirementType.CUSTOMER);
		int num = 0;
		if (last != null) {
			customersList.add(createCustomerRecord((Customer) last));
			num++;
		}
		List<Customer> customers = getCustomers(context.getSession());
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

	private Record createTaxCodeRecord(TAXCode taxCode) {
		Record record = new Record(taxCode);
		record.add("", taxCode.getName());
		return record;
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

	protected Company getCompany() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<TAXCode> getTaxCodes() {
		// TODO Auto-generated method stub
		return null;
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

	protected Result contactList(Context context, Customer customer,
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

}
