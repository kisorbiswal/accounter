package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;

public class VendorPaymentsCommand extends AbstractTransactionCommand {

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
		result = payments(context, viewType);
		return result;
	}

	private Result payments(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Payments List");
		ResultList billsListData = new ResultList("payments");
		int num = 0;

		List<PaymentsList> payments = getPayments(getType(viewType));

		for (PaymentsList p : payments) {
			billsListData.add(createPaymentRecord(p));
			num++;
			if (num == PAYMENTS_TO_SHOW) {
				break;
			}
		}

		int size = billsListData.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a payment");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create New");

		result.add(message.toString());
		result.add(billsListData);
		result.add(commandList);
		result.add("Type for payment");

		return result;
	}

	private int getType(String viewType) {
		if (viewType.equals(Accounter.constants().issued())) {
			return 2;
		} else if (viewType.equals(Accounter.constants().notIssued())) {
			return 0;
		}
		return 0;
	}

	private Record createPaymentRecord(PaymentsList p) {
		Record payment = new Record(p);
		payment.add("PaymentDate", p.getPaymentDate());
		payment.add("PaymentNumber", p.getPaymentNumber());
		payment.add("Status", p.getStatus());
		payment.add("IssueDate", p.getIssuedDate());
		payment.add("Name", p.getName());
		payment.add("paymentMethod", p.getPaymentMethodName());
		payment.add("AmountPaid", p.getAmountPaid());
		payment.add("Voided", p.isVoided());
		return payment;
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
