package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;

public class CustomComboItem extends FormItem {

	@Override
	public void setFocus() {
		textBox.setFocus(true);
	}

	public TextBoxItem textBox;
	private SimplePanel downarrowpanel;
	private Widget mainWidget;

	public CustomComboItem() {

		FlowPanel panel = new FlowPanel();

		textBox = new TextBoxItem() {
			protected void onAttach() {
				super.onAttach();
				CustomComboItem.this.onAttach();
			};
		};
		textBox.sinkEvents(0);
		panel.add(textBox);
		downarrowpanel = new SimplePanel();

		downarrowpanel.addStyleName("downarrow-button");
		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				textBox.setFocus(true);
			}
		};
		addClickHandler(clickHandler);
		downarrowpanel.addDomHandler(clickHandler, ClickEvent.getType());
		panel.add(downarrowpanel);

		panel.getWidget(0).getElement().getParentElement().getStyle()
				.setPaddingLeft(0, Unit.PX);
		mainWidget = panel;

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		String tooltip = toolTip.replace("  ", " ");
		textBox.setTitle(tooltip);
	}

	protected void onAttach() {

	}

	public CustomComboItem(String title) {
		this();
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
	public void addFocusHandler(FocusHandler handler) {
		textBox.addFocusHandler(handler);
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

	@Override
	public Widget getMainWidget() {
		return mainWidget;
	}

	@Override
	public void setDisabled(boolean b) {
		if (b)
			this.textBox.addStyleName("disable-TextField");
		else
			this.textBox.removeStyleName("disable-TextField");
		this.textBox.setEnabled(!b);
		this.downarrowpanel.getElement().getStyle().setOpacity(b ? 0.6 : 1);

	}

	public void addStyleName(String style) {
		textBox.addStyleName(style);
	}

	public void removeStyleName(String style) {
		textBox.removeStyleName(style);
	}
}