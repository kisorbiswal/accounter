package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.VATReturnBox;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewVATItemCommand extends Command {

	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String IS_PERCENTAGE = "isPercentage";
	private static final String TAX_RATE = "taxRate";
	private static final String IS_ACTIVE = "isActive";
	private static final String TAX_AGENCY = "taxAgency";
	private static final String VAT_RETURN_BOX = "vatReturnBox";

	private static final int TAXAGENCIES_TO_SHOW = 0;
	private static final int VATRETURN_BOXES_TO_SHOW = 0;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(NAME, false, true));
		list.add(new Requirement(DESCRIPTION, true, true));
		list.add(new Requirement(IS_PERCENTAGE, true, true));
		list.add(new Requirement(TAX_RATE, false, true));
		list.add(new Requirement(IS_ACTIVE, true, true));
		list.add(new Requirement(TAX_AGENCY, false, true));
		list.add(new Requirement(VAT_RETURN_BOX, false, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = nameRequirement(context);
		if (result == null) {
			// TODO
		}

		result = taxRateRequirement(context);
		if (result == null) {
			// TODO
		}

		result = taxAgencyRequirement(context);
		if (result == null) {
			// TODO
		}

		result = vatReturnBoxRequirement(context);
		if (result == null) {
			// TODO
		}

		result = createOptionalResult(context);
		if (result == null) {
			// TODO
		}

		return createVATItem(context);
	}

	private Result vatReturnBoxRequirement(Context context) {
		Requirement vatReturnBox = get(VAT_RETURN_BOX);
		if (!vatReturnBox.isDone()) {
			return getVatReturnBoxResult(context);
		}
		return null;
	}

	private Result taxAgencyRequirement(Context context) {
		Requirement taxAgency = get(TAX_AGENCY);
		if (!taxAgency.isDone()) {
			return getTaxAgencyResult(context);
		}
		return null;
	}

	private Result taxRateRequirement(Context context) {
		Requirement taxRate = get(TAX_RATE);
		double selTaxRate = context.getSelection(TAX_RATE);
		if (selTaxRate != 0) {
			taxRate.setValue(selTaxRate);
		}
		if (!taxRate.isDone()) {
			return getResultToAsk(context, "Please Enter the TaxRate.");
		}
		return null;
	}

	private Result nameRequirement(Context context) {
		Requirement name = get(NAME);
		String selName = context.getSelection(NAME);
		if (selName != null) {
			name.setValue(selName);
		}
		if (!name.isDone()) {
			return getResultToAsk(context, "Please Enter the VAT Item Name.");
		}
		return null;
	}

	private Result createOptionalResult(Context context) {
		Object selection = context.getSelection("actions");
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

		Requirement nameReq = get(NAME);
		String name = (String) nameReq.getValue();
		if (name == selection) {
			return nameRequirement(context);
		}

		Requirement taxRateReq = get(TAX_RATE);
		Double taxRate = (Double) taxRateReq.getValue();
		if (taxRate == selection) {
			return taxRateRequirement(context);
		}

		Requirement taxAgencyrReq = get(TAX_AGENCY);
		TAXAgency taxAgency = (TAXAgency) taxAgencyrReq.getValue();
		if (taxAgency == selection) {
			return taxAgencyRequirement(context);
		}

		Requirement vatReturnBoxReq = get(VAT_RETURN_BOX);
		VATReturnBox vatReturnBox = (VATReturnBox) vatReturnBoxReq.getValue();
		if (vatReturnBox == selection) {
			return vatReturnBoxRequirement(context);
		}

		ResultList list = new ResultList("values");

		Record nameRecord = new Record(name);
		nameRecord.add("Name", "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);

		Result result = descriptionRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = isPercentageRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Record taxRateRecord = new Record(taxRate);
		taxRateRecord.add("Name", "Tax Rate");
		taxRateRecord.add("Value", taxRate);
		list.add(taxRateRecord);

		result = isActiveRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Record taxAgencyRecord = new Record(taxAgency);
		taxAgencyRecord.add("Name", "Tax Agency");
		taxAgencyRecord.add("Value", taxAgency);
		list.add(taxAgencyRecord);

		Record vatReturnBoxRecord = new Record(vatReturnBox);
		vatReturnBoxRecord.add("Name", "VAT Return Box");
		vatReturnBoxRecord.add("Value", vatReturnBox);
		list.add(vatReturnBoxRecord);

		result = context.makeResult();
		result.add("VAT Item is ready to create with following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create VAT Item.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result isActiveRequirement(Context context, ResultList list,
			Object selection) {

		Requirement isActiveReq = get(IS_ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getDefaultValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals(IS_ACTIVE)) {
			Boolean isAct = context.getSelection(IS_ACTIVE);
			if (isAct == null) {
				isAct = context.getInteger() == 1 ? true : false;
			}
			isActive = isAct;
			isActiveReq.setDefaultValue(isActive);
		}
		if (selection == isActive) {
			context.setAttribute("input", IS_ACTIVE);
			// TODO return text(context, description);
		}

		Record isActiveRecord = new Record(isActive);
		isActiveRecord.add("Name", "Is Active");
		isActiveRecord.add("Value", isActive);
		list.add(isActiveRecord);
		return null;
	}

	private Result isPercentageRequirement(Context context, ResultList list,
			Object selection) {

		Requirement isPercentageReq = get(IS_PERCENTAGE);
		Boolean isPercentage = (Boolean) isPercentageReq.getDefaultValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals(IS_PERCENTAGE)) {
			Boolean isPerc = context.getSelection(IS_PERCENTAGE);
			if (isPerc == null) {
				isPerc = context.getInteger() == 1 ? true : false;
			}
			isPercentage = isPerc;
			isPercentageReq.setDefaultValue(isPercentage);
		}
		if (selection == isPercentage) {
			context.setAttribute("input", IS_PERCENTAGE);
			// TODO return text(context, description);
		}

		Record isPercentageRecord = new Record(isPercentage);
		isPercentageRecord.add("Name", "Is Percentage");
		isPercentageRecord.add("Value", isPercentage);
		list.add(isPercentageRecord);
		return null;
	}

	private Result descriptionRequirement(Context context, ResultList list,
			Object selection) {

		Requirement descriptionReq = get(DESCRIPTION);
		String description = (String) descriptionReq.getDefaultValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals(DESCRIPTION)) {
			String desc = context.getSelection(DESCRIPTION);
			if (desc == null) {
				desc = context.getString();
			}
			description = desc;
			descriptionReq.setDefaultValue(description);
		}
		if (selection == description) {
			context.setAttribute("input", DESCRIPTION);
			// TODO return text(context, description);
		}

		Record descRecord = new Record(description);
		descRecord.add("Name", "Description");
		descRecord.add("Value", description);
		list.add(descRecord);
		return null;
	}

	private Result createVATItem(Context context) {
		String name = (String) get("name").getValue();
		String description = (String) get("description").getValue();
		boolean isPercentage = (Boolean) get("isPercentage").getValue();
		double taxRate = (Double) get("taxRate").getValue();
		boolean isActive = (Boolean) get("isActive").getValue();
		TAXAgency taxAgency = (TAXAgency) get("taxAgency").getValue();
		VATReturnBox vatReturnBox = (VATReturnBox) get("vatReturnBox")
				.getValue();

		TAXItem taxItem = new TAXItem();
		taxItem.setName(name);
		taxItem.setDescription(description);
		taxItem.setPercentage(isPercentage);
		taxItem.setTaxRate(taxRate);
		taxItem.setActive(isActive);
		taxItem.setTaxAgency(taxAgency);
		taxItem.setVatReturnBox(vatReturnBox);

		return null;
	}

	private Result getVatReturnBoxResult(Context context) {
		Result result = context.makeResult();
		ResultList vatReturnBoxesList = new ResultList("");

		Object last = context.getLast(RequirementType.VAT_RETURN_BOX);
		if (last != null) {
			vatReturnBoxesList
					.add(createVATReturnBoxRecord((VATReturnBox) last));
		}

		List<VATReturnBox> vatReturnBoxes = getVATReturnBoxes(context
				.getSession());
		for (int i = 0; i < VATRETURN_BOXES_TO_SHOW
				|| i < vatReturnBoxes.size(); i++) {
			VATReturnBox vatReturnBox = vatReturnBoxes.get(i);
			if (vatReturnBox != last) {
				vatReturnBoxesList
						.add(createVATReturnBoxRecord((VATReturnBox) vatReturnBox));
			}
		}

		int size = vatReturnBoxesList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the vatReturnBox");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(vatReturnBoxesList);
		result.add(commandList);
		result.add("Select the vatReturnBox");

		return result;
	}

	private List<VATReturnBox> getVATReturnBoxes(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createVATReturnBoxRecord(VATReturnBox last) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result getTaxAgencyResult(Context context) {
		Result result = context.makeResult();
		ResultList taxAgenciesList = new ResultList("");

		Object last = context.getLast(RequirementType.TAXAGENCY);
		if (last != null) {
			taxAgenciesList.add(createTaxAgencyRecord((TAXAgency) last));
		}

		List<TAXAgency> taxAgencies = getTaxAgencies(context.getSession());
		for (int i = 0; i < TAXAGENCIES_TO_SHOW || i < taxAgencies.size(); i++) {
			TAXAgency taxAgency = taxAgencies.get(i);
			if (taxAgency != last) {
				taxAgenciesList
						.add(createTaxAgencyRecord((TAXAgency) taxAgency));
			}
		}

		int size = taxAgenciesList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the TaxAgency");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(taxAgenciesList);
		result.add(commandList);
		result.add("Select the TaxAgency");

		return result;
	}

	private List<TAXAgency> getTaxAgencies(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createTaxAgencyRecord(TAXAgency last) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result getResultToAsk(Context context, String message) {
		Result result = context.makeResult();
		result.add(message);
		return result;
	}

}
