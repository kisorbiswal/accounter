package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ItemView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SelectItemTypeDialog;
import com.vimukti.accounter.web.client.ui.combo.ServiceCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal modified by Rajesh.A
 */

public class NewItemAction extends Action {

	
	private boolean isEdit;
	private ClientItem item;
	int type;
	private boolean isGeneratedFromCustomer;
	public static final int TYPE_SERVICE = 1;
	public static final int NON_INVENTORY_PART = 3;

	public NewItemAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().company();
	}

	public NewItemAction(String text, String iconString,
			boolean isGeneratedFromCustomer) {
		super(text, iconString);
		this.catagory = Accounter.constants().company();
		this.isGeneratedFromCustomer = isGeneratedFromCustomer;
		// this.baseView = baseView;
	}

	public NewItemAction(String text, String iconString, ClientItem item,
			AsyncCallback<Object> callback, boolean isGeneratedFromCustomer) {
		super(text, iconString, item, callback);
		this.catagory = Accounter.constants().company();
		this.isGeneratedFromCustomer = isGeneratedFromCustomer;
		// this.baseView = baseView;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Item", t);

			}

			public void onCreated() {

				try {

					if (!isDependent) {

						new SelectItemTypeDialog(NewItemAction.this,
								isGeneratedFromCustomer).show();

					} else {
						if (data == null) {
							item = null;
							if (getActionSource() != null) {
								if (getActionSource() instanceof ServiceCombo)
									type = TYPE_SERVICE;
								else
									type = NON_INVENTORY_PART;
							}
							// else
							// type = TYPE_SERVICE;
						} else {
							item = (ClientItem) data;
							type = item.getType();
						}

						ItemView view = new ItemView(item, type,
								isGeneratedFromCustomer);
						MainFinanceWindow.getViewManager().showView(view, data,
								isDependent, NewItemAction.this);

						/*
						 * UIUtils.setCanvas(new ItemView(item, type),
						 * getViewConfiguration());
						 */
					}

				} catch (Throwable e) {
					onCreateFailed(e);

				}
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
		return Accounter.getFinanceMenuImages().newItem();
	}

	@Override
	public String getImageUrl() {
		return "/images/new_item.png";
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String getHistoryToken() {
		return "newItem";
	}
}
