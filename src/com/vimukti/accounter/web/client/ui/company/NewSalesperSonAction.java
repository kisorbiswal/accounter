package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * @modified by Ravi Kiran.G
 * 
 */
public class NewSalesperSonAction extends Action {

	
	private ClientSalesPerson salesPerson;
	
	private boolean isEdit;

	public NewSalesperSonAction(String icon) {
		super(Accounter.constants().newSalesPerson(), icon);
		this.catagory = Accounter.constants().company();
	}

	public NewSalesperSonAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					NewSalesPersonView view = NewSalesPersonView.getInstance();

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewSalesperSonAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Sales Person...", t);
			}
		});

	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "newSalesPerson";
	}

}
