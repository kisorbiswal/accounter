package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddEditSalesTaxCodeView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AddEditSalesTaxCodeAction extends Action {

	protected AddEditSalesTaxCodeView view;
	private String title;

	public AddEditSalesTaxCodeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
		title = Accounter.constants().taxCode();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new AddEditSalesTaxCodeView(title);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, AddEditSalesTaxCodeAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
		if (Accounter.getCompany().getPreferences().isRegisteredForVAT())
			return "newVatCode";
		else
			return "newTaxCode";
	}

	@Override
	public String getHelpToken() {
		return "sales_tax-code";
	}

}
