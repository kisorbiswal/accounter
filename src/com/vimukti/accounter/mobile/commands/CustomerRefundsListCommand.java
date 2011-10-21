package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.server.FinanceTool;

public class CustomerRefundsListCommand extends AbstractTransactionCommand {

	AccounterConstants constants = getConstants();
	private static final String CURRENT_VIEW = "Current View";

	private static final int ITEMS_TO_SHOW = 4;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CURRENT_VIEW, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;
		setDefaultValues();
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private void setDefaultValues() {
		get(CURRENT_VIEW).setDefaultValue(ISSUED);

	}

	private Result createOptionalResult(Context context) {

		List<String> viewType = new ArrayList<String>();
		viewType.add(NOT_ISSUED);
		viewType.add(ISSUED);
		viewType.add(VOIDED);
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
				selection, CURRENT_VIEW, CURRENT_VIEW, viewType, getMessages()
						.pleaseSelect(CURRENT_VIEW), ITEMS_TO_SHOW);
		if (result != null) {
			return result;
		}

		String view = get(CURRENT_VIEW).getValue();
		result = getCustomerRefundList(context, view);
		result.add(resultList);
		return result;
	}

	private Result getCustomerRefundList(Context context, String view) {
		ArrayList<CustomerRefundsList> list = getCustomerRefundsList(context
				.getCompany().getID());
		ArrayList<CustomerRefundsList> filterList = filterList(view, list);

		Result result = context.makeResult();
		ResultList resultList = new ResultList("customerrefundsList");
		ResultList actions = new ResultList("actions");

		for (CustomerRefundsList customer : filterList) {

			resultList.add(createCustomerRefundRecord(customer));

		}

		StringBuilder message = new StringBuilder();
		if (resultList.size() > 0) {
			message.append(getMessages().pleaseSelect(
					getMessages().customerRefund(Global.get().Customer())));
		}

		result.add(message.toString());
		result.add(resultList);

		CommandList commandList = new CommandList();
		commandList.add(getMessages().addaNewCustomerRefund(
				Global.get().Customer()));
		result.add(commandList);

		Record finishRecord = new Record(ActionNames.FINISH);
		finishRecord.add("", getConstants().close());
		actions.add(finishRecord);

		result.add(actions);

		return result;
	}

	private ArrayList<CustomerRefundsList> filterList(String text,
			ArrayList<CustomerRefundsList> list) {

		ArrayList<CustomerRefundsList> newList = new ArrayList<CustomerRefundsList>();
		for (CustomerRefundsList customerRefund : list) {
			if (text.equals(NOT_ISSUED)) {
				if ((customerRefund.getStatus() == STATUS_NOT_ISSUED || customerRefund
						.getStatus() == STATUS_PARTIALLY_PAID)
						&& (!customerRefund.isVoided()))
					newList.add(customerRefund);
			}
			if (text.equals(ISSUED)) {
				if (customerRefund.getStatus() == STATUS_ISSUED
						&& (!customerRefund.isVoided()))
					newList.add(customerRefund);
			}
			if (text.equals(VOIDED)) {
				if (customerRefund.isVoided() && !customerRefund.isDeleted())
					newList.add(customerRefund);
			}
			if (text.equals(ALL)) {

				newList.add(customerRefund);
			}
		}
		return newList;
	}

	public ArrayList<CustomerRefundsList> getCustomerRefundsList(long companyId) {
		List<CustomerRefundsList> customerRefundsList = null;
		try {

			customerRefundsList = new FinanceTool().getCustomerManager()
					.getCustomerRefundsList(companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CustomerRefundsList>(customerRefundsList);
	}

	private Record createCustomerRefundRecord(CustomerRefundsList customerRefund) {
		Record record = new Record(customerRefund);

		record.add("", getConstants().paymentNo());
		record.add("", customerRefund.getPaymentNumber());

		record.add("", getConstants().paymentDate());
		record.add("", customerRefund.getPaymentDate());

		record.add("", getConstants().issueDate());
		record.add("", customerRefund.getIssueDate());
		record.add("", getConstants().name());
		record.add("", customerRefund.getName());
		record.add("", getConstants().type());
		record.add("", Utility.getTransactionName((customerRefund.getType())));
		record.add("", getConstants().paymentMethod());
		record.add("", customerRefund.getPaymentMethod());
		record.add("", getConstants().amountPaid());
		record.add("", customerRefund.getAmountPaid());
		record.add("", getConstants().status());
		record.add("", Utility.getStatus(
				ClientTransaction.TYPE_CUSTOMER_REFUNDS,
				customerRefund.getStatus()));
		record.add("", getConstants().voided());
		record.add("", customerRefund.isVoided() ? getConstants().voided()
				: getConstants().nonVoided());
		return record;
	}

}
