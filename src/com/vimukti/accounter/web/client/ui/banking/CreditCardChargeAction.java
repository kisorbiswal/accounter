package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreditCardChargeView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CreditCardChargeAction extends Action {

	// private boolean isEdit;
	// private ClientCreditCardCharge creditCardCharge;
	protected CreditCardChargeView view;

	public CreditCardChargeAction(String text, String iconString) {
		super(Accounter.constants().newCreditCardCharge(),
				text);
		this.catagory = Accounter.constants().banking();

	}

	public CreditCardChargeAction(String text, String iconString,
			ClientCreditCardCharge creditCardCharge,
			AsyncCallback<Object> callback) {
		super(text, iconString, creditCardCharge, callback);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					view = CreditCardChargeView.getInstance();

					MainFinanceWindow.getViewManager().showView(view, data,
							isEditable, CreditCardChargeAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Credit Card Charge.....",
				// t);
			}
		});
	}

	
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCreditCardCharge();
	}

	@Override
	public String getImageUrl() {
		
		return "/images/credit_card_charge.png";
	}

	@Override
	public String getHistoryToken() {
		return "creditCardCharge";
	}
}
