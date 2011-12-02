package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;

public class CurrencyFormItem extends FormItem<String> {

	public TextBoxItem textBox;

	HorizontalPanel datePanel = new HorizontalPanel();

	private Label lable1;

	public CurrencyFormItem() {

		textBox = new TextBoxItem() {
			protected void onAttach() {
				super.onAttach();
				CurrencyFormItem.this.onAttach();
			};
		};

		this.textBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				CurrencyFormItem.this.showValidated();
			}
		});
		lable1 = new com.google.gwt.user.client.ui.Label(" ");
		datePanel.add(textBox);
		datePanel.add(lable1);

	}

	protected void onAttach() {

	}

	public CurrencyFormItem(String header, String footerText) {
		textBox = new TextBoxItem();
		lable1 = new com.google.gwt.user.client.ui.Label(footerText);
		datePanel.setStyleName("company_currency_name");
		lable1.setStyleName("");
		datePanel.add(textBox);
		datePanel.add(lable1);
		setTitle(header);
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
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
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

		return datePanel;
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

	public void setTabIndex(int index) {
		textBox.setTabIndex(index);
	}
	
	public void changeTextField(double value){
		textBox.setText(Double.toString(value));
	}
}
