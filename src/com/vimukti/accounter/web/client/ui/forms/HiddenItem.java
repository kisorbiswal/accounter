package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HiddenItem extends FormItem {

	TextBox hiddenBox;

	public HiddenItem() {
		hiddenBox = new TextBox();
		this.setVisible(false);
	}

	@Override
	public Object getValue() {
		return hiddenBox.getValue();
	}

	@Override
	public String getDisplayValue() {
		return hiddenBox.getText();

	}

	@Override
	public void setValue(Object value) {
		this.hiddenBox.setValue(value.toString());
	}

	@Override
	public Widget getMainWidget() {
		return this.hiddenBox;
	}

}
