package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TAXAdjustment;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ListFilter;

public class VATAdjustmentCommand extends AbstractVATCommand {

	private static final String IS_INCREASE_VATLINE = "isIncreaseVatLine";
	private static final String ADJUSTMENT_ACCOUNT = "adjustmentAccount";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
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
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = null;

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().taxAdjustment()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		setOptionalValues();

		result = taxAgencyRequirement(context, list, TAX_AGENCY);
		if (result != null) {
			return result;
		}

		result = taxItemRequirement(context, list, TAX_ITEM);
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, list, ADJUSTMENT_ACCOUNT,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		if (result != null) {
			return result;
		}

		result = amountRequirement(context, list, AMOUNT, getMessages()
				.pleaseEnter(getConstants().amount()));
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
		get(DATE).setDefaultValue(new Date());
		get(ORDER_NO).setDefaultValue("1");
		get(MEMO).setDefaultValue(new String());
	}

	private Result createTaxAdjustment(Context context) {
		TAXAdjustment taxAdjustment = new TAXAdjustment();
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		Account account = get(ADJUSTMENT_ACCOUNT).getValue();
		double amount = get(AMOUNT).getValue();
		boolean isIncreaseVatLine = get(IS_INCREASE_VATLINE).getValue();
		FinanceDate date = get(DATE).getValue();
		String number = get(NUMBER).getValue();
		String memo = get(MEMO).getValue();

		taxAdjustment.setCompany(context.getCompany());
		taxAdjustment.setTaxAgency(taxAgency);
		taxAdjustment.setAdjustmentAccount(account);
		taxAdjustment.setNetAmount(amount);
		taxAdjustment.setIncreaseVATLine(isIncreaseVatLine);
		taxAdjustment.setDate(date);
		taxAdjustment.setNumber(number);
		taxAdjustment.setMemo(memo);
//		if (getCompanyType(context) == ACCOUNTING_TYPE_UK) {
//			TAXItem taxItem = get(TAX_ITEM).getValue();
//			taxAdjustment.setTaxItem(taxItem);
//		}

		create(taxAdjustment, context);
		markDone();

		Result result = new Result();
		result.add("Tax Adjustment was created successfully.");

		return result;
	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		// context.setAttribute(INPUT_ATTR, "optional");

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
				IS_INCREASE_VATLINE, "Increase VAT line.", "Decrease VAT line.");

		Result result = stringOptionalRequirement(context, list, selection,
				"memo", "Add a memo");
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context, list, selection, DATE,
				"Enter the date");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Tax Adjustment.");
		actions.add(finish);

		return makeResult;
	}
}
