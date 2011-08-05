package com.vimukti.accounter.web.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.theme.ThemesUtil;

public class ImageButton extends Button {

	private ImageResource res;

	public ImageButton(String title, ImageResource res) {
		this.setText(title);
		this.res = res;
	}

	/**
	 * Should be called only after adding to view
	 * 
	 * @param res
	 */
	private void setImage(ImageResource res) {
		Element closeseparator = DOM.createSpan();
		closeseparator.addClassName("button-separator");
		DOM.appendChild(this.getElement(), closeseparator);

		Element closeimage = DOM.createSpan();
		closeimage.addClassName("button-image");
		DOM.appendChild(this.getElement(), closeimage);

		ThemesUtil.addDivToButton(this, res, "image-button-right-image");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		setImage(res);
	}

}
