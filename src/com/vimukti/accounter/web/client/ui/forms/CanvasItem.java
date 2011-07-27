package com.vimukti.accounter.web.client.ui.forms;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CanvasItem extends FormItem implements HasWidgets {

	VerticalPanel vPanel;

	public CanvasItem() {
		vPanel = new VerticalPanel();
	}

	public void setCanvas(Widget widget) {
		vPanel.add(widget);

	}

	@Override
	public Widget getMainWidget() {

		return this.vPanel;
	}

	@Override
	public void add(Widget w) {

	}

	@Override
	public void clear() {

	}

	@Override
	public Iterator<Widget> iterator() {
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		return false;
	}

}
