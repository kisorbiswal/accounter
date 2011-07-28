package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class VatGroupAction extends Action {

	private VATGroupView view;

	public VatGroupAction(String text) {
		super(text);
		this.catagory = Accounter.constants().VAT();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return Accounter.getFinanceMenuImages().vatGroup();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// NOTHING TO DO.
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

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public String getImageUrl() {
		return "/images/Vat_group.png";
	}

	@Override
	public String getHistoryToken() {
		return "vatGroup";
	}

}
