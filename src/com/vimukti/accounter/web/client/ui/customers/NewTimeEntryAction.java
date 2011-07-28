package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class NewTimeEntryAction extends Action {

	public NewTimeEntryAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	public NewTimeEntryAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().customer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		// TODO Auto-generated method stub

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "newTimeEntry";
	}

}
