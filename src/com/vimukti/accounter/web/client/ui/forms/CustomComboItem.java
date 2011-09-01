package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	private CustomFocusWidget customFocusWidget;
	private ClickHandler clickHandler;
	private KeyPressHandler keyPressHandler;
	private KeyDownHandler keyDownHandler;

	// public BlurHandler blurHandler;

	// TextBoxItem textBoxItem;
	public CustomComboItem() {

		HorizontalPanel horizontalPanel = new HorizontalPanel();

		textBox = new TextBoxItem() {
			protected void onAttach() {
				super.onAttach();
				CustomComboItem.this.onAttach();
			};
		};

		horizontalPanel.add(textBox);
		downarrowpanel = new SimplePanel();
		downarrowpanel.addStyleName("downarrow-button");
		horizontalPanel.add(downarrowpanel);
		horizontalPanel.getWidget(0).getElement().getParentElement().getStyle()
				.setPaddingLeft(0, Unit.PX);
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

				CustomComboItem.this.showValidated();

			}
		});

		customFocusWidget = new CustomFocusWidget(horizontalPanel.getElement());

		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (textBox.isEnabled())
					CustomComboItem.this.clickHandler.onClick(event);
			}
		};

		customFocusWidget.addClickHandler(clickHandler);

		KeyPressHandler keyPressHandler = new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (CustomComboItem.this.keyPressHandler != null)
					CustomComboItem.this.keyPressHandler.onKeyPress(event);
			}
		};

		customFocusWidget.addKeyPressHandler(keyPressHandler);

		KeyDownHandler keyDownHandler = new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (CustomComboItem.this.keyDownHandler != null)
					CustomComboItem.this.keyDownHandler.onKeyDown(event);
			}
		};

		customFocusWidget.addKeyDownHandler(keyDownHandler);

		// BlurHandler blurHandler = new BlurHandler() {
		//
		// @Override
		// public void onBlur(BlurEvent event) {
		// textBox.setFocus(true);
		// }
		// };
		//
		// customFocusWidget.addBlurHandler(blurHandler);

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
	public void addBlurHandler(BlurHandler blurHandler) {
		textBox.addBlurHandler(blurHandler);
		// this.blurHandler = blurHandler;
	}

	public void setKeyBoardHandler(KeyPressHandler keyPressHandler) {
		textBox.addKeyPressHandler(keyPressHandler);
		this.keyPressHandler = keyPressHandler;
	}

	public void setKeyDownHandler(KeyDownHandler keyDownHandler) {
		textBox.addKeyDownHandler(keyDownHandler);
		this.keyDownHandler = keyDownHandler;
	}

	@Override
	public void addChangeHandler(ChangeHandler changeHandler) {
		textBox.addChangeHandler(changeHandler);
	}

	@Override
	public void addClickHandler(ClickHandler handler) {
		// textBox.addClickHandler(handler);
		this.clickHandler = handler;
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

	private void setBorder() {

	}

	@Override
	public Widget getMainWidget() {
		return customFocusWidget;
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

	public class CustomFocusWidget extends FocusWidget {
		public CustomFocusWidget() {
			super();
		}

		public CustomFocusWidget(Element element) {
			super(element);
		}

		@Override
		protected void onAttach() {
			super.onAttach();
			textBox.setFocus(true);
		}
	}

}