package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class SellingRegisteredItemAction extends Action {
	private SellingRegisteredItemView view;

	public SellingRegisteredItemAction(String text) {
		super(text);
		this.catagory = Accounter.getFixedAssetConstants()
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
		return null;
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

					view = new SellingRegisteredItemView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, SellingRegisteredItemAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				Accounter.showError(Accounter.getFixedAssetConstants()
						.failedToLoadSellFixedAssetView());
			}
		});

	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}
}
