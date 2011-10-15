package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXItemGroup;
import com.vimukti.accounter.core.TransactionIssuePayment;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class AbstractTransactionCommand extends AbstractCommand {
	protected static final int ITEMS_TO_SHOW = 5;
	protected static final int CUSTOMERS_TO_SHOW = 5;
	private static final int PAYMENTTERMS_TO_SHOW = 5;
	private static final int CONTACTS_TO_SHOW = 5;
	protected static final String PAYMENT_TERMS = "paymentTerms";
	protected static final int ACCOUNTS_TO_SHOW = 5;
	protected static final String TRANSACTION_ITEM_PROCESS = "transactionitemprocess";
	protected static final String OLD_TRANSACTION_ITEM_ATTR = "oldtransactionitemattr";
	protected static final String ITEM_DETAILS = "itemdetails";
	protected static final String ITEM_PROPERTY_ATTR = "itempropertyattr";
	protected static final String PAYMENT_MENTHOD = "Payment method";
	private static final int PAYEES_TO_SHOW = 5;
	protected static final int BANK_ACCOUNTS_TO_SHOW = 5;
	protected static final int EXPENSES_TO_SHOW = 5;
	protected static final int SHIPPING_TERMS_TO_SHOW = 5;
	protected static final int BILLS_TO_SHOW = 5;
	protected static final int ESTIMATES_TO_SHOW = 5;
	protected static final int INVOICES_TO_SHOW = 5;

	protected static final int PAYMENTS_TO_SHOW = 5;

	protected static final int ISSUE_PAYMENTS_TO_SHOW = 5;
	protected static final String ACCOUNT_NUMBER = "Account Number";

	protected static final String TRANSACTION_ACCOUNT_ITEM_PROCESS = "transaccountitemprocess";
	protected static final String OLD_TRANSACTION_ACCOUNT_ITEM_ATTR = "oldtransaccountitemprocess";
	private static final String ACCOUNT_ITEM_PROPERTY_ATTR = "accountitempropertyattr";
	private static final String ACCOUNT_ITEM_DETAILS = "accountitemdetails";
	protected static final String VENDOR = "vendor";
	protected static final String PAY_FROM = "payFrom";
	protected static final String US_CHECK = "Check";
	protected static final String UK_CHECK = "Cheque";
	protected static final String ACCOUNTS = "accounts";
	protected static final String BILL_TO = "billTo";
	protected static final String ITEMS = "items";
	protected static final int VENDOR_TRANSACTION = 2;
	protected static final int CUSTOMER_TRANSACTION = 1;
	protected static final String SUPPLIER = "supplier";
	protected static final String CONTACT = "contact";
	protected static final String PHONE = "phone";
	private int transactionType;

	protected Result itemsRequirement(Context context, Result result,
			ResultList actions) {
		Requirement transItemsReq = get(ITEMS);
		List<Item> items = context.getSelections(ITEMS);
		if (items != null && items.size() > 0) {
			for (Item item : items) {
				TransactionItem transactionItem = new TransactionItem();
				transactionItem.setType(TransactionItem.TYPE_ITEM);
				transactionItem.setItem(item);
				if (getTransactionType() == VENDOR_TRANSACTION) {
					transactionItem.setUnitPrice(item.getPurchasePrice());
				} else if (getTransactionType() == CUSTOMER_TRANSACTION) {
					transactionItem.setUnitPrice(item.getSalesPrice());
				}
				Quantity quantity = new Quantity();
				quantity.setValue(1);
				transactionItem.setQuantity(quantity);

				List<TransactionItem> transactionItems = transItemsReq
						.getValue();
				if (transactionItems == null) {
					transactionItems = new ArrayList<TransactionItem>();
					transItemsReq.setValue(transactionItems);
				}
				double lt = transactionItem.getQuantity().getValue()
						* transactionItem.getUnitPrice();
				double disc = transactionItem.getDiscount();
				transactionItem
						.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
								* disc / 100))
								: lt);
				transactionItems.add(transactionItem);
				if (transactionItem.getUnitPrice() == 0) {
					context.putSelection(ITEM_DETAILS, "unitPrice");
					Result transactionItemResult = transactionItem(context,
							transactionItem);
					if (transactionItemResult != null) {
						return transactionItemResult;
					}
				} else if (context.getCompany().getPreferences().isTrackTax()
						&& context.getCompany().getPreferences()
								.isTaxPerDetailLine()
						&& transactionItem.getTaxCode() == null) {
					context.putSelection(ITEM_DETAILS, "taxCode");
					Result transactionItemResult = transactionItem(context,
							transactionItem);
					if (transactionItemResult != null) {
						return transactionItemResult;
					}
				}

			}

		}
		if (!transItemsReq.isDone()) {
			return items(context);
		}

		Object selection = context.getSelection("transactionItems");
		if (selection != null) {
			result = transactionItem(context, (TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ITEMS) {
			return items(context);
		}

		result.add("Items:-");
		ResultList itemsList = new ResultList("transactionItems");
		List<TransactionItem> transItems = transItemsReq.getValue();
		for (TransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getItem().getName());
			itemRec.add(", Total", item.getLineTotal());
			itemRec.add(", VatCode", item.getVATfraction());
			itemsList.add(itemRec);
		}
		result.add(itemsList);

		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
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

	protected Result transactionAccountProcess(Context context) {

		TransactionItem transactionItem = (TransactionItem) context
				.getAttribute(OLD_TRANSACTION_ACCOUNT_ITEM_ATTR);
		Result result = transactionAccountItem(context, transactionItem);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement itemsReq = get("accounts");
				List<TransactionItem> transItems = itemsReq.getValue();
				transItems.remove(transactionItem);
				context.removeAttribute(OLD_TRANSACTION_ACCOUNT_ITEM_ATTR);
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
				if (context.getDouble() != null) {
					transactionItem.getQuantity().setValue(context.getDouble());
				} else {
					transactionItem.getQuantity().setValue(
							context.getInteger().doubleValue());
				}
			} else if (lineAttr.equals("unitPrice")) {
				if (context.getDouble() != null) {
					transactionItem.setUnitPrice(context.getDouble());
				} else {
					transactionItem.setUnitPrice(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals("discount")) {
				if (context.getDouble() != null) {
					transactionItem.setDiscount(context.getDouble());
				} else {
					transactionItem.setDiscount(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals("taxCode")) {
				TAXCode taxCode = context.getSelection(TAXCODE);
				transactionItem.setTaxCode(taxCode);
			} else if (lineAttr.equals("description")) {
				transactionItem.setDescription(context.getString());
			}
		} else {
			Object selection = context.getSelection(ITEM_DETAILS);
			if (selection != null) {
				if (selection.equals("quantity")) {
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
				} else if (selection.equals("taxCode")) {
					context.setAttribute(ITEM_PROPERTY_ATTR, "taxCode");
					return taxCode(context, transactionItem.getTaxCode());
				} else if (selection.equals("tax")) {
					transactionItem.setTaxable(!transactionItem.isTaxable());
				} else if (selection.equals("description")) {
					context.setAttribute(ITEM_PROPERTY_ATTR, "description");
					return number(context, "Enter Description",
							transactionItem.getDescription());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					if (transactionItem.getUnitPrice() == 0) {
						context.setAttribute(ITEM_PROPERTY_ATTR, "unitPrice");
						return amount(context, "Enter Unitprice",
								transactionItem.getUnitPrice());
					} else if (context.getCompany().getPreferences()
							.isTrackTax()
							&& context.getCompany().getPreferences()
									.isTaxPerDetailLine()
							&& transactionItem.getTaxCode() == null) {
						context.setAttribute(ITEM_PROPERTY_ATTR, "taxCode");
						return taxCode(context, transactionItem.getTaxCode());
					}
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
		Record record = new Record("quantity");
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

		Company company = context.getCompany();
		if (company.getPreferences().isTrackTax()
				&& company.getPreferences().isTaxPerDetailLine()) {
			record = new Record("taxCode");
			record.add("", "VatCode");
			if (transactionItem.getTaxCode() != null) {
				record.add("", transactionItem.getTaxCode().getName());
			} else {
				record.add("", "");
			}
			list.add(record);
		} else {
			record = new Record("tax");
			if (transactionItem.isTaxable()) {
				record.add("Is Taxable", "True");
			} else {
				record.add("Is Taxable", "False");
			}
			list.add(record);
		}

		record = new Record("description");
		record.add("", "Description");
		record.add("", transactionItem.getDescription());
		list.add(record);

		Result result = context.makeResult();
		result.add("Item details");
		result.add("Item Name :" + transactionItem.getItem().getName());
		result.add(list);
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			result.add("Item Vat :" + transactionItem.getVATfraction());
		}
		double lt = transactionItem.getQuantity().getValue()
				* transactionItem.getUnitPrice();
		double disc = transactionItem.getDiscount();
		transactionItem
				.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
		result.add("Item Total :" + transactionItem.getLineTotal());

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", "Delete");
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", "Finish");
		actions.add(record);
		result.add(actions);
		return result;
	}

	protected Result transactionAccountItem(Context context,
			TransactionItem transactionItem) {
		context.setAttribute(PROCESS_ATTR, TRANSACTION_ACCOUNT_ITEM_PROCESS);
		context.setAttribute(OLD_TRANSACTION_ACCOUNT_ITEM_ATTR, transactionItem);

		String lineAttr = (String) context
				.getAttribute(ACCOUNT_ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ACCOUNT_ITEM_PROPERTY_ATTR);
			if (lineAttr.equals("amount")) {
				if (context.getDouble() != null) {
					transactionItem.setUnitPrice(context.getDouble());
				} else {
					transactionItem.setUnitPrice(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals("discount")) {
				if (context.getDouble() != null) {
					transactionItem.setDiscount(context.getDouble());
				} else {
					transactionItem.setDiscount(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals("taxCode")) {
				TAXCode taxCode = context.getSelection(TAXCODE);
				transactionItem.setTaxCode(taxCode);
			} else if (lineAttr.equals("description")) {
				transactionItem.setDescription(context.getString());
			}
		} else {
			Object selection = context.getSelection(ACCOUNT_ITEM_DETAILS);
			if (selection != null) {
				if (selection.equals("amount")) {
					context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR, "amount");
					return amount(context, "Enter Amount",
							transactionItem.getUnitPrice());
				} else if (selection.equals("discount")) {
					context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR, "discount");
					return amount(context, "Enter Discount",
							transactionItem.getDiscount());
				} else if (selection.equals("taxCode")) {
					context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR, "taxCode");
					return taxCode(context, transactionItem.getTaxCode());
				} else if (selection.equals("tax")) {
					transactionItem.setTaxable(!transactionItem.isTaxable());
				} else if (selection.equals("description")) {
					context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR,
							"description");
					return number(context, "Enter Description",
							transactionItem.getDescription());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					if (transactionItem.getUnitPrice() == 0) {
						context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR,
								"amount");
						return amount(context, "Enter Amount",
								transactionItem.getUnitPrice());
					} else if (context.getCompany().getPreferences()
							.isTrackTax()
							&& context.getCompany().getPreferences()
									.isTaxPerDetailLine()
							&& transactionItem.getTaxCode() == null) {
						context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR,
								"taxCode");
						return taxCode(context, transactionItem.getTaxCode());
					}
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_TRANSACTION_ACCOUNT_ITEM_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}

		ResultList list = new ResultList(ACCOUNT_ITEM_DETAILS);
		Record record = new Record("amount");
		record.add("", "Amount");
		record.add("", transactionItem.getUnitPrice());
		list.add(record);

		record = new Record("discount");
		record.add("", "Discount %");
		record.add("", transactionItem.getDiscount());
		list.add(record);
		Company company = context.getCompany();
		if (company.getPreferences().isTrackTax()
				&& company.getPreferences().isTaxPerDetailLine()) {
			record = new Record("taxCode");
			record.add("", "VatCode");
			if (transactionItem.getTaxCode() != null) {
				record.add("", transactionItem.getTaxCode().getName());
			} else {
				record.add("", "");
			}
			list.add(record);
		} else {
			record = new Record("tax");
			record.add("", "Tax");
			if (transactionItem.isTaxable()) {
				record.add("", "Taxable");
			} else {
				record.add("", "Non-Taxable");
			}
			list.add(record);
		}

		record = new Record("description");
		record.add("", "Description");
		record.add("", transactionItem.getDescription());
		list.add(record);

		// TODO NEED TO CALCULATE LINE TOTAL
		double lineTotal = transactionItem.getUnitPrice()
				- ((transactionItem.getUnitPrice() * transactionItem
						.getDiscount()) / 100);
		transactionItem.setLineTotal(lineTotal);
		Result result = context.makeResult();
		result.add("Account details");
		result.add("Account Name :" + transactionItem.getAccount().getName());
		double lt = transactionItem.getUnitPrice();
		double disc = transactionItem.getDiscount();
		transactionItem
				.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
		result.add("Amount Total :" + transactionItem.getLineTotal());
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", "Delete");
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
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
			return address(context, "Ship to Address", "shipTo", shipTo);
		}

		Record shipToRecord = new Record(shipTo);
		shipToRecord.add("Name", "Ship To");
		shipToRecord.add("Value", shipTo.toString());
		list.add(shipToRecord);
		return null;
	}

	protected Result items(Context context) {
		Result result = context.makeResult();
		Set<Item> items = getItems(context.getCompany());
		ResultList list = new ResultList(ITEMS);
		Object last = context.getLast(RequirementType.ITEM);
		int num = 0;
		if (last != null) {
			list.add(creatItemRecord((Item) last));
			num++;
		}
		Requirement itemsReq = get(ITEMS);
		List<TransactionItem> transItems = itemsReq.getValue();
		if (transItems == null) {
			transItems = new ArrayList<TransactionItem>();
		}
		List<Item> availableItems = new ArrayList<Item>();
		for (TransactionItem transactionItem : transItems) {
			availableItems.add(transactionItem.getItem());
		}
		for (Item item : items) {
			if (item != last && !availableItems.contains(item)) {
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
			Record record = new Record("escape");
			record.add("", "Skip");
			ResultList resultList = new ResultList("escape");
			resultList.add(record);
			result.add(resultList);
		}

		result.add(list);
		CommandList commands = new CommandList();
		commands.add("Create New Item");
		result.add(commands);
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
		Set<Customer> customers = context.getCompany().getCustomers();
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
		commandList.add("Create Customer");
		result.add(message.toString());
		result.add(customersList);
		result.add(commandList);
		// result.add("Type for Customer");
		return result;
	}

	protected Result payFromRequirement(Context context) {

		Requirement payFromReq = get(PAY_FROM);
		Account account = (Account) context.getSelection(PAY_FROM);

		if (account != null) {

			payFromReq.setValue(account);
		}

		if (!payFromReq.isDone()) {
			return paymentFrom(context, account);

		}

		return null;
	}

	protected Result paymentTermRequirement(Context context, ResultList list,
			Object selection) {
		Object payamentObj = context.getSelection(PAYMENT_TERMS);
		if (payamentObj instanceof ActionNames) {
			payamentObj = null;
			selection = "Payment Terms";
		}
		Requirement paymentReq = get("paymentTerms");
		PaymentTerms paymentTerm = (PaymentTerms) paymentReq.getValue();

		if (payamentObj != null) {
			paymentTerm = (PaymentTerms) payamentObj;
			paymentReq.setValue(paymentTerm);
		}

		if (selection != null)
			if (selection == "Payment Terms") {
				context.setAttribute(INPUT_ATTR, PAYMENT_TERMS);
				return paymentTerms(context, paymentTerm);

			}

		Record paymentTermRecord = new Record("Payment Terms");
		paymentTermRecord.add("Name", "Payment Terms");
		paymentTermRecord.add("Value",
				paymentTerm == null ? "" : paymentTerm.getName());

		list.add(paymentTermRecord);
		return null;
	}

	protected Result billToRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("address");
		Address billTo = (Address) req.getValue();

		// String attribute = (String) context.getAttribute(INPUT_ATTR);
		// if (attribute.equals("billTo")) {
		// Address input = context.getSelection("address");
		// if (input == null) {
		// input = context.getAddress();
		// }
		// billTo = input;
		// req.setValue(billTo);
		// }
		if (selection != null)
			if (selection == "Bill To") {
				context.setAttribute(INPUT_ATTR, "billTo");
				return address(context, "Bill To", "address", billTo);
			}
		billTo = req.getValue();
		Record billToRecord = new Record("Bill To");
		billToRecord.add("Name", "Bill To");
		billToRecord.add("Value", billTo == null ? "" : billTo.toString());
		list.add(billToRecord);
		return null;
	}

	protected Result contactRequirement(Context context, ResultList list,
			Object selection, Payee payee) {
		Object contactObj = context.getSelection(CONTACTS);
		Requirement contactReq = get("contact");
		Contact contact = (Contact) contactReq.getValue();
		if (contactObj != null) {
			contact = (Contact) contactObj;
			contactReq.setValue(contact);
		}
		if (contact != null && contact.equals(selection)) {
			return contactList(context, payee, contact);
		}
		Record contactRecord = new Record(contact);
		contactRecord.add("", "Contact");
		if (contact != null && contact.getName() != null) {
			contactRecord.add("", contact.getName());
		} else {
			contactRecord.add("", "No contact");
		}
		list.add(contactRecord);
		return null;
	}

	protected Record creatItemRecord(Item item) {
		Record record = new Record(item);
		record.add("Item Name", "Item Name:");
		record.add("Item value", item.getName());
		if (item.getTaxCode() != null) {
			record.add("Tax Code", item.getTaxCode().getName());
		}
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

	protected Set<Item> getItems(Company company) {
		return company.getItems();

	}

	protected Set<Account> getAccounts(Company company) {
		return company.getAccounts();

	}

	protected List<Activity> getActivityList(Date fromDate, Date endDate) {
		return null;
	}

	protected List<Customer> getCustomers(Company company, Boolean isActive) {
		Set<Customer> customers = company.getCustomers();
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
		return result;
	}

	private Set<PaymentTerms> getPaymentTerms(Company company) {
		return company.getPaymentTerms();
	}

	protected Result paymentFrom(Context context, Account oldAccount) {
		List<Account> accounts = new ArrayList<Account>();
		Set<Account> allAccounts = getAccounts(context.getCompany());

		for (Account a : allAccounts) {
			if (Arrays.asList(Account.TYPE_BANK,
					Account.TYPE_OTHER_CURRENT_ASSET).contains(a.getType()))
				accounts.add(a);

		}

		Result result = context.makeResult();
		result.add("Select Account");

		ResultList list = new ResultList(PAY_FROM);
		int num = 0;
		if (oldAccount != null) {
			list.add(createAccountRecord(oldAccount));
			num++;
		}
		for (Account acc : accounts) {
			if (acc != oldAccount) {
				list.add(createAccountRecord(acc));
				num++;
			}
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Account");
		result.add(commandList);
		return result;

	}

	protected Result paymentTerms(Context context, PaymentTerms oldPaymentTerms) {
		ArrayList<PaymentTerms> paymentTerms = new ArrayList<PaymentTerms>(
				context.getCompany().getPaymentTerms());
		Result result = context.makeResult();
		result.add("Select PaymentTerms");

		ResultList list = new ResultList(PAYMENT_TERMS);
		if (oldPaymentTerms != null) {
			list.add(createPaymentTermRecord(oldPaymentTerms));
		}

		ActionNames selection = context.getSelection(PAYMENT_TERMS);

		List<Record> actions = new ArrayList<Record>();

		List<PaymentTerms> pagination = pagination(context, selection, actions,
				paymentTerms, new ArrayList<PaymentTerms>(),
				PAYMENTTERMS_TO_SHOW);

		for (PaymentTerms term : pagination) {
			list.add(createPaymentTermRecord(term));
		}

		for (Record record : actions) {
			list.add(record);
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
		if (oldContact != null && oldContact.getName() != null) {
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

	protected Result depositeOrTransferTo(Context context, String name) {
		Requirement transferedReq = get(name);
		Account account = context.getSelection(name);
		if (!transferedReq.isDone()) {
			if (account != null) {
				transferedReq.setValue(account);
			} else {
				return accounts(context, name, new ListFilter<Account>() {

					@Override
					public boolean filter(Account account) {
						return Arrays.asList(Account.TYPE_BANK,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET)
								.contains(account.getType());

					}
				});
			}
		}
		if (account != null) {
			transferedReq.setValue(account);

		}
		return null;
	}

	protected Result accounts(Context context, String name,
			ListFilter<Account> listFilter) {
		Result result = context.makeResult();
		ResultList list = new ResultList(name);

		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			list.add(createAccountRecord((Account) last));
			num++;
		}
		Set<Account> transferAccountList = getAccounts(context.getCompany(),
				listFilter);
		if (!transferAccountList.isEmpty())
			result.add("Select an Account.");
		if (!transferAccountList.isEmpty()) {
			for (Account account : transferAccountList) {
				if (account != last) {
					list.add(createAccountRecord(account));
					num++;
				}
				if (num == ACCOUNTS_TO_SHOW) {
					break;
				}
			}
		}

		CommandList commands = new CommandList();
		commands.add("Create New Account");
		if (list.size() > 5) {
			Record moreAccounts = new Record("More Accounts");
			moreAccounts.add("", "MORE ACCOUNTS");
			list.add(moreAccounts);
		}
		result.add(list);
		result.add(commands);
		return result;
	}

	private Set<Account> getAccounts(Company company,
			ListFilter<Account> listFilter) {
		Set<Account> accounts = getAccounts(company);
		Set<Account> filtered = new HashSet<Account>();
		for (Account account : accounts) {
			if (listFilter.filter(account)) {
				filtered.add(account);
			}
		}
		return filtered;
	}

	protected Record createAccountRecord(Account last) {
		Record record = new Record(last);
		record.add("Account Number", "Account Number:");
		record.add("Account Number", last.getNumber());
		record.add("Account Name", "Account Name:");
		record.add("Account Name", last.getName());
		record.add("Account Type", "Account Type:");
		record.add("Account Type", getAccountTypeString(last.getType()));
		record.add("Balance", "Balance:");
		record.add("Balance", last.getTotalBalance());
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
			Object selection) {
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
		cashSaleNoRec.add("", "Phone Number");
		cashSaleNoRec.add("", phoneNo);
		// cashSaleNoRec.add("Value", phoneNo);
		list.add(cashSaleNoRec);
		return null;
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

	protected Result bankAccountRequirement(Context context) {
		Requirement bankAccountReq = get("bankAccount");
		Account bankAccount = context.getSelection("bankAccounts");
		if (bankAccount != null) {
			bankAccountReq.setValue(bankAccount);
		}
		if (!bankAccountReq.isDone()) {
			return bankAccounts(context);
		}

		return null;
	}

	private Result bankAccounts(Context context) {
		Result result = context.makeResult();
		ResultList accountsList = new ResultList("bankAccounts");
		result.add("Bank Accounts List");
		int num = 0;
		Set<Account> accounts2 = context.getCompany().getAccounts();
		List<Account> accounts = new ArrayList<Account>(accounts2);
		for (Account account : accounts) {
			if (account.getType() == Account.TYPE_OTHER_CURRENT_ASSET) {
				accountsList.add(createAccountRecord(account));
				num++;
			}
			if (num == BANK_ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		int size = accountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Bank Account");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create Bank Account");

		result.add(message.toString());
		result.add(accountsList);
		result.add(commandList);

		return result;

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
		Set<Payee> payees = context.getCompany().getPayees();
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
		commandList.add("Create Customer");
		commandList.add("Create Supplier");

		result.add(message.toString());
		result.add(payeeList);
		result.add(commandList);
		result.add("Type of Payee");
		return result;
	}

	protected Record createPayeeRecord(Payee payee) {
		Record record = new Record(payee);
		record.add("Name", payee.getName());
		record.add("Balance", payee.getBalance());
		return record;
	}

	protected Record creatBankAccountRecord(Account last) {
		Record record = new Record(last);
		record.add("Bank Account Number", last.getNumber());
		record.add("Bank Account Name", last.getName());
		record.add("Bank Account Type", ClientAccount.TYPE_OTHER_CURRENT_ASSET);
		return record;
	}

	protected List<BankAccount> getBankAccounts(Company company) {

		List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		FinanceTool financeTool = new FinanceTool();
		ArrayList<Account> accountsListBySorted = null;// financeTool
		// .getAccountsListBySorted(company.getAccountingType(),
		// company.getID());
		for (Account a : accountsListBySorted) {
			if (a.getType() == Account.TYPE_BANK) {
				bankAccounts.add((BankAccount) a);
			}
		}

		return bankAccounts;
	}

	protected Result itemsAndAccountsRequirement(Context context,
			Result makeResult, ResultList actions,
			ListFilter<Account> accountFilter) {
		Result result = itemsRequirement(context, makeResult, actions);
		if (result != null) {
			return result;
		}
		result = accountsRequirement(context, makeResult, accountFilter,
				actions);
		if (result != null) {
			return result;
		}
		return null;
	}

	protected Result accountsRequirement(Context context, Result result,
			ListFilter<Account> listFilter, ResultList actions) {
		Requirement transItemsReq = get(ACCOUNTS);
		List<Account> accounts = context.getSelections(ACCOUNTS);

		if (accounts != null && accounts.size() > 0) {
			for (Account account : accounts) {
				TransactionItem transactionItem = new TransactionItem();
				transactionItem.setType(TransactionItem.TYPE_ACCOUNT);
				transactionItem.setAccount(account);
				List<TransactionItem> transactionItems = transItemsReq
						.getValue();
				if (transactionItems == null) {
					transactionItems = new ArrayList<TransactionItem>();
					transItemsReq.setValue(transactionItems);
				}
				transactionItems.add(transactionItem);
				if (transactionItem.getUnitPrice() == 0) {
					context.putSelection(ACCOUNT_ITEM_DETAILS, "amount");
					Result transactionItemResult = transactionAccountItem(
							context, transactionItem);
					if (transactionItemResult != null) {
						return transactionItemResult;
					}
				} else if (context.getCompany().getPreferences().isTrackTax()
						&& context.getCompany().getPreferences()
								.isTaxPerDetailLine()
						&& transactionItem.getTaxCode() == null) {
					context.putSelection(ACCOUNT_ITEM_DETAILS, "taxCode");
					Result transactionItemResult = transactionAccountItem(
							context, transactionItem);
					if (transactionItemResult != null) {
						return transactionItemResult;
					}
				}
			}
		}

		if (!transItemsReq.isDone()) {
			return accountItems(context, ACCOUNTS, listFilter);
		}

		Object selection = context.getSelection("accountItems");
		if (selection != null) {
			result = transactionAccountItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ACCOUNTS) {
			return accounts(context, "accounts", listFilter);
		}

		List<TransactionItem> accountTransItems = transItemsReq.getValue();
		ResultList accountItems = new ResultList("accountItems");
		result.add("Account Transaction Items:-");
		for (TransactionItem item : accountTransItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getAccount().getName());
			itemRec.add("Amount", item.getUnitPrice());
			itemRec.add("Discount", item.getDiscount());
			itemRec.add("Total", item.getLineTotal());
			accountItems.add(itemRec);
		}
		result.add(accountItems);

		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
		return null;
	}

	protected Result accountItems(Context context, String label,
			ListFilter<Account> listFilter) {
		Result result = context.makeResult();
		Set<Account> accounts2 = getAccounts(context.getCompany(), listFilter);
		List<Account> accounts = new ArrayList<Account>(accounts2);
		Collections.sort(accounts, new Comparator<Account>() {

			@Override
			public int compare(Account o1, Account o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		ResultList list = new ResultList(label);
		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			list.add(creatAccountRecord((Account) last));
			num++;
		}
		Requirement itemsReq = get(label);
		List<TransactionItem> transItems = itemsReq.getValue();
		if (transItems == null) {
			transItems = new ArrayList<TransactionItem>();
		}
		List<Account> availableAccounts = new ArrayList<Account>();
		for (TransactionItem transactionItem : transItems) {
			availableAccounts.add(transactionItem.getAccount());
		}
		for (Account account : accounts) {
			if (account != last || !availableAccounts.contains(account)) {
				list.add(creatAccountRecord(account));
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
			result.add("You don't have any Account.");
		}

		result.add(list);
		CommandList commands = new CommandList();
		commands.add("Add Account");
		result.add(commands);
		return result;
	}

	private Record creatAccountRecord(Account last) {
		Record record = new Record(last);
		record.add("Account Name", "Account Name:");
		record.add("Account Name value", last.getName());
		record.add("Account Balance", "Current Balance:");
		record.add("Current Balance", last.getCurrentBalance());
		record.add("Account Type", "Account Type:");
		record.add("Account Type Value", getAccountTypeString(last.getType()));
		return record;
	}

	protected double getTransactionTotal(List<TransactionItem> items,
			Company company) {

		int totaldiscount = 0;
		double totallinetotal = 0.0;
		double taxableTotal = 0.0;
		double totalVat = 0.0;
		double grandTotal = 0.0;
		double totalValue = 0.0;
		int accountType = company.getAccountingType();
		for (TransactionItem citem : items) {
			totaldiscount += citem.getDiscount();

			Double lineTotalAmt = citem.getLineTotal();
			totallinetotal += lineTotalAmt;

			if (citem != null && citem.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// citem.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				taxableTotal += lineTotalAmt;
			}

			citem.setVATfraction(getVATAmount(citem.getTaxCode(), citem,
					company));
			totalVat += citem.getVATfraction();
			// totalVat += citem.getVATfraction();
		}

		if (company.getPreferences().isChargeSalesTax()) {
			grandTotal = totalVat + totallinetotal;
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}
		if (company.getPreferences().isRegisteredForVAT()) {
			// if (transactionView.vatinclusiveCheck != null
			// && (Boolean) transactionView.vatinclusiveCheck.getValue()) {
			// grandTotal = totallinetotal - totalVat;
			// setTotalValue(totallinetotal);
			//
			// } else {
			grandTotal = totallinetotal;
			totalValue = grandTotal + totalVat;
			// }
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}
		return totallinetotal;
	}

	public double getTaxTotal(List<TransactionItem> items, Company company,
			TAXCode taxCode) {

		int totaldiscount = 0;
		double totallinetotal = 0.0;
		double taxableTotal = 0.0;
		double totalVat = 0.0;
		double grandTotal = 0.0;
		double totalValue = 0.0;
		int accountType = company.getAccountingType();

		for (TransactionItem citem : items) {
			totaldiscount += citem.getDiscount();

			Double lineTotalAmt = citem.getLineTotal();
			totallinetotal += lineTotalAmt;

			if (citem != null && citem.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// citem.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				taxableTotal += lineTotalAmt;
			}

			citem.setVATfraction(getVATAmount(citem.getTaxCode(), citem,
					company));
			totalVat += citem.getVATfraction();
			// totalVat += citem.getVATfraction();
		}

		return taxableTotal;
	}

	public double getNetTotal(List<TransactionItem> items, Company company) {

		int totaldiscount = 0;
		double totallinetotal = 0.0;
		double taxableTotal = 0.0;
		double totalVat = 0.0;
		double grandTotal = 0.0;
		double totalValue = 0.0;
		int accountType = company.getAccountingType();
		for (TransactionItem citem : items) {
			totaldiscount += citem.getDiscount();

			Double lineTotalAmt = citem.getLineTotal();
			totallinetotal += lineTotalAmt;

			if (citem != null && citem.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// citem.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				taxableTotal += lineTotalAmt;
			}

			citem.setVATfraction(getVATAmount(citem.getTaxCode(), citem,
					company));
			totalVat += citem.getVATfraction();
			// totalVat += citem.getVATfraction();
		}

		if (company.getPreferences().isChargeSalesTax()) {
			grandTotal = totalVat + totallinetotal;
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}
		if (company.getPreferences().isRegisteredForVAT()) {
			// if (transactionView.vatinclusiveCheck != null
			// && (Boolean) transactionView.vatinclusiveCheck.getValue()) {
			// grandTotal = totallinetotal - totalVat;
			// setTotalValue(totallinetotal);
			//
			// } else {
			grandTotal = totallinetotal;
			totalValue = grandTotal + totalVat;
			// }
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}
		return totallinetotal;
	}

	public double getVATAmount(TAXCode taxCode, TransactionItem record,
			Company company) {

		double vatRate = 0.0;
		if (taxCode != null) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise 'GroupRate
			try {

				TAXItemGroup item = taxCode.getTAXItemGrpForSales();
				if (item == null) {
					vatRate = 0.0;
				} else if (item instanceof TAXItem) {
					// The selected one is VATItem,so get 'VATRate' from
					// 'VATItem'
					vatRate = ((TAXItem) item).getTaxRate();
				} else {
					// The selected one is VATGroup,so get 'GroupRate' from
					// 'VATGroup'
					vatRate = ((TAXGroup) item).getGroupRate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Double vat = 0.0;
		vat = record.getLineTotal() * vatRate / 100;
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	protected Result vendorRequirement(Context context) {
		Requirement vendReq = get(VENDOR);
		Vendor vendor = context.getSelection("suppliers");
		if (vendor != null) {
			vendReq.setValue(vendor);
		}
		if (!vendReq.isDone()) {
			return vendors(context);
		}
		return null;
	}

	protected List<Account> getAccounts(Boolean isActive, Company company) {
		FinanceTool financeTool = new FinanceTool();
		List<Account> accounts = new ArrayList<Account>();
		List<Account> allaccounts = null;// financeTool.getCompanyManager().getAccountsListBySorted(
		// company.getAccountingType(), company.getID());
		for (Account acc : allaccounts) {
			if (isActive) {
				if (acc.getIsActive()) {
					accounts.add(acc);
				}
			} else {
				accounts.add(acc);
			}
		}

		return accounts;

	}

	protected Result chequeNoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement requirement = get("Payment method");
		if (requirement != null) {
			String paymentMethod = (String) requirement.getValue();
			if (paymentMethod.equals(US_CHECK)
					|| paymentMethod.equals(UK_CHECK)) {
				Requirement req = get("chequeNo");
				String invoiceNo = (String) req.getValue();

				String attribute = (String) context.getAttribute(INPUT_ATTR);
				if (attribute.equals("chequeNo")) {
					String order = context.getSelection(NUMBER);
					if (order == null) {
						order = context.getInteger().toString();
					}
					invoiceNo = order;
					req.setValue(invoiceNo);
				}

				if (selection == invoiceNo) {
					context.setAttribute(INPUT_ATTR, "chequeNo");
					return number(context, "Enter Cheque number", invoiceNo);
				}

				Record invoiceNoRec = new Record(invoiceNo);
				invoiceNoRec.add("Name", "Cheque Number");
				invoiceNoRec.add("Value", invoiceNo);
				list.add(invoiceNoRec);
			}
		}
		return null;
	}

	protected List<BillsList> getExpenses(String viewType, Company company) {

		ArrayList<BillsList> billsList = null;
		try {
			billsList = new FinanceTool().getVendorManager().getBillsList(true,
					company.getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return filterList(viewType, billsList);
	}

	protected List<BillsList> getBills(String viewType, Company company) {
		ArrayList<BillsList> billsList = null;

		try {
			billsList = new FinanceTool().getVendorManager().getBillsList(
					false, company.getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return filterList(viewType, billsList);
	}

	private List<BillsList> filterList(String text,
			List<BillsList> initialRecords) {

		if (text.equalsIgnoreCase(Accounter.getFinanceConstants().open())) {
			ArrayList<BillsList> openRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if ((rec.getType() == com.vimukti.accounter.core.Transaction.TYPE_CREDIT_CARD_EXPENSE
						|| rec.getType() == com.vimukti.accounter.core.Transaction.TYPE_CASH_EXPENSE || rec
						.getType() == com.vimukti.accounter.core.Transaction.TYPE_EMPLOYEE_EXPENSE)
						|| ((rec.getType() == com.vimukti.accounter.core.Transaction.TYPE_ENTER_BILL || rec
								.getType() == com.vimukti.accounter.core.Transaction.TYPE_VENDOR_CREDIT_MEMO) && DecimalUtil
								.isGreaterThan(rec.getBalance(), 0))) {
					if (!rec.isDeleted() && !rec.isVoided())
						openRecs.add(rec);

				}
			}
			return openRecs;

		} else if (text.equalsIgnoreCase(Accounter.getFinanceConstants()
				.voided())) {
			ArrayList<BillsList> voidedRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.isVoided() && !rec.isDeleted()) {
					voidedRecs.add(rec);

				}
			}
			return voidedRecs;

		} else if (text.equalsIgnoreCase(Accounter.getFinanceConstants()
				.overDue())) {
			ArrayList<BillsList> overDueRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.getType() == com.vimukti.accounter.core.Transaction.TYPE_ENTER_BILL
						&& new ClientFinanceDate().after(rec.getDueDate())
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					overDueRecs.add(rec);
				}
			}

		}

		if (text.equalsIgnoreCase(Accounter.getFinanceConstants().all())) {
			ArrayList<BillsList> list = new ArrayList<BillsList>();
			list.addAll(initialRecords);
			return list;
		}
		return initialRecords;

	}

	protected Result preferredShippingMethodRequirement(Context context,
			ResultList list, Object selection) {
		Object shippingObj = context.getSelection("Preferred Shipping Method");
		if (shippingObj instanceof ActionNames) {
			shippingObj = null;
			selection = "Preferred Shipping Method";
		}
		Requirement shipreq = get("Preferred Shipping Method");
		ShippingMethod shippingTerm = (ShippingMethod) shipreq.getValue();

		if (shippingObj != null) {
			shippingTerm = (ShippingMethod) shippingObj;
			shipreq.setValue(shippingTerm);
		}
		if (selection != null)
			if (selection == "Preferred Shipping Method") {
				context.setAttribute(INPUT_ATTR, "Preferred Shipping Method");
				return shippingMethod(context, shippingTerm);

			}

		Record shippingMethodRecord = new Record("Preferred Shipping Method");
		shippingMethodRecord.add("Name", "Preferred Shipping Method");
		shippingMethodRecord.add("Value", shippingTerm == null ? ""
				: shippingTerm.getName() + "-" + shippingTerm.getDescription());
		list.add(shippingMethodRecord);

		return null;
	}

	protected Result shippingMethod(Context context,
			ShippingMethod shippingmethod) {
		ArrayList<ShippingMethod> shippingMethods = new ArrayList<ShippingMethod>(
				getShippingMethods(context.getCompany()));
		Result result = context.makeResult();
		result.add("Select Shipping Methods");

		ResultList list = new ResultList("Preferred Shipping Method");
		if (shippingmethod != null) {
			list.add(createShippingMethodRecord(shippingmethod));
		}

		ActionNames selection = context
				.getSelection("Preferred Shipping Method");

		List<Record> actions = new ArrayList<Record>();

		List<ShippingMethod> pagination = pagination(context, selection,
				actions, shippingMethods, new ArrayList<ShippingMethod>(),
				SHIPPING_TERMS_TO_SHOW);

		for (ShippingMethod term : pagination) {
			list.add(createShippingMethodRecord(term));
		}

		for (Record record : actions) {
			list.add(record);
		}

		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Preferred Shipping Method");
		result.add(commandList);
		return result;
	}

	private Set<ShippingMethod> getShippingMethods(Company company) {
		return company.getShippingMethods();
	}

	protected Record createShippingMethodRecord(ShippingMethod shippingmethod) {
		Record record = new Record(shippingmethod);
		record.add("Name", "Preferred Shipping Method");
		record.add("Value",
				shippingmethod == null ? "" : shippingmethod.getName() + "-"
						+ shippingmethod.getDescription());
		return record;
	}

	protected List<Estimate> getEstimates(String viewType, Company company) {

		List<Estimate> result = new ArrayList<Estimate>();
		List<Estimate> data = null;
		try {
			data = new FinanceTool().getCustomerManager().getEstimates(
					company.getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		for (Estimate e : data) {
			if (viewType.equals(Estimate.STATUS_OPEN)) {
				if (e.getStatus() == Estimate.STATUS_OPEN)
					result.add(e);

			} else if (viewType.equals(Estimate.STATUS_ACCECPTED)) {
				result.add(e);
			} else if (viewType.equals(Estimate.STATUS_REJECTED)) {
				result.add(e);
			} else {
				result.add(e);
			}
		}

		return result;
	}

	protected List<InvoicesList> getInvoices(String viewType, Company company) {

		ArrayList<InvoicesList> invoiceList = null;
		try {
			invoiceList = new FinanceTool().getInventoryManager()
					.getInvoiceList(company.getID());
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return invoiceList;

	}

	protected List<ReceivePaymentsList> getReceivePaymentsList(String viewType,
			Company company) {

		ArrayList<ReceivePaymentsList> invoiceList = null;
		try {
			invoiceList = new FinanceTool().getCustomerManager()
					.getReceivePaymentsList(company.getID());
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return invoiceList;

	}

	protected List<PaymentsList> getPayments(int type, Company company) {

		ArrayList<PaymentsList> paymentsList = null;
		ArrayList<PaymentsList> result = new ArrayList<PaymentsList>();
		try {
			paymentsList = new FinanceTool().getCustomerManager()
					.getPaymentsList(company.getID());
		} catch (DAOException e) {

			e.printStackTrace();
		}
		for (PaymentsList p : paymentsList) {
			if (p.getType() == type) {
				result.add(p);
			}
		}
		return result;

	}

	protected List<TransactionIssuePayment> getIssuePaymentTransactionsList(
			String paymentMethod, String accountName, Company company) {
		return null;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	protected Result taxCodeRequirement(Context context, ResultList list) {
		Requirement taxCodeRequirement = get(TAXCODE);
		TAXCode taxCode = context.getSelection(TAXCODE);
		if (taxCode != null) {
			taxCodeRequirement.setValue(taxCode);
		}
		if (!taxCodeRequirement.isDone()) {
			return taxCode(context, null);
		}

		TAXCode value = taxCodeRequirement.getValue();
		Object selection = context.getSelection("values");
		if (value == selection) {
			return taxCode(context, taxCode);
		}
		Record taxcodeRecord = new Record(value);
		taxcodeRecord.add("", "Tax Code Name");
		taxcodeRecord.add("", value.getName());
		taxcodeRecord.add("", "Purchase Tax Rate");
		taxcodeRecord.add("", value.getPurchaseTaxRate());
		list.add(taxcodeRecord);
		return null;
	}

}
