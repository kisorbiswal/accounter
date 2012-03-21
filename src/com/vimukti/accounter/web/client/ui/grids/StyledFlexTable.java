package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.StyledPanel;

//public class StyledFlexTable extends GwtFlexTable {
public class StyledFlexTable {
	private StyledPanel flexTable;

	public StyledFlexTable() {
		flexTable = new StyledPanel("flexTable");
	}

	public void onClick(Event event) {

	}

	public RowFormatter getRowFormatter() {
		return null;
	}

	public void setStyleName(String string) {
		flexTable.setStyleName(string);
	}

	public CellFormatter getCellFormatter() {
		return null;
	}

	// public void addClickHandler(ClickHandler clickHandler) {
	//
	// }

	public Cell getCellForEvent(ClickEvent event) {
		return null;
	}

	public Node getElement() {
		return null;
	}

	public void setText(int row, int column, String text) {
		Label label = new Label(text);
		label.getElement().setClassName("gridElementLabel");
		flexTable.add(label);
	}

	public int getRowCount() {
		return flexTable.getWidgetCount();
	}

	public void removeRow(int row) {
		flexTable.remove(row);
	}

	public void removeCell(int row, int col) {
		flexTable.remove(col);
	}

	public void removeAllRows() {
		flexTable.clear();
	}

	public void setWidget(int row, int column, Widget widget) {
		flexTable.add(widget);
	}

	public Widget getWidget(int row, int col) {
		return flexTable.getWidget(col);
	}

	public void setHTML(int row, int column, String html) {
		Label label = new Label(html);
		label.getElement().setClassName("gridElementLabel");
		flexTable.add(label);
	}

	public int getCellCount(int i) {
		return flexTable.getWidgetCount();
	}

	public int getOffsetWidth() {
		return flexTable.getOffsetWidth();
	}

	public void setWidth(String string) {
		flexTable.setWidth(string);
	}

	public Widget getParent() {
		return flexTable.getParent();
	}

	public boolean remove(Widget widget) {
		return flexTable.remove(widget);
	}

	public Widget getPanel() {
		return flexTable;

	}

	public void removeStyleName(String string) {
		flexTable.removeStyleName(string);
	}

	public FlexTable getFlexTable() {
		return null;
	}

	public void addStyleName(String string) {
		flexTable.addStyleName(string);
	}

}
