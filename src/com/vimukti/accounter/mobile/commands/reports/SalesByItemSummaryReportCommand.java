package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesByItemSummaryReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ShowListRequirement<SalesByCustomerDetail>("Result", null,
				40) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Result run = super.run(context, makeResult, list, actions);
				makeResult.add("Total : " + total);
				return run;
			}

			@Override
			protected String onSelection(SalesByCustomerDetail value) {
				return addCommandOnRecordClick(value);
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected Record createRecord(SalesByCustomerDetail value) {
				return createReportRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected boolean filter(SalesByCustomerDetail e, String name) {
				return false;
			}

			@Override
			protected List<SalesByCustomerDetail> getLists(Context context) {
				return getRecords();
			}
		});
	}

	double total = 0.0;

	protected Record createReportRecord(SalesByCustomerDetail record) {
		total += record.getAmount();
		Record salesRecord = new Record(record);
		salesRecord.add(getMessages().item(), record.getItemName());
		salesRecord.add(getMessages().quantity(), record.getQuantity());
		salesRecord.add(getMessages().amount(), record.getAmount());
		return salesRecord;
	}

	protected List<SalesByCustomerDetail> getRecords() {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetails = new ArrayList<SalesByCustomerDetail>();
		try {
			salesByCustomerDetails = new FinanceTool().getSalesManager()
					.getSalesByItemSummary(getStartDate(), getEndDate(),
							getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByCustomerDetails;
	}

	protected String addCommandOnRecordClick(SalesByCustomerDetail selection) {
		return "Sales By Item Detail ," + selection.getItemName();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().salesByItemSummary());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().salesByItemSummary());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().salesByItemSummary());
	}

}