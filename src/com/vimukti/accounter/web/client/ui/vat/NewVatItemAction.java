package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewVatItemAction extends Action {

	protected NewVATItemView view;

	public NewVatItemAction(String text) {
		super(text);
		String flag;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			flag = Accounter.constants().company();
		else
			flag = Accounter.constants().VAT();
		this.catagory = flag;
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVatItem();
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

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed To Load Account", t);
			}

			public void onCreated() {
				try {

					view = new NewVATItemView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewVatItemAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);

				}
			}
		});

	}

	@Override
	public String getImageUrl() {
		return "/images/Vat_item.png";
	}

	@Override
	public String getHistoryToken() {
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			return "newVatItem";
		else
			return "newTaxItem";
	}
}
