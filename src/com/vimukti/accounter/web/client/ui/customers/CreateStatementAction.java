package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class CreateStatementAction extends Action {

	
	private boolean isEdit;

	public CreateStatementAction(String text) {
		super(text);
	}

	public CreateStatementAction(String text, String iconString) {
		super(text, iconString);
	}

//	
//	@Override
//	public ParentCanvas getView() {
//		// its not using any where
//		return null;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		Accounter.showError("Not yet Implemented.........!");

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "CreateStatement";
	}

}
