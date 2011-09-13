package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;

public class ImageButton extends Button {

	private ImageResource res;

	public ImageButton(ImageResource res) {
		this.res = res;
		setStyleName("Empty-text");
	}

	public ImageButton(String title, ImageResource res) {
		super(title);
		this.res = res;
		setStyleName("image-button");
	}

	public ImageButton(String title, ImageResource res, ClickHandler handler) {
		super(title, handler);
		this.res = res;
	}

	/**
	 * Should be called only after adding to view
	 * 
	 * @param res
	 */
	private void setImage(ImageResource res) {
		// Element closeseparator = DOM.createSpan();
		// closeseparator.addClassName("button-separator");
		// closeseparator.setInnerText("|");
		// DOM.appendChild(this.getElement(), closeseparator);

		// Element closeimage = DOM.createSpan();
		// closeimage.addClassName("button-image");
		// DOM.appendChild(this.getElement(), closeimage);
		//
		// ThemesUtil.addDivToButton(this, res, "image-button-right-image");
		this.getElement().getStyle()
				.setBackgroundImage("url(" + res.getURL() + ")");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		setImage(res);
	}
	
	@Override
	public void setTabIndex(int index) {
		super.setTabIndex(index);
	}

	
	

}
