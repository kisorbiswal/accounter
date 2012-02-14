package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesOrderListCommand extends AbstractTransactionListCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new ShowListRequirement<SalesOrdersList>("SalesOrderList",
				getMessages().pleaseSelect(""), 5) {
			@Override
			protected Record createRecord(SalesOrdersList value) {
				Record record = new Record(value);
				record.add(getMessages().name(), value.getCustomerName());
				record.add(getMessages().number(), value.getNumber());
				record.add(getMessages().total(), value.getTotal());
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

				String type = SalesOrderListCommand.this.get(VIEW_BY)
						.getValue();
				for (SalesOrdersList salesOrder : completeList) {
					if (type.equals(SalesOrderListCommand.this.getMessages()
							.open())) {
						if (salesOrder.getStatus() == Transaction.STATUS_OPEN)
							list.add(salesOrder);
					}
					if (type.equals(SalesOrderListCommand.this.getMessages()
							.completed())) {
						if (salesOrder.getStatus() == Transaction.STATUS_COMPLETED)
							list.add(salesOrder);
					}
					if (type.equals(SalesOrderListCommand.this.getMessages()
							.cancelled())) {
						if (salesOrder.getStatus() == Transaction.STATUS_CANCELLED)
							list.add(salesOrder);
					}
				}

				return list;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().salesOrderList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(SalesOrdersList value) {
				return null;
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
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().open());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	private List<SalesOrdersList> getSalesOrders(Context context) {
		FinanceTool tool = new FinanceTool();
		try {
			return tool.getSalesManager().getSalesOrdersList(
					context.getCompany().getID(), getStartDate().getDate(),
					getEndDate().getDate());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().open());
		list.add(getMessages().completed());
		list.add(getMessages().cancelled());
		return list;
	}
}
