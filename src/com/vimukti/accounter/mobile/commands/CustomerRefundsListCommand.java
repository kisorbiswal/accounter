package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.server.FinanceTool;

public class CustomerRefundsListCommand extends AbstractCommand {

	private static final String CURRENT_VIEW = "Current View";

	private static final String ISSUED = "Issued";
	private static final String NOT_ISSUED = "Not issued";
	private static final String VOIDED = "Voided";
	private static final String ALL = "All";

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
				markDone();
				return null;
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

		for (CustomerRefundsList customer : filterList) {

			resultList.add(createCustomerRefundRecord(customer));

		}

		StringBuilder message = new StringBuilder();
		if (resultList.size() > 0) {
			message.append("Select a Customer Refund record");
		}

		result.add(message.toString());
		result.add(resultList);

		CommandList commandList = new CommandList();
		commandList.add("Add a New Customer Refund record");
		result.add(commandList);
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

	private Result customerRefunds(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Payments List");
		ResultList billsListData = new ResultList("customerRefunds");
		int num = 0;

		List<CustomerRefundsList> customerRefunds = getCustomerRefunds(
				viewType, context.getCompany());

		for (CustomerRefundsList cusRef : customerRefunds) {
			billsListData.add(createCustomerRefundRecord(cusRef));
			num++;
			if (num == VALUES_TO_SHOW) {
				break;
			}
		}

		int size = billsListData.size();
		StringBuilder message = new StringBuilder();
		if (size == 0) {
			message.append("No records to show.");
			result.add(message.toString());
			return result;
		}
		CommandList commandList = new CommandList();
		commandList.add("Create New");

		result.add(message.toString());
		result.add(billsListData);
		result.add(commandList);

		return result;
	}

	private List<CustomerRefundsList> getCustomerRefunds(String type,
			Company company) {

		ArrayList<CustomerRefundsList> refundsList = null;
		ArrayList<CustomerRefundsList> result = new ArrayList<CustomerRefundsList>();
		try {
			refundsList = new FinanceTool().getCustomerManager()
					.getCustomerRefundsList(company.getID());
		} catch (DAOException e) {

			e.printStackTrace();
		}

		for (CustomerRefundsList refund : refundsList) {
			if (type.equals(Accounter.constants().all())) {
				result.add(refund);
			} else if (type.equals(Accounter.constants().notIssued())) {
				if ((refund.getStatus() == STATUS_NOT_ISSUED || refund
						.getStatus() == STATUS_PARTIALLY_PAID)
						&& (!refund.isVoided())) {
					result.add(refund);
				}
			} else if (type.equals(Accounter.constants().issued())) {
				if (refund.getStatus() == STATUS_ISSUED && (!refund.isVoided()))
					result.add(refund);
			} else if (type.equals(Accounter.constants().voided())) {
				if (refund.isVoided() && !refund.isDeleted())
					result.add(refund);
			}
		}
		return result;

	}

	private Record createCustomerRefundRecord(CustomerRefundsList customerRefund) {
		Record record = new Record(customerRefund);
		record.add("Payment Date", customerRefund.getPaymentDate());
		record.add("Payment No.", customerRefund.getPaymentNumber());
		record.add("Status", Utility.getStatus(
				ClientTransaction.TYPE_CUSTOMER_REFUNDS,
				customerRefund.getStatus()));
		record.add("Issue Date", customerRefund.getIssueDate());
		record.add("Name", customerRefund.getName());
		record.add("Type",
				Utility.getTransactionName((customerRefund.getType())));
		record.add("Payment Method", customerRefund.getPaymentMethod());
		record.add("Amount Paid", customerRefund.getAmountPaid());
		record.add("Voided", customerRefund.isVoided() ? "Voided"
				: "Not Voided");
		return record;
	}

	@Override
	protected List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add(Accounter.constants().issued());
		list.add(Accounter.constants().notIssued());
		list.add(Accounter.constants().Voided());
		list.add(Accounter.constants().all());

		return list;
	}

}
