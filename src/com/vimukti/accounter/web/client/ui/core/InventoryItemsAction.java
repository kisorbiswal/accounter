package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;

public class InventoryItemsAction extends Action {

	public InventoryItemsAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {

		return null;
	}

	@Override
	public void run() {

	}

	@Override
	public String getHistoryToken() {

		return null;
	}

	@Override
	public String getHelpToken() {
		return "inventory-item";
	}

}
