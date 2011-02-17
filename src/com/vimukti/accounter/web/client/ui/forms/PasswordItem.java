package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

public class PasswordItem extends FormItem {

	PasswordTextBox passwordTextBox;

	public PasswordItem(String string) {
		this.passwordTextBox.setValue(string);
	}

	public PasswordItem() {
		passwordTextBox = new PasswordTextBox();
	}

	@Override
	public Object getValue() {
		return passwordTextBox.getValue();
	}

	@Override
	public String getDisplayValue() {
		return passwordTextBox.getText();

	}

	@Override
	public void setValue(Object value) {
		this.passwordTextBox.setValue(value.toString());
	}

	@Override
	public Widget getMainWidget() {
		return this.passwordTextBox;
	}
	
	@Override
	public void setDisabled(boolean b) {
		// this.getMainWidget().setEnabled(!b);
		this.setDisabled(b);
		this.passwordTextBox.setEnabled(!b);

	}

}
