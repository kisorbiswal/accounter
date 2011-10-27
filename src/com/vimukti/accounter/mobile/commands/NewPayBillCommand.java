package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

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
	private static final String PAY_BILL_LIST = "paybilllist";
	private static final String FILTER_BY_DUE_ON_BEFORE = "filterbydueonbefore";
	private static final String TRANSACTION_PAY_BILL_PROCESS = "paybillprocess";
	private static final String OLD_TRANSACTION_PAY_BILL_ATTR = "oldpaybillprocess";
	private static final String TRANSACTION_PAY_BILL_PROPERTY_ATTR = "paybillpropertyattr";
	private static final String PAY_BILL_DETAILS = "paybilldetails";
	private static final String VALUES = "values";
	private static final String SUPPLIERS = "suppliers";
	private static final String OPTIONAL = "optional";

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
		list.add(new ObjectListRequirement(PAY_BILL_LIST, false, true) {

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
		setDefaultValues();
		Result result = null;
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_PAY_BILL_PROCESS)) {
				ClientTransactionPayBill transactionItem = (ClientTransactionPayBill) context
						.getAttribute(OLD_TRANSACTION_PAY_BILL_ATTR);
				result = clientTransactionPayBillProcess(context,
						transactionItem);
				if (result != null) {
					return result;
				}
			}
		}

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().payBill()));
		ResultList list = new ResultList(VALUES);
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		result = createSupplierRequirement(context, list, VENDOR, Global.get()
				.Vendor());

		if (result != null) {
			return result;
		}
		if (context.getSelection(SUPPLIERS) != null) {
			get(PAY_BILL_LIST).setValue(
					new ArrayList<ClientTransactionPayBill>());
		}

		result = accountRequirement(context, list, PAY_FROM, getConstants()
				.payFrom(), new ListFilter<ClientAccount>() {
			@Override
			public boolean filter(ClientAccount e) {
				return true;
			}
		});

		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context, list, PAYMENT_METHOD,
				getConstants().paymentMethod());
		if (result != null) {
			return result;
		}

		result = paybillListRequirement(context, makeResult, actions);
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		result = new Result(getMessages().createSuccessfully(
				getConstants().payBill()));
		return result;
	}

	private void setDefaultValues() {
		get(NUMBER).setDefaultValue("1");
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(FILTER_BY_DUE_ON_BEFORE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
	}

	private Result paybillListRequirement(Context context, Result result,
			ResultList actions) {
		Requirement transItemsReq = get(PAY_BILL_LIST);
		List<ClientTransactionPayBill> items = context
				.getSelections(PAY_BILL_LIST);
		List<ClientTransactionPayBill> transactionItems = transItemsReq
				.getValue();
		if (items != null && items.size() > 0) {
			for (ClientTransactionPayBill item : items) {
				item.setPayment(item.getAmountDue());
				updateValue(item);
				transactionItems.add(item);
			}

		}
		if (transactionItems.size() == 0) {
			return clientTransactionPayBills(context);
		}

		Object selection = context.getSelection("transactionPayBills");
		if (selection != null) {
			result = clientTransactionPayBillProcess(context,
					(ClientTransactionPayBill) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ITEMS) {
			return clientTransactionPayBills(context);
		}

		result.add(getConstants().payBills());
		ResultList itemsList = new ResultList("transactionPayBills");
		for (ClientTransactionPayBill item : transactionItems) {
			itemsList.add(creatTransactionPayBillRecord(item));
		}
		result.add(itemsList);

		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", getMessages().addMore(getConstants().payBills()));
		actions.add(moreItems);
		return null;
	}

	private Result clientTransactionPayBillProcess(Context context,
			ClientTransactionPayBill transactionItem) {
		context.setAttribute(PROCESS_ATTR, TRANSACTION_PAY_BILL_PROCESS);
		context.setAttribute(OLD_TRANSACTION_PAY_BILL_ATTR, transactionItem);

		String lineAttr = (String) context
				.getAttribute(TRANSACTION_PAY_BILL_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(TRANSACTION_PAY_BILL_PROPERTY_ATTR);
			if (lineAttr.equals("payment")) {
				if (context.getDouble() != null) {
					transactionItem.setPayment(context.getDouble());
				} else if (context.getInteger() != null) {
					transactionItem.setPayment(context.getInteger());
				}
			}
		} else {
			Object selection = context.getSelection(PAY_BILL_DETAILS);
			if (selection != null) {
				if (selection.equals("payment")) {
					context.setAttribute(TRANSACTION_PAY_BILL_PROPERTY_ATTR,
							"payment");
					return amount(
							context,
							getMessages().pleaseEnter(getConstants().payment()),
							transactionItem.getPayment());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_PAY_BILL) {
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_PAY_BILL) {
					context.removeAttribute(PROCESS_ATTR);
					Requirement itemsReq = get(PAY_BILL_LIST);
					List<ClientTransactionPayBill> transItems = itemsReq
							.getValue();
					transItems.remove(transactionItem);
					context.removeAttribute(OLD_TRANSACTION_PAY_BILL_ATTR);
					return null;
				}
			}
		}
		ResultList list = new ResultList(PAY_BILL_DETAILS);
		Result result = context.makeResult();
		result.add(getConstants().dueDate()
				+ " : "
				+ getDateAsString(new ClientFinanceDate(transactionItem
						.getDueDate())));
		result.add(getConstants().billNo() + " : "
				+ transactionItem.getBillNumber());
		result.add(getConstants().originalAmount() + " : "
				+ transactionItem.getOriginalAmount());
		result.add(getConstants().amountDue() + " : "
				+ transactionItem.getAmountDue());
		result.add(getConstants().discountDate()
				+ " : "
				+ getDateAsString(new ClientFinanceDate(transactionItem
						.getDiscountDate())));
		result.add(getConstants().cashDiscount() + " : "
				+ transactionItem.getCashDiscount());
		Record record = new Record("payment");
		record.add("", getConstants().payment());
		record.add("", transactionItem.getPayment());
		list.add(record);
		result.add(list);
		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_PAY_BILL);
		record.add("", getConstants().delete());
		actions.add(record);
		record = new Record(ActionNames.FINISH_PAY_BILL);
		record.add("", getConstants().finish());
		updateValue(transactionItem);
		actions.add(record);
		result.add(actions);
		return result;
	}

	private Result clientTransactionPayBills(Context context) {
		Result result = context.makeResult();
		List<ClientTransactionPayBill> items = getclientTransactionPayBills();
		ResultList list = new ResultList(PAY_BILL_LIST);
		ClientTransactionPayBill last = (ClientTransactionPayBill) context
				.getLast(RequirementType.TRANSACTION_PAY_BILL);
		int num = 0;
		if (last != null) {
			list.add(creatTransactionPayBillRecord(last));
			num++;
		}
		Requirement itemsReq = get(PAY_BILL_LIST);
		List<ClientTransactionPayBill> transItems = itemsReq.getValue();
		if (transItems == null) {
			transItems = new ArrayList<ClientTransactionPayBill>();
		}
		List<ClientTransactionPayBill> availableItems = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill transactionItem : transItems) {
			availableItems.add(transactionItem);
		}
		for (ClientTransactionPayBill item : items) {
			if (item != last && !availableItems.contains(item)) {
				list.add(creatTransactionPayBillRecord(item));
				num++;
			}
			if (num == ITEMS_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add(getMessages().pleaseSelect(getConstants().payBills()));
		} else {
			result.add(getMessages().youDontHaveAny(getConstants().payBills()));
		}
		result.add(list);
		return result;
	}

	private Record creatTransactionPayBillRecord(ClientTransactionPayBill last) {
		Record paybillRecord = new Record(last);
		paybillRecord.add("", getConstants().dueDate());
		paybillRecord.add("",
				getDateAsString(new ClientFinanceDate(last.getDueDate())));
		paybillRecord.add("", getConstants().billNo());
		paybillRecord.add("", last.getBillNumber());
		paybillRecord.add("", getConstants().originalAmount());
		paybillRecord.add("", last.getOriginalAmount());
		paybillRecord.add("", getConstants().amountDue());
		paybillRecord.add("", last.getAmountDue());
		paybillRecord.add("", getConstants().discountDate());
		paybillRecord.add("",
				getDateAsString(new ClientFinanceDate(last.getDiscountDate())));
		paybillRecord.add("", getConstants().cashDiscount());
		paybillRecord.add("", last.getCashDiscount());
		return paybillRecord;
	}

	private List<ClientTransactionPayBill> getclientTransactionPayBills() {
		return filterGrid();
	}

	private void completeProcess(Context context) {
		ClientPayBill paybill = new ClientPayBill();
		paybill.setType(ClientTransaction.TYPE_PAY_BILL);
		paybill.setPayBillType(ClientPayBill.TYPE_PAYBILL);
		paybill.setAccountsPayable(getClientCompany()
				.getAccountsPayableAccount());
		ClientVendor vendor = get(VENDOR).getValue();
		ClientAccount payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		ClientFinanceDate dueDate = get(FILTER_BY_DUE_ON_BEFORE).getValue();
		String number = get(NUMBER).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		paybill.setVendor(vendor);
		paybill.setPayFrom(payFrom);
		paybill.setPaymentMethod(paymentMethod);

		paybill.setBillDueOnOrBefore(dueDate);
		paybill.setNumber(number);
		paybill.setDate(date.getDate());
		String memo = get(MEMO).getValue();
		paybill.setMemo(memo);
		if (getClientCompany().getPreferences().isTDSEnabled()) {

			ClientTAXItem taxItem = getClientCompany().getTAXItem(
					vendor.getTaxItemCode());
			if (taxItem != null) {
				paybill.setTdsTaxItem(taxItem);
			}
		}
		setTransactionPayBills(paybill);
		Double totalCredits = 0D;
		for (ClientCreditsAndPayments credit : getCreditsAndPayments(vendor)) {
			totalCredits += credit.getBalance();
		}
		paybill.setUnUsedCredits(totalCredits);
		adjustAmountAndEndingBalance(paybill);
		create(paybill, context);
	}

	private void updateValue(ClientTransactionPayBill obj) {
		updateAmountDue(obj);
		updateTotalPayment(obj);
	}

	private void updateTotalPayment(ClientTransactionPayBill obj) {
		ClientTransaction transactionObject = getTransactionObject();
		transactionObject.setTotal(0.0);
		ClientVendor vendor = get(VENDOR).getValue();
		ArrayList<ClientTransactionPayBill> list = get(PAY_BILL_LIST)
				.getValue();
		double payment = 0.0;
		for (ClientTransactionPayBill rec : list) {
			if (getClientCompany().getPreferences().isTDSEnabled()) {
				ClientTAXItem taxItem = getClientCompany().getTAXItem(
						vendor.getTaxItemCode());
				if (taxItem != null)
					payment = obj.getOriginalAmount()
							* (taxItem.getTaxRate() / 100);
				payment = obj.getOriginalAmount() - payment;
				obj.setPayment(payment);
			}

			transactionObject.setTotal(transactionObject.getTotal()
					+ rec.getPayment());
		}
		adjustPaymentValue(obj);
	}

	private ClientTransaction getTransactionObject() {
		ClientPayBill paybill = new ClientPayBill();
		paybill.setType(ClientTransaction.TYPE_PAY_BILL);
		paybill.setPayBillType(ClientPayBill.TYPE_PAYBILL);
		paybill.setAccountsPayable(getClientCompany()
				.getAccountsPayableAccount());
		ClientVendor vendor = get(VENDOR).getValue();
		ClientAccount payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		ClientFinanceDate dueDate = get(FILTER_BY_DUE_ON_BEFORE).getValue();
		String number = get(NUMBER).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		paybill.setVendor(vendor);
		paybill.setPayFrom(payFrom);
		paybill.setPaymentMethod(paymentMethod);

		paybill.setBillDueOnOrBefore(dueDate);
		paybill.setNumber(number);
		paybill.setDate(date.getDate());
		String memo = get(MEMO).getValue();
		paybill.setMemo(memo);
		if (getClientCompany().getPreferences().isTDSEnabled()) {

			ClientTAXItem taxItem = getClientCompany().getTAXItem(
					vendor.getTaxItemCode());
			if (taxItem != null) {
				paybill.setTdsTaxItem(taxItem);
			}
		}
		setTransactionPayBills(paybill);
		Double totalCredits = 0D;
		for (ClientCreditsAndPayments credit : getCreditsAndPayments(vendor)) {
			totalCredits += credit.getBalance();
		}
		paybill.setUnUsedCredits(totalCredits);
		adjustAmountAndEndingBalance(paybill);
		return paybill;
	}

	private void adjustPaymentValue(ClientTransactionPayBill rec) {
		Double cashDiscount = rec.getCashDiscount();
		Double credit = rec.getAppliedCredits();
		(rec).setCashDiscount(cashDiscount);
		(rec).setAppliedCredits(credit);
		updateAmountDue(rec);
	}

	private void updateAmountDue(ClientTransactionPayBill item) {
		ClientVendor vendor = get(VENDOR).getValue();
		double totalValue = item.getCashDiscount() + item.getAppliedCredits()
				+ item.getPayment();

		if (!DecimalUtil.isGreaterThan(totalValue, item.getAmountDue())) {
			if (getClientCompany().getCountryPreferences().isTDSAvailable()
					&& getClientCompany().getPreferences().isTDSEnabled()) {
				ClientTAXItem taxItem = getClientCompany().getTAXItem(
						vendor.getTaxItemCode());
				if (taxItem != null)
					item.setDummyDue(item.getAmountDue()
							- (totalValue + (taxItem.getTaxRate() / 100 * item
									.getOriginalAmount())));
			} else {
				item.setDummyDue(item.getAmountDue() - totalValue);
			}
		} else {
			item.setDummyDue(0.0);
		}
	}

	private ArrayList<ClientCreditsAndPayments> getCreditsAndPayments(
			ClientVendor vendor) {
		return getCreditsAndPayments(vendor.getID());
	}

	private void setTransactionPayBills(ClientPayBill paybill) {
		List<ClientTransactionPayBill> selectedRecords = get(PAY_BILL_LIST)
				.getValue();
		ArrayList<PayBillTransactionList> paybillTransactionList = getTransactionPayBills(paybill
				.getVendor());
		List<ClientTransactionPayBill> transactionPayBill = new ArrayList<ClientTransactionPayBill>();
		for (int i = 0; i < selectedRecords.size(); i++) {
			ClientTransactionPayBill tpbRecord = selectedRecords.get(i);
			PayBillTransactionList payBillTX = paybillTransactionList.get(i);

			if (payBillTX.getType() == ClientTransaction.TYPE_ENTER_BILL) {
				tpbRecord.setEnterBill(payBillTX.getTransactionId());
			} else if (payBillTX.getType() == ClientTransaction.TYPE_MAKE_DEPOSIT) {
				tpbRecord.setTransactionMakeDeposit(payBillTX
						.getTransactionId());
			} else if (payBillTX.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
				tpbRecord.setJournalEntry(payBillTX.getTransactionId());
			}
			tpbRecord.setAccountsPayable(getClientCompany()
					.getAccountsPayableAccount());
			tpbRecord.setPayBill(paybill);
			ClientVendor vendor = getClientCompany().getVendor(
					paybill.getVendor());
			if (getClientCompany().getPreferences().isTDSEnabled()) {

				ClientTAXItem taxItem = getClientCompany().getTAXItem(
						vendor.getTaxItemCode());

				if (taxItem != null) {
					double tds = taxItem.getTaxRate() / 100
							* tpbRecord.getPayment();
					tpbRecord.setTdsAmount(tds);
				}

			}
			if (tpbRecord.getTempCredits() != null)
				tpbRecord.getTempCredits().clear();

			transactionPayBill.add(tpbRecord);
		}
		paybill.setTransactionPayBill(transactionPayBill);
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, OPTIONAL);
		}
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

		selection = context.getSelection(VALUES);
		Result result = dateOptionalRequirement(context, list, DATE,
				getConstants().date(),
				getMessages().pleaseEnter(getConstants().date()), selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				getConstants().number(),
				getMessages().pleaseEnter(getConstants().number()));
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				getConstants().memo(),
				getMessages().pleaseEnter(getConstants().memo()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().finishToCreate(getConstants().payBill()));
		actions.add(finish);

		return makeResult;
	}

	protected List<ClientTransactionPayBill> filterGrid() {
		ArrayList<PayBillTransactionList> filterList = new ArrayList<PayBillTransactionList>();
		ArrayList<PayBillTransactionList> tempList = new ArrayList<PayBillTransactionList>();
		ClientVendor vendor = get(VENDOR).getValue();
		filterList.addAll(getTransactionPayBills(vendor.getID()));

		if (get(FILTER_BY_DUE_ON_BEFORE).getValue() != null) {
			ClientFinanceDate date = get(FILTER_BY_DUE_ON_BEFORE).getValue();
			ClientFinanceDate dueDateOnOrBefore = date;
			for (PayBillTransactionList cont : filterList) {
				if (cont.getDueDate().before(dueDateOnOrBefore)
						|| cont.getDueDate().equals(dueDateOnOrBefore))
					tempList.add(cont);

			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		if (vendor != null) {
			for (PayBillTransactionList cont : filterList) {
				if (vendor.getName().toString()
						.equalsIgnoreCase(cont.getVendorName().toString())) {

					tempList.add(cont);
				}
			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		List<ClientTransactionPayBill> records = new ArrayList<ClientTransactionPayBill>();
		for (PayBillTransactionList cont : filterList) {
			ClientTransactionPayBill record = new ClientTransactionPayBill();

			record.setAmountDue(cont.getAmountDue());
			record.setDummyDue(cont.getAmountDue());

			record.setBillNumber(cont.getBillNumber());

			record.setCashDiscount(cont.getCashDiscount());

			record.setAppliedCredits(cont.getCredits());
			if (cont.getDiscountDate() != null)
				record.setDiscountDate(cont.getDiscountDate().getDate());
			if (cont.getDueDate() != null)
				record.setDueDate(cont.getDueDate().getDate());

			record.setOriginalAmount(cont.getOriginalAmount());

			record.setPayment(cont.getPayment());
			records.add(record);
		}
		return records;
	}

	private void adjustAmountAndEndingBalance(ClientPayBill transaction) {
		List<ClientTransactionPayBill> selectedRecords = get(PAY_BILL_LIST)
				.getValue();
		double toBeSetAmount = 0.0;
		for (ClientTransactionPayBill rec : selectedRecords) {
			toBeSetAmount += rec.getPayment();
		}
		if (transaction != null) {
			transaction.setTotal(toBeSetAmount);

			if (get(VENDOR).getValue() != null) {
				double toBeSetEndingBalance = 0.0;
				ClientAccount payFromAccount = get(PAY_FROM).getValue();
				if (payFromAccount.isIncrease())
					toBeSetEndingBalance = payFromAccount.getTotalBalance()
							+ transaction.getTotal();
				else
					toBeSetEndingBalance = payFromAccount.getTotalBalance()
							- transaction.getTotal();
				transaction.setEndingBalance(toBeSetEndingBalance);
			}
		}

	}
}
