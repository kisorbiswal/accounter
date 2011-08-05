package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;

public abstract class ParentCanvas<T> extends VerticalPanel {

	abstract public void init(ViewManager viewManager);

	private Action action;

	/**
	 * Flag, to Determine, whether in Edit Mode or Create mode.
	 */

	protected T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void close() {
		MainFinanceWindow.getViewManager().closeCurrentView();
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

	public boolean shouldSaveInHistory() {
		return false;
	}

	public boolean isAListView() {
		return false;
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
		return super.getChildren();
	}

	/**
	 * call this method to set focus in View
	 */
	public abstract void setFocus();

	public abstract void fitToSize(int height, int width);

	public void createID() {

	}

	// public long getID() {
	// return this.id;
	// }
	//
	// public void setID(long id) {
	// this.id = id;
	// }

	public abstract void onEdit();

	public abstract void printPreview();

	public abstract void print();

	public void exportToCsv() {
	}

	public void initData() {
		// TODO Auto-generated method stub
		
	}

}
