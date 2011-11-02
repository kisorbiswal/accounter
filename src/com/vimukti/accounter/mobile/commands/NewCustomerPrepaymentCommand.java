package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.List;

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
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class NewCustomerPrepaymentCommand extends NewAbstractTransactionCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(
				getMessages().customerPrePayment(Global.get().Customer()));

	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(
				getMessages().customerPrePayment(Global.get().Customer()));
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_PREPAYMENT,
						context.getCompany()));

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(
				getMessages().customerPrePayment(Global.get().Customer()));
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER,
				"Please Enter Customer name or number ", "Customer", false,
				true, null) {

			@Override
			protected List<ClientCustomer> getLists(Context context) {
				return getClientCompany().getCustomers();
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
			protected List<ClientAccount> getLists(Context context) {

				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() == ClientAccount.TYPE_BANK) {
							return true;
						}
						return false;
					}
				}, getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().bankAccounts());
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
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

		list.add(new BooleanRequirement(TO_BE_PRINTED, true) {

			@Override
			protected String getTrueString() {
				return getConstants().toBePrinted();
			}

			@Override
			protected String getFalseString() {
				return "Not Printed ";
			}
		});
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getConstants().checkNo()), getConstants().checkNo(), true, true) {
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
				getConstants().memo()), getConstants().memo(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientCustomerPrePayment prePayment = new ClientCustomerPrePayment();
		ClientFinanceDate date = get(DATE).getValue();
		prePayment.setDate(date.getDate());
		String number = get(NUMBER).getValue();
		prePayment.setNumber(number);
		ClientCustomer customer = get(CUSTOMER).getValue();
		prePayment.setCustomer(customer.getID());
		ClientAccount depositIn = get(PAY_FROM).getValue();
		prePayment.setDepositIn(depositIn.getID());
		double amount = get(AMOUNT).getValue();
		prePayment.setTotal(amount);
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		prePayment.setPaymentMethod(paymentMethod);
		String checkNum = get(CHEQUE_NO).getValue();
		prePayment.setCheckNumber(checkNum);
		Boolean tobePrinted = get(TO_BE_PRINTED).getValue();
		prePayment.setToBePrinted(tobePrinted);
		String memo = get(MEMO).getValue();
		ClientCurrency currency = get(CURRENCY).getValue();

		prePayment.setCurrency(currency.getID());
		prePayment.setMemo(memo);
		prePayment.setStatus(ClientCustomerPrePayment.STATUS_OPEN);
		prePayment.setType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
		adjustBalance(amount, customer, prePayment, context);
		create(prePayment, context);
		return null;
	}

	private void adjustBalance(double amount, ClientCustomer customer,
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
		ClientAccount depositIn = context.getClientCompany().getAccount(
				customerPrePayment.getDepositIn());
		if (depositIn.isIncrease()) {
			customerPrePayment.setEndingBalance(depositIn.getTotalBalance()
					- enteredBalance);
		} else {
			customerPrePayment.setEndingBalance(depositIn.getTotalBalance()
					+ enteredBalance);
		}
	}

}