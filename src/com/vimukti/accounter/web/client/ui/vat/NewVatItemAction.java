package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewVatItemAction extends Action<ClientTAXItem> {

	protected NewVATItemView view;

	public NewVatItemAction(String text) {
		super(text);
		this.catagory = Accounter.constants().tax();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVatItem();
	}

	// @Override
	// public ParentCanvas getView() {
	// return view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new NewVATItemView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewVatItemAction.this);

			}

			@Override
			public void onFailure(Throwable reason) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Vat_item.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newTaxItem";
	}

	@Override
	public String getHelpToken() {
		return "new-vat-item";
	}
}
