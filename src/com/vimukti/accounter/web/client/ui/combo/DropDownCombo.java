package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.vimukti.accounter.web.client.externalization.AccounterComboMessges;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.CustomComboItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public abstract class DropDownCombo<T> extends CustomComboItem {

	protected IAccounterComboSelectionChangeHandler<T> handler;

	AccounterComboMessges comboMessages = Accounter.comboMessages();
	private boolean isAddNewRequire;
	private DropDownTable<T> dropDown;
	private int cols;
	List<T> comboItems = new ArrayList<T>();
	private PopupPanel popup;
	ListGrid grid = null;

	String selectedName;
	int selectedIndex = -1;
	protected T selectedObject;
	private boolean haveCloseHandle;

	List<T> maincomboItems = new ArrayList<T>();

	private ScrollPanel panel;

	public DropDownCombo(String title, boolean isAddNewRequire, int noOfcols) {
		super();
		createControls(title, isAddNewRequire, noOfcols);
		init();
	}

	private void createControls(String title, boolean isAddNewRequire,
			int noOfcols) {
		this.isAddNewRequire = isAddNewRequire;
		this.cols = noOfcols;
		super.setName(title);
		setTitle(title);
		this.addStyleName("custom-combo");
		this.addStyleName("dropdown-button");

		dropDown = new DropDownTable<T>(this) {
			@Override
			public void cellClicked(int row, int col) {
				if (DropDownCombo.this.comboItems.size() >= row)
					selectedObject = DropDownCombo.this.comboItems.get(row);
				super.cellClicked(row, col);
			}

			@Override
			public Column[] getColumns() {
				Column[] columns = new Column[cols];
				int i;
				for (i = 0; i < cols; i++) {
					columns[i] = createColumn(i);
				}
				setColumnActions(columns);
				return columns;
			}

			private Column createColumn(final int col) {
				Column<T, String> column = new Column<T, String>(
						new ClickableTextCell()) {

					@Override
					public String getValue(T object) {
						// if (object.equals("emptyRow"))
						// return "    ";
						if (object.equals("addNewCaption")) {
							if (cols > 1)
								return (col == 1) ? getDefaultAddNewCaption()
										: "  ";
							else
								return getDefaultAddNewCaption();
						}
						return getColumnData(object, 0, col);
					}

				};
				return column;
			}

			private void setColumnActions(Column<T, String>[] columns) {
				for (int i = 0; i < cols; i++) {
					columns[i].setFieldUpdater(new FieldUpdater<T, String>() {

						@Override
						public void update(int index, T object, String value) {
							selectedIndex = index;
							eventFired(index);
						}
					});
				}
			}

		};

		panel = new ScrollPanel();
		panel.getElement().removeAttribute("style");
		panel.addStyleName("dropdownTable");
		panel.add(dropDown);

		dropDown.setNoColumns(noOfcols);
		dropDown.init(isAddNewRequire);

		// this.setWidth("99.5%");

		popup = new PopupPanel(true) {
			@Override
			protected void onUnload() {
				super.onUnload();
				// dropDown.resetPopupStyle();
				resetComboList();
			}

			@Override
			protected void onLoad() {
				super.onLoad();
			}

			@Override
			public void onBrowserEvent(Event event) {
				switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEOUT:
					Element element = event.getTarget();
					if (this.getElement().equals(element)) {
						this.hide();
					}
					break;

				default:
					break;
				}
				super.onBrowserEvent(event);
			}
		};

		if (!UIUtils.isMSIEBrowser()) {
			popup.setWidth("100%");
		}

		popup.add(panel);
		popup.setStyleName("popup");
		popup.getElement().setAttribute("id", "popuppaneldropdown");
		dropDown.getElement().getStyle().setCursor(Cursor.POINTER);

		FocusHandler focusHandler = new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				showPopup();
			}
		};
		addFocusHandler(focusHandler);

		dropDown.addEscapEventHandler(new EscapEventHandler() {

			@Override
			public void onEscap(KeyDownEvent event) {
				popup.hide();
				textBox.setFocus(true);
			}
		});
		// addBlurHandler(blurHandler);
		// dropDown.addDomHandler(focusHandler, FocusEvent.getType());
		// dropDown.addDomHandler(blurHandler, BlurEvent.getType());
		addKeyPressHandler();

		this.removeStyleName("gwt-TextBox");
	}

	protected void showPopup() {
		if (DropDownCombo.this.getDisabled())
			return;
		if(!isAddNewRequire && comboItems.isEmpty())
			return;
		dropDown.getRowElement(0).getStyle().setHeight(15, Unit.PX);
		int x = getMainWidget().getAbsoluteLeft();
		int y = getMainWidget().getAbsoluteTop() + 27;
		dropDown.setWidth(getMainWidget().getOffsetWidth() - 2 + "px");
		// dropDown.setHeight(getMainWidget().getOffsetHeight() + "px");
		//
		popup.setPopupPosition(x + 1, y);

		popup.show();
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if ((selectedName == null || !selectedName.equals(getValue()
						.toString())) && selectedIndex == -1)
					if (haveCloseHandle)
						setRelatedComboItem(getValue().toString());
			}
		});
		int clientwidth = Window.getClientWidth();
		int clientHeight = Window.getClientHeight();
		int popupWdth = popup.getWidget().getOffsetWidth();
		int popupHeight = 0;
		if (DropDownCombo.this instanceof AccountCombo) {
			popupHeight = 200;
		} else
			popupHeight = popup.getWidget().getOffsetHeight();

		if (UIUtils.isMSIEBrowser()) {
			dropDown.setHeight(Math.min((comboItems.size() * 10), 100) + "px");
			popup.setHeight(Math.min(dropDown.getOffsetHeight(), 100) + "px");
			panel.setWidth(dropDown.getOffsetWidth() + "px");
		}
		panel.setHeight(Math.min(dropDown.getOffsetHeight(), 200) + "px");

		if ((x + popupWdth) > clientwidth) {
			x = x - (popup.getOffsetWidth() - getMainWidget().getOffsetWidth());
			popup.setPopupPosition(x + 1, y);
		}

		if ((y + popupHeight) > clientHeight) {
			y = y
					- (popup.getOffsetHeight() - getMainWidget()
							.getOffsetHeight());
			popup.setPopupPosition(x, y - 42);
		}
		panel.setHeight(Math.min(dropDown.getOffsetHeight(), 200) + "px");
	}

	private void resetComboList() {
		if (!maincomboItems.isEmpty()) {
			initCombo(maincomboItems);
			maincomboItems.clear();
		}
	}

	private void addKeyPressHandler() {

		setKeyBoardHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				haveCloseHandle = true;
				char key = event.getCharCode();
				// if ((key >= 48 && key <= 57) || (key >= 65 && key <= 90)
				// || (key >= 96 && key <= 122)) {
				if (key >= 32 && key <= 126) {
					onKeyEnter(key);
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE
						|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE) {

					Timer timer = new Timer() {

						@Override
						public void run() {
							onKeyEnter('/');
						}
					};
					timer.schedule(200);
					// key codes 38 for up key 40 for down key
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN
						&& popup.isShowing()) {
					dropDown.setKeyboardSelected(0, true, true);

					// dropDown.setKeyboardSelected(++dropDown.selectedPosition,
					// true, true);
					// if (dropDown.selectedPosition == dropDown
					// .getDisplayedItems().size()) {
					// dropDown.selectedPosition = -1;
					// }
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP
						&& popup.isShowing()) {
					dropDown.setKeyboardSelected(dropDown.getDisplayedItems()
							.size() - 1, true, true);

					// int posi = dropDown.getDisplayedItems().size();
					// dropDown.setKeyboardSelected(posi
					// + (--dropDown.selectedPosition), true, true);
					// if ((dropDown.selectedPosition + dropDown
					// .getDisplayedItems().size()) == 0) {
					// dropDown.selectedPosition = 0;
					// }
				} else if ((event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE || event
						.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB)
						&& popup.isShowing()) {
					haveCloseHandle = false;
					popup.hide();
				}
			}

		});

		// if (UIUtils.isMSIEBrowser()) {
		setKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE
						|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE) {

					Timer timer = new Timer() {

						@Override
						public void run() {
							onKeyEnter('/');
						}
					};
					timer.schedule(200);
					// key codes 38 for up key 40 for down key
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN
						&& popup.isShowing()) {
					dropDown.setKeyboardSelected(0, true, true);

				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP
						&& popup.isShowing()) {
					dropDown.setKeyboardSelected(dropDown.getDisplayedItems()
							.size() - 1, true, true);
				}
				// else if (event.getNativeEvent().getKeyCode() ==
				// KeyCodes.KEY_ESCAPE
				// && popup.isShowing()) {
				// popup.hide();
				// }
			}
		});
		// }

	}

	/**
	 * Override this method to do anything before the other overridden methods
	 * are called from constructor.
	 */
	protected void init() {
	}

	public void setWidth(String width) {
		super.setWidth(width);

	}

	/**
	 * "" Should be Called only Once For Re-Initializing use, setComboItems
	 * 
	 * @param list
	 */
	public void initCombo(List<T> list) {
		// this.setValue(null);
		this.comboItems.clear();
		// this.dropDown.clear();
		if (list != null)
			for (T object : list) {
				if (object != null)
					checkObject(object);
			}
		setRowsData(comboItems);
	}

	private void setRowsData(List<T> cmbItems) {
		this.dropDown.setRowCount(cmbItems.size()
				+ dropDown.getDummyRecords().size());
		this.dropDown.setPageSize(dropDown.getRowCount());
		this.dropDown.setRowData(dropDown.getDummyRecords().size(), cmbItems);

	}

	public List<T> getComboItems() {
		return comboItems;
	}

	public void addItem(T object) {
		// checks the object is conains in comboitems
		checkObject(object);

		setRowsData(comboItems);

	}

	private void checkObject(T object) {
		if (object == null)
			return;
		// String text;
		// int rowCount = comboItems.contains(object) ?
		// comboItems.indexOf(object) > 0 ? comboItems
		// .indexOf(object) + 1
		// : comboItems.indexOf(object)
		// : this.dropDown.getRowCount();

		if (!comboItems.contains(object))
			comboItems.add(object);

	}

	/**
	 * This method is invoked from AbstractBaseView to setPreviousOutput
	 * 
	 * @param obj
	 */

	public void addItemThenfireEvent(T obj) {
		this.initCombo(new ArrayList<T>(this.comboItems));
		setComboItem(obj);
		if (handler != null) {
			handler.selectedComboBoxItem(obj);
		}
	}

	@Override
	public void setValue(Object value) {

		if (value == null) {
			super.setValue("");
		} else {
			if (!value.equals(getDefaultAddNewCaption()))
				super.setValue(value);
		}
	}

	/**
	 * This method is invoked when a new item is added to the listbox
	 * 
	 * @param object
	 *            -- object to be added
	 * @param addIfNotinList
	 */

	public void setComboItem(T obj) {
		selectedObject = obj;
		if (comboItems != null && obj != null) {
			addComboItem(obj);
			int index = comboItems.indexOf(obj);
			if (isAddNewRequire)
				setSelectedItem(obj, index + 1);
			else
				setSelectedItem(obj, index);
		} else
			addItem(obj);

	}

	/**
	 * This method will be invoked when a new item about to add in effect of
	 * "addNewXXX" option
	 * 
	 * @param obj
	 */
	public void addComboItem(T obj) {

		if (!hasItem(obj)) {
			addItem(obj);
		}
	}

	public boolean hasItem(T object) {
		return this.comboItems.contains(object);
	}

	public IAccounterComboSelectionChangeHandler<T> getHandler() {
		return handler;
	}

	public void addSelectionChangeHandler(
			IAccounterComboSelectionChangeHandler<T> handler) {
		this.handler = handler;
	}

	protected abstract String getColumnData(T object, int row, int col);

	protected abstract String getDisplayName(T object);

	public void setNoColumns(int cols) {
		this.cols = cols;
		dropDown.setNoColumns(cols);
	}

	public abstract String getDefaultAddNewCaption();

	public abstract void onAddNew();

	public void changeValue(int rowIndex) {
		// int index = listbox.getSelectedIndex();

		IAccounterComboSelectionChangeHandler<T> handler = getHandler();
		if (rowIndex > 0)
			selectedObject = comboItems.get(rowIndex
					- (isAddNewRequire ? 1 : 0));
		switch (rowIndex) {
		case -1:
			if (popup.isShowing())
				popup.hide();
			setSelectedItem(selectedObject, rowIndex);
			break;
		case 0:
			if (isAddNewRequire) {
				if (popup.isShowing())
					popup.hide();
				setValue(getDefaultAddNewCaption());
				onAddNew();
			} else {
				selectedObject = comboItems.get(rowIndex
						- (isAddNewRequire ? 1 : 0));
				if (handler != null) {
					handler.selectedComboBoxItem(selectedObject);
				}
				setSelectedItem(selectedObject, rowIndex);
				if (popup.isShowing())
					popup.hide();
			}

			break;

		default:

			if (handler != null) {
				selectedObject = comboItems.get(rowIndex
						- (isAddNewRequire ? 1 : 0));
				handler.selectedComboBoxItem(selectedObject);
				setSelectedItem(selectedObject, rowIndex);
				if (popup.isShowing())
					popup.hide();

			}

			break;
		}

		if (handler == null) {
			if (popup.isShowing())
				popup.hide();
			setSelectedItem(selectedObject, rowIndex);
		}
		if (grid != null) {
			grid.remove(getMainWidget());
			setSelectedItem(selectedObject, 0);
			if (popup.isShowing())
				popup.hide();
		}
		selectedIndex = -1;
	}

	protected void setSelectedItem(T obj, int row) {

		if (obj == null) {
			setValue("");
			return;
		}

		String displayName = "";
		if (cols == 0) {
			displayName = getDisplayName(obj);
		} else {
			for (int i = 0; i < cols; i++) {
				displayName += getColumnData(obj, row, i);
				if (i < cols - 1)
					displayName += " - ";
			}
		}
		setValue(displayName);
	}

	public void setGrid(ListGrid grid) {
		this.grid = grid;
	}

	public void removeComboItem(T coreObject) {

		int index = getObjectIndex(coreObject);
		if (index >= 0) {
			T selectedValue = getSelectedValue();
			comboItems.remove(index);
			dropDown.removeRow(isAddNewRequire ? index + 1 : index + 0);

			if (!comboItems.contains(selectedValue)) {
				// select the first one
				try {
					setSelectedItem(comboItems.get(0), isAddNewRequire ? 1 : 0);
				} catch (ArrayIndexOutOfBoundsException e) {
					setSelectedItem(null, 0); // row optional here.
				}
			}

		}

	}

	protected int getObjectIndex(T coreObject) {
		return comboItems.indexOf(coreObject);
	}

	public void updateComboItem(T coreObject) {
		if (getObjectIndex(coreObject) != -1) {
			removeComboItem(coreObject);
			addComboItem(coreObject);
		}
	}

	public T getSelectedValue() {
		return selectedObject;
	}

	@Override
	public void setDisabled(boolean b) {
		if (b)
			this.getMainWidget().addStyleName("dropdown-disabled");
		else
			this.getMainWidget().removeStyleName("dropdown-disabled");
		super.setDisabled(b);
	}

	public void setWidth(int width) {
		this.getMainWidget().setWidth(width + "%");
	}

	public void setPopupWidth(String width) {
		// this.panel.setWidth(width);
	}

	protected void onKeyEnter(char key) {
		filterValues(key);
	}

	private void filterValues(char key) {

		String val = getValue() != null ? getValue().toString()
				+ String.valueOf(key).replace("/", "").trim() : String
				.valueOf(key).replace("/", "").trim();

		resetComboList();
		if (key == '/') {
			if (val.replace("/", "").trim().isEmpty()) {
				showPopup();
				return;
			}
		}

		final String val1 = val.toLowerCase();
		List<T> autocompleteItems = getMatchedComboItems(val);

		updateComboItemsInSorted(autocompleteItems, val1);

		maincomboItems.addAll(comboItems);
		initCombo(autocompleteItems);

		showPopup();

	}

	// protected List<T> getComboitemsByName(String value) {
	// List<T> autocompleteItems = new ArrayList<T>();
	// for (T t : comboItems) {
	// if (getDisplayName(t).toLowerCase().contains(value.toLowerCase())) {
	// autocompleteItems.add(t);
	// }
	// }
	// return autocompleteItems;
	// }

	// private void addBlurHandler() {
	// addBlurHandler(new BlurHandler() {
	//
	// @Override
	// public void onBlur(BlurEvent event) {
	// if (!getValue().toString().isEmpty() && !popup.isShowing())
	// setRelatedComboItem(getValue().toString());
	// }
	// });
	// }

	private void setRelatedComboItem(final String value) {
		int index = 0;
		if (!getValue().toString().isEmpty()) {
			List<T> combos = getComboitemsByName(value);
			for (T t : combos) {
				String name = getDisplayName(t);
				if (name.equalsIgnoreCase(value)) {
					combos.clear();
					combos.add(t);

					break;
				}
			}
			updateComboItemsInSorted(combos, value);
			if (combos != null && combos.size() > 0)
				index = comboItems.indexOf(combos.get(0))
						+ (isAddNewRequire ? 1 : 0);
		} else {
			index = -1;
		}
		selectedName = value;

		if (isAddNewRequire && index == 0) {
			selectionFaildOnClose();
		} else {
			changeValue(index);
		}
	}

	/**
	 * this will be called when {@link #isAddNewRequire} value is true, and
	 * noting has been selected for the entered text on close.
	 */
	protected void selectionFaildOnClose() {
		// implement as you required at child
	}

	private void updateComboItemsInSorted(List<T> comboObjects,
			final String value) {
		Collections.sort(comboObjects, new Comparator<T>() {

			@Override
			public int compare(T obj1, T obj2) {
				String name = getDisplayName(obj1).toLowerCase();
				String name1 = getDisplayName(obj2).toLowerCase();

				if (name.indexOf(value) == name1.indexOf(value))
					return 0;
				if (name.indexOf(value) < name1.indexOf(value))
					return -1;
				if (name.indexOf(value) > name1.indexOf(value))
					return 1;

				return 0;
			}
		});
	}

	private List<T> getMatchedComboItems(String val) {
		List<T> autocompleteItems = new ArrayList<T>();
		for (T t : comboItems) {
			String displayName = getDisplayName(t);
			displayName = displayName.toLowerCase();
			if (displayName.indexOf(val) == 0) {
				autocompleteItems.add(t);
			}
		}
		return autocompleteItems;
	}

	// protected void onKeyEnter(char key) {
	// filterValues(key);
	// }
	//
	// private void filterValues(char key) {
	//
	// String val = getValue() != null ? getValue().toString()
	// + String.valueOf(key).replace("/", "").trim() : String.valueOf(
	// key).replace("/", "").trim();
	//
	// resetComboList();
	// if (key == '/') {
	// if (val.replace("/", "").trim().isEmpty()) {
	// showPopup();
	// return;
	// }
	// }

	// final String val1 = val.toLowerCase();
	// List<T> autocompleteItems = getComboitemsByName(val);
	//
	// updateComboItemsInSorted(autocompleteItems, val1);
	//
	// maincomboItems.addAll(comboItems);
	// initCombo(autocompleteItems);
	//
	// showPopup();

	// }

	protected List<T> getComboitemsByName(String value) {
		List<T> autocompleteItems = new ArrayList<T>();
		for (T t : comboItems) {
			String displayName = getDisplayName(t);
			if (displayName.equalsIgnoreCase(value)) {
				autocompleteItems.add(t);
			}
		}
		return autocompleteItems;
	}

	// private void addBlurHandler() {
	// addBlurHandler(new BlurHandler() {
	//
	// @Override
	// public void onBlur(BlurEvent event) {
	// if (!getValue().toString().isEmpty() && !popup.isShowing())
	// setRelatedComboItem(getValue().toString());
	// }
	// });
	// }

	// private void setRelatedComboItem(final String value) {
	// int index = 0;
	// if (!getValue().toString().isEmpty()) {
	// List<T> combos = getComboitemsByName(value);
	// for (T t : combos) {
	// String name;
	// if (t instanceof ClientVATCode)
	// name = getOnlyName(t);
	// else
	// name = getDisplayName(t);
	// if (name.toLowerCase().equals(value.toLowerCase())) {
	// combos.clear();
	// combos.add(t);
	//
	// break;
	// }
	// }
	// updateComboItemsInSorted(combos, value);
	// if (combos != null && combos.size() > 0)
	// index = comboItems.indexOf(combos.get(0))
	// + (isAddNewRequire ? 2 : 1);
	// }
	// selectedName = value;
	// changeValue(index);
	// }

	// private void updateComboItemsInSorted(List<T> comboObjects,
	// final String value) {
	// Collections.sort(comboObjects, new Comparator<T>() {
	//
	// @Override
	// public int compare(T obj1, T obj2) {
	// String name = getDisplayName(obj1).toLowerCase();
	// String name1 = getDisplayName(obj2).toLowerCase();
	//
	// if (name.indexOf(value) == name1.indexOf(value))
	// return 0;
	// if (name.indexOf(value) < name1.indexOf(value))
	// return -1;
	// if (name.indexOf(value) > name1.indexOf(value))
	// return 1;
	//
	// return 0;
	// }
	// });
	// }

	private String getDisplayNameForAccountVatCode(T obj) {
		String displayName = "";
		for (int i = 0; i < cols; i++) {
			displayName += getColumnData(obj, 0, i);
			if (i < cols - 1)
				displayName += " - ";
		}
		return displayName;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		// addDiv();
	}

	protected void onAddAllInfo(String text) {

	}

	protected T getQuickAddData(String text) {
		return null;
	}
}
