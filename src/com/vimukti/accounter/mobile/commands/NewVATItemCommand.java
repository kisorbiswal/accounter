package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
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
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().vatItem()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		setOptionalFields();

		result = nameRequirement(context, list, NAME, getConstants()
				.vatItemName(),
				getMessages().pleaseEnter(getConstants().vatItemName()));
		if (result != null) {
			return result;
		}

		result = amountRequirement(context, list, AMOUNT, getConstants()
				.amount(), getMessages().pleaseEnter(getConstants().amount()));
		if (result != null) {
			return result;
		}

		result = taxAgencyRequirement(context, list, TAX_AGENCY);
		if (result != null) {
			return result;
		}

		if (getClientCompany().getPreferences().isTrackTax()) {
			result = vatReturnBoxRequirement(context, list, VAT_RETURN,
					(ClientTAXAgency) get(TAX_AGENCY).getValue());
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

	private Result vatReturnBoxRequirement(Context context, ResultList list,
			String name, ClientTAXAgency taxAgency) {

		Requirement vatReturnReq = get(VAT_RETURN_BOX);
		String vatReturn = context.getSelection(VAT_RETURN_BOXES);

		if (vatReturn != null) {
			vatReturnReq.setValue(vatReturn);
		}

		String value = vatReturnReq.getValue();
		Object selection = context.getSelection("values");
		if (!vatReturnReq.isDone() || (value == selection)) {
			return getVatReturnBoxResult(context, taxAgency);
		}

		Record record = new Record(value);
		record.add("", getConstants().vatReturnBox());
		record.add("", value);
		list.add(record);

		return null;

	}

	private Result getVatReturnBoxResult(Context context,
			ClientTAXAgency taxAgency) {
		Result result = context.makeResult();
		ResultList vatReturnsList = new ResultList(VAT_RETURN_BOXES);

		Object last = context.getLast(RequirementType.VAT_RETURN_BOX);
		if (last != null) {
			vatReturnsList
					.add(createVatReturnBoxRecord((ClientVATReturnBox) last));
		}

		List<ClientVATReturnBox> vatReturns = getVatReturnBoxes(
				context.getHibernateSession(), taxAgency);
		for (int i = 0; i < VALUES_TO_SHOW && i < vatReturns.size(); i++) {
			ClientVATReturnBox vatReturn = vatReturns.get(i);
			if (vatReturn != last) {
				vatReturnsList.add(createVatReturnBoxRecord(vatReturn));
			}
		}

		int size = vatReturnsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getMessages().pleaseSelect(
					getConstants().vatReturnBox()));
		}

		result.add(message.toString());
		result.add(vatReturnsList);

		return result;
	}

	private List<ClientVATReturnBox> getVatReturnBoxes(
			Session hibernateSession, ClientTAXAgency vatAgency) {
		List<ClientVATReturnBox> vatBoxes = getClientCompany()
				.getVatReturnBoxes();
		List<ClientVATReturnBox> vatBoxes2 = new ArrayList<ClientVATReturnBox>();
		for (ClientVATReturnBox vatBox : vatBoxes) {
			if (vatAgency.getVATReturn() == vatBox.getVatReturnType())
				vatBoxes2.add(vatBox);
		}
		return vatBoxes2;
	}

	private Record createVatReturnBoxRecord(ClientVATReturnBox vatReturn) {
		Record record = new Record(vatReturn);
		record.add("Name", vatReturn.getName());
		return record;
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
				DESCRIPTION, getConstants().description(), getMessages()
						.pleaseEnter(getConstants().description()));
		if (result != null) {
			return result;
		}

		booleanOptionalRequirement(context, selection, list, IS_ACTIVE,
				getConstants().itemIsActive(), getConstants().itemIsInactive());

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().finishToCreate(getConstants().taxItem()));
		actions.add(finish);

		return makeResult;
	}

	private Result createVATItem(Context context) {
		ClientTAXItem taxItem = new ClientTAXItem();
		String name = (String) get(NAME).getValue();
		String description = (String) get(DESCRIPTION).getValue();
		double taxRate = Double.parseDouble((String) get(AMOUNT).getValue());
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
		result.add(getMessages().createSuccessfully(getConstants().taxItem()));

		return result;
	}

}
