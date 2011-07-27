package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class DepreciationAction extends Action {

	private DepreciationView view;

	public DepreciationAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public DepreciationAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getFixedAssetConstants().fixedAssets();
	}

	public DepreciationAction(String text, String iconString,
			Object editableObject, AsyncCallback<Object> callbackObject) {
		super(text, iconString, editableObject, callbackObject);
		this.catagory = Accounter.getFixedAssetConstants().fixedAssets();
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
	public String getImageUrl() {
		return "/images/Depreciation.png";
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new DepreciationView();

		try {
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		} catch (Exception e) {
			Accounter.showError(Accounter.getCompanyMessages()
					.failedToLoadCompanyHome());
		}
	}

	@Override
	public String getHistoryToken() {
		return "depreciation";
	}

}
