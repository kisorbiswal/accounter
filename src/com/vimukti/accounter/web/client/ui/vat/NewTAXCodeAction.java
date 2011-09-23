package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * @author Murali.A
 * 
 */
public class NewTAXCodeAction extends Action<ClientTAXCode> {

	private NewTAXCodeView view;

	public NewTAXCodeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().tax();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().salesTaxCode();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				view = new NewTAXCodeView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewTAXCodeAction.this);

			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Sales_Tax.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newVatCode";
	}

	@Override
	public String getHelpToken() {
		return "new-tax-code";
	}

}
