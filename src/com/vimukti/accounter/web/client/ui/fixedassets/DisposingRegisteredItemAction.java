package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class DisposingRegisteredItemAction extends Action {
	private DisposingRegisteredItemView view;;

	public DisposingRegisteredItemAction(String text) {
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
		// NOTHING TO DO
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new DisposingRegisteredItemView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, DisposingRegisteredItemAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				Accounter.showError(Accounter.constants()
						.failedToLoadDisposedFixedAssetView());
			}
		});

	}

	@Override
	public String getHistoryToken() {
		return "disposingRegisteredItem";
	}

}
