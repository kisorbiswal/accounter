package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class ReceivedPaymentsListCommand extends NewAbstractCommand {

	private static final int NO_OF_RECORDS_TO_SHOW = 10;
	private static final int STATUS_UNAPPLIED = 0;
	private static final int STATUS_PARTIALLY_APPLIED = 1;
	private static final int STATUS_APPLIED = 2;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {

		return getConstants().receivedPayments();
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(VIEW_BY).setDefaultValue(getConstants().open());

	}

	@Override
	public String getSuccessMessage() {

		return "Success";
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ActionRequirement(VIEW_BY, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().all());
				list.add(getConstants().open());
				list.add(getConstants().fullyApplied());
				list.add(getConstants().voided());

				return list;
			}
		});

		list.add(new ShowListRequirement<ReceivePaymentsList>(getConstants()
				.receivedPayments(), "", NO_OF_RECORDS_TO_SHOW) {

			@Override
			protected String onSelection(ReceivePaymentsList value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getShowMessage() {

				return getConstants().receivedPayments();
			}

			@Override
			protected String getEmptyString() {

				return "No" + getConstants().receivedPayments();
			}

			@Override
			protected Record createRecord(ReceivePaymentsList value) {

				Record record = new Record(value);

				record.add("", (value.getPaymentDate()));
				record.add("", value.getNumber());
				record.add("", value.getCustomerName());
				record.add("", value.getPaymentMethodName());
				record.add("", value.getAmountPaid());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create CustomerPrepayment");
				list.add("Create Receive Payment");

			}

			@Override
			protected boolean filter(ReceivePaymentsList e, String name) {
				return e.getCustomerName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<ReceivePaymentsList> getLists(Context context) {
				return getListData(context);
			}
		});
	}

	protected List<ReceivePaymentsList> getListData(Context context) {

		String currentView = get(VIEW_BY).getValue();
		FinanceTool tool = new FinanceTool();
		List<ReceivePaymentsList> result = new ArrayList<ReceivePaymentsList>();
		try {
			List<ReceivePaymentsList> receivePaymentsLists = tool
					.getCustomerManager().getReceivePaymentsList(
							context.getCompany().getID());
			if (receivePaymentsLists != null) {
				for (ReceivePaymentsList recievePayment : receivePaymentsLists) {
					if (currentView.equals(getConstants().open())) {
						if ((recievePayment.getStatus() == STATUS_UNAPPLIED || recievePayment
								.getStatus() == STATUS_PARTIALLY_APPLIED)
								&& (!recievePayment.isVoided()))
							result.add(recievePayment);
						continue;
					}
					if (currentView.equals(getConstants().fullyApplied())) {
						if (recievePayment.getStatus() == STATUS_APPLIED
								&& !recievePayment.isVoided())
							result.add(recievePayment);
						continue;
					}
					if (currentView.equals(getConstants().voided())) {
						if (recievePayment.isVoided()
								&& !recievePayment.isDeleted())
							result.add(recievePayment);
						continue;
					}
					if (currentView.equals(getConstants().all())) {
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

}