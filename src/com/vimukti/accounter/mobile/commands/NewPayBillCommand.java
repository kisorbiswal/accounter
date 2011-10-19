package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewPayBillCommand extends AbstractTransactionCommand {

	private static final String DUE_DATE = "duedate";
	private static final String BILL_NO = "billno";
	private static final String ORIGINAL_AMOUNT = "orginalamount";
	private static final String AMOUNT_DUE = "amountdue";
	private static final String DISCOUNT_DATE = "discountdate";
	private static final String CASH_DISCOUNT = "cashdiscount";
	private static final String CREDITS = "credits";
	private static final String PAYMENT = "payment";
	private static final String FILTER_BY_DUE_ON_BEFORE = "filterbydueonbefore";
	private static final String NUMBER = "number";

	@Override
	public String getId() {

		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VENDOR, false, true));
		list.add(new Requirement(NUMBER, true, true));
		list.add(new Requirement(PAY_FROM, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(FILTER_BY_DUE_ON_BEFORE, true, true));
		list.add(new ObjectListRequirement("items", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(BILL_NO, true, true));
				list.add(new Requirement(ORIGINAL_AMOUNT, true, true));
				list.add(new Requirement(DUE_DATE, true, true));
				list.add(new Requirement(AMOUNT_DUE, true, true));
				list.add(new Requirement(DISCOUNT_DATE, true, true));
				list.add(new Requirement(CASH_DISCOUNT, true, true));
				list.add(new Requirement(CREDITS, true, true));
				list.add(new Requirement(PAYMENT, true, true));

			}
		});
		list.add(new Requirement(MEMO, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		Result makeResult = context.makeResult();
		makeResult.add("Paybill  is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = createSupplierRequirement(context, list, VENDOR);
		if (result != null) {
			return result;
		}

		try {
			result = transactionItems(context);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, list, PAY_FROM,
				new ListFilter<ClientAccount>() {
					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}
				});

		if (result != null) {
			return null;
		}
		result = paymentMethodRequirement(context, list, PAYMENT_METHOD);
		if (result != null) {
			return result;
		}
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();

		return result;
	}

	private void completeProcess(Context context) {

		PayBill paybill = new PayBill();
		Vendor vendor = get(VENDOR).getValue();
		Account payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		Date dueDate = (Date) get(FILTER_BY_DUE_ON_BEFORE).getValue();
		String number = get(NUMBER).getValue();
		Date date = (Date) get(DATE).getValue();
		String billNumber = get(BILL_NO).getValue();

		paybill.setVendor(vendor);
		paybill.setPayFrom(payFrom);
		paybill.setPaymentMethod(paymentMethod);

		paybill.setBillDueOnOrBefore(new FinanceDate(dueDate));
		paybill.setNumber(number);
		paybill.setDate(new FinanceDate(date));

		List<TransactionPayBill> transactionPayBills = get("items").getValue();

		paybill.setTransactionPayBill(transactionPayBills);
		String memo = get(MEMO).getValue();
		paybill.setMemo(memo);
		create(paybill, context);

	}

	private Result transactionItems(Context context) throws DAOException {

		ClientVendor value = get(VENDOR).getValue();
		// long date = context.getSelection(DATE);
		List<PayBillTransactionList> transactionItems = new FinanceTool()
				.getVendorManager().getTransactionPayBills(
						context.getCompany().getID());

		List<TransactionPayBill> records = new ArrayList<TransactionPayBill>();
		for (PayBillTransactionList billTransactionList : transactionItems) {
			TransactionPayBill transactionPayBill = new TransactionPayBill();
			transactionPayBill.setPayment(billTransactionList.getPayment());
			transactionPayBill.setDueDate(new FinanceDate(billTransactionList
					.getDueDate()));
			transactionPayBill.setOriginalAmount(billTransactionList
					.getOriginalAmount());
			transactionPayBill.setAmountDue(billTransactionList.getAmountDue());
			// transactionPayBill.setDiscountDate(billTransactionList
			// .getDiscountDate() != null ? new FinanceDate(
			// billTransactionList.getDiscountDate()) : "");
			transactionPayBill.setCashDiscount(billTransactionList
					.getCashDiscount());
			transactionPayBill.setAppliedCredits(billTransactionList
					.getCredits());
			transactionPayBill.setBillNumber(billTransactionList
					.getBillNumber());

			records.add(transactionPayBill);
		}
		Result item = item(context, records);
		return item;

	}

	private Result item(Context context, List<TransactionPayBill> records) {
		Result result = context.makeResult();
		List<TransactionPayBill> items = records;

		ResultList list = new ResultList("items");
		Object last = context.getLast(RequirementType.ITEM);
		int num = 0;
		if (last != null) {
			list.add(createItemRecord((ClientItem) last));
			num++;
		}
		Requirement itemsReq = get("items");
		List<TransactionItem> transItems = itemsReq.getValue();
		List<Item> availableItems = new ArrayList<Item>();
		if (transItems != null)
			for (TransactionItem transactionItem : transItems) {
				availableItems.add(transactionItem.getItem());
			}
		for (TransactionPayBill item : items) {
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

	private Record creatItemRecord(TransactionPayBill item) {
		Record record = new Record(item);
		record.add("Name", "transactionItem");
		record.add("value", item.getPayment());
		return record;
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
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
		List<TransactionPayBill> transItems = itemsReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = payBillItems(context,
					(TransactionPayBill) selection);
			if (result != null) {
				return result;
			}
		}
		selection = context.getSelection("values");

		Result result = dateOptionalRequirement(context, list, DATE,
				"Enter date", selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				"Enter number");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter memo");
		if (result != null) {
			return result;
		}

		result.add("Items:-");
		ResultList items = new ResultList("transactionItems");
		for (TransactionPayBill item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Total", item.getPayment());
			items.add(itemRec);
		}
		result.add(items);

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish payment.");
		actions.add(finish);

		return makeResult;
	}

	private Result payBillItems(Context context,
			TransactionPayBill paybillTransaction) {
		context.setAttribute(PROCESS_ATTR, "items");
		context.setAttribute(OLD_TRANSACTION_ITEM_ATTR, paybillTransaction);

		String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ITEM_PROPERTY_ATTR);
			if (lineAttr.equals(ORIGINAL_AMOUNT)) {
				paybillTransaction.setOriginalAmount(context.getDouble());
			} else if (lineAttr.equals(CASH_DISCOUNT)) {
				paybillTransaction.setCashDiscount(context.getDouble());
			} else if (lineAttr.equals(CREDITS)) {
				paybillTransaction.setAppliedCredits(context.getDouble());
			} else if (lineAttr.equals(PAYMENT)) {
				paybillTransaction.setPayment(context.getDouble());
			} else if (lineAttr.equals(BILL_NO)) {
				paybillTransaction.setBillNumber(context.getString());
			}
		} else {
			Object selection = context.getSelection(ITEM_DETAILS);
			if (selection != null) {
				if (selection.equals(ORIGINAL_AMOUNT)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, ORIGINAL_AMOUNT);
					return amount(context, "Enter Original amount",
							paybillTransaction.getOriginalAmount());
				} else if (selection.equals(CASH_DISCOUNT)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, CASH_DISCOUNT);
					return amount(context, "Enter cash discount Date",
							paybillTransaction.getCashDiscount());
				} else if (selection.equals(CREDITS)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, CREDITS);
					return amount(context, "Enter applied credit",
							paybillTransaction.getAppliedCredits());
				} else if (selection.equals(PAYMENT)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, PAYMENT);
					return amount(context, "Enter payment",
							paybillTransaction.getPayment());
				} else if (selection.equals(BILL_NO)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, BILL_NO);
					return text(context, "Enter bill number", ""
							+ paybillTransaction.getBillNumber());
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

		Record record = new Record(ORIGINAL_AMOUNT);
		record.add("", ORIGINAL_AMOUNT);
		record.add("", paybillTransaction.getOriginalAmount());
		list.add(record);

		record = new Record(BILL_NO);
		record.add("", BILL_NO);
		record.add("", paybillTransaction.getBillNumber());
		list.add(record);

		record = new Record(CASH_DISCOUNT);
		record.add("", CASH_DISCOUNT);
		record.add("", paybillTransaction.getCashDiscount());
		list.add(record);

		record = new Record(CREDITS);
		record.add("", CREDITS);
		record.add("", paybillTransaction.getAppliedCredits());
		list.add(record);

		record = new Record(PAYMENT);
		record.add("", PAYMENT);
		record.add("", paybillTransaction.getPayment());
		list.add(record);

		Result result = context.makeResult();
		result.add("Item details");
		result.add("Item Name :" + paybillTransaction.getBillNumber());
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		actions.add(record);
		result.add(actions);
		return result;
	}
}
