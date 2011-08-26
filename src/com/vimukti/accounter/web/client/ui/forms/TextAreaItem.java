package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TextAreaItem extends FormItem<String> {

	TextArea textArea;
	boolean isMemo;

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
		this.isMemo = isMemo;
		if (isMemo) {
			textArea.removeStyleName("gwt-TextArea");
			textArea.addStyleName("memoTextArea");
		}
	}

	public String getValue() {
		return textArea.getText();
	}

	public void setValue(String value) {
		this.textArea.setValue(value);
	}

	public String getDisplayValue() {
		return textArea.getText();

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		textArea.setTitle(toolTip);
	}

	@Override
	public Widget getMainWidget() {
		return this.textArea;
	}

	public void setDisabled(boolean b) {
		// this.getMainWidget().setEnabled(!b);
		if (b) {
			this.textArea.addStyleName("disable-TextField");
		} else {
			// this.textArea.setStyleName("gwt-TextBox");
			textArea.setStyleName("gwt-TextArea");
			// textArea.addStyleName("memoTextArea");
			if (isMemo) {
				textArea.removeStyleName("gwt-TextArea");
				textArea.addStyleName("memoTextArea");
			}
		}
		this.textArea.setEnabled(!b);

	}

	public void addClickHandler(ClickHandler handler) {

		this.textArea.addClickHandler(handler);
	}

}
