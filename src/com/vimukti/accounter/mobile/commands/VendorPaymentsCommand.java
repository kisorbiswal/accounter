package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class VendorPaymentsCommand extends NewAbstractCommand {
	private String commandString = null;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		commandString = context.getCommandString();
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(VIEW_BY).setDefaultValue(getMessages().notIssued());
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(VIEW_BY) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().notIssued());
				list.add(getMessages().issued());
				list.add(getMessages().voided());
				list.add(getMessages().all());
				return list;
			}
		});

		list.add(new ShowListRequirement<PaymentsList>(getMessages()
				.payeePaymentList(Global.get().Vendor()), "", 20) {
			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// PaymentsList value) {
			// commandList.add(new UserCommand("update Payment", String
			// .valueOf(value.getTransactionId())));
			// }

			@Override
			protected String onSelection(PaymentsList value) {
				return "Edit Transaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {
				if (commandString.contains("Vendor")) {
					return getMessages()
							.payeePaymentList(Global.get().Vendor());
				} else {
					return getMessages().paymentsList();
				}

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
				payment.add("", Utility.getStatus(p.getType(), p.getStatus()));
				payment.add("", p.getIssuedDate());
				payment.add("", p.getName());
				payment.add("", Utility.getTransactionName(p.getType()));
				payment.add("", p.getPaymentMethodName());
				payment.add("", p.getCheckNumber());
				payment.add("", p.getAmountPaid());
				payment.add(getMessages().paymentDate(), p.getPaymentDate());
				payment.add(getMessages().paymentNo(), p.getPaymentNumber());
				payment.add(getMessages().status(),
						Utility.getStatus(p.getType(), p.getStatus()));
				payment.add(getMessages().issueDate(), p.getIssuedDate());
				payment.add(getMessages().name(), p.getName());
				payment.add(getMessages().transactionName(),
						Utility.getTransactionName(p.getType()));
				payment.add(getMessages().paymentMethod(),
						p.getPaymentMethodName());
				payment.add(getMessages().checkNo(), p.getCheckNumber());
				payment.add(getMessages().amountPaid(), p.getAmountPaid());
				return payment;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				if (commandString.contains("Vendor")) {
					list.add("Add Vendor payment");
				} else {
					list.add("Add a New Payment");
				}

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

	/**
	 * get payments
	 * 
	 * @param context
	 * 
	 * @return {@link List<paymentsList>}
	 */
	protected List<PaymentsList> getListData(Context context) {

		String currentView = get(VIEW_BY).getValue();
		FinanceTool tool = new FinanceTool();
		List<PaymentsList> result = new ArrayList<PaymentsList>();
		List<PaymentsList> paymentsLists = null;
		try {
			if (commandString.contains("Vendor")) {
				paymentsLists = tool.getVendorManager().getVendorPaymentsList(
						context.getCompany().getId());
			} else {
				paymentsLists = tool.getCustomerManager().getPaymentsList(
						context.getCompany().getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (paymentsLists != null) {
			for (PaymentsList list : paymentsLists) {
				if (currentView.equals(getMessages().notIssued())) {
					if (((Utility.getStatus(list.getType(), list.getStatus())
							.equalsIgnoreCase(getMessages().notIssued())) && !(list
							.isVoided()))) {
						result.add(list);
					}
				} else if (currentView.equals(getMessages().issued())) {
					if (Utility.getStatus(list.getType(), list.getStatus())
							.equalsIgnoreCase(getMessages().issued())
							&& !list.isVoided()) {
						result.add(list);
					}
				} else if (currentView.equalsIgnoreCase(getMessages().voided())) {
					if (list.isVoided()
							&& list.getStatus() != ClientTransaction.STATUS_DELETED) {
						result.add(list);
					}
				} else if (currentView.equalsIgnoreCase(getMessages().all())) {
					result.add(list);
				}
			}
			return result;
		}
		return null;

	}
}
