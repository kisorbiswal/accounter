package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;

public class MISC1099TransactionDetailServerReport extends
		AbstractFinaneReport<MISC1099TransactionDetail> {

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getConstants().name(), getConstants().date(),
				getConstants().type(), getConstants().num(),
				getConstants().memo(), getConstants().box1099(),
				getConstants().Account(), getConstants().amount() };
	}

	@Override
	public String getTitle() {
		return Accounter.messages().MISC1099TransactionDetailByVendor(
				Global.get().Vendor());
	}

	@Override
	public String[] getColunms() {
		return new String[] { getConstants().name(), getConstants().date(),
				getConstants().type(), getConstants().num(),
				getConstants().memo(), getConstants().box1099(),
				getConstants().Account(), getConstants().amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(MISC1099TransactionDetail record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(MISC1099TransactionDetail record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();

		case 1:
			return record.getDate();

		case 2:
			return ReportUtility.getTransactionName(record.getType());

		case 3:
			return record.getNumber();

		case 4:
			return record.getMemo();

		case 5:
			return record.getBox1099();

		case 6:
			return record.getAccount();

		case 7:
			return record.getAmount();

		default:
			break;
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(MISC1099TransactionDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(MISC1099TransactionDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
