package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Company;
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
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.server.FinanceTool;

public class CustomerRefundsListCommand extends AbstractCommand {

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
		result = customerRefunds(context, viewType);
		return result;
	}

	private Result customerRefunds(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Payments List");
		ResultList billsListData = new ResultList("customerRefunds");
		int num = 0;

		List<CustomerRefundsList> customerRefunds = getCustomerRefunds(
				viewType, context.getCompany());

		for (CustomerRefundsList cusRef : customerRefunds) {
			billsListData.add(createCustoemrRefundRecord(cusRef));
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
			refundsList = new FinanceTool().getCustomerRefundsList(company
					.getID());
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

	private Record createCustoemrRefundRecord(CustomerRefundsList customerRefund) {
		Record record = new Record(customerRefund);
		record.add("Payment Date",
				UIUtils.getDateByCompanyType(customerRefund.getPaymentDate()));
		record.add("Payment No.", customerRefund.getPaymentNumber());
		record.add("Status", Utility.getStatus(
				ClientTransaction.TYPE_CUSTOMER_REFUNDS,
				customerRefund.getStatus()));
		record.add("Issue Date",
				UIUtils.getDateByCompanyType(customerRefund.getIssueDate()));
		record.add("Name", customerRefund.getName());
		record.add("Type",
				Utility.getTransactionName((customerRefund.getType())));
		record.add("Payment Method", customerRefund.getPaymentMethod());
		record.add("Amount Paid",
				DataUtils.getAmountAsString(customerRefund.getAmountPaid()));
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
