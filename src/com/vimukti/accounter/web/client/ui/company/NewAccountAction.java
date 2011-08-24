package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.NewAccountView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewAccountAction extends Action<ClientAccount> {

	private List<Integer> accountTypes;
	// private AbstractBaseView<?> baseView;
	protected NewAccountView view;

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
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new NewAccountView();
				view.setAccountTypes(getAccountTypes());

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewAccountAction.this);

			}

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
}
