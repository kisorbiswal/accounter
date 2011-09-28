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

public class VendorPaymentsCommand extends AbstractTransactionCommand {

	private static final String CURRENT_VIEW = "Current View";
	protected static final int PAYMENTS_TO_SHOW = 5;

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
		Result result = optionalRequirement(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result optionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(CURRENT_VIEW);

		ResultList list = new ResultList("viewlist");
		Result result = viewTypeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		String viewType = get(CURRENT_VIEW).getValue();
		result = paymentsList(context, viewType);
		return result;
	}

	private Result paymentsList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("VendorPayments List");
		ResultList paymentList = new ResultList("paymentsList");
		int num = 0;
		List<PaymentsList> payments = getPaymentsList();
		for (PaymentsList bill : payments) {
			paymentList.add(createPaymentRecord(bill));
			num++;

			if (num == PAYMENTS_TO_SHOW) {
				break;
			}
		}

		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(paymentList);
		result.add(commandList);
		result.add("Type for Payment");

		return result;
	}

	private List<PaymentsList> getPaymentsList() {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createPaymentRecord(PaymentsList payment) {

		Record rec = new Record(payment);
		rec.add("Date", payment.getPaymentDate());
		rec.add("Payment No.", payment.getPaymentNumber());
		// TODO need to change to String
		rec.add("Status", payment.getStatus());
		rec.add("Issue Date", payment.getIssuedDate());
		rec.add("Name", payment.getName());
		rec.add("Type", payment.getType());
		rec.add("Payment Method", payment.getPaymentMethodName());
		rec.add("Amount Paid", payment.getAmountPaid());
		rec.add("Voided", payment.isVoided());

		return rec;
	}

	private Result viewTypeRequirement(Context context, ResultList list,
			Object selection) {

		Object viewType = context.getSelection(CURRENT_VIEW);
		Requirement viewReq = get(CURRENT_VIEW);
		String view = viewReq.getValue();

		if (selection == view) {
			return viewTypes(context, view);

		}
		if (viewType != null) {
			view = (String) viewType;
			viewReq.setValue(view);
		}

		Record viewtermRecord = new Record(view);
		viewtermRecord.add("Name", "viewType");
		viewtermRecord.add("Value", view);
		list.add(viewtermRecord);
		return null;
	}

	private Result viewTypes(Context context, String view) {
		ResultList list = new ResultList("viewslist");
		Result result = null;
		List<String> viewTypes = getViewTypes();
		result = context.makeResult();
		result.add("Select View Type");

		int num = 0;
		if (view != null) {
			list.add(createViewTypeRecord(view));
			num++;
		}
		for (String v : viewTypes) {
			if (v != view) {
				list.add(createViewTypeRecord(v));
				num++;
			}
			if (num == 0) {
				break;
			}

		}

		result.add(list);

		return result;
	}

	private Record createViewTypeRecord(String view) {
		Record record = new Record(view);
		record.add("Name", "ViewType");
		record.add("Value", view);
		return record;
	}

	private List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add("Not Issued");
		list.add("Issued");
		list.add("Voided");
		list.add("All");
		return list;
	}

}
