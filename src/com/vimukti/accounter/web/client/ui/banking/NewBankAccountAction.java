package com.vimukti.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.NewAccountView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

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
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				NewAccountView view = new NewAccountView();
				// view.setNewBankAccountAction(NewBankAccountAction.this);
				view.setNewBankAccount(true);
				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewBankAccountAction.this);

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
