package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * For History Management
 * 
 * @author Raj Vimal
 * 
 */
public class History {

	AbstractView<?> view;

	Action action;

	IAccounterCore data;

	boolean isDependent;

	public History() {

	}

	public History(AbstractView<?> view, IAccounterCore input, Action action,
			boolean dependent) {

		setView(view);
		setData(input);
		setAction(action);
		setDependent(dependent);
	}

	/**
	 * @return view
	 */

	public AbstractView getView() {
		return view;
	}

	/**
	 * ViewOrData to set
	 * 
	 * @param view
	 * 
	 */

	public void setView(AbstractView viewOrData) {
		this.view = viewOrData;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Setter for Action
	 * 
	 * @param action
	 * 
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Check for views Dependent property
	 * 
	 * @return isDependent
	 */
	public boolean isDependent() {
		return isDependent;
	}

	/**
	 * Setter for isDependent
	 * 
	 * @param isDependent
	 */
	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}

	/**
	 * Setter for the Data hold by View
	 * 
	 * @param data
	 */
	public void setData(IAccounterCore data) {
		this.data = data;
	}

	/**
	 * Getter for the Data hold by View
	 * 
	 * @return
	 */
	public IAccounterCore getData() {
		return data;
	}

	/**
	 * Update for History if Form data/ View data is not null for the Data hold
	 * by View
	 * 
	 * @param input
	 */
	public void updateHistory(Object input) {
		if (input instanceof IAccounterCore) {
			setData((IAccounterCore) input);
		}
	}
}
