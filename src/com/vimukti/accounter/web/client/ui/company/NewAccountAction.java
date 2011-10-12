package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.NewAccountView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewAccountAction extends Action<ClientAccount> {

	private List<Integer> accountTypes;
	// private AbstractBaseView<?> baseView;
	protected NewAccountView view;
	private String accountName;

	public NewAccountAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	// public void setBaseCanvas(AbstractBaseView<?> baseView) {
	// this.baseView = baseView;
	//
	// }

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new NewAccountView();
				view.setAccountTypes(getAccountTypes());
				view.setAccountName(accountName);

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewAccountAction.this);

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
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newAccount();
	}

	@Override
	public String getHistoryToken() {
		return "newAccount";
	}

	@Override
	public String getHelpToken() {
		return "new-account";
	}

	public void setAccountName(String text) {
		this.accountName = text;
	}
}
