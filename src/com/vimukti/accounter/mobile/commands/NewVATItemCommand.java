package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.VATReturnBox;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

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

		setOptionalFields();

		result = nameRequirement(context, null, null, null);
		if (result != null) {
			return result;
		}

		result = amountRequirement(context);
		if (result != null) {
			return result;
		}

		result = taxAgencyRequirement(context);
		if (result != null) {
			return result;
		}

		if (getCompanyType(context) == ACCOUNTING_TYPE_UK) {
			result = vatReturnBoxRequirement(context);
			if (result != null) {
				return result;
			}
		}

		result = createOptionalResult(context);
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

	private Result vatReturnBoxRequirement(Context context) {
		Requirement vatReturnBoxReq = get(VAT_RETURN_BOX);
		VATReturnBox vatReturnBox = context.getSelection(VAT_RETURN_BOXES);
		if (vatReturnBox != null) {
			vatReturnBoxReq.setValue(vatReturnBox);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!vatReturnBoxReq.isDone()) {
			return getVatReturnBoxResult(context);
		}
		return null;
	}

	private Result createOptionalResult(Context context) {
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

		Requirement nameReq = get(NAME);
		String name = (String) nameReq.getValue();
		if (NAME == selection) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context, "Please Enter the Tax Item Name.", name);
		}

		Requirement taxRateReq = get(AMOUNT);
		Double taxRate = (Double) taxRateReq.getValue();
		if (AMOUNT == selection) {
			context.setAttribute(INPUT_ATTR, AMOUNT);
			return amount(context, "Please Enter the Tax Rate.", taxRate);
		}

		Requirement taxAgencyrReq = get(TAX_AGENCY);
		TAXAgency taxAgency = (TAXAgency) taxAgencyrReq.getValue();
		if (TAX_AGENCY == selection) {
			context.setAttribute(INPUT_ATTR, TAX_AGENCY);
			return getTaxAgencyResult(context);
		}

		ResultList list = new ResultList("values");

		Record nameRecord = new Record(NAME);
		nameRecord.add(INPUT_ATTR, "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);

		Result result = stringOptionalRequirement(context, list, selection,
				DESCRIPTION, "Enter Discription");
		if (result != null) {
			return result;
		}

		Record taxRateRecord = new Record(AMOUNT);
		taxRateRecord.add("Name", "Tax Rate");
		taxRateRecord.add("Value", taxRate);
		list.add(taxRateRecord);

		Record taxAgencyRecord = new Record(TAX_AGENCY);
		taxAgencyRecord.add("Name", "Tax Agency");
		taxAgencyRecord.add("Value", taxAgency.getName());
		list.add(taxAgencyRecord);

		if (getCompanyType(context) == ACCOUNTING_TYPE_UK) {
			Requirement vatReturnBoxReq = get(VAT_RETURN_BOX);
			VATReturnBox vatReturnBox = (VATReturnBox) vatReturnBoxReq
					.getValue();
			if (VAT_RETURN_BOX == selection) {
				context.setAttribute(INPUT_ATTR, VAT_RETURN_BOX);
				return getVatReturnBoxResult(context);
			}
			Record vatReturnBoxRecord = new Record(VAT_RETURN_BOX);
			vatReturnBoxRecord.add("Name", "VAT Return Box");
			vatReturnBoxRecord.add("Value", vatReturnBox.getName());
			list.add(vatReturnBoxRecord);

			Requirement isPercentageReq = get(IS_PERCENTAGE);
			Boolean isPercentage = (Boolean) isPercentageReq.getValue();

			if (selection == IS_PERCENTAGE) {
				context.setAttribute(INPUT_ATTR, IS_PERCENTAGE);
				isPercentage = !isPercentage;
				isPercentageReq.setValue(isPercentage);
			}
			String percentageString = "";
			if (isPercentage) {
				percentageString = "Considerd As Percentage.";
			} else {
				percentageString = "Considered As Amount.";
			}
			Record isPercentageRecord = new Record(IS_PERCENTAGE);
			isPercentageRecord.add("Name", "");
			isPercentageRecord.add("Value", percentageString);
			list.add(isPercentageRecord);
		}

		Requirement isActiveReq = get(IS_ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();

		if (selection == IS_ACTIVE) {
			context.setAttribute(INPUT_ATTR, IS_ACTIVE);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This Item is Active";
		} else {
			activeString = "This Item is InActive";
		}
		Record isActiveRecord = new Record(IS_ACTIVE);
		isActiveRecord.add("Name", "");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);

		result = context.makeResult();
		result.add("Tax Item is ready to create with following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Tax Item.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result getVatReturnBoxResult(Context context) {
		Result result = context.makeResult();
		ResultList vatReturnBoxesList = new ResultList(VAT_RETURN_BOXES);

		Object last = context.getLast(RequirementType.VAT_RETURN_BOX);
		if (last != null) {
			vatReturnBoxesList
					.add(createVATReturnBoxRecord((VATReturnBox) last));
		}

		List<VATReturnBox> vatReturnBoxes = getVATReturnBoxes(context);
		for (int i = 0; i < VALUES_TO_SHOW && i < vatReturnBoxes.size(); i++) {
			VATReturnBox vatReturnBox = vatReturnBoxes.get(i);
			if (vatReturnBox != last) {
				vatReturnBoxesList
						.add(createVATReturnBoxRecord((VATReturnBox) vatReturnBox));
			}
		}

		int size = vatReturnBoxesList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the VAT Return Box");
		}

		result.add(message.toString());
		result.add(vatReturnBoxesList);
		result.add("Select the vatReturnBox");

		return result;
	}

	private List<VATReturnBox> getVATReturnBoxes(Context context) {
		Company company = context.getCompany();
		Set<VATReturnBox> vatReturnBoxes = company.getVatReturnBoxes();
		return new ArrayList<VATReturnBox>(vatReturnBoxes);
	}

	private Record createVATReturnBoxRecord(VATReturnBox vatReturnBox) {
		Record record = new Record(vatReturnBox);
		record.add("Name", vatReturnBox.getName());
		return record;
	}

	private Result createVATItem(Context context) {
		TAXItem taxItem = new TAXItem();
		taxItem.setCompany(context.getCompany());

		String name = (String) get(NAME).getValue();
		String description = (String) get(DESCRIPTION).getValue();
		double taxRate = (Double) get(AMOUNT).getValue();
		boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		TAXAgency taxAgency = (TAXAgency) get(TAX_AGENCY).getValue();

		if (getCompanyType(context) == ACCOUNTING_TYPE_UK) {
			boolean isPercentage = (Boolean) get(IS_PERCENTAGE).getValue();
			VATReturnBox vatReturnBox = (VATReturnBox) get(VAT_RETURN_BOX)
					.getValue();
			taxItem.setPercentage(isPercentage);
			taxItem.setVatReturnBox(vatReturnBox);
		}

		taxItem.setName(name);
		taxItem.setDescription(description);
		taxItem.setTaxRate(taxRate);
		taxItem.setActive(isActive);
		taxItem.setTaxAgency(taxAgency);

		create(taxItem, context);

		markDone();

		Result result = new Result();
		result.add("Tax Item was created successfully.");

		return result;
	}

}
