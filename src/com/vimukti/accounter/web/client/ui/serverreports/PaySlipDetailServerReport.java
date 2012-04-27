package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.ui.reports.PaySlipDetailReport;

public class PaySlipDetailServerReport extends
		AbstractFinaneReport<PaySlipDetail> {

	private int type;

	public PaySlipDetailServerReport(PaySlipDetailReport paySlipDetailReport) {
		this.reportView = paySlipDetailReport;
	}

	@Override
	public String[] getDynamicHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		return messages.payslipDetail();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.name(), messages.earnings(),
				messages.deductions(), messages.value() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT };
	}

	@Override
	public void processRecord(PaySlipDetail record) {
		if (sectionDepth == 0) {
			this.type = record.getType();
			String typeName = getItemTypeName(type);
			addSection(new String[] { typeName },
					type != 1 ? new String[] { getMessages().total() }
							: new String[] {},
					type != 1 ? type == 2 ? new int[] { 1 } : new int[] { 2 }
							: new int[] {});
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (this.type != record.getType()) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	private String getItemTypeName(int type) {
		if (type == 1) {
			return messages.attendance();
		} else if (type == 2) {
			return messages.earnings();
		} else {
			return messages.deductions();
		}
	}

	@Override
	public Object getColumnData(PaySlipDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getType() == 2 ? getAmountAsString(record) : null;

		case 2:
			return record.getType() == 3 ? getAmountAsString(record) : null;

		case 3:
			return record.getType() == 1 ? getAmountAsString(record) : null;

		default:
			break;
		}
		return null;
	}

	protected String getAmountAsString(PaySlipDetail detail) {
		return String.valueOf(detail.getAmount());
	}

	@Override
	public ClientFinanceDate getStartDate(PaySlipDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(PaySlipDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
