package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;

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
	public void setMemo(boolean isMemo, AbstractBaseView view) {
		this.isMemo = isMemo;
		if (isMemo) {
			textArea.removeStyleName("gwt-TextArea");
			textArea.addStyleName("memoTextArea");
			textArea.setTitle(Accounter.messages().writeCommentsForThis(
					view.getAction().getViewName()));
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
	public void setTabIndex(int index) {
		textArea.setTabIndex(index);
	}

}
