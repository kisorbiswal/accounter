package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaybillTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewPayBillCommand extends NewAbstractTransactionCommand {

	ArrayList<PayBillTransactionList> billsList = new ArrayList<PayBillTransactionList>();

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(getConstants().payBill());
	}

	@Override
	protected String getDetailsMessage() {

		return getMessages().readyToCreate(getConstants().payBill());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_PAY_BILL, context.getCompany()));

		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(FILTER_BY_DUE_ON_BEFORE).setDefaultValue(new ClientFinanceDate());
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().payBill());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getConstants().Vendor()), getConstants().vendor(), false, true,
				new ChangeListner<Vendor>() {

					@Override
					public void onSelection(Vendor value) {
					}
				}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return new ArrayList<Vendor>(context.getCompany().getVendors());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}
		});
		/*
		 * list.add(new CurrencyRequirement(CURRENCY,
		 * getMessages().pleaseSelect( getConstants().currency()),
		 * getConstants().currency(), true, true, null) {
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if
		 * (getPreferences().isEnableMultiCurrency()) { return
		 * super.run(context, makeResult, list, actions); } else { return null;
		 * } }
		 * 
		 * @Override protected List<Currency> getLists(Context context) { return
		 * new ArrayList<Currency>(context.getCompany() .getCurrencies()); } });
		 * 
		 * list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
		 * .pleaseSelect(getConstants().currency()), getConstants() .currency(),
		 * false, true) {
		 * 
		 * @Override protected String getDisplayValue(Double value) {
		 * ClientCurrency primaryCurrency = getPreferences()
		 * .getPrimaryCurrency(); Currency selc = get(CURRENCY).getValue();
		 * return "1 " + selc.getFormalName() + " = " + value + " " +
		 * primaryCurrency.getFormalName(); }
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if (get(CURRENCY).getValue()
		 * != null) { if (getPreferences().isEnableMultiCurrency() &&
		 * !((Currency) get(CURRENCY).getValue()) .equals(getPreferences()
		 * .getPrimaryCurrency())) { return super.run(context, makeResult, list,
		 * actions); } } return null; } });
		 */

		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().payFrom());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(getConstants().bankAccount()));
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
				return getMessages().youDontHaveAny(
						getConstants().bankAccounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);

			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().billNo()), getConstants().billNo(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));

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

		list.add(new DateRequirement(FILTER_BY_DUE_ON_BEFORE, getMessages()
				.pleaseEnter(getConstants().filterByBilldueonorbefore()),
				getConstants().filterByBilldueonorbefore(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new PaybillTableRequirement(BILLS_DUE, getMessages()
				.pleaseSelect(getConstants().billsToPay()), getConstants()
				.billsToPay()) {

			@Override
			protected List<PayBillTransactionList> getList() {
				try {
					return new FinanceTool().getVendorManager()
							.getTransactionPayBills(
									((Vendor) NewPayBillCommand.this
											.get(VENDOR).getValue()).getID(),
									getCompany().getID());
				} catch (DAOException e) {
					e.printStackTrace();
				}
				return billsList;
			}
		});
	}

	protected List<ClientCreditsAndPayments> getVendorCreditsAndPayments(
			long vendorId, long companyId) {
		List<ClientCreditsAndPayments> clientCreditsAndPaymentsList = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = new FinanceTool().getVendorManager()
					.getVendorCreditsAndPayments(vendorId, companyId);
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				clientCreditsAndPaymentsList.add(new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class));
			}
			return clientCreditsAndPaymentsList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCreditsAndPayments>(
				clientCreditsAndPaymentsList);

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientPayBill paybill = new ClientPayBill();
		paybill.setType(ClientTransaction.TYPE_PAY_BILL);
		paybill.setPayBillType(ClientPayBill.TYPE_PAYBILL);
		paybill.setAccountsPayable(context.getCompany()
				.getAccountsPayableAccount().getID());
		Vendor vendor = get(VENDOR).getValue();
		Account payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		ClientFinanceDate dueDate = get(FILTER_BY_DUE_ON_BEFORE).getValue();
		String number = get(NUMBER).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		paybill.setVendor(vendor.getID());
		paybill.setPayFrom(payFrom.getID());
		paybill.setPaymentMethod(paymentMethod);

		paybill.setBillDueOnOrBefore(dueDate);
		paybill.setNumber(number);
		paybill.setDate(date.getDate());
		String memo = get(MEMO).getValue();
		paybill.setMemo(memo);

		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * paybill.setCurrency(currency.getID()); } double factor =
		 * get(CURRENCY_FACTOR).getValue(); paybill.setCurrencyFactor(factor);
		 * 
		 * }
		 */

		if (context.getPreferences().isTDSEnabled()) {

			TAXItem taxItem = vendor.getTAXItem();
			if (taxItem != null) {
				paybill.setTdsTaxItem((ClientTAXItem) CommandUtils
						.getClientObjectById(taxItem.getID(),
								AccounterCoreType.TAXITEM, getCompanyId()));
			}
		}
		List<PayBillTransactionList> paybills = get(BILLS_DUE).getValue();
		List<ClientTransactionPayBill> transactionPayBills = getTransactionPayBills(
				paybills, context);
		for (ClientTransactionPayBill p : transactionPayBills) {
			p.setAmountDue(p.getAmountDue());
			p.setPayment(p.getPayment());
			p.setPayBill(paybill);
		}

		paybill.setTransactionPayBill(transactionPayBills);
		Double totalCredits = 0D;
		for (ClientCreditsAndPayments credit : getVendorCreditsAndPayments(
				vendor.getID(), context.getCompany().getId())) {
			totalCredits += credit.getBalance();
		}
		paybill.setUnUsedCredits(totalCredits);

		adjustAmountAndEndingBalance(paybill, transactionPayBills, context);
		create(paybill, context);

		return null;
	}

	private List<ClientTransactionPayBill> getTransactionPayBills(
			List<PayBillTransactionList> paybills, Context context) {
		List<ClientTransactionPayBill> clientTransactionPayBills = new ArrayList<ClientTransactionPayBill>();
		for (PayBillTransactionList curntRec : paybills) {

			ClientTransactionPayBill record = new ClientTransactionPayBill();
			if (curntRec.getType() == ClientTransaction.TYPE_ENTER_BILL) {
				record.setEnterBill(curntRec.getTransactionId());
			}
			record.setAmountDue(curntRec.getAmountDue());

			record.setDummyDue(curntRec.getAmountDue());

			record.setBillNumber(curntRec.getBillNumber());

			record.setCashDiscount(curntRec.getCashDiscount());

			record.setAppliedCredits(curntRec.getCredits());

			record.setDiscountDate(curntRec.getDiscountDate().getDate());

			record.setDueDate(curntRec.getDueDate() != null ? curntRec
					.getDueDate().getDate() : 0);

			record.setOriginalAmount(curntRec.getOriginalAmount());
			record.setPayment(curntRec.getPayment());

			// record.setPayment(curntRec.getPayment());
			Payee vendor = get(VENDOR).getValue();
			if (vendor != null)
				record.setVendor(vendor.getID());
			clientTransactionPayBills.add(record);

		}
		return clientTransactionPayBills;

	}

	private void adjustAmountAndEndingBalance(ClientPayBill transaction,
			List<ClientTransactionPayBill> transactionPayBills, Context context) {
		// List<ClientTransactionPayBill> selectedRecords = get(BILLS_DUE)
		// .getValue();
		double toBeSetAmount = 0.0;
		for (ClientTransactionPayBill rec : transactionPayBills) {
			toBeSetAmount += rec.getPayment();

		}
		if (transaction != null) {
			transaction.setTotal(toBeSetAmount);

			if (get(VENDOR).getValue() != null) {
				double toBeSetEndingBalance = 0.0;
				Account payFromAccount = get(PAY_FROM).getValue();
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