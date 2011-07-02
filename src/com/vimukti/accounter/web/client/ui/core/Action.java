package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

/**
 * @author Raj Vimal
 * @modified by kumar kasimala
 * @modified by Fernandez
 */
public abstract class Action {

	/**
	 * This action's text, or <code>null</code> if none.
	 */
	private String text;

	/**
	 * This action's tool tip text, or <code>null</code> if none.
	 */
	private String toolTipText;

	private String iconString;

	@SuppressWarnings("unused")
	private AsyncCallback<Object> callbackObject;

	public String catagory = "";

	private FormItem formItemResponsibleForAction;

	private boolean allowMultiple;

	/**
	 * setting Text for Action
	 */
	public Action(String text) {
		setText(text);
	}

	/**
	 * Setting Text & Icon for Action
	 * 
	 * @param text
	 * @param iconString
	 */
	public Action(String text, String iconString) {
		setText(text);
		setIcon(iconString);
	}

	public Action(String text, String iconString, Object editableObject,
			AsyncCallback<Object> callbackObject) {
		setText(text);
		setIcon(iconString);
		// this.editableObject = editableObject;
		this.callbackObject = callbackObject;
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
	 * returns Icon path
	 * 
	 * @return String
	 */
	public String getIconString() {
		return iconString;
	}

	/**
	 * Runs this action. Each action implementation must define the steps needed
	 * to carry out this action. The default implementation of this method in
	 * <code>Action</code> does nothing.
	 * 
	 * @throws Throwable
	 */
	// public abstract void run();
	public abstract void run(Object data, Boolean isDependent);

	// public void run(IsSerializable data, Boolean isDependent, Date startDate,
	// Date endDate) {
	// }

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
	public void setIcon(String iconString) {
		this.iconString = iconString;
	}

	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public FormItem getActionSource() {

		return this.formItemResponsibleForAction;
	}

	public void setActionSource(FormItem comboItem) {
		this.formItemResponsibleForAction = comboItem;
	}

	public boolean allowMultiple() {

		return this.allowMultiple;
	}

	@Override
	public boolean equals(Object object) {

		Action action = (Action) object;

		if (action == null)

			return false;

		else {

			boolean isSameClass = object.getClass().getName().equals(
					this.getClass().getName());

			// boolean hasSameHashCode = object.hashCode() == this.hashCode();

			return isSameClass;

		}

	}

	public abstract ImageResource getBigImage();

	public abstract ImageResource getSmallImage();

	@SuppressWarnings("unchecked")
	public abstract ParentCanvas getView();

	public String getImageUrl() {
		return "";
	}
	
	public abstract String getHistoryToken();

}
