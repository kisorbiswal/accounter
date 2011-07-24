package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.AddEditSalesTaxCodeView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class AddEditSalesTaxCodeAction extends Action {

	protected AddEditSalesTaxCodeView view;
	private String title;

	public AddEditSalesTaxCodeAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
		title = Accounter.getCompanyMessages().taxCode();
	}

	public AddEditSalesTaxCodeAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
		title = Accounter.getCompanyMessages().taxCode();
	}

	@Override
	public ParentCanvas<?> getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load "+title+" Code Dialog",
				// t);

			}

			public void onCreated() {
				try {
					view = new AddEditSalesTaxCodeView(title);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, AddEditSalesTaxCodeAction.this);
				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}
		});

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			return "newVatCode";
		else
			return "newTaxCode";
	}

}
