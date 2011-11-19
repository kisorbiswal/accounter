package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.ReceivePaymentTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class NewReceivePaymentCommand extends NewAbstractTransactionCommand {
	private static final String AMOUNT_RECEIVED = "amountreceived";
	private static final String CHECK_NUMBER = "checknum";
	private static final String TRANSACTIONS = "transactions";
	private static final String DEPOSIT_OR_TRANSFER_TO = "depositOrTransferTo";
	ClientReceivePayment payment;

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
			protected List<Customer> getLists(Context context) {
				return new ArrayList<Customer>(context.getCompany()
						.getCustomers());
			}
		});

		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getMessages().currency()), getMessages().currency(), true,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isEnableMultiCurrency()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});

		list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseSelect(getMessages().currency()), getMessages()
				.currency(), false, true) {
			@Override
			protected String getDisplayValue(Double value) {
				ClientCurrency primaryCurrency = getPreferences()
						.getPrimaryCurrency();
				ClientCurrency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " "
						+ primaryCurrency.getFormalName();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (getPreferences().isEnableMultiCurrency()
							&& !((ClientCurrency) get(CURRENCY).getValue())
									.equals(getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;
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
			protected List<Account> getLists(final Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return Arrays.asList(Account.TYPE_BANK,
									Account.TYPE_CREDIT_CARD,
									Account.TYPE_OTHER_CURRENT_ASSET,
									Account.TYPE_FIXED_ASSET).contains(
									e.getType())
									&& e.getID() != context.getCompany()
											.getAccountsReceivableAccount()
											.getID();
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()),
				getMessages().paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				String payVatMethodArray[] = new String[] {
						getMessages().cash(), getMessages().check() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AmountRequirement(AMOUNT_RECEIVED, getMessages()
				.pleaseEnter(getMessages().amountReceived()), getMessages()
				.amountReceived(), true, true));

		list.add(new ReceivePaymentTableRequirement(TRANSACTIONS, getMessages()
				.pleaseSelect(getMessages().dueForPayment()), getMessages()
				.dueForPayment()) {

			@Override
			protected List<ClientTransactionReceivePayment> getList() {
				return getTransactionRecievePayments();
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new NumberRequirement(CHECK_NUMBER, getMessages().pleaseEnter(
				getMessages().checkNo()), getMessages().checkNo(), true, true));
	}

	private ArrayList<ClientTransactionReceivePayment> getTransactionRecievePayments() {
		Customer customer = get(CUSTOMER).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		ArrayList<ReceivePaymentTransactionList> transactionReceivePayments;
		try {
			transactionReceivePayments = getTransactionReceivePayments(customer
					.getCompany().getID(), customer.getID(), date.getDate());
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

	@Override
	protected Result onCompleteProcess(Context context) {
		Customer customer = get(CUSTOMER).getValue();
		payment.setCustomer(customer.getID());
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

		Account account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		payment.setDepositIn(account.getID());
		List<ClientTransactionReceivePayment> list = get(TRANSACTIONS)
				.getValue();
		payment.setTransactionReceivePayment(list);

		if (context.getPreferences().isEnableMultiCurrency()) {
			ClientCurrency currency = get(CURRENCY).getValue();
			if (currency != null) {
				payment.setCurrency(currency.getID());
			}
			double factor = get(CURRENCY_FACTOR).getValue();
			payment.setCurrencyFactor(factor);

		}
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
		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Received Payment to update.");
				return "Received Payments List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientReceivePayment transactionByNum = (ClientReceivePayment) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.RECEIVEPAYMENT);
			if (transactionByNum == null) {
				addFirstMessage(context, "Select a Received Payment to update.");
				return "Received Payments List " + string;
			}
			payment = transactionByNum;
			setValues();
		} else {
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			payment = new ClientReceivePayment();
		}
		return null;
	}

	private void setValues() {
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(payment.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(AMOUNT_RECEIVED).setValue(payment.getAmount());
		get(PAYMENT_METHOD).setValue(payment.getPaymentMethod());
		get(DATE).setValue(payment.getDate());
		get(NUMBER).setValue(payment.getNumber());
		get(DEPOSIT_OR_TRANSFER_TO).setValue(
				CommandUtils.getServerObjectById(payment.getDepositIn(),
						AccounterCoreType.ACCOUNT));
		get(TRANSACTIONS).setValue(payment.getTransactionReceivePayment());
		get(CURRENCY_FACTOR).setValue(payment.getCurrencyFactor());
		get(MEMO).setValue(payment.getMemo());
		get(CHECK_NUMBER).setValue(payment.getCheckNumber());
	}

	@Override
	protected String getWelcomeMessage() {
		return payment.getID() == 0 ? getMessages().creating(
				getMessages().receivePayment())
				: "Update Receive Payment command activated";
	}

	@Override
	protected String getDetailsMessage() {
		return payment.getID() == 0 ? getMessages().readyToCreate(
				getMessages().receivePayment())
				: "Receive payment is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(MEMO).setDefaultValue("");
		get(AMOUNT_RECEIVED).setDefaultValue(new Double(0));
		get(CHECK_NUMBER).setDefaultValue("1");
		get(CURRENCY).setDefaultValue(null);
		get(CURRENCY_FACTOR).setDefaultValue(1.0);
	}

	@Override
	public String getSuccessMessage() {
		return payment.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().receivePayment()) : getMessages()
				.updateSuccessfully(getMessages().receivePayment());
	}
}
