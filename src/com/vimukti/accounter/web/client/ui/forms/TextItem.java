package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;

public class TextItem extends FormItem {

	public TextBoxItem textBox;

	// TextBoxItem textBoxItem;
	public TextItem() {

		textBox = new TextBoxItem();
		// textBoxItem= new TextBoxItem();
		// @Override
		// public void sinkEvents(int eventBitsToAdd) {
		// super.sinkEvents(Event.ONFOCUS);
		// }
		//
		// @Override
		// public void onBrowserEvent(Event event) {
		// TextItem.this.showValidated();
		// super.onBrowserEvent(event);
		// }
		// };

		this.textBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {

				TextItem.this.showValidated();

			}
		});

		// if(!validate()){
		// parent.setNumCols(3);
		// addLabelWidget(parent);
		// parent.add(getMainWidget(), columnSpan);
		// Label label = new Label("Error");
		// label.setStyleName("ErrorLabel");
		// parent.add(label,1);
		// }
		// else{

	}

	public TextItem(String title) {
		textBox = new TextBoxItem();
		// @Override
		// public void sinkEvents(int eventBitsToAdd) {
		// super.sinkEvents(Event.ONFOCUS);
		// }
		//
		// @Override
		// public void onBrowserEvent(Event event) {
		// TextItem.this.showValidated();
		// super.onBrowserEvent(event);
		// }
		// };
		setTitle(title);
	}

	@Override
	public Object getValue() {
		if (textBox.getText() == null)
			return "";
		return textBox.getText();
	}

	@Override
	public String getDisplayValue() {
		if (textBox.getText() == null)
			return "";
		return textBox.getText();

	}

	@Override
	public void setValue(Object value) {
		if (value != null)
			this.textBox.setValue(value.toString());

	}

	public void setKeyPressHandler(KeyPressListener keyPressListener) {
		this.textBox.setKeyPressHandler(keyPressListener);
	}

	@Override
	public void addBlurHandler(BlurHandler blurHandler) {
		textBox.addBlurHandler(blurHandler);

	}

	public void setKeyBoardHandler(KeyPressHandler keyPressHandler) {
		textBox.addKeyPressHandler(keyPressHandler);
	}

	public void setKeyDownHandler(KeyDownHandler keyDownHandler) {
		textBox.addKeyDownHandler(keyDownHandler);
	}

	@Override
	public void addChangeHandler(ChangeHandler changeHandler) {
		textBox.addChangeHandler(changeHandler);
	}

	@Override
	public void addClickHandler(ClickHandler handler) {
		textBox.addClickHandler(handler);
	}

	public void setDefaultValue(int i) {
		// TODO Auto-generated method stub

	}

	// void helpimformationsetposition(){
	//		
	// }

	public void setHint(String string) {

	}

	public void focusInItem() {
		this.textBox.setFocus(true);

	}

	@SuppressWarnings("unused")
	private void setBorder() {

	}

	@Override
	public Widget getMainWidget() {
		return textBox;
	}

	@Override
	public void setDisabled(boolean b) {
		if (b)
			this.textBox.addStyleName("disable-TextField");
		this.textBox.setEnabled(!b);

	}

	public void addStyleName(String style) {
		textBox.addStyleName(style);
	}

	public void removeStyleName(String style) {
		textBox.removeStyleName(style);
	}

}