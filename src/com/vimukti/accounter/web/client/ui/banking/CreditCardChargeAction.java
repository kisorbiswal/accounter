package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreditCardChargeView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class CreditCardChargeAction extends Action {

	// private boolean isEdit;
	// private ClientCreditCardCharge creditCardCharge;
	protected CreditCardChargeView view;

	public CreditCardChargeAction() {
		super();
		this.catagory = Accounter.messages().banking();

	}

	public CreditCardChargeAction(ClientCreditCardCharge creditCardCharge,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = Accounter.messages().banking();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = new CreditCardChargeView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, CreditCardChargeAction.this);

			}

			public void onCreateFailed(Throwable t) {
			}
		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCreditCardCharge();
	}

	@Override
	public String getHistoryToken() {
		return "creditCardCharge";
	}

	@Override
	public String getHelpToken() {
		return "newcreditcardcharge";
	}

	@Override
	public String getText() {
		return messages.creditCardCharge();
	}
}
