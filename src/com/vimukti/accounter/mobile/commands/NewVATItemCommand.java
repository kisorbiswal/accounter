package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;

public class NewVATItemCommand extends AbstractVATCommand {

	private static final String DESCRIPTION = "description";
	private static final String IS_PERCENTAGE = "isPercentage";
	private static final String IS_ACTIVE = "isActive";
	private static final String VAT_RETURN_BOX = "vatReturnBox";
	private static final String VAT_RETURN_BOXES = "vatReturnBoxes";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(NAME, false, true));
		list.add(new Requirement(DESCRIPTION, true, true));
		list.add(new Requirement(AMOUNT, false, true));
		list.add(new Requirement(IS_ACTIVE, true, true));
		list.add(new Requirement(TAX_AGENCY, false, true));
		// if (isUkCompany()) {
		list.add(new Requirement(IS_PERCENTAGE, true, true));
		list.add(new Requirement(VAT_RETURN_BOX, false, true));
		// }
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		Result makeResult = context.makeResult();
		makeResult.add(" Customer is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		setOptionalFields();

		result = nameRequirement(context, list, NAME, "Enter  VatItem Name");
		if (result != null) {
			return result;
		}

		result = amountRequirement(context, list, AMOUNT, "Enter  Amount");
		if (result != null) {
			return result;
		}

		result = taxAgencyRequirement(context, list, TAX_AGENCY);
		if (result != null) {
			return result;
		}

		if (getClientCompany().getPreferences().isTrackTax()) {
			result = vatReturnRequirement(context, list, VAT_RETURN);
			if (result != null) {
				return result;
			}
		}

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createVATItem(context);
	}

	private void setOptionalFields() {
		Requirement descReq = get(DESCRIPTION);
		if (descReq.getDefaultValue() == null) {
			descReq.setDefaultValue(new String());
		}

		Requirement isActiveReq = get(IS_ACTIVE);
		if (isActiveReq.getDefaultValue() == null) {
			isActiveReq.setDefaultValue(true);
		}

		Requirement isPercentageReq = get(IS_PERCENTAGE);
		if (isPercentageReq.getDefaultValue() == null) {
			isPercentageReq.setDefaultValue(true);
		}
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		// context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		Result result = stringOptionalRequirement(context, list, selection,
				DESCRIPTION, "Enter Discription");
		if (result != null) {
			return result;
		}

		booleanOptionalRequirement(context, selection, list, IS_ACTIVE,
				"This Item is Active", "This Item is InActive");

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Tax Item.");
		actions.add(finish);

		return makeResult;
	}

	private Result createVATItem(Context context) {
		ClientTAXItem taxItem = new ClientTAXItem();
		String name = (String) get(NAME).getValue();
		String description = (String) get(DESCRIPTION).getValue();
		double taxRate = (Double) get(AMOUNT).getValue();
		boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		ClientTAXAgency taxAgency = (ClientTAXAgency) get(TAX_AGENCY)
				.getValue();

		taxItem.setPercentage(true);
		if (getClientCompany().getPreferences().isTrackTax()) {
			ClientVATReturnBox vatReturnBox = (ClientVATReturnBox) get(
					VAT_RETURN_BOX).getValue();
			taxItem.setVatReturnBox(vatReturnBox.getID());
		}
		taxItem.setName(name);
		taxItem.setDescription(description);
		taxItem.setTaxRate(taxRate);
		taxItem.setActive(isActive);
		taxItem.setTaxAgency(taxAgency.getID());

		create(taxItem, context);

		markDone();

		Result result = new Result();
		result.add("Tax Item was created successfully.");

		return result;
	}

}
