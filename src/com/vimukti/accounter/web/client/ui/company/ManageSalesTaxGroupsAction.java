package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SalesTaxGroupListView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ManageSalesTaxGroupsAction extends Action {

	SalesTaxGroupListView view;

	public ManageSalesTaxGroupsAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public ManageSalesTaxGroupsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Sales Tax Code Dialog",
				// t);

			}

			public void onCreated() {
				try {

					// SalesTaxGroupListDialog dialog = new
					// SalesTaxGroupListDialog(
					// FinanceApplication.getCompanyMessages()
					// .manageSalesTaxGroup(), FinanceApplication
					// .getCompanyMessages().toAddTaxGroup());
					// ViewManager viewManager = ViewManager.getInstance();
					// viewManager.setCurrentDialog(dialog);
					view = new SalesTaxGroupListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ManageSalesTaxGroupsAction.this);
					// dialog.addCallBack(getViewConfiguration().getCallback());
					// dialog.show();

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
		return Accounter.getFinanceMenuImages().manageSalesTaxGroup();
	}

	@Override
	public String getImageUrl() {
		return "/images/Manage_Sales_Tax_Group.png";
	}

	@Override
	public String getHistoryToken() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return "manageSalesTaxGroups";
		else
			return "salesTaxGroups";

	}

}
