package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;

public class EditTable<R> extends SimplePanel {

	private FlexTable table;
	private List<EditColumn<R>> columns = new ArrayList<EditColumn<R>>();
	private CellFormatter cellFormatter;
	private RowFormatter rowFormatter;
	private List<R> rows = new ArrayList<R>();
	private boolean isDesable;

	public EditTable() {
		this.addStyleName("editTable");
		table = new FlexTable();
		table.setWidth("100%");
		this.add(table);
		cellFormatter = table.getCellFormatter();
		rowFormatter = table.getRowFormatter();
		rowFormatter.addStyleName(0, "editheader");
	}

	public void addColumn(EditColumn<R> column) {
		columns.add(column);
		int index = columns.size() - 1;
		table.setWidget(0, index, column.getHeader());
		// Set width
		int width = column.getWidth();
		if (width != -1) {
			cellFormatter.setWidth(0, index, width + "px");
		}
		column.setTable(this);
	}

	public void setDisabled(boolean isDesable) {
		this.setDesable(isDesable);
		for (R r : rows) {
			update(r);
		}
	}

	/**
	 * Update a given row
	 * 
	 * @param row
	 */
	public void update(R row) {
		int index = rows.indexOf(row);
		index += 1;// for header
		RenderContext<R> context = new RenderContext<R>(this, row);
		context.setDesable(isDesable());
		context.setCellFormatter(cellFormatter);
		context.setRowFormatter(rowFormatter);
		for (int x = 0; x < columns.size(); x++) {
			EditColumn<R> column = columns.get(x);
			IsWidget widget = table.getWidget(index, x);
			column.render(widget, context);
		}
	}

	/**
	 * Add a new row
	 * 
	 * @param row
	 */
	public void add(R row) {
		rows.add(row);
		int index = rows.size() - 1;
		index += 1;// for header
		RenderContext<R> context = new RenderContext<R>(this, row);
		context.setCellFormatter(cellFormatter);
		context.setDesable(isDesable());
		context.setRowFormatter(rowFormatter);
		for (int x = 0; x < columns.size(); x++) {
			EditColumn<R> column = columns.get(x);
			IsWidget widget = column.getWidget(context);
			table.setWidget(index, x, widget);
			column.render(widget, context);
		}
	}

	/**
	 * Delete given row
	 * 
	 * @param row
	 */
	public void delete(R row) {
		int index = rows.indexOf(row);
		rows.remove(row);
		if (index != -1) {
			index += 1;// For header
			table.removeRow(index);
		}
	}

	/**
	 * Return copy list of all rows
	 * 
	 * @return
	 */
	public List<R> getAllRows() {
		return new ArrayList<R>(rows);
	}

	/**
	 * Remove all rows from table
	 */
	public void clear() {
		for (int x = 1; x <= rows.size(); x++) {
			table.removeRow(1);
		}
		rows.clear();
	}

	public void setAllRows(List<R> rows) {
		clear();
		for (R row : rows) {
			add(row);
		}

	}

	public void addRows(List<R> rows) {
		for (R row : rows) {
			add(row);
		}
	}

	public List<R> getSelectedRecords(int colInd) {
		List<R> selected = new ArrayList<R>();
		for (int x = 0; x < rows.size(); x++) {
			IsWidget widget = table.getWidget(x + 1, colInd);
			if (widget instanceof CheckBox) {
				CheckBox checkedWidget = (CheckBox) widget;
				if (checkedWidget.getValue()) {
					selected.add(rows.get(x));
				}
			}
		}
		return selected;
	}

	public void checkColumn(int row, int colInd, boolean isChecked) {
		IsWidget widget = table.getWidget(row + 1, colInd);
		if (widget instanceof CheckBox) {
			CheckBox checkedWidget = (CheckBox) widget;
			checkedWidget.setValue(isChecked);
			if (isChecked) {
				rowFormatter.removeStyleName(row + 1, "selected");
			} else {
				rowFormatter.addStyleName(row + 1, "selected");
			}
		}
	}

	public boolean isChecked(R row, int colInd) {
		int x = getAllRows().indexOf(row);
		IsWidget widget = table.getWidget(x + 1, colInd);
		if (widget instanceof CheckBox) {
			CheckBox checkedWidget = (CheckBox) widget;
			return checkedWidget.getValue();
		}
		return false;
	}

	public boolean isDesable() {
		return isDesable;
	}

	public void setDesable(boolean isDesable) {
		this.isDesable = isDesable;
	}

	protected void onDelete(R obj) {

	}
}
