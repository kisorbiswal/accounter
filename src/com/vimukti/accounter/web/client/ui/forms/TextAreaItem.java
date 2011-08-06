package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TextAreaItem extends FormItem {

	TextArea textArea;

	public TextAreaItem(String title) {
		setTitle(title);
		textArea = new TextArea();
	}

	public TextAreaItem() {
		textArea = new TextArea();
	}

	// public TextAreaItem(String title){
	// textArea = new TextArea();
	// textArea.setWidth("100%");
	// textArea.setText(title);
	// }
	public void setMemo(boolean isMemo) {
		if (isMemo) {
			textArea.removeStyleName("gwt-TextArea");
			textArea.addStyleName("memoTextArea");
		}
	}

	public Object getValue() {
		return textArea.getText();
	}

	public void setValue(Object value) {
		this.textArea.setValue(String.valueOf(value));
	}

	public String getDisplayValue() {
		return textArea.getText();

	}

	@Override
	public Widget getMainWidget() {
		return this.textArea;
	}

	public void setDisabled(boolean b) {
		// this.getMainWidget().setEnabled(!b);
		if (b)
			this.textArea.addStyleName("disable-TextField");
		this.textArea.setEnabled(!b);

	}

	public void addClickHandler(ClickHandler handler) {

		this.textArea.addClickHandler(handler);
	}

}
