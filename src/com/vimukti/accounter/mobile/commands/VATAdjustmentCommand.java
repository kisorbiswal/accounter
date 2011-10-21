package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ListFilter;

public class VATAdjustmentCommand extends AbstractVATCommand {

	private static final String IS_INCREASE_VATLINE = "isIncreaseVatLine";
	private static final String ADJUSTMENT_ACCOUNT = "adjustmentAccount";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(TAX_AGENCY, false, true));
		list.add(new Requirement(TAX_ITEM, false, true));
		list.add(new Requirement(ADJUSTMENT_ACCOUNT, false, true));
		list.add(new Requirement(AMOUNT, false, true));
		list.add(new Requirement(IS_INCREASE_VATLINE, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {
		setOptionalValues();
		Result result = null;
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().taxAdjustment()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = taxAgencyRequirement(context, list, TAX_AGENCY);
		if (result != null) {
			return result;
		}

		result = taxItemRequirement(context, list, TAX_ITEM, getMessages()
				.pleaseSelect(getConstants().taxItem()), getConstants()
				.taxItem(), new ListFilter<ClientTAXItem>() {

			@Override
			public boolean filter(ClientTAXItem e) {
				return true;
			}
		});
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, list, ADJUSTMENT_ACCOUNT,
				getMessages().adjustmentAccount(Global.get().Account()),
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						return account.getIsActive()
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET;
					}
				});
		if (result != null) {
			return result;
		}

		result = amountRequirement(context, list, AMOUNT, getMessages()
				.pleaseEnter(getConstants().amount()), getConstants().amount());
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createTaxAdjustment(context);
	}

	private void setOptionalValues() {
		get(IS_INCREASE_VATLINE).setDefaultValue(true);
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(ORDER_NO).setDefaultValue("1");
		get(MEMO).setDefaultValue(new String());
	}

	private Result createTaxAdjustment(Context context) {

		ClientTAXAdjustment taxAdjustment = new ClientTAXAdjustment();
		ClientTAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ClientAccount account = get(ADJUSTMENT_ACCOUNT).getValue();
		String amount1 = get(AMOUNT).getValue();
		double amount = Double.parseDouble(amount1);
		boolean isIncreaseVatLine = get(IS_INCREASE_VATLINE).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		String number = get(ORDER_NO).getValue();
		String memo = get(MEMO).getValue();

		taxAdjustment.setTaxAgency(taxAgency.getID());
		taxAdjustment.setAdjustmentAccount(account.getID());
		taxAdjustment.setNetAmount(amount);
		taxAdjustment.setIncreaseVATLine(isIncreaseVatLine);
		taxAdjustment.setDate(new FinanceDate(date).getDate());
		taxAdjustment.setNumber(number);
		taxAdjustment.setMemo(memo);

		ClientTAXItem taxItem = get(TAX_ITEM).getValue();
		taxAdjustment.setTaxItem(taxItem.getID());

		create(taxAdjustment, context);
		markDone();

		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().taxAdjustment()));

		return result;
	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		booleanOptionalRequirement(context, selection, list,
				IS_INCREASE_VATLINE, getConstants().increaseVATLine(),
				getConstants().decreaseVATLine());

		Result result = stringOptionalRequirement(context, list, selection,
				MEMO, getConstants().memo(),
				getMessages().pleaseEnter(getConstants().memo()));
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context, list, selection, DATE, getMessages()
				.pleaseEnter(getConstants().date()), getConstants().date());
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().taxAdjustment()));
		actions.add(finish);

		return makeResult;
	}
}
