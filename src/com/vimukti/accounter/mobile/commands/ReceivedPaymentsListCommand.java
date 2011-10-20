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
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class ReceivedPaymentsListCommand extends AbstractTransactionCommand {

	private static final String CURRENT_VIEW = "currentView";
	private static String ALL = "all";
	private static String OPEN = "open";
	private static String FULLY_APPLIED = "fullyApplied";
	private static String VOIDED = "voided";

	private static final int STATUS_UNAPPLIED = 0;
	private static final int STATUS_PARTIALLY_APPLIED = 1;
	private static final int STATUS_APPLIED = 2;

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

		Result result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createOptionalResult(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");
		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			case OPEN:
				context.setAttribute(CURRENT_VIEW, "open");
				break;
			case VOIDED:
				context.setAttribute(CURRENT_VIEW, "voided");
				break;
			case FULLY_APPLIED:
				context.setAttribute(CURRENT_VIEW, "fullyApplied");
				break;
			case ALL:
				context.setAttribute(CURRENT_VIEW, null);
				break;
			default:
				break;
			}
		}

		Result result = receivePaymentsList(context, selection);
		return result;
	}

	@Override
	protected List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add("All");
		list.add("Open");
		list.add("Voided");
		list.add("OverDue");

		return list;
	}

	private Result receivePaymentsList(Context context, ActionNames selection) {

		Result result = context.makeResult();
		result.add(getConstants().receivePaymentList());
		ResultList receivedPaymentsListData = new ResultList("receivedPayments");

		String currentView = (String) context.getAttribute(CURRENT_VIEW);
		List<ReceivePaymentsList> paymentsLists = getReceivePaymentsList(
				context, currentView);

		ResultList actions = new ResultList("actions");

		List<ReceivePaymentsList> paginationList = pagination(context,
				selection, receivedPaymentsListData, paymentsLists,
				new ArrayList<ReceivePaymentsList>(), VALUES_TO_SHOW);

		for (ReceivePaymentsList receivedPayment : paginationList) {
			receivedPaymentsListData
					.add(createReceivePaymentRecord(receivedPayment));
		}

		result.add(receivedPaymentsListData);

		Record inActiveRec = new Record(ActionNames.OPEN);
		inActiveRec.add("", getConstants().open());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.VOIDED);
		inActiveRec.add("", getConstants().voided());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FULLY_APPLIED);
		inActiveRec.add("", getConstants().fullyApplied());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", getConstants().all());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add(getConstants().newReceivePayment());
		result.add(commandList);

		return result;
	}

	private List<ReceivePaymentsList> getReceivePaymentsList(Context context,
			String currentView) {
		FinanceTool tool = new FinanceTool();
		List<ReceivePaymentsList> result = new ArrayList<ReceivePaymentsList>();
		try {
			List<ReceivePaymentsList> receivePaymentsLists = tool
					.getCustomerManager().getReceivePaymentsList(
							context.getCompany().getID());
			if (currentView == null) {
				return receivePaymentsLists;
			}

			if (receivePaymentsLists != null) {
				for (ReceivePaymentsList recievePayment : receivePaymentsLists) {
					if (currentView.equals(OPEN)) {
						if ((recievePayment.getStatus() == STATUS_UNAPPLIED || recievePayment
								.getStatus() == STATUS_PARTIALLY_APPLIED)
								&& (!recievePayment.isVoided()))
							result.add(recievePayment);
						continue;
					}
					if (currentView.equals(FULLY_APPLIED)) {
						if (recievePayment.getStatus() == STATUS_APPLIED
								&& !recievePayment.isVoided())
							result.add(recievePayment);
						continue;
					}
					if (currentView.equals(VOIDED)) {
						if (recievePayment.isVoided()
								&& !recievePayment.isDeleted())
							result.add(recievePayment);
						continue;
					}
					if (currentView.equals(ALL)) {
						result.add(recievePayment);
					}
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Record createReceivePaymentRecord(ReceivePaymentsList receivepayment) {

		Record record = new Record(receivepayment);

		record.add("PaymentDate",
				getDateAsString(receivepayment.getPaymentDate()));
		record.add("Number", receivepayment.getNumber());
		record.add("CustomerName", receivepayment.getCustomerName());
		record.add("PaymentMethod", receivepayment.getPaymentMethodName());
		record.add("AmountPaid", receivepayment.getAmountPaid());
		return record;
	}
}
