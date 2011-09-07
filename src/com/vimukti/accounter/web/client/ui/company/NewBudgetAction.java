package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewBudgetAction extends Action<ClientBudget> {

	NewBudgetView view;
	boolean edit;

	public NewBudgetAction(String text) {
		super(text);
		this.catagory = Global.get().constants().budget();
		super.setToolTip(Global.get().constants().budget());
	}

	public NewBudgetAction(String text, ClientBudget budget,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Global.get().constants().budget();
	}

	public void run() {

		if (listData != null) {
			runAsync(listData);
		} else {
			runAsync(data, isDependent);
		}
	}

	private void runAsync(final List<ClientBudget> listData) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new NewBudgetView(listData);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewBudgetAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});

	}

	// private void runAsync(final boolean isEdit, final Object data) {
	//
	// GWT.runAsync(new RunAsyncCallback() {
	//
	// @Override
	// public void onSuccess() {
	// view = new NewBudgetView(isEdit, data);
	// view.onEdit();
	// MainFinanceWindow.getViewManager().showView(view, data,
	// isDependent, NewBudgetAction.this);
	//
	// }
	//
	// @Override
	// public void onFailure(Throwable arg0) {
	// Accounter
	// .showError(Accounter.constants().unableToshowtheview());
	//
	// }
	// });
	//
	// }

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new NewBudgetView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewBudgetAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_customer.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newBudget";
	}

	@Override
	public String getHelpToken() {
		return "add-Budget";
	}

	public void runEdit(Object budgetItem) {
		edit = true;
		// runAsync(edit, budgetItem);

	}
}
