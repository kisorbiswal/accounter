package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class AbstractTransactionCommand extends AbstractCommand {
	protected static final int ITEMS_TO_SHOW = 5;
	protected static final int CUSTOMERS_TO_SHOW = 5;
	private static final int PAYMENTTERMS_TO_SHOW = 3;
	private static final int CONTACTS_TO_SHOW = 5;
	protected static final String PAYMENT_TERMS = "paymentTerms";
	protected static final int ACCOUNTS_TO_SHOW = 5;
	protected static final String TRANSACTION_ITEM_PROCESS = "transactionitemprocess";
	protected static final String OLD_TRANSACTION_ITEM_ATTR = "oldtransactionitemattr";
	protected static final String ITEM_DETAILS = "itemdetails";
	protected static final String ITEM_PROPERTY_ATTR = "itempropertyattr";
	protected static final int BANK_ACCOUNTS_TO_SHOW = 5;
	protected static final int EXPENSES_TO_SHOW = 5;
	protected static final int SHIPPING_TERMS_TO_SHOW = 5;
	protected static final int BILLS_TO_SHOW = 5;
	protected static final int ESTIMATES_TO_SHOW = 5;
	protected static final int INVOICES_TO_SHOW = 5;
	protected static final int ESTIMATESANDSALESORDERS_TO_SHOW = 5;

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
	protected static final String BILL_TO = "address";
	protected static final String ITEMS = "items";
	protected static final int ENTERBILL_TRANSACTION = 3;
	protected static final String SUPPLIER = "supplier";
	protected static final String CUSTOMER = "customer";
	protected static final String CONTACT = "contact";
	protected static final String PHONE = "phone";

	protected static final String OPEN = "Open";
	protected static final String ALL = "All";
	protected static final String VOIDED = "Voided";
	protected static final String OVER_DUE = "Over-Due";
	protected static final String REJECTED = "Rejected";
	protected static final String ACCEPTED = "Accepted";
	protected static final String EXPIRED = "Expired";
	protected static final String ISSUED = "Issued";
	protected static final String NOT_ISSUED = "Not issued";
	private static final Object UNIT_PRICE = "unitPrice";
	private static final String TRANSACTION_ITEMS = "transactionItems";
	private static final String QUANTITY = "quantity";
	private static final String DISCOUNT = "discount";
	private static final String DESCRIPTION = "description";
	private static final String TAX = "tax";
	private static final String AMOUNT = "amount";
	private static final String SHIP_TO = "shipTo";
	private static final String CUSTOMERS = "customers";
	private static final String VALUES = "values";
	protected static final String CASH = "Cash";
	protected static final String CREDIT_CARD = "Credit Card";

	protected static final String PREFERED_SHIPMETHOD = "Preferred Shipping Method";
	private static final String PAYEE = "payee";
	private static final String ACCOUNT_ITEMS = "accountItems";
	private int transactionType;

	protected Result itemsRequirement(Context context, Result result,
			ResultList actions, boolean isSales) {
		Requirement transItemsReq = get(ITEMS);
		List<ClientItem> items = context.getSelections(ITEMS);
		if (items != null && items.size() > 0) {
			for (ClientItem item : items) {
				ClientTransactionItem transactionItem = new ClientTransactionItem();
				transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
				transactionItem.setItem(item.getID());
				if (isSales) {
					transactionItem.setUnitPrice(item.getSalesPrice());
				} else {
					transactionItem.setUnitPrice(item.getPurchasePrice());
				}
				ClientQuantity quantity = new ClientQuantity();
				quantity.setValue(1);
				transactionItem.setQuantity(quantity);

				List<ClientTransactionItem> transactionItems = transItemsReq
						.getValue();
				if (transactionItems == null) {
					transactionItems = new ArrayList<ClientTransactionItem>();
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
					context.putSelection(ITEM_DETAILS, UNIT_PRICE);
					Result transactionItemResult = transactionItem(context,
							transactionItem);
					if (transactionItemResult != null) {
						return transactionItemResult;
					}
				} else if (context.getCompany().getPreferences().isTrackTax()
						&& context.getCompany().getPreferences()
								.isTaxPerDetailLine()
						&& transactionItem.getTaxCode() == 0) {
					context.putSelection(ITEM_DETAILS, TAXCODE);
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

		Object selection = context.getSelection(TRANSACTION_ITEMS);
		if (selection != null) {
			result = transactionItem(context, (ClientTransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ITEMS) {
			return items(context);
		}

		result.add(getConstants().items());
		ResultList itemsList = new ResultList(TRANSACTION_ITEMS);
		List<ClientTransactionItem> transItems = transItemsReq.getValue();
		for (ClientTransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("", getClientCompany().getItem(item.getItem())
					.getName());
			itemRec.add("", item.getLineTotal());
			itemRec.add("", item.getVATfraction());
			itemsList.add(itemRec);
		}
		result.add(itemsList);

		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", getMessages().addMore(getConstants().items()));
		actions.add(moreItems);
		return null;
	}

	protected Result transactionItemProcess(Context context) {
		ClientTransactionItem transactionItem = (ClientTransactionItem) context
				.getAttribute(OLD_TRANSACTION_ITEM_ATTR);
		Result result = transactionItem(context, transactionItem);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement itemsReq = get(ITEMS);
				List<ClientTransactionItem> transItems = itemsReq.getValue();
				transItems.remove(transactionItem);
				context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
			}
		}
		return result;
	}

	protected Result transactionAccountProcess(Context context) {

		ClientTransactionItem transactionItem = (ClientTransactionItem) context
				.getAttribute(OLD_TRANSACTION_ACCOUNT_ITEM_ATTR);
		Result result = transactionAccountItem(context, transactionItem);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement itemsReq = get(ACCOUNTS);
				List<ClientTransactionItem> transItems = itemsReq.getValue();
				transItems.remove(transactionItem);
				context.removeAttribute(OLD_TRANSACTION_ACCOUNT_ITEM_ATTR);
			}
		}
		return result;

	}

	protected Result transactionItem(Context context,
			ClientTransactionItem transactionItem) {
		context.setAttribute(PROCESS_ATTR, TRANSACTION_ITEM_PROCESS);
		context.setAttribute(OLD_TRANSACTION_ITEM_ATTR, transactionItem);

		String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ITEM_PROPERTY_ATTR);
			if (lineAttr.equals(QUANTITY)) {
				if (context.getDouble() != null) {
					transactionItem.getQuantity().setValue(context.getDouble());
				} else {
					transactionItem.getQuantity().setValue(
							context.getInteger().doubleValue());
				}
			} else if (lineAttr.equals(UNIT_PRICE)) {
				if (context.getDouble() != null) {
					transactionItem.setUnitPrice(context.getDouble());
				} else {
					transactionItem.setUnitPrice(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals(DISCOUNT)) {
				if (context.getDouble() != null) {
					transactionItem.setDiscount(context.getDouble());
				} else {
					transactionItem.setDiscount(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals(TAXCODE)) {
				ClientTAXCode taxCode = context.getSelection(TAXCODE);
				transactionItem.setTaxCode(taxCode.getID());
			} else if (lineAttr.equals(DESCRIPTION)) {
				transactionItem.setDescription(context.getString());
			}
		} else {
			Object selection = context.getSelection(ITEM_DETAILS);
			if (selection != null) {
				if (selection.equals(QUANTITY)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, QUANTITY);
					return amount(context,
							getMessages()
									.pleaseEnter(getConstants().quantity()),
							transactionItem.getQuantity().getValue());
				} else if (selection.equals(UNIT_PRICE)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, UNIT_PRICE);
					return amount(
							context,
							getMessages().pleaseEnter(
									getConstants().unitPrice()),
							transactionItem.getUnitPrice());
				} else if (selection.equals(DISCOUNT)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, DISCOUNT);
					return amount(context,
							getMessages()
									.pleaseEnter(getConstants().discount()),
							transactionItem.getDiscount());
				} else if (selection.equals(TAXCODE)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, TAXCODE);
					return taxCode(
							context,
							getClientCompany().getTAXCode(
									transactionItem.getTaxCode()));
				} else if (selection.equals(TAX)) {
					transactionItem.setTaxable(!transactionItem.isTaxable());
				} else if (selection.equals(DESCRIPTION)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, DESCRIPTION);
					return number(
							context,
							getMessages().pleaseEnter(
									getConstants().description()),
							transactionItem.getDescription());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					if (transactionItem.getUnitPrice() == 0) {
						context.setAttribute(ITEM_PROPERTY_ATTR, UNIT_PRICE);
						return amount(
								context,
								getMessages().pleaseEnter(
										getConstants().unitPrice()),
								transactionItem.getUnitPrice());
					} else if (context.getCompany().getPreferences()
							.isTrackTax()
							&& context.getCompany().getPreferences()
									.isTaxPerDetailLine()
							&& transactionItem.getTaxCode() == 0) {
						context.setAttribute(ITEM_PROPERTY_ATTR, TAXCODE);
						return taxCode(
								context,
								getClientCompany().getTAXCode(
										transactionItem.getTaxCode()));
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
		Record record = new Record(QUANTITY);
		record.add("", getConstants().quantity());
		record.add("", transactionItem.getQuantity());
		list.add(record);

		record = new Record(UNIT_PRICE);
		record.add("", getConstants().unitPrice());
		record.add("", transactionItem.getUnitPrice());
		list.add(record);

		record = new Record(DISCOUNT);
		record.add("", getConstants().discount());
		record.add("", transactionItem.getDiscount());
		list.add(record);

		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			record = new Record(TAXCODE);
			record.add("", getConstants().taxCode());
			if (transactionItem.getTaxCode() != 0) {
				record.add(
						"",
						getClientCompany().getTAXCode(
								transactionItem.getTaxCode()).getName());
			} else {
				record.add("", "");
			}
			list.add(record);
		} else {
			record = new Record(TAX);
			record.add("", getConstants().isTaxable());
			if (transactionItem.isTaxable()) {
				record.add("", getConstants().taxable());
			} else {
				record.add("", getConstants().taxExempt());
			}
			list.add(record);
		}

		record = new Record(DESCRIPTION);
		record.add("", getConstants().description());
		record.add("", transactionItem.getDescription());
		list.add(record);

		Result result = context.makeResult();
		result.add(getMessages().details(getConstants().item()));
		result.add(getConstants().name()
				+ getClientCompany().getItem(transactionItem.getItem())
						.getName());
		result.add(list);
		if (getClientCompany().getPreferences().isTaxPerDetailLine()) {
			result.add(getConstants().vat() + transactionItem.getVATfraction());
		}
		double lt = transactionItem.getQuantity().getValue()
				* transactionItem.getUnitPrice();
		double disc = transactionItem.getDiscount();
		transactionItem
				.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
		result.add(getConstants().total() + transactionItem.getLineTotal());

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", getConstants().delete());
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", getConstants().finish());
		actions.add(record);
		result.add(actions);
		return result;
	}

	protected Result transactionAccountItem(Context context,
			ClientTransactionItem transactionItem) {
		context.setAttribute(PROCESS_ATTR, TRANSACTION_ACCOUNT_ITEM_PROCESS);
		context.setAttribute(OLD_TRANSACTION_ACCOUNT_ITEM_ATTR, transactionItem);

		String lineAttr = (String) context
				.getAttribute(ACCOUNT_ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ACCOUNT_ITEM_PROPERTY_ATTR);
			if (lineAttr.equals(AMOUNT)) {
				if (context.getDouble() != null) {
					transactionItem.setUnitPrice(context.getDouble());
				} else {
					transactionItem.setUnitPrice(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals(DISCOUNT)) {
				if (context.getDouble() != null) {
					transactionItem.setDiscount(context.getDouble());
				} else {
					transactionItem.setDiscount(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals(TAXCODE)) {
				ClientTAXCode taxCode = context.getSelection(TAXCODE);
				transactionItem.setTaxCode(taxCode.getID());
			} else if (lineAttr.equals(DESCRIPTION)) {
				transactionItem.setDescription(context.getString());
			}
		} else {
			Object selection = context.getSelection(ACCOUNT_ITEM_DETAILS);
			if (selection != null) {
				if (selection.equals(AMOUNT)) {
					context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR, AMOUNT);
					return amount(context,
							getMessages().pleaseEnter(getConstants().amount()),
							transactionItem.getUnitPrice());
				} else if (selection.equals(DISCOUNT)) {
					context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR, DISCOUNT);
					return amount(context,
							getMessages()
									.pleaseEnter(getConstants().discount()),
							transactionItem.getDiscount());
				} else if (selection.equals(TAXCODE)) {
					context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR, TAXCODE);
					return taxCode(
							context,
							getClientCompany().getTAXCode(
									transactionItem.getTaxCode()));
				} else if (selection.equals(TAX)) {
					transactionItem.setTaxable(!transactionItem.isTaxable());
				} else if (selection.equals(DESCRIPTION)) {
					context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR,
							DESCRIPTION);
					return number(
							context,
							getMessages().pleaseEnter(
									getConstants().description()),
							transactionItem.getDescription());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					if (transactionItem.getUnitPrice() == 0) {
						context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR, AMOUNT);
						return amount(
								context,
								getMessages().pleaseEnter(
										getConstants().amount()),
								transactionItem.getUnitPrice());
					} else if (context.getCompany().getPreferences()
							.isTrackTax()
							&& context.getCompany().getPreferences()
									.isTaxPerDetailLine()
							&& transactionItem.getTaxCode() == 0) {
						context.setAttribute(ACCOUNT_ITEM_PROPERTY_ATTR,
								TAXCODE);
						return taxCode(
								context,
								getClientCompany().getTAXCode(
										transactionItem.getTaxCode()));
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
		Record record = new Record(AMOUNT);
		record.add("", getConstants().amount());
		record.add("", transactionItem.getUnitPrice());
		list.add(record);

		record = new Record(DISCOUNT);
		record.add("", getConstants().discount());
		record.add("", transactionItem.getDiscount());
		list.add(record);
		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			record = new Record(TAXCODE);
			record.add("", getConstants().taxCode());
			if (transactionItem.getTaxCode() != 0) {
				ClientTAXCode code = getClientCompany().getTAXCode(
						transactionItem.getTaxCode());
				if (code != null) {
					record.add("", code.getName());
				}
			} else {
				record.add("", "");
			}
			list.add(record);
		} else {
			record = new Record(TAX);
			record.add("", getConstants().tax());
			if (transactionItem.isTaxable()) {
				record.add("", getConstants().taxable());
			} else {
				record.add("", getConstants().nonTaxable());
			}
			list.add(record);
		}

		record = new Record(DESCRIPTION);
		record.add("", getConstants().description());
		record.add("", transactionItem.getDescription());
		list.add(record);

		// TODO NEED TO CALCULATE LINE TOTAL
		double lineTotal = transactionItem.getUnitPrice()
				- ((transactionItem.getUnitPrice() * transactionItem
						.getDiscount()) / 100);
		transactionItem.setLineTotal(lineTotal);
		Result result = context.makeResult();
		result.add(getMessages().details(Global.get().Account()));
		ClientAccount account = getClientCompany().getAccount(
				transactionItem.getAccount());
		if (account != null) {
			result.add(getMessages().accountName(Global.get().Account())
					+ account.getName());
		}
		double lt = transactionItem.getUnitPrice();
		double disc = transactionItem.getDiscount();
		transactionItem
				.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
		result.add(getConstants().total() + transactionItem.getLineTotal());
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", getConstants().delete());
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", getConstants().finish());
		actions.add(record);
		result.add(actions);
		return result;
	}

	protected Result shipToRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(SHIP_TO);
		ClientAddress shipTo = (ClientAddress) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(SHIP_TO)) {
			ClientAddress input = context.getSelection(ADDRESS);
			if (input == null) {
				input = context.getAddress();
			}
			shipTo = input;
			req.setValue(shipTo);
		}
		if (selection != null)
			if (selection == SHIP_TO) {
				context.setAttribute(INPUT_ATTR, SHIP_TO);
				return address(context, getConstants().shipTo(), SHIP_TO,
						shipTo);
			}
		shipTo = req.getValue();

		Record shipToRecord = new Record(SHIP_TO);
		shipToRecord.add("", getConstants().shipTo());
		shipToRecord.add("", shipTo == null ? "" : shipTo.toString());

		list.add(shipToRecord);
		return null;
	}

	protected Result items(Context context) {
		Result result = context.makeResult();
		List<ClientItem> items = getItems();
		ResultList list = new ResultList(ITEMS);
		ClientItem last = (ClientItem) context.getLast(RequirementType.ITEM);
		int num = 0;
		if (last != null) {
			list.add(creatItemRecord(last));
			num++;
		}
		Requirement itemsReq = get(ITEMS);
		List<ClientTransactionItem> transItems = itemsReq.getValue();
		if (transItems == null) {
			transItems = new ArrayList<ClientTransactionItem>();
		}
		List<Long> availableItems = new ArrayList<Long>();
		for (ClientTransactionItem transactionItem : transItems) {
			availableItems.add(transactionItem.getItem());
		}
		for (ClientItem item : items) {
			if (item != last && !availableItems.contains(item.getID())) {
				list.add(creatItemRecord(item));
				num++;
			}
			if (num == ITEMS_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add(getMessages().pleaseSelect(getConstants().item()));
		}
		result.add(list);
		CommandList commands = new CommandList();
		commands.add(getMessages().create(getConstants().item()));
		result.add(commands);
		return result;
	}

	private Record creatItemRecord(ClientItem item) {
		Record record = new Record(item);
		record.add("", getConstants().itemName());
		record.add("", item.getName());
		ClientTAXCode taxCode = getClientCompany()
				.getTAXCode(item.getTaxCode());
		if (taxCode != null) {
			record.add("", taxCode.getName());
		}
		return record;
	}

	protected Result customerRequirement(Context context, ResultList list,
			String requirementName, String name) {
		Requirement customerReq = get(requirementName);
		ClientCustomer customer = context.getSelection(CUSTOMERS);
		if (customer != null) {
			customerReq.setValue(customer);
		}

		ClientCustomer value = customerReq.getValue();
		Object selection = context.getSelection(VALUES);
		if (!customerReq.isDone() || (value == selection)) {
			return customers(context);
		}

		Record customerRecord = new Record(value);
		customerRecord.add("", name);
		customerRecord.add("", value.getName());
		list.add(customerRecord);
		return null;
	}

	protected Result customers(Context context) {

		Result result = context.makeResult();

		ResultList customerList = new ResultList(CUSTOMERS);

		Object last = context.getLast(RequirementType.CUSTOMER);
		List<ClientCustomer> skipCustomers = new ArrayList<ClientCustomer>();
		if (last != null) {
			customerList.add(createCustomerRecord((ClientCustomer) last));
			skipCustomers.add((ClientCustomer) last);
		}
		List<ClientCustomer> customers = getClientCompany().getCustomers();

		ResultList actions = new ResultList(ACTIONS);

		ActionNames selection = context.getSelection(ACTIONS);

		List<ClientCustomer> pagination = pagination(context, selection,
				actions, customers, skipCustomers, CUSTOMERS_TO_SHOW);

		for (ClientCustomer vendor : pagination) {
			customerList.add(createCustomerRecord(vendor));
		}

		int size = customerList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getMessages().pleaseSelect(Global.get().Customer()));
		}
		CommandList commandList = new CommandList();
		commandList.add(getMessages().create(Global.get().Customer()));

		result.add(message.toString());
		result.add(customerList);
		result.add(actions);
		result.add(commandList);
		return result;
	}

	protected Result paymentTermRequirement(Context context, ResultList list,
			Object selection) {
		Object payamentObj = context.getSelection(PAYMENT_TERMS);
		if (payamentObj instanceof ActionNames) {
			payamentObj = null;
			selection = PAYMENT_TERMS;
		}
		Requirement paymentReq = get(PAYMENT_TERMS);
		ClientPaymentTerms paymentTerm = (ClientPaymentTerms) paymentReq
				.getValue();

		if (payamentObj != null) {
			paymentTerm = (ClientPaymentTerms) payamentObj;
			paymentReq.setValue(paymentTerm);
		}

		if (selection != null)
			if (selection.equals(PAYMENT_TERMS)) {
				context.setAttribute(INPUT_ATTR, PAYMENT_TERMS);
				return paymentTerms(context, paymentTerm);

			}

		Record paymentTermRecord = new Record(PAYMENT_TERMS);
		paymentTermRecord.add("", getConstants().paymentTerms());
		paymentTermRecord.add("",
				paymentTerm == null ? "" : paymentTerm.getName());

		list.add(paymentTermRecord);
		return null;
	}

	protected Result addressOptionalRequirement(Context context,
			ResultList list, Object selection, String reqName,
			String displayName, String name) {
		Requirement req = get(reqName);
		ClientAddress billTo = req.getValue();
		if (selection != null)
			if (selection == reqName) {
				return address(context, displayName, reqName, billTo);
			}
		billTo = req.getValue();
		Record billToRecord = new Record(reqName);
		billToRecord.add("", name);
		billToRecord.add("", billTo.toString());
		list.add(billToRecord);
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @param reqName
	 * @param displayName
	 * @param name
	 * @return
	 */
	protected Result contactOptionalRequirement(Context context,
			ResultList list, Object selection, String reqName,
			String displayName, String name) {
		Requirement req = get(reqName);
		ClientContact contact = req.getValue();
		if (selection != null)
			if (selection == reqName) {
				return contact(context, displayName, reqName, contact);
			}
		contact = req.getValue();
		Record contactRecord = new Record(reqName);
		contactRecord.add("", name);
		contactRecord.add("", contact == null ? "" : contact.getName() + "-"
				+ contact.getTitle() + "-" + contact.getBusinessPhone() + "-"
				+ contact.getEmail());
		list.add(contactRecord);
		return null;
	}

	protected Result contactRequirement(Context context, ResultList list,
			Object selection, ClientPayee payee) {
		Object contactObj = context.getSelection(CONTACTS);
		Requirement contactReq = get(CONTACT);
		ClientContact contact = (ClientContact) contactReq.getValue();
		if (contactObj != null) {
			contact = (ClientContact) contactObj;
			contactReq.setValue(contact);
		}
		if (selection != null && selection.equals(CONTACT)) {
			return contactList(context, payee, contact);
		}
		Record contactRecord = new Record(CONTACT);
		contactRecord.add("", getConstants().contact());
		if (contact != null && contact.getName() != null) {
			contactRecord.add("", contact.getName());
		} else {
			contactRecord.add("", "No contact");
		}
		list.add(contactRecord);
		return null;
	}

	// protected Record createItemRecord(ClientItem item) {
	// Record record = new Record(item);
	// record.add("", getConstants().itemName());
	// record.add("", item.getName());
	// ClientTAXCode taxCode = getClientCompany()
	// .getTAXCode(item.getTaxCode());
	// if (taxCode != null) {
	// record.add("", taxCode.getName());
	// }
	// return record;
	// }

	protected Record createCustomerRecord(ClientCustomer customer) {
		Record record = new Record(customer);
		record.add("", customer.getName());
		record.add("", customer.getBalance());
		return record;
	}

	protected Record createContactRecord(ClientContact contact) {
		Record record = new Record(contact);
		record.add("", contact.getName());
		return record;
	}

	protected Record createPaymentTermRecord(ClientPaymentTerms paymentTerms) {
		Record record = new Record(paymentTerms);
		record.add("", paymentTerms.getName());
		record.add("", paymentTerms.getDescription());
		return record;
	}

	protected ArrayList<ClientItem> getItems() {
		return getClientCompany().getItems();

	}

	protected ArrayList<ClientAccount> getAccounts() {
		return getClientCompany().getAccounts();

	}

	protected List<ClientCustomer> getCustomers(Boolean isActive) {
		ArrayList<ClientCustomer> customers = getClientCompany().getCustomers();
		if (isActive == null) {
			return customers;
		}
		ArrayList<ClientCustomer> result = new ArrayList<ClientCustomer>();
		for (ClientCustomer customer : customers) {
			if (customer.isActive() == isActive) {
				result.add(customer);
			}
		}
		return result;
	}

	protected Result paymentTerms(Context context,
			ClientPaymentTerms oldPaymentTerms) {
		ArrayList<ClientPaymentTerms> paymentTerms = new ArrayList<ClientPaymentTerms>(
				getClientCompany().getPaymentsTerms());
		Result result = context.makeResult();
		result.add(getMessages().pleaseSelect(getConstants().paymentTerms()));

		ResultList list = new ResultList(PAYMENT_TERMS);
		if (oldPaymentTerms != null) {
			list.add(createPaymentTermRecord(oldPaymentTerms));
		}

		ActionNames selection = context.getSelection(PAYMENT_TERMS);

		List<Record> actions = new ArrayList<Record>();

		List<ClientPaymentTerms> pagination = pagination(context, selection,
				actions, paymentTerms, new ArrayList<ClientPaymentTerms>(),
				PAYMENTTERMS_TO_SHOW);

		for (ClientPaymentTerms term : pagination) {
			list.add(createPaymentTermRecord(term));
		}

		for (Record record : actions) {
			list.add(record);
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Payment Term");

		result.add(commandList);
		return result;
	}

	protected Result contactList(Context context, ClientPayee payee,
			ClientContact contact2) {
		Set<ClientContact> contacts = payee.getContacts();
		ResultList list = new ResultList(CONTACTS);
		int num = 0;
		if (contact2 != null && contact2.getName() != null) {
			list.add(createContactRecord(contact2));
			num++;
		}
		for (ClientContact contact : contacts) {
			if (contact != contact2) {
				list.add(createContactRecord(contact));
				num++;
			}
			if (num == CONTACTS_TO_SHOW) {
				break;
			}
		}

		Result result = context.makeResult();
		result.add("Select " + payee.getName() + "'s Contact");
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add(getMessages().create(getConstants().contact()));
		result.add(commandList);

		return result;
	}

	protected ArrayList<ClientAccount> getAccounts(
			ListFilter<ClientAccount> listFilter) {

		ArrayList<ClientAccount> accounts = getClientCompany().getAccounts();
		ArrayList<ClientAccount> filtered = new ArrayList<ClientAccount>();
		for (ClientAccount account : accounts) {
			if (listFilter.filter(account)) {
				filtered.add(account);
			}
		}
		return filtered;
	}

	protected Record createAccountRecord(ClientAccount last) {
		Record record = new Record(last);
		record.add("", getMessages().accountNumber(Global.get().Account()));
		record.add("", last.getNumber());
		record.add("", getMessages().accountName(Global.get().Account()));
		record.add("", last.getName());
		record.add("", getMessages().accountType(Global.get().Account()));
		record.add("", getAccountTypeString(last.getType()));
		record.add("", getConstants().balance());
		record.add("", last.getTotalBalance());
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

	protected Result payeeRequirement(Context context, ResultList list,
			String requirementName) {
		Requirement payeeReq = get(requirementName);
		ClientPayee payee = context.getSelection(requirementName);

		if (payee != null) {
			payeeReq.setValue(payee);
		}

		ClientPayee value = payeeReq.getValue();
		Object selection = context.getSelection(VALUES);
		if (!payeeReq.isDone() || value == selection) {
			return payee(context);
		}

		Record record = new Record(requirementName);
		record.add("", getConstants().payee());
		record.add("", value.getName());
		list.add(record);
		return null;
	}

	private Result payee(Context context) {
		Result result = context.makeResult();
		ResultList payeeList = new ResultList(PAYEE);

		Object last = context.getLast(RequirementType.PAYEE);
		List<ClientPayee> skipPayee = new ArrayList<ClientPayee>();
		if (last != null) {
			payeeList.add(createPayeeRecord((ClientPayee) last));
			skipPayee.add((ClientPayee) last);
		}

		ArrayList<ClientPayee> payees = getClientCompany().getPayees();
		ResultList actions = new ResultList(ACTIONS);
		ActionNames selection = context.getSelection(ACTIONS);

		List<ClientPayee> pagination = pagination(context, selection, actions,
				payees, skipPayee, VALUES_TO_SHOW);

		for (ClientPayee payee : pagination) {
			payeeList.add(createPayeeRecord(payee));
		}

		int size = payeeList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getMessages().pleaseSelect(getConstants().payee()));
		}
		CommandList commandList = new CommandList();
		commandList.add(getMessages().create(Global.get().Customer()));
		commandList.add(getMessages().create(Global.get().Vendor()));

		result.add(message.toString());
		result.add(payeeList);
		result.add(commandList);
		return result;
	}

	protected Record createPayeeRecord(ClientPayee payee) {
		Record record = new Record(payee);
		record.add("", payee.getName());
		record.add("", payee.getBalance());
		return record;
	}

	protected Result itemsAndAccountsRequirement(Context context,
			Result makeResult, ResultList actions,
			ListFilter<ClientAccount> accountFilter, boolean isSales) {
		Result result = itemsRequirement(context, makeResult, actions, isSales);
		if (result != null) {
			return result;
		}
		result = accountItemsRequirement(context, makeResult, accountFilter,
				actions);
		if (result != null) {
			return result;
		}
		return null;
	}

	protected Result accountItemsRequirement(Context context, Result result,
			ListFilter<ClientAccount> listFilter, ResultList actions) {
		Requirement transItemsReq = get(ACCOUNTS);
		List<ClientAccount> accounts = context.getSelections(ACCOUNTS);

		if (accounts != null && accounts.size() > 0) {
			for (ClientAccount account : accounts) {
				ClientTransactionItem transactionItem = new ClientTransactionItem();
				transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				transactionItem.setAccount(account.getID());
				List<ClientTransactionItem> transactionItems = transItemsReq
						.getValue();
				if (transactionItems == null) {
					transactionItems = new ArrayList<ClientTransactionItem>();
					transItemsReq.setValue(transactionItems);
				}
				transactionItems.add(transactionItem);

				if (transactionItem.getUnitPrice() == 0) {
					context.putSelection(ACCOUNT_ITEM_DETAILS, AMOUNT);
					Result transactionItemResult = transactionAccountItem(
							context, transactionItem);
					if (transactionItemResult != null) {
						return transactionItemResult;
					}
				} else if (context.getCompany().getPreferences().isTrackTax()
						&& context.getCompany().getPreferences()
								.isTaxPerDetailLine()
						&& transactionItem.getTaxCode() == 0) {
					context.putSelection(ACCOUNT_ITEM_DETAILS, TAXCODE);
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

		Object selection = context.getSelection(ACCOUNT_ITEMS);
		if (selection != null) {
			result = transactionAccountItem(context,
					(ClientTransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ACCOUNTS) {
			return accounts(context, ACCOUNTS, listFilter);
		}

		List<ClientTransactionItem> accountTransItems = transItemsReq
				.getValue();
		ResultList accountItems = new ResultList(ACCOUNT_ITEMS);
		result.add(getMessages()
				.accountTransactionItems(Global.get().Account()));
		for (ClientTransactionItem item : accountTransItems) {
			Record itemRec = new Record(item);
			itemRec.add("", getClientCompany().getAccount(item.getAccount())
					.getName());
			itemRec.add("", item.getUnitPrice());
			itemRec.add("", item.getDiscount());
			itemRec.add("", item.getLineTotal());
			accountItems.add(itemRec);
		}
		result.add(accountItems);

		Record moreItems = new Record(ActionNames.ADD_MORE_ACCOUNTS);
		moreItems.add("", getMessages().addMore(Global.get().Accounts()));
		actions.add(moreItems);
		return null;
	}

	protected Result accountItems(Context context, String label,
			ListFilter<ClientAccount> listFilter) {
		Result result = context.makeResult();
		ArrayList<ClientAccount> accounts = getAccounts(listFilter);
		accounts = new ArrayList<ClientAccount>(accounts);
		Collections.sort(accounts, new Comparator<ClientAccount>() {

			@Override
			public int compare(ClientAccount o1, ClientAccount o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		ResultList list = new ResultList(label);
		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			list.add(createAccountRecord((ClientAccount) last));
			num++;
		}
		Requirement itemsReq = get(label);
		List<ClientTransactionItem> transItems = itemsReq.getValue();
		if (transItems == null) {
			transItems = new ArrayList<ClientTransactionItem>();
		}
		List<Long> availableAccounts = new ArrayList<Long>();
		for (ClientTransactionItem transactionItem : transItems) {
			availableAccounts.add(transactionItem.getAccount());
		}
		for (ClientAccount account : accounts) {
			if (account != last || !availableAccounts.contains(account.getID())) {
				list.add(createAccountRecord(account));
				num++;
			}
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add(getMessages().pleaseSelect(Global.get().Account()));
		} else {
			result.add(getMessages().youDontHaveAny(Global.get().Accounts()));
		}

		result.add(list);
		CommandList commands = new CommandList();
		commands.add(getMessages().addMore(Global.get().Accounts()));
		result.add(commands);
		return result;
	}

	public double getTaxTotal(List<ClientTransactionItem> items,
			ClientTAXCode taxCode) {

		int totaldiscount = 0;
		double totallinetotal = 0.0;
		double taxableTotal = 0.0;
		double totalVat = 0.0;
		double grandTotal = 0.0;
		double totalValue = 0.0;

		for (ClientTransactionItem citem : items) {
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

			// citem.setVATfraction(getVATAmount(citem.getTaxCode(), citem,
			// company));
			totalVat += citem.getVATfraction();
			// totalVat += citem.getVATfraction();
		}

		return taxableTotal;
	}

	public double getNetTotal(List<ClientTransactionItem> items) {

		int totaldiscount = 0;
		double totallinetotal = 0.0;
		double taxableTotal = 0.0;
		double totalVat = 0.0;
		double grandTotal = 0.0;
		double totalValue = 0.0;
		for (ClientTransactionItem citem : items) {
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

			// citem.setVATfraction(getVATAmount(citem.getTaxCode(), citem,
			// company));
			totalVat += citem.getVATfraction();
			// totalVat += citem.getVATfraction();
		}

		if (getClientCompany().getPreferences().isTrackTax()) {
			grandTotal = totalVat + totallinetotal;
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}
		if (getClientCompany().getPreferences().isTrackTax()) {
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

	protected List<ClientAccount> getAccounts(Boolean isActive) {
		FinanceTool financeTool = new FinanceTool();
		List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		List<ClientAccount> allaccounts = null;// financeTool.getCompanyManager().getAccountsListBySorted(
		// company.getAccountingType(), company.getID());
		for (ClientAccount acc : allaccounts) {
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

	protected List<BillsList> getExpenses(String viewType) {

		ArrayList<BillsList> billsList = null;
		try {
			billsList = new FinanceTool().getVendorManager().getBillsList(true,
					getClientCompany().getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return filterList(viewType, billsList);
	}

	protected List<BillsList> getBills(String viewType) {
		ArrayList<BillsList> billsList = null;

		try {
			billsList = new FinanceTool().getVendorManager().getBillsList(
					false, getClientCompany().getID());
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

	protected List<Estimate> getEstimates(String viewType) {

		List<Estimate> result = new ArrayList<Estimate>();
		List<Estimate> data = null;
		try {
			data = new FinanceTool().getCustomerManager().getEstimates(
					getClientCompany().getID());
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

	protected List<InvoicesList> getInvoices(String viewType) {

		ArrayList<InvoicesList> invoiceList = null;
		try {
			invoiceList = new FinanceTool().getInventoryManager()
					.getInvoiceList(getClientCompany().getID());
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return invoiceList;

	}

	protected List<ReceivePaymentsList> getReceivePaymentsList(String viewType) {

		ArrayList<ReceivePaymentsList> invoiceList = null;
		try {
			invoiceList = new FinanceTool().getCustomerManager()
					.getReceivePaymentsList(getClientCompany().getID());
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return invoiceList;

	}

	protected List<PaymentsList> getPayments(int type) {

		ArrayList<PaymentsList> paymentsList = null;
		ArrayList<PaymentsList> result = new ArrayList<PaymentsList>();
		try {
			paymentsList = new FinanceTool().getCustomerManager()
					.getPaymentsList(getClientCompany().getID());
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

	protected List<ClientTransactionIssuePayment> getIssuePaymentTransactionsList(
			String paymentMethod, String accountName) {
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
		Object obj = context.getSelection(TAXCODE);
		ClientTAXCode taxCode = null;
		if (obj instanceof ActionNames) {
			taxCode = null;
		} else {
			taxCode = (ClientTAXCode) obj;
		}

		if (taxCode != null) {
			taxCodeRequirement.setValue(taxCode);
		}
		if (!taxCodeRequirement.isDone()) {
			return taxCode(context, null);
		}

		ClientTAXCode value = taxCodeRequirement.getValue();
		Object selection = context.getSelection(VALUES);
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

	public void updateTotals(ClientTransaction transaction, boolean isSales) {
		List<ClientTransactionItem> allrecords = transaction
				.getTransactionItems();
		double lineTotal = 0.0;
		double totalTax = 0.0;

		for (ClientTransactionItem record : allrecords) {

			int type = record.getType();

			if (type == 0)
				continue;

			Double lineTotalAmt = record.getLineTotal();
			lineTotal += lineTotalAmt;

			if (record != null && record.isTaxable()) {
				double taxAmount = getVATAmount(transaction,
						record.getTaxCode(), record, isSales);
				if (transaction.isAmountsIncludeVAT()) {
					lineTotal -= taxAmount;
				}
				record.setVATfraction(taxAmount);
				totalTax += record.getVATfraction();
			}
		}

		double grandTotal = totalTax + lineTotal;

		transaction.setTotal(grandTotal);
		transaction.setNetAmount(lineTotal);
	}

	public double getVATAmount(ClientTransaction transaction, long TAXCodeID,
			ClientTransactionItem record, boolean isSales) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				vatRate = UIUtils.getVATItemRate(
						getClientCompany().getTAXCode(TAXCodeID), isSales);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (transaction.isAmountsIncludeVAT()) {
			vat = ((ClientTransactionItem) record).getLineTotal()
					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
		} else {
			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
					/ 100;
		}
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	protected ArrayList<ClientCreditsAndPayments> getCustomerCreditsAndPayments(
			long customerId) {
		List<ClientCreditsAndPayments> clientCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = new FinanceTool().getCustomerManager()
					.getCustomerCreditsAndPayments(customerId,
							getClientCompany().getID());
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				clientCreditsAndPayments.add(new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCreditsAndPayments>(clientCreditsAndPayments);
	}

	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate) throws AccounterException {
		List<ReceivePaymentTransactionList> receivePaymentTransactionList = null;
		receivePaymentTransactionList = new FinanceTool()
				.getTransactionReceivePayments(customerId, paymentDate,
						getClientCompany().getID());
		return new ArrayList<ReceivePaymentTransactionList>(
				receivePaymentTransactionList);
	}

	protected ArrayList<PayBillTransactionList> getTransactionPayBills(
			long vendorId) {
		ArrayList<PayBillTransactionList> bills = new ArrayList<PayBillTransactionList>();
		try {
			bills = new FinanceTool().getVendorManager()
					.getTransactionPayBills(vendorId,
							getClientCompany().getID());
		} catch (DAOException e) {
			e.printStackTrace();
			bills = new ArrayList<PayBillTransactionList>();
		}
		return bills;
	}

	protected ArrayList<ClientCreditsAndPayments> getCreditsAndPayments(
			long vendorId) {
		ArrayList<CreditsAndPayments> vendorCreditsAndPayments = new ArrayList<CreditsAndPayments>();
		try {
			vendorCreditsAndPayments = new FinanceTool().getVendorManager()
					.getVendorCreditsAndPayments(vendorId,
							getClientCompany().getID());
		} catch (DAOException e) {
			e.printStackTrace();
			vendorCreditsAndPayments = new ArrayList<CreditsAndPayments>();
		}
		ArrayList<ClientCreditsAndPayments> clientCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		for (CreditsAndPayments creditsAndPayments : vendorCreditsAndPayments) {
			try {
				clientCreditsAndPayments.add(new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class));
			} catch (AccounterException e) {
				e.printStackTrace();
				clientCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
			}
		}
		return clientCreditsAndPayments;
	}

	protected ArrayList<ClientTransactionIssuePayment> getchecks(long accountId) {
		ArrayList<IssuePaymentTransactionsList> checks;
		ArrayList<ClientTransactionIssuePayment> issuepayments = new ArrayList<ClientTransactionIssuePayment>();
		try {
			checks = new FinanceTool().getVendorManager().getChecks(accountId,
					getClientCompany().getID());

			for (IssuePaymentTransactionsList entry : checks) {
				ClientTransactionIssuePayment record = new ClientTransactionIssuePayment();
				if (entry.getDate() != null)
					record.setDate(entry.getDate().getDate());
				if (entry.getNumber() != null)
					record.setNumber(entry.getNumber());
				record.setName(entry.getName() != null ? entry.getName() : "");
				record.setMemo(entry.getMemo() != null ? entry.getMemo() : "");
				if (entry.getAmount() != null)
					record.setAmount(entry.getAmount());
				if (entry.getPaymentMethod() != null)
					record.setPaymentMethod(entry.getPaymentMethod());
				record.setRecordType(entry.getType());
				if (record.getRecordType() == ClientTransaction.TYPE_WRITE_CHECK)
					record.setWriteCheck(entry.getTransactionId());
				else if (record.getRecordType() == ClientTransaction.TYPE_CUSTOMER_REFUNDS)
					record.setCustomerRefund(entry.getTransactionId());
				record.setID(entry.getTransactionId());
				issuepayments.add(record);
			}

		} catch (DAOException e) {
			e.printStackTrace();
			issuepayments = new ArrayList<ClientTransactionIssuePayment>();
		}
		return issuepayments;
	}
}
