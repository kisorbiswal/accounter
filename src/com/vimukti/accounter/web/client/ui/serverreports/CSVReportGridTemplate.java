package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.DataUtils;

public class CSVReportGridTemplate<R> extends ReportGridTemplate {

	public CSVReportGridTemplate(String[] columns, boolean ishowGridFooter) {
		super(columns, ishowGridFooter);
	}

	@Override
	public void addRow(Object record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {
		if (list.size() == 0) {
			prepareBodyHead();
		}
		list.add(record);
		for (int i = 0; i < columns.length; i++) {
			if (maxDepth < depth)
				this.maxDepth = depth;
			if (values.length > i && values[i] != null) {
				if (reportView.getColumnstoHide().contains(i) && record != null) {
					this.body.append("");
				} else if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					this.body.append(getValue(values[i]).replace(",", ""));
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					if (getValue(values[i]).isEmpty()) {
						this.body.append("");
					} else {
						this.body.append(getValue(values[i]).replace(",", "")
								+ " %");
					}
				} else {
					this.body
							.append(String.valueOf(values[i]).replace(",", ""));
				}
			} else {
				this.body.append("");
			}
			this.body.append(',');
		}
		this.body.append('\n');
	}

	@Override
	public String getBody() {
		System.out.println("It is the body of CSV Report " + this.body);
		System.err.println(this.body);
		return this.body.toString();
	}

	@Override
	public String getValue(Object object) {
		return (object instanceof Double ? DataUtils
				.getAmountAsString((Double) object) : object.toString());
	}

	@Override
	public void initBody() {

	}

	@Override
	public void addDepthStyle(int row, int column, int depth) {

	}

	@Override
	protected int getCellWidth(int index) {
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return null;
	}

	@Override
	public String getFooter() {
		return null;
	}

	@Override
	public String getHeader() {
		return null;
	}

	@Override
	public boolean isReportViewHasToCols() {
		return false;
	}

	@Override
	public void removeAllRows() {

	}

	@Override
	public void setColumnTypes(int[] columnTypes) {
		this.columnTypes = columnTypes;
	}

	@Override
	public void addCell(boolean bold, String cellValue, int depth,
			boolean underline, int cellWidth, int columnType) {

	}

	public void prepareBodyHead() {
		String[] headerTitles = reportView.getColunms();
		if (body == null) {
			body = new StringBuffer();
		}
		for (int i = 0; i < headerTitles.length; i++) {
			body.append(headerTitles[i].replace(",", "") + ",");
		}
		body.append('\n');
	}

	@Override
	public String getBody(AccounterConstants accounterConstants) {
		if (body == null || body.toString().isEmpty()) {
			this.body.append(accounterConstants.noRecordsToShow());
		}
		return this.body.toString();
	}

}
