//package com.vimukti.accounter.web.client.ui.grids;
//
//import com.google.gwt.dom.client.Node;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.user.client.Event;
//import com.google.gwt.user.client.ui.FlexTable;
//import com.google.gwt.user.client.ui.HTMLTable.Cell;
//import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
//import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
//import com.google.gwt.user.client.ui.Widget;
//import com.vimukti.accounter.web.client.ui.StyledPanel;
//
//public abstract class StyledFlexTable extends GwtFlexTable {
//
//
//
//	private StyledPanel flexTable;
//
//	public StyledFlexTable() {
//		flexTable = new StyledPanel("flexTable");
//	}
//	
//	public void onClick(Event event){
//		
//	}
//
//	public RowFormatter getRowFormatter() {
//		 RowFormatter rowFormatter = new RowFormatter();
//		return rowFormatter;
//	}
//
//	public void setStyleName(String string) {
//		flexTable.setStyleName(string);
//	}
//
//	public CellFormatter getCellFormatter() {
//		CellFormatter cellFormatter = new CellFormatter();
//		return cellFormatter;
//	}
//
//	// public void addClickHandler(ClickHandler clickHandler) {
//	//
//	// }
//
//	public Cell getCellForEvent(ClickEvent event) {
//		return flexTable.getCellForEvent(event);
//	}
//
//	public Node getElement() {
//		return flexTable.getElement();
//	}
//
//	public void setText(int row, int column, String text) {
//		flexTable.setText(row, column, text);
//	}
//
//	public int getRowCount() {
//		return flexTable.getRowCount();
//	}
//
//	public void removeRow(int row) {
//		flexTable.removeRow(row);
//	}
//
//	public void removeCell(int row, int col) {
//		flexTable.removeCell(row, col);
//	}
//
//	public void removeAllRows() {
//		flexTable.removeAllRows();
//	}
//
//	public void setWidget(int row, int column, Widget widget) {
//		flexTable.setWidget(row, column, widget);
//	}
//
//	public Widget getWidget(int row, int col) {
//		return flexTable.getWidget(row, col);
//	}
//
//	public void setHTML(int row, int column, String html) {
//		flexTable.setHTML(row, column, html);
//	}
//
//	public int getCellCount(int i) {
//		return flexTable.getCellCount(i);
//	}
//
//	public int getOffsetWidth() {
//		return flexTable.getOffsetWidth();
//	}
//
//	public void setWidth(String string) {
//		flexTable.setWidth(string);
//	}
//
//	public Widget getParent() {
//		return flexTable.getParent();
//	}
//
//	public boolean remove(Widget widget) {
//		return flexTable.remove(widget);
//	}
//
//	public Widget getPanel() {
//		return flexTable;
//
//	}
//
//	public void removeStyleName(String string) {
//		flexTable.removeStyleName(string);
//	}
//	
//	
//	public FlexTable getFlexTable() {
//		return null;
//	}
//	
//
//
//	
//}
