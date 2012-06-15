package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public class StyledPanel extends FlowPanel {

	private Object userObject;

	public StyledPanel(String styleName) {
		super.setStyleName(styleName);
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	@SuppressWarnings("rawtypes")
	public void add(FormItem... items) {
		for (FormItem item : items) {
			add(item);
		}
	}
}
