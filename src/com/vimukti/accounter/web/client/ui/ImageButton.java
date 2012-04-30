package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class ImageButton extends Button {

	protected static AccounterMessages messages = Global.get().messages();
	private ImageResource res;

	public ImageButton(ImageResource res) {
		setImage(res);
		setStyleName("Empty-text");
	}

	public ImageButton(String title, ImageResource res) {
		super(title);
		setImage(res);
		setStyleName("image-button");
	}

	public ImageButton(String title, ImageResource res, ClickHandler handler) {
		super(title, handler);
		setImage(res);
	}

	/**
	 * Should be called only after adding to view
	 * 
	 * @param res
	 */
	private void setImage(ImageResource res) {
		this.res = res;
		if (res == null) {
			return;
		}
		Image image = new Image(res);
		getElement().insertFirst(image.getElement());
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		setImage(res);
	}

	@Override
	public void setHTML(String html) {
		super.setHTML(html);
		setImage(res);
	}

	@Override
	public void setTabIndex(int index) {
		super.setTabIndex(index);
	}

}
