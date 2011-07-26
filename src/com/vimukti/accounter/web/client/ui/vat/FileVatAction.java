package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class FileVatAction extends Action {

	public FileVatAction(String text) {
		super(text);
		this.catagory = Accounter.getVATMessages().VAT();
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
	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			private FileVATView view;

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed To Load Account", t);
			}

			public void onCreated() {
				try {

					view = new FileVATView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, FileVatAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);

				}
			}
		});

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public String getImageUrl() {
		return "/images/File_vat.png";
	}

	@Override
	public String getHistoryToken() {
		return "fileVAT";
	}

}
