package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

public class CheckboxItem extends FormItem<Boolean> {

	private CheckBox checkBox = new CheckBox();

	@Override
	public Boolean getValue() {
		if (checkBox.getValue() == null)
			return false;
		return checkBox.getValue();
	}

	@Override
	public String getDisplayValue() {
		if (checkBox.getText() == null)
			return "";
		return checkBox.getText();

	}

	@Override
	public void setValue(Boolean value) {
		checkBox.setValue(value);

	}

//	public CheckboxItem() {
//
//	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		checkBox.setTitle(toolTip);
	}

	public CheckboxItem(String title,String styleName, String description) {
		super(title,styleName);
		this.checkBox = new CheckBox();
		this.checkBox.setText(description);
		this.checkBox.addStyleName("checkBox");
		this.add(checkBox);
		this.addStyleName("checkboxItem");

	}

	public CheckboxItem(String title,String styleName) {
		super(title,styleName);
		this.checkBox = new CheckBox();
		this.add(checkBox);
		this.addStyleName("checkboxItem");
	}

	public CheckboxItem() {
		super("checkBox","wareHouseCheckbox");
	}

	public void addChangeHandler(ValueChangeHandler<Boolean> ChangeHandler) {
		this.checkBox.addValueChangeHandler(ChangeHandler);
	}

	@Override
	public Widget getMainWidget() {
		return this.checkBox;
	}

	@Override
	public void setEnabled(boolean b) {
		this.checkBox.setEnabled(b);
	}

	public boolean isChecked() {
		return getValue();
	}

	public void setTabIndex(int index) {
		checkBox.setTabIndex(index);
	}


}
