package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseOrderListCommand extends AbstractTransactionListCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
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
				String type = PurchaseOrderListCommand.this.get(VIEW_BY)
						.getValue();
				int order = -1;
				if (type.equals(getMessages().open())) {
					order = ClientTransaction.STATUS_OPEN;
				}
				if (type.equals(getMessages().completed())) {
					order = ClientTransaction.STATUS_APPLIED;
				}
				if (type.equals(getMessages().cancelled())) {
					order = ClientTransaction.STATUS_CANCELLED;
				}
				return getPurchaseOrder(context, order);

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
	}

	private List<PurchaseOrdersList> getPurchaseOrder(Context context, int order) {
		FinanceTool tool = new FinanceTool();
		try {
			return tool.getPurchageManager().getPurchaseOrdersList(
					context.getCompany().getID(), getStartDate().getDate(),
					getEndDate().getDate(), order, 0, -1);
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
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().open());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().open());
		list.add(getMessages().cancelled());
		list.add(getMessages().completed());
		return list;
	}

}
