package com.vimukti.accounter.web.client.ui.translation;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class TranslationAction extends Action {
	private TranslationView view;

	public TranslationAction() {
		super();
		this.catagory = messages.settings();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "translation";
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new TranslationView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, TranslationAction.this);
			}
		});
	}

	@Override
	public String getText() {
		return messages.translation();
	}

}
