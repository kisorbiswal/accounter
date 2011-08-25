package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;

public class TextItem extends FormItem<String> {

	public TextBoxItem textBox;

	// TextBoxItem textBoxItem;
	public TextItem() {

		textBox = new TextBoxItem() {
			protected void onAttach() {
				super.onAttach();
				TextItem.this.onAttach();
			};
		};
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

	protected void onAttach() {

	}

	public TextItem(String title) {
		textBox = new TextBoxItem();
		setTitle(title);
	}

	@Override
	public String getValue() {
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
	public void setToopTip(String toolTip) {
		super.setToopTip(toolTip);
		textBox.setTitle(toolTip);
	}

	@Override
	public void setValue(String value) {
		if (value != null) {
			this.textBox.setText(value);
		}
	}

	public void setKeyPressHandler(KeyPressListener keyPressListener) {
		this.textBox.setKeyPressHandler(keyPressListener);
	}

	@Override
	public void addBlurHandler(BlurHandler blurHandler) {
		textBox.addBlurHandler(blurHandler);

	}

	@Override
	public void addFocusHandler(
			com.google.gwt.event.dom.client.FocusHandler focusHandler) {
		this.textBox.addFocusHandler(focusHandler);
	};

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
		// NOTHING TO DO.
	}

	// void helpimformationsetposition(){
	//
	// }

	public void setHint(String string) {

	}

	public void focusInItem() {
		this.textBox.setFocus(true);

	}

	@Override
	public Widget getMainWidget() {
		return textBox;
	}

	@Override
	public void setDisabled(boolean b) {
		if (b) {
			this.textBox.addStyleName("disable-TextField");
		} else {
			this.textBox.setStyleName("gwt-TextBox");
		}
		this.textBox.setEnabled(!b);

	}

	public void addStyleName(String style) {
		textBox.addStyleName(style);
	}

	public void removeStyleName(String style) {
		textBox.removeStyleName(style);
	}

}