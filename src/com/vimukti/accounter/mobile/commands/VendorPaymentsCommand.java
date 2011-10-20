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
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.server.FinanceTool;

public class VendorPaymentsCommand extends AbstractTransactionCommand {

	private static final String VIEW_BY = "ViewBy";

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

		result = getVendorPaymentsList(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result getVendorPaymentsList(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			case ISSUED:
				context.setAttribute(VIEW_BY, 1);
				break;
			case NOT_ISSUED:
				context.setAttribute(VIEW_BY, 2);
				break;
			case VOIDED:
				context.setAttribute(VIEW_BY, 3);
				break;
			case ALL:
				context.setAttribute(VIEW_BY, null);
				break;
			default:
				break;
			}
		}
		Result result = vendorPaymentsList(context, selection);
		return result;
	}

	private Result vendorPaymentsList(Context context, ActionNames selection) {
		Result result = context.makeResult();
		ResultList vendorPaymentsList = new ResultList("vendorPaymentsList");
		result.add(getMessages().vendorPaymentsList(Global.get().Vendor()));

		Integer vendorPaymentType = (Integer) context.getAttribute(VIEW_BY);
		List<PaymentsList> vendorPayments = getVendorPayments(context
				.getCompany().getID(), vendorPaymentType);

		ResultList actions = new ResultList("actions");

		List<PaymentsList> pagination = pagination(context, selection, actions,
				vendorPayments, new ArrayList<PaymentsList>(), VALUES_TO_SHOW);

		for (PaymentsList payment : pagination) {
			vendorPaymentsList.add(createPaymentRecord(payment));
		}

		StringBuilder message = new StringBuilder();
		if (vendorPaymentsList.size() > 0) {
			message.append("Select an Vendor Payment");
		}

		result.add(message.toString());
		result.add(vendorPaymentsList);

		Record inActiveRec = new Record(ActionNames.ISSUED);
		inActiveRec.add("", "Issued Payments");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.NOT_ISSUED);
		inActiveRec.add("", "Not issued Payments");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.VOIDED);
		inActiveRec.add("", "Voided Payments");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", "All Payments");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", "Close");
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add("Add Vendor payment");
		result.add(commandList);
		return result;

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

	private List<PaymentsList> getVendorPayments(long companyId,
			Integer vendorPaymentType) {
		ArrayList<PaymentsList> vendorPaymentsList = null;
		try {
			vendorPaymentsList = new FinanceTool().getVendorManager()
					.getVendorPaymentsList(companyId);

		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (vendorPaymentType == null) {
			return vendorPaymentsList;
		}
		ArrayList<PaymentsList> result = new ArrayList<PaymentsList>();
		for (PaymentsList paymentsList : vendorPaymentsList) {
			if (vendorPaymentType == 1) {
				if (Utility.getStatus(paymentsList.getType(),
						paymentsList.getStatus()).equalsIgnoreCase("Issued")
						&& !paymentsList.isVoided()) {
					result.add(paymentsList);
				}
			}
			if (vendorPaymentType == 2) {
				if (Utility.getStatus(paymentsList.getType(),
						paymentsList.getStatus())
						.equalsIgnoreCase("Not Issued")
						&& !paymentsList.isVoided()) {
					result.add(paymentsList);
				}
			}
			if (vendorPaymentType == 3) {
				if (paymentsList.isVoided()
						&& paymentsList.getStatus() != ClientTransaction.STATUS_DELETED) {
					result.add(paymentsList);
				}
			}

		}
		return result;

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
