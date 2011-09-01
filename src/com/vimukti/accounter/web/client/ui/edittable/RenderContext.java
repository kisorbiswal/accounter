package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;

public class RenderContext<R> {

	private CellFormatter cellFormatter;
	private RowFormatter rowFormatter;
	private EditTable<R> table;
	private R row;

	public RenderContext(EditTable<R> table,R row) {
		this.table=table;
		this.row=row;
	}

	public RowFormatter getRowFormatter() {
		return rowFormatter;
	}

	public CellFormatter getCellFormatter() {
		return cellFormatter;
	}

	public void setCellFormatter(CellFormatter cellFormatter) {
		this.cellFormatter = cellFormatter;
	}

	public void setRowFormatter(RowFormatter rowFormatter) {
		this.rowFormatter = rowFormatter;
	}

	public EditTable<R> getTable() {
		return table;
	}

	public R getRow() {
		return row;
	}


	
}
