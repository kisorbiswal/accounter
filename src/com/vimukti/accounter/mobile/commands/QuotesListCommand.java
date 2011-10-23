package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.server.FinanceTool;


public class QuotesListCommand extends AbstractTransactionCommand {

	private static final String CURRENT_VIEW = "Current View";

	private static final int STATUS_OPEN = 0;
	private static final int STATUS_REJECTED = 1;
	private static final int STATUS_ACCECPTED = 2;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(CURRENT_VIEW, true, true));
	}

	@Override
	public Result run(Context context) {

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = null;
		setDefaultValues();
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;

	}

	private void setDefaultValues() {
		get(CURRENT_VIEW).setDefaultValue(OPEN);

	}

	private Result createOptionalResult(Context context) {

		List<String> viewType = new ArrayList<String>();
		viewType.add(OPEN);
		viewType.add(REJECTED);
		viewType.add(ACCEPTED);
		viewType.add(EXPIRED);
		viewType.add(ALL);

		ResultList resultList = new ResultList("values");
		Object selection = context.getSelection(ACTIONS);
		ActionNames actionNames;
		if (selection != null) {
			actionNames = (ActionNames) selection;
			switch (actionNames) {
			case FINISH:
				return closeCommand();
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		Result result = stringListOptionalRequirement(context, resultList,
				selection, CURRENT_VIEW, "Current View", viewType,
				"Select View type", ITEMS_TO_SHOW);
		if (result != null) {
			return result;
		}

		String view = get(CURRENT_VIEW).getValue();
		result = getEstimatesList(context, view);
		result.add(resultList);
		return result;
	}

	private Result getEstimatesList(Context context, String view) {
		ArrayList<ClientEstimate> estimates = getEstimates(context);

		Result result = context.makeResult();
		ResultList resultList = new ResultList("quotesList");
		ResultList actions = new ResultList("actions");

		ArrayList<ClientEstimate> list = filterList(estimates, view);
		for (ClientEstimate estimate : list) {

			resultList.add(createEstimateRecord(estimate));

		}

		StringBuilder message = new StringBuilder();
		if (resultList.size() > 0) {
			message.append("Select a Quote");
		}

		result.add(message.toString());
		result.add(resultList);

		Record finishRecord = new Record(ActionNames.FINISH);
		finishRecord.add("", getConstants().close());
		actions.add(finishRecord);

		CommandList commandList = new CommandList();
		commandList.add("Add a New Quote");
		result.add(commandList);

		result.add(actions);

		return result;
	}

	public ArrayList<ClientEstimate> getEstimates(Context context) {
		List<ClientEstimate> clientEstimate = new ArrayList<ClientEstimate>();
		List<Estimate> serverEstimates = null;

		try {

			serverEstimates = new FinanceTool().getCustomerManager()
					.getEstimates(context.getCompany().getID());
			for (Estimate estimate : serverEstimates) {
				clientEstimate.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientEstimate>(clientEstimate);
	}

	private ArrayList<ClientEstimate> filterList(
			ArrayList<ClientEstimate> list, String text) {

		ArrayList<ClientEstimate> estimatesList = new ArrayList<ClientEstimate>();

		for (ClientEstimate estimate : list) {
			if (text.equals(OPEN)) {
				if (estimate.getStatus() == STATUS_OPEN)
					estimatesList.add(estimate);
				continue;
			}
			if (text.equals(REJECTED)) {
				if (estimate.getStatus() == STATUS_REJECTED)
					estimatesList.add(estimate);
			}
			if (text.equals(ACCEPTED)) {
				if (estimate.getStatus() == STATUS_ACCECPTED)
					estimatesList.add(estimate);
			}
			if (text.equals(EXPIRED)) {
				ClientFinanceDate expiryDate = new ClientFinanceDate(
						estimate.getExpirationDate());
				if (expiryDate.before(new ClientFinanceDate()))
					estimatesList.add(estimate);
			}
			if (text.equals(ALL)) {
				estimatesList.add(estimate);
			}
		}

		return estimatesList;

	}

	private Record createEstimateRecord(ClientEstimate est) {
		Record estrecord = new Record(est);

		estrecord.add("", getConstants().date());
		estrecord.add("", est.getDate());
		estrecord.add("", getConstants().number());
		estrecord.add("", est.getNumber());

		ClientCustomer customer = getClientCompany().getCustomer(
				est.getCustomer());
		estrecord.add("", getMessages().customerName(Global.get().Customer()));
		estrecord.add("", customer != null ? customer.getName() : "");
		estrecord.add("", getConstants().phone());
		estrecord.add("", est.getPhone());
		estrecord.add("", getConstants().expirationDate());
		estrecord.add("", new ClientFinanceDate(est.getExpirationDate()));
		estrecord.add("", getConstants().deliveryDate());
		estrecord.add("", new ClientFinanceDate(est.getDeliveryDate()));
		estrecord.add("", getConstants().total());
		estrecord.add("", est.getTotal());
		return estrecord;

	}

}
