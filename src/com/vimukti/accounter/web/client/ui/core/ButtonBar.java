package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ButtonBar extends HorizontalPanel {
	private BaseView<?> view;
	private List<Widget> widgets = new ArrayList<Widget>();

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
		this.widgets.add(widget);
	}

	@Override
	public void insert(Widget w, int beforeIndex) {
		super.insert(w, beforeIndex);
		if (this.widgets.indexOf(w) == -1) {
			this.widgets.add(w);
		}
	}

	public void setDisabled(boolean disable) {
		for (Widget widget : widgets) {
			if (widget instanceof Button) {
				((Button) widget).setEnabled(!disable);
				if (disable) {
					widget.addStyleName("disable");
				} else {
					widget.removeStyleName("disable");
				}
			}
		}
	}
}
