package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LabelItem extends FormItem {

	private Label label;

	public LabelItem() {
		label = new Label();
	}

	@Override
	public Widget getMainWidget() {
		return label;
	}

	@Override
	public void setValue(Object value) {
		this.label.setText(value.toString());
		super.setValue(value);
	}

	public void addStyleName(String style) {
		label.addStyleName(style);

	}

	public void setWidth(String width) {
		label.setWidth(width);
	}
	
	public void addClickHandler(ClickHandler handler){
		label.addClickHandler(handler);
	}

}
