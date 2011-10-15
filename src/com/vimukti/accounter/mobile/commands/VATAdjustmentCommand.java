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
		// if (isUkCompany()) {
		list.add(new Requirement(TAX_ITEM, false, true));
		// }
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

		setOptionalValues();

		result = taxAgencyRequirement(context);
		if (result != null) {
			return result;
		}

		result = taxItemRequirement(context);
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, ADJUSTMENT_ACCOUNT);
		if (result != null) {
			return result;
		}

		result = amountRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context);
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
		if (getCompanyType(context) == ACCOUNTING_TYPE_UK) {
			TAXItem taxItem = get(TAX_ITEM).getValue();
			taxAdjustment.setTaxItem(taxItem);
		}

		create(taxAdjustment, context);
		markDone();

		Result result = new Result();
		result.add("Tax Adjustment was created successfully.");

		return result;
	}

	private Result createOptionalRequirement(Context context) {
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

		Requirement taxAgencyrReq = get(TAX_AGENCY);
		TAXAgency taxAgency = (TAXAgency) taxAgencyrReq.getValue();
		if (taxAgency == selection) {
			context.setAttribute(INPUT_ATTR, TAX_AGENCY);
			return getTaxAgencyResult(context);
		}

		Requirement accountReq = get(ADJUSTMENT_ACCOUNT);
		Account account = (Account) accountReq.getValue();
		if (account == selection) {
			context.setAttribute(INPUT_ATTR, ADJUSTMENT_ACCOUNT);
			return getAccountResult(context);
		}

		Requirement amountReq = get(AMOUNT);
		Double amount = (Double) amountReq.getValue();
		if (amount == selection) {
			context.setAttribute(INPUT_ATTR, AMOUNT);
			return number(context, "Please Enter the Tax Rate.", "" + amount);
		}

		ResultList list = new ResultList("values");

		Record taxAgencyRecord = new Record(TAX_AGENCY);
		taxAgencyRecord.add(INPUT_ATTR, "Tax Agency");
		taxAgencyRecord.add("Value", taxAgency);
		list.add(taxAgencyRecord);

		if (getCompanyType(context) == ACCOUNTING_TYPE_UK) {
			Requirement taxItemReq = get(TAX_ITEM);
			TAXItem taxItem = (TAXItem) taxItemReq.getValue();
			if (taxItem == selection) {
				context.setAttribute(INPUT_ATTR, TAX_ITEM);
				return getTaxItemResult(context);
			}

			Record taxItemRecord = new Record(TAX_ITEM);
			taxItemRecord.add(INPUT_ATTR, "Tax Item");
			taxItemRecord.add("Value", taxItem);
			list.add(taxItemRecord);
		}

		Record accountRecord = new Record(ADJUSTMENT_ACCOUNT);
		accountRecord.add(INPUT_ATTR, "Adjustment Account");
		accountRecord.add("Value", account);
		list.add(accountRecord);

		Record amountRecord = new Record(AMOUNT);
		amountRecord.add(INPUT_ATTR, "Amount");
		amountRecord.add("Value", amount);
		list.add(amountRecord);

		Requirement isIncreaseVatReq = get(IS_INCREASE_VATLINE);
		Boolean isIncreaseVat = (Boolean) isIncreaseVatReq.getValue();
		if (selection == isIncreaseVat) {
			context.setAttribute(INPUT_ATTR, IS_INCREASE_VATLINE);
			isIncreaseVat = !isIncreaseVat;
			isIncreaseVatReq.setValue(isIncreaseVat);
		}
		String increaseVatString = "";
		if (isIncreaseVat) {
			increaseVatString = "Increase VAT line.";
		} else {
			increaseVatString = "Decrease VAT line.";
		}
		Record isIncreaseVatRecord = new Record(IS_INCREASE_VATLINE);
		isIncreaseVatRecord.add("Name", "");
		isIncreaseVatRecord.add("Value", increaseVatString);
		list.add(isIncreaseVatRecord);

		Result result = stringOptionalRequirement(context, list, selection,
				"memo", "Add a memo");
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Tax Adjustment is ready to create with following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Tax Adjustment.");
		actions.add(finish);
		result.add(actions);

		return result;
	}
}
