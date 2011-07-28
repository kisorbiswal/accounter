package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.ui.DataUtils;


public class CSVReportGridTemplate<R> extends ReportGridTemplate {

	public CSVReportGridTemplate(String[] columns, boolean ishowGridFooter) {
		super(columns, ishowGridFooter);
	}

	@Override
	public void addRow(Object record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {
		if (list.size() == 0) {
			this.body = getBodyHead();
		}
		list.add(record);
		for (int i = 0; i < columns.length; i++) {
			if (maxDepth < depth)
				this.maxDepth = depth;
			if (values.length > i && values[i] != null) {
				if (reportView.getColumnstoHide().contains(i) && record != null) {
					this.body = this.body + "";
				} else if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					this.body = this.body
							+ getValue(values[i]).replace(",", "");
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					if (getValue(values[i]) == "") {
						this.body = this.body + "";
					} else {
						this.body = this.body
								+ getValue(values[i]).replace(",", "") + " %";
					}
				} else {
					this.body = this.body
							+ String.valueOf(values[i]).replace(",", "");
				}
			} else {
				this.body = this.body + "";
			}
			this.body = this.body + ",";
		}
		this.body = this.body + "\n";
	}

	@Override
	public String getBody() {
		System.out.println("It is the body of CSV Report " + this.body);
		System.err.println(this.body);
		return this.body;
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

	@Override
	public String getBodyHead() {
		String[] headerTitles = reportView.getColunms();
		String columnHeader = null;
		for (int i = 0; i < headerTitles.length; i++) {
			if (columnHeader == null) {
				columnHeader = headerTitles[i].replace(",", "") + ",";
			} else {
				columnHeader = columnHeader + headerTitles[i].replace(",", "")
						+ ",";
			}
		}
		columnHeader = columnHeader + "\n";
		return columnHeader;
	}

}
