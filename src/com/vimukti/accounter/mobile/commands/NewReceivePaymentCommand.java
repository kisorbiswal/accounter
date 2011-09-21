package com.vimukti.accounter.mobile.commands;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
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

		// if (!itemsReq.isDone()) {
		// if (transactionItems.size() > 0) {
		// itemsReq.setValue(transactionItems);
		// } else {
		// return item(context);
		// }
		// }
		// if (transactionItems != null && transactionItems.size() > 0) {
		// List<TransactionItem> items = itemsReq.getValue();
		// items.addAll(transactionItems);
		// }

		return null;

	}

	protected Result item(Context context) {
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
		return result;
	}

	/**
	 * create Optional requirement
	 * 
	 * @param context
	 * @return {@link Result}
	 */
	private Result createOptionalRequirement(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
