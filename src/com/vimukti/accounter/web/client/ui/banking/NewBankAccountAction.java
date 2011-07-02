package com.vimukti.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.NewAccountView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewBankAccountAction extends Action {
	private List<Integer> accountTypes;

	public NewBankAccountAction(String text, String iconString) {

		super(text, iconString);
		this.catagory = FinanceApplication.getBankingsMessages().banking();

	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Bank Account", t);

			}

			public void onCreated() {
				try {
					NewAccountView view = new NewAccountView();
					// view.setNewBankAccountAction(NewBankAccountAction.this);
					view.setNewBankAccount(true);
					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewBankAccountAction.this);
				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}
		});

	}

	public List<Integer> getAccountTypes() {
		return accountTypes;
	}

	public void setAccountTypes(List<Integer> accountTypes) {
		this.accountTypes = accountTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().newBankAccount();
	}
	@Override
	public String getImageUrl() {
		
		return "/images/new_bank_account.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "newBankAccount";
	}
}
