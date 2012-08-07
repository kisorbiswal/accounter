package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ButtonBar implements IButtonBar {
	private List<Widget> widgets = new ArrayList<Widget>();

	FlowPanel buttonBar;

	public ButtonBar() {
		buttonBar = new FlowPanel();
		buttonBar.addStyleName("button_bar");
	}

	public void add(Button widget) {
		this.add(widget, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public void remove(Button widget) {
		buttonBar.remove(widget);
	}

	public void add(Button widget, HorizontalAlignmentConstant alignment) {
		buttonBar.add(widget);
		// this.setCellHorizontalAlignment(widget, alignment);
		this.widgets.add(widget);
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

	@Override
	public void addTo(HasWidgets w) {
		w.add(buttonBar);
	}

	@Override
	public void removeButton(HasWidgets parent, Button child) {
		parent.remove(child);
	}

	@Override
	public void addButton(HasWidgets parent, Button child) {
		parent.add(child);
	}

	@Override
	public Widget asWidget() {
		return buttonBar;
	}
}
