package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.countries.UnitedKingdom;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class FileTAXAction extends Action {

	public FileTAXAction() {
		super();
		this.catagory = messages.tax();
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

			public void onSuccess() {
				ICountryPreferences countryPreferences = Accounter.getCompany()
						.getCountryPreferences();
				AbstractFileTAXView view;
				if (countryPreferences instanceof UnitedKingdom
						&& Accounter.getCompany().getCountryPreferences()
								.isVatAvailable()) {
					view = new FileVATView();
				} else {
					view = new FileTAXView();
				}

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, FileTAXAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//				
//			}
//		});

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
		return messages.fileTaxHistoryToken();
	}

	@Override
	public String getHelpToken() {
		return messages.filetaxHelp();
	}

	@Override
	public String getText() {
		return messages.fileTAX();
	}

}
