package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class QuotesListCommand extends AbstractTransactionCommand {

	private static final String VIEW_BY = "ViewBy";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(VIEW_BY, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(VIEW_BY);
		ResultList list = new ResultList("viewlist");
		Result result = viewTypeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		String viewType = get(VIEW_BY).getValue();
		result = qoutesList(context, viewType);
		return result;
	}

	private Result qoutesList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Qoutes List");
		ResultList billsListData = new ResultList("QoutesList");
		int num = 0;
		List<Estimate> estimates = getEstimates(viewType, context.getCompany());
		for (Estimate est : estimates) {
			billsListData.add(createEstimateRecord(est));
			num++;
			if (num == ESTIMATES_TO_SHOW) {
				break;
			}
		}

		int size = billsListData.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Estimate");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(billsListData);
		result.add(commandList);
		result.add("Type for Estimate");

		return result;
	}

	private Record createEstimateRecord(Estimate est) {
		Record estrecord = new Record(est);
		estrecord.add("Date", est.getDate());
		estrecord.add("Number", est.getNumber());
		estrecord.add("VendorName", est.getPayee().getName());
		estrecord.add("Phone", est.getPhone());
		estrecord.add("ExpirationDate", est.getExpirationDate());
		estrecord.add("DeliveryDate", est.getDeliveryDate());
		estrecord.add("Total", est.getTotal());
		return estrecord;

	}

	@Override
	protected List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add("All");
		list.add("Open");
		list.add("Rejected");
		list.add("Accepted");
		list.add("Expired");

		return list;
	}

}
