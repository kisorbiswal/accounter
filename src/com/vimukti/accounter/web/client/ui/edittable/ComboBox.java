package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class ComboBox<T, C> extends FlowPanel implements RowSelectHandler<C> {
	private T row;
	private C value;
	private PopupPanel popupPanel;
	// private ScrollPanel scrollPanel;
	private TextBox textBox;
	private ComboChangeHandler<T, C> changeHandler;
	private AbstractDropDownTable<C> dropDown;
	private ScrollPanel scrollPanel;

	public ComboBox() {
		this.addStyleName("comboBox");
		textBox = new TextBox();
		textBox.sinkEvents(0);// Check and remove
		this.add(textBox);
		SimplePanel downarrowpanel = new SimplePanel();

		downarrowpanel.addStyleName("downarrow-button");

		downarrowpanel.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				textBox.setFocus(true);
			}
		}, ClickEvent.getType());
		this.add(downarrowpanel);

		FocusHandler focusHandler = new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				showPopup();
			}

		};
		textBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {

					@Override
					public boolean execute() {
						hidePopup(true);
						return false;
					}
				}, 200);
			}
		});
		textBox.addFocusHandler(focusHandler);
		textBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				switch (event.getNativeEvent().getKeyCode()) {
				case KeyCodes.KEY_ESCAPE:
					hidePopup(true);
					break;
				case KeyCodes.KEY_DOWN:
					textBox.setText("");
					showPopup();
					dropDown.downKeyPress();
					break;
				case KeyCodes.KEY_UP:
					textBox.setText("");
					showPopup();
					dropDown.upKeyPress();
					return;
				default:
				}
			}
		});
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				switch (event.getNativeKeyCode()) {
				case KeyCodes.KEY_BACKSPACE:
				case KeyCodes.KEY_DELETE:
				case KeyCodes.KEY_UP:
				case KeyCodes.KEY_DOWN:
					return;
				case KeyCodes.KEY_ENTER:
					if (value == null) {
						textBox.setText("");
						dropDown.addNewItem();
					}
					hidePopup(value != null);
					break;
				case KeyCodes.KEY_ESCAPE:
					hidePopup(false);
					break;
				default:
					dropDown.updateSelection(textBox.getText().toLowerCase());
				}
			}
		});
		popupPanel = new PopupPanel(false, false);
		scrollPanel = new ScrollPanel();
		popupPanel.add(scrollPanel);
	}

	private void hidePopup(boolean updateValue) {
		if (updateValue) {
			C filteredValue = dropDown.getFilteredValue(textBox.getText());
			if (filteredValue != null) {
				setValue(filteredValue);
				editComplete();
			} else {
				if (changeHandler != null) {
					changeHandler.onAddNew(textBox.getText());
				}
			}
		}
		popupPanel.hide();
	}

	private void editComplete() {
		if (changeHandler != null && value != null) {
			changeHandler.onChange(row, value);
		}
	}

	private void showPopup() {
		if (popupPanel.isShowing()) {
			return;
		}
		scrollPanel.clear();
		scrollPanel.add(dropDown);
		dropDown.addRowSelectHandler(this);
		int x = textBox.getAbsoluteLeft();
		int y = textBox.getAbsoluteTop() + textBox.getOffsetHeight();

		int clientwidth = Window.getClientWidth();
		int clientHeight = Window.getClientHeight();

		int popupWdth = popupPanel.getOffsetWidth();
		int popupHeight = popupPanel.getOffsetHeight();

		if (UIUtils.isMSIEBrowser()) {
			popupPanel.setHeight(Math.min(dropDown.getOffsetHeight(), 100)
					+ "px");
			popupPanel.setWidth(dropDown.getOffsetWidth() + "px");
		} else {
			popupPanel.setWidth(textBox.getOffsetWidth() + "px");
		}
		// popupPanel.setHeight(Math.min(dropDown.getOffsetHeight(), 200) +
		// "px");
		popupPanel.setHeight("100px");

		if ((x + popupWdth) > clientwidth) {
			x = x - (popupPanel.getOffsetWidth() - this.getOffsetWidth());
		}

		if ((y + popupHeight) > clientHeight) {
			y = y - (popupPanel.getOffsetHeight() - textBox.getOffsetHeight());
		}

		popupPanel.setPopupPosition(x, y);
		popupPanel.show();
	}

	public C getValue() {
		return value;
	}

	public void setValue(C value) {
		if (value != null) {
			textBox.setText(dropDown.getDisplayValue(value));
			dropDown.selectRow(value);
		}
		this.value = value;
	}

	public void setComboChangeHandler(ComboChangeHandler<T, C> changeHandler) {
		this.changeHandler = changeHandler;
	}

	public AbstractDropDownTable<C> getDropDown() {
		return dropDown;
	}

	public void setDropDown(AbstractDropDownTable<C> table) {
		this.dropDown = table;
	}

	public void setDesable(boolean desable) {
		textBox.setEnabled(!desable);
	}

	public T getRow() {
		return row;
	}

	public void setRow(T row) {
		this.row = row;
	}

	@Override
	public void onRowSelect(C selectedObj, boolean isClicked) {
		value = selectedObj;
		if (selectedObj != null) {
			String text = textBox.getText().toLowerCase();
			String displayValue = dropDown.getDisplayValue(selectedObj);
			textBox.setText(displayValue);
			int len = displayValue.length() - text.length();
			int pos = displayValue.toLowerCase().indexOf(text);
			if (len > 0) {
				textBox.setSelectionRange(pos + text.length(), len);
			}
			editComplete();
		} else {
			if (isClicked) {
				hidePopup(false);
				textBox.setText("");
				dropDown.addNewItem();
			}
		}
	}
}
