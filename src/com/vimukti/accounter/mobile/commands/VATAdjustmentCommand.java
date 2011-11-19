package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.TaxAgencyRequirement;
import com.vimukti.accounter.mobile.requirements.TaxItemRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ListFilter;

public class VATAdjustmentCommand extends NewAbstractTransactionCommand {

	private static final String IS_INCREASE_VATLINE = "isIncreaseVatLine";
	private static final String ADJUSTMENT_ACCOUNT = "adjustmentAccount";
	private static final String TAX_AGENCY = "taxAgency";
	private static final String TAX_ITEM = "taxItem";
	private static final String AMOUNT = "amount";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnterName(getConstants().taxAgency()), getConstants()
				.taxAgency(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().taxAgency());
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(context.getCompany()
						.getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getConstants().taxAgency());
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return e.getName().startsWith(name);
			}
		});

		/*
		 * list.add(new CurrencyRequirement(CURRENCY,
		 * getMessages().pleaseSelect( getConstants().currency()),
		 * getConstants().currency(), true, true, null) {
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if
		 * (context.getPreferences().isEnableMultiCurrency()) { return
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
		 * != null) { if (context.getPreferences().isEnableMultiCurrency() &&
		 * !((Currency) get(CURRENCY).getValue())
		 * .equals(context.getPreferences().getPrimaryCurrency())) { return
		 * super.run(context, makeResult, list, actions); } } return null;
		 * 
		 * } });
		 */

		list.add(new TaxItemRequirement(TAX_ITEM, getMessages()
				.pleaseEnterName(getConstants().taxItem()), getConstants()
				.taxItem(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().taxItem());
			}

			@Override
			protected List<TAXItem> getLists(Context context) {
				return new ArrayList<TAXItem>(context.getCompany()
						.getTaxItems());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getConstants().taxItem());
			}

			@Override
			protected boolean filter(TAXItem e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new AccountRequirement(ADJUSTMENT_ACCOUNT,
				getMessages()
						.pleaseEnterName(
								getMessages().adjustmentAccount(
										Global.get().Account())), getMessages()
						.adjustmentAccount(Global.get().Account()), false,
				true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(
								getMessages().adjustmentAccount(
										Global.get().Account()));
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getIsActive()
									&& e.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
									&& e.getType() != Account.TYPE_ACCOUNT_PAYABLE
									&& e.getType() != Account.TYPE_INVENTORY_ASSET;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages()
						.youDontHaveAny(
								getMessages().adjustmentAccount(
										Global.get().Account()));
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getConstants().amount()), getConstants().amount(), false, true));

		list.add(new BooleanRequirement(IS_INCREASE_VATLINE, true) {

			@Override
			protected String getTrueString() {
				return getConstants().increaseVATLine();
			}

			@Override
			protected String getFalseString() {
				return getConstants().decreaseVATLine();
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().date()), getConstants().date(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getConstants().orderNo()), getConstants().orderNo(), true, true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientTAXAdjustment taxAdjustment = new ClientTAXAdjustment();
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		Account account = get(ADJUSTMENT_ACCOUNT).getValue();
		Double amount = get(AMOUNT).getValue();
		boolean isIncreaseVatLine = get(IS_INCREASE_VATLINE).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		String number = get(ORDER_NO).getValue();
		String memo = get(MEMO).getValue();

		taxAdjustment.setTaxAgency(taxAgency.getID());
		taxAdjustment.setAdjustmentAccount(account.getID());
		taxAdjustment.setTotal(amount);
		taxAdjustment.setIncreaseVATLine(isIncreaseVatLine);
		taxAdjustment.setDate(new FinanceDate(date).getDate());
		taxAdjustment.setNumber(number);
		taxAdjustment.setMemo(memo);

		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * taxAdjustment.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * taxAdjustment.setCurrencyFactor(factor); }
		 */

		TAXItem taxItem = get(TAX_ITEM).getValue();
		taxAdjustment.setTaxItem(taxItem.getID());

		create(taxAdjustment, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().vatAdjustment());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().vatAdjustment());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_INCREASE_VATLINE).setDefaultValue(true);
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(ORDER_NO).setDefaultValue("1");
		get(MEMO).setDefaultValue(new String());
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().vatAdjustment());
	}

}
