package com.vimukti.accounter.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.ui.core.Action;

public class InventoryAssemblyAction extends Action<ClientInventoryAssembly> {

	private String itemname;
	private boolean forCustomer;
	private boolean isItemEditable;

	public InventoryAssemblyAction() {
		super();
		this.catagory = messages.company();
	}

	public InventoryAssemblyAction(boolean forCustomer) {
		super();
		this.forCustomer = forCustomer;
	}

	@Override
	public String getText() {
		return messages.inventoryAssembly();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientInventoryAssembly data,
			final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				InventoryAssemblyView view = GWT
						.create(InventoryAssemblyView.class);
				view.setItemName(itemname);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, InventoryAssemblyAction.this);
				if (isItemEditable) {
					view.onEdit();
				}

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		// }
		// });
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCatagory() {
		if (forCustomer) {
			return Global.get().Customer();
		} else {
			return Global.get().Vendor();
		}
	}

	@Override
	public String getHistoryToken() {
		return "inventoryassembly";
	}

	@Override
	public String getHelpToken() {
		return "inventoryassembly";
	}

	public void setItemText(String itemname) {
		this.itemname = itemname;
	}

	public void setisItemEditable(boolean isItemEditable) {
		this.isItemEditable = isItemEditable;
	}

}
