package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PayeeRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public class WriteCheckCommand extends NewAbstractTransactionCommand {

	private static final String PAYEE = "payee";
	private static final String BANK_ACCOUNT = "bankAccount";
	private static final String AMOUNT = "amount";

	ClientWriteCheck writeCheck;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new PayeeRequirement(PAYEE, getMessages().pleaseEnterName(
				getConstants().payee()), getConstants().payee(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().payee());
			}

			@Override
			protected List<Payee> getLists(Context context) {
				return new ArrayList<Payee>(context.getCompany().getPayees());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getConstants().payee());
			}

			@Override
			protected boolean filter(Payee e, String name) {
				return e.getName().startsWith(name);
			}
		});

		// list.add(new CurrencyRequirement(CURRENCY,
		// getMessages().pleaseSelect(
		// getConstants().currency()), getConstants().currency(), true,
		// true, null) {
		// @Override
		// public Result run(Context context, Result makeResult,
		// ResultList list, ResultList actions) {
		// if (getPreferences().isEnableMultiCurrency()) {
		// return super.run(context, makeResult, list, actions);
		// } else {
		// return null;
		// }
		// }
		//
		// @Override
		// protected List<Currency> getLists(Context context) {
		// return new ArrayList<Currency>(context.getCompany()
		// .getCurrencies());
		// }
		// });
		//
		// list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
		// .pleaseSelect(getConstants().currency()), getConstants()
		// .currency(), true, true) {
		// @Override
		// protected String getDisplayValue(Double value) {
		// ClientCurrency primaryCurrency = getPreferences()
		// .getPrimaryCurrency();
		// Currency selc = get(CURRENCY).getValue();
		// return "1 " + selc.getFormalName() + " = " + value + " "
		// + primaryCurrency.getFormalName();
		// }
		//
		// @Override
		// public Result run(Context context, Result makeResult,
		// ResultList list, ResultList actions) {
		// if (get(CURRENCY).getValue() != null) {
		// if (getPreferences().isEnableMultiCurrency()
		// && !((Currency) get(CURRENCY).getValue())
		// .equals(getPreferences()
		// .getPrimaryCurrency())) {
		// return super.run(context, makeResult, list, actions);
		// }
		// }
		// return null;
		// }
		// });

		list.add(new AccountRequirement(BANK_ACCOUNT, getMessages()
				.pleaseEnterNameOrNumber(getConstants().bankAccount()),
				getConstants().bankAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().bankAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return Arrays.asList(Account.TYPE_BANK,
									Account.TYPE_CREDIT_CARD,
									Account.TYPE_OTHER_CURRENT_ASSET).contains(
									e.getType());
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
						getConstants().bankAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}
		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Account()), Global.get()
				.Account(), true, true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account account) {
							if (account.getType() != Account.TYPE_CASH
									&& account.getType() != Account.TYPE_BANK
									&& account.getType() != Account.TYPE_INVENTORY_ASSET
									&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
									&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
									&& account.getType() != Account.TYPE_INCOME
									&& account.getType() != Account.TYPE_OTHER_INCOME
									&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
									&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
									&& account.getType() != Account.TYPE_OTHER_ASSET
									&& account.getType() != Account.TYPE_EQUITY
									&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY) {
								return true;
							} else {
								return false;
							}
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().date()), getConstants().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, false));

		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getConstants().amount()), getConstants().amount(), true, true));

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Payee payee = (Payee) get(PAYEE).getValue();

		if (payee != null) {
			// In Edit mode If payee was changed to customer to vendor or
			// anything else
			// previous object should become null;
			writeCheck.setCustomer(0);
			writeCheck.setVendor(0);
			writeCheck.setTaxAgency(0);

			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				writeCheck.setCustomer(payee.getID());
				writeCheck.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				break;
			case ClientPayee.TYPE_VENDOR:
				writeCheck.setVendor(payee.getID());
				writeCheck.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				break;

			case ClientPayee.TYPE_TAX_AGENCY:
				writeCheck.setTaxAgency(payee.getID());
				writeCheck.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);

				break;
			}
		}
		// if (payee.getType() == Payee.TYPE_CUSTOMER) {
		// writeCheck.setCustomer(payee.getID());
		// } else if (payee.getType() == Payee.TYPE_VENDOR) {
		// writeCheck.setVendor(payee.getID());
		// } else {
		// writeCheck.setTaxAgency(payee.getID());
		// }

		Account bankAccount = get(BANK_ACCOUNT).getValue();
		writeCheck.setBankAccount(bankAccount.getID());
		writeCheck.setToBePrinted(true);

		ClientFinanceDate date = get(DATE).getValue();
		writeCheck.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		writeCheck.setNumber(number);

		Double amount = get(AMOUNT).getValue();
		writeCheck.setAmount(amount);

		writeCheck.setType(ClientTransaction.TYPE_WRITE_CHECK);
		writeCheck.setPaymentMethod("Check");

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			writeCheck.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}
		Currency primaryCurrency = context.getCompany().getPrimaryCurrency();
		writeCheck.setCurrency(primaryCurrency.getID());
		// if (preferences.isEnableMultiCurrency()) {
		// Currency currency = get(CURRENCY).getValue();
		// if (currency != null) {
		// writeCheck.setCurrency(currency.getID());
		// }
		//
		// double factor = get(CURRENCY_FACTOR).getValue();
		// writeCheck.setCurrencyFactor(factor);
		// }

		writeCheck.setTransactionItems(accounts);
		updateTotals(context, writeCheck, false);
		if (amount < writeCheck.getTotal()) {
			amount = writeCheck.getTotal();
			writeCheck.setTotal(amount);
			writeCheck.setAmount(amount);
			writeCheck.setInWords(amount.toString());

		}
		String memo = get(MEMO).getValue();
		writeCheck.setMemo(memo);
		create(writeCheck, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				return "Invoices List";
			}
			ClientWriteCheck invoiceByNum = (ClientWriteCheck) CommandUtils
					.getClientTransactionByNumber(context.getCompany(),
							getNumberFromString(string));
			if (invoiceByNum == null) {
				return "Invoices List " + string;
			}
			writeCheck = invoiceByNum;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			writeCheck = new ClientWriteCheck();
		}
		return null;

	}

	private void setValues() {
		if (writeCheck.getCustomer() != 0) {
			get(PAYEE).setValue(
					CommandUtils.getServerObjectById(writeCheck.getCustomer(),
							AccounterCoreType.PAYEE));
		} else if (writeCheck.getVendor() != 0) {
			get(PAYEE).setValue(
					CommandUtils.getServerObjectById(writeCheck.getVendor(),
							AccounterCoreType.PAYEE));
		} else if (writeCheck.getTaxAgency() != 0) {
			get(PAYEE).setValue(
					CommandUtils.getServerObjectById(writeCheck.getTaxAgency(),
							AccounterCoreType.PAYEE));
		}
		get(BANK_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(writeCheck.getBankAccount(),
						AccounterCoreType.ACCOUNT));
		get(DATE).setValue(writeCheck.getDate());
		get(NUMBER).setValue(writeCheck.getNumber());
		get(AMOUNT).setValue(writeCheck.getNetAmount());
		get(ACCOUNTS).setValue(writeCheck.getTransactionItems());
		get(IS_VAT_INCLUSIVE).setValue(writeCheck.isAmountsIncludeVAT());
		get(CURRENCY_FACTOR).setValue(writeCheck.getCurrencyFactor());
		get(MEMO).setValue(writeCheck.getMemo());
	}

	@Override
	protected String getWelcomeMessage() {
		return writeCheck.getID() == 0 ? getMessages().creating(
				getConstants().writeCheck()) : "Updating write check";
	}

	@Override
	protected String getDetailsMessage() {
		return writeCheck.getID() == 0 ? getMessages().readyToCreate(
				getConstants().writeCheck())
				: "Write check is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(AMOUNT).setDefaultValue(0.0);
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		// get(CURRENCY).setDefaultValue(null);
		// get(CURRENCY_FACTOR).setDefaultValue(1.0);
	}

	@Override
	public String getSuccessMessage() {
		return writeCheck.getID() == 0 ? getMessages().createSuccessfully(
				getConstants().writeCheck()) : getMessages()
				.updateSuccessfully(getConstants().writeCheck());
	}

}
