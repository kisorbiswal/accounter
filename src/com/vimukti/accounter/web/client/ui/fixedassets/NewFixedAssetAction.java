package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewFixedAssetAction extends Action {

	private NewFixedAssetView view;

	public NewFixedAssetAction(String text) {
		super(text);
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
		return FinanceApplication.getFinanceMenuImages().newFixedAsset();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new NewFixedAssetView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewFixedAssetAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor View..", t);
			}
		});

	}

	@Override
	public String getImageUrl() {
		return "/images/New Fixed Asset.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "newFixedAsset";
	}

}
