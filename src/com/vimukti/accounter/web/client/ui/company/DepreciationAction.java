package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class DepreciationAction extends Action {

	private DepreciationView view;

	public DepreciationAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	public DepreciationAction(String text, Object editableObject,
			AccounterAsyncCallback<Object> callbackObject) {
		super(text);
		this.catagory = Accounter.constants().fixedAssets();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().Depreciation();
	}

	@Override
	public void run() {
		view = new DepreciationView();

		MainFinanceWindow.getViewManager().showView(view, null, false, this);
	}

	@Override
	public String getHistoryToken() {
		return "depreciation";
	}

	@Override
	public String getHelpToken() {
		return "depreciation";
	}

}
