package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxcode;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class TransactionDetailByTaxCodeServerReport extends
		AbstractFinaneReport<TransactionDetailByTaxcode> {

	private String sectionName = "";

	public TransactionDetailByTaxCodeServerReport(
			IFinanceReport<TransactionDetailByTaxcode> reportView) {
		this.reportView = reportView;
	}

	public TransactionDetailByTaxCodeServerReport(long startDate, long endDate,int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(TransactionDetailByTaxcode record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return ReportUtility.getTransactionName(record.getTransactionType());
		case 1:
			return record.getRate();
		case 2:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 4:
			return record.getTaxCodeName();
		case 5:
			return record.getMemo();
		case 6:
			return record.getSalesTaxAmount();
		case 7:
			return record.getTaxableAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", "Tax Rate", "Date", "No", "Name", "Memo",
				"Sales Tax", "Taxable Amount" };

	}

	@Override
	public String getTitle() {
		return "Transaction Detail By TaxCode";
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// if (navigateObjectName == null) {
		//
		// onSuccess(this.financeTool.getTransactionDetailByTaxcode(start,
		// end));
		// } else {
		// onSuccess(this.financeTool.getTransactionDetailByTaxcode(
		// navigateObjectName, start, end));
		// }
		// } catch (DAOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	@Override
	public void processRecord(TransactionDetailByTaxcode record) {
		if (sectionDepth == 0) {
			addSection("", "Total", new int[] { 6, 7 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getTaxCodeName();
			addSection(record.getTaxAgencyName() + "-" + sectionName, "",
					new int[] { 6, 7 });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getTaxCodeName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(TransactionDetailByTaxcode obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionDetailByTaxcode obj) {
		return obj.getStartDate();
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", "Tax Rate", "Date", "No", "Name", "Memo",
				"Sales Tax", "Taxable Amount" };
	}

}
