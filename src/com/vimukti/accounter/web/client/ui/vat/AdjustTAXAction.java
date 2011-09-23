package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class AdjustTAXAction extends Action {
	protected AdjustTAXView view;
	private ClientTAXAgency vatAgency;

	public AdjustTAXAction(String text) {
		super(text);
		this.catagory = Accounter.constants().tax();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vatAdjustment();
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				if (isDependent) {
					view = new AdjustTAXView(vatAgency);
					MainFinanceWindow.getViewManager().showView(view, null,
							isDependent, AdjustTAXAction.this);
				} else {
					view = new AdjustTAXView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, AdjustTAXAction.this);
				}

			}
		});

	}

	public void setVatAgency(ClientTAXAgency selectedVatAgency) {
		this.vatAgency = selectedVatAgency;
	}

	@Override
	public String getHistoryToken() {
		return "taxAdjustment";
	}

	@Override
	public String getHelpToken() {
		return "adjust-tax";
	}

}
