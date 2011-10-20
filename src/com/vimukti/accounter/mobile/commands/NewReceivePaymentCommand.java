package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionReceivePaymentTable.TempCredit;

public class NewReceivePaymentCommand extends AbstractTransactionCommand {
	private static final String AMOUNT_RECEIVED = "amountreceived";
	private static final String DEPOSITSANDTRANSFERS = "DepositOrTransferTo";
	private static final String DUE_DATE = "due Date";
	private static final String INVOICE = "invoice";
	private static final String INVOICE_AMOUNT = "invoice amount";
	private static final String DISCOUNT_DATE = "Discount Date";
	private static final String CASH_DISCOUNT = "Cash discount";
	private static final String WRITE_OFF = "Write off";
	private static final String APPLIED_CREDITS = "Applied Credits";
	private static final String AMOUNT_DUE = "Applied Credits";
	private static final String PAYMENT = "Payment";
	private static final String TRANSACTION_RECEIVE_PAYMENT_PROCESS = "transreceivepaymentproc";
	private static final String OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR = "oldtransreceivepaymentproc";
	private static final String TRANSACTION_RECEIVE_PAYMENT_ATTR = "receivepaymentattr";
	private static final String TRANSACTION_RECEIVE_PAYMENT_DETAILS = "receivepaymentdetails";
	private static final String CHECK_NUMBER = "checknum";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CUSTOMER, false, true));
		list.add(new Requirement(DEPOSITSANDTRANSFERS, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(AMOUNT_RECEIVED, true, true));
		list.add(new ObjectListRequirement("transactions", false, true) {
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
		list.add(new Requirement(CHECK_NUMBER, true, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues();
		Result result = null;
		Result makeResult = context.makeResult();
		ResultList actions = new ResultList(ACTIONS);
		ResultList list = new ResultList("values");
		makeResult.add(list);
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(TRANSACTION_RECEIVE_PAYMENT_PROCESS)) {
				ClientTransactionReceivePayment transactionReceivePayment = (ClientTransactionReceivePayment) context
						.getAttribute(OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR);
				result = clientTransactionReceivePaymentProcess(context,
						transactionReceivePayment);
				if (result != null) {
					return result;
				}
			}
		}

		result = customerRequirement(context, list, CUSTOMER);
		if (result != null) {
			return result;
		}

		if (context.getSelection("customers") != null) {
			get("transactions").setValue(
					new ArrayList<ClientTransactionReceivePayment>());
		}

		result = receivePaymentsRequirement(context, makeResult, actions);
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context, list, PAYMENT_METHOD);
		if (result != null) {
			return result;
		}
		result = accountRequirement(context, list, DEPOSITSANDTRANSFERS,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount acc) {
						return Arrays.asList(
								ClientAccount.TYPE_BANK,
								// ClientAccount.TYPE_CASH,
								ClientAccount.TYPE_CREDIT_CARD,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_FIXED_ASSET).contains(
								acc.getType())
								&& acc.getID() != getClientCompany()
										.getAccountsReceivableAccountId();
					}
				});
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		result = new Result("Receive Payment was created successfully");
		return result;
	}

	private void updatePayment(ClientTransactionReceivePayment payment) {
		payment.setPayment(0);
		double paymentValue = payment.getAmountDue() - getTotalValue(payment);
		payment.setPayment(paymentValue);
		updateAmountDue(payment);
	}

	private void updateAmountDue(ClientTransactionReceivePayment item) {
		double totalValue = item.getCashDiscount() + item.getWriteOff()
				+ item.getAppliedCredits() + item.getPayment();

		if (!DecimalUtil.isGreaterThan(totalValue, item.getAmountDue())) {
			if (!DecimalUtil.isLessThan(item.getPayment(), 0.00))
				item.setDummyDue(item.getAmountDue() - totalValue);
			else
				item.setDummyDue(item.getAmountDue() + item.getPayment()
						- totalValue);

		}
	}

	private double getTotalValue(ClientTransactionReceivePayment payment) {
		double totalValue = payment.getCashDiscount() + payment.getWriteOff()
				+ payment.getAppliedCredits() + payment.getPayment();
		return totalValue;
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new Date());
		get(NUMBER).setDefaultValue("1");
		get(MEMO).setDefaultValue("");
		get(AMOUNT_RECEIVED).setDefaultValue(new Double(0));
		get(CHECK_NUMBER).setDefaultValue("1");
	}

	private void completeProcess(Context context) {
		ClientReceivePayment payment = new ClientReceivePayment();

		ClientCustomer customer = get(CUSTOMER).getValue();
		payment.setCustomer(customer);
		payment.setType(ClientTransaction.TYPE_RECEIVE_PAYMENT);
		double amount = get(AMOUNT_RECEIVED).getValue();
		payment.setAmount(amount);
		payment.setCustomerBalance(customer.getBalance());
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		payment.setPaymentMethod(paymentMethod);

		Date date = get(DATE).getValue();
		payment.setDate(new ClientFinanceDate(date).getDate());

		String receivePaymentNum = get(NUMBER).getValue();
		payment.setNumber(receivePaymentNum);

		ClientAccount account = get(DEPOSITSANDTRANSFERS).getValue();
		payment.setDepositIn(account);
		List<ClientTransactionReceivePayment> list = get("transactions")
				.getValue();
		payment.setTransactionReceivePayment(list);

		String memo = get(MEMO).getValue();
		payment.setMemo(memo);

		String checkNumber = get(CHECK_NUMBER).getValue();
		payment.setCheckNumber(checkNumber);

		recalculateGridAmounts(payment);

		create(payment, context);
	}

	/**
	 * create Optional requirement
	 * 
	 * @param context
	 * @param makeResult
	 * @param actions2
	 * @param list2
	 * @return {@link Result}
	 */
	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
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

		selection = context.getSelection("values");

		Result result = amountOptionalRequirement(context, list, selection,
				AMOUNT_RECEIVED, "Enter the Amount received");
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DATE, "Enter date",
				selection);
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, NUMBER,
				"Enter Receive Payment Number");
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection,
				CHECK_NUMBER, "Enter Check Number");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter memo");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create receive payment.");
		actions.add(finish);
		return makeResult;
	}

	private Result addTransactionsOfSelectedCustomer(Context context) {
		Result result = context.makeResult();
		List<ClientTransactionReceivePayment> transactionRecievePayments;
		try {
			transactionRecievePayments = getTransactionRecievePayments();
		} catch (AccounterException e) {
			e.printStackTrace();
			transactionRecievePayments = new ArrayList<ClientTransactionReceivePayment>();
		}

		ResultList list = new ResultList("receivepayments");
		ClientTransactionReceivePayment last = (ClientTransactionReceivePayment) context
				.getLast(RequirementType.TRANSACTION_RECEIVE_PAYMENT);
		int num = 0;
		if (last != null) {
			list.add(creatReceivePaymentRecord(last));
			num++;
		}
		Requirement itemsReq = get("transactions");
		List<ClientTransactionReceivePayment> transItems = itemsReq.getValue();
		if (transItems == null) {
			transItems = new ArrayList<ClientTransactionReceivePayment>();
		}
		List<ClientTransactionReceivePayment> availableItems = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment transactionItem : transItems) {
			availableItems.add(transactionItem);
		}
		for (ClientTransactionReceivePayment item : transactionRecievePayments) {
			if (item != last && !availableItems.contains(item)) {
				list.add(creatReceivePaymentRecord(item));
				num++;
			}
			if (num == ITEMS_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add("Slect Receive Payment(s).");
		} else {
			result.add("You don't have any receive payments.");
		}
		result.add(list);
		return result;
	}

	private Record creatReceivePaymentRecord(
			ClientTransactionReceivePayment last) {
		Record record = new Record(last);
		record.add("", "Due Date");
		record.add("", last.getDueDate());
		record.add("", "Invoice");
		record.add("", last.getInvoice());
		record.add("", "Invoice amount");
		record.add("", last.getInvoiceAmount());
		record.add("", "Amount Due");
		record.add("", last.getAmountDue());
		record.add("", "Paymens");
		record.add("", last.getPayment());
		return record;
	}

	private Result clientTransactionReceivePaymentProcess(Context context,
			ClientTransactionReceivePayment transactionReceivePayment) {
		context.setAttribute(PROCESS_ATTR, TRANSACTION_RECEIVE_PAYMENT_PROCESS);
		context.setAttribute(OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR,
				transactionReceivePayment);
		Result result = context.makeResult();
		String lineAttr = (String) context
				.getAttribute(TRANSACTION_RECEIVE_PAYMENT_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(TRANSACTION_RECEIVE_PAYMENT_ATTR);
			if (lineAttr.equals("Payment")) {
				if (context.getDouble() != null) {
					transactionReceivePayment.setPayment(context.getDouble());
				} else if (context.getInteger() != null) {
					transactionReceivePayment.setPayment(context.getInteger());
				}
			}
		} else {
			Object selection = context
					.getSelection(TRANSACTION_RECEIVE_PAYMENT_DETAILS);
			if (selection != null) {
				if (selection.equals("Payment")) {
					context.setAttribute(TRANSACTION_RECEIVE_PAYMENT_ATTR,
							"Payment");
					return amount(context, "Enter Payment",
							transactionReceivePayment.getPayment());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_RECEIVE_PAYMENT) {
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_RECEIVE_PAYMENT) {
					context.removeAttribute(PROCESS_ATTR);
					Requirement itemsReq = get("transactions");
					List<ClientReceivePayment> transItems = itemsReq.getValue();
					transItems.remove(transactionReceivePayment);
					context.removeAttribute(OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR);
					resetValue(transactionReceivePayment);
					return null;
				}
			}
		}
		ResultList list = new ResultList(TRANSACTION_RECEIVE_PAYMENT_DETAILS);

		Record receivePaymentRecord;
		result.add("Due Date :" + transactionReceivePayment.getDueDate());

		result.add("Number : " + transactionReceivePayment.getNumber());

		result.add("Invoice Amount : "
				+ transactionReceivePayment.getInvoiceAmount());

		result.add("Amount Due : " + transactionReceivePayment.getAmountDue());

		result.add("Dummy Due : " + transactionReceivePayment.getDummyDue());

		result.add("Cash Discount  : "
				+ transactionReceivePayment.getCashDiscount());

		result.add("Write Off : " + transactionReceivePayment.getWriteOff());

		result.add("Applied Credits : "
				+ transactionReceivePayment.getAppliedCredits());

		receivePaymentRecord = new Record("Payment");
		list.add(receivePaymentRecord);
		receivePaymentRecord.add("", "Payment");
		receivePaymentRecord.add("", transactionReceivePayment.getPayment());
		result.add(list);
		ResultList actions = new ResultList(ACTIONS);
		receivePaymentRecord = new Record(ActionNames.DELETE_RECEIVE_PAYMENT);
		receivePaymentRecord.add("", "Delete");
		actions.add(receivePaymentRecord);
		receivePaymentRecord = new Record(ActionNames.FINISH_RECEIVE_PAYMENT);
		receivePaymentRecord.add("", "Finish");
		actions.add(receivePaymentRecord);
		result.add(actions);
		updatePayment(transactionReceivePayment);
		return result;
	}

	private void calculateUnusedCredits(ClientReceivePayment payment) {
		ClientCustomer customer = get(CUSTOMER).getValue();
		ArrayList<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments = getCustomerCreditsAndPayments(customer
				.getID());
		Double totalCredits = 0D;
		for (ClientCreditsAndPayments credit : updatedCustomerCreditsAndPayments) {

			totalCredits += credit.getBalance();
		}
		payment.setUnUsedCredits(totalCredits);
	}

	private ArrayList<ClientTransactionReceivePayment> getTransactionRecievePayments()
			throws AccounterException {
		ClientCustomer customer = get(CUSTOMER).getValue();
		ArrayList<ReceivePaymentTransactionList> transactionReceivePayments = getTransactionReceivePayments(
				customer.getID(), new Date().getTime());

		ArrayList<ClientTransactionReceivePayment> records = new ArrayList<ClientTransactionReceivePayment>();
		for (ReceivePaymentTransactionList receivePaymentTransaction : transactionReceivePayments) {
			ClientTransactionReceivePayment record = new ClientTransactionReceivePayment();
			record.setDueDate(receivePaymentTransaction.getDueDate() != null ? receivePaymentTransaction
					.getDueDate().getDate() : 0);

			record.setNumber(receivePaymentTransaction.getNumber());

			record.setInvoiceAmount(receivePaymentTransaction
					.getInvoiceAmount());
			record.setInvoice(receivePaymentTransaction.getTransactionId());
			record.setAmountDue(receivePaymentTransaction.getAmountDue());

			record.setDummyDue(receivePaymentTransaction.getAmountDue());

			record.setDiscountDate(receivePaymentTransaction.getDiscountDate() != null ? receivePaymentTransaction
					.getDiscountDate().getDate() : 0);

			record.setCashDiscount(receivePaymentTransaction.getCashDiscount());

			record.setWriteOff(receivePaymentTransaction.getWriteOff());

			record.setAppliedCredits(receivePaymentTransaction
					.getAppliedCredits());
			record.setPayment(0);

			if (receivePaymentTransaction.getType() == ClientTransaction.TYPE_INVOICE) {
				record.isInvoice = true;
				record.setInvoice(receivePaymentTransaction.getTransactionId());
			} else if (receivePaymentTransaction.getType() == ClientTransaction.TYPE_CUSTOMER_REFUNDS) {
				record.isInvoice = false;
				record.setCustomerRefund(receivePaymentTransaction
						.getTransactionId());
			} else if (receivePaymentTransaction.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
				record.isInvoice = false;
				record.setJournalEntry(receivePaymentTransaction
						.getTransactionId());
			}
			records.add(record);
		}
		return records;
	}

	private void recalculateGridAmounts(ClientReceivePayment payment) {
		payment.setTotal(getGridTotal());
		double toBeSetAmount = 0.0;
		for (ClientTransactionReceivePayment receivePayment : payment
				.getTransactionReceivePayment()) {
			toBeSetAmount += receivePayment.getPayment();
		}
		payment.setAmount(toBeSetAmount);
		payment.setUnUsedPayments(payment.getAmount() - payment.getTotal());
		setUnusedPayments(payment.getUnUsedPayments(), payment);
		calculateUnusedCredits(payment);
	}

	private void setUnusedPayments(Double unusedAmounts,
			ClientReceivePayment payment) {
		if (unusedAmounts == null)
			unusedAmounts = 0.0D;
		payment.setUnUsedPayments(unusedAmounts);
	}

	public Double getGridTotal() {
		Double total = 0.0D;
		ArrayList<ClientTransactionReceivePayment> records = get("transactions")
				.getValue();
		for (ClientTransactionReceivePayment record : records) {
			total += record.getPayment();
		}
		return total;
	}

	private Result receivePaymentsRequirement(Context context, Result result,
			ResultList actions) {
		Requirement receivePaymentReq = get("transactions");
		List<ClientTransactionReceivePayment> transactionReceivePayments = context
				.getSelections("receivepayments");
		List<ClientTransactionReceivePayment> transReceivePayments = receivePaymentReq
				.getValue();
		if (transactionReceivePayments != null
				&& transactionReceivePayments.size() > 0) {
			for (ClientTransactionReceivePayment payment : transactionReceivePayments) {
				updatePayment(payment);
				transReceivePayments.add(payment);
			}
		}
		if (transReceivePayments.size() == 0) {
			return addTransactionsOfSelectedCustomer(context);
		}

		Object selection = context.getSelection("transactions");
		if (selection != null) {
			result = clientTransactionReceivePaymentProcess(context,
					(ClientTransactionReceivePayment) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_PAYMENTS) {
			return addTransactionsOfSelectedCustomer(context);
		}
		result.add("Received Payments:-");
		ResultList itemsList = new ResultList("transactions");
		for (ClientTransactionReceivePayment item : transReceivePayments) {
			itemsList.add(creatReceivePaymentRecord(item));
		}
		result.add(itemsList);
		Record moreItems = new Record(ActionNames.ADD_MORE_PAYMENTS);
		moreItems.add("", "Add more payments");
		actions.add(moreItems);
		return null;
	}

	public void resetValue(ClientTransactionReceivePayment obj) {
		if (obj.isCreditsApplied()) {
			ClientCustomer customer = get(CUSTOMER).getValue();
			int size = getCustomerCreditsAndPayments(customer.getID()).size();
			Map<Integer, Object> toBeRvrtMap = obj.getTempCredits();
			for (int i = 0; i < size; i++) {
				if (toBeRvrtMap.containsKey(i)) {
					TempCredit toBeAddCr = (TempCredit) toBeRvrtMap.get(i);
					List<ClientTransactionReceivePayment> selectedRecords = get(
							"transactions").getValue();
					if (selectedRecords.size() != 0) {
						for (int j = 0; j < selectedRecords.size(); j++) {
							Map<Integer, Object> rcvCrsMap = selectedRecords
									.get(j).getTempCredits();
							if (rcvCrsMap.containsKey(i)) {
								TempCredit chngCrd = (TempCredit) rcvCrsMap
										.get(i);
								chngCrd.setRemainingBalance(chngCrd
										.getRemainingBalance()
										+ toBeAddCr.getAmountToUse());
							}
						}
					}
				}
				obj.setCreditsApplied(false);
			}
			obj.setPayment(0.0d);
			obj.setCashDiscount(0.0d);
			obj.setWriteOff(0.0d);
			obj.setAppliedCredits(0.0d);
			obj.setDummyDue(obj.getAmountDue());
		}
	}
}
