package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.NewAccountView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewAccountAction extends Action {

	private List<Integer> accountTypes;
	// private AbstractBaseView<?> baseView;
	protected NewAccountView view;

	public NewAccountAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public NewAccountAction(String text, String iconString) {

		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();

	}

	// public void setBaseCanvas(AbstractBaseView<?> baseView) {
	// this.baseView = baseView;
	//
	// }

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Account", t);

			}

			public void onCreated() {
				try {

					view = new NewAccountView();
					view.setAccountTypes(getAccountTypes());

					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewAccountAction.this);

					// UIUtils.setCanvas(new NewAccountView(),
					// getViewConfiguration());

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

	@Override
	public ParentCanvas<?> getView() {
		return view;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newAccount();
	}

	@Override
	public String getImageUrl() {
		return "/images/new_account.png";
	}

	@Override
	public String getHistoryToken() {
		return "newAccount";
	}
}
