package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

/**
 * @author kumar kasimala
 * 
 * 
 */
public class VendorTransactionHistoryServerReport extends
		AbstractFinaneReport<TransactionHistory> {

	protected TransactionHistory transactionHistory;
	private String sectionName = "";

	public VendorTransactionHistoryServerReport(
			IFinanceReport<TransactionHistory> reportView) {
		this.reportView = reportView;
	}

	public VendorTransactionHistoryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				getVendorString(Global.get().vendor(), getConstants().vendor()),
				getConstants().date(), getConstants().type(),
				getConstants().no(),
				// FinanceApplication.constants().reference(),
				Global.get().account(), constants.amount()
		// FinanceApplication.constants().transactionAmount(),
		// FinanceApplication.constants().paidAmount(),
		// FinanceApplication.constants().discount(),
		// FinanceApplication.constants().balance(),
		// FinanceApplication.constants().paymentSatus(),
		// FinanceApplication.constants().PONum(),
		// FinanceApplication.constants().memo(),
		// FinanceApplication.constants().dueDate(),
		// FinanceApplication.constants().debit(),
		// FinanceApplication.constants().credit()
		};

		// "Aging(days)", "Payment Terms",
		// "Vendor Group"
		// FIXME if required add it, "Void", "Reference"

	}

	@Override
	public String getTitle() {
		return messages.supplierTransactionHistory(Global.get().Vendor());
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1)
			return 85;
		else if (index == 3)
			return 60;
		else if (index == 2)
			return 145;
		// else if (index == 4)
		// return 250;
		// else if (index == 4)
		// return 200;
		else if (index == 0)
			return 150;
		else if (index == 5)
			return 145;
		else
			return 175;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getVendorTransactionHistory(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	public Object getColumnData(TransactionHistory record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 2:
			return ReportUtility.getTransactionName(getType(record));
		case 1:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
			// case 3:
			// return record.getReference();
			// return record.getInvoicedAmount();
		case 4:
			return record.getAccount();
			// return record.getPaidAmount();
		case 5:
			return DecimalUtil.isEquals(record.getInvoicedAmount(), 0.0) ? record
					.getPaidAmount() : record.getInvoicedAmount();
			// return record.getDiscount();
			// case 6:
			// return record.getInvoicedAmount() - record.getPaidAmount()
			// - record.getDiscount();
			// case 7:
			// return Utility.getTransactionStatus(record.getType(), record
			// .getStatus());
			// case 8:
			// return record.getNumber();
			// case 9:
			// return record.getMemo();sort
			// case 10:
			// return record.getDueDate();
			// case 11:
			// return record.getDebit();
			// case 12:
			// return record.getCredit();

		}
		return null;
	}

	public void processRecord(TransactionHistory record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "", "" }, new String[] { "", "", "", "",
					getConstants().total() }, new int[] { 5 });
		} else if (sectionDepth == 1) {
			// First time
			this.sectionName = record.getName();
			addSection(new String[] { sectionName }, new String[] { "", "", "",
					"", getConstants().total() }, new int[] { 5 });
		}
		// else if (sectionDepth == 2) {
		// // Inside fist section
		// addSection(FinanceApplication.constants()
		// .beginingBalance(), FinanceApplication.constants()
		// .endingBalance(), new int[] { 5 });
		// }
		else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
				endSection();
				// endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	public ClientFinanceDate getEndDate(TransactionHistory obj) {
		return obj.getEndDate();
	}

	public ClientFinanceDate getStartDate(TransactionHistory obj) {
		return obj.getStartDate();
	}

	int getType(TransactionHistory record) {
		if (record.getType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase("supplier prepayment")) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getType();
	}

	public int sort(TransactionHistory obj1, TransactionHistory obj2, int col) {
		int ret = obj1.getName().toLowerCase()
				.compareTo(obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getName().compareTo(obj2.getName());
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
			// case 3:
			// return obj1.getReference().toLowerCase().compareTo(
			// obj2.getReference().toLowerCase());
		case 4:
			return obj1.getAccount().toLowerCase()
					.compareTo(obj2.getAccount().toLowerCase());
		case 5:
			if (DecimalUtil.isEquals(obj1.getInvoicedAmount(), 0.0))
				return UIUtils.compareDouble(obj1.getPaidAmount(),
						obj2.getPaidAmount());
			else
				return UIUtils.compareDouble(obj1.getInvoicedAmount(),
						obj2.getInvoicedAmount());
		}
		return 0;
	}

	public void resetVariables() {
		sectionDepth = 0;
		sectionName = "";
		super.resetVariables();
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				getVendorString(getConstants().supplier(), getConstants()
						.vendor()), getConstants().date(),
				getConstants().type(), getConstants().no(),
				Global.get().account(), constants.amount() };
	}

}
