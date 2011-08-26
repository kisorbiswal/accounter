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

	public CheckboxItem() {

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		checkBox.setTitle(toolTip);
	}

	public CheckboxItem(String name, String description) {

		this.checkBox = new CheckBox();
		// @Override
		// public void sinkEvents(int eventBitsToAdd) {
		// super.sinkEvents(Event.ONFOCUS);
		// }
		//
		// @Override
		// public void onBrowserEvent(Event event) {
		// CheckboxItem.this.showValidated();
		// super.onBrowserEvent(event);
		// }
		this.setName(name);
		this.checkBox.setText(description);

	}

	public CheckboxItem(String description) {
		this.checkBox = new CheckBox();
		this.checkBox.setText(description);

	}

	public void addChangeHandler(ValueChangeHandler<Boolean> ChangeHandler) {
		this.checkBox.addValueChangeHandler(ChangeHandler);
	}

	@Override
	public Widget getMainWidget() {
		return this.checkBox;
	}

	@Override
	public void setDisabled(boolean b) {
		// this.getMainWidget().setEnabled(!b);
		// this.setDisabled(b);
		this.checkBox.setEnabled(!b);

	}

	public boolean isChecked() {
		return (Boolean) getValue();
	}

}
