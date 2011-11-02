package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaybillTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewPayBillCommand extends NewAbstractTransactionCommand {

	private ArrayList<ClientTransactionPayBill> records;
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
		list.add(new VendorRequirement(VENDOR, getMessages()
				.pleaseSelectVendor(getConstants().Vendor()), getConstants()
				.vendor(), false, true, new ChangeListner<ClientVendor>() {

			@Override
			public void onSelection(ClientVendor value) {
				records = null;
			}
		}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<ClientVendor> getLists(Context context) {
				return context.getClientCompany().getVendors();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(ClientVendor e, String name) {
				return e.getDisplayName().toLowerCase()
						.startsWith(name.toLowerCase());
			}
		});

		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().payFrom());
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {

				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() == ClientAccount.TYPE_BANK
								|| e.getType() == ClientAccount.TYPE_OTHER_ASSET) {
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
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getClientCompany().getPreferences()
						.isEnableMultiCurrency()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
			}
		});

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new PaybillTableRequirement(BILLS_DUE, getMessages()
				.pleaseSelect(getConstants().billsToPay()), getConstants()
				.billsToPay()) {

			@Override
			protected List<ClientTransactionPayBill> getList() {
				return getTransactionPayBills(
						(ClientVendor) NewPayBillCommand.this.get(VENDOR)
								.getValue(), getClientCompany());
			}
		});
	}

	private List<ClientTransactionPayBill> getTransactionPayBills(
			ClientVendor clinetVendor, ClientCompany clientCompany) {
		if (records != null) {
			return records;
		}
		try {
			billsList = new FinanceTool().getVendorManager()
					.getTransactionPayBills(clinetVendor.getID(),
							clientCompany.getID());

			if (billsList != null) {
				records = new ArrayList<ClientTransactionPayBill>();
				for (PayBillTransactionList curntRec : billsList) {
					ClientTransactionPayBill record = new ClientTransactionPayBill();
					if (curntRec.getType() == ClientTransaction.TYPE_ENTER_BILL) {
						record.setEnterBill(curntRec.getTransactionId());
					}
					record.setAmountDue(curntRec.getAmountDue());
					record.setDummyDue(curntRec.getAmountDue());

					record.setBillNumber(curntRec.getBillNumber());

					record.setCashDiscount(curntRec.getCashDiscount());

					record.setAppliedCredits(curntRec.getCredits());

					record.setDiscountDate(curntRec.getDiscountDate() != null ? curntRec
							.getDiscountDate().getDate() : 0);

					record.setDueDate(curntRec.getDueDate() != null ? curntRec
							.getDueDate().getDate() : 0);

					record.setOriginalAmount(curntRec.getOriginalAmount());

					// record.setPayment(curntRec.getPayment());
					ClientVendor vendor = clientCompany
							.getVendorByName(curntRec.getVendorName());
					if (vendor != null)
						record.setVendor(vendor.getID());
					records.add(record);
				}
				return records;
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
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
		paybill.setPayBillType(ClientPayBill.TYPE_VENDOR_PAYMENT);
		paybill.setAccountsPayable(context.getClientCompany()
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
		if (context.getClientCompany().getPreferences().isTDSEnabled()) {

			ClientTAXItem taxItem = context.getClientCompany().getTAXItem(
					vendor.getTaxItemCode());
			if (taxItem != null) {
				paybill.setTdsTaxItem(taxItem);
			}
		}
		List<ClientTransactionPayBill> paybills = get(BILLS_DUE).getValue();
		for (ClientTransactionPayBill p : paybills) {
			p.setAmountDue(p.getPayment());
			p.setPayBill(paybill);
		}

		paybill.setTransactionPayBill(paybills);
		Double totalCredits = 0D;
		for (ClientCreditsAndPayments credit : getVendorCreditsAndPayments(
				vendor.getID(), context.getCompany().getId())) {
			totalCredits += credit.getBalance();
		}
		paybill.setUnUsedCredits(totalCredits);

		adjustAmountAndEndingBalance(paybill, context);
		create(paybill, context);

		return null;
	}

	private void adjustAmountAndEndingBalance(ClientPayBill transaction,
			Context context) {
		List<ClientTransactionPayBill> selectedRecords = get(BILLS_DUE)
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