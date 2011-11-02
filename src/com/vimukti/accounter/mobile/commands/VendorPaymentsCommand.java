package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class VendorPaymentsCommand extends NewAbstractCommand {


	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().vendorPaymentList(Global.get().Vendor());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().vendorPaymentList(Global.get().Vendor());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(VIEW_BY).setDefaultValue(getConstants().all());
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
				list.add(getConstants().notIssued());
				list.add(getConstants().issued());
				list.add(getConstants().voided());
				list.add(getConstants().all());
				return list;
			}
		});

		list.add(new ShowListRequirement<PaymentsList>(getMessages()
				.vendorPaymentList(Global.get().Vendor()), "", 10) {

			@Override
			protected String onSelection(PaymentsList value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().vendorPaymentList(Global.get().Vendor());
			}

			@Override
			protected String getEmptyString() {
				return "No Payments are available";
			}

			@Override
			protected Record createRecord(PaymentsList p) {
				Record payment = new Record(p);
				payment.add("", p.getPaymentDate());
				payment.add("", p.getPaymentNumber());
				payment.add("", p.getStatus());
				payment.add("", p.getIssuedDate());
				payment.add("", p.getName());
				payment.add("", p.getPaymentMethodName());
				payment.add("", p.getAmountPaid());
				return payment;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Add Vendor payment");
			}

			@Override
			protected boolean filter(PaymentsList e, String name) {
				return e.getName().startsWith(name)
						|| e.getPaymentNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<PaymentsList> getLists(Context context) {
				return getListData(context);
			}
		});
	}

	protected List<PaymentsList> getListData(Context context) {

		String currentView = get(VIEW_BY).getValue();
		FinanceTool tool = new FinanceTool();
		List<PaymentsList> result = new ArrayList<PaymentsList>();
		List<PaymentsList> paymentsLists = null;
		try {
			paymentsLists = tool.getVendorManager().getVendorPaymentsList(
					context.getCompany().getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (paymentsLists != null) {
			for (PaymentsList list : paymentsLists) {
				if (currentView.equals(getConstants().notIssued())) {
					if (Utility.getStatus(list.getType(), list.getStatus())
							.equals(getConstants().notIssued())
							&& !list.isVoided()) {
						result.add(list);
					}
				} else if (currentView.equals(getConstants().issued())) {
					if (Utility.getStatus(list.getType(), list.getStatus())
							.equalsIgnoreCase(getConstants().issued())
							&& !list.isVoided()) {
						result.add(list);
					}
				} else if (currentView.equals(getConstants().voided())) {
					if (list.isVoided()
							&& list.getStatus() != ClientTransaction.STATUS_DELETED) {
						result.add(list);
					}
				} else if (currentView.equalsIgnoreCase(getConstants().all())) {
					result.addAll(paymentsLists);
				}
			}
			return result;
		}
		return null;

	}

}
