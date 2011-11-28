package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseOrderListCommand extends NewAbstractCommand {

	private static final String CURRENT_VIEW = "currentView";
	private static String OPEN = "Open";
	private static String COMPLETED = "Completed";
	private static String CANCELLED = "Cancelled";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<PurchaseOrdersList>(
				"purchaseOrderList", getMessages().pleaseSelect(
						getMessages().purchaseOrder()), 5) {
			@Override
			protected Record createRecord(PurchaseOrdersList value) {
				Record record = new Record(value);
				record.add(getMessages().number(), value.getNumber());
				record.add(getMessages().name(), value.getVendorName());
				record.add(getMessages().purchasePrice(),
						value.getPurchasePrice());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("New PurchaseOrder");
			}

			@Override
			protected boolean filter(PurchaseOrdersList e, String name) {
				return e.getVendorName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<PurchaseOrdersList> getLists(Context context) {
				List<PurchaseOrdersList> list = new ArrayList<PurchaseOrdersList>();
				List<PurchaseOrdersList> completeList = getPurchaseOrder(context);
				String type = PurchaseOrderListCommand.this.get(CURRENT_VIEW)
						.getValue();
				for (PurchaseOrdersList order : completeList) {

					if (type.equals(OPEN)) {
						if (order.getStatus() == ClientTransaction.STATUS_OPEN
								|| order.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
							list.add(order);
					}
					if (type.equals(COMPLETED)) {
						if (order.getStatus() == ClientTransaction.STATUS_COMPLETED)
							list.add(order);
					}
					if (type.equals(CANCELLED)) {
						if (order.getStatus() == ClientTransaction.STATUS_CANCELLED)
							list.add(order);
					}
				}
				return list;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().purchaseOrderList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(PurchaseOrdersList value) {
				return null;
			}
		});

		list.add(new CommandsRequirement(CURRENT_VIEW, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add("Open");
				list.add("Completed");
				list.add("Cancelled");
				return list;
			}
		});

	}

	private List<PurchaseOrdersList> getPurchaseOrder(Context context) {
		FinanceTool tool = new FinanceTool();
		try {
			return tool.getPurchageManager().getPurchaseOrdersList(
					context.getCompany().getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
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
		get(CURRENT_VIEW).setDefaultValue("Open");
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

}
