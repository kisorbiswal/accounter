package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class EmployeesAction extends Action {

	public EmployeesAction(String text) {
		super(text);
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return null;
	// }

	@Override
	public void run() {

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "employee";
	}

}
