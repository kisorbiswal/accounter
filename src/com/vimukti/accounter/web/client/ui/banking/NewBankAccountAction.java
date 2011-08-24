package com.vimukti.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.NewAccountView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewBankAccountAction extends Action<ClientBankAccount> {
	private List<Integer> accountTypes;

	public NewBankAccountAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				NewAccountView view = new NewAccountView();
				// view.setNewBankAccountAction(NewBankAccountAction.this);
				view.setNewBankAccount(true);
				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewBankAccountAction.this);

			}

//			@Override
//			public void onCreateFailed(Throwable t) {
//				// TODO Auto-generated method stub
//				
//			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	public List<Integer> getAccountTypes() {
		return accountTypes;
	}

	public void setAccountTypes(List<Integer> accountTypes) {
		this.accountTypes = accountTypes;
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newBankAccount();
	}

	@Override
	public String getHistoryToken() {
		return "newBankAccount";
	}

	@Override
	public String getHelpToken() {
		return "new-bank-account";
	}
}
