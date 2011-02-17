package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class LinkItem extends FormItem {

	Hyperlink hyperlink;

	public LinkItem() {
		this.hyperlink = new Hyperlink();
	}

	public void setLinkTitle(String title) {
		this.hyperlink.setText(title);

	}

	@Override
	public Object getValue() {
		return hyperlink.getText();
	}
	
	@Override
	public void setDisabled(boolean b) {
		this.hyperlink.setVisible(!b);
	
	}

	@Override
	public String getDisplayValue() {
		return hyperlink.getText();

	}

	@Override
	@SuppressWarnings("deprecation")
	public void addClickHandler(ClickHandler clickHandler) {
		this.hyperlink.addClickHandler(clickHandler);

	}

	@Override
	public void setValue(Object value) {
		this.hyperlink.setText(value.toString());
	}

	@Override
	public Widget getMainWidget() {
		if (this.getDisabled()) {
			this.hyperlink.setVisible(false);
			return this.hyperlink;
		} else {
			return this.hyperlink;
		}

	}
	
	

}
