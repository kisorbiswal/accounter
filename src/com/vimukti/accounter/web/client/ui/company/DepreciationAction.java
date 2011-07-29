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

	// @Override
	// public String getImageUrl() {
	// return "/images/Depreciation.png";
	// }
	//
	//
	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new DepreciationView();

		try {
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		} catch (Exception e) {
			Accounter
					.showError(Accounter.constants().failedToLoadCompanyHome());
		}
	}

	@Override
	public String getHistoryToken() {
		return "depreciation";
	}

}
