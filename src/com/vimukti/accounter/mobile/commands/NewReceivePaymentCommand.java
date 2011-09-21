package com.vimukti.accounter.mobile.commands;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewReceivePaymentCommand extends AbstractTransactionCommand {

	private static final String RECEIVED_FROM = "Received from";
	private static final String DATE = "date";
	private static final String NUMBER = "number";
	private static final String AMOUNT_RECEIVED = "amount received";
	private static final String PAYMENT_MENTHOD = "payment method";
	private static final String DEPOSITSANDTRANSFERS = "Deposit / Transfer To";
	private static final String DUE_DATE = "due Date";
	private static final String INVOICE = "invoice";
	private static final String INVOICE_AMOUNT = "invoice amount";
	private static final String DISCOUNT_DATE = "Discount Date";
	private static final String CASH_DISCOUNT = "Cash discount";
	private static final String WRITE_OFF = "Write off";
	private static final String APPLIED_CREDITS = "Applied Credits";
	private static final String AMOUNT_DUE = "Applied Credits";
	private static final String PAYMENT = "Payment";
	private static final String MEMO = "memo";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(RECEIVED_FROM, false, true));
		list.add(new Requirement(DEPOSITSANDTRANSFERS, false, true));
		list.add(new Requirement(PAYMENT_MENTHOD, false, true));
		list.add(new Requirement(AMOUNT_RECEIVED, true, true));
		list.add(new ObjectListRequirement("transactionItems", false, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(DUE_DATE, true, true));
				list.add(new Requirement(INVOICE, true, true));
				list.add(new Requirement(INVOICE_AMOUNT, true, true));
				list.add(new Requirement(AMOUNT_DUE, true, true));
				list.add(new Requirement(DISCOUNT_DATE, true, true));
				list.add(new Requirement(CASH_DISCOUNT, true, true));
				list.add(new Requirement(WRITE_OFF, true, true));
				list.add(new Requirement(APPLIED_CREDITS, true, true));
				list.add(new Requirement(PAYMENT, true, true));
			}
		});
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, true));
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {

		Result result = null;
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals("items")) {
				result = receivePaymentItemProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		result = customerRequirement(context);
		if (result != null) {
			return result;
		}
		try {
			result = transactionItems(context);
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context);
		if (result != null) {
			return result;
		}
		result = depositeOrTransferTo(context);
		if (result != null) {
			return result;
		}
		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 * @throws ParseException
	 * @throws DAOException
	 */

	private Result transactionItems(Context context) throws DAOException,
			ParseException {
		// Requirement itemsReq = get("transactionItems");
		Customer customer = context.getSelection(RECEIVED_FROM);
		long date = context.getSelection(DATE);
		List<ReceivePaymentTransactionList> transactionItems = new FinanceTool()
				.getTransactionReceivePayments(customer.getID(), date);
		Result item = item(context, transactionItems);
		return item;

	}

	protected Result item(Context context,
			List<ReceivePaymentTransactionList> transactionItems) {

		Result result = context.makeResult();
		List<ReceivePaymentTransactionList> items = transactionItems;

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
		for (ReceivePaymentTransactionList item : items) {
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
		return result;
	}

	private Record creatItemRecord(ReceivePaymentTransactionList item) {
		Record record = new Record(item);
		record.add("Name", "transactionItem");
		record.add("value", item.getPayment());
		return record;
	}

	/**
	 * create Optional requirement
	 * 
	 * @param context
	 * @return {@link Result}
	 */
	private Result createOptionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		Requirement itemsReq = get("items");
		List<ReceivePaymentTransactionList> transItems = itemsReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = receivePayment(context,
					(ReceivePaymentTransactionList) selection);
			if (result != null) {
				return result;
			}
		}
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Result result = context.makeResult();
		result.add("Receive Payment is ready to create with following values.");
		result.add(list);

		result.add("Items:-");
		ResultList items = new ResultList("transactionItems");
		for (ReceivePaymentTransactionList item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add(NUMBER, item.getNumber());
			itemRec.add("Total", item.getPayment());
			items.add(itemRec);
		}
		result.add(items);

		ResultList actions = new ResultList(ACTIONS);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create receive payment.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	protected Result receivePaymentItemProcess(Context context) {
		ReceivePaymentTransactionList receivePaymentTransactionList = (ReceivePaymentTransactionList) context
				.getAttribute("items");
		Result result = receivePayment(context, receivePaymentTransactionList);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement itemsReq = get("items");
				List<TransactionItem> transItems = itemsReq.getValue();
				transItems.remove(receivePaymentTransactionList);
				context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
			}
		}
		return result;
	}

	protected Result receivePayment(Context context,
			ReceivePaymentTransactionList receivePaymentTransactionList) {
		context.setAttribute(PROCESS_ATTR, "items");
		context.setAttribute(OLD_TRANSACTION_ITEM_ATTR,
				receivePaymentTransactionList);

		String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ITEM_PROPERTY_ATTR);
			if (lineAttr.equals(DUE_DATE)) {
				receivePaymentTransactionList.setDueDate(new ClientFinanceDate(
						context.getDate()));
			} else if (lineAttr.equals(INVOICE)) {
				receivePaymentTransactionList.setNumber(context.getString());
			} else if (lineAttr.equals(INVOICE_AMOUNT)) {
				receivePaymentTransactionList.setInvoiceAmount(context
						.getDouble());
			} else if (lineAttr.equals(AMOUNT_DUE)) {
				receivePaymentTransactionList.setAmountDue(context.getDouble());
			} else if (lineAttr.equals(DISCOUNT_DATE)) {
				receivePaymentTransactionList
						.setDiscountDate(new ClientFinanceDate(context
								.getDate()));
			} else if (lineAttr.equals(CASH_DISCOUNT)) {
				receivePaymentTransactionList.setCashDiscount(context
						.getDouble());
			} else if (lineAttr.equals(WRITE_OFF)) {
				receivePaymentTransactionList.setWriteOff(context.getDouble());
			} else if (lineAttr.equals(APPLIED_CREDITS)) {
				receivePaymentTransactionList.setAppliedCredits(context
						.getDouble());
			} else if (lineAttr.equals(PAYMENT)) {
				receivePaymentTransactionList.setPayment(context.getDouble());
			}
		} else {
			Object selection = context.getSelection(ITEM_DETAILS);
			if (selection != null) {
				if (selection == receivePaymentTransactionList.getDueDate()) {
					context.setAttribute(ITEM_PROPERTY_ATTR, DUE_DATE);
					return date(context, "Enter due Date",
							receivePaymentTransactionList.getDueDate()
									.getDateAsObject());
				} else if (selection.equals(receivePaymentTransactionList
						.getNumber())) {
					context.setAttribute(ITEM_PROPERTY_ATTR, NUMBER);
					return number(context, "Enter Invoice Number",
							receivePaymentTransactionList.getNumber());
				} else if (selection.equals(INVOICE_AMOUNT)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, INVOICE_AMOUNT);
					return amount(context, "Enter invoice amount",
							receivePaymentTransactionList.getInvoiceAmount());
				} else if (selection == receivePaymentTransactionList
						.getAmountDue()) {
					context.setAttribute(ITEM_PROPERTY_ATTR, AMOUNT_DUE);
					return amount(context, "Enter amount due",
							receivePaymentTransactionList.getAmountDue());
				} else if (selection.equals(receivePaymentTransactionList
						.getDiscountDate())) {
					context.setAttribute(ITEM_PROPERTY_ATTR, DISCOUNT_DATE);
					return date(context, "Enter discount Date",
							receivePaymentTransactionList.getDiscountDate()
									.getDateAsObject());
				} else if (selection == receivePaymentTransactionList
						.getCashDiscount()) {
					context.setAttribute(ITEM_PROPERTY_ATTR, CASH_DISCOUNT);
					return amount(context, "Enter cash discount Date",
							receivePaymentTransactionList.getCashDiscount());
				} else if (selection == receivePaymentTransactionList
						.getWriteOff()) {
					context.setAttribute(ITEM_PROPERTY_ATTR, WRITE_OFF);
					return amount(context, "Enter write off",
							receivePaymentTransactionList.getWriteOff());
				} else if (selection == receivePaymentTransactionList
						.getAppliedCredits()) {
					context.setAttribute(ITEM_PROPERTY_ATTR, APPLIED_CREDITS);
					return amount(context, "Enter applied credit",
							receivePaymentTransactionList.getAppliedCredits());
				} else if (selection == (receivePaymentTransactionList
						.getPayment())) {
					context.setAttribute(ITEM_PROPERTY_ATTR, PAYMENT);
					return amount(context, "Enter payment",
							receivePaymentTransactionList.getPayment());
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
		Record record = new Record(DUE_DATE);
		record.add("", DUE_DATE);
		record.add("", receivePaymentTransactionList.getDueDate());
		list.add(record);

		record = new Record(INVOICE);
		record.add("", INVOICE);
		record.add("", receivePaymentTransactionList.getNumber());
		list.add(record);

		record = new Record(INVOICE_AMOUNT);
		record.add("", INVOICE_AMOUNT);
		record.add("", receivePaymentTransactionList.getInvoiceAmount());
		list.add(record);

		record = new Record(AMOUNT_DUE);
		record.add("", AMOUNT_DUE);
		record.add("", receivePaymentTransactionList.getAmountDue());
		list.add(record);

		record = new Record(DISCOUNT_DATE);
		record.add("", DISCOUNT_DATE);
		record.add("", receivePaymentTransactionList.getDiscountDate());
		list.add(record);

		record = new Record(CASH_DISCOUNT);
		record.add("", CASH_DISCOUNT);
		record.add("", receivePaymentTransactionList.getCashDiscount());
		list.add(record);

		record = new Record(WRITE_OFF);
		record.add("", WRITE_OFF);
		record.add("", receivePaymentTransactionList.getWriteOff());
		list.add(record);

		record = new Record(APPLIED_CREDITS);
		record.add("", APPLIED_CREDITS);
		record.add("", receivePaymentTransactionList.getAppliedCredits());
		list.add(record);

		record = new Record(PAYMENT);
		record.add("", PAYMENT);
		record.add("", receivePaymentTransactionList.getPayment());
		list.add(record);

		Result result = context.makeResult();
		result.add("Item details");
		result.add("Item Name :" + receivePaymentTransactionList.getNumber());
		result.add(list);

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
}
