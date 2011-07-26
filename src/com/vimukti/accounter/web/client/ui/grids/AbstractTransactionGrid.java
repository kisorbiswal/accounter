package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public abstract class AbstractTransactionGrid<T> extends ListGrid<T> {

	protected AbstractTransactionBaseView<?> transactionView;

	private boolean isDeleteEnable;

	public static final int NON_TRANSACTIONAL = 0;
	public static final int CUSTOMER_TRANSACTION = 1;
	public static final int VENDOR_TRANSACTION = 2;
	public static final int BANKING_TRANSACTION = 3;

	public static final int TYPE_ITEM = 1;
	public static final int TYPE_COMMENT = 2;
	public static final int TYPE_SALESTAX = 3;
	public static final int TYPE_ACCOUNT = 4;
	public static final int TYPE_VATITEM = 5;
	public static final int TYPE_SERVICE = 6;
	public static final int TYPE_NONE = 0;

	private RecordClickHandler<T> recordClickHandler;
	private RecordDoubleClickHandler<T> doubleClickHandler;
	public boolean isItemRecieptView;

	protected CompanyMessages companyConstants = GWT
			.create(CompanyMessages.class);

	public AbstractTransactionGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	public AbstractTransactionGrid() {
		super(false);
	}

	public AbstractTransactionGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);

	}

	/**
	 * Override this method if you want to show a menu when add new is clicked,
	 * and then when user selects one menu item you want to do some thing.
	 * 
	 * @param selection
	 */
	protected void onAddNew(String selection) {

	}

	public String[] getColumnNamesForPrinting() {
		return null;
	}

	public String getColumnValueForPrinting(ClientTransactionItem item,
			int index) {
		return null;
	}

	public Map<Integer, Map<String, String>> getVATDetailsMapForPrinting() {
		return null;
	}

	public Double getVatTotal() {
		return 0.0;
	}

	public void setAllTransactions(List<ClientTransactionItem> transactionItems) {

	}

	public abstract List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException;

	@SuppressWarnings("unchecked")
	@Override
	protected void addOrEditSelectBox(T obj, Object value) {
		CustomCombo box = getCustomCombo(obj, currentCol);
		if (box != null) {
			Widget widget = box.getMainWidget();
			this.widgetsMap.put(currentCol, widget);
			((FocusWidget) widget).getElement().getStyle()
					.setWidth(100, Unit.PCT);
			this.setWidget(currentRow, currentCol, widget);
		} else
			super.addOrEditSelectBox(obj, value);
	}

	public void addRecordDoubleClickHandler(
			RecordDoubleClickHandler<T> doubleClickHandler) {
		this.doubleClickHandler = doubleClickHandler;
	}

	/**
	 * check the whether listgrid contains records or not
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (this.getRecords().size() > 1);
	}

	public void addRecordClickHandler(RecordClickHandler<T> recordClickHandler) {
		this.recordClickHandler = recordClickHandler;
	}

	/**
	 * set Enable or disable delete button on each record, by default
	 * isDeleteEnable is true
	 * 
	 * @param enable
	 */

	public void canDeleteRecord(boolean isDeleteEnable) {
		this.isDeleteEnable = isDeleteEnable;
	}

	public void setAddRequired(boolean isAddRequired) {
		this.isAddRequired = isAddRequired;
	}

	@Override
	protected String[] getSelectValues(T obj, int index) {
		return null;// this.getCurrentView().getGridSelectBoxValues(index);
	}

	@Override
	protected void onClick(T obj, int row, int index) {
		if (isDeleteEnable) {
			if (index == nofCols - 1) {
				deleteRecord(obj);
				return;
			}
		}
		if (recordClickHandler != null) {
			recordClickHandler.onRecordClick(obj, index);
		}
	}

	@Override
	protected void onValueChange(T obj, int index, Object value) {

	}

	@Override
	protected int sort(T obj1, T obj2, int index) {

		return 0;
	}

	public interface RecordDoubleClickHandler<T> {
		public void OnCellDoubleClick(T core, int column);
	}

	public interface RecordClickHandler<T> {
		public boolean onRecordClick(T core, int column);
	}

	public interface EditCompleteHandler<T> {
		public void OnEditComplete(T core, Object value, int col);
	}

	public void canDelete(boolean canDelete) {
		this.isDeleteEnable = canDelete;
	}

	@Override
	protected void onDoubleClick(T obj, int row, int col) {
		if (this.doubleClickHandler != null)
			this.doubleClickHandler.OnCellDoubleClick(obj, col);
		super.onDoubleClick(obj, row, col);
	}

	// @Override
	// public void addFooterValue(String value, int col) {
	// super.addFooterValue(value, col);
	// }
	//
	// @Override
	// public void addFooterValues(String... values) {
	// super.addFooterValues(values);
	// }

	@Override
	public void onDoubleClick(T obj) {

	}

	public void setCustomerTaxCode(ClientTransactionItem selectedObject) {

	}

	public void setVendorTaxCode(ClientTransactionItem selectedObject) {

	}

	public void setTransactionView(
			AbstractTransactionBaseView<?> abstractTransactionBaseView) {
		this.transactionView = abstractTransactionBaseView;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		return true;
	}

	public abstract <E> CustomCombo<E> getCustomCombo(T obj, int colIndex);

	public Double getTotal() {
		return 0.0;
	}

	public void refreshVatValue(ClientTransactionItem rec) {

	}

	public void refreshVatValue() {

	}

	public void updateTotals() {

	}

	public void resetGridEditEvent() {

	}

	public void refreshVatValue(IsSerializable record) {

	}

	public void updatePriceLevel() {

	}

	public void priceLevelSelected(ClientPriceLevel priceLevel) {

	}

	public Double getTaxableLineTotal() {

		return null;
	}

	public Double getGrandTotal() {

		return null;
	}

	public Double getTotalValue() {
		return null;
	}

	public abstract void setTaxCode(long taxCode);
}
