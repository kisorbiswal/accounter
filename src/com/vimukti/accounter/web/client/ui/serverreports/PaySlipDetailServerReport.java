package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.ui.reports.PaySlipDetailReport;
import com.vimukti.accounter.web.client.ui.reports.Section;

public class PaySlipDetailServerReport extends
		AbstractFinaneReport<PaySlipDetail> {

	private int type;

	public PaySlipDetailServerReport(PaySlipDetailReport paySlipDetailReport) {
		this.reportView = paySlipDetailReport;
	}

	public PaySlipDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
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
	public void initRecords(List<PaySlipDetail> records) {
		initGrid();
		removeAllRows();
		row = -1;
		this.records = records;

		for (PaySlipDetail record : records) {
			processRecord(record);
			Object[] values = new Object[this.getColunms().length];
			Object[] updatedValues = new Object[this.getColunms().length];
			for (int x = 0; x < this.getColunms().length; x++) {
				values[x] = getColumnData(record, x);
				updatedValues[x] = getColumnData(record, x);
				if (x == 1 || x == 2) {
					updatedValues[x] = record.getAmount();
				}
			}
			updateTotals(updatedValues);
			addRow(record, 2, values, false, false, false);
		}
		endAllSections();
		sections.clear();
		endStatus();
		showRecords();
	}

	@Override
	public void endSection() {
		try {
			this.sectionDepth--;
			if (sectionDepth >= 0 && !sections.isEmpty()) {
				Section<PaySlipDetail> s = sections.remove(sectionDepth);
				Object[] data = s.data;
				if (data[1] != null) {
					Double value = (Double) data[1];
					data[1] = getAmountAsString(null, value);
				}
				if (data[2] != null) {
					Double value = (Double) data[2];
					data[2] = getAmountAsString(null, value);
				}
				s.endSection();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
			return record.getType() == 2 ? getAmountAsString(record,
					record.getAmount()) : null;

		case 2:
			return record.getType() == 3 ? getAmountAsString(record,
					record.getAmount()) : null;

		case 3:
			return record.getType() == 1 ? getAmountAsString(record,
					record.getAmount()) : null;

		default:
			break;
		}
		return null;
	}

	protected String getAmountAsString(PaySlipDetail detail, double amount) {
		return String.valueOf(amount);
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
