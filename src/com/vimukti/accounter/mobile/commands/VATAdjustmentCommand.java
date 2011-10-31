package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

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
			protected List<ClientTAXAgency> getLists(Context context) {
				return context.getClientCompany().gettaxAgencies();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getConstants().taxAgency());
			}

			@Override
			protected boolean filter(ClientTAXAgency e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new TaxItemRequirement(TAX_ITEM, getMessages()
				.pleaseEnterName(getConstants().taxItem()), getConstants()
				.taxItem(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().taxItem());
			}

			@Override
			protected List<ClientTAXItem> getLists(Context context) {
				return context.getClientCompany().getTaxItems();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getConstants().taxItem());
			}

			@Override
			protected boolean filter(ClientTAXItem e, String name) {
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
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return e.getIsActive()
								&& e.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& e.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& e.getType() != ClientAccount.TYPE_INVENTORY_ASSET;
					}
				}, context.getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages()
						.youDontHaveAny(
								getMessages().adjustmentAccount(
										Global.get().Account()));
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
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
		ClientTAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ClientAccount account = get(ADJUSTMENT_ACCOUNT).getValue();
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

		ClientTAXItem taxItem = get(TAX_ITEM).getValue();
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
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().vatAdjustment());
	}

}
