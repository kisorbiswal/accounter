package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ButtonBar extends HorizontalPanel {
	private BaseView<?> view;

	public ButtonBar(BaseView<?> view) {
		this.view = view;
		this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		this.setSpacing(10);
		this.setHeight("30px");
	}

	public void add(Widget widget) {
		this.add(widget, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public void add(Widget widget, HorizontalAlignmentConstant alignment) {
		super.add(widget);
		this.setCellHorizontalAlignment(widget, alignment);
	}
}
