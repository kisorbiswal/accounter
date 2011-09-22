package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

public class DropDownTable<T> extends CellTable<T> {

	public FlexTable body;
	private int nofCols;
	private RowFormatter rowFormatter;
	private CellFormatter cellFormatter;
	private int cols = 1;
	private String addNewText;
	private DropDownCombo<T> combo;
	protected SingleSelectionModel<T> selectionModel;
	boolean isAddNewRequire;
	private Element bodyrowElem;
	protected ClickableTextCell clickableTextCell;
	private EscapEventHandler escapEventHandler;

	public DropDownTable(DropDownCombo<T> combo) {
		this.combo = combo;
		sinkEvents(Event.ONMOUSEOVER);
		initGrid();
	}

	/**
	 * Initialise the table
	 */
	private void initGrid() {
		addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE
						|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
					if (escapEventHandler != null) {
						escapEventHandler.onEscap(event);
					}
				}
			}
		}, KeyDownEvent.getType());
		this.body = new FlexTable();
		this.setStyleName("dropdownFlex");

		this.setWidth("100%");
		this.nofCols = getNoColumns();

		clickableTextCell = new ClickableTextCell();
		Column[] columns = getColumns();
		for (int i = 0; i < columns.length; i++)
			this.addColumn(columns[i]);
		selectionModel = new SingleSelectionModel<T>();
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		this.setSize("100%", "");

	}

	public void addEscapEventHandler(EscapEventHandler handler) {
		escapEventHandler = handler;
	}

	@Override
	public void setVisibleRange(Range range) {
		super.setVisibleRange(range);
	}

	@Override
	protected void setKeyboardSelected(int index, boolean selected,
			boolean stealFocus) {
		super.setKeyboardSelected(index, selected, stealFocus);
	}

	@Override
	public TableRowElement getRowElement(int row) {
		// TODO Auto-generated method stub
		return super.getRowElement(row);
	}

	@Override
	protected void setSelected(com.google.gwt.dom.client.Element elem,
			boolean selected) {
		super.setSelected(elem, selected);
	}

	protected Column<T, String>[] getColumns() {
		return null;
	}

	protected void eventFired(int rowIndex) {
		combo.changeValue(rowIndex);

	}

	private int getNoColumns() {
		return cols;
	}

	public void setNoColumns(int cols) {
		this.cols = cols;

	}

	public void setText(int row, int column, String text) {
		this.body.setText(row, column, text);
		this.cellFormatter.getElement(row, column).setTitle(text);
	}

	public int insertRow(int beforeRow) {
		this.body.insertRow(beforeRow);
		return beforeRow;
	}

	public void addLabel(int row, int column, String text) {
		final Label label = new Label();
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				widgetClicked((Label) event.getSource());
			}
		});

		if (text != null)
			label.setText(text.toString());
		else
			label.setText(" ");

		setWidget(row, column, label);
	}

	public void setWidget(int row, int column, final Widget widget) {
		this.body.setWidget(row, column, widget);
	}

	private void widgetClicked(Widget widget) {

		RowCell cell = getCellByWidget(widget);
		cellClicked(cell.getRowIndex(), cell.getCellIndex());

		// /**
		// * preventing Cell click event when widget clicked in Cell
		// */
		// Timer timer = new Timer() {
		//
		// @Override
		// public void run() {
		// disable = false;
		// }
		// };
		// timer.schedule(100);
	}

	/**
	 * Called this method when cell clicked in Row on This Grid
	 * 
	 * @param cell
	 */
	public void cellClicked(int row, int col) {

	}

	protected RowCell getCellByWidget(Widget widget) {
		Element td = DOM.getParent(widget.getElement());
		Element tr = DOM.getParent(td);
		Element parent = DOM.getParent(tr);
		int row = DOM.getChildIndex(parent, tr);
		int col = DOM.getChildIndex(tr, td);
		return new RowCell(row, col);
	}

	@Override
	protected int getKeyboardSelectedRow() {
		return super.getKeyboardSelectedRow();
	}

	@Override
	protected com.google.gwt.dom.client.Element getKeyboardSelectedElement() {
		return super.getKeyboardSelectedElement();
	}

	public void init(boolean isAddNewRequire) {

		this.isAddNewRequire = isAddNewRequire;
		List<T> Dummy = getDummyRecords();
		this.setRowData(0, Dummy);
		// // this.setText(0, 0, " ");
		// // this.cellFormatter.setHeight(0, 0, "20px");
		// // this.cellFormatter.setHorizontalAlignment(0, 0,
		// // HasHorizontalAlignment.ALIGN_CENTER);
		// if (isAddNewRequire) {
		// if (cols > 1) {
		// this.setText(1, 1, combo.getDefaultAddNewCaption());
		// this.setText(0, 1, "  ");
		// for (int i = 0; i < cols; i++) {
		// if (i == 1)
		// continue;
		// this.setText(1, i, "  ");
		// this.setText(0, i, "  ");
		// this.cellFormatter.setHeight(0, i, "20px");
		// }
		// this.cellFormatter.setHorizontalAlignment(1, 1,
		// HasHorizontalAlignment.ALIGN_CENTER);
		// } else {
		// this.setText(1, 0, combo.getDefaultAddNewCaption());
		// }
		//
		// this.cellFormatter.setHorizontalAlignment(1, 0,
		// HasHorizontalAlignment.ALIGN_CENTER);
		// }

	}

	public List<T> getDummyRecords() {
		List<T> Dummy = new ArrayList<T>();
		// Dummy.add((T) "emptyRow");
		if (isAddNewRequire)
			Dummy.add((T) "addNewCaption");
		return Dummy;
	}

	protected void setAddNewText(String text) {
		this.addNewText = text;
	}

	/**
	 * class is used to Hold Row&ColumnIndex
	 */
	class RowCell {

		private final int rowIndex;
		private final int cellIndex;

		/**
		 * Creates a cell.
		 * 
		 * @param rowIndex
		 *            the cell's row
		 * @param cellIndex
		 *            the cell's index
		 */
		public RowCell(int rowIndex, int cellIndex) {
			this.cellIndex = cellIndex;
			this.rowIndex = rowIndex;
		}

		/**
		 * Gets the cell index.
		 * 
		 * @return the cell index
		 */
		public int getCellIndex() {
			return cellIndex;
		}

		/**
		 * Gets the cell's element.
		 * 
		 * @return the cell's element.
		 */
		public Element getElement() {
			return cellFormatter.getElement(cellIndex, rowIndex);
		}

		/**
		 * Get row index.
		 * 
		 * @return the row index
		 */
		public int getRowIndex() {
			return rowIndex;
		}

	}

	public void removeAllrows() {
		this.body.removeAllRows();
		removeColumn(getColumns()[0]);
		init(isAddNewRequire);
	}

	public void clear() {
		removeColumn(getColumns()[0]);
		init(isAddNewRequire);
	}

	@Override
	public void onBrowserEvent2(Event event) {

		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:
			if (bodyrowElem != null)
				bodyrowElem.getStyle().clearBackgroundColor();
			bodyrowElem = (Element) DOM.eventGetTarget(event)
					.getParentElement();

			String tagName = bodyrowElem.getTagName();
			if ("td".equalsIgnoreCase(tagName)
					|| "th".equalsIgnoreCase(tagName)) {
				bodyrowElem = (Element) bodyrowElem.getParentElement();
			}
			if (DOM.isOrHasChild(this.getElement(), bodyrowElem)) {
				bodyrowElem.getStyle().setBackgroundColor("#D3E8A3");

			}
			eatEvent(event);
			break;
		case Event.ONCLICK:
		case Event.ONMOUSEOUT:
			if (bodyrowElem != null)
				bodyrowElem.getStyle().clearBackgroundColor();

			eatEvent(event);
			break;
		}
		super.onBrowserEvent2(event);
	}

	public void removeRow(int rowindex) {
		((TableSectionElement) this.getChildContainer()).deleteRow(rowindex);
	}

	private void eatEvent(Event event) {
		DOM.eventCancelBubble(event, true);
		DOM.eventPreventDefault(event);
	}
}

interface EscapEventHandler {
	public void onEscap(KeyDownEvent event);
}