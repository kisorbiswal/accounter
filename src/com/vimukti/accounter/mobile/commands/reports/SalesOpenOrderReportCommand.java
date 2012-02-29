package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesOpenOrderReportCommand extends
		NewAbstractReportCommand<OpenAndClosedOrders> {
	private int status;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(STATUS, getMessages().pleaseSelect(
				getMessages().yourTitle()), getMessages().status(), false,
				true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						if (value.equals(getMessages().open())) {
							status = 1;
						} else if (value.equals(getMessages().completed())) {
							status = 2;
						} else if (value.equals(getMessages().cancelled())) {
							status = 3;
						} else if (value.equals(getMessages().all())) {
							status = 4;
						}
					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> strings = new ArrayList<String>();
				strings.add(getMessages().open());
				strings.add(getMessages().completed());
				strings.add(getMessages().cancelled());
				strings.add(getMessages().all());
				return strings;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<OpenAndClosedOrders>() {

			@Override
			protected String onSelection(OpenAndClosedOrders selection,
					String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<OpenAndClosedOrders> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add("No Records to show");
					return;
				}

			}
		});
	}

	protected Record createReportRecord(OpenAndClosedOrders record) {
		Record openRecord = new Record(record);
		if (record.getTransactionDate() != null)
			openRecord.add(
					getMessages().orderDate(),
					getDateByCompanyType(record.getTransactionDate(),
							getPreferences()));
		else
			openRecord.add("", "");
		openRecord.add(Global.get().Customer(),
				record.getVendorOrCustomerName());
		openRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));

		return openRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<OpenAndClosedOrders> getRecords() {

		ArrayList<OpenAndClosedOrders> openAndClosedOrders = new ArrayList<OpenAndClosedOrders>();
		try {
			if (status == 1) {
				openAndClosedOrders = new FinanceTool().getSalesManager()
						.getSalesOrders(ClientTransaction.STATUS_OPEN,
								getStartDate(), getEndDate(), getCompanyId());
			} else if (status == 2) {
				openAndClosedOrders = new FinanceTool().getSalesManager()
						.getSalesOrders(ClientTransaction.STATUS_COMPLETED,
								getStartDate(), getEndDate(), getCompanyId());
			} else if (status == 3) {
				openAndClosedOrders = new FinanceTool().getSalesManager()
						.getSalesOrders(ClientTransaction.STATUS_CANCELLED,
								getStartDate(), getEndDate(), getCompanyId());
			} else if (status == 4) {
				openAndClosedOrders = new FinanceTool().getSalesManager()
						.getSalesOrders(-1, getStartDate(), getEndDate(),
								getCompanyId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return openAndClosedOrders;

	}

	protected String addCommandOnRecordClick(OpenAndClosedOrders selection) {
		return "updateTransaction " + selection.getTransactionID();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().sales() + getMessages().open());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().sales() + getMessages().open());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().sales() + getMessages().open());
	}

}