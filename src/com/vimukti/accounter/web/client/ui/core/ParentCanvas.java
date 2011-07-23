package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.WidgetCollection;

public abstract class ParentCanvas<T> extends VerticalPanel implements
		IAccounterWidget {

	abstract public void init();

	abstract public void initData();

	public Object data;
	// protected boolean isHide;

	private Action action;
	public boolean isSave = false;
	public boolean isContiueExecution = true;
	protected long id;
	// public boolean isContinueValidattion = true;

	/**
	 * Flag, to Determine, whether in Edit Mode or Create mode.
	 */
	protected boolean isEdit;

	protected boolean isInitialized;

	public boolean showSaveAndNewButton;

	public boolean isSaveAndNew;

	/**
	 * @param isInitialized
	 *            the isInitialized to set
	 */
	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setPrevoiusOutput(Object preObjectm) {

	}

	public void close() {
		// ViewManager.close(this, data);
	}

	public boolean isHidden() {
		return false;
	}

	// public void setHasHistory(boolean hasHistory) {
	// this.hasHistory = hasHistory;
	// }

	public void setAction(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	@Override
	public String toString() {
		if (action == null)
			return super.toString();
		else
			return action.getText();
	}

	private boolean isNotNull() {

		return this != null;

	}

	public boolean isTransactionView() {

		return false;

	}

	public boolean isReportView() {
		return isNotNull();
	}

	public boolean shouldSaveInHistory() {
		return false;

	}

	public boolean isAListView() {
		return false;
	}

	public boolean isEditMode() {

		return isEdit;
	}

	public void disableUserEntry() {

	}

	/**
	 * return String values for Select box in Grid,
	 * 
	 * @param colIndex
	 *            is selectBox index in row
	 */
	public String[] getGridSelectBoxValues(int colIndex) {
		return null;
	}

	/**
	 * called when values has been changed of Selectbox in grid
	 * 
	 * @param colIndex
	 *            is selectBox index in row
	 */
	public void onSelectBoxValueChange(int colIndex, Object value) {
	}

	@Override
	public WidgetCollection getChildren() {
		// TODO Auto-generated method stub
		return super.getChildren();
	}

	/**
	 * call this method to set focus in View
	 */
	public abstract void setFocus();

	public abstract void fitToSize(int height, int width);

	public void createID() {

	}

	@Override
	public long getID(){
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;
	}

	public abstract void onEdit();

	public abstract void printPreview();

	public abstract void print();

	public void exportToCsv() {
	}

}
