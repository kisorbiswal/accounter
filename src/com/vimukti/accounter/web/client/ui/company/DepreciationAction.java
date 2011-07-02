package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class DepreciationAction extends Action {

	private DepreciationView view;

	public DepreciationAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	public DepreciationAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getFixedAssetConstants()
		.fixedAssets();
	}

	public DepreciationAction(String text, String iconString,
			Object editableObject, AsyncCallback<Object> callbackObject) {
		super(text, iconString, editableObject, callbackObject);
		this.catagory = FinanceApplication.getFixedAssetConstants()
		.fixedAssets();
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return FinanceApplication.getFinanceMenuImages().Depreciation();
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
			Accounter.showError(FinanceApplication.getCompanyMessages()
					.failedToLoadCompanyHome());
		}
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "depreciation";
	}

}
