package com.vimukti.accounter.web.client.imports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.customers.UploadCSVFileDialog;

public class UploadCSVFileDialogAction extends Action {

	public UploadCSVFileDialogAction() {
		this.catagory = messages.importFile();
	}

	@Override
	public String getText() {
		return messages.uploadCsvFileDialog();
	}

	@Override
	public void run() {
		runAsync(isDependent, data);
	}

	private void runAsync(final boolean isDependent, final Object data) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				UploadCSVFileDialog dialog = new UploadCSVFileDialog();
				dialog.center();
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "import";
	}

	@Override
	public String getHelpToken() {
		return "import";
	}

}
