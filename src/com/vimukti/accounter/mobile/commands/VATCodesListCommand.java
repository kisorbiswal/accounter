package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class VATCodesListCommand extends AbstractCommand {

	private static final String CURRENT_VIEW = "currentView";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createVatCodesList(context);
		return result;
	}

	private Result createVatCodesList(Context context) {

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				return closeCommand();
			case ACTIVE:
				context.setAttribute(CURRENT_VIEW, true);
				break;
			case IN_ACTIVE:
				context.setAttribute(CURRENT_VIEW, false);
				break;
			case ALL:
				context.setAttribute(CURRENT_VIEW, false);
				break;
			default:
				break;
			}
		}
		Result result = vatCodesList(context, selection);
		return result;
	}

	private Result vatCodesList(Context context, ActionNames selection) {

		Result result = context.makeResult();
		ResultList vatCodesList = new ResultList("vatCodesList");
		result.add(getConstants().vatCodeList());

		Boolean isActive = (Boolean) context.getAttribute(CURRENT_VIEW);
		List<TAXCode> vatCodes = getVatCodes(context.getCompany(), isActive);

		ResultList actions = new ResultList("actions");

		List<TAXCode> pagination = pagination(context, selection, vatCodesList,
				vatCodes, new ArrayList<TAXCode>(), VALUES_TO_SHOW);

		for (TAXCode taxCode : pagination) {
			vatCodesList.add(createVatCodeRecord(taxCode));
		}

		result.add(vatCodesList);

		Record inActiveRec = new Record(ActionNames.ACTIVE);
		inActiveRec.add("", getConstants().active());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.IN_ACTIVE);
		inActiveRec.add("", getConstants().inActive());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", getConstants().all());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add(getConstants().newVATCode());
		result.add(commandList);
		return result;
	}

	private List<TAXCode> getVatCodes(Company company, Boolean isActive) {
		List<TAXCode> result = new ArrayList<TAXCode>();
		Set<TAXCode> taxCodes = company.getTaxCodes();
		if (isActive == null) {
			return new ArrayList<TAXCode>(taxCodes);
		}
		for (TAXCode taxCode : taxCodes) {
			if (isActive) {
				if (taxCode.isActive()) {
					result.add(taxCode);
				}
			} else {
				if (!taxCode.isActive()) {
					result.add(taxCode);
				}
			}
		}
		return result;
	}

	private Record createVatCodeRecord(TAXCode last) {
		Record record = new Record(last);
		record.add("Name", last.getName() != null ? last.getName() : "");
		record.add("Description", last.getDescription());
		return record;
	}

}
