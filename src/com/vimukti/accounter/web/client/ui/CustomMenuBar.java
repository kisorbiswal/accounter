package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.vimukti.accounter.web.client.images.FinanceMenuImages;
import com.vimukti.accounter.web.client.ui.core.Action;

public class CustomMenuBar extends MenuBar {
	List<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();

	public CustomMenuBar() {
		super(true);
		setAutoOpen(false);
	}

	public CustomMenuBar(Resources resource) {
		super(true, resource);
	}

	public CustomMenuBar(FinanceMenuImages resource) {
		super(true, resource);
	}

	@SuppressWarnings("deprecation")
	public CustomMenuBar(MenuBarImages resource) {
		super(true, resource);
	}

	public void addItem(final Action action) {
		Command cmd = new Command() {

			@Override
			public void execute() {
				action.run(null, false);

			}
		};
		CustomMenuItem menuItem = new CustomMenuItem(action.getText(), cmd);
		this.menuItems.add(menuItem);
		super.addItem(menuItem);
		menuItem.setIcon(action.getSmallImage());
		// url("images/previous.png") no-repeat scroll 0 0 transparent
		// menuItem.getElement().getStyle().setProperty(
		// "background",
		// "url(" + action.getImageUrl()
		// + ") no-repeat scroll 0 0 transparent");
	}

	// @SuppressWarnings("unused")
	// protected CustomMenuItem findItem(Element hItem) {
	// for (CustomMenuItem item : menuItems) {
	// if (DOM.isOrHasChild(item.getElement(), hItem)) {
	// return item;
	// }
	// }
	// return null;
	// }
	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOUT:
			Element element = event.getTarget();
			if (CustomMenuBar.this.getElement().equals(element)) {
				CustomMenuBar.this.removeFromParent();
			}
			break;
		default:
			break;
		}
		super.onBrowserEvent(event);
	}

}
