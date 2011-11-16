package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.IssuePaymentTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewIssuePaymentCommand extends NewAbstractTransactionCommand {
	private static final String PAYMENTS_TO_ISSUED = "paymentstoissued";
	private static final String CHEQUE_NO = "checknum";
	private static final String PAYMENT_METHOD = "paymentMethod";
	private static final String ACCOUNT = "account";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(PAYMENT_METHOD,
				"Please select payment method", PAYMENT_METHOD, false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return "Payment method selected";
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

		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isEnableMultiCurrency()) {
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
				.pleaseSelect(getConstants().currency()), getConstants()
				.currency(), false, true) {
			@Override
			protected String getDisplayValue(Double value) {
				ClientCurrency primaryCurrency = getPreferences()
						.getPrimaryCurrency();
				Currency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " "
						+ primaryCurrency.getFormalName();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (context.getPreferences().isEnableMultiCurrency()
							&& !((Currency) get(CURRENCY).getValue())
									.equals(context.getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;
			}
		});

		list.add(new AccountRequirement(ACCOUNT, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return "";
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getType() == Account.TYPE_BANK
									|| e.getType() == Account.TYPE_OTHER_ASSET) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return "No bank acounts available";
			}

			@Override
			protected boolean filter(Account e, String name) {
				return false;
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getConstants().checkNo()), getConstants().checkNo(), true, true));
		list.add(new IssuePaymentTableRequirement(PAYMENTS_TO_ISSUED,
				getMessages().selectTypeOfThis(getConstants().payment()),
				"payments list", true, true) {

			@Override
			protected Account getAccount() {
				return (Account) NewIssuePaymentCommand.this.get(ACCOUNT)
						.getValue();
			}

		});
	}

	private String getNextCheckNumber(Context context) {
		Account account = get(ACCOUNT).getValue();
		if (account == null) {
			return "";
		}
		String checknumber = "";
		try {
			String nextIssuePaymentCheckNumber = new FinanceTool()
					.getNextIssuePaymentCheckNumber(account.getID(), context
							.getCompany().getID());
			if (nextIssuePaymentCheckNumber != null) {
				checknumber = nextIssuePaymentCheckNumber;
			}
		} catch (Exception e) {
			checknumber = "";
		}
		return checknumber;
	}

	private String getNextTransactionNumber(Context context) {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(ClientTransaction.TYPE_ISSUE_PAYMENT,
						context.getCompany().getID());
		return nextTransactionNumber;
	}

	// private List<TransactionIssuePayment> getclientTransactionIssuePayments()
	// {
	// Account account = get(ACCOUNT).getValue();
	// return getchecks(account.getCompany().getId(), account.getID());
	// }

	private void completeProcess(Context context) {
		ClientIssuePayment issuePayment = new ClientIssuePayment();
		issuePayment.setType(ClientTransaction.TYPE_ISSUE_PAYMENT);
		issuePayment.setNumber(getNextTransactionNumber(context));
		issuePayment.setDate(new ClientFinanceDate().getDate());
		String paymentmethod = get(PAYMENT_METHOD).getValue();
		issuePayment.setPaymentMethod(paymentmethod);
		Account account = get(ACCOUNT).getValue();
		issuePayment.setAccount(account.getID());

		if (context.getPreferences().isEnableMultiCurrency()) {
			Currency currency = get(CURRENCY).getValue();
			if (currency != null) {
				issuePayment.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			issuePayment.setCurrencyFactor(factor);
		}

		String chequenum = get(CHEQUE_NO).getValue();
		if (chequenum.isEmpty()) {
			chequenum = "1";
		}
		issuePayment.setCheckNumber(chequenum);
		List<ClientTransactionIssuePayment> issuepayments = getTransactionIssuePayments(issuePayment);
		issuePayment.setTransactionIssuePayment(issuepayments);
		setTransactionTotal(issuePayment);
		create(issuePayment, context);
	}

	/**
	 * 
	 * @param issuePayment
	 * @return
	 */
	private List<ClientTransactionIssuePayment> getTransactionIssuePayments(
			ClientIssuePayment issuePayment) {
		List<ClientTransactionIssuePayment> transactionIssuePaymentsList = new ArrayList<ClientTransactionIssuePayment>();

		ClientTransactionIssuePayment entry;
		ArrayList<IssuePaymentTransactionsList> issuepayments = get(
				PAYMENTS_TO_ISSUED).getValue();
		List<ClientTransactionIssuePayment> setValuesToRecords = setValuesToRecord(issuepayments);
		for (ClientTransactionIssuePayment record : setValuesToRecords) {
			entry = new ClientTransactionIssuePayment();
			if (record.getDate() != 0)
				entry.setDate(record.getDate());
			if (record.getNumber() != null)
				entry.setNumber(record.getNumber());

			if (record.getName() != null)
				entry.setName(record.getName());

			entry.setAmount(record.getAmount());
			entry.setMemo(record.getMemo());

			if (record.getPaymentMethod() != null) {
				entry.setPaymentMethod(record.getPaymentMethod());
			}

			switch (record.getRecordType()) {
			case ClientTransaction.TYPE_WRITE_CHECK:
				entry.setWriteCheck(record.getWriteCheck());
				entry.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
				break;
			case ClientTransaction.TYPE_CASH_PURCHASE:
			case ClientTransaction.TYPE_CASH_EXPENSE:
			case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
				entry.setCashPurchase(record.getCashPurchase());
				entry.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
				break;
			case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
				entry.setCustomerRefund(record.getCustomerRefund());
				entry.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
				break;
			case ClientTransaction.TYPE_PAY_TAX:
				entry.setPaySalesTax(record.getPaySalesTax());
				entry.setRecordType(ClientTransaction.TYPE_PAY_TAX);
				break;
			case ClientTransaction.TYPE_PAY_BILL:
				entry.setPayBill(record.getPayBill());
				entry.setRecordType(ClientTransaction.TYPE_PAY_BILL);
				break;
			case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
				entry.setCreditCardCharge(record.getCreditCardCharge());
				entry.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
				break;
			case ClientTransaction.TYPE_RECEIVE_TAX:
				entry.setReceiveVAT(record.getReceiveVAT());
				entry.setRecordType(ClientTransaction.TYPE_RECEIVE_TAX);
				break;
			case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
				entry.setCustomerPrepayment(record.getCustomerPrepayment());
				entry.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
				break;

			}

			entry.setTransaction(issuePayment);

			transactionIssuePaymentsList.add(entry);

		}
		return transactionIssuePaymentsList;

	}

	private List<ClientTransactionIssuePayment> setValuesToRecord(

	List<IssuePaymentTransactionsList> entries) {
		List<ClientTransactionIssuePayment> clientTransactionIssuePayments = new ArrayList<ClientTransactionIssuePayment>();
		for (IssuePaymentTransactionsList entry : entries) {
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

			switch (entry.getType()) {
			case ClientTransaction.TYPE_WRITE_CHECK:
				record.setWriteCheck(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
				break;
			case ClientTransaction.TYPE_CASH_PURCHASE:
			case ClientTransaction.TYPE_CASH_EXPENSE:
			case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
				record.setCashPurchase(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
				break;
			case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
				record.setCustomerRefund(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
				break;
			case ClientTransaction.TYPE_PAY_TAX:
				record.setPaySalesTax(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_PAY_TAX);
				break;
			case ClientTransaction.TYPE_PAY_BILL:
				record.setPayBill(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_PAY_BILL);
				break;
			case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
				record.setCreditCardCharge(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
				break;
			case ClientTransaction.TYPE_RECEIVE_TAX:
				record.setReceiveVAT(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_RECEIVE_TAX);
				break;
			case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
				record.setCustomerPrepayment(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
				break;

			}
			clientTransactionIssuePayments.add(record);
		}
		return clientTransactionIssuePayments;

		// record.setID(entry.getTransactionId());

	}

	private void setTransactionTotal(ClientIssuePayment issuePayment) {
		double total = 0.0;
		for (ClientTransactionIssuePayment rec : issuePayment
				.getTransactionIssuePayment()) {
			total += rec.getAmount();
			rec.setTransaction(issuePayment);
		}
		issuePayment.setTotal(total);

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Creating Issue Payment... ";
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().issuePayment());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(CHEQUE_NO).setValue(getNextCheckNumber(context));
		get(CURRENCY_FACTOR).setDefaultValue(1.0);
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().issuePayment());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		completeProcess(context);
		return null;
	}

	// private List<ClientTransactionIssuePayment> getTransactionIssuePayments(
	// ClientIssuePayment issuePayment) {
	// List<ClientTransactionIssuePayment> transactionIssuePaymentsList = new
	// ArrayList<ClientTransactionIssuePayment>();
	//
	// ClientTransactionIssuePayment entry;
	// ArrayList<IssuePaymentTransactionsList> issuepayments = get(
	// PAYMENTS_TO_ISSUED).getValue();
	// for (IssuePaymentTransactionsList record : issuepayments) {
	// entry = new ClientTransactionIssuePayment();
	//
	// entry.setDate(record.getDate().getDate());
	// if (record.getNumber() != null)
	// entry.setNumber(record.getNumber());
	//
	// if (record.getName() != null)
	// entry.setName(record.getName());
	//
	// entry.setAmount(record.getAmount());
	// entry.setMemo(record.getMemo());
	//
	// if (record.getPaymentMethod() != null) {
	// entry.setPaymentMethod(record.getPaymentMethod());
	// }
	//
	// switch (record.getType()) {
	// case ClientTransaction.TYPE_WRITE_CHECK:
	// entry.setWriteCheck(record.getType());
	// entry.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
	// break;
	// case ClientTransaction.TYPE_CASH_PURCHASE:
	// case ClientTransaction.TYPE_CASH_EXPENSE:
	// case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
	// entry.setCashPurchase(record.getTransactionId());
	// entry.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
	// break;
	// case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
	// entry.setCustomerRefund(record.getTransactionId());
	// entry.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
	// break;
	// case ClientTransaction.TYPE_PAY_TAX:
	// entry.setPaySalesTax(record.getType());
	// entry.setRecordType(ClientTransaction.TYPE_PAY_TAX);
	// break;
	// case ClientTransaction.TYPE_PAY_BILL:
	// entry.setPayBill(record.getType());
	// entry.setRecordType(ClientTransaction.TYPE_PAY_BILL);
	// break;
	// case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
	// case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
	// entry.setCreditCardCharge(record.getType());
	// entry.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
	// break;
	// case ClientTransaction.TYPE_RECEIVE_TAX:
	// entry.setReceiveVAT(record.getType());
	// entry.setRecordType(ClientTransaction.TYPE_RECEIVE_TAX);
	// break;
	// case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
	// entry.setCustomerPrepayment(record.getType());
	// entry.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
	// break;
	//
	// }
	//
	// entry.setTransaction(issuePayment);
	//
	// transactionIssuePaymentsList.add(entry);
	//
	// }
	// return transactionIssuePaymentsList;
	//
	// }

}
