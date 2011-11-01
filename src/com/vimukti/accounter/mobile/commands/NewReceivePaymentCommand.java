package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.ReceivePaymentTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class NewReceivePaymentCommand extends NewAbstractTransactionCommand {
	private static final String AMOUNT_RECEIVED = "amountreceived";
	private static final String DEPOSITSANDTRANSFERS = "DepositOrTransferTo";
	private static final String CHECK_NUMBER = "checknum";
	private static final String TRANSACTIONS = "transactions";
	private static final String DEPOSIT_OR_TRANSFER_TO = "depositOrTransferTo";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages()
				.pleaseEnterName(Global.get().Customer()), Global.get()
				.Customer(), false, true, null) {

			@Override
			protected List<ClientCustomer> getLists(Context context) {
				return context.getClientCompany().getCustomers();
			}
		});

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_TO, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Account()), getMessages()
				.depositAccount(Global.get().Account()), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().depositAccount(Global.get().Account()));
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount acc) {
						return Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_CREDIT_CARD,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_FIXED_ASSET).contains(
								acc.getType())
								&& acc.getID() != getClientCompany()
										.getAccountsReceivableAccountId();
					}
				}, context.getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getConstants().paymentMethod()),
				getConstants().paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getConstants().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				String payVatMethodArray[] = new String[] {
						getConstants().cash(), getConstants().check() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AmountRequirement(AMOUNT_RECEIVED, getMessages()
				.pleaseEnter(getConstants().amountReceived()), getConstants()
				.amountReceived(), true, true));

		list.add(new ReceivePaymentTableRequirement(TRANSACTIONS, getMessages()
				.pleaseSelect(getConstants().dueForPayment()), getConstants()
				.dueForPayment()) {

			@Override
			protected List<ClientTransactionReceivePayment> getList() {
				return getTransactionRecievePayments(getClientCompany());
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new NumberRequirement(CHECK_NUMBER, getMessages().pleaseEnter(
				getConstants().checkNo()), getConstants().checkNo(), true, true));
	}

	// @Override
	// public Result run(Context context) {
	// setDefaultValues();
	// Result result = null;
	// Result makeResult = context.makeResult();
	// makeResult.add(getMessages().readyToCreate(
	// getConstants().receivePayment()));
	// ResultList actions = new ResultList(ACTIONS);
	// ResultList list = new ResultList(VALUES);
	// makeResult.add(list);
	// String process = (String) context.getAttribute(PROCESS_ATTR);
	// if (process != null) {
	// if (process.equals(TRANSACTION_RECEIVE_PAYMENT_PROCESS)) {
	// ClientTransactionReceivePayment transactionReceivePayment =
	// (ClientTransactionReceivePayment) context
	// .getAttribute(OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR);
	// result = clientTransactionReceivePaymentProcess(context,
	// transactionReceivePayment);
	// if (result != null) {
	// return result;
	// }
	// }
	// }
	//
	// result = customerRequirement(context, list, CUSTOMER, Global.get()
	// .customer());
	// if (result != null) {
	// return result;
	// }
	//
	// if (context.getSelection(CUSTOMERS) != null) {
	// get(TRANSACTIONS).setValue(
	// new ArrayList<ClientTransactionReceivePayment>());
	// }
	//
	// result = receivePaymentsRequirement(context, makeResult, actions);
	// if (result != null) {
	// return result;
	// }
	//
	// result = paymentMethodRequirement(context, list, PAYMENT_METHOD,
	// getConstants().paymentMethod());
	// if (result != null) {
	// return result;
	// }
	// result = accountRequirement(context, list, DEPOSITSANDTRANSFERS,
	// getConstants().depositTransferFunds(),
	// new ListFilter<ClientAccount>() {
	//
	// @Override
	// public boolean filter(ClientAccount acc) {
	// return Arrays.asList(ClientAccount.TYPE_BANK,
	// ClientAccount.TYPE_CREDIT_CARD,
	// ClientAccount.TYPE_OTHER_CURRENT_ASSET,
	// ClientAccount.TYPE_FIXED_ASSET).contains(
	// acc.getType())
	// && acc.getID() != getClientCompany()
	// .getAccountsReceivableAccountId();
	// }
	// });
	// if (result != null) {
	// return result;
	// }
	// makeResult.add(actions);
	// result = createOptionalRequirement(context, list, actions, makeResult);
	// if (result != null) {
	// return result;
	// }
	// completeProcess(context);
	// markDone();
	// result = new Result(getMessages().createSuccessfully(
	// getConstants().receivePayment()));
	// return result;
	// }

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
		get(DATE).setDefaultValue(new ClientFinanceDate());
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

		ClientFinanceDate date = get(DATE).getValue();
		payment.setDate(date.getDate());

		String receivePaymentNum = get(NUMBER).getValue();
		payment.setNumber(receivePaymentNum);

		ClientAccount account = get(DEPOSITSANDTRANSFERS).getValue();
		payment.setDepositIn(account);
		List<ClientTransactionReceivePayment> list = get(TRANSACTIONS)
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
	 * @param clientCompany
	 * @param makeResult
	 * @param actions2
	 * @param list2
	 * @return {@link Result}
	 */
	// private Result createOptionalRequirement(Context context, ResultList
	// list,
	// ResultList actions, Result makeResult) {
	// if (context.getAttribute(INPUT_ATTR) == null) {
	// context.setAttribute(INPUT_ATTR, OPTIONAL);
	// }
	// Object selection = context.getSelection(ACTIONS);
	// if (selection != null) {
	// ActionNames actionName = (ActionNames) selection;
	// switch (actionName) {
	// case FINISH:
	// context.removeAttribute(INPUT_ATTR);
	// return null;
	// default:
	// break;
	// }
	// }
	//
	// selection = context.getSelection(VALUES);
	//
	// Result result = amountOptionalRequirement(context, list, selection,
	// AMOUNT_RECEIVED,
	// getMessages().pleaseEnter(getConstants().amount()),
	// getConstants().amount());
	// if (result != null) {
	// return result;
	// }
	//
	// result = dateOptionalRequirement(context, list, DATE, getConstants()
	// .date(), getMessages().pleaseEnter(getConstants().date()),
	// selection);
	// if (result != null) {
	// return result;
	// }
	// result = numberOptionalRequirement(context, list, selection, NUMBER,
	// getConstants().number(),
	// getMessages().pleaseEnter(getConstants().number()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberOptionalRequirement(context, list, selection,
	// CHECK_NUMBER, getConstants().chequeNo(), getMessages()
	// .pleaseEnter(getConstants().chequeNo()));
	// if (result != null) {
	// return result;
	// }
	// result = stringOptionalRequirement(context, list, selection, MEMO,
	// getConstants().memo(),
	// getMessages().pleaseEnter(getConstants().memo()));
	// if (result != null) {
	// return result;
	// }
	//
	// Record finish = new Record(ActionNames.FINISH);
	// finish.add("",
	// getMessages().finishToCreate(getConstants().receivePayment()));
	// actions.add(finish);
	// return makeResult;
	// }

	// private Result addTransactionsOfSelectedCustomer(Context context) {
	// Result result = context.makeResult();
	// List<ClientTransactionReceivePayment> transactionRecievePayments;
	// transactionRecievePayments = getTransactionRecievePayments();
	//
	// ResultList list = new ResultList("receivepayments");
	// ClientTransactionReceivePayment last = (ClientTransactionReceivePayment)
	// context
	// .getLast(RequirementType.TRANSACTION_RECEIVE_PAYMENT);
	// int num = 0;
	// if (last != null) {
	// list.add(creatReceivePaymentRecord(last));
	// num++;
	// }
	// Requirement itemsReq = get(TRANSACTIONS);
	// List<ClientTransactionReceivePayment> transItems = itemsReq.getValue();
	// if (transItems == null) {
	// transItems = new ArrayList<ClientTransactionReceivePayment>();
	// }
	// List<ClientTransactionReceivePayment> availableItems = new
	// ArrayList<ClientTransactionReceivePayment>();
	// for (ClientTransactionReceivePayment transactionItem : transItems) {
	// availableItems.add(transactionItem);
	// }
	// for (ClientTransactionReceivePayment item : transactionRecievePayments) {
	// if (item != last && !availableItems.contains(item)) {
	// list.add(creatReceivePaymentRecord(item));
	// num++;
	// }
	// if (num == ITEMS_TO_SHOW) {
	// break;
	// }
	// }
	// list.setMultiSelection(true);
	// if (list.size() > 0) {
	// result.add(getMessages().pleaseSelect(
	// getConstants().receivePayments()));
	// } else {
	// result.add(getMessages().youDontHaveAny(
	// getConstants().receivePayments()));
	// }
	// result.add(list);
	// return result;
	// }

	// private Record creatReceivePaymentRecord(
	// ClientTransactionReceivePayment last) {
	// Record record = new Record(last);
	// record.add("", getConstants().dueDate());
	// record.add("",
	// getDateAsString(new ClientFinanceDate(last.getDueDate())));
	// record.add("", getConstants().invoice());
	// record.add("", last.getInvoice());
	// record.add("", getConstants().invoiceAmount());
	// record.add("", last.getInvoiceAmount());
	// record.add("", getConstants().amountDue());
	// record.add("", last.getAmountDue());
	// record.add("", getConstants().payments());
	// record.add("", last.getPayment());
	// return record;
	// }

	// private Result clientTransactionReceivePaymentProcess(Context context,
	// ClientTransactionReceivePayment transactionReceivePayment) {
	// context.setAttribute(PROCESS_ATTR, TRANSACTION_RECEIVE_PAYMENT_PROCESS);
	// context.setAttribute(OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR,
	// transactionReceivePayment);
	// Result result = context.makeResult();
	// String lineAttr = (String) context
	// .getAttribute(TRANSACTION_RECEIVE_PAYMENT_ATTR);
	// if (lineAttr != null) {
	// context.removeAttribute(TRANSACTION_RECEIVE_PAYMENT_ATTR);
	// if (lineAttr.equals("Payment")) {
	// if (context.getDouble() != null) {
	// transactionReceivePayment.setPayment(context.getDouble());
	// } else if (context.getInteger() != null) {
	// transactionReceivePayment.setPayment(context.getInteger());
	// }
	// }
	// } else {
	// Object selection = context
	// .getSelection(TRANSACTION_RECEIVE_PAYMENT_DETAILS);
	// if (selection != null) {
	// if (selection.equals("Payment")) {
	// context.setAttribute(TRANSACTION_RECEIVE_PAYMENT_ATTR,
	// "Payment");
	// return amount(
	// context,
	// getMessages().pleaseEnter(getConstants().payment()),
	// transactionReceivePayment.getPayment());
	// }
	// } else {
	// selection = context.getSelection(ACTIONS);
	// if (selection == ActionNames.FINISH_RECEIVE_PAYMENT) {
	// context.removeAttribute(PROCESS_ATTR);
	// context.removeAttribute(OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR);
	// return null;
	// } else if (selection == ActionNames.DELETE_RECEIVE_PAYMENT) {
	// context.removeAttribute(PROCESS_ATTR);
	// Requirement itemsReq = get(TRANSACTIONS);
	// List<ClientReceivePayment> transItems = itemsReq.getValue();
	// transItems.remove(transactionReceivePayment);
	// context.removeAttribute(OLD_TRANSACTION_RECEIVE_PAYMENT_ATTR);
	// resetValue(transactionReceivePayment);
	// return null;
	// }
	// }
	// }
	// ResultList list = new ResultList(TRANSACTION_RECEIVE_PAYMENT_DETAILS);
	//
	// Record receivePaymentRecord;
	// result.add(getConstants().dueDate()
	// + " : "
	// + getDateAsString(new ClientFinanceDate(
	// transactionReceivePayment.getDueDate())));
	//
	// result.add(getConstants().number() + " : "
	// + transactionReceivePayment.getNumber());
	//
	// result.add(getConstants().invoiceAmount() + " : "
	// + transactionReceivePayment.getInvoiceAmount());
	//
	// result.add(getConstants().amountDue() + " : "
	// + transactionReceivePayment.getAmountDue());
	//
	// result.add(getConstants().dummyDue()
	// + transactionReceivePayment.getDummyDue());
	//
	// result.add(getConstants().cashDiscount() + " : "
	// + transactionReceivePayment.getCashDiscount());
	//
	// result.add(getConstants().writeOff() + " :"
	// + transactionReceivePayment.getWriteOff());
	//
	// result.add(getConstants().appliedCredits() + " : "
	// + transactionReceivePayment.getAppliedCredits());
	//
	// receivePaymentRecord = new Record("Payment");
	// list.add(receivePaymentRecord);
	// receivePaymentRecord.add("", getConstants().payment());
	// receivePaymentRecord.add("", transactionReceivePayment.getPayment());
	// result.add(list);
	// ResultList actions = new ResultList(ACTIONS);
	// receivePaymentRecord = new Record(ActionNames.DELETE_RECEIVE_PAYMENT);
	// receivePaymentRecord.add("", getConstants().delete());
	// actions.add(receivePaymentRecord);
	// receivePaymentRecord = new Record(ActionNames.FINISH_RECEIVE_PAYMENT);
	// receivePaymentRecord.add("", getConstants().finish());
	// actions.add(receivePaymentRecord);
	// result.add(actions);
	// updatePayment(transactionReceivePayment);
	// return result;
	// }

	// private void calculateUnusedCredits(ClientReceivePayment payment) {
	// ClientCustomer customer = get(CUSTOMER).getValue();
	// ArrayList<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments =
	// getCustomerCreditsAndPayments(customer
	// .getID());
	// Double totalCredits = 0D;
	// for (ClientCreditsAndPayments credit : updatedCustomerCreditsAndPayments)
	// {
	//
	// totalCredits += credit.getBalance();
	// }
	// payment.setUnUsedCredits(totalCredits);
	// }

	private ArrayList<ClientTransactionReceivePayment> getTransactionRecievePayments(
			ClientCompany clientCompany) {
		ClientCustomer customer = get(CUSTOMER).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		ArrayList<ReceivePaymentTransactionList> transactionReceivePayments;
		try {
			transactionReceivePayments = getTransactionReceivePayments(
					clientCompany, customer.getID(), date.getDate());
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

				record.setDiscountDate(receivePaymentTransaction
						.getDiscountDate() != null ? receivePaymentTransaction
						.getDiscountDate().getDate() : 0);

				record.setCashDiscount(receivePaymentTransaction
						.getCashDiscount());

				record.setWriteOff(receivePaymentTransaction.getWriteOff());

				record.setAppliedCredits(receivePaymentTransaction
						.getAppliedCredits());
				record.setPayment(0);

				if (receivePaymentTransaction.getType() == ClientTransaction.TYPE_INVOICE) {
					record.isInvoice = true;
					record.setInvoice(receivePaymentTransaction
							.getTransactionId());
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
		} catch (AccounterException e) {
			e.printStackTrace();
			return new ArrayList<ClientTransactionReceivePayment>();
		}
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
		// calculateUnusedCredits(payment);
	}

	private void setUnusedPayments(Double unusedAmounts,
			ClientReceivePayment payment) {
		if (unusedAmounts == null)
			unusedAmounts = 0.0D;
		payment.setUnUsedPayments(unusedAmounts);
	}

	public Double getGridTotal() {
		Double total = 0.0D;
		ArrayList<ClientTransactionReceivePayment> records = get(TRANSACTIONS)
				.getValue();
		for (ClientTransactionReceivePayment record : records) {
			total += record.getPayment();
		}
		return total;
	}

	// private Result receivePaymentsRequirement(Context context, Result result,
	// ResultList actions) {
	// Requirement receivePaymentReq = get(TRANSACTIONS);
	// List<ClientTransactionReceivePayment> transactionReceivePayments =
	// context
	// .getSelections("receivepayments");
	// List<ClientTransactionReceivePayment> transReceivePayments =
	// receivePaymentReq
	// .getValue();
	// if (transactionReceivePayments != null
	// && transactionReceivePayments.size() > 0) {
	// for (ClientTransactionReceivePayment payment :
	// transactionReceivePayments) {
	// updatePayment(payment);
	// transReceivePayments.add(payment);
	// }
	// }
	// if (transReceivePayments.size() == 0) {
	// return addTransactionsOfSelectedCustomer(context);
	// }
	//
	// Object selection = context.getSelection(TRANSACTIONS);
	// if (selection != null) {
	// result = clientTransactionReceivePaymentProcess(context,
	// (ClientTransactionReceivePayment) selection);
	// if (result != null) {
	// return result;
	// }
	// }
	//
	// selection = context.getSelection(ACTIONS);
	// ActionNames actionName = (ActionNames) selection;
	// if (actionName != null && actionName == ActionNames.ADD_MORE_PAYMENTS) {
	// return addTransactionsOfSelectedCustomer(context);
	// }
	// result.add(getConstants().receivePayments());
	// ResultList itemsList = new ResultList(TRANSACTIONS);
	// for (ClientTransactionReceivePayment item : transReceivePayments) {
	// itemsList.add(creatReceivePaymentRecord(item));
	// }
	// result.add(itemsList);
	// Record moreItems = new Record(ActionNames.ADD_MORE_PAYMENTS);
	// moreItems.add("",
	// getMessages().addMore(getConstants().receivePayments()));
	// actions.add(moreItems);
	// return null;
	// }

	// public void resetValue(ClientTransactionReceivePayment obj) {
	// if (obj.isCreditsApplied()) {
	// ClientCustomer customer = get(CUSTOMER).getValue();
	// int size = getCustomerCreditsAndPayments(customer.getID()).size();
	// Map<Integer, Object> toBeRvrtMap = obj.getTempCredits();
	// for (int i = 0; i < size; i++) {
	// if (toBeRvrtMap.containsKey(i)) {
	// TempCredit toBeAddCr = (TempCredit) toBeRvrtMap.get(i);
	// List<ClientTransactionReceivePayment> selectedRecords = get(
	// TRANSACTIONS).getValue();
	// if (selectedRecords.size() != 0) {
	// for (int j = 0; j < selectedRecords.size(); j++) {
	// Map<Integer, Object> rcvCrsMap = selectedRecords
	// .get(j).getTempCredits();
	// if (rcvCrsMap.containsKey(i)) {
	// TempCredit chngCrd = (TempCredit) rcvCrsMap
	// .get(i);
	// chngCrd.setRemainingBalance(chngCrd
	// .getRemainingBalance()
	// + toBeAddCr.getAmountToUse());
	// }
	// }
	// }
	// }
	// obj.setCreditsApplied(false);
	// }
	// obj.setPayment(0.0d);
	// obj.setCashDiscount(0.0d);
	// obj.setWriteOff(0.0d);
	// obj.setAppliedCredits(0.0d);
	// obj.setDummyDue(obj.getAmountDue());
	// }
	// }

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientReceivePayment payment = new ClientReceivePayment();

		ClientCustomer customer = get(CUSTOMER).getValue();
		payment.setCustomer(customer);
		payment.setType(ClientTransaction.TYPE_RECEIVE_PAYMENT);
		double amount = get(AMOUNT_RECEIVED).getValue();
		payment.setAmount(amount);
		payment.setCustomerBalance(customer.getBalance());
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		payment.setPaymentMethod(paymentMethod);

		ClientFinanceDate date = get(DATE).getValue();
		payment.setDate(date.getDate());

		String receivePaymentNum = get(NUMBER).getValue();
		payment.setNumber(receivePaymentNum);

		ClientAccount account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		payment.setDepositIn(account);
		List<ClientTransactionReceivePayment> list = get(TRANSACTIONS)
				.getValue();
		payment.setTransactionReceivePayment(list);

		String memo = get(MEMO).getValue();
		payment.setMemo(memo);

		String checkNumber = get(CHECK_NUMBER).getValue();
		payment.setCheckNumber(checkNumber);

		recalculateGridAmounts(payment);

		create(payment, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().receivePayment());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().receivePayment());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(MEMO).setDefaultValue("");
		get(AMOUNT_RECEIVED).setDefaultValue(new Double(0));
		get(CHECK_NUMBER).setDefaultValue("1");
	}

	@Override
	public String getSuccessMessage() {
		return getMessages()
				.createSuccessfully(getConstants().receivePayment());
	}
}
