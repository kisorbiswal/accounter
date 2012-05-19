package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ManageItemTaxAction extends Action {

	public ManageItemTaxAction(String text) {
		super();
		this.catagory = messages.company();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				
				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//			}
//		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "manageItemTaxAction";
	}

	@Override
	public String getHelpToken() {
		return "item-tax";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}
}
