package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;

/**
 */
public abstract class Action<T> implements Command {

	// @Override
	// public String mayStop() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public void onCancel() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onStop() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void start(AcceptsOneWidget panel, EventBus eventBus) {
	// // TODO Auto-generated method stub
	//
	// }

	/**
	 * This action's text, or <code>null</code> if none.
	 */
	private String text;
	private String viewName;
	/**
	 * This action's tool tip text, or <code>null</code> if none.
	 */
	private String toolTipText;

	// private String iconString;

	public String catagory = "";

	private boolean allowMultiple;

	protected T data;
	public List<T> listData;

	protected boolean isDependent;

	private ActionCallback<T> callback;

	/**
	 * setting Text for Action
	 */
	public Action(String text) {
		setText(text);
		setViewName(null);
	}

	/**
	 * Returns the text for this action.
	 * 
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns the Tool Tip Text for this action
	 * 
	 * @return String
	 */
	public String getToolTip() {
		return toolTipText;
	}

	/**
	 * Runs this action. Each action implementation must define the steps needed
	 * to carry out this action. The default implementation of this method in
	 * <code>Action</code> does nothing.
	 * 
	 * @return
	 * 
	 * @throws Throwable
	 */
	public abstract void run();

	public void execute() {
		run();
	}

	public void setInput(T data) {
		this.data = data;
	}

	public Object getInput() {
		return data;
	}

	/**
	 * Setter for the Text for the action
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Setter for Tool tip
	 * 
	 * @param toolTip
	 */
	public void setToolTip(String toolTip) {
		this.toolTipText = toolTip;
	}

	/**
	 * Setter for Icon
	 * 
	 * @param iconString
	 */
	// public void setIcon(String iconString) {
	// this.iconString = iconString;
	// }

	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public boolean allowMultiple() {

		return this.allowMultiple;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Action<?>)) {
			return false;
		}

		boolean isSameClass = object.getClass().getName().equals(
				this.getClass().getName());

		return isSameClass;

	}

	public abstract ImageResource getBigImage();

	public abstract ImageResource getSmallImage();

	public abstract String getHistoryToken();

	public abstract String getHelpToken();

	public boolean isDependent() {
		return isDependent;
	}

	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}

	public void run(T object, boolean isDependent) {

		setInput(object);

		setDependent(isDependent);
		run();
	}

	public void run(List<T> object) {

		setListData(object);

		run();
	}

	public ActionCallback<T> getCallback() {
		return callback;
	}

	public void setCallback(ActionCallback<T> callback) {
		this.callback = callback;
	}

	public List<T> getListData() {
		return listData;
	}

	public void setListData(List<T> listData) {
		this.listData = listData;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
		if (viewName == null && this.getText() != null
				&& getText().contains("New "))
			this.viewName = this.getText().replace("New ", "").toLowerCase()
					.trim();
	}

	public String getViewName() {
		return viewName;
	}
}
