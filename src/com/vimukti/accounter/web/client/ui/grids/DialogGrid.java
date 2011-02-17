package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public class DialogGrid extends ListGrid<IsSerializable> {

	@SuppressWarnings("unchecked")
	protected AbstractBaseDialog view;
	private String name;

	private List<String> columns = new ArrayList<String>();
	private List<Integer> colTypes = new ArrayList<Integer>();
	private List<Integer> disableFields = new ArrayList<Integer>();
	private List<Integer> cellsWidth = new ArrayList<Integer>();

	private GridRecordClickHandler recordClickHandler;
	@SuppressWarnings("unused")
	private RecordDeleteHandler recordDeleteHandler;
	@SuppressWarnings("unchecked")
	private RecordDoubleClickHandler doubleClickHandler;

	/* This var. tells the core type currently the grid holds */
	private AccounterCoreType type;

	private Map<Integer, FormItem> itemsMap = new HashMap<Integer, FormItem>();
	private SelectionChangedHandler selectionChangedHandler;

	public DialogGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);

	}

	/**
	 * called for initialise object
	 */
	@Override
	public void init() {
		super.init();
		super.setSize("100%", "170px");
	}

	protected void menuItemClicked(String item) {
		// implement in sub classes
	}

	/**
	 * add listener on record delete,implement listener if you want to do on
	 * record delete
	 * 
	 * @param deleteHandler
	 *            ;
	 */
	public void addRecordDeleteHandler(RecordDeleteHandler deleteHandler) {
		this.recordDeleteHandler = deleteHandler;
	}

	@Override
	public void setRecords(List<IsSerializable> list) {
		super.setRecords(list);
	}

	public void selectRecord(int index) {
		// this.listgrid.selectRecord(index);
	}

	/**
	 * check the whether listgrid contains records or not
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (this.getRecords().size() <= 0);
	}

	public void addRecordClickHandler(GridRecordClickHandler recordClickHandler2) {
		this.recordClickHandler = recordClickHandler2;
	}

	public void setDiableFields(Integer... fields) {
		this.disableFields.addAll(Arrays.asList(fields));
	}

	public void setCellsWidth(Integer... cellsWidth) {
		for (int wth : cellsWidth) {
			this.cellsWidth.add(wth);
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (cellsWidth.size() > index)
			return cellsWidth.get(index);
		return -1;

	}

	@Override
	protected int getColumnType(int index) {
		return colTypes.get(index > 0 ? index : index);
	}

	@Override
	protected Object getColumnValue(IsSerializable obj, int index) {
		return this.view.getGridColumnValue(obj, index);
	}

	@Override
	protected String[] getColumns() {
		return this.columns.toArray(new String[] {});
	}

	@Override
	protected String[] getSelectValues(IsSerializable obj, int index) {
		return null;
	}

	@Override
	protected boolean isEditable(IsSerializable obj, int row, int index) {
		if (this.disableFields.contains(index))
			return false;
		return true;
	}

	@Override
	protected void onClick(IsSerializable obj, int row, int index) {
		if (this.recordClickHandler != null) {
			this.recordClickHandler.onRecordClick(obj, index);
		}
		// if (isDeleteEnable)
		// if (index == nofCols - 1) {
		// if (isDeleteEnable
		// && recordDeleteHandler.onRecordDelete(obj, row)) {
		// deleteRecord(obj);
		// return;
		// }
		// }
		//
		// switch (index) {
		// case 0:
		// if (isShowMenu)
		// showMenu(event);
		// else
		// addRecord(0);
		// break;
		// default:
		// break;
		// }
		// if (index > 0) {
		// this.recordClickHandler.onRecordClick(obj, index);
		// }
	}

	@Override
	protected void onValueChange(IsSerializable obj, int index, Object value) {
		// this.getCurrentView().onSelectBoxValueChange(index, value);
	}

	@Override
	protected int sort(IsSerializable obj1, IsSerializable obj2, int index) {

		return 0;
	}

	public void addColumns(String... cols) {
		this.columns.addAll(Arrays.asList(cols));
		for (int i = 0; i < cols.length; i++)
			colTypes.add(2);
	}

	public void addColumnType(Integer... types) {
		this.colTypes.addAll(Arrays.asList(types));
	}

	public void setColumnTypes(Integer... types) {
		if (types == null)
			return;
		this.colTypes.clear();
		this.colTypes.addAll(Arrays.asList(types));
	}

	public void addColumn(int fieldType, String col) {
		colTypes.add(fieldType);
		columns.add(col);
	}

	public interface RecordDoubleClickHandler<T> {
		public void OnCellDoubleClick(T core, int column);
	}

	public interface RecordDeleteHandler {
		public boolean onRecordDelete(IsSerializable core, int row);
	}

	public interface GridRecordClickHandler {
		public boolean onRecordClick(IsSerializable core, int column);
	}

	public interface EditCompleteHandler {
		public void OnEditComplete(IsSerializable core, Object value, int col);
	}

	public interface SelectionChangedHandler {
		public void OnSelectionChanged(IsSerializable core, int row,
				boolean isChecked);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addOrEditSelectBox(IsSerializable obj, Object value) {
		CustomCombo box = getCustomCombo(obj, currentCol);
		if (box != null) {
			FocusWidget widget = (FocusWidget) box.getMainWidget();
			this.setWidget(currentRow, currentCol, widget);
		} else
			super.addOrEditSelectBox(obj, value);
	}

	public <E> CustomCombo<E> getCustomCombo(IsSerializable obj, int colIndex) {
		return null;
	}

	@Override
	protected void addColumnData(IsSerializable obj, int row, int col) {
		super.addColumnData(obj, row, col);
	}

	/**
	 * This method is to add FormItems in grid. if it is custom FormItem which
	 * has extract functionality than existing FormItems, the addFormItem
	 * 
	 * @param combo
	 * @param colIndex
	 */
	public void addFormItem(FormItem combo, int colIndex) {
		this.itemsMap.put(colIndex, combo);
	}

	/**
	 * Delete FormItem from list of items that grid had
	 * 
	 * @param colIndex
	 */
	public void deleteFormItem(int colIndex) {
		this.itemsMap.remove(colIndex);
	}

	@Override
	public void addFooterValue(String value, int col) {
		// TODO Auto-generated method stub
		super.addFooterValue(value, col);
	}

	@Override
	public void addFooterValues(String... values) {
		// TODO Auto-generated method stub
		super.addFooterValues(values);
	}

	@SuppressWarnings("unchecked")
	public void setView(AbstractBaseDialog view) {
		this.view = view;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public void addRecordDoubleClickHandler(
			RecordDoubleClickHandler doubleClickHandler) {
		this.doubleClickHandler = doubleClickHandler;
	}

	@Override
	public boolean validateGrid() {
		return false;
	}

	@Override
	public void onDoubleClick(IsSerializable obj) {

	}

	@Override
	protected void onSelectionChanged(IsSerializable obj, int row,
			boolean isChecked) {
		if (this.selectionChangedHandler != null)
			this.selectionChangedHandler
					.OnSelectionChanged(obj, row, isChecked);
	}

	public void addSelectionChangedHandler(SelectionChangedHandler handler) {
		this.selectionChangedHandler = handler;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDoubleClick(IsSerializable obj, int row, int index) {
		if (this.doubleClickHandler != null)
			this.doubleClickHandler.OnCellDoubleClick(obj, index);
		super.onDoubleClick(obj, row, index);
	}

	public AccounterCoreType getType() {
		return type;
	}

	public void setType(AccounterCoreType type) {
		this.type = type;
	}
	
	

}
