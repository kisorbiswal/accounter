package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.server.FinanceTool;

public class TransactionDetailbyTaxItemReportCommand extends
		NewAbstractReportCommand<TransactionDetailByTaxItem> {
	private String taxItemName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
	}

	protected Record createReportRecord(TransactionDetailByTaxItem record) {

		Record tdRecord = new Record(record);

		tdRecord.add(ReportUtility.getTransactionName(record
				.getTransactionType()));
		tdRecord.add(getMessages().taxRate(), record.getRate());
		tdRecord.add(getMessages().date(), record.getDate());
		tdRecord.add(getMessages().number(), record.getNumber());
		tdRecord.add(getMessages().taxItemName(), record.getTaxItemName());
		tdRecord.add(getMessages().memo(), record.getMemo());
		tdRecord.add(getMessages().salesTax(), record.getSalesTaxAmount());
		tdRecord.add(getMessages().taxable(), record.getTaxableAmount());

		return tdRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<TransactionDetailByTaxItem> getRecords() {
		ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItems = new ArrayList<TransactionDetailByTaxItem>();
		try {
			if (taxItemName == null && taxItemName.isEmpty()) {
				transactionDetailByTaxItems = new FinanceTool()
						.getReportManager().getTransactionDetailByTaxItem(
								getStartDate(), getEndDate(), getCompanyId());
			} else if (taxItemName != null) {
				transactionDetailByTaxItems = new FinanceTool()
						.getReportManager().getTransactionDetailByTaxItem(
								taxItemName, getStartDate(), getEndDate(),
								getCompanyId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDetailByTaxItems;
	}

	protected String addCommandOnRecordClick(
			TransactionDetailByTaxItem selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			taxItemName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().transactionDetailByTaxItem());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().transactionDetailByTaxItem());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().transactionDetailByTaxItem());
	}

}
