package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class NewCustomerPrepaymentCommand extends NewAbstractTransactionCommand {
	ClientCustomerPrePayment prePayment;

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context,
						"Select a Customer Prepayment to update.");
				return "Received Payments List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientCustomerPrePayment transactionByNum = (ClientCustomerPrePayment) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.CUSTOMERPREPAYMENT);
			if (transactionByNum == null) {
				addFirstMessage(context,
						"Select a Customer Prepayment to update.");
				return "Received Payments List " + string;
			}
			prePayment = transactionByNum;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			prePayment = new ClientCustomerPrePayment();
		}
		return null;
	}

	private void setValues() {
		get(DATE).setValue(prePayment.getDate());
		get(NUMBER).setValue(prePayment.getNumber());
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(prePayment.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(prePayment.getDepositIn(),
						AccounterCoreType.ACCOUNT));
		get(AMOUNT).setValue(prePayment.getTotal());
		get(PAYMENT_METHOD).setValue(prePayment.getPaymentMethod());
		get(CHEQUE_NO).setValue(prePayment.getCheckNumber());
		// get(TO_BE_PRINTED).setValue(prePayment.isToBePrinted());
		get(MEMO).setValue(prePayment.getMemo());
		get(CURRENCY_FACTOR).setValue(prePayment.getCurrencyFactor());
	}

	@Override
	protected String getWelcomeMessage() {
		return prePayment.getID() == 0 ? getMessages().create(
				getMessages().payeePrePayment(Global.get().Customer()))
				: "Update Customer Prepayment command activated";

	}

	@Override
	protected String getDetailsMessage() {
		return prePayment.getID() == 0 ? getMessages().readyToCreate(
				getMessages().payeePrePayment(Global.get().Customer()))
				: "Customer Prepayment is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_PREPAYMENT,
						context.getCompany()));
		get(CURRENCY).setDefaultValue(null);
		get(CURRENCY_FACTOR).setDefaultValue(1.0);

	}

	@Override
	public String getSuccessMessage() {
		return prePayment.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().payeePrePayment(Global.get().Customer()))
				: getMessages().updateSuccessfully(
						getMessages().payeePrePayment(Global.get().Customer()));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER,
				"Please Enter Customer name or number ", "Customer", false,
				true, null) {

			@Override
			protected List<Customer> getLists(Context context) {
				return new ArrayList<Customer>(context.getCompany()
						.getCustomers());
			}
		});

		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
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
					if (getPreferences().isEnableMultiCurrency()
							&& !((Currency) get(CURRENCY).getValue())
									.equals(getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;

			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().billNo()), getConstants().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().transferTo()),
				getConstants().transferTo(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().payFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {

				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getType() == Account.TYPE_BANK) {
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
				return getMessages().youDontHaveAny(
						getConstants().bankAccounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getConstants().paymentMethod()), getConstants()
				.paymentMethod(), false, true, null) {

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

				/*
				 * Map<String, String> paymentMethods =
				 * context.getClientCompany() .getPaymentMethods(); List<String>
				 * paymentMethod = new ArrayList<String>(
				 * paymentMethods.values());
				 */
				String payVatMethodArray[] = new String[] {
						getConstants().cash(), getConstants().creditCard(),
						getConstants().check(), getConstants().directDebit(),
						getConstants().masterCard(),
						getConstants().onlineBanking(),
						getConstants().standingOrder(),
						getConstants().switchMaestro() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
			}
		});
		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getConstants().amount()), getConstants().amount(), false, true));

		// list.add(new BooleanRequirement(TO_BE_PRINTED, true) {
		//
		// @Override
		// protected String getTrueString() {
		// return getConstants().toBePrinted();
		// }
		//
		// @Override
		// protected String getFalseString() {
		// return "Not Printed ";
		// }
		// });
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getConstants().checkNo()), getConstants().checkNo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				return super.run(context, makeResult, list, actions);
			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientFinanceDate date = get(DATE).getValue();
		prePayment.setDate(date.getDate());
		String number = get(NUMBER).getValue();
		prePayment.setNumber(number);
		Customer customer = get(CUSTOMER).getValue();
		prePayment.setCustomer(customer.getID());
		Account depositIn = get(PAY_FROM).getValue();
		prePayment.setDepositIn(depositIn.getID());
		double amount = get(AMOUNT).getValue();
		prePayment.setTotal(amount);
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		prePayment.setPaymentMethod(paymentMethod);
		String checkNum = get(CHEQUE_NO).getValue();
		prePayment.setCheckNumber(checkNum);
		// Boolean tobePrinted = get(TO_BE_PRINTED).getValue();
		// prePayment.setToBePrinted(tobePrinted);
		String memo = get(MEMO).getValue();

		if (context.getPreferences().isEnableMultiCurrency()) {
			Currency currency = get(CURRENCY).getValue();
			if (currency != null) {
				prePayment.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			prePayment.setCurrencyFactor(factor);
		}
		prePayment.setMemo(memo);
		prePayment.setStatus(ClientCustomerPrePayment.STATUS_OPEN);
		prePayment.setType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
		adjustBalance(amount, customer, prePayment, context);
		create(prePayment, context);
		return null;
	}

	private void adjustBalance(double amount, Customer customer,
			ClientCustomerPrePayment customerPrePayment, Context context) {
		double enteredBalance = amount;

		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			enteredBalance = 0D;
		}
		if (customer != null) {
			customerPrePayment.setCustomerBalance(customer.getBalance()
					- enteredBalance);

		}
		// ClientAccount depositIn = (ClientAccount) CommandUtils
		// .getClientObjectById(customerPrePayment.getDepositIn(),
		// AccounterCoreType.ACCOUNT, getCompanyId());
		// if (depositIn.isIncrease()) {
		// customerPrePayment.setEndingBalance(depositIn.getTotalBalance()
		// - enteredBalance);
		// } else {
		// customerPrePayment.setEndingBalance(depositIn.getTotalBalance()
		// + enteredBalance);
		// }
	}
}