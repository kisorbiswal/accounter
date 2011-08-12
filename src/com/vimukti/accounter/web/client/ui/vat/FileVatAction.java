package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class FileVatAction extends Action {

	public FileVatAction(String text) {
		super(text);
		this.catagory = Accounter.constants().vat();
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().fileVAT();
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				FileVATView view;
				view = new FileVATView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, FileVatAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	// @Override
	// public String getImageUrl() {
	// return "/images/File_vat.png";
	// }

	@Override
	public String getHistoryToken() {
		return "fileVAT";
	}

}
