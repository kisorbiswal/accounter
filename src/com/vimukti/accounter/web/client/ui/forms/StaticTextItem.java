package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class StaticTextItem extends FormItem {

	HTML label;

	@Override
	public Widget getMainWidget() {
		return label;
	}

	public StaticTextItem() {
		label = new HTML((String) getValue());
	}

	@Override
	public void setValue(Object value) {
		if (value != null)
			this.label.setText(value.toString());
	}

	public void setHTML(String content) {
		this.label.setHTML(content);
	}

	public void setStyleName(String styleName) {
		label.addStyleName(styleName);
	}

}
