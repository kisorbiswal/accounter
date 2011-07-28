package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class AdjustTAXAction extends Action {
	protected AdjustTAXView view;

	public AdjustTAXAction(String text) {
		super(text);
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			this.catagory = Accounter.constants().company();
		else
			this.catagory = Accounter.constants().VAT();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vatAdjustment();
	}

	
//	@Override
//	public ParentCanvas getView() {
//		return null;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

			}

			public void onCreated() {
				try {
					if (isDependent) {
						ClientTAXAgency taxAgency = (ClientTAXAgency) data;
						view = new AdjustTAXView(taxAgency);
						MainFinanceWindow.getViewManager().showView(view, null,
								isDependent, AdjustTAXAction.this);
					} else {
						view = new AdjustTAXView();
						MainFinanceWindow.getViewManager().showView(view, data,
								isDependent, AdjustTAXAction.this);
					}

				} catch (Throwable e) {
					onCreateFailed(e);

				}
			}
		});

	}

//	@Override
//	public String getImageUrl() {
//		return "/images/Vat_adjustment.png";
//	}

	@Override
	public String getHistoryToken() {
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			return "vatAdjustment";
		else
			return "taxAdjustment";
	}

}
