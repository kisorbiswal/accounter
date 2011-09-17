package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class AbstractTransactionCommand extends Command {
	private static final int ITEMS_TO_SHOW = 5;
	private static final int CUSTOMERS_TO_SHOW = 5;

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
		if (!customerReq.isDone()) {
			if (customer != null) {
				customerReq.setValue(customer);
			} else {
				return customers(context);
			}
		}
		if (customer != null) {
			customerReq.setValue(customer);
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

	private Record creatItemRecord(Item item) {
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

	protected Record createCustomerRecord(Customer last) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result text(Context context, String string, String memo) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result address(Context context, Address billTo) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result number(Context context, String invoiceNo) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result paymentList(Context context, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result contactList(Context context, Customer customer,
			String string) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result date(Context context, Date date) {
		// TODO Auto-generated method stub
		return null;
	}
}
