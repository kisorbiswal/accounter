package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class FileVatAction extends Action {

	public FileVatAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getVATMessages().VAT();
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().fileVAT();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
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
	
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/File_vat.png";
	}

}
