package com.vimukti.accounter.web.client.imports;

import java.util.List;
import java.util.Map;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class UploadCSVFileDialogAction extends Action {

	private Map<String, List<String>> columnData;
	private int importType;

	public UploadCSVFileDialogAction(Map<String, List<String>> data, int type) {
		this.columnData = data;
		this.importType = type;
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
				ImportView view = new ImportView(importType, columnData);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, UploadCSVFileDialogAction.this);
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
		return "uploadCSVFileDialog";
	}

	@Override
	public String getHelpToken() {
		return "Upload-CSV-file-dialog";
	}

}
