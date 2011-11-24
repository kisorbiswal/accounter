package com.vimukti.accounter.mobile.commands.reports;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class VATItemDetailReportCommand extends
		NewAbstractReportCommand<VATItemDetail> {
	TAXItem taxItem;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
	}

	@Override
	protected Record createReportRecord(VATItemDetail record) {
		Record ecRecord = new Record(record);
		ecRecord.add("Type",
				Utility.getTransactionName(record.getTransactionType()));
		ecRecord.add("Date", record.getDate());
		ecRecord.add("No", record.getTransactionNumber());
		ecRecord.add("Name", record.getName());
		ecRecord.add("Memo", record.getMemo());
		ecRecord.add("Amount", record.getAmount());
		ecRecord.add("Sales Price", record.getSalesPrice());
		return ecRecord;
	}

	@Override
	protected List<VATItemDetail> getRecords() {
		if (taxItem == null) {
			try {
				return new FinanceTool().getReportManager()
						.getVATItemDetailReport(getStartDate(), getEndDate(),
								getCompanyId());
			} catch (DAOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				return new FinanceTool().getReportManager()
						.getVATItemDetailReport(taxItem.getName(),
								getStartDate(), getEndDate(), getCompanyId());
			} catch (DAOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return new ArrayList<VATItemDetail>();
	}

	@Override
	protected String addCommandOnRecordClick(VATItemDetail selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String getEmptyString() {
		return "You don't have any VAT Item detail reports";
	}

	@Override
	protected String getShowMessage() {
		return null;
	}

	@Override
	protected String getSelectRecordString() {
		return "Select a report to see the Transaction details";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String accountName = null;
		String string = context.getString();
		if (string != null) {
			String[] split = string.split(",");
			if (split.length > 1) {
				context.setString(split[0]);
				accountName = split[1];
			}
		}
		if (accountName != null) {
			Set<TAXItem> taxItems = context.getCompany().getTaxItems();
			for (TAXItem taxItem : taxItems) {
				if (taxItem.getName().equalsIgnoreCase(accountName)) {
					this.taxItem = taxItem;
				}
			}
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "VAT Item detail report command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "VAT Item report details";
	}

	@Override
	public String getSuccessMessage() {
		return "VAT Item detail report command closed successfully";
	}

}
