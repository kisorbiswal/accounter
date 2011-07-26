package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ImageItem extends FormItem {

	private Image image;

	public ImageItem() {
	}

	@Override
	public Widget getMainWidget() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public void addClickHandler(ClickHandler handler) {
		this.image.addClickHandler(handler);

	}
}
