package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class VatGroupAction extends Action {

	private VATGroupView view;

	public VatGroupAction(String text) {
		super(text);
		this.catagory = Accounter.constants().vat();
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

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = new VATGroupView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, VatGroupAction.this);

			}
		});

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	// @Override
	// public String getImageUrl() {
	// return "/images/Vat_group.png";
	// }

	@Override
	public String getHistoryToken() {
		return "vatGroup";
	}

	@Override
	public String getHelpToken() {
		return "vat-group";
	}

}
