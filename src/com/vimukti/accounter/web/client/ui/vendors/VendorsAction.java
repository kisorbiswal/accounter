package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class VendorsAction extends Action {

	public VendorsAction(String text) {
		super(text);
	}

	public VendorsAction(String text, String iconString) {
		super(text, iconString);
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	public ImageResource getBigImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	public ImageResource getSmallImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public String getImageUrl() {
		return "";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
