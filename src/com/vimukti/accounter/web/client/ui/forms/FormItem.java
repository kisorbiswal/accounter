package com.vimukti.accounter.web.client.ui.forms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

public abstract class FormItem {

	private String title;
	private String name;
	private Object value;
	private List<Validator> validator = new ArrayList<Validator>();
	private boolean required;
	private Label label;
	@SuppressWarnings("unused")
	private int width;
	protected boolean isDisabled = false;
	private boolean showTitle = true;
	private String defaultValue;
	private int columnSpan = 1;
	private boolean isHighlighted = false;
	private String titleStyleName;

	public Object getValue() {
		return this.value;
	}

	public String getTitle() {
		return title;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public String getDisplayValue() {
		return this.value.toString();

	}

	public void setColSpan(int colSpan) {
		this.columnSpan = colSpan;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public void setValueField(String user) {
		this.value = user;

	}

	public void highlight() {
		getMainWidget().addStyleName("highlightedFormItem");
		getMainWidget().setTitle(
				FinanceApplication.getCustomersMessages().invalidValue());
		this.isHighlighted = true;

	}

	public boolean isHighLighted() {
		return isHighlighted;
	}

	public String getValueField() {
		return this.name;
	}

	public void setTitle(String string) {
		this.title = string;
	}

	public void setWidth(int width) {
		this.width = width;
		getMainWidget().setWidth(new Integer(width).toString() + "%");
	}

	public void setWidth(String width) {
		this.width = Integer.parseInt(width.replace("%", "").replace("px", ""));
		getMainWidget().setWidth(width);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setValidators(Validator validate) {
		this.getValidator().add(validate);
	}

	public void setDisabled(boolean b) {
		this.isDisabled = b;

	}

	public void setRequired(boolean required) {
		this.required = required;
		if (required) {
			this.getValidator().add(new RequiredFieldValidator());
		} else {
			this.getValidator().clear();
		}
	}

	public void show() {
		getLabelWidget().setVisible(true);
		getMainWidget().setVisible(true);
	}

	private Label getLabelWidget() {
		return this.label;
	}

	public void hide() {
		getLabelWidget().setVisible(false);
		getMainWidget().setVisible(false);
	}

	public void addClickHandler(ClickHandler handler) {
		// getMainWid/get().addClickHandler(handler);

	}// TODO Auto-generated method stub

	public void setValueMap(String value) {
		// TODO Auto-generated method stub

	}

	public void setDefaultValue(String value) {
		defaultValue = value;
	}

	public void setValueMap(String[] options) {
		// TODO Auto-generated method stub

	}

	public boolean getDisabled() {
		return isDisabled;
	}

	public String getName() {
		return this.name;

	}

	void addWidgets(DynamicForm parent) {

		addLabelWidget(parent);
		parent.add(getMainWidget(), columnSpan);
	}

	public abstract Widget getMainWidget();

	private void addLabelWidget(DynamicForm parent) {
		if (showTitle) {
			label = new Label(this.title);
			label.addStyleName("NoWrapping");
			if (this.titleStyleName != null && !this.titleStyleName.equals(""))
				label.addStyleName(titleStyleName);
			if (required) {
				label.addStyleName("requiredField");
			}
			parent.add(label, 1);
		}
	}

	public void setVisible(boolean visible) {
		getMainWidget().setVisible(visible);
		if (label != null)
			label.setVisible(visible);
	}

	public void setHeight(int height) {
		getMainWidget().setHeight(new Integer(height).toString());
	}

	public void addChangeHandler(ChangeHandler changeHandler) {

	}

	public void addChangedHandler(ChangeHandler changeHandler) {

	}

	public void addFocusHandler(FocusHandler focusHandler) {

	}

	public void addBlurHandler(BlurHandler blurHandler) {

	}

	public void addKeyBoardHandler(KeyPressHandler keyPressHandler) {

	}

	public List<Validator> getValidator() {
		return validator;
	}

	public void setValidator(List<Validator> validator) {
		this.validator = validator;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public boolean validate() {
		if (this.getValidator() != null) {
			int matchingStatus = 0;
			for (Validator validator : this.getValidator()) {

				if (validator.validate(this)) {
					matchingStatus++;
				}

			}
			if (matchingStatus == this.getValidator().size()) {
				return true;

			} else
				highlight();
			return false;

		}
		return true;

	}

	public void showValidated() {

		getMainWidget().removeStyleName("highlightedFormItem");
		getMainWidget().setTitle("");

	}

	public void setTitleStyleName(String titleStyleName) {
		this.titleStyleName = titleStyleName;

	}

	public void setType(String type) {

	}

	public String getType() {

		return null;
	}

	public void setAttribute(String attribute, String value) {

	}

	public void setAttribute(String attribute, Object value) {

	}

	/**
	 * call this method to set focus to Item
	 */
	public void setFocus() {
		if (this.getMainWidget() instanceof FocusWidget) {
			((FocusWidget) this.getMainWidget()).setFocus(true);
		}
	}
}
