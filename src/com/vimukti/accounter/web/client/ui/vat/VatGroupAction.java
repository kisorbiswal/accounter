package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class VatGroupAction extends Action {

	private VATGroupView view;

	public VatGroupAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getVATMessages().VAT();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().vatGroup();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed To Load Account", t);
			}

			public void onCreated() {
				try {

					view = new VATGroupView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, VatGroupAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);

				}
			}
		});

	}
	
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/Vat_group.png";
	}

}
