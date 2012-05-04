package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.FlowPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.CountryPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractView<T> extends FlowPanel {

	abstract public void init();

	private Action<?> action;

	/**
	 * Flag, to Determine, whether in Edit Mode or Create mode.
	 */

	protected T data;

	protected static final AccounterMessages messages = Global.get().messages();
	protected static final AccounterMessages2 messages2 = Global.get()
			.messages2();

	private ViewManager manager;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void cancel() {
		setData(null);
		getManager().closeCurrentView();
	}

	public void setAction(Action<?> action) {
		this.action = action;
	}

	public Action<?> getAction() {
		if (action.getText() != null)
			action.setViewName(action.getText());
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

	/**
	 * call this method to set focus in View
	 */
	public abstract void setFocus();

	public abstract void fitToSize(int height, int width);

	public abstract void onEdit();

	public abstract void printPreview();

	public abstract void print();

	public void exportToCsv() {
	}

	public void initData() {
		// TODO Auto-generated method stub

	}

	public ViewManager getManager() {
		return manager;
	}

	public void setManager(ViewManager manager) {
		this.manager = manager;
	}

	public ClientCompanyPreferences getPreferences() {
		return Accounter.getCompany().getPreferences();
	}

	public CountryPreferences getCountryPreferences() {
		return Accounter.getCompany().getCountryPreferences();
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		setFocus();
	}
}
