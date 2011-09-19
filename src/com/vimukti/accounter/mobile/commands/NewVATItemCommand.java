package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.VATReturnBox;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewVATItemCommand extends AbstractCommand {

	private static final String INPUT_ATTR = "input";

	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String IS_PERCENTAGE = "isPercentage";
	private static final String TAX_RATE = "taxRate";
	private static final String IS_ACTIVE = "isActive";
	private static final String TAX_AGENCY = "taxAgency";
	private static final String VAT_RETURN_BOX = "vatReturnBox";
	private static final String TAX_AGENCIES = "taxAgencies";
	private static final String VAT_RETURN_BOXES = "vatReturnBoxes";
	private static final int VALUES_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(NAME, false, true));
		list.add(new Requirement(DESCRIPTION, true, true));
		list.add(new Requirement(TAX_RATE, false, true));
		list.add(new Requirement(IS_ACTIVE, true, true));
		list.add(new Requirement(TAX_AGENCY, false, true));
		if (isUkCompany()) {
			list.add(new Requirement(IS_PERCENTAGE, true, true));
			list.add(new Requirement(VAT_RETURN_BOX, false, true));
		}
	}

	private boolean isUkCompany() {
		Company company = new FinanceTool().getCompany();
		int accountingType = company.getAccountingType();
		if (accountingType == 1) {
			return true;
		}
		return false;
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = nameRequirement(context);
		if (result != null) {
			return result;
		}

		result = taxRateRequirement(context);
		if (result != null) {
			return result;
		}

		result = taxAgencyRequirement(context);
		if (result != null) {
			return result;
		}

		if (isUkCompany()) {
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

	private Result vatReturnBoxRequirement(Context context) {
		Requirement vatReturnBoxReq = get(VAT_RETURN_BOX);
		VATReturnBox vatReturnBox = context.getSelection(VAT_RETURN_BOXES);
		if (vatReturnBox != null) {
			vatReturnBoxReq.setValue(vatReturnBox);
		}
		if (!vatReturnBoxReq.isDone()) {
			return getVatReturnBoxResult(context);
		}
		return null;
	}

	private Result taxAgencyRequirement(Context context) {
		Requirement taxAgencyReq = get(TAX_AGENCY);
		TAXAgency taxAgency = context.getSelection(TAX_AGENCIES);
		if (taxAgency != null) {
			taxAgencyReq.setValue(taxAgency);
		}
		if (!taxAgencyReq.isDone()) {
			return getTaxAgencyResult(context);
		}
		return null;
	}

	private Result taxRateRequirement(Context context) {
		Requirement taxRateReq = get(TAX_RATE);
		if (!taxRateReq.isDone()) {
			Double taxRate = context.getDouble();
			if (taxRate != null) {
				taxRateReq.setValue(taxRate);
			} else {
				return number(context, "Please Enter the " + getString()
						+ " Rate.", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(TAX_RATE)) {
			taxRateReq.setValue(input);
		}
		return null;
	}

	private Result nameRequirement(Context context) {
		Requirement nameReq = get(NAME);
		if (!nameReq.isDone()) {
			String string = context.getString();
			if (string != null) {
				nameReq.setValue(string);
			} else {
				return text(context, "Please Enter the " + getString()
						+ " Item Name.", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(NAME)) {
			nameReq.setValue(input);
		}
		return null;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

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

		Requirement nameReq = get(NAME);
		String name = (String) nameReq.getValue();
		if (name == selection) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context, "Please Enter the " + getString()
					+ " Item Name.", name);
		}

		Requirement taxRateReq = get(TAX_RATE);
		Double taxRate = (Double) taxRateReq.getValue();
		if (taxRate == selection) {
			context.setAttribute(INPUT_ATTR, TAX_RATE);
			return number(context,
					"Please Enter the " + getString() + " Rate.", "" + taxRate);
		}

		Requirement taxAgencyrReq = get(TAX_AGENCY);
		TAXAgency taxAgency = (TAXAgency) taxAgencyrReq.getValue();
		if (taxAgency == selection) {
			context.setAttribute(INPUT_ATTR, TAX_AGENCY);
			return getTaxAgencyResult(context);
		}

		ResultList list = new ResultList("values");

		Record nameRecord = new Record(name);
		nameRecord.add(INPUT_ATTR, "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);

		Result result = descriptionRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Record taxRateRecord = new Record(taxRate);
		taxRateRecord.add("Name", getString() + " Rate");
		taxRateRecord.add("Value", taxRate);
		list.add(taxRateRecord);

		Record taxAgencyRecord = new Record(taxAgency);
		taxAgencyRecord.add("Name", getString() + " Agency");
		taxAgencyRecord.add("Value", taxAgency);
		list.add(taxAgencyRecord);

		if (isUkCompany()) {
			Requirement vatReturnBoxReq = get(VAT_RETURN_BOX);
			VATReturnBox vatReturnBox = (VATReturnBox) vatReturnBoxReq
					.getValue();
			if (vatReturnBox == selection) {
				context.setAttribute(INPUT_ATTR, VAT_RETURN_BOX);
				return getVatReturnBoxResult(context);
			}
			Record vatReturnBoxRecord = new Record(vatReturnBox);
			vatReturnBoxRecord.add("Name", "VAT Return Box");
			vatReturnBoxRecord.add("Value", vatReturnBox);
			list.add(vatReturnBoxRecord);

			Requirement isPercentageReq = get(IS_PERCENTAGE);
			Boolean isPercentage = (Boolean) isPercentageReq.getDefaultValue();
			if (selection == isPercentage) {
				context.setAttribute(INPUT_ATTR, IS_PERCENTAGE);
				isPercentage = !isPercentage;
				isPercentageReq.setDefaultValue(isPercentage);
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
		Boolean isActive = (Boolean) isActiveReq.getDefaultValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, IS_ACTIVE);
			isActive = !isActive;
			isActiveReq.setDefaultValue(isActive);
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
		result.add(getString()
				+ " Item is ready to create with following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create " + getString() + " Item.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result descriptionRequirement(Context context, ResultList list,
			Object selection) {

		Requirement descriptionReq = get(DESCRIPTION);
		String description = (String) descriptionReq.getDefaultValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(DESCRIPTION)) {
			String desc = context.getSelection(TEXT);
			if (desc == null) {
				desc = context.getString();
			}
			description = desc;
			descriptionReq.setDefaultValue(description);
		}
		if (selection == description) {
			context.setAttribute(INPUT_ATTR, DESCRIPTION);
			return text(context, "Description", description);
		}

		Record descRecord = new Record(description);
		descRecord.add("Name", "Description");
		descRecord.add("Value", description);
		list.add(descRecord);
		return null;
	}

	private Result getVatReturnBoxResult(Context context) {
		Result result = context.makeResult();
		ResultList vatReturnBoxesList = new ResultList(VAT_RETURN_BOXES);

		Object last = context.getLast(RequirementType.VAT_RETURN_BOX);
		if (last != null) {
			vatReturnBoxesList
					.add(createVATReturnBoxRecord((VATReturnBox) last));
		}

		List<VATReturnBox> vatReturnBoxes = getVATReturnBoxes(context
				.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < vatReturnBoxes.size(); i++) {
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

	private List<VATReturnBox> getVATReturnBoxes(Session session) {
		// TODO
		return null;
	}

	private Record createVATReturnBoxRecord(VATReturnBox vatReturnBox) {
		Record record = new Record(vatReturnBox);
		record.add("Name", vatReturnBox.getName());
		return record;
	}

	private Result getTaxAgencyResult(Context context) {
		Result result = context.makeResult();
		ResultList taxAgenciesList = new ResultList(TAX_AGENCIES);

		Object last = context.getLast(RequirementType.TAXAGENCY);
		if (last != null) {
			taxAgenciesList.add(createTaxAgencyRecord((TAXAgency) last));
		}

		List<TAXAgency> taxAgencies = getTaxAgencies(context.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < taxAgencies.size(); i++) {
			TAXAgency taxAgency = taxAgencies.get(i);
			if (taxAgency != last) {
				taxAgenciesList
						.add(createTaxAgencyRecord((TAXAgency) taxAgency));
			}
		}

		int size = taxAgenciesList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the " + getString() + " Agency");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(taxAgenciesList);
		result.add(commandList);
		result.add("Select the " + getString() + " Agency");

		return result;
	}

	private List<TAXAgency> getTaxAgencies(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createTaxAgencyRecord(TAXAgency taxAgency) {
		Record record = new Record(taxAgency);
		record.add("Name", taxAgency.getName());
		return record;
	}

	private Result createVATItem(Context context) {
		TAXItem taxItem = new TAXItem();

		String name = (String) get(NAME).getValue();
		String description = (String) get(DESCRIPTION).getDefaultValue();
		double taxRate = (Double) get(TAX_RATE).getDefaultValue();
		boolean isActive = (Boolean) get(IS_ACTIVE).getDefaultValue();
		TAXAgency taxAgency = (TAXAgency) get(TAX_AGENCY).getValue();

		if (isUkCompany()) {
			boolean isPercentage = (Boolean) get(IS_PERCENTAGE)
					.getDefaultValue();
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

		Session session = context.getSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(taxItem);
		transaction.commit();

		markDone();

		Result result = new Result();
		result.add(getString() + " Item was created successfully.");

		return result;
	}

	private String getString() {
		String s = "TAX";
		if (isUkCompany()) {
			s = "VAT";
		}
		return s;
	}

}
