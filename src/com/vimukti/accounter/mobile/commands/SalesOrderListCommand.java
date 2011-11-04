package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesOrderListCommand extends NewAbstractCommand {

	private static final String CURRENT_VIEW = "currentView";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		
		list.add(new ActionRequirement(CURRENT_VIEW, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().open());
				list.add(getConstants().completed());
				list.add(getConstants().cancelled());
				return list;
			}
		});
		
		list.add(new ShowListRequirement<SalesOrdersList>("SalesOrderList",
				"Please Select", 5) {
			@Override
			protected Record createRecord(SalesOrdersList value) {
				Record record = new Record(value);
				record.add("", value.getCustomerName());
				record.add("", value.getNumber());
				record.add("", value.getTotal());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("New SalesOrder");
			}

			@Override
			protected boolean filter(SalesOrdersList e, String name) {
				return e.getCustomerName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<SalesOrdersList> getLists(Context context) {
				List<SalesOrdersList> completeList = getSalesOrders(context);
				List<SalesOrdersList> list = new ArrayList<SalesOrdersList>();

				String type = SalesOrderListCommand.this.get(CURRENT_VIEW)
						.getValue();
				for (SalesOrdersList salesOrder : completeList) {
					if (type.equals("Open")) {
						if (salesOrder.getStatus() == Transaction.STATUS_OPEN)
							list.add(salesOrder);
					}
					if (type.equals("Completed")) {
						if (salesOrder.getStatus() == Transaction.STATUS_COMPLETED)
							list.add(salesOrder);
					}
					if (type.equals("Cancelled")) {
						if (salesOrder.getStatus() == Transaction.STATUS_CANCELLED)
							list.add(salesOrder);
					}
				}
			
				return list;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().salesOrderList();
			}

			@Override
			protected String getEmptyString() {
				return getConstants().noRecordsToShow();
			}

			@Override
			protected String onSelection(SalesOrdersList value) {
				return null;
			}
		});

		list.add(new ActionRequirement(CURRENT_VIEW, null) {
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

	private List<SalesOrdersList> getSalesOrders(Context context) {
		FinanceTool tool = new FinanceTool();
		try {
			return tool.getSalesManager().getSalesOrdersList(
					context.getCompany().getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
