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
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
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
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class NewCustomerRefundCommand extends NewAbstractTransactionCommand {
	ClientCustomerRefund customerRefund;

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Customer Refund to update.");
				return "Customer Refunds List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientCustomerRefund transactionByNum = (ClientCustomerRefund) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.CUSTOMERREFUND);
			if (transactionByNum == null) {
				addFirstMessage(context, "Select a Customer Refund to update.");
				return "Customer Refunds List " + string;
			}
			customerRefund = transactionByNum;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			customerRefund = new ClientCustomerRefund();
		}
		return null;
	}

	private void setValues() {
		get(DATE).setValue(customerRefund.getDate());
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(customerRefund.getPayTo(),
						AccounterCoreType.CUSTOMER));
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(customerRefund.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		get(PAYMENT_METHOD).setValue(customerRefund.getPaymentMethod());
		get(AMOUNT).setValue(customerRefund.getTotal());
		get(TO_BE_PRINTED).setValue(customerRefund.getIsToBePrinted());
		get(CHEQUE_NO).setValue(customerRefund.getCheckNumber());
		get(CURRENCY_FACTOR).setValue(customerRefund.getCurrencyFactor());
		get(MEMO).setValue(customerRefund.getMemo());
	}

	@Override
	protected String getWelcomeMessage() {
		return customerRefund.getID() == 0 ? getMessages().create(
				getMessages().customerRefund(Global.get().Customer()))
				: "Update Customer Refund command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return customerRefund.getID() == 0 ? getMessages().readyToCreate(
				getMessages().customerRefund(Global.get().Customer()))
				: "Customer Refund is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_REFUNDS,
						context.getCompany()));
		get(CURRENCY).setDefaultValue(null);
		get(CURRENCY_FACTOR).setDefaultValue(1.0);

	}

	@Override
	public String getSuccessMessage() {
		return customerRefund.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().customerRefund(Global.get().Customer()))
				: getMessages().updateSuccessfully(
						getMessages().customerRefund(Global.get().Customer()));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseSelect(
				getMessages().payTo()), getMessages().payTo(), false, true,
				null) {

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
				getMessages().billNo()), getMessages().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getMessages().transferTo()),
				getMessages().transferTo(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
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
						getMessages().bankAccount(Global.get().Account()));
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
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
						getMessages().cash(), getMessages().creditCard(),
						getMessages().check(), getMessages().directDebit(),
						getMessages().masterCard(),
						getMessages().onlineBanking(),
						getMessages().standingOrder(),
						getMessages().switchMaestro() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});
		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getMessages().amount()), getMessages().amount(), false, true));

		list.add(new BooleanRequirement(TO_BE_PRINTED, true) {

			@Override
			protected String getTrueString() {
				return getMessages().toBePrinted();
			}

			@Override
			protected String getFalseString() {
				return "Not Printed ";
			}
		});
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getMessages().currency()), getMessages().currency(), true,
				true, null) {

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getMessages().checkNo()), getMessages().checkNo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if ((Boolean) get(TO_BE_PRINTED).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;

			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientFinanceDate date = get(DATE).getValue();
		Customer clientcustomer = get(CUSTOMER).getValue();
		Account account = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		double amount = get(AMOUNT).getValue();
		boolean istobePrinted = get(TO_BE_PRINTED).getValue();
		customerRefund.setPayTo(clientcustomer.getID());
		customerRefund.setPayFrom(account.getID());
		customerRefund.setPaymentMethod(paymentMethod);
		customerRefund.setIsToBePrinted(istobePrinted);
		if (!istobePrinted) {
			String cheqNum = get(CHEQUE_NO).getValue();
			customerRefund.setCheckNumber(cheqNum);
		}

		if (context.getPreferences().isEnableMultiCurrency()) {
			Currency currency = get(CURRENCY).getValue();
			if (currency != null) {
				customerRefund.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			customerRefund.setCurrencyFactor(factor);
		}
		customerRefund.setMemo(get(MEMO).getValue() == null ? "" : get(MEMO)
				.getValue().toString());
		customerRefund.setTotal(amount);
		customerRefund.setDate(date.getDate());
		customerRefund.setType(ClientCustomerRefund.TYPE_CUSTOMER_REFUNDS);
		customerRefund.setStatus(ClientCustomerRefund.STATUS_OPEN);
		adjustBalance(amount, clientcustomer, customerRefund, context);
		create(customerRefund, context);
		return null;

	}

	private void adjustBalance(double amount, Customer customer,
			ClientCustomerRefund refund, Context context) {
		double enteredBalance = amount;

		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			enteredBalance = 0D;
		}
		// if (depositIn.isIncrease()) {
		// refund.setEndingBalance(depositIn.getTotalBalance()
		// - enteredBalance);
		// } else {
		// refund.setEndingBalance(depositIn.getTotalBalance()
		// + enteredBalance);
		// }
	}
}